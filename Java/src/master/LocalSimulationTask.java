package master;

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import com.csvreader.CsvWriter;

public class LocalSimulationTask extends TimerTask 
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
	private String csvFile;
	private String globalCSVFile = "";
	private Logger logger;
	private ArrayList<Interaction> regularInteractions; 
	private ArrayList<Interaction> crossingInteractions;
	private ArrayList<Interaction> interactionBetweenPeers;
//	private ArrayList<Movement> avatarMovementData;
	private HashMap<Integer, ArrayList<Movement>>avatarMovementDataInRounds;
//	private ArrayList<Movement> objectMovementData;
	private HashMap<Integer, ArrayList<Movement>>objectMovementDataInRounds;
	private ArrayList<Peer> oldAssignment;
	private ArrayList<String> processedInteractionFile;
	private int nonRegularInteractionFactor = 20; 
	private long executionTime = 0;
	private int communicationCost = 0;
	private int overloadedPeers = 0;
	private int reassignments = 0;
	private int metisExecutionDelay = 0;
	private int lastRun = 0;
	private int executionRoundNumber = 0;
	private int interactionRoundDelay = 0;
	private int localityFactor = 0;
	private int interactionNumber = 0;
	private int interactionRoundNumber = 1;
	private int lastInteractionRoundIncrease = 0;
	private int avatarWeight = 1;
	private int objectWeight = 1;
	private int aoiSize = 20;
	private int interestChance = 20;
	private int avatarNumber = 0;
	private int objectNumber = 0;
	private float distributionFactor = 0;
	private float idealDistributionWeight = 0;
	private double auraEdgesFactor = 1;
	private double interestedInFactor = 1;
	private boolean useExistingInterestedInFile = true;
	private boolean useExistingInteractionFile = true;
	private ArrayList<Avatar> avatarList;
	private ArrayList<MMVEObject> objectList;
	
	private CsvWriter csvWriter;
	private CsvWriter globalCSVWriter;
	private Timer timer;
	private static final String csvFilePath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\CSV\\";
	
	
	public LocalSimulationTask(int periodNumber, String avatarFile, String objectFile, String interestedInFile, String logFile, String interactionFile, String csvFile, int peerNumber, int metisExecutionDelay, 
			int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			int interestChance, double auraEdgesFactor, double interestedInFactor, boolean useExistingInterestedInFile, boolean useExistingInteractionFile, Timer timer)
	{
		super();
		MMVEObject.counter = 0;
		Peer.counter = 0;
		numberofIterations = periodNumber;
		this.peerNumber = peerNumber;
		this.metisExecutionDelay = metisExecutionDelay;
		if(this.metisExecutionDelay == 0)
			this.metisExecutionDelay++;
		this.avatarFile = avatarFile;
		this.objectFile = objectFile;
		this.interestedInFile = interestedInFile;
		this.logFile = logFile;
		this.regularInteractions = new ArrayList<Interaction>();
		this.crossingInteractions = new ArrayList<Interaction>();
		this.interactionBetweenPeers = new ArrayList<Interaction>();
		this.logger = new Logger(logFile);
		this.csvFile = csvFile;
		this.csvWriter = new CsvWriter(csvFilePath + this.csvFile);
		this.interactionRoundDelay = interactionRoundDelay;
		this.interactionNumber = interactionNumber;
		this.localityFactor = localityFactor;
		this.timer = timer;
		this.avatarWeight = avatarWeight;
		this.objectWeight = objectWeight;
		this.aoiSize = aoiSize;
		this.interestChance = interestChance;
		this.auraEdgesFactor = auraEdgesFactor;
		this.interestedInFactor = interestedInFactor;
		this.useExistingInterestedInFile = useExistingInterestedInFile;
		this.useExistingInteractionFile = useExistingInteractionFile;
		avatarList = MobiSim.getAvatarsAndInitialPositions(avatarFile);
		this.avatarNumber = avatarList.size();
		//set the weight of all avatars to 1:
		avatarList = Util.setAvatarListWeight(avatarList, avatarWeight);
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, aoiSize);
		//get initial positions for the objects
		objectList = MobiSim.getObjectsAndInitialPositions(objectFile);
		this.objectNumber = objectList.size();
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, objectWeight);
		
		//create interestGraph
		interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//add avatars and objects
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		//add interested in
		if(Randomizer.testIfInterestedInFileExists(this.interestedInFile) && this.useExistingInterestedInFile)
		{
			interestGraph = Randomizer.readInterestedInInfoFromFile(interestGraph, interestedInFile);
		}
		else
		{
			interestGraph = Randomizer.addRandomInterestRelationshipToObjectGraph(interestGraph, interestChance);
			Randomizer.saveInterestedInToFile(interestGraph, this.interestedInFile);
		}
		
		//create edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph, auraEdgesFactor, interestedInFactor);
		//created crossed edges and peers
		crossedEdges = new ArrayList<DefaultWeightedEdge>();
		peers = Util.getStandardPeerList(this.peerNumber);
		oldAssignment = new ArrayList<Peer>();
		
		if(InteractionSimulator.doesInteractionFileExist(interactionFile) && this.useExistingInteractionFile)
		{
			this.interactionFile = interactionFile;
		}
		else
		{
			
			//the total round number is calculated by dividing the period number through interactionRoundDelay
			//and rounding the result up, Math.ceil takes care of this, but requires some variable type magic
			int totalRoundNumber = (int)Math.ceil((double)periodNumber /(double)interactionRoundDelay);
			InteractionSimulator.generateMultiRoundComplexInterestFile(interestGraph, interactionNumber, interactionFile, localityFactor, totalRoundNumber);
			this.interactionFile = interactionFile;
		}
		
		//process movement files for both objects and avatars
//		avatarMovementData = MobiSim.processMovementFile(avatarFile);
		avatarMovementDataInRounds = MobiSim.processMovementFileToHashMap(avatarFile);
//		objectMovementData = MobiSim.processMovementFile(objectFile);
		objectMovementDataInRounds = MobiSim.processMovementFileToHashMap(objectFile);
		//process interactionFile
		processedInteractionFile = InteractionSimulator.readInteractionFile(interactionFile);
		
	}
	
	public LocalSimulationTask(int periodNumber, String avatarFile, String objectFile, String interestedInFile, String logFile, String interactionFile, String csvFile, int peerNumber, int metisExecutionDelay, 
			int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			int interestChance, double auraEdgesFactor, double interestedInFactor, boolean useExistingInterestedInFile, boolean useExistingInteractionFile, String globalCSVFile, Timer timer)
	{
		//calls the default constructor 
		this(periodNumber,  avatarFile,  objectFile,  interestedInFile,  logFile,  interactionFile,  csvFile,  
				peerNumber, metisExecutionDelay, interactionNumber,  localityFactor,  interactionRoundDelay,  
				avatarWeight,  objectWeight,  aoiSize, interestChance,  auraEdgesFactor,  interestedInFactor, useExistingInterestedInFile, useExistingInteractionFile, timer);
		this.globalCSVFile = globalCSVFile;
		File f = new File(csvFilePath + this.globalCSVFile);
		
		try
		{
			if(!f.exists())
			{
				f.createNewFile();
			}
			char delimiter = ',';
			globalCSVWriter = new CsvWriter(new FileOutputStream(f, true),delimiter, Charset.forName("ISO-8859-1"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			csvWriter.close();
			GNUPlot.createGraph(csvFile, getGraphName(csvFile));
			this.cancel();
			//remove the timer from timers so that another one may run
			LocalSimulation.timers.remove(timer);
			timer.cancel();
			
		}
		else
		{
			//using a dynamic file name here because otherwise all active simulations use the same one
			//=> trouble, we remove the directory here because the MetisInOut methods use a different base directory
			String FileName = getGraphNameWithoutDirectory(csvFile)+ ".graph";
			boolean recalculateInteractions = areInteractionsStable();
			if(recalculateInteractions == false)
			{
				lastInteractionRoundIncrease = counter;
				interactionRoundNumber++;
				System.out.println("Interaction round increased");
				logger.newInteractions();
			}
			if(firstRun)
			{
				Stopwatch timer = new Stopwatch();
				timer.start();
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
				simulateInteraction(interactionRoundNumber);
				interactionBetweenPeers = determineInteractionBetweenPeers();
				overloadedPeers = determineOverloadedPeers();
				communicationCost = calculateCommunicationCost();
				timer.stop();
				executionTime = calculateExecutionCost(timer.getElapsedTime());
				idealDistributionWeight = calculateIdealDistributionWeight();
				writeCSVHeader();
				if(globalCSVFile!="")
				{
					writeGlobalCSVHeader();
				}
				//I commented this out because the first round is shorter than the others, so this may fuck up
				//my results
//				executionRoundNumber++;
//				writeCSVData();
				oldAssignment = Util.copyPeerList(peers);
				lastRun = counter;
				firstRun = false;
				
			}
			else
			{
				Stopwatch timer = new Stopwatch();
				timer.start();
				//get current partition list from peers:
				logger.newRound(counter);
				//check whether it is time to run metis again
				if(counter == (lastRun + metisExecutionDelay))
				{
					ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = InterestGraphConstruction.getPartitionsFromPeerList(peers);
					interestGraph = InterestGraphConstruction.constructObjectInterestGraphFromPartitions(partitions);
					//everybody moves
					executeMovementForCurrentTime(interestGraph);
					logger.printNewSection();
					//interaction goes here
					//get edges
					interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph, auraEdgesFactor, interestedInFactor);
					//add information from previous interactions
					interestGraph = InterestGraphConstruction.constructInterestGraphEdgesFromPastInteractions(interestGraph, regularInteractions, crossingInteractions, 0.5);
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
					
//					if(recalculateInteractions == false)
//					{
//						simulateInteraction(interactionRoundNumber);
//					}
//					else
//					{
//						//recalculate the interactions
//						InteractionSimulator.generateComplexInteractionFile(interestGraph, interactionNumber, interactionFile, localityFactor);
//						System.out.println("New interactions were calculated");
//						logger.newInteractions();
//						simulateInteraction(interactionRoundNumber);
//						
//					}
					simulateInteraction(interactionRoundNumber);
					logger.printNewSection();
					overloadedPeers = determineOverloadedPeers();
					communicationCost = calculateCommunicationCost();
					reassignments = determineReassignments();
					distributionFactor = generateDistributionFactor();
					lastRun = counter;
					timer.stop();
					executionTime = calculateExecutionCost(timer.getElapsedTime());
					timer.reset();
					executionRoundNumber++;
					writeCSVData();
					if(globalCSVFile!="")
					{
						writeGlobalCSVData();
					}
					//for debug
//					if(executionRoundNumber == 7 ||executionRoundNumber == 8 || executionRoundNumber == 9 || executionRoundNumber == 10 )
//					{
//						debugForCrowdingTest();
//					}
				}
				else
				{
					//we have to perform the movement and the interactions here to keep everything consistent, even though I am not sure about the interactions
					//we do not write the information into the .csv file, because we don't actually do anything with it.
					//this won't work with the zonal approach, because there the algorithm triggers as soon as one object moves into another server's territory
					//hence we will have to do this differently there
					executeMovementForCurrentTime(interestGraph);
					simulateInteraction(interactionRoundNumber);
//					if(recalculateInteractions == false)
//					{
//						System.out.println("Interactions are stable!");
//						logger.stableInteractions();
//						simulateInteraction(interactionRoundNumber);
//					}
//					else
//					{
//						//recalculate the interactions
////						InteractionSimulator.generateComplexInteractionFile(interestGraph, interactionNumber, interactionFile, localityFactor);
////						System.out.println("New interactions were calculated");
//						System.out.println("Interaction round increased");
//						logger.newInteractions();
//						lastInteractionRoundIncrease = counter;
//						interactionRoundNumber++;
//						simulateInteraction(interactionRoundNumber);
//					}
					timer.stop();
					timer.reset();
					logger.noExecution();
				}
				
				
			}
			
		}
		
				
	}

	private float calculateIdealDistributionWeight() 
	{
		float idealWeight = Util.calculateGraphWeigth(interestGraph) / peerNumber;
		return idealWeight;
	}

	private float generateDistributionFactor()
	{
		float result = 0;
		Iterator<Peer> itPeers = peers.iterator();
		while(itPeers.hasNext())
		{
			Peer peerToProcess = itPeers.next();
			//we iterate through all peers and calculate the absolute value of the object weight they have to manage
			// and the ideal weight each peer should carry. Then we sum these values to measure the quality of the
			//distribution. In an ideal case, it should be zero
			result = result + Math.abs(PartitionAssignment.getObjectPartitionWeight(peerToProcess.getManagedObjects()) - idealDistributionWeight);
		}
		result = result / Util.calculateGraphWeigth(interestGraph);
		//round it up to two decimal values
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		String temp = df.format(result);
		result = Float.parseFloat(temp);
		if(result > 1)
			result = 1;
		return result;
	}

	//purpose of the function is to determine whether or not the interactions have to be recalculated
	private boolean areInteractionsStable() 
	{
//		//at first check for dumb stability values
//		if(stabilityFactor < 0)
//			stabilityFactor = 0;
//		
//		if(stabilityFactor > 100)
//			stabilityFactor = 100;
//		
//		if(stabilityFactor == 0)
//			return false;
//		
//		if(stabilityFactor == 100)
//			return true;
//		
//		Random randomGenerator = new Random();
//		//possible values: 0-100
//		int probability = randomGenerator.nextInt(101);
//		//calculate whether or not stability is preserved
//		if(stabilityFactor <= probability)
//		{
//			//current solution is stable
//			return true;
//		}
//		else
//		{
//			//current solution is unstable
//			return false;
//		}
		
		if(counter == (lastInteractionRoundIncrease + interactionRoundDelay))
		{
			return false;
		}
		else
		{
			return true;
		}
		
		
	}

	private ArrayList<Interaction> determineInteractionBetweenPeers() 
	{
		ArrayList<Interaction> result = new ArrayList<Interaction>();
		Iterator<Interaction> itInteractions = regularInteractions.iterator();
		while(itInteractions.hasNext())
		{
			Interaction interactionToProcess = itInteractions.next();
			if(!Util.objectsInSamePartition(peers, interactionToProcess.getSource(), interactionToProcess.getTarget()))
			{
				result.add(interactionToProcess);
			}
		}
		
		Iterator<Interaction> itCrossing = crossingInteractions.iterator();
		while(itCrossing.hasNext())
		{
			Interaction interactionToProcess = itCrossing.next();
			if(!Util.objectsInSamePartition(peers, interactionToProcess.getSource(), interactionToProcess.getTarget()))
			{
				result.add(interactionToProcess);
			}
		}
		
		return result;
	}

	private int determineReassignments() 
	{
		Iterator<Peer> itCurrent = peers.iterator();
		int movingObjects = 0;
		while(itCurrent.hasNext())
		{
			Peer peerToCheck = itCurrent.next();
			//compare this to same peer from last period
			Peer oldPeer = Util.getPeerWithCopyID(oldAssignment, peerToCheck.getID());
			if(oldPeer != null)
			{			
				Iterator<MMVEObject> itCurrentPartition = peerToCheck.getManagedObjects().vertexSet().iterator();
				while(itCurrentPartition.hasNext())
				{
					MMVEObject objectToCheck = itCurrentPartition.next();
					if(!oldPeer.getManagedObjects().containsVertex(objectToCheck))
					{
						//this object is new so it has moved to us
						movingObjects++;
					}
				}
				
				Iterator<MMVEObject> itOldPartition = oldPeer.getManagedObjects().vertexSet().iterator();
				while(itOldPartition.hasNext())
				{
					MMVEObject objectToCheck = itOldPartition.next();
					if(!peerToCheck.getManagedObjects().containsVertex(objectToCheck))
					{
						//this object is not part of our partition anymore, so it is gone
						movingObjects++;
					}
				}
				
			}
		}
		oldAssignment = Util.copyPeerList(peers);
		//returns movingObjects/2 because with the current implementation movements are counted twice 
		//this is the most easy fix for this problem
		return movingObjects/2;
	}

	private void writeCSVData() 
	{
		try
		{
						
			String[] row = new String[7];
			row[0] = String.valueOf(executionRoundNumber);
			row[1] = String.valueOf(interactionBetweenPeers.size());
			row[2] = String.valueOf(executionTime);
			row[3] = String.valueOf(communicationCost);
			row[4] = String.valueOf(reassignments);
			row[5] = String.valueOf(overloadedPeers);
			row[6] = String.valueOf(distributionFactor);
			csvWriter.writeRecord(row);
			csvWriter.flush();			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	private void writeGlobalCSVData() 
	{
		try
		{
						
			String[] row = new String[7];
			row[0] = String.valueOf(executionRoundNumber);
			row[1] = String.valueOf(interactionBetweenPeers.size());
			row[2] = String.valueOf(executionTime);
			row[3] = String.valueOf(communicationCost);
			row[4] = String.valueOf(reassignments);
			row[5] = String.valueOf(overloadedPeers);
			row[6] = String.valueOf(distributionFactor);
			globalCSVWriter.writeRecord(row);
			globalCSVWriter.flush();			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	private void writeCSVHeader()
	{
		try
		{
			csvWriter.writeComment("employed config for this test:");
			csvWriter.writeComment("avatarFile: " + avatarFile);
			csvWriter.writeComment("number of avatars: " + avatarNumber);
			csvWriter.writeComment("objectFile: " + objectFile);
			csvWriter.writeComment("number of objects: " + objectNumber);
			csvWriter.writeComment("interestedInFile: " + interestedInFile);
			csvWriter.writeComment("interactionFile: " + interactionFile);
			csvWriter.writeComment("logFile: " + logFile);
			csvWriter.writeComment("peerNumber: " + peerNumber);
			csvWriter.writeComment("number of interactions: " + interactionNumber);
			csvWriter.writeComment("locality factor: " + localityFactor);
			csvWriter.writeComment("interaction round delay: " + interactionRoundDelay);
			csvWriter.writeComment("avatar weight: " + avatarWeight);
			csvWriter.writeComment("object weight: " + objectWeight);
			csvWriter.writeComment("AoI size: " + aoiSize);
			csvWriter.writeComment("interest chance: " + interestChance);
			csvWriter.writeComment("aura factor: " + auraEdgesFactor);
			csvWriter.writeComment("interested in factor: " + interestedInFactor);
			String[] header = new String[7];
			header[0] = "round";
			header[1] = "interactions between peers";
			header[2] = "execution cost (ms)";
			header[3] = "communication cost";
			header[4] = "reassignments";
			header[5] = "overloaded peers";
			header[6] = "distribution factor";
			csvWriter.writeRecord(header);
			csvWriter.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeGlobalCSVHeader()
	{
		try
		{
			globalCSVWriter.writeComment("A new round has begun");
			globalCSVWriter.writeComment("employed config for this test:");
			globalCSVWriter.writeComment("avatarFile: " + avatarFile);
			globalCSVWriter.writeComment("number of avatars: " + avatarNumber);
			globalCSVWriter.writeComment("objectFile: " + objectFile);
			globalCSVWriter.writeComment("number of objects: " + objectNumber);
			globalCSVWriter.writeComment("interestedInFile: " + interestedInFile);
			globalCSVWriter.writeComment("interactionFile: " + interactionFile);
			globalCSVWriter.writeComment("logFile: " + logFile);
			globalCSVWriter.writeComment("peerNumber: " + peerNumber);
			globalCSVWriter.writeComment("number of interactions: " + interactionNumber);
			globalCSVWriter.writeComment("locality factor: " + localityFactor);
			globalCSVWriter.writeComment("interaction round delay: " + interactionRoundDelay);
			globalCSVWriter.writeComment("avatar weight: " + avatarWeight);
			globalCSVWriter.writeComment("object weight: " + objectWeight);
			globalCSVWriter.writeComment("AoI size: " + aoiSize);
			globalCSVWriter.writeComment("interest chance: " + interestChance);
			globalCSVWriter.writeComment("aura factor: " + auraEdgesFactor);
			globalCSVWriter.writeComment("interested in factor: " + interestedInFactor);
			String[] header = new String[7];
			header[0] = "round";
			header[1] = "interactions between peers";
			header[2] = "execution cost (ms)";
			header[3] = "communication cost";
			header[4] = "reassignments";
			header[5] = "overloaded peers";
			header[6] = "distribution factor";
			globalCSVWriter.writeRecord(header);
			globalCSVWriter.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private int calculateCommunicationCost() 
	{
		//we assume that the peers have to send their interactions and their positions to the coordinator
		//and receive the objects they have to manage from the coordinator.
		//we further assume that each message gets acked with another message
		//hence a total of 4 messages is needed for each peer (movement and interactions are sent in the same message)
		int cost = peers.size()*4;
		
		return cost;
	}

	private int determineOverloadedPeers() 
	{
		Iterator<Peer> itPeers = peers.iterator();
		int overloadedPeers = 0;
		while(itPeers.hasNext())
		{
			Peer p = itPeers.next();
			if(p.isOverloaded())
				overloadedPeers++;
		}
		
		return overloadedPeers;
	}

	private void simulateInteraction(int currentRound) 
	{
		//empty both interaction list, because this a new round
		regularInteractions.clear();
		crossingInteractions.clear();
		Iterator<String> itInteractions = processedInteractionFile.iterator();
		while(itInteractions.hasNext())
		{
			String[] content = itInteractions.next().split(" ");
			if(currentRound == Integer.parseInt(content[0]))
			{
				MMVEObject interactingObject = Util.getObjectWithID(interestGraph, Integer.parseInt(content[1]));
				MMVEObject objectToInterActWith = Util.getObjectWithID(interestGraph, Integer.parseInt(content[2]));
				
				if(interactingObject!=null && objectToInterActWith != null)
				{
					if(interestGraph.containsEdge(interactingObject, objectToInterActWith))
					{
						regularInteractions.add(new Interaction(interactingObject, objectToInterActWith));
						logger.logInteraction(interactingObject, objectToInterActWith, true);
					}
					else
					{
						crossingInteractions.add(new Interaction(interactingObject, objectToInterActWith));
						logger.logInteraction(interactingObject, objectToInterActWith, false);
					}
				}
				else
				{
					logger.getLogFileWriter().println("Can't execute interaction, objects not found!");
				}
			}
			
		}
		
		
	}

	private void executeMovementForCurrentTime(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph) 
	{
//		Iterator<MMVEObject> itObjects = graph.vertexSet().iterator();
//		if(logger.isReady())
//			logger.getLogFileWriter().println("Executing Moving");
//		ArrayList<Movement> avatarMovementDataToDelete = new ArrayList<Movement>();
//		ArrayList<Movement> objectMovementDataToDelete = new ArrayList<Movement>();
//		while(itObjects.hasNext())
//		{
//			MMVEObject objectToWorkWith = itObjects.next();
//			if(objectToWorkWith instanceof Avatar)
//			{
//				Point oldPosition = objectToWorkWith.getPosition();
//				Movement movementOfThisObject = MobiSim.getAvatarOrObjectMovement(avatarMovementData, objectToWorkWith.getMobiSimID(), counter);
//				objectToWorkWith.setPosition(movementOfThisObject.getNewPosition());
//				logger.objectMovement(oldPosition, objectToWorkWith);
//				avatarMovementDataToDelete.add(movementOfThisObject);
//			}
//			else
//			{
//				Point oldPosition = objectToWorkWith.getPosition();
//				Movement movementOfThisObject = MobiSim.getAvatarOrObjectMovement(objectMovementData, objectToWorkWith.getMobiSimID(), counter);
//				objectToWorkWith.setPosition(movementOfThisObject.getNewPosition());
//				logger.objectMovement(oldPosition, objectToWorkWith);
//				objectMovementDataToDelete.add(movementOfThisObject);
//			}
//		}
//		
//		avatarMovementData.removeAll(avatarMovementDataToDelete);
//		objectMovementData.removeAll(objectMovementDataToDelete);
		
		Iterator<MMVEObject> itObjects = graph.vertexSet().iterator();
		if(logger.isReady())
			logger.getLogFileWriter().println("Executing Moving");
		
		while(itObjects.hasNext())
		{
			MMVEObject objectToWorkWith = itObjects.next();
			if(objectToWorkWith instanceof Avatar)
			{
				Point oldPosition = objectToWorkWith.getPosition();
				Movement movementOfThisObject = MobiSim.getAvatarOrObjectMovement(avatarMovementDataInRounds.get(counter), objectToWorkWith.getMobiSimID(), counter);
				objectToWorkWith.setPosition(movementOfThisObject.getNewPosition());
				logger.objectMovement(oldPosition, objectToWorkWith);
				
			}
			else
			{
				Point oldPosition = objectToWorkWith.getPosition();
				Movement movementOfThisObject = MobiSim.getAvatarOrObjectMovement(objectMovementDataInRounds.get(counter), objectToWorkWith.getMobiSimID(), counter);
				objectToWorkWith.setPosition(movementOfThisObject.getNewPosition());
				logger.objectMovement(oldPosition, objectToWorkWith);
				
			}
		}
		
		
		
		
		
	}
		
	
	
	private void debugForCrowdingTest()
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter("D:\\crowdingDebug.txt", true));
			Iterator<Peer> itPeers = peers.iterator();
			while(itPeers.hasNext())
			{
				Peer peerToProcess = itPeers.next();
				out.println("peer " + peerToProcess.getID() + " manages " + 
						peerToProcess.getManagedObjects().vertexSet().size() + " objects");
				out.println("It's avatar is located at position: " + peerToProcess.getOwnAvatar().getPosition());
				Avatar avatarToProcess = peerToProcess.getOwnAvatar();
				Iterator<DefaultWeightedEdge> itEdgesOfAvatar = interestGraph.edgesOf(avatarToProcess).iterator();
				String connectedTo = "";
				while(itEdgesOfAvatar.hasNext())
				{
					DefaultWeightedEdge edgeToProcess = itEdgesOfAvatar.next();
					if(interestGraph.getEdgeSource(edgeToProcess).equals(avatarToProcess))
					{
						connectedTo = connectedTo + " " + interestGraph.getEdgeTarget(edgeToProcess).getID();
					}
				}
				out.println("Its aura encompasses the following objects: " + connectedTo);
				out.println("It contains " + peerToProcess.getManagedObjects().edgeSet().size() + " edges");
			}
			out.flush();
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private long calculateExecutionCost(long elapsedTime)
	{
		return elapsedTime;
	}
	
	private String getGraphName(String csvFile)
	{
		String graph = csvFile.substring(0, (csvFile.indexOf(".csv")));
		return graph;
	}
	
	private String getGraphNameWithoutDirectory(String csvFile)
	{
		String graph = csvFile.substring((csvFile.indexOf("\\"))+1, (csvFile.indexOf(".csv")));
		return graph;
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

	public int getNonRegularInteractionFactor() {
		return nonRegularInteractionFactor;
	}

	public void setNonRegularInteractionFactor(int nonRegularInteractionFactor) {
		this.nonRegularInteractionFactor = nonRegularInteractionFactor;
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

	public ArrayList<Interaction> getInteractions() {
		return regularInteractions;
	}

	public void setInteractions(ArrayList<Interaction> interactions) {
		this.regularInteractions = interactions;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public int getCommunicationCost() {
		return communicationCost;
	}

	public void setCommunicationCost(int communicationCost) {
		this.communicationCost = communicationCost;
	}

	public int getOverloadedPeers() {
		return overloadedPeers;
	}

	public void setOverloadedPeers(int overloadedPeers) {
		this.overloadedPeers = overloadedPeers;
	}

	public String getCsvFilePath() {
		return csvFilePath;
	}

	public String getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

	public static String getCsvfilepath() {
		return csvFilePath;
	}

	public int getMetisExecutionDelay() {
		return metisExecutionDelay;
	}

	public void setMetisExecutionDelay(int metisExecutionDelay) {
		this.metisExecutionDelay = metisExecutionDelay;
	}

	public int getInteractionRoundDelay() {
		return interactionRoundDelay;
	}

	public void setInteractionRoundDelay(int stabilityFactor) {
		this.interactionRoundDelay = stabilityFactor;
	}

	public int getLocalityFactor() {
		return localityFactor;
	}

	public void setLocalityFactor(int localityFactor) {
		this.localityFactor = localityFactor;
	}

	public int getInteractionNumber() {
		return interactionNumber;
	}

	public void setInteractionNumber(int interactionNumber) {
		this.interactionNumber = interactionNumber;
	}

	public int getAvatarWeight() {
		return avatarWeight;
	}

	public void setAvatarWeight(int avatarWeight) {
		this.avatarWeight = avatarWeight;
	}

	public int getObjectWeight() {
		return objectWeight;
	}

	public void setObjectWeight(int objectWeight) {
		this.objectWeight = objectWeight;
	}

	public int getAoiSize() {
		return aoiSize;
	}

	public void setAoiSize(int aoiSize) {
		this.aoiSize = aoiSize;
	}

	public int getInterestChance() {
		return interestChance;
	}

	public void setInterestChance(int interestChance) {
		this.interestChance = interestChance;
	}

	public double getAuraEdgesFactor() {
		return auraEdgesFactor;
	}

	public void setAuraEdgesFactor(double auraEdgesFactor) {
		this.auraEdgesFactor = auraEdgesFactor;
	}

	public double getInterestedInFactor() {
		return interestedInFactor;
	}

	public void setInterestedInFactor(double interestedInFactor) {
		this.interestedInFactor = interestedInFactor;
	}

	public boolean isUseExistingInterestedInFile() {
		return useExistingInterestedInFile;
	}

	public void setUseExistingInterestedInFile(boolean useExistingInterestedInFile) {
		this.useExistingInterestedInFile = useExistingInterestedInFile;
	}

	public boolean isUseExistingInteractionFile() {
		return useExistingInteractionFile;
	}

	public void setUseExistingInteractionFile(boolean useExistingInteractionFile) {
		this.useExistingInteractionFile = useExistingInteractionFile;
	}
}
