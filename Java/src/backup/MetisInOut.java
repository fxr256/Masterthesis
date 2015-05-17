package backup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class MetisInOut 

{
	
	public static boolean convertVertexGraphToGraphFile(DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g, String FileName)
	{
		try
		{
			if(g.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			FileWriter file = new FileWriter(FileName);
			PrintWriter out = new PrintWriter(file);
			//first line in the textfile describes the type of graph for metis
			//x y 11 means a graph with x vertices and y edges with weights on vertices and edges, but only one weight for each is allowed
			String firstline = String.valueOf(g.vertexSet().size());
			firstline = firstline + " " +  String.valueOf(Util.metisEdgeNumberVertex(g));
			firstline = firstline + " " + 11;
			
			out.println(firstline);
			
			ArrayList<Vertex> list = Util.sortVertices(g.vertexSet());
			Iterator<Vertex> itList = list.iterator();
			
			//all weights get casted to int here, because metis only supports int weights
			while(itList.hasNext())
			{
				Vertex v = itList.next();
				String output = String.valueOf((int)v.getWeight());
				Set<DefaultWeightedEdge> edgesV = g.edgesOf(v);
				Iterator<DefaultWeightedEdge> itEV = edgesV.iterator();
				
					while(itEV.hasNext())
					{
						DefaultWeightedEdge e = itEV.next();
						if(g.getEdgeSource(e).equals(v))
						{
							output = output + " " + String.valueOf(g.getEdgeTarget(e).getID()) + " " +
							String.valueOf((int)g.getEdgeWeight(e));
						}
						
					}
				//print the line
				out.println(output);
				out.flush();
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
	
	
	
	//only constructs a graph with weight on the edges, because weight on both vertices and edges seems to be buggy
	public static boolean convertVertexGraphToSimpleGraphFile(DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g, String FileName)
	{
		try
		{
			if(g.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			FileWriter file = new FileWriter(FileName);
			PrintWriter out = new PrintWriter(file);
			//first line in the textfile describes the type of graph for metis
			//x y 11 means a graph with x vertices and y edges with weights on vertices and edges, but only one weight for each is allowed
			String firstline = String.valueOf(g.vertexSet().size());
			firstline = firstline + " " +  String.valueOf(Util.metisEdgeNumberVertex(g));
			firstline = firstline + " " + 1;
			
			out.println(firstline);
			
			ArrayList<Vertex> list = Util.sortVertices(g.vertexSet());
			Iterator<Vertex> itList = list.iterator();
			
			//all weights get casted to int here, because metis only supports int weights
			while(itList.hasNext())
			{
				Vertex v = itList.next();
				String output = "";
				Set<DefaultWeightedEdge> edgesV = g.edgesOf(v);
				Iterator<DefaultWeightedEdge> itEV = edgesV.iterator();
				
					while(itEV.hasNext())
					{
						DefaultWeightedEdge e = itEV.next();
						if(g.getEdgeSource(e).equals(v))
						{
							if (output=="")
							{
								output = output + String.valueOf(g.getEdgeTarget(e).getID()) + " " +
								String.valueOf((int)g.getEdgeWeight(e));
							}
							else
							{
							output = output + " " + String.valueOf(g.getEdgeTarget(e).getID()) + " " +
							String.valueOf((int)g.getEdgeWeight(e));
							}
						}
						
					}
				//print the line
				out.println(output);
				out.flush();
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

	
	
	
	public static ArrayList<DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>> convertPartitionstoVertexGraphs(String FileName, DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> originalGraph)
	{
		ArrayList<DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>> result = new ArrayList<DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>>();
		try
		{
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			BufferedReader reader = new BufferedReader(new FileReader (FileName));
			int numberOfPartitions = Integer.parseInt((FileName.substring(FileName.lastIndexOf(".")+1)));
			
			//reads the generate file multiple times and creates a graph for each partition
			for (int i=0; i<=numberOfPartitions; i++)
			{
				String line = null;
				DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> subgraph = new DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
				int lineNumber = 0;
				while((line = reader.readLine()) != null)
				{
					lineNumber++;
					if(Integer.parseInt(line)==i)
					{
						subgraph.addVertex(Util.getVertixWithID(originalGraph, lineNumber));
					}
				}
				
				//restart reader for the next pass
				reader.close();
				reader = new BufferedReader(new FileReader (FileName));
				//add subgraph to result if it contains any vertices
				if(subgraph.vertexSet().size()>0)
				{
					//add matching edges to subgraph
					subgraph = MetisInOut.addEdgesToVertexPartition(originalGraph, subgraph);
					result.add(subgraph);
				}
			}
			
			reader.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		
		return result;
	}
	
	
	
	
	
	
	
	
	private static DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> addEdgesToVertexPartition(DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> originalGraph, 
			DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> partition)
	{
		Set<DefaultWeightedEdge> edgesORG = originalGraph.edgeSet();
		Iterator<DefaultWeightedEdge> itORG = edgesORG.iterator();
		
		while(itORG.hasNext())
		{
			DefaultWeightedEdge e = itORG.next();
			
			//if the partition contains both source and target, add the edge to the partition
			if(partition.containsVertex(originalGraph.getEdgeSource(e)) && partition.containsVertex(originalGraph.getEdgeTarget(e)))
			{
				partition.addEdge(originalGraph.getEdgeSource(e), originalGraph.getEdgeTarget(e), e);
			}
		
		}
		
		return partition;
	}
	
	

	public static ArrayList<Integer> getPartitionsOfACrossedEdgeVertex(DefaultWeightedEdge e, ArrayList<DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>> partition,
			DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> originalGraph )
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
	
		try
		{
			
			if(partition.isEmpty())
				throw new Exception("The partition list is empty!");
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
		
			Iterator<DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>> itP = partition.iterator();
			
			//find the partition of the edge source
			while(itP.hasNext())
			{
				DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g = itP.next();
				if(g.containsVertex(originalGraph.getEdgeSource(e)))
				{
					result.add(partition.indexOf(g));
				}
				
			}
			itP = partition.iterator();
			//find the partition of the edge target
			while(itP.hasNext())
			{
				DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g = itP.next();
				if(g.containsVertex(originalGraph.getEdgeTarget(e)))
				{
					result.add(partition.indexOf(g));
				}
				
			}
			
			if(result.isEmpty())
				throw new Exception("Edge coult not be found!");
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;		
		}
			
		
		return result;
	}
	
	

}
