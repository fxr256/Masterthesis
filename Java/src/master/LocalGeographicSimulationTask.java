package master;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.csvreader.CsvWriter;

public class LocalGeographicSimulationTask extends TimerTask
{

	private ArrayList<Peer> peers;
	private ArrayList<DefaultWeightedEdge> crossedEdges;
	private int numberofIterations = 0;
	private int counter = 0;
	private int peerNumber = 0;
	private boolean firstRun = true;
	private String avatarFile;
	private String objectFile;
	private String logFile;
	private String interactionFile;
	private String csvFile;
	private String globalCSVFile = "";
	private Logger logger;
	private ArrayList<Interaction> interactionList;
	private ArrayList<Interaction> interactionBetweenPeers;
//	private ArrayList<Movement> avatarMovementData;
//	private ArrayList<Movement> objectMovementData;	
	private HashMap<Integer, ArrayList<Movement>>avatarMovementDataInRounds;
	private HashMap<Integer, ArrayList<Movement>>objectMovementDataInRounds;
	private ArrayList<String> processedInteractionFile;
	private ArrayList<Avatar> avatarList;
	private ArrayList<MMVEObject> objectList;
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
	private int avatarNumber = 0;
	private int objectNumber = 0;
	private Rectangle gameWorld;
	private ArrayList<Region> regions;
	private boolean enforceMappingAvatarsToPeers = true;
	private int objectsCommunicatingBetweenRegions = 0;
	private boolean useExistingInteractionFile = true;
	private float distributionFactor = 0;
	private float idealDistributionWeight = 0;
	
	private CsvWriter csvWriter;
	private CsvWriter globalCSVWriter;
	private Timer timer;
	private static final String csvFilePath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\CSV\\";
	
	
	public LocalGeographicSimulationTask(int periodNumber, String avatarFile, String objectFile, String logFile, String interactionFile, String csvFile, 
			int peerNumber, int metisExecutionDelay, int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			boolean useExistingInteractionFile, Timer timer)
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
		this.logFile = logFile;
		this.interactionList = new ArrayList<Interaction>();
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
		this.useExistingInteractionFile = useExistingInteractionFile;
		avatarList = MobiSim.getAvatarsAndInitialPositions(avatarFile);
		this.avatarNumber = avatarList.size();
		avatarList = Util.setAvatarListWeight(avatarList, avatarWeight);
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, aoiSize);
		//get initial positions for the objects
		objectList = MobiSim.getObjectsAndInitialPositions(objectFile);
		this.objectNumber = objectList.size();
		objectList = Util.setObjectListWeight(objectList, objectWeight);
		//create the map. 500/500 is the default map size of mobisim
		this.gameWorld = new Rectangle(0, 0, 500, 500);
		regions = divideGameMapIntoRegions();
		placeObjectsInRegions();
		
		//created crossed edges and peers
		crossedEdges = new ArrayList<DefaultWeightedEdge>();
		peers = Util.getStandardPeerList(this.peerNumber);
		regions = RegionAssignment.assignRegionsToPeersRandomly(regions, peers);
		
		if(InteractionSimulator.doesInteractionFileExist(interactionFile) && this.useExistingInteractionFile)
		{
			this.interactionFile = interactionFile;
		}
		else
		{
			
			//the total round number is calculated by dividing the period number through interactionRoundDelay
			//and rounding the result up, Math.ceil takes care of this, but requires some variable type magic
			int totalRoundNumber = (int)Math.ceil((double)periodNumber /(double)interactionRoundDelay);
			InteractionSimulator.generateMultiRoundComplexInterestFile(avatarList, objectList, interactionNumber, interactionFile, localityFactor, totalRoundNumber);
			this.interactionFile = interactionFile;
		}
		
		//process movement files for both objects and avatars
//		avatarMovementData = MobiSim.processMovementFile(avatarFile);
//		objectMovementData = MobiSim.processMovementFile(objectFile);
		avatarMovementDataInRounds = MobiSim.processMovementFileToHashMap(avatarFile);
		objectMovementDataInRounds = MobiSim.processMovementFileToHashMap(objectFile);
		//process interactionFile
		processedInteractionFile = InteractionSimulator.readInteractionFile(interactionFile);
		
	}
	
	

	public LocalGeographicSimulationTask(int periodNumber, String avatarFile, String objectFile, String logFile, String interactionFile, String csvFile, int peerNumber, int metisExecutionDelay, 
			int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			boolean useExistingInteractionFile, String globalCSVFile, Timer timer)
	{
		//calls the default constructor 
		this(periodNumber,  avatarFile,  objectFile,  logFile,  interactionFile,  csvFile,  
				peerNumber, metisExecutionDelay, interactionNumber,  localityFactor,  interactionRoundDelay,  
				avatarWeight,  objectWeight,  aoiSize, useExistingInteractionFile,  timer);
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
				//no reassignments either because first round
				objectsCommunicatingBetweenRegions = determineInterRegionCommunication();
				//do partitioning
				logger.geographicSimulationStarted(numberofIterations);
				peers = Util.assignAvatarToPeerWithMatchingID(peers, avatarList);
//				commented out because it not really fits to geographic regionalisation
//				if(enforceMappingAvatarsToPeers)
//				{
//					regions = RegionAssignment.assignRegionsToPeersDependingOnOwnAvatar(regions, peers, true);
//				}
//				else
//				{
//					regions = RegionAssignment.assignRegionsToPeersRandomly(regions, peers);
//				}
				
				simulateInteraction(interactionRoundNumber);
				interactionBetweenPeers = determineInteractionBetweenPeers();
				overloadedPeers = determineOverloadedPeers();
				communicationCost = calculateCommunicationCost();
				idealDistributionWeight = calculateIdealDistributionWeight();
				timer.stop();
				executionTime = calculateExecutionCost(timer.getElapsedTime());
				writeCSVHeader();
				if(globalCSVFile!="")
				{
					writeGlobalCSVHeader();
				}
				
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
					
					//everybody moves
					executeMovementForCurrentTime();
					logger.printNewSection();
					objectsCommunicatingBetweenRegions = determineInterRegionCommunication();
//					Commented out because gregor says it is better to change assignments as little as possible
//					if(enforceMappingAvatarsToPeers)
//					{
//						regions = RegionAssignment.assignRegionsToPeersDependingOnOwnAvatar(regions, peers, true);
//					}
//					else
//					{
//						regions = RegionAssignment.assignRegionsToPeersRandomly(regions, peers);
//					}
					simulateInteraction(interactionRoundNumber);
					interactionBetweenPeers = determineInteractionBetweenPeers();
					overloadedPeers = determineOverloadedPeers();
					communicationCost = calculateCommunicationCost();
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
				}
				else
				{
					//we have to perform the movement and the interactions here to keep everything consistent, even though I am not sure about the interactions
					//we do not write the information into the .csv file, because we don't actually do anything with it.
					//this won't work with the zonal approach, because there the algorithm triggers as soon as one object moves into another server's territory
					//hence we will have to do this differently there
					executeMovementForCurrentTime();
					logger.printNewSection();
					simulateInteraction(interactionRoundNumber);
					timer.stop();
					timer.reset();
					logger.noExecution();
				}
				
				
			}
			
		}
		
				
	}
	
	private float calculateIdealDistributionWeight() 
	{
		float idealWeight = Util.calculateTotalRegionWeigth(regions) / peerNumber;
		return idealWeight;
	}
	
	private float generateDistributionFactor()
	{
		float result = 0;
		Iterator<Peer> itPeers = peers.iterator();
		while(itPeers.hasNext())
		{
			Peer peerToProcess = itPeers.next();
			ArrayList<Region> regionsManagedByPeer = RegionAssignment.getRegionsManagedByPeer(peerToProcess, regions);
			Iterator<Region> itRegionsManagedByPeer = regionsManagedByPeer.iterator();
			int weightManagedByPeer = 0;
			while(itRegionsManagedByPeer.hasNext())
			{
				weightManagedByPeer = weightManagedByPeer + RegionAssignment.getRegionWeight(itRegionsManagedByPeer.next());
			}
			
			//we iterate through all peers and calculate the absolute value of the object weight they have to manage
			// and the ideal weight each peer should carry. Then we sum these values to measure the quality of the
			//distribution. In an ideal case, it should be zero
			result = result + Math.abs(weightManagedByPeer - idealDistributionWeight);
		}
		result = result / Util.calculateTotalRegionWeigth(regions);
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

	//this function calculates the number of objects with AoIs spanning several regions
	private int determineInterRegionCommunication() 
	{
		int objectsInOtherRegions = 0;
		//only avatars have auras, which can span regions. Hence, we only have to evaluate them here
		Iterator<Avatar> itAvatarList = avatarList.iterator();
		while(itAvatarList.hasNext())
		{
			Avatar avatarToCheck = itAvatarList.next();
			Region regionContainingThisObject = RegionAssignment.findRegionContainingObjectByPosition(avatarToCheck, regions);
			ArrayList<MMVEObject> objectsInAura = InterestGraphConstruction.getAllObjectsWithIntersectingAoIs(avatarToCheck, avatarList, objectList);
			if(objectsInAura!=null)
			{
				Iterator<MMVEObject> itObjectsInAura = objectsInAura.iterator();
				while(itObjectsInAura.hasNext())
				{
					MMVEObject objectToCheck = itObjectsInAura.next();
					if(!regionContainingThisObject.getObjectsInRegion().contains(objectToCheck))
					{
						objectsInOtherRegions++;
					}
				}
			}
			
		}
		return objectsInOtherRegions;
	}

	//purpose of the function is to determine whether or not the interactions have to be recalculated
	private boolean areInteractionsStable() 
	{
		
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
		Iterator<Interaction> itInteractions = interactionList.iterator();
		while(itInteractions.hasNext())
		{
			Interaction interactionToProcess = itInteractions.next();
			if(!RegionAssignment.objectsInSameRegion(regions, interactionToProcess.getSource(), interactionToProcess.getTarget()))
			{
				result.add(interactionToProcess);
			}
		}
		
				
		return result;
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
	
	private void placeObjectsInRegions()
	{
		//place avatars
		Iterator<Avatar> itAvatarList = avatarList.iterator();
		while(itAvatarList.hasNext())
		{
			Avatar avatarToPlace = itAvatarList.next();
			Region regionContainingTheObject = RegionAssignment.findRegionContainingObjectByPosition(avatarToPlace, regions);
			if(regionContainingTheObject!=null)
			{
				regionContainingTheObject.getObjectsInRegion().add(avatarToPlace);
			}
			
		}
		//place non avatar objects
		Iterator<MMVEObject> itObjectList = objectList.iterator();
		while(itObjectList.hasNext())
		{
			MMVEObject objectToPlace = itObjectList.next();
			Region regionContainingTheObject = RegionAssignment.findRegionContainingObjectByPosition(objectToPlace, regions);
			if(regionContainingTheObject!=null)
			{
				regionContainingTheObject.getObjectsInRegion().add(objectToPlace);
			}
		}
		
	}




	//divides the gameWorld into regions. This function aims to divide the world into a number of regions
	//equal to the peerNumber. However, it is likely that a part of the world may not be split "cleanly".
	//Hence, this function often returns peerNumber-1 regions of full size and two remainingRegions containing
	//the remains. These get flagged by setting RemaingRegion to true
	private ArrayList<Region> divideGameMapIntoRegions() 
	{
		ArrayList<Region> result = new ArrayList<Region>();
		
		Rectangle gameWorld = new Rectangle(0, 0, 500, 500);
		//calculate size of each region
		double regionArea = (gameWorld.getSize().width * gameWorld.getSize().height) / peerNumber;
		int regionHeight = (int)Math.ceil(Math.sqrt(regionArea));
		int regionWidth = (int)Math.ceil(Math.sqrt(regionArea));
	
		int regionsPerRow = 0;
		//determine how many regions fit in one column
		Point p = new Point(regionWidth, 0);
		while(gameWorld.contains(p))
		{
			regionsPerRow++;
			p.setLocation(p.x + regionWidth, p.y);
		}
				
		p.setLocation(0, regionHeight);
		int regionsPerColumn = 0;
		while(gameWorld.contains(p))
		{
			regionsPerColumn++;
			p.setLocation(p.x, p.y + regionHeight);
		}
		
		//add the regions to the result
		for(int i=0; i<regionsPerRow; i++)
		{
			
			for(int j=0;j<regionsPerColumn; j++)
			{
				Region regionToAdd = new Region(i * regionWidth, j * regionHeight, regionWidth, regionHeight);
				result.add(regionToAdd);
			}
			
		}
		
		//check for any unassigned parts of the map
		if(result.size() != peerNumber)
		{
			//y position is on top of the highest row
			int topYPosition = regionsPerColumn * regionHeight;
			//width is equal to the total width of all regions in a row
			int topRemainingWidth = regionsPerRow * regionWidth;
			//the height is the border of the gameWorld minus the maximum height of the regions
			int topRemainingHeight = (int)gameWorld.getHeight()- regionsPerColumn * regionHeight;
			Region remainingTop = new Region(0, topYPosition , topRemainingWidth,  topRemainingHeight);
			remainingTop.setRemainingRegion(true);
			//x position is next to the last region in a row
			int bottomXPostion = regionsPerRow * regionWidth;
			//the width is the border of the gameWorld minus the maximum width of the regions
			int bottomRemainingWidth = (int)gameWorld.getWidth() - regionsPerRow * regionWidth;
			//height is equal to the total height of the gameWorld
			int bottomRemainingHeight = (int)gameWorld.getHeight();
			Region remainingBottom = new Region(bottomXPostion, 0, bottomRemainingWidth, bottomRemainingHeight);
			remainingBottom.setRemainingRegion(true);
			if(result.size()+2 != peerNumber)
			{
				ArrayList<Region> availableRemainingRegions = new ArrayList<Region>();
				int necessaryNumberOfRegions = peerNumber - (result.size());
				int divisionOfTop = necessaryNumberOfRegions / 2;
				int divisionsOfBottom = necessaryNumberOfRegions - divisionOfTop;
				availableRemainingRegions = divideRemainingRegions(remainingTop, divisionOfTop, availableRemainingRegions, true);
				availableRemainingRegions = divideRemainingRegions(remainingBottom, divisionsOfBottom, availableRemainingRegions, false);
				result.addAll(availableRemainingRegions);
			}
			else
			{
				result.add(remainingTop);
				result.add(remainingBottom);
			}
			
			
		}
		
		return result;
	}
	
	private ArrayList<Region> divideRemainingRegions(Region regionToProcess,int divisions, ArrayList<Region> availableRemainingRegions, boolean topRegion) 
	{
		if(divisions == 0)
			return availableRemainingRegions;
		
		int regionWidth = 0;
		int regionHeight = 0;
		if(topRegion)
		{
			regionWidth = regionToProcess.getGraphicalRepresentation().width / divisions;
			regionHeight = regionToProcess.getGraphicalRepresentation().height;
		}
		else
		{
			regionWidth = regionToProcess.getGraphicalRepresentation().width;
			regionHeight = regionToProcess.getGraphicalRepresentation().height / divisions;
		}
		
		int counter = 0;
		while(counter < divisions)
		{
			Region regionToAdd = null;
			int xCoordinate = 0;
			int yCoordinate = 0;
			if(topRegion)
			{
				xCoordinate = regionToProcess.getGraphicalRepresentation().x + counter * regionWidth;
				yCoordinate = regionToProcess.getGraphicalRepresentation().y;
			}
			else
			{
				xCoordinate = regionToProcess.getGraphicalRepresentation().x ;
				yCoordinate = regionToProcess.getGraphicalRepresentation().y + counter*regionHeight;
			}
			
			regionToAdd = new Region(xCoordinate, yCoordinate, regionWidth, regionHeight);
			availableRemainingRegions.add(regionToAdd);
			counter++;
		}
			
		
		return availableRemainingRegions;
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
			csvWriter.writeComment("interactionFile: " + interactionFile);
			csvWriter.writeComment("logFile: " + logFile);
			csvWriter.writeComment("peerNumber: " + peerNumber);
			csvWriter.writeComment("number of interactions: " + interactionNumber);
			csvWriter.writeComment("locality factor: " + localityFactor);
			csvWriter.writeComment("interaction round delay: " + interactionRoundDelay);
			csvWriter.writeComment("avatar weight: " + avatarWeight);
			csvWriter.writeComment("object weight: " + objectWeight);
			csvWriter.writeComment("AoI size: " + aoiSize);
			csvWriter.writeComment("use existing interaction file: " + useExistingInteractionFile);
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
			globalCSVWriter.writeComment("interactionFile: " + interactionFile);
			globalCSVWriter.writeComment("logFile: " + logFile);
			globalCSVWriter.writeComment("peerNumber: " + peerNumber);
			globalCSVWriter.writeComment("number of interactions: " + interactionNumber);
			globalCSVWriter.writeComment("locality factor: " + localityFactor);
			globalCSVWriter.writeComment("interaction round delay: " + interactionRoundDelay);
			globalCSVWriter.writeComment("avatar weight: " + avatarWeight);
			globalCSVWriter.writeComment("object weight: " + objectWeight);
			globalCSVWriter.writeComment("AoI size: " + aoiSize);
			globalCSVWriter.writeComment("use existing interaction file: " + useExistingInteractionFile);
			
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
		//in the geographic approach, there is no central authority which has to receive messages.
		//hence, the communication cost is zero, also because we disregard the communication between peers.
		int cost = 0;
		
		return cost;
	}

	private int determineOverloadedPeers() 
	{
		Iterator<Peer> itPeers = peers.iterator();
		int overloadedPeers = 0;
		while(itPeers.hasNext())
		{
			Peer peerToProcess = itPeers.next();
			int weightOfThisPeer = Util.calculateTotalRegionWeigth(RegionAssignment.getRegionsManagedByPeer(peerToProcess, regions));
			if(weightOfThisPeer > peerToProcess.getCapacity())
			{
				peerToProcess.setOverloaded(true);
				overloadedPeers++;
			}
			else
			{
				peerToProcess.setOverloaded(false);
			}
			
		}
		
		return overloadedPeers;
	}

	private void simulateInteraction(int currentRound) 
	{
		//empty both interaction list, because this a new round
		interactionList.clear();
		Iterator<String> itInteractions = processedInteractionFile.iterator();
		while(itInteractions.hasNext())
		{
			String[] content = itInteractions.next().split(" ");
			if(currentRound == Integer.parseInt(content[0]))
			{
				MMVEObject interactingObject = Util.getObjectWithID(avatarList,objectList, Integer.parseInt(content[1]));
				MMVEObject objectToInterActWith = Util.getObjectWithID(avatarList,objectList, Integer.parseInt(content[2]));
				
				if(interactingObject!=null && objectToInterActWith != null)
				{
					
						interactionList.add(new Interaction(interactingObject, objectToInterActWith));
						logger.logInteraction(interactingObject, objectToInterActWith);
					
				}
				else
				{
					logger.getLogFileWriter().println("Can't execute interaction, objects not found!");
				}
			}
			
		}
		
		
	}

	private void executeMovementForCurrentTime() 
	{
		reassignments = 0;
		Iterator<Avatar> itAvatars = avatarList.iterator();
		if(logger.isReady())
			logger.getLogFileWriter().println("Executing Moving");
//		ArrayList<Movement> avatarMovementDataToDelete = new ArrayList<Movement>();
//		ArrayList<Movement> objectMovementDataToDelete = new ArrayList<Movement>();
		while(itAvatars.hasNext())
		{
			Avatar avatarToProcess = itAvatars.next();
			Region regionContainingAvatar = RegionAssignment.findRegionContainingObjectByPosition(avatarToProcess, regions);
			Point oldPosition = avatarToProcess.getPosition();
			Movement movementOfThisObject = MobiSim.getAvatarOrObjectMovement(avatarMovementDataInRounds.get(counter), avatarToProcess.getMobiSimID(), counter);
			Point newPosition = movementOfThisObject.getNewPosition();
//			avatarMovementDataToDelete.add(movementOfThisObject);
			//if the object has moved to another region place it there and increase reassignments
			if(!regionContainingAvatar.getGraphicalRepresentation().contains(newPosition))
			{
				regionContainingAvatar.getObjectsInRegion().remove(avatarToProcess);
				avatarToProcess.setPosition(newPosition);
				Region newRegionContainingAvatar = RegionAssignment.findRegionContainingObjectByPosition(avatarToProcess, regions);
				newRegionContainingAvatar.getObjectsInRegion().add(avatarToProcess);
				logger.objectMovement(oldPosition, avatarToProcess);
				logger.objectTransfer(avatarToProcess, regionContainingAvatar, newRegionContainingAvatar);
				reassignments++;
			}
		}
		//delete old movement data for performance increase
//		avatarMovementData.removeAll(avatarMovementDataToDelete);
		
		Iterator<MMVEObject> itObjects = objectList.iterator();
		while(itObjects.hasNext())
		{
			MMVEObject objectToProcess = itObjects.next();
			Region regionContainingObject = RegionAssignment.findRegionContainingObjectByPosition(objectToProcess, regions);
			Point oldPosition = objectToProcess.getPosition();
			Movement movementOfThisObject = MobiSim.getAvatarOrObjectMovement(objectMovementDataInRounds.get(counter), objectToProcess.getMobiSimID(), counter);
			Point newPosition = movementOfThisObject.getNewPosition();
//			objectMovementDataToDelete.add(movementOfThisObject);
			//if the object has moved to another region place it there and increase reassignments
			if(!regionContainingObject.getGraphicalRepresentation().contains(newPosition))
			{
				regionContainingObject.getObjectsInRegion().remove(objectToProcess);
				objectToProcess.setPosition(newPosition);
				Region newRegionContainingObject = RegionAssignment.findRegionContainingObjectByPosition(objectToProcess, regions);
				newRegionContainingObject.getObjectsInRegion().add(objectToProcess);
				logger.objectMovement(oldPosition, objectToProcess);
				logger.objectTransfer(objectToProcess, regionContainingObject, newRegionContainingObject);
				reassignments++;
			}
		}
//		objectMovementData.removeAll(objectMovementDataToDelete);
		
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
	
	
	public ArrayList<Peer> getPeers() {
		return peers;
	}

	public void setPeers(ArrayList<Peer> peers) {
		this.peers = peers;
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

	public Rectangle getGameWorld() {
		return gameWorld;
	}



	public void setGameWorld(Rectangle gameWorld) {
		this.gameWorld = gameWorld;
	}



	public int getObjectsCommunicatingBetweenRegions() {
		return objectsCommunicatingBetweenRegions;
	}



	public void setObjectsCommunicatingBetweenRegions(
			int objectsCommunicatingBetweenRegions) {
		this.objectsCommunicatingBetweenRegions = objectsCommunicatingBetweenRegions;
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



	public boolean isUseExistingInteractionFile() {
		return useExistingInteractionFile;
	}



	public void setUseExistingInteractionFile(boolean useExistingInteractionFile) {
		this.useExistingInteractionFile = useExistingInteractionFile;
	}


	}
