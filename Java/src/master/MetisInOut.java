package master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class MetisInOut 

{
	
	 private final static int avatarModificationConstant = 20;
	 private final static String metisGraphPath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\metis3\\Graphs\\";
	 private final static String kmetisPath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\metis3\\kmetis.exe";
	 private final static String pmetisPath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\metis3\\pmetis.exe";
	
		
	public static boolean convertAvatarGraphToGraphFile(DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g, String FileName)
	{
		try
		{
			if(g.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			FileWriter file = new FileWriter(metisGraphPath + FileName);
			PrintWriter out = new PrintWriter(file);
			//first line in the textfile describes the type of graph for metis
			//x y 11 means a graph with x vertices and y edges with weights on vertices and edges, but only one weight for each is allowed
			String firstline = String.valueOf(g.vertexSet().size());
			firstline = firstline + " " +  String.valueOf(Util.metisEdgeNumberAvatar(g));
			firstline = firstline + " " + 11;
			
			out.println(firstline);
			
			ArrayList<Avatar> list = Util.sortAvatars(g.vertexSet());
			Iterator<Avatar> itList = list.iterator();
			
			//all weights get casted to int here, because metis only supports int weights
			while(itList.hasNext())
			{
				Avatar a = itList.next();
				//the weight of the avatar is added to the output
				String output = String.valueOf(a.getWeight());
				Set<DefaultWeightedEdge> edgesA = g.edgesOf(a);
				Iterator<DefaultWeightedEdge> itEA = edgesA.iterator();
				
					while(itEA.hasNext())
					{
						DefaultWeightedEdge e = itEA.next();
						if(g.getEdgeSource(e).equals(a))
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
	
	public static boolean convertObjectGraphToGraphFile(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g, String FileName)
	{
		try
		{
			if(g.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			
			
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> modifiedGraph = (DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>)g.clone();
			modifiedGraph = Util.CopyObjectGraph(g, modifiedGraph);
			modifiedGraph = convertObjectGraphToBidirectionalGraph(modifiedGraph);
			FileWriter file = new FileWriter(metisGraphPath + FileName);
			PrintWriter out = new PrintWriter(file);
			//first line in the textfile describes the type of graph for metis
			//x y 11 means a graph with x vertices and y edges with weights on vertices and edges, but only one weight for each is allowed
			String firstline = String.valueOf(modifiedGraph.vertexSet().size());
			firstline = firstline + " " +  String.valueOf(Util.metisEdgeNumberObject(modifiedGraph));
			firstline = firstline + " " + 11;
			
			out.println(firstline);
			
			ArrayList<MMVEObject> list = Util.sortObjects(modifiedGraph.vertexSet());
			Iterator<MMVEObject> itList = list.iterator();
			
			//all weights get casted to int here, because metis only supports int weights
			while(itList.hasNext())
			{
				MMVEObject o = itList.next();
				String output = "";
				//the weight of the object is added to the output, it gets modified here to assure that each partition only contains one avatar
				if(o instanceof Avatar)
				{
					output = String.valueOf(o.getWeight() * avatarModificationConstant);
				}
				else
				{
					output = String.valueOf(o.getWeight());
				}
				
				Set<DefaultWeightedEdge> edgesO = modifiedGraph.edgesOf(o);
				Iterator<DefaultWeightedEdge> itEO = edgesO.iterator();
				
					while(itEO.hasNext())
					{
						DefaultWeightedEdge e = itEO.next();
						if(modifiedGraph.getEdgeSource(e).equals(o))
						{
							output = output + " " + String.valueOf(modifiedGraph.getEdgeTarget(e).getID()) + " " +
							String.valueOf((int)modifiedGraph.getEdgeWeight(e));
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
	public static boolean convertAvatarGraphToSimpleGraphFile(DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g, String FileName)
	{
		try
		{
			if(g.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			FileWriter file = new FileWriter(metisGraphPath + FileName);
			PrintWriter out = new PrintWriter(file);
			//first line in the textfile describes the type of graph for metis
			//x y 11 means a graph with x vertices and y edges with weights on vertices and edges, but only one weight for each is allowed
			String firstline = String.valueOf(g.vertexSet().size());
			firstline = firstline + " " +  String.valueOf(Util.metisEdgeNumberAvatar(g));
			firstline = firstline + " " + 1;
			
			out.println(firstline);
			
			ArrayList<Avatar> list = Util.sortAvatars(g.vertexSet());
			Iterator<Avatar> itList = list.iterator();
			
			//all weights get casted to int here, because metis only supports int weights
			while(itList.hasNext())
			{
				Avatar a = itList.next();
				String output = "";
				Set<DefaultWeightedEdge> edgesA = g.edgesOf(a);
				Iterator<DefaultWeightedEdge> itEA = edgesA.iterator();
				
					while(itEA.hasNext())
					{
						DefaultWeightedEdge e = itEA.next();
						if(g.getEdgeSource(e).equals(a))
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
	
		
	public static ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> convertPartitionstoAvatarGraphs(String FileName, DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> originalGraph)
	{
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> result = new ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>>();
		try
		{
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			BufferedReader reader = new BufferedReader(new FileReader (metisGraphPath + FileName));
			int numberOfPartitions = Integer.parseInt((FileName.substring(FileName.lastIndexOf(".")+1)));
			
			//reads the generate file multiple times and creates a graph for each partition
			for (int i=0; i<=numberOfPartitions; i++)
			{
				String line = null;
				DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> subgraph = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
				int lineNumber = 0;
				while((line = reader.readLine()) != null)
				{
					lineNumber++;
					if(Integer.parseInt(line)==i)
					{
						subgraph.addVertex(Util.getAvatarWithID(originalGraph, lineNumber));
					}
				}
				
				//restart reader for the next pass
				reader.close();
				reader = new BufferedReader(new FileReader (metisGraphPath + FileName));
				//add subgraph to result if it contains any vertices
				if(subgraph.vertexSet().size()>0)
				{
					//add matching edges to subgraph
					subgraph = MetisInOut.addEdgesToAvatarPartition(originalGraph, subgraph);
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
	
	public static ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> convertPartitionstoObjectGraphs(String FileName, DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> originalGraph)
	{
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> result = new ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>>();
		try
		{
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty Graph!");
			BufferedReader reader = new BufferedReader(new FileReader (metisGraphPath + FileName));
			int numberOfPartitions = Integer.parseInt((FileName.substring(FileName.lastIndexOf(".")+1)));
			
			//reads the generate file multiple times and creates a graph for each partition
			for (int i=0; i<=numberOfPartitions; i++)
			{
				String line = null;
				DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> subgraph = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
				int lineNumber = 0;
				while((line = reader.readLine()) != null)
				{
					lineNumber++;
					if(Integer.parseInt(line)==i)
					{
						subgraph.addVertex(Util.getObjectWithID(originalGraph, lineNumber));
					}
				}
				
				//restart reader for the next pass
				reader.close();
				reader = new BufferedReader(new FileReader (metisGraphPath + FileName));
				//add subgraph to result if it contains any vertices
				if(subgraph.vertexSet().size()>0)
				{
					//add matching edges to subgraph
					subgraph = MetisInOut.addEdgesToObjectPartition(originalGraph, subgraph);
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
	
	
	
	public static ArrayList<DefaultWeightedEdge> getCrossedEdgesAvatar(ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> partition, 
			DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> originalGraph)
	{
		
		ArrayList<DefaultWeightedEdge> crossedEdges = new ArrayList<DefaultWeightedEdge>();
		try
		{
			Set<DefaultWeightedEdge> edgesORG = originalGraph.edgeSet();
			if(edgesORG.isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
			if(partition.isEmpty())
				throw new Exception("The partitions are empty");
			Iterator<DefaultWeightedEdge> itORG = edgesORG.iterator();
			
				while(itORG.hasNext())
				{
					DefaultWeightedEdge e = itORG.next();
					boolean crossed = true;
					Iterator<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> itPART = partition.iterator();
					
						while(itPART.hasNext())
						{
							DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> h = itPART.next();
							if(h.containsEdge(e))
							{
								crossed = false;
							}
						}
					
					if(crossed==true)
					{
						crossedEdges.add(e);
					}
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		return crossedEdges;
	}
	
	public static ArrayList<DefaultWeightedEdge> getCrossedEdgesObject(ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partition, 
			DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> originalGraph)
	{

		ArrayList<DefaultWeightedEdge> crossedEdges = new ArrayList<DefaultWeightedEdge>();
		try
		{
			Set<DefaultWeightedEdge> edgesORG = originalGraph.edgeSet();
			if(edgesORG.isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
			if(partition.isEmpty())
				throw new Exception("The partitions are empty");
			Iterator<DefaultWeightedEdge> itORG = edgesORG.iterator();
			
				while(itORG.hasNext())
				{
					DefaultWeightedEdge e = itORG.next();
					boolean crossed = true;
					Iterator<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> itPART = partition.iterator();
					
						while(itPART.hasNext())
						{
							DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> h = itPART.next();
							if(h.containsEdge(e))
							{
								crossed = false;
							}
						}
					
					if(crossed==true)
					{
						crossedEdges.add(e);
					}
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		return crossedEdges;
	}
	
	public static ArrayList<DefaultWeightedEdge> getCrossedEdgesBetweenObjectGraphs(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> constructedGraph, DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> originalGraph)
	{
		ArrayList<DefaultWeightedEdge> crossedEdges = new ArrayList<DefaultWeightedEdge>();
		try
		{
			Set<DefaultWeightedEdge> edgesORG = originalGraph.edgeSet();
			if(edgesORG.isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
			if(constructedGraph.vertexSet().isEmpty())
				throw new Exception("The partitions are empty");
			Iterator<DefaultWeightedEdge> itORG = edgesORG.iterator();
			
				while(itORG.hasNext())
				{
					DefaultWeightedEdge e = itORG.next();
					boolean crossed = true;
					
					if(constructedGraph.containsEdge(e))
					{
						crossed = false;
					}						
					
					if(crossed==true)
					{
						crossedEdges.add(e);
					}
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		return crossedEdges;
	}
	
	
		
	private static DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> addEdgesToAvatarPartition(DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> originalGraph, 
			DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> partition)
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
	
	private static DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> addEdgesToObjectPartition(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> originalGraph, 
			DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> partition)
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

	
	
	public static ArrayList<Integer> getPartitionsOfACrossedEdgeAvatar(DefaultWeightedEdge e, ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> partition,
			DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> originalGraph )
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		try
		{
			
			if(partition.isEmpty())
				throw new Exception("The partition list is empty!");
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
		
			Iterator<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> itP = partition.iterator();
			
			//find the partition of the edge source
			while(itP.hasNext())
			{
				DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = itP.next();
				if(g.containsVertex(originalGraph.getEdgeSource(e)))
				{
					result.add(partition.indexOf(g));
				}
				
			}
			itP = partition.iterator();
			//find the partition of the edge target
			while(itP.hasNext())
			{
				DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = itP.next();
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
	
	//calls metis to partition the graph and constructs partitions from the resulting graph file
	public static ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> executeAvatarPartition(String FileName,
			int partitionnumber, DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> interestGraph)
	{
		
			final String metispath;
		
			if (partitionnumber > 8)
			{
				metispath  = kmetisPath;
			}				
			else
			{
				metispath  = pmetisPath;
			}
			String filePath = metisGraphPath + FileName;
		
			
			
			ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> partitions = null;
				
			try
			{
				if(interestGraph.vertexSet().isEmpty())
					throw new GraphEmptyException("The graph you are trying to use is empty!");
				
				if(partitionnumber==0)
					throw new Exception("It is impossible to partition a graph in zero pieces!");
			
				Runtime rt = Runtime.getRuntime();
				
				
				String[] command = new String[3];
				command[0] = metispath;
				command[1] = filePath;
				command[2] = String.valueOf(partitionnumber);
				
				
				Process metis = rt.exec(command);
				
				//Process metis = rt.exec(metispath + " "  + filePath + " " + String.valueOf(partitionnumber));
				
				//get the output of metis and print it on the console
				BufferedReader in = new BufferedReader(new InputStreamReader(metis.getInputStream()));
				String str = null;
				
				 while ((str = in.readLine()) != null)
				 {
					 System.out.println(str);
				 }
				 in.close();
		
				//this causes the java program to wait until metis is done
				//if this is missing, the partition file may not be found
				 int exitvalue = metis.waitFor();
				System.out.println("Exitvalue :" + exitvalue);
				
				
				File graphfile = new File (FileName + ".part." + partitionnumber);
				
				
				partitions = MetisInOut.convertPartitionstoAvatarGraphs(FileName + ".part." + 
						partitionnumber, interestGraph);
				
				//delete file because we dont need it anymore
				if(graphfile.exists())
				{
					graphfile.delete();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
			
			return partitions;
	}
	
	//calls metis to partition the graph and constructs partitions from the resulting graph file
	public static ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> executeObjectPartition(String FileName,
			int partitionnumber, DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> interestGraph)
	{
		
			final String metispath;
		
			if (partitionnumber > 8)
			{
				metispath  = kmetisPath;
			}				
			else
			{
				metispath  = pmetisPath;
			}
			
			String filePath = metisGraphPath + FileName;
			ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = null;
				
			try
			{
				if(interestGraph.vertexSet().isEmpty())
					throw new GraphEmptyException("The graph you are trying to use is empty!");
				
				if(partitionnumber==0)
					throw new Exception("It is impossible to partition a graph in zero pieces!");
			
				Runtime rt = Runtime.getRuntime();
				
				String[] command = new String[3];
				command[0] = metispath;
				command[1] = filePath;
				command[2] = String.valueOf(partitionnumber);
				
				
				Process metis = rt.exec(command);
				
				//get the output of metis and print it on the console
				BufferedReader in = new BufferedReader(new InputStreamReader(metis.getInputStream()));
				String str = null;
				
				 while ((str = in.readLine()) != null)
				 {
					 System.out.println(str);
				 }
				 in.close();
		
				//this causes the java program to wait until metis is done
				//if this is missing, the partition file may not be found
				int exitvalue = metis.waitFor();
				System.out.println("Exitvalue :" + exitvalue);
				
				File graphfile = new File (filePath + ".part." + partitionnumber);
				
				
				partitions = MetisInOut.convertPartitionstoObjectGraphs(FileName + ".part." + 
						partitionnumber, interestGraph);
				
				//delete file because we dont need it anymore
				if(graphfile.exists())
				{
					graphfile.delete();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
			
			return partitions;
	}
	
	private static DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> convertObjectGraphToBidirectionalGraph(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> originalGraph)
	{
		Set<MMVEObject> objects = originalGraph.vertexSet();
		Iterator<MMVEObject> itObjects = objects.iterator();
		
		while(itObjects.hasNext())
		{
			MMVEObject o = itObjects.next();
			Set<DefaultWeightedEdge> edges = originalGraph.edgesOf(o);
			Iterator<DefaultWeightedEdge> itEdges = edges.iterator();
			
			while(itEdges.hasNext())
			{
				DefaultWeightedEdge e = itEdges.next();
				if(!originalGraph.containsEdge(originalGraph.getEdgeSource(e), originalGraph.getEdgeTarget(e)))
				{
					originalGraph.addEdge(originalGraph.getEdgeSource(e), originalGraph.getEdgeTarget(e));
				}
				if(!originalGraph.containsEdge(originalGraph.getEdgeTarget(e), originalGraph.getEdgeSource(e)))
				{
					originalGraph.addEdge(originalGraph.getEdgeTarget(e), originalGraph.getEdgeSource(e));
				}
			}
		}
		
		return originalGraph;
	}


	
	

}
