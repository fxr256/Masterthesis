package backup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;

import master.GraphEmptyException;
import master.MMVEObject;
import master.Util;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class HistoricInterest 
{
	private final static String historicInterestPath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\GraphInput\\HistoricInterest\\";
	
	//saves all edges of a graph to a file for later use
	public static boolean saveInterestPatternToFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph, String fileName)
	{
		try
		{
			if(interestGraph.edgeSet().isEmpty())
				throw new Exception("The interest graph does not have any edges");
			
			String filePath = historicInterestPath + fileName;
			FileWriter file = new FileWriter(filePath);
			PrintWriter out = new PrintWriter(file);
			
			String firstLine = interestGraph.vertexSet().size() + " " + interestGraph.edgeSet().size();
			out.println(firstLine);
			
			Iterator<DefaultWeightedEdge> itEdges = interestGraph.edgeSet().iterator();
			while(itEdges.hasNext())
			{
				DefaultWeightedEdge edgeToSave = itEdges.next();
				String line = interestGraph.getEdgeSource(edgeToSave).getID() + " " + interestGraph.getEdgeTarget(edgeToSave).getID() + 
				" " + interestGraph.getEdgeWeight(edgeToSave);
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
	
	//reads the given historic interest file and adds all found edges to the graph, if it does not already contain edges between
	//the objects. If new edges are created, their weight is equal to the historic value multiplied with a factor.
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> readHistoricInterestFromFile(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph, String fileName, double historicWeightFactor)
	{
		
		try
		{
			if(interestGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("The graph you are trying to use is empty!");
			
			String filePath = historicInterestPath + fileName;
			BufferedReader reader = new BufferedReader(new FileReader (filePath));
			
			String FirstLine = reader.readLine();
			String[] firstLineProcessed = FirstLine.split(" ");
			if(Integer.parseInt(firstLineProcessed[0]) != interestGraph.vertexSet().size())
				throw new Exception("The saved vertex set size does not match this graph!");
			
			String line = "";			
			while((line=reader.readLine())!=null)
			{
				String[] lineContent = line.split(" ");
				//check if the graph contains the searched objects
				if(interestGraph.containsVertex(Util.getObjectWithID(interestGraph, Integer.parseInt(lineContent[0]))) &&
						interestGraph.containsVertex(Util.getObjectWithID(interestGraph, Integer.parseInt(lineContent[1]))))
				{
					if(!interestGraph.containsEdge(Util.getObjectWithID(interestGraph, Integer.parseInt(lineContent[0])), Util.getObjectWithID(interestGraph, Integer.parseInt(lineContent[1]))))
					{
						DefaultWeightedEdge edgeToAdd = interestGraph.addEdge(Util.getObjectWithID(interestGraph, Integer.parseInt(lineContent[0])), Util.getObjectWithID(interestGraph, Integer.parseInt(lineContent[1])));
						interestGraph.setEdgeWeight(edgeToAdd, Double.parseDouble(lineContent[2]) * historicWeightFactor);
					}
					
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return interestGraph;
	}
	
	public static boolean testIfHistoricInterestFileExists(String fileName)
	{
		try
		{
			String filePath = historicInterestPath + fileName;
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

}
