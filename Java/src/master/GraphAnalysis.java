package master;

import java.util.ArrayList;
import java.util.Iterator;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class GraphAnalysis 
{
	
	public static void analyseAvatarGraph (DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> Graph)
	{
		//look for isolated vertices
		Iterator<Avatar> itG = Graph.vertexSet().iterator();
		ArrayList<Avatar> isolated = new ArrayList<Avatar>();
		int totalWeightOfThisPartition = 0;
		
		while(itG.hasNext())
		{
			Avatar a = itG.next();
			totalWeightOfThisPartition = totalWeightOfThisPartition + a.getWeight();
			if(Graph.edgesOf(a).isEmpty())
			{
				isolated.add(a);
				System.out.println("Node " + a.getID() + " is isolated!");
			}
			else
			{
				Iterator<DefaultWeightedEdge> itE = Graph.edgesOf(a).iterator();
				String neighbors = "";
				while(itE.hasNext())
				{
					DefaultWeightedEdge e = itE.next();
					if(Graph.getEdgeSource(e).equals(a))
					{
						if(!neighbors.contains(String.valueOf(Graph.getEdgeTarget(e).getID())))
							neighbors = neighbors +  " " + Graph.getEdgeTarget(e).getID() + 
							" (weight: " + Graph.getEdgeWeight(e) + ")";
					}
										
					
				}
				
				if(neighbors.isEmpty())
				{
					System.out.println("Node " + a.getID() +  " is an avatar and does not have any outgoing edges!");
				}
				else
				{
					System.out.println("Node " + a.getID() +  " is an avatar and is connected to: " + neighbors);
				}
				
			}
		}
		System.out.println("This partition contains " + Graph.vertexSet().size() + " nodes");
		System.out.println("There are " + isolated.size() + " isolated nodes in this partition");
		System.out.println("The total weight of this partition is: " + totalWeightOfThisPartition);
	}
	
	
	public static void analyseObjectGraph (DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> Graph)
	{

		//look for isolated vertices
		Iterator<MMVEObject> itG = Graph.vertexSet().iterator();
		ArrayList<MMVEObject> isolated = new ArrayList<MMVEObject>();
		int totalWeightOfThisPartition = 0;
		
		while(itG.hasNext())
		{
			MMVEObject o = itG.next();
			totalWeightOfThisPartition = totalWeightOfThisPartition + o.getWeight();
			if(Graph.edgesOf(o).isEmpty())
			{
				isolated.add(o);
				if(o instanceof Avatar)
					System.out.println("Node " + o.getID() + " is an Avatar and is isolated!");
				else
					System.out.println("Node " + o.getID() + " is an Object and is isolated!");
			}
			else
			{
				Iterator<DefaultWeightedEdge> itE = Graph.edgesOf(o).iterator();
				String neighbors = "";
				while(itE.hasNext())
				{
					DefaultWeightedEdge e = itE.next();
					if(Graph.getEdgeSource(e).equals(o))
					{
						if(!neighbors.contains(String.valueOf(Graph.getEdgeTarget(e).getID())))
							neighbors = neighbors +  " " + Graph.getEdgeTarget(e).getID() + 
							" (weight: " + Graph.getEdgeWeight(e) + ")";
					}
										
					
				}
				if(neighbors.isEmpty())
				{
					if(o instanceof Avatar)
						System.out.println("Node " + o.getID() + " is an Avatar and does not have any outgoing edges!");
					else
						System.out.println("Node " + o.getID() + " is an Object and does not have any outgoing edges!");	
				}
				else
				{
					if(o instanceof Avatar)
						System.out.println("Node " + o.getID() + " is an Avatar and is connected to: " + neighbors);
					else
						System.out.println("Node " + o.getID() + " is an Object and is connected to: " + neighbors);					
				}
				
			}
		}
		System.out.println("This partition contains " + Graph.vertexSet().size() + " nodes");
		System.out.println("There are " + isolated.size() + " isolated nodes in this partition");
		System.out.println("The total weight of this partition is: " + totalWeightOfThisPartition);
	}

	
	public static void analysePeerObjectGraphList(ArrayList<Peer> peers)
	{
		Iterator<Peer> itPeers = peers.iterator();
		while(itPeers.hasNext())
		{
			Peer p = itPeers.next();
			analyseObjectGraph(p.getManagedObjects());
		}
	}
	
	public static void analyseCrossedEdges(ArrayList<DefaultWeightedEdge> crossedEdges, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph)
	{
		Iterator<DefaultWeightedEdge> itCrossed = crossedEdges.iterator();
		
		while(itCrossed.hasNext())
		{
			DefaultWeightedEdge e = itCrossed.next();
			System.out.println("There is an crossed edge between node " + interestGraph.getEdgeSource(e).getID() + " and node " + interestGraph.getEdgeTarget(e).getID()
					+ " with weight " + interestGraph.getEdgeWeight(e));
		}
	}
	
	public static void analyseObjectGraphEdgeSet( DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph)
	{
		Iterator<DefaultWeightedEdge> itEdges = interestGraph.edgeSet().iterator();
		while(itEdges.hasNext())
		{
			DefaultWeightedEdge e = itEdges.next();
			System.out.println("There is a edge between " + interestGraph.getEdgeSource(e).getID() + " and " + interestGraph.getEdgeTarget(e).getID()
			+ " with weight " + interestGraph.getEdgeWeight(e));
		}
	}
	
	public static boolean allPartitionsContainOwnAvatar(ArrayList<Peer> peers)
	{
		
		Iterator<Peer> itPeer = peers.iterator();
		
		while(itPeer.hasNext())
		{
			Peer p = itPeer.next();
			if(!p.getManagedObjects().containsVertex(p.getOwnAvatar()))
			{
				return false;
			}
				
		}
		
		
		return true;
	}
	
	public static boolean allPartitionsHaveTheSameWeightObjectGraph(ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions)
	{
		Iterator<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> itPart = partitions.iterator();
		int lastPartitionWeight = getTotalGraphWeightObject(partitions.get(0));
		
		while(itPart.hasNext())
		{
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> subgraph = itPart.next();
			if(lastPartitionWeight != getTotalGraphWeightObject(subgraph))
			{
				return false;
			}
				
			
		}
		
		return true;
	}
	
	public static int getTotalGraphWeightObject(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> subGraph)
	{
		int totalWeight = 0;
		Iterator<MMVEObject> itGraph = subGraph.vertexSet().iterator();
		while(itGraph.hasNext())
		{
			totalWeight = totalWeight + itGraph.next().getWeight(); 
			
		}
		
		return totalWeight;
	}
	
	public static boolean allPartitionsHaveTheSameWeightAvatarGraph(ArrayList<DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>> partitions)
	{
		Iterator<DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>> itPart = partitions.iterator();
		int lastPartitionWeight = getTotalGraphWeightAvatar(partitions.get(0));
		
		while(itPart.hasNext())
		{
			DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> subgraph = itPart.next();
			if(lastPartitionWeight != getTotalGraphWeightAvatar(subgraph))
			{
				return false;
			}
				
			
		}
		
		return true;
	}
	
	public static int getTotalGraphWeightAvatar(DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> subGraph)
	{
		int totalWeight = 0;
		Iterator<Avatar> itGraph = subGraph.vertexSet().iterator();
		while(itGraph.hasNext())
		{
			totalWeight = totalWeight + itGraph.next().getWeight(); 
			
		}
		
		return totalWeight;
	}
	
	public static void analyseInterestedInObjectList(ArrayList<MMVEObject> input)
	{
		Iterator<MMVEObject> itInput = input.iterator();
		while(itInput.hasNext())
		{
			MMVEObject o = itInput.next();
			String output = "";
			if(o.getInterestedIn().isEmpty())
			{
				output = "Object " + o.getID() + " is not interested in other object!";
			}
			else
			{
				Iterator<MMVEObject> itInterested = o.getInterestedIn().iterator();
				String interestingObjects = "";
				while(itInterested.hasNext())
				{
					interestingObjects = interestingObjects + " " + itInterested.next().getID();
				}
				output = "Object " + o.getID() + " is interested in: " + interestingObjects;
				
			}
			
			System.out.println(output);
			
		}
	}
	
	public static void analyseInterestedInObjectGraph(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> inputGraph)
	{
		Iterator<MMVEObject> itInput = inputGraph.vertexSet().iterator();
		while(itInput.hasNext())
		{
			MMVEObject o = itInput.next();
			String output = "";
			if(o.getInterestedIn().isEmpty())
			{
				output = "Object " + o.getID() + " is not interested in other object!";
			}
			else
			{
				Iterator<MMVEObject> itInterested = o.getInterestedIn().iterator();
				String interestingObjects = "";
				while(itInterested.hasNext())
				{
					interestingObjects = interestingObjects + " " + itInterested.next().getID();
				}
				output = "Object " + o.getID() + " is interested in: " + interestingObjects;
				
			}
			
			System.out.println(output);
			
		}
	}

}
