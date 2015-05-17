package master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class InteractionSimulator 
{
	private static final String interactionDirectoryPath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\Interactions\\";
	
	//generates an Interaction File
	public static boolean generateInteractionFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph, int totalInteractionNumber, String fileName)
	{
		
		//delete the file if it already exists, because we append later in the function
		File interactionFile = new File(interactionDirectoryPath + fileName);
		
		if(interactionFile.exists())
		{
			interactionFile.delete();
		}
		
		Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
		Random randomGenerator = new Random();
		
		int counter = 0;
		//the double while loop is used just in case the graph is too small to deliver the required number of
		//interactions, if this happens we just start again at the beginning
		while(counter < totalInteractionNumber)
		{
			//restart the iterator if we have traversed through all objects, but have not reached
			//the necessary amount of interactions
			if(counter < totalInteractionNumber && !itInterestGraph.hasNext())
			{
				itInterestGraph = interestGraph.vertexSet().iterator();
			}
			
			while(itInterestGraph.hasNext())
			{
				//used to control the random numbers which are generate
				int remaining = totalInteractionNumber - counter;
				//necessary because the outer while loop does not get checked until the
				//inner loop terminates. WIthout this nextInt() throws an exception
				if(remaining == 0)
				{
					break;
				}
				
				MMVEObject interactingObject = itInterestGraph.next();
				int interactionsOfThisObject = randomGenerator.nextInt(remaining);
				//if remaining is 1, the random generator only generates zeros. This leads to an infinite loop. 
				//Hence, interactionsOfThisObject is set to 1
				if(remaining == 1)
					interactionsOfThisObject = 1;
				//write the interactions of the current object to a file
				counter = counter + writeInteractionsOfThisObject(interactingObject, interactionsOfThisObject, interestGraph, fileName);
				
			}
			
		}
		return true;
	}
	
	public static boolean generateInteractionFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph, int totalInteractionNumber, String fileName, 
			int percentageNonRegularInteractions)
	
	{
		
		//delete the file if it already exists, because we append later in the function
		File interactionFile = new File(interactionDirectoryPath + fileName);
		
		if(interactionFile.exists())
		{
			interactionFile.delete();
		}
		
		Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
		Random randomGenerator = new Random();
		
		int counter = 0;
		int nonregularInteractions = totalInteractionNumber * percentageNonRegularInteractions / 100;
		int regularInteractions = totalInteractionNumber - nonregularInteractions;
		
		//the double while loop is used just in case the graph is too small to deliver the required number of
		//interactions, if this happens we just start again at the beginning
		// we determine the regular interactions out of the loop, because repeating them
		// only leads to duplicates thanks to the limited number of options.
		counter = counter + generateRegularInteractions(interestGraph, regularInteractions, fileName);
		
		while(counter < totalInteractionNumber)
		{
			//restart the iterator if we have traversed through all objects, but have not reached
			//the necessary amount of interactions
			if(counter < totalInteractionNumber && !itInterestGraph.hasNext())
			{
				itInterestGraph = interestGraph.vertexSet().iterator();
			}
									
			while(itInterestGraph.hasNext())
			{
				//used to control the random numbers which are generate
				int remaining = totalInteractionNumber - counter;
				//necessary because the outer while loop does not get checked until the
				//inner loop terminates. WIthout this nextInt() throws an exception
				if(remaining == 0)
				{
					break;
				}
				
				MMVEObject interactingObject = itInterestGraph.next();
				int interactionsOfThisObject = randomGenerator.nextInt(remaining);
				//if remaining is 1, the random generator only generates zeros. This leads to an infinite loop. 
				//Hence, interactionsOfThisObject is set to 1
				if(remaining == 1)
					interactionsOfThisObject = 1;
				//write the interactions of the current object to a file
				counter = counter + writeInteractionsOfThisObject(interactingObject, interactionsOfThisObject, interestGraph, fileName);
				
			}
			
		}
		return true;
		
	}
	
	//as suggested by gregor
	public static boolean generateComplexInteractionFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph, int totalInteractionNumber, String fileName,
			int percentageLocalInteractions)
	{
		
		//delete the file if it already exists, because we append later in the function
		File interactionFile = new File(interactionDirectoryPath + fileName);
		
		if(interactionFile.exists())
		{
			interactionFile.delete();
		}
		
		Random randomGenerator = new Random();
		//calculate the number of local interactions
		int numberOfLocalInteractions = totalInteractionNumber * percentageLocalInteractions / 100;
		//gregor says that the number of random interactions is equal to the remaining number
//		int numberOfRandomInteractions = totalInteractionNumber - numberOfLocalInteractions;
		int counter = 0;
		
		Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
		//do local interactions first
		counter = counter + doLocalInteractions(interestGraph, numberOfLocalInteractions, fileName);
		
		//do remaining interactions
		while(counter < totalInteractionNumber)
		{
			//restart the iterator if we have traversed through all objects, but have not reached
			//the necessary amount of interactions
			if(counter < totalInteractionNumber && !itInterestGraph.hasNext())
			{
				itInterestGraph = interestGraph.vertexSet().iterator();
			}
			
			while(itInterestGraph.hasNext())
			{
			
				//used to control the random numbers which are generate
				int remaining = totalInteractionNumber - counter;
				//necessary because the outer while loop does not get checked until the
				//inner loop terminates. WIthout this nextInt() throws an exception
				if(remaining == 0)
				{
					break;
				}
				
				MMVEObject interactingObject = itInterestGraph.next();
				int interactionsOfThisObject = randomGenerator.nextInt(remaining+1);
				//if remaining is 1, the random generator only generates zeros. This leads to an infinite loop. 
				//Hence, interactionsOfThisObject is set to 1
				if(remaining == 1)
					interactionsOfThisObject = 1;
				//write the interactions of the current object to a file
				counter = counter + writeInteractionsOfThisObject(interactingObject, interactionsOfThisObject, interestGraph, fileName);
			}
			
		}
		
		return true;
	}
	
	public static boolean generateMultiRoundComplexInterestFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph, int interactionsPerRound, String fileName,
			int percentageLocalInteractions, int totalRoundNumber)
	{
		//delete the file if it already exists, because we append later in the function
		File interactionFile = new File(interactionDirectoryPath + fileName);
		
		if(interactionFile.exists())
		{
			interactionFile.delete();
		}
		
		int counter = 0;
		
		while(counter < totalRoundNumber)
		{
			boolean success = calculateInteractionsForThisRound(interestGraph, interactionsPerRound, fileName, percentageLocalInteractions, counter+1);
			if(success)
			{
				counter++;
			}
		}
		return true;
	}
	
	public static boolean generateMultiRoundComplexInterestFile(ArrayList<Avatar> avatarList, ArrayList<MMVEObject> objectList, int interactionsPerRound, String fileName,
			int percentageLocalInteractions, int totalRoundNumber)
	{
		//delete the file if it already exists, because we append later in the function
		File interactionFile = new File(interactionDirectoryPath + fileName);
		
		if(interactionFile.exists())
		{
			interactionFile.delete();
		}
		
		int counter = 0;
		
		while(counter < totalRoundNumber)
		{
			boolean success = calculateInteractionsForThisRound(avatarList, objectList, interactionsPerRound, fileName, percentageLocalInteractions, counter+1);
			if(success)
			{
				counter++;
			}
		}
		return true;
	}
	
	private static boolean calculateInteractionsForThisRound(ArrayList<Avatar> avatarList, ArrayList<MMVEObject> objectList,int interactionsPerRound, String fileName,
			int percentageLocalInteractions, int currentRoundNumber) 
	{
		Random randomGenerator = new Random();
		//calculate the number of local interactions
		int numberOfLocalInteractions = interactionsPerRound * percentageLocalInteractions / 100;
		
		int counter = 0;
		
		Iterator<Avatar> itAvatarList = avatarList.iterator();
		Iterator<MMVEObject> itObjectList = objectList.iterator();
		//do local interactions first
		counter = counter + doLocalInteractions(avatarList, objectList, numberOfLocalInteractions, fileName, currentRoundNumber);
		
		ArrayList<MMVEObject> mergedList = Util.mergeAvatarAndObjectList(avatarList, objectList);
		Iterator<MMVEObject> itMergedList = mergedList.iterator();
		
		//do remaining interactions
		while(counter < interactionsPerRound)
		{
			//restart the iterator if we have traversed through all objects, but have not reached
			//the necessary amount of interactions
			if(counter < interactionsPerRound && !itMergedList.hasNext())
			{
				itMergedList = mergedList.iterator();
			}
			
			while(itMergedList.hasNext())
			{
			
				//used to control the random numbers which are generate
				int remaining = interactionsPerRound - counter;
				//necessary because the outer while loop does not get checked until the
				//inner loop terminates. WIthout this nextInt() throws an exception
				if(remaining == 0)
				{
					break;
				}
				
				MMVEObject interactingObject = itMergedList.next();
				int interactionsOfThisObject = randomGenerator.nextInt(remaining+1);
				//if remaining is 1, the random generator only generates zeros. This leads to an infinite loop. 
				//Hence, interactionsOfThisObject is set to 1
				if(remaining == 1)
					interactionsOfThisObject = 1;
				//write the interactions of the current object to a file
				counter = counter + writeInteractionsOfThisObject(interactingObject, interactionsOfThisObject, mergedList, fileName, currentRoundNumber);
			}
			
		}
		
		return true;
	}
	
	private static boolean calculateInteractionsForThisRound(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,	int interactionsPerRound, String fileName,
			int percentageLocalInteractions, int currentRoundNumber) 
	{
		Random randomGenerator = new Random();
		//calculate the number of local interactions
		int numberOfLocalInteractions = interactionsPerRound * percentageLocalInteractions / 100;
		
		int counter = 0;
		
		Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
		//do local interactions first
		counter = counter + doLocalInteractions(interestGraph, numberOfLocalInteractions, fileName, currentRoundNumber);
		
		//do remaining interactions
		while(counter < interactionsPerRound)
		{
			//restart the iterator if we have traversed through all objects, but have not reached
			//the necessary amount of interactions
			if(counter < interactionsPerRound && !itInterestGraph.hasNext())
			{
				itInterestGraph = interestGraph.vertexSet().iterator();
			}
			
			while(itInterestGraph.hasNext())
			{
			
				//used to control the random numbers which are generate
				int remaining = interactionsPerRound - counter;
				//necessary because the outer while loop does not get checked until the
				//inner loop terminates. WIthout this nextInt() throws an exception
				if(remaining == 0)
				{
					break;
				}
				
				MMVEObject interactingObject = itInterestGraph.next();
				int interactionsOfThisObject = randomGenerator.nextInt(remaining+1);
				//if remaining is 1, the random generator only generates zeros. This leads to an infinite loop. 
				//Hence, interactionsOfThisObject is set to 1
				if(remaining == 1)
					interactionsOfThisObject = 1;
				//write the interactions of the current object to a file
				counter = counter + writeInteractionsOfThisObject(interactingObject, interactionsOfThisObject, interestGraph, fileName, currentRoundNumber);
			}
			
		}
		
		return true;
	}

	//we traverse through the graph and interact with every object within our aura
	//this may return fewer interactions than desired, but not more because the stop as soon
	//as the desired number is reached.
	private static int doLocalInteractions(	DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,
			int numberOfLocalInteractions, String fileName) 
	{
		int counter = 0;
		Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
		
		while(counter < numberOfLocalInteractions)
		{
			//we traverse the graph as long as it takes to calculate the necessary number of interactions
			if(!itInterestGraph.hasNext() && counter < numberOfLocalInteractions)
			{
				itInterestGraph = interestGraph.vertexSet().iterator();
			}
			
			//it is possible that there are no local interactions at all, this variable is used to detect this
			int identifiedInteractionsPerGraphPass = 0;
			
			while(itInterestGraph.hasNext())
			{
				//get object to process
				MMVEObject objectToProcess = itInterestGraph.next();
				
				//check whether object is an avatar
				if(objectToProcess instanceof Avatar)
				{
					ArrayList<MMVEObject> objectsToInteractWith = InterestGraphConstruction.getAllObjectsWithIntersectingAoIs(objectToProcess, interestGraph);
					if(objectsToInteractWith != null)
					{
						Iterator<MMVEObject> itObjectList = objectsToInteractWith.iterator();
						//iterate through the determined list and add the interactions
						//aborts if the maximum number of local interactions is reached
						while(itObjectList.hasNext() && counter < numberOfLocalInteractions)
						{
							MMVEObject objectToInteractWith = itObjectList.next();
							counter = counter + writeInteractionBetweenTwoObject(objectToProcess, objectToInteractWith, fileName);
							identifiedInteractionsPerGraphPass++;
						}
					}
				}
				//currently we do nothing here => ask gregor
			}
			
			if(identifiedInteractionsPerGraphPass == 0 && counter < numberOfLocalInteractions)
			{
				System.out.println("No possible local interactions found, will only use random interacts instead");
				break;
			}
		}
		
		return counter;
	}
	
	private static int doLocalInteractions(	DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,
			int numberOfLocalInteractions, String fileName, int currentRoundNumber) 
	{
		int counter = 0;
		Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
		
		while(counter < numberOfLocalInteractions)
		{
			//we traverse the graph as long as it takes to calculate the necessary number of interactions
			if(!itInterestGraph.hasNext() && counter < numberOfLocalInteractions)
			{
				itInterestGraph = interestGraph.vertexSet().iterator();
			}
			
			//it is possible that there are no local interactions at all, this variable is used to detect this
			int identifiedInteractionsPerGraphPass = 0;
			
			while(itInterestGraph.hasNext())
			{
				//get object to process
				MMVEObject objectToProcess = itInterestGraph.next();
				
				//check whether object is an avatar
				if(objectToProcess instanceof Avatar)
				{
					ArrayList<MMVEObject> objectsToInteractWith = InterestGraphConstruction.getAllObjectsWithIntersectingAoIs(objectToProcess, interestGraph);
					if(objectsToInteractWith != null)
					{
						Iterator<MMVEObject> itObjectList = objectsToInteractWith.iterator();
						//iterate through the determined list and add the interactions
						//aborts if the maximum number of local interactions is reached
						while(itObjectList.hasNext() && counter < numberOfLocalInteractions)
						{
							MMVEObject objectToInteractWith = itObjectList.next();
							counter = counter + writeInteractionBetweenTwoObject(objectToProcess, objectToInteractWith, fileName, currentRoundNumber);
							identifiedInteractionsPerGraphPass++;
						}
					}
				}
				//currently we do nothing here => ask gregor
			}
			
			if(identifiedInteractionsPerGraphPass == 0 && counter < numberOfLocalInteractions)
			{
				System.out.println("No possible local interactions found, will only use random interacts instead");
				break;
			}
		}
		
		return counter;
	}
	
	private static int doLocalInteractions(	ArrayList<Avatar> avatarList, ArrayList<MMVEObject> objectList,
			int numberOfLocalInteractions, String fileName, int currentRoundNumber) 
	{
		int counter = 0;
		Iterator<Avatar> itAvatarList = avatarList.iterator();
		
		while(counter < numberOfLocalInteractions)
		{
			//we traverse the graph as long as it takes to calculate the necessary number of interactions
			if(!itAvatarList.hasNext() && counter < numberOfLocalInteractions)
			{
				itAvatarList = avatarList.iterator();
			}
			
			//it is possible that there are no local interactions at all, this variable is used to detect this
			int identifiedInteractionsPerGraphPass = 0;
			
			while(itAvatarList.hasNext())
			{
				//get object to process
				Avatar avatarToProcess = itAvatarList.next();
				
				//check whether object is an avatar
				
				ArrayList<MMVEObject> objectsToInteractWith = InterestGraphConstruction.getAllObjectsWithIntersectingAoIs(avatarToProcess, avatarList, objectList);
				if(objectsToInteractWith != null)
				{
					Iterator<MMVEObject> itObjectList = objectsToInteractWith.iterator();
					//iterate through the determined list and add the interactions
					//aborts if the maximum number of local interactions is reached
					while(itObjectList.hasNext() && counter < numberOfLocalInteractions)
					{
						MMVEObject objectToInteractWith = itObjectList.next();
						counter = counter + writeInteractionBetweenTwoObject(avatarToProcess, objectToInteractWith, fileName, currentRoundNumber);
						identifiedInteractionsPerGraphPass++;
					}
				}
				
				//currently we do nothing here => ask gregor
			}
			
			if(identifiedInteractionsPerGraphPass == 0 && counter < numberOfLocalInteractions)
			{
				System.out.println("No possible local interactions found, will only use random interacts instead");
				break;
			}
		}
		
		return counter;
	}

	private static int generateRegularInteractions(
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,
			int regularInteractions, String fileName) 
	{
		int counter = 0;
		Iterator<MMVEObject> itGraph = interestGraph.vertexSet().iterator();
		
		Random randomGenerator = new Random();
		
		while(counter < regularInteractions)
		{
			if(!itGraph.hasNext() && counter < regularInteractions)
			{
				System.out.println("Not enough suitable edges for the desired number of regualar interactions");
				break;
			}
			while(itGraph.hasNext())
			{
				int remaining = regularInteractions - counter;
				//necessary because the outer while loop does not get checked until the
				//inner loop terminates. WIthout this nextInt() throws an exception
				if(remaining == 0)
				{
					break;
				}
				
				MMVEObject interactingObject = itGraph.next();
				int interactionsOfThisObject = 0;
				
				if(remaining == 1)
					interactionsOfThisObject = 1;
				else
					interactionsOfThisObject = randomGenerator.nextInt(remaining);
				
				counter = counter + writeRegularInteractionsOfThisObject(interactingObject, interactionsOfThisObject, interestGraph, fileName);
				
			}
			
		}
		
		
		return counter;
	}

	private static int writeRegularInteractionsOfThisObject(MMVEObject interactingObject,int interactionsOfThisObject,
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,String fileName) 
	{
		try
		{
			
			File interactionFile = new File(interactionDirectoryPath + fileName);
			if(!interactionFile.exists())
			{
				interactionFile.createNewFile();
			}
			// true = we append the content to the file
			PrintWriter interactionWriter = new PrintWriter(new FileWriter(interactionFile, true));
			
			int counter = 0;
			Random randomGenerator = new Random();
			
			//grab all edges touching the interacting object 
			ArrayList<DefaultWeightedEdge> edges = new ArrayList<DefaultWeightedEdge>();
			//remove any incoming edges to the interactingObject
			edges = Util.removeIncomingEdgesFromEdgesOf(interestGraph.edgesOf(interactingObject), interestGraph, interactingObject);
			//because the number of interactions for this object are random it is possible that
			//it surpasses the number of suitable edges
			//hence we try to fix this here
			if(edges.size() < interactionsOfThisObject)
				interactionsOfThisObject = edges.size();
			
			while(counter < interactionsOfThisObject)
			{
								
				DefaultWeightedEdge edgeToWorkWith = edges.get(randomGenerator.nextInt(edges.size()));
				MMVEObject objectToInteractWith = interestGraph.getEdgeTarget(edgeToWorkWith);
				//probably not necessary
				if(objectToInteractWith!=null && objectToInteractWith.getID() != interactingObject.getID())
				{
					//write the interaction to file
					interactionWriter.println("regular Interaction between: " + interactingObject.getID() + " " + objectToInteractWith.getID());
					counter++;
					//remove the processed edge form edges to speed things up
					edges.remove(edgeToWorkWith);
				}
				
			}
			
			interactionWriter.close();
			return interactionsOfThisObject;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean doesInteractionFileExist(String fileName)
	{
		try
		{
			File interestFile = new File(interactionDirectoryPath + fileName);
			if(!interestFile.exists())
				return false;
			BufferedReader reader = new BufferedReader(new FileReader (interestFile));
			reader.close();			
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
		
	}
	
	public static ArrayList<String> readInteractionFile(String fileName)
	{
		ArrayList<String> result = new ArrayList<String>();
		try
		{
			File interestFile = new File(interactionDirectoryPath + fileName);
			BufferedReader reader = new BufferedReader(new FileReader (interestFile));
			String line = "";
			while((line = reader.readLine())!= null)
			{
				String[] content = line.split(" ");
				//normally the length of the array should be 5, but some older interactions files are still smaller
				//hence we check this here
				//if the length is too low, we return 0 as the round number
				if(content.length == 4)
				{
					result.add(0 + " " + content[2] + " " + content[3]);
				}
				
				if(content.length == 5)
				{
					result.add(0 + " " + content[3] + " " + content[4]);
				}
				
				//length == 6 indicates that there is a round number in the record
				//hence adjust the numbers
				if(content.length == 6)
				{
					result.add(content[3] + " " + content[4] + " " + content[5]);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
		

	private static int writeInteractionsOfThisObject(
			MMVEObject interactingObject, int interactionsOfThisObject, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,
			String fileName, int currentRoundNumber) 
	{
		try
		{
			
			File interactionFile = new File(interactionDirectoryPath + fileName);
			
			if(!interactionFile.exists())
			{
				interactionFile.createNewFile();
			}
			// true = we append the content to the file
			PrintWriter interactionWriter = new PrintWriter(new FileWriter(interactionFile, true));
			
			int counter = 0;
			Random randomGenerator = new Random();
			
			while(counter < interactionsOfThisObject)
			{
				//grab a random object of the graph to interact with it
				int objectToGet = randomGenerator.nextInt(interestGraph.vertexSet().size());
				if(objectToGet != interactingObject.getID())
				{
					MMVEObject objectToInterActWith = Util.getObjectWithID(interestGraph, objectToGet);
					if(objectToInterActWith!= null)
					{
							//write the interaction to file
							interactionWriter.println("nonregular Interaction between: " + currentRoundNumber + " " + interactingObject.getID() + " " + objectToInterActWith.getID());
							counter++;
						
					}
				}
			}
			
			interactionWriter.close();
			return interactionsOfThisObject;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		
	}
	
	private static int writeInteractionsOfThisObject(
			MMVEObject interactingObject, int interactionsOfThisObject, ArrayList<MMVEObject> mergedList,
			String fileName, int currentRoundNumber) 
	{
		try
		{
			
			File interactionFile = new File(interactionDirectoryPath + fileName);
			
			if(!interactionFile.exists())
			{
				interactionFile.createNewFile();
			}
			// true = we append the content to the file
			PrintWriter interactionWriter = new PrintWriter(new FileWriter(interactionFile, true));
			
			int counter = 0;
			Random randomGenerator = new Random();
			
			while(counter < interactionsOfThisObject)
			{
				//grab a random object of the graph to interact with it
				int objectToGet = randomGenerator.nextInt(mergedList.size());
				if(objectToGet != interactingObject.getID())
				{
					MMVEObject objectToInterActWith = Util.getObjectWithID(mergedList, objectToGet);
					if(objectToInterActWith!= null)
					{
							//write the interaction to file
							interactionWriter.println("nonregular Interaction between: " + currentRoundNumber + " " + interactingObject.getID() + " " + objectToInterActWith.getID());
							counter++;
						
					}
				}
			}
			
			interactionWriter.close();
			return interactionsOfThisObject;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		
	}
	
	private static int writeInteractionsOfThisObject(
			MMVEObject interactingObject, int interactionsOfThisObject, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,
			String fileName) 
	{
		try
		{
			
			File interactionFile = new File(interactionDirectoryPath + fileName);
			
			if(!interactionFile.exists())
			{
				interactionFile.createNewFile();
			}
			// true = we append the content to the file
			PrintWriter interactionWriter = new PrintWriter(new FileWriter(interactionFile, true));
			
			int counter = 0;
			Random randomGenerator = new Random();
			
			while(counter < interactionsOfThisObject)
			{
				//grab a random object of the graph to interact with it
				int objectToGet = randomGenerator.nextInt(interestGraph.vertexSet().size());
				if(objectToGet != interactingObject.getID())
				{
					MMVEObject objectToInterActWith = Util.getObjectWithID(interestGraph, objectToGet);
					if(objectToInterActWith!= null)
					{
							//write the interaction to file
							interactionWriter.println("nonregular Interaction between: " + interactingObject.getID() + " " + objectToInterActWith.getID());
							counter++;
						
					}
				}
			}
			
			interactionWriter.close();
			return interactionsOfThisObject;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		
	}
	
	//prints the interaction between two objects to the interaction file
	//has no random components whatsoever.
	private static int writeInteractionBetweenTwoObject(MMVEObject interactingObject, MMVEObject objectToInteractWith,
			String fileName)
	{
		try
		{
			
			File interactionFile = new File(interactionDirectoryPath + fileName);
			if(!interactionFile.exists())
			{
				interactionFile.createNewFile();
			}
			// true = we append the content to the file
			PrintWriter interactionWriter = new PrintWriter(new FileWriter(interactionFile, true));
			
			//write the interaction to file
			interactionWriter.println("local Interaction between: " + interactingObject.getID() + " " + objectToInteractWith.getID());
			interactionWriter.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	    
   }
	
	private static int writeInteractionBetweenTwoObject(MMVEObject interactingObject, MMVEObject objectToInteractWith,
			String fileName, int currentRoundNumber)
	{
		try
		{
			
			File interactionFile = new File(interactionDirectoryPath + fileName);
			if(!interactionFile.exists())
			{
				interactionFile.createNewFile();
			}
			// true = we append the content to the file
			PrintWriter interactionWriter = new PrintWriter(new FileWriter(interactionFile, true));
			
			//write the interaction to file
			interactionWriter.println("local Interaction between: " + currentRoundNumber + " " + interactingObject.getID() + " " + objectToInteractWith.getID());
			interactionWriter.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	    
   }
}
