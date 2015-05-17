package master;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class Randomizer 
{
	private final static String interestedInPath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\GraphInput\\InterestedIn\\";
	
	public static ArrayList<MMVEObject> addRandomInterestRelationshipToObjectList(ArrayList<MMVEObject> input, 
			int interestChance)
	{
		try
		{
			if(input.isEmpty())
				throw new Exception("The input list is empty!");
			
			if(interestChance > 100)
				throw new Exception("The interest chance is too high!");
			
			if(interestChance == 0)
				throw new Exception("The interest chance is 0");
			
			
			Iterator<MMVEObject> itInput = input.iterator();
			
			Random randomGenerator = new Random();
						
			while(itInput.hasNext())
			{
				MMVEObject objectToWorkWith = itInput.next();
				//random number from 0-100
				int chance = randomGenerator.nextInt(101);
				//determine whether the object is interested in other objects
				//we are checking here if the object is NOT interested in other objects
				//e.g. 20% interestChance = 80% chance that the object is not interested
				//if chance > 20 => not interested (= 80% chance, if all numbers appeach with 1/100 propability
				if(chance > interestChance)
				{
					//System.out.println("Object " + objectToWorkWith.getID() + " is not interested in other objects!");
					System.out.println("Object " + objectToWorkWith.getID() + " was unlucky!");
				}
				else
				{
					//the object in others
					objectToWorkWith = generateInterestingObjects(objectToWorkWith, input);
				}
			
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return input;
	}
	//this function saves the interestedIn information of each object in a graph to a textfile
	//this is used to create reproduceable result in testing
	public static boolean saveInterestedInToFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graphToSave, String fileName)
	{
		try
		{
			if(graphToSave.vertexSet().isEmpty())
				throw new GraphEmptyException("The graph you are using is empty!");
			String filePath = interestedInPath + fileName;
			FileWriter file = new FileWriter(filePath);
			PrintWriter out = new PrintWriter(file);
			
			Iterator<MMVEObject> itGraph = graphToSave.vertexSet().iterator();
			
			while(itGraph.hasNext())
			{
				MMVEObject currentObject = itGraph.next();
				String line = String.valueOf(currentObject.getID());
				line = createStringForInterestedIn(currentObject, line);
				out.println(line);
			}
			out.close();
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> readInterestedInInfoFromFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graphToWorkWith, String fileName)
	{
		try
		{
			if(graphToWorkWith.vertexSet().isEmpty())
				throw new GraphEmptyException("The Graph you are trying to use is empty!");
			
			String filePath = interestedInPath + fileName;
			BufferedReader reader = new BufferedReader(new FileReader (filePath));
			String line = "";
			
			while((line=reader.readLine())!=null)
			{
				String[] lineContent = line.split(" ");
				MMVEObject interestedObject = Util.getObjectWithID(graphToWorkWith, Integer.parseInt(lineContent[0]));
				//check if the object could be retrieved and if the line actually contains other objects which
				//the object is interested in
				if(interestedObject!=null && lineContent.length > 1)
				{
					for(int i = 1; i<lineContent.length; i++)
					{
						MMVEObject objectToAdd = Util.getObjectWithID(graphToWorkWith, Integer.parseInt(lineContent[i]));
						if(objectToAdd != null)
						{
							interestedObject.addInterest(objectToAdd);
						}
					}
				}
			}
			
			reader.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return graphToWorkWith;
	}

	
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addRandomInterestRelationshipToObjectGraph(	DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> inputGraph, 
			int interestChance)
	{
		try
		{
			if(inputGraph.vertexSet().isEmpty())
				throw new Exception("The input list is empty!");
			
			if(interestChance > 100)
				throw new Exception("The interest chance is too high!");
			
			if(interestChance == 0)
				throw new Exception("The interest chance is 0");
			
			
			Iterator<MMVEObject> itInput = inputGraph.vertexSet().iterator();
			
			Random randomGenerator = new Random();
						
			while(itInput.hasNext())
			{
				MMVEObject objectToWorkWith = itInput.next();
				//random number from 0-100
				int chance = randomGenerator.nextInt(101);
				//determine whether the object is interested in other objects
				//we are checking here if the object is NOT interested in other objects
				//e.g. 20% interestChance = 80% chance that the object is not interested
				//if chance > 20 => not interested (= 80% chance, if all numbers appeach with 1/100 propability
				if(chance > interestChance)
				{
					//System.out.println("Object " + objectToWorkWith.getID() + " is not interested in other objects!");
					System.out.println("Object " + objectToWorkWith.getID() + " was unlucky!");
				}
				else
				{
					//the object in others
					objectToWorkWith = generateInterestingObjectsGraph(objectToWorkWith, inputGraph);
				}
			
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return inputGraph;
	}
	
	public static boolean testIfInterestedInFileExists(String fileName)
	{
		try
		{
			String filePath = interestedInPath + fileName;
			BufferedReader reader = new BufferedReader(new FileReader (filePath));
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static String createStringForInterestedIn(MMVEObject currentObject,	String line) 
	{
		Iterator<MMVEObject> itInterested = currentObject.getInterestedIn().iterator();
		while(itInterested.hasNext())
		{
			line = line + " " + itInterested.next().getID();
		}
		return line;
	}

	private static MMVEObject generateInterestingObjects(MMVEObject objectToWorkWith, ArrayList<MMVEObject> input) 
	{
		Random generator = new Random();
		
		//generate number of objects this one is interested in
		int numberOfItems = generator.nextInt(input.size()-1);
		//set the numberofitems to 1 if its zero
		//the case that this object is not interested in others was already handled
		if(numberOfItems == 0)
			numberOfItems++;
		
		//now pick items at random, until the number of items is reached
		while(objectToWorkWith.getInterestedIn().size() < numberOfItems)
		{
			int objectNumberToAdd = generator.nextInt(input.size());
			objectToWorkWith.addInterest(input.get(objectNumberToAdd));
		}
		
		return objectToWorkWith;
	}
	
	private static MMVEObject generateInterestingObjectsGraph(MMVEObject objectToWorkWith, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> inputGraph) 
	{
		Random generator = new Random();
		
		//generate number of objects this one is interested in
		//size -1, because we can't add ourself
		int numberOfItems = generator.nextInt(inputGraph.vertexSet().size()-1);
		//set the numberofitems to 1 if its zero
		//the case that this object is not interested in others was already handled
		if(numberOfItems == 0)
			numberOfItems++;
		
		//now pick items at random, until the number of items is reached
		while(objectToWorkWith.getInterestedIn().size() < numberOfItems)
		{
			//size +1 because the graph starts with 1 instead of 0 
			int objectNumberToAdd = generator.nextInt(inputGraph.vertexSet().size()+1);
			if(objectNumberToAdd == 0)
				objectNumberToAdd++;
			if(objectNumberToAdd == objectToWorkWith.getID())
				objectNumberToAdd++;
			objectToWorkWith.addInterest(Util.getObjectWithID(inputGraph, objectNumberToAdd));
		}
		
		return objectToWorkWith;
	}

}
