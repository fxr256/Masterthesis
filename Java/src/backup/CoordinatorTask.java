package backup;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

import master.Avatar;
import master.InteractionSimulator;
import master.InterestGraphConstruction;
import master.Logger;
import master.MMVEObject;
import master.MetisInOut;
import master.MobiSim;
import master.PartitionAssignment;
import master.Peer;
import master.Randomizer;
import master.Util;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class CoordinatorTask extends TimerTask 
{
	
	private ArrayList<Peer> peers;
	private DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph;
	private ArrayList<DefaultWeightedEdge> crossedEdges;
	private int numberofIterations = 0;
	private int counter = 0;
	private int peerNumber = 0;
	private boolean firstRun = true;
	private String avatarFile;
	private String objectFile;
	private String interestedInFile;
	private String logFile;
	private String interactionFile;
	private Logger logger;
	private boolean done = false;
	private Coordinator owningCoordinator; 

	
	public CoordinatorTask(int periodNumber, String avatarFile, String objectFile, String interestedInFile, String logFile, String interactionFile, 
			Coordinator coordinator)
	{
		super();
		MMVEObject.counter = 0;
		Peer.counter = 0;
		numberofIterations = periodNumber;
		this.avatarFile = avatarFile;
		this.objectFile = objectFile;
		this.interestedInFile = interestedInFile;
		this.logFile = logFile;
		this.owningCoordinator = coordinator;
		
		this.logger = new Logger(logFile);
		ArrayList<Avatar> avatarList = MobiSim.getAvatarsAndInitialPositions(avatarFile);
		//set the weight of all avatars to 1:
		avatarList = Util.setAvatarListWeight(avatarList, 1);
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, 20);
		//get initial positions for the objects
		ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(objectFile);
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, 1);
		//create interestGraph
		interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//add avatars and objects
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		//add interested in
		interestGraph = Randomizer.readInterestedInInfoFromFile(interestGraph, interestedInFile);
		//create edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph, 1, 1);
		//created crossed edges and peers
		crossedEdges = new ArrayList<DefaultWeightedEdge>();
		peers = new ArrayList<Peer>();
		
		if(InteractionSimulator.doesInteractionFileExist(interactionFile))
		{
			this.interactionFile = interactionFile;
		}
		else
		{
			InteractionSimulator.generateInteractionFile(interestGraph, interestGraph.vertexSet().size(), interactionFile);
			this.interactionFile = interactionFile;
		}
		
	}
	
	@Override
	public void run()
	{
		
		logger.printNewRound();
		counter++;
		if(counter > numberofIterations)
		{
			//we are done simulating so we stop the execution
			logger.doneLogging();
			done = true;
			this.owningCoordinator.shutDownClients();
			this.owningCoordinator.shutDown();
			this.cancel();
		}
		else
		{
			String FileName = "taskObjectGraph.graph";
			if(firstRun)
			{
				//added to intialize peer number, because we are not using
				//standard peers anymore, but the peers associated to
				//simulation client. The fixed number is used to prevent
				//reduction of the peer number due to fewer partitions than expected
				peerNumber = peers.size();
				//no movement here because first iteration
				//do partitioning
				logger.simulationStarted(numberofIterations);
				logger.analyzeGraph(interestGraph);
				logger.printNewSection();
				MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
				ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
					MetisInOut.executeObjectPartition(FileName, peerNumber, interestGraph);
				//get crossed edges
				crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
				peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
				//assign partitions to peers:
				peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
				//refine it
				peers = PartitionAssignment.refinePartitions(partitions, peers, true);
				crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(Util.constructObjectGraphFromPeerList(peers), interestGraph);
				logger.analyzePeerList(peers);
				logger.printNewSection();
				logger.analyzeCrossedEdges(crossedEdges, interestGraph);
				logger.printNewSection();
				simulateInteraction();
				owningCoordinator.propagateUpdates();
				firstRun = false;
			}
			else
			{
				//get current partition list from peers:
				logger.newRound(counter);
				ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = InterestGraphConstruction.getPartitionsFromPeerList(peers);
				interestGraph = InterestGraphConstruction.constructObjectInterestGraphFromPartitions(partitions);
				//everybody moves
				executeMovementForCurrentTime(interestGraph);
				logger.printNewSection();
				//interaction goes here
				//get edges
				interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
				//do partitioning
				logger.analyzeGraph(interestGraph);
				logger.printNewSection();
				MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
				partitions = MetisInOut.executeObjectPartition(FileName, peerNumber, interestGraph);
				//get crossed edges
				crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
				peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
				//assign partitions to peers:
				peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
				//refine it
				peers = PartitionAssignment.refinePartitions(partitions, peers, true);
				crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(Util.constructObjectGraphFromPeerList(peers), interestGraph);
				logger.analyzePeerList(peers);
				logger.printNewSection();
				logger.analyzeCrossedEdges(crossedEdges, interestGraph);
				logger.printNewSection();
				simulateInteraction();
				owningCoordinator.propagateUpdates();
				logger.printNewSection();
			}
			
		}
		
	}
	
	private void simulateInteraction() 
	{
		Iterator<String> itInteractions = InteractionSimulator.readInteractionFile(interactionFile).iterator();
		while(itInteractions.hasNext())
		{
			String[] content = itInteractions.next().split(" ");
			MMVEObject interactingObject = Util.getObjectWithID(interestGraph, Integer.parseInt(content[0]));
			MMVEObject objectToInterActWith = Util.getObjectWithID(interestGraph, Integer.parseInt(content[1]));
			
			if(interactingObject!=null && objectToInterActWith != null)
			{
				if(interestGraph.containsEdge(interactingObject, objectToInterActWith))
				{
					logger.logInteraction(interactingObject, objectToInterActWith, true);
				}
				else
				{
					logger.logInteraction(interactingObject, objectToInterActWith, false);
				}
			}
			else
			{
				logger.getLogFileWriter().println("Can't execute interaction, objects not found!");
			}
		}
		
	}

	

	private void executeMovementForCurrentTime(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph) 
	{
		Iterator<MMVEObject> itObjects = graph.vertexSet().iterator();
		if(logger.isReady())
			logger.getLogFileWriter().println("Executing Moving");
		while(itObjects.hasNext())
		{
			MMVEObject objectToWorkWith = itObjects.next();
			if(objectToWorkWith instanceof Avatar)
			{
				Point oldPosition = objectToWorkWith.getPosition();
				objectToWorkWith.setPosition(MobiSim.getAvatarOrObjectPosition(avatarFile, objectToWorkWith.getMobiSimID(), counter));
				logger.objectMovement(oldPosition, objectToWorkWith);
			}
			else
			{
				Point oldPosition = objectToWorkWith.getPosition();
				objectToWorkWith.setPosition(MobiSim.getAvatarOrObjectPosition(objectFile, objectToWorkWith.getMobiSimID(), counter));
				logger.objectMovement(oldPosition, objectToWorkWith);
			}
		}
		
	}

	public ArrayList<Peer> getPeers() {
		return peers;
	}

	public void setPeers(ArrayList<Peer> peers) {
		this.peers = peers;
	}

	public DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> getInterestGraph() {
		return interestGraph;
	}

	public void setInterestGraph(
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph) {
		this.interestGraph = interestGraph;
	}

	public ArrayList<DefaultWeightedEdge> getCrossedEdges() {
		return crossedEdges;
	}

	public void setCrossedEdges(ArrayList<DefaultWeightedEdge> crossedEdges) {
		this.crossedEdges = crossedEdges;
	}

	public int getNumberofIterations() {
		return numberofIterations;
	}

	public void setNumberofIterations(int numberofIterations) {
		this.numberofIterations = numberofIterations;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getPeerNumber() {
		return peerNumber;
	}

	public void setPeerNumber(int peerNumber) {
		this.peerNumber = peerNumber;
	}

	public String getAvatarFile() {
		return avatarFile;
	}

	public void setAvatarFile(String avatarFile) {
		this.avatarFile = avatarFile;
	}

	public String getObjectFile() {
		return objectFile;
	}

	public void setObjectFile(String objectFile) {
		this.objectFile = objectFile;
	}

	public String getInterestedInFile() {
		return interestedInFile;
	}

	public void setInterestedInFile(String interestedInFile) {
		this.interestedInFile = interestedInFile;
	}

	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public String getInteractionFile() {
		return interactionFile;
	}

	public void setInteractionFile(String interactionFile) {
		this.interactionFile = interactionFile;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Coordinator getOwningCoordinator() {
		return owningCoordinator;
	}

	public void setOwningCoordinator(Coordinator owningCoordinator) {
		this.owningCoordinator = owningCoordinator;
	}
}
