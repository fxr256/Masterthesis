package master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;

public class LocalSimulation 
{
	private static final String configFilePath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\Config\\";
	private static final String valueFilePath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\ValueFiles\\";
	
	public static ArrayList<Timer> timers = new ArrayList<Timer>();
	
		
	public static void doLocalSimulationForXPeriods(int periodNumber, String avatarFile, String objectFile, String interestedInFile, 
			String logfile, String interactionFile, String csvFile, int peerNumber, int executionInterval, int metisExecutionDelay, 
			int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			int interestChance, double auraEdgesFactor, double interestedInFactor, boolean useExistingInterestedInFile, boolean useExistingInteractionFile, String configFile)
	{
		try
		{
			if(periodNumber == 0)
				throw new Exception("Periodnumber is zero!");
			// the interestedInFile may be created when running the task for the first time, hence
			//this is not needed
//			if(!Randomizer.testIfInterestedInFileExists(interestedInFile))
//				throw new Exception("InterestedIn file does not exist!");
			if(!MobiSim.testIfInputFileExists(avatarFile))
				throw new Exception("Avatar file not found!");
			if(!MobiSim.testIfInputFileExists(objectFile))
				throw new Exception("Object file not found!");
			
			
			Timer lokalSimulationTimer = new Timer(false);
			LocalSimulationTask task = new LocalSimulationTask(periodNumber, avatarFile, objectFile, interestedInFile, 
					logfile, interactionFile, csvFile, peerNumber, metisExecutionDelay, interactionNumber, 
					localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
					interestChance, auraEdgesFactor, interestedInFactor, useExistingInterestedInFile,  useExistingInteractionFile, lokalSimulationTimer);
			saveConfigurationToFile(configFile, task, executionInterval);
			timers.add(lokalSimulationTimer);
			lokalSimulationTimer.schedule(task, 0, executionInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void doLocalSimulationForXPeriods(int periodNumber, String avatarFile, String objectFile, String interestedInFile, 
			String logfile, String interactionFile, String csvFile, int peerNumber, int executionInterval, int metisExecutionDelay, int interactionNumber, int localityFactor, 
			int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,int interestChance, double auraEdgesFactor, double interestedInFactor, boolean useExistingInterestedInFile, boolean useExistingInteractionFile)
	{
		try
		{
			if(periodNumber == 0)
				throw new Exception("Periodnumber is zero!");
			if(!Randomizer.testIfInterestedInFileExists(interestedInFile))
				throw new Exception("InterestedIn file does not exist!");
			if(!MobiSim.testIfInputFileExists(avatarFile))
				throw new Exception("Avatar file not found!");
			if(!MobiSim.testIfInputFileExists(objectFile))
				throw new Exception("Object file not found!");
			
			
			Timer lokalSimulationTimer = new Timer();
			LocalSimulationTask task = new LocalSimulationTask(periodNumber, avatarFile, objectFile, interestedInFile,logfile,interactionFile, csvFile, peerNumber, metisExecutionDelay,interactionNumber, 
					localityFactor, interactionRoundDelay,avatarWeight, objectWeight, aoiSize,
					interestChance, auraEdgesFactor, interestedInFactor, useExistingInterestedInFile,  useExistingInteractionFile, lokalSimulationTimer);
			timers.add(lokalSimulationTimer);
			lokalSimulationTimer.schedule(task, 0, executionInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void doMultiValueLocalSimulationForXPeriods(int periodNumber, String avatarFile, String objectFile, String interestedInFile, 
			String logfile, String interactionFile, String csvFile, int peerNumber, int executionInterval, int metisExecutionDelay, int interactionNumber, int localityFactor, 
			int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,int interestChance, double auraEdgesFactor, double interestedInFactor, boolean useExistingInterestedInFile, boolean useExistingInteractionFile, String globalCSVFile)
	{
		try
		{
			if(periodNumber == 0)
				throw new Exception("Periodnumber is zero!");
			if(!Randomizer.testIfInterestedInFileExists(interestedInFile))
				throw new Exception("InterestedIn file does not exist!");
			if(!MobiSim.testIfInputFileExists(avatarFile))
				throw new Exception("Avatar file not found!");
			if(!MobiSim.testIfInputFileExists(objectFile))
				throw new Exception("Object file not found!");
			
			
			Timer lokalSimulationTimer = new Timer();
			LocalSimulationTask task = new LocalSimulationTask(periodNumber, avatarFile, objectFile, interestedInFile,logfile,interactionFile, csvFile, peerNumber, metisExecutionDelay,interactionNumber, 
					localityFactor, interactionRoundDelay,avatarWeight, objectWeight, aoiSize,
					interestChance, auraEdgesFactor, interestedInFactor, useExistingInterestedInFile, useExistingInteractionFile, globalCSVFile, lokalSimulationTimer);
			timers.add(lokalSimulationTimer);
			lokalSimulationTimer.schedule(task, 0, executionInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void doMultiValueLocalGeographicSimulationForXPeriods(int periodNumber, String avatarFile, String objectFile, 
			String logfile, String interactionFile, String csvFile, int peerNumber, int executionInterval, int metisExecutionDelay, 
			int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			boolean useExistingInteractionFile, String globalCSVFile)
	{
		try
		{
			if(periodNumber == 0)
				throw new Exception("Periodnumber is zero!");
			// the interestedInFile may be created when running the task for the first time, hence
			//this is not needed
//			if(!Randomizer.testIfInterestedInFileExists(interestedInFile))
//				throw new Exception("InterestedIn file does not exist!");
			if(!MobiSim.testIfInputFileExists(avatarFile))
				throw new Exception("Avatar file not found!");
			if(!MobiSim.testIfInputFileExists(objectFile))
				throw new Exception("Object file not found!");
			
			
			Timer lokalSimulationTimer = new Timer(false);
			LocalGeographicSimulationTask task = new LocalGeographicSimulationTask(periodNumber, avatarFile, objectFile, 
					logfile, interactionFile, csvFile, peerNumber, metisExecutionDelay, interactionNumber, 
					localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
					useExistingInteractionFile,globalCSVFile, lokalSimulationTimer);
			timers.add(lokalSimulationTimer);
			lokalSimulationTimer.schedule(task, 0, executionInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void doLocalSimulationForXPeriods(String configFile)
	{
		readAndExecuteConfigurationFile(configFile);		
	}
	
	public static void doLocalGeographicSimulationForXPeriods(int periodNumber, String avatarFile, String objectFile, 
			String logfile, String interactionFile, String csvFile, int peerNumber, int executionInterval, int metisExecutionDelay, 
			int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			boolean useExistingInteractionFile, String configFile)
	{
		try
		{
			if(periodNumber == 0)
				throw new Exception("Periodnumber is zero!");
			// the interestedInFile may be created when running the task for the first time, hence
			//this is not needed
//			if(!Randomizer.testIfInterestedInFileExists(interestedInFile))
//				throw new Exception("InterestedIn file does not exist!");
			if(!MobiSim.testIfInputFileExists(avatarFile))
				throw new Exception("Avatar file not found!");
			if(!MobiSim.testIfInputFileExists(objectFile))
				throw new Exception("Object file not found!");
			
			
			Timer lokalSimulationTimer = new Timer(false);
			LocalGeographicSimulationTask task = new LocalGeographicSimulationTask(periodNumber, avatarFile, objectFile, 
					logfile, interactionFile, csvFile, peerNumber, metisExecutionDelay, interactionNumber, 
					localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
					useExistingInteractionFile, lokalSimulationTimer);
			saveConfigurationToFile(configFile, task, executionInterval);
			timers.add(lokalSimulationTimer);
			lokalSimulationTimer.schedule(task, 0, executionInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void doLocalGeographicSimulationForXPeriods(int periodNumber, String avatarFile, String objectFile, 
			String logfile, String interactionFile, String csvFile, int peerNumber, int executionInterval, int metisExecutionDelay, 
			int interactionNumber, int localityFactor, int interactionRoundDelay, int avatarWeight, int objectWeight, int aoiSize,
			boolean useExistingInteractionFile)
	{
		try
		{
			if(periodNumber == 0)
				throw new Exception("Periodnumber is zero!");
			// the interestedInFile may be created when running the task for the first time, hence
			//this is not needed
//			if(!Randomizer.testIfInterestedInFileExists(interestedInFile))
//				throw new Exception("InterestedIn file does not exist!");
			if(!MobiSim.testIfInputFileExists(avatarFile))
				throw new Exception("Avatar file not found!");
			if(!MobiSim.testIfInputFileExists(objectFile))
				throw new Exception("Object file not found!");
			
			
			Timer lokalSimulationTimer = new Timer(false);
			LocalGeographicSimulationTask task = new LocalGeographicSimulationTask(periodNumber, avatarFile, objectFile, 
					logfile, interactionFile, csvFile, peerNumber, metisExecutionDelay, interactionNumber, 
					localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
					useExistingInteractionFile, lokalSimulationTimer);
			timers.add(lokalSimulationTimer);
			lokalSimulationTimer.schedule(task, 0, executionInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void doSimulationWithValueRange(String valueToVary, double startingPoint, double endPoint, 
			double iterationSteps, String configFile, boolean parallel, String dataFolder, String globalCSVFile)
	{
		if(checkIfValueToVaryIsValid(valueToVary))
		{
			//append a \\ if necessary
			if(!dataFolder.endsWith("\\"))
			{
				dataFolder = dataFolder + "\\";
			}
			
			File dataDirectory = new File (LocalSimulationTask.getCsvfilepath() + dataFolder);
			
			if(!dataDirectory.exists())
			{
				dataDirectory.mkdir();
			}
			//if the file exists, delete it
			File globalCSVf = new File(LocalSimulationTask.getCsvfilepath() + globalCSVFile);
			if(globalCSVf.exists())
			{
				globalCSVf.delete();
			}
			//at first read the config file
			try
			{
				BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + configFile));
				String line = null;
				int periodNumber = 0;
				String avatarFile = "";
				String objectFile = "";
				String interestedInFile = "";
				String logFile = "";
				String interactionFile = "";
				String csvFile = "";
				int peerNumber = 0;
				int executionInterval = 0;
				int metisExecutionDelay = 0;
				int interactionNumber = 0;
				int localityFactor = 0;
				int interactionRoundDelay = 0;
				int avatarWeight = 0;
				int objectWeight = 0;
				int aoiSize = 0;
				int interestChance = 0;
				double auraEdgesFactor = 0;
				double interestedInFactor = 0;
				boolean useExistingInterestedInFile = true; 
				boolean useExistingInteractionFile = true;
				
				while((line = configReader.readLine()) != null)
				{
					String[] content = line.split(" ");
					
					if(content[0].equals("periodNumber"))
					{
						periodNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarFile"))
					{
						avatarFile = content[1];
					}
					
					if(content[0].equals("objectFile"))
					{
						objectFile = content[1];
					}
					
					if(content[0].equals("interestedInFile"))
					{
						interestedInFile = content[1];
					}
					
					if(content[0].equals("logFile"))
					{
						logFile = content[1];
					}
					
					if(content[0].equals("interactionFile"))
					{
						interactionFile = content[1];
					}
					
					if(content[0].equals("csvFile"))
					{
						csvFile = content[1];
					}
					
					if(content[0].equals("peerNumber"))
					{
						peerNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("executionInterval"))
					{
						executionInterval = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("metisExecutionDelay"))
					{
						metisExecutionDelay = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interactionNumber"))
					{
						interactionNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("localityFactor"))
					{
						localityFactor = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interactionRoundDelay"))
					{
						interactionRoundDelay = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarWeight"))
					{
						avatarWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("objectWeight"))
					{
						objectWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("aoiSize"))
					{
						aoiSize = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interestChance"))
					{
						interestChance = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("auraEdgesFactor"))
					{
						auraEdgesFactor = Double.parseDouble(content[1]);
					}
					
					if(content[0].equals("interestedInFactor"))
					{
						interestedInFactor = Double.parseDouble(content[1]);
					}
					
					if(content[0].equals("useExistingInterestedInFile"))
					{
						useExistingInterestedInFile = Boolean.parseBoolean(content[1]);
					}
					
					if(content[0].equals("useExistingInteractionFile"))
					{
						useExistingInteractionFile = Boolean.parseBoolean(content[1]);
					}
				}
				
				//now change the value of the variable we are going to vary to the starting point
				//and execute a matching simulation
				if(valueToVary.equals("avatarWeight"))
				{
					avatarWeight = (int)startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(avatarWeight <= endPoint)
					{
						//we use timers here to make sure that only one timer gets executed at once
						if(timers.isEmpty() || parallel)
						{
							System.out.println("New Timer started!");
							doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
									dataFolder + "avatarWeight" + avatarWeight + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
							avatarWeight = avatarWeight + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("objectWeight"))
				{
					objectWeight = (int)startingPoint;
					
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(objectWeight <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
									dataFolder + "objectWeight" + objectWeight + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
							objectWeight = objectWeight + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("aoiSize"))
				{
					aoiSize = (int)startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(aoiSize <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
									dataFolder + "aoiSize" + aoiSize + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
							aoiSize = aoiSize + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("peerNumber"))
				{
					peerNumber = (int)startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(peerNumber <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
									dataFolder + "peerNumber" + peerNumber + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
							peerNumber = peerNumber + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("interestChance"))
				{
					interestChance = (int)startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(interestChance <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
									dataFolder + "interestChance" + interestChance + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
							interestChance = interestChance + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("auraEdgesFactor"))
				{
					auraEdgesFactor = startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
										
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(auraEdgesFactor <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
									dataFolder + "auraEdgesFactor" + auraEdgesFactor + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
							auraEdgesFactor = auraEdgesFactor + iterationSteps;
							
						}
						
					}
				}
				
				if(valueToVary.equals("interestedInFactor"))
				{
					interestedInFactor = startingPoint;
					
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
										
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(interestedInFactor <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
									dataFolder + "interestedInFactor" + interestedInFactor + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
							interestedInFactor = interestedInFactor + iterationSteps;
							
						}
						
					}
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("Can't vary this value!");
		}
		
	}
	

	
	private static boolean checkIfValueToVaryIsValid(String valueToVary) 
	{
		if(!valueToVary.equals("avatarWeight") && !valueToVary.equals("objectWeight") && !valueToVary.equals("aoiSize")
				&& !valueToVary.equals("interestChance") && !valueToVary.equals("auraEdgesFactor") && 
				!valueToVary.equals("interestedInFactor") && !valueToVary.equals("peerNumber"))
		{
			return false;
		}
		
		else
		{
			return true;
		}
			
	}
	
	public static void doGeographicSimulationWithValueRange(String valueToVary, double startingPoint, double endPoint, 
			double iterationSteps, String configFile, boolean parallel, String dataFolder, String globalCSVFile)
	{
		if(checkIfValueToVaryIsValidGeographic(valueToVary))
		{
			//append a \\ if necessary
			if(!dataFolder.endsWith("\\"))
			{
				dataFolder = dataFolder + "\\";
			}
			
			File dataDirectory = new File (LocalSimulationTask.getCsvfilepath() + dataFolder);
			
			if(!dataDirectory.exists())
			{
				dataDirectory.mkdir();
			}
			//if the file exists, delete it
			File globalCSVf = new File(LocalSimulationTask.getCsvfilepath() + globalCSVFile);
			if(globalCSVf.exists())
			{
				globalCSVf.delete();
			}
			//at first read the config file
			try
			{
				BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + configFile));
				String line = null;
				int periodNumber = 0;
				String avatarFile = "";
				String objectFile = "";
				String logFile = "";
				String interactionFile = "";
				String csvFile = "";
				int peerNumber = 0;
				int executionInterval = 0;
				int metisExecutionDelay = 0;
				int interactionNumber = 0;
				int localityFactor = 0;
				int interactionRoundDelay = 0;
				int avatarWeight = 0;
				int objectWeight = 0;
				int aoiSize = 0;
				boolean useExistingInteractionFile = true;
				
				while((line = configReader.readLine()) != null)
				{
					String[] content = line.split(" ");
					
					if(content[0].equals("periodNumber"))
					{
						periodNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarFile"))
					{
						avatarFile = content[1];
					}
					
					if(content[0].equals("objectFile"))
					{
						objectFile = content[1];
					}
					
					if(content[0].equals("logFile"))
					{
						logFile = content[1];
					}
					
					if(content[0].equals("interactionFile"))
					{
						interactionFile = content[1];
					}
					
					if(content[0].equals("csvFile"))
					{
						csvFile = content[1];
					}
					
					if(content[0].equals("peerNumber"))
					{
						peerNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("executionInterval"))
					{
						executionInterval = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("metisExecutionDelay"))
					{
						metisExecutionDelay = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interactionNumber"))
					{
						interactionNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("localityFactor"))
					{
						localityFactor = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interactionRoundDelay"))
					{
						interactionRoundDelay = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarWeight"))
					{
						avatarWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("objectWeight"))
					{
						objectWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("aoiSize"))
					{
						aoiSize = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("useExistingInteractionFile"))
					{
						useExistingInteractionFile = Boolean.parseBoolean(content[1]);
					}
				}
				
				//now change the value of the variable we are going to vary to the starting point
				//and execute a matching simulation
				if(valueToVary.equals("avatarWeight"))
				{
					avatarWeight = (int)startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(avatarWeight <= endPoint)
					{
						//we use timers here to make sure that only one timer gets executed at once
						if(timers.isEmpty() || parallel)
						{
							System.out.println("New Timer started!");
							doMultiValueLocalGeographicSimulationForXPeriods(periodNumber, avatarFile, objectFile, logFile, interactionFile,
									dataFolder + "avatarWeight" + avatarWeight + csvFile, peerNumber, executionInterval,metisExecutionDelay, 
									interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									 useExistingInteractionFile, dataFolder + globalCSVFile);
							avatarWeight = avatarWeight + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("objectWeight"))
				{
					objectWeight = (int)startingPoint;
					
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(objectWeight <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalGeographicSimulationForXPeriods(periodNumber, avatarFile, objectFile, logFile, interactionFile,
									dataFolder + "objectWeight" + objectWeight + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									useExistingInteractionFile, dataFolder + globalCSVFile);
							objectWeight = objectWeight + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("aoiSize"))
				{
					aoiSize = (int)startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(aoiSize <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalGeographicSimulationForXPeriods(periodNumber, avatarFile, objectFile, logFile, interactionFile,
									dataFolder + "aoiSize" + aoiSize + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									useExistingInteractionFile, dataFolder + globalCSVFile);
							aoiSize = aoiSize + (int)iterationSteps;
						}
						
					}
				}
				
				if(valueToVary.equals("peerNumber"))
				{
					peerNumber = (int)startingPoint;
					//test iterationSteps for stupid values
					if(iterationSteps <= 0)
					{
						iterationSteps = 1;
					}
					//round iteration steps up, because this is an integer
					iterationSteps = Math.ceil(iterationSteps);
					
					//test endPoint for stupid values
					if(endPoint < startingPoint)
					{
						endPoint = startingPoint;
					}
					//start the simulations with different values
					while(peerNumber <= endPoint)
					{
						if(timers.isEmpty() || parallel)
						{
							doMultiValueLocalGeographicSimulationForXPeriods(periodNumber, avatarFile, objectFile, logFile, interactionFile,
									dataFolder + "peerNumber" + peerNumber + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
									useExistingInteractionFile, dataFolder + globalCSVFile);
							peerNumber = peerNumber + (int)iterationSteps;
						}
						
					}
				}
				
				
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("Can't vary this value!");
		}
		
	}
	
	private static boolean checkIfValueToVaryIsValidGeographic(String valueToVary) 
	{
		if(!valueToVary.equals("avatarWeight") && !valueToVary.equals("objectWeight") && !valueToVary.equals("aoiSize")
				 && !valueToVary.equals("peerNumber"))
		{
			return false;
		}
		
		else
		{
			return true;
		}
			
	}

	public static boolean saveConfigurationToFile(String fileName, LocalSimulationTask task, int executionInterval)
	{
		try
		{
			File configFile = new File(configFilePath + fileName);
			if(!configFile.exists())
			{
				configFile.createNewFile();
			}
			PrintWriter configFileWriter = new PrintWriter(new FileWriter(configFile,false));
			
			//start writing new configFile
			configFileWriter.println("periodNumber" + " " + task.getNumberofIterations());
			configFileWriter.println("avatarFile" + " " + task.getAvatarFile());
			configFileWriter.println("objectFile" + " " + task.getObjectFile());
			configFileWriter.println("interestedInFile" + " " + task.getInterestedInFile());
			configFileWriter.println("logFile" + " " + task.getLogFile());
			configFileWriter.println("interactionFile" + " " + task.getInteractionFile());
			configFileWriter.println("csvFile" + " " + task.getCsvFile());
			configFileWriter.println("peerNumber" + " " + task.getPeerNumber());
			configFileWriter.println("executionInterval" + " " + executionInterval);
			configFileWriter.println("metisExecutionDelay" + " " + task.getMetisExecutionDelay());
			configFileWriter.println("interactionNumber" + " " + task.getInteractionNumber());
			configFileWriter.println("localityFactor" + " " + task.getLocalityFactor());
			configFileWriter.println("interactionRoundDelay" + " " + task.getInteractionRoundDelay());
			configFileWriter.println("avatarWeight" + " " + task.getAvatarWeight());
			configFileWriter.println("objectWeight" + " " + task.getObjectWeight());
			configFileWriter.println("aoiSize" + " " + task.getAoiSize());
			configFileWriter.println("interestChance" + " " + task.getInterestChance());
			configFileWriter.println("auraEdgesFactor" + " " + task.getAuraEdgesFactor());
			configFileWriter.println("interestedInFactor" + " " + task.getInterestedInFactor());
			configFileWriter.println("useExistingInterestedInFile" + " " + task.isUseExistingInterestedInFile());
			configFileWriter.println("useExistingInteractionFile" + " " + task.isUseExistingInteractionFile());
			
			configFileWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean saveConfigurationToFile(String fileName, LocalGeographicSimulationTask task, int executionInterval)
	{
		try
		{
			File configFile = new File(configFilePath + fileName);
			if(!configFile.exists())
			{
				configFile.createNewFile();
			}
			PrintWriter configFileWriter = new PrintWriter(new FileWriter(configFile,false));
			
			//start writing new configFile
			configFileWriter.println("periodNumber" + " " + task.getNumberofIterations());
			configFileWriter.println("avatarFile" + " " + task.getAvatarFile());
			configFileWriter.println("objectFile" + " " + task.getObjectFile());
			configFileWriter.println("logFile" + " " + task.getLogFile());
			configFileWriter.println("interactionFile" + " " + task.getInteractionFile());
			configFileWriter.println("csvFile" + " " + task.getCsvFile());
			configFileWriter.println("peerNumber" + " " + task.getPeerNumber());
			configFileWriter.println("executionInterval" + " " + executionInterval);
			configFileWriter.println("metisExecutionDelay" + " " + task.getMetisExecutionDelay());
			configFileWriter.println("interactionNumber" + " " + task.getInteractionNumber());
			configFileWriter.println("localityFactor" + " " + task.getLocalityFactor());
			configFileWriter.println("interactionRoundDelay" + " " + task.getInteractionRoundDelay());
			configFileWriter.println("avatarWeight" + " " + task.getAvatarWeight());
			configFileWriter.println("objectWeight" + " " + task.getObjectWeight());
			configFileWriter.println("aoiSize" + " " + task.getAoiSize());
			configFileWriter.println("useExistingInteractionFile" + " " + task.isUseExistingInteractionFile());
			
			
			configFileWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void readAndExecuteConfigurationFile(String fileName)
	{
		try
		{
			BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + fileName));
			String line = null;
			int periodNumber = 0;
			String avatarFile = "";
			String objectFile = "";
			String interestedInFile = "";
			String logFile = "";
			String interactionFile = "";
			String csvFile = "";
			int peerNumber = 0;
			int executionInterval = 0;
			int metisExecutionDelay = 0;
			int interactionNumber = 0;
			int localityFactor = 0;
			int interactionRoundDelay = 0;
			int avatarWeight = 0;
			int objectWeight = 0;
			int aoiSize = 0;
			int interestChance = 0;
			double auraEdgesFactor = 0;
			double interestedInFactor = 0;
			boolean useExistingInterestedInFile = true;
			boolean useExistingInteractionFile = true;
			
			while((line = configReader.readLine()) != null)
			{
				String[] content = line.split(" ");
				
				if(content[0].equals("periodNumber"))
				{
					periodNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarFile"))
				{
					avatarFile = content[1];
				}
				
				if(content[0].equals("objectFile"))
				{
					objectFile = content[1];
				}
				
				if(content[0].equals("interestedInFile"))
				{
					interestedInFile = content[1];
				}
				
				if(content[0].equals("logFile"))
				{
					logFile = content[1];
				}
				
				if(content[0].equals("interactionFile"))
				{
					interactionFile = content[1];
				}
				
				if(content[0].equals("csvFile"))
				{
					csvFile = content[1];
				}
				
				if(content[0].equals("peerNumber"))
				{
					peerNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("executionInterval"))
				{
					executionInterval = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("metisExecutionDelay"))
				{
					metisExecutionDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionNumber"))
				{
					interactionNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("localityFactor"))
				{
					localityFactor = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionRoundDelay"))
				{
					interactionRoundDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarWeight"))
				{
					avatarWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("objectWeight"))
				{
					objectWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("aoiSize"))
				{
					aoiSize = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interestChance"))
				{
					interestChance = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("auraEdgesFactor"))
				{
					auraEdgesFactor = Double.parseDouble(content[1]);
				}
				
				if(content[0].equals("interestedInFactor"))
				{
					interestedInFactor = Double.parseDouble(content[1]);
				}
				
				if(content[0].equals("useExistingInterestedInFile"))
				{
					useExistingInterestedInFile = Boolean.parseBoolean(content[1]);
				}
				
				if(content[0].equals("useExistingInteractionFile"))
				{
					useExistingInteractionFile = Boolean.parseBoolean(content[1]);
				}
			}
			//do execution
			doLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile,
					csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
					interestChance, auraEdgesFactor, interestedInFactor, useExistingInterestedInFile, useExistingInteractionFile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void readAndExecuteGeographicConfigurationFile(String fileName)
	{
		try
		{
			BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + fileName));
			String line = null;
			int periodNumber = 0;
			String avatarFile = "";
			String objectFile = "";
			String logFile = "";
			String interactionFile = "";
			String csvFile = "";
			int peerNumber = 0;
			int executionInterval = 0;
			int metisExecutionDelay = 0;
			int interactionNumber = 0;
			int localityFactor = 0;
			int interactionRoundDelay = 0;
			int avatarWeight = 0;
			int objectWeight = 0;
			int aoiSize = 0;
			
			boolean useExistingInteractionFile = true;
			
			while((line = configReader.readLine()) != null)
			{
				String[] content = line.split(" ");
				
				if(content[0].equals("periodNumber"))
				{
					periodNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarFile"))
				{
					avatarFile = content[1];
				}
				
				if(content[0].equals("objectFile"))
				{
					objectFile = content[1];
				}
				
					
				if(content[0].equals("logFile"))
				{
					logFile = content[1];
				}
				
				if(content[0].equals("interactionFile"))
				{
					interactionFile = content[1];
				}
				
				if(content[0].equals("csvFile"))
				{
					csvFile = content[1];
				}
				
				if(content[0].equals("peerNumber"))
				{
					peerNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("executionInterval"))
				{
					executionInterval = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("metisExecutionDelay"))
				{
					metisExecutionDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionNumber"))
				{
					interactionNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("localityFactor"))
				{
					localityFactor = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionRoundDelay"))
				{
					interactionRoundDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarWeight"))
				{
					avatarWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("objectWeight"))
				{
					objectWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("aoiSize"))
				{
					aoiSize = Integer.parseInt(content[1]);
				}
				
						
				if(content[0].equals("useExistingInteractionFile"))
				{
					useExistingInteractionFile = Boolean.parseBoolean(content[1]);
				}
			}
			//do execution
			doLocalGeographicSimulationForXPeriods(periodNumber, avatarFile, objectFile, logFile, interactionFile,
					csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize, useExistingInteractionFile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void generateValueFile(String fileName, String[] valuesToVary, double[] startingValues, double[] endValues, double[] iterationSteps, boolean[] parallel)
	{
		//check input
		if(valuesToVary.length == startingValues.length && valuesToVary.length == endValues.length
				&& valuesToVary.length == iterationSteps.length && valuesToVary.length == parallel.length)
		{
			try
			{
				File valueFile = new File(valueFilePath + fileName);
				if(!valueFile.exists())
				{
					valueFile.createNewFile();
					
				}
				PrintWriter valueFileWriter = new PrintWriter(new FileWriter(valueFile,false));
				//write all the values
				for(int i = 0; i<valuesToVary.length; i++)
				{
					valueFileWriter.println(valuesToVary[i] + " " + startingValues[i] + " " + endValues[i] + 
							" " + iterationSteps[i] + " " + parallel[i]);
				}
				
				valueFileWriter.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("invalid Input!");
		}
	}
	
	public static void readAndExecuteValueFile(String valueFileName, String configFileName, String dataFolder, String globalCSVFile)
	{
		try
		{
			BufferedReader valueFileReader = new BufferedReader(new FileReader(valueFilePath + valueFileName));
			String line = null;
			
			//read content and execute
			while((line = valueFileReader.readLine())!= null)
			{
				String[] content = line.split(" ");
				//check size of the read line
				if(content.length == 5)
				{
					String valueToVary = content[0];
					Double startingPoint = Double.parseDouble(content[1]);
					Double endPoint = Double.parseDouble(content[2]);
					Double iterationSteps = Double.parseDouble(content[3]);
					Boolean parallel = Boolean.parseBoolean(content[4]);
					doSimulationWithValueRange(valueToVary, startingPoint, endPoint, iterationSteps, configFileName, parallel, dataFolder, globalCSVFile);
				}
				else
				{
					System.out.println("Error reading valueFile!");
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//used to test the scalability of the system with increasing number of objects
	//the user needs to specify the names of the objects files which are going to be used throughout the test
	public static void conductInterestBasedScalabilityTest(String[] objectFiles, String configFile, boolean parallel, String dataFolder, String globalCSVFile)
	{
		
		//append a \\ if necessary
		if(!dataFolder.endsWith("\\"))
		{
			dataFolder = dataFolder + "\\";
		}
		
		File dataDirectory = new File (LocalSimulationTask.getCsvfilepath() + dataFolder);
		
		if(!dataDirectory.exists())
		{
			dataDirectory.mkdir();
		}
		//if the file exists, delete it
		File globalCSVf = new File(LocalSimulationTask.getCsvfilepath() + globalCSVFile);
		if(globalCSVf.exists())
		{
			globalCSVf.delete();
		}
		//at first read the config file
		try
		{
			BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + configFile));
			String line = null;
			int periodNumber = 0;
			String avatarFile = "";
			String interestedInFile = "";
			String logFile = "";
			String interactionFile = "";
			String csvFile = "";
			int peerNumber = 0;
			int executionInterval = 0;
			int metisExecutionDelay = 0;
			int interactionNumber = 0;
			int localityFactor = 0;
			int interactionRoundDelay = 0;
			int avatarWeight = 0;
			int objectWeight = 0;
			int aoiSize = 0;
			int interestChance = 0;
			double auraEdgesFactor = 0;
			double interestedInFactor = 0;
			boolean useExistingInterestedInFile = true; 
			boolean useExistingInteractionFile = true;
			
			while((line = configReader.readLine()) != null)
			{
				String[] content = line.split(" ");
				
				if(content[0].equals("periodNumber"))
				{
					periodNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarFile"))
				{
					avatarFile = content[1];
				}
							
				if(content[0].equals("interestedInFile"))
				{
					interestedInFile = content[1];
				}
				
				if(content[0].equals("logFile"))
				{
					logFile = content[1];
				}
				
				if(content[0].equals("interactionFile"))
				{
					interactionFile = content[1];
				}
				
				if(content[0].equals("csvFile"))
				{
					csvFile = content[1];
				}
				
				if(content[0].equals("peerNumber"))
				{
					peerNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("executionInterval"))
				{
					executionInterval = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("metisExecutionDelay"))
				{
					metisExecutionDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionNumber"))
				{
					interactionNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("localityFactor"))
				{
					localityFactor = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionRoundDelay"))
				{
					interactionRoundDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarWeight"))
				{
					avatarWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("objectWeight"))
				{
					objectWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("aoiSize"))
				{
					aoiSize = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interestChance"))
				{
					interestChance = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("auraEdgesFactor"))
				{
					auraEdgesFactor = Double.parseDouble(content[1]);
				}
				
				if(content[0].equals("interestedInFactor"))
				{
					interestedInFactor = Double.parseDouble(content[1]);
				}
				
				if(content[0].equals("useExistingInterestedInFile"))
				{
					useExistingInterestedInFile = Boolean.parseBoolean(content[1]);
				}
				
				if(content[0].equals("useExistingInteractionFile"))
				{
					useExistingInteractionFile = Boolean.parseBoolean(content[1]);
				}
				
			}
		
			int counter = 0;
			
			while(counter < objectFiles.length)
			{
				if(timers.isEmpty() || parallel)
				{
					doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFiles[counter], interestedInFile, logFile, interactionFile,
							dataFolder + "objectNumber" + counter + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
							interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
					counter++;
					
				}				
				
			}
			
			
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void conductGeographicScalabilityTest(String[] objectFiles, String configFile, boolean parallel, String dataFolder, String globalCSVFile)
	{
		
		
		//append a \\ if necessary
		if(!dataFolder.endsWith("\\"))
		{
			dataFolder = dataFolder + "\\";
		}
		
		File dataDirectory = new File (LocalSimulationTask.getCsvfilepath() + dataFolder);
		
		if(!dataDirectory.exists())
		{
			dataDirectory.mkdir();
		}
		//if the file exists, delete it
		File globalCSVf = new File(LocalSimulationTask.getCsvfilepath() + globalCSVFile);
		if(globalCSVf.exists())
		{
			globalCSVf.delete();
		}
		//at first read the config file
		try
		{
			BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + configFile));
			String line = null;
			int periodNumber = 0;
			String avatarFile = "";
			String logFile = "";
			String csvFile = "";
			String interactionFile = "";
			int peerNumber = 0;
			int executionInterval = 0;
			int metisExecutionDelay = 0;
			int interactionNumber = 0;
			int localityFactor = 0;
			int interactionRoundDelay = 0;
			int avatarWeight = 0;
			int objectWeight = 0;
			int aoiSize = 0;
			boolean useExistingInteractionFile = true;
			
			while((line = configReader.readLine()) != null)
			{
				String[] content = line.split(" ");
				
				if(content[0].equals("periodNumber"))
				{
					periodNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarFile"))
				{
					avatarFile = content[1];
				}
				
					
				if(content[0].equals("logFile"))
				{
					logFile = content[1];
				}
				
				if(content[0].equals("csvFile"))
				{
					csvFile = content[1];
				}
				
				if(content[0].equals("peerNumber"))
				{
					peerNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("executionInterval"))
				{
					executionInterval = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("metisExecutionDelay"))
				{
					metisExecutionDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionNumber"))
				{
					interactionNumber = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("localityFactor"))
				{
					localityFactor = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionRoundDelay"))
				{
					interactionRoundDelay = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("avatarWeight"))
				{
					avatarWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("objectWeight"))
				{
					objectWeight = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("aoiSize"))
				{
					aoiSize = Integer.parseInt(content[1]);
				}
				
				if(content[0].equals("interactionFile"))
				{
					interactionFile = content[1];
				}
				
				if(content[0].equals("useExistingInteractionFile"))
				{
					useExistingInteractionFile = Boolean.parseBoolean(content[1]);
				}
			}
			
			//now change the value of the variable we are going to vary to the starting point
			//and execute a matching simulation
			int counter = 0;
			
			while(counter < objectFiles.length)
			{
				if(timers.isEmpty() || parallel)
				{
					System.out.println("New Timer started!");
					doMultiValueLocalGeographicSimulationForXPeriods(periodNumber, avatarFile, objectFiles[counter], logFile, interactionFile,
							dataFolder + "objectNumber" + counter + csvFile, peerNumber, executionInterval,metisExecutionDelay, 
							interactionNumber, localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
							 useExistingInteractionFile, dataFolder + globalCSVFile);
					counter++;
				}
			}
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void testInteractionBehaviourInterest(String[] interactionFiles, int[] interactionNumbers, String configFile, boolean parallel, String dataFolder, String globalCSVFile)
	{
		
		//append a \\ if necessary
		if(!dataFolder.endsWith("\\"))
		{
			dataFolder = dataFolder + "\\";
		}
		
		File dataDirectory = new File (LocalSimulationTask.getCsvfilepath() + dataFolder);
		
		if(!dataDirectory.exists())
		{
			dataDirectory.mkdir();
		}
		//if the file exists, delete it
		File globalCSVf = new File(LocalSimulationTask.getCsvfilepath() + globalCSVFile);
		if(globalCSVf.exists())
		{
			globalCSVf.delete();
		}
		
		if(interactionFiles.length == interactionNumbers.length)
		{
			//at first read the config file
			try
			{
				BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + configFile));
				String line = null;
				int periodNumber = 0;
				String avatarFile = "";
				String objectFile = "";
				String interestedInFile = "";
				String logFile = "";
				String csvFile = "";
				int peerNumber = 0;
				int executionInterval = 0;
				int metisExecutionDelay = 0;
				int localityFactor = 0;
				int interactionRoundDelay = 0;
				int avatarWeight = 0;
				int objectWeight = 0;
				int aoiSize = 0;
				int interestChance = 0;
				double auraEdgesFactor = 0;
				double interestedInFactor = 0;
				boolean useExistingInterestedInFile = true; 
				boolean useExistingInteractionFile = true;
				
				while((line = configReader.readLine()) != null)
				{
					String[] content = line.split(" ");
					
					if(content[0].equals("periodNumber"))
					{
						periodNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarFile"))
					{
						avatarFile = content[1];
					}
					
					if(content[0].equals("objectFile"))
					{
						objectFile = content[1];
					}
								
					if(content[0].equals("interestedInFile"))
					{
						interestedInFile = content[1];
					}
					
					if(content[0].equals("logFile"))
					{
						logFile = content[1];
					}
					
					if(content[0].equals("csvFile"))
					{
						csvFile = content[1];
					}
					
					if(content[0].equals("peerNumber"))
					{
						peerNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("executionInterval"))
					{
						executionInterval = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("metisExecutionDelay"))
					{
						metisExecutionDelay = Integer.parseInt(content[1]);
					}
					
										
					if(content[0].equals("localityFactor"))
					{
						localityFactor = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interactionRoundDelay"))
					{
						interactionRoundDelay = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarWeight"))
					{
						avatarWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("objectWeight"))
					{
						objectWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("aoiSize"))
					{
						aoiSize = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interestChance"))
					{
						interestChance = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("auraEdgesFactor"))
					{
						auraEdgesFactor = Double.parseDouble(content[1]);
					}
					
					if(content[0].equals("interestedInFactor"))
					{
						interestedInFactor = Double.parseDouble(content[1]);
					}
					
					if(content[0].equals("useExistingInterestedInFile"))
					{
						useExistingInterestedInFile = Boolean.parseBoolean(content[1]);
					}
					
					if(content[0].equals("useExistingInteractionFile"))
					{
						useExistingInteractionFile = Boolean.parseBoolean(content[1]);
					}
				}
				
				//now change the value of the variable we are going to vary to the starting point
				//and execute a matching simulation
				int counter = 0;
				
				while(counter < interactionFiles.length)
				{
					if(timers.isEmpty() || parallel)
					{
						System.out.println("New Timer started!");
						doMultiValueLocalSimulationForXPeriods(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFiles[counter],
								dataFolder + "interactions" + counter + csvFile, peerNumber, executionInterval,metisExecutionDelay, interactionNumbers[counter], localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
								interestChance, auraEdgesFactor, interestedInFactor,useExistingInterestedInFile, useExistingInteractionFile, dataFolder + globalCSVFile);
						counter++;
					}
				}
					
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("Input error, not the same amount of interaction files and interaction numbers");
		}
		
		
	}

	
	public static void testInteractionBehaviourGeographic(String[] interactionFiles, int[] interactionNumbers, String configFile, boolean parallel, String dataFolder, String globalCSVFile)
	{
		
		//append a \\ if necessary
		if(!dataFolder.endsWith("\\"))
		{
			dataFolder = dataFolder + "\\";
		}
		
		File dataDirectory = new File (LocalSimulationTask.getCsvfilepath() + dataFolder);
		
		if(!dataDirectory.exists())
		{
			dataDirectory.mkdir();
		}
		//if the file exists, delete it
		File globalCSVf = new File(LocalSimulationTask.getCsvfilepath() + globalCSVFile);
		if(globalCSVf.exists())
		{
			globalCSVf.delete();
		}
		
		if(interactionFiles.length == interactionNumbers.length)
		{
			//at first read the config file
			try
			{
				BufferedReader configReader = new BufferedReader(new FileReader(configFilePath + configFile));
				String line = null;
				int periodNumber = 0;
				String avatarFile = "";
				String objectFile = "";
				String logFile = "";
				String csvFile = "";
				int peerNumber = 0;
				int executionInterval = 0;
				int metisExecutionDelay = 0;
				int localityFactor = 0;
				int interactionRoundDelay = 0;
				int avatarWeight = 0;
				int objectWeight = 0;
				int aoiSize = 0;
				boolean useExistingInteractionFile = true;
				
				while((line = configReader.readLine()) != null)
				{
					String[] content = line.split(" ");
					
					if(content[0].equals("periodNumber"))
					{
						periodNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarFile"))
					{
						avatarFile = content[1];
					}
					
					if(content[0].equals("objectFile"))
					{
						objectFile = content[1];
					}
						
					if(content[0].equals("logFile"))
					{
						logFile = content[1];
					}
					
					if(content[0].equals("csvFile"))
					{
						csvFile = content[1];
					}
					
					if(content[0].equals("peerNumber"))
					{
						peerNumber = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("executionInterval"))
					{
						executionInterval = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("metisExecutionDelay"))
					{
						metisExecutionDelay = Integer.parseInt(content[1]);
					}
					
					
					if(content[0].equals("localityFactor"))
					{
						localityFactor = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("interactionRoundDelay"))
					{
						interactionRoundDelay = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("avatarWeight"))
					{
						avatarWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("objectWeight"))
					{
						objectWeight = Integer.parseInt(content[1]);
					}
					
					if(content[0].equals("aoiSize"))
					{
						aoiSize = Integer.parseInt(content[1]);
					}
										
					if(content[0].equals("useExistingInteractionFile"))
					{
						useExistingInteractionFile = Boolean.parseBoolean(content[1]);
					}
				}
				
				//now change the value of the variable we are going to vary to the starting point
				//and execute a matching simulation
				int counter = 0;
				
				while(counter < interactionFiles.length)
				{
					if(timers.isEmpty() || parallel)
					{
						System.out.println("New Timer started!");
						doMultiValueLocalGeographicSimulationForXPeriods(periodNumber, avatarFile, objectFile, logFile, interactionFiles[counter],
								dataFolder + "interactions" + counter + csvFile, peerNumber, executionInterval,metisExecutionDelay, 
								interactionNumbers[counter], localityFactor, interactionRoundDelay, avatarWeight, objectWeight, aoiSize,
								 useExistingInteractionFile, dataFolder + globalCSVFile);
						counter++;
					}
				}
					
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("Input error, not the same amount of interaction files and interaction numbers");
		}
		
		
	}
}
