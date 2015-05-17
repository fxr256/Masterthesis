package master;

import java.util.ArrayList;
import java.util.Iterator;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class InterestGraphConstruction 
{
	//adds edges to the graph, currently only if the aoi intersects. 
	public static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> constructAvatarEdges(DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph)
	{
		return doAvatarEdgeConstruction(originalGraph,1,1);
	}
	
	public static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> constructAvatarEdges(DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph, 
			double auraFactor, double interestedInFactor)
	{		
		return doAvatarEdgeConstruction(originalGraph, auraFactor, interestedInFactor);
	}
	
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> constructObjectEdges (DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph)
	{
		return doObjectEdgeConstruction(originalGraph,1,1);
	}
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> constructObjectEdges (DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph,
			double auraFactor, double interestedInFactor)
	{
		return doObjectEdgeConstruction(originalGraph,auraFactor,interestedInFactor);
	}
	
	
	
	public static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> constructAvatarInterestGraphFromPartitions
	(ArrayList<DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>> partitions)
	{
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		try
		{
			if(partitions.isEmpty())
				throw new Exception("The partition list is empty!");
			
			Iterator<DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>> itPart = partitions.iterator();
			while(itPart.hasNext())
			{
				DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> partition = itPart.next();
				if(!partition.vertexSet().isEmpty())
				{
					//add all avatars of this subgraph to the interestGraph
					interestGraph = Util.addAllAvatarsFromSet(interestGraph, partition.vertexSet());
				}
				
			}
			
			//construct edges for the interest graph
			//interestGraph = InterestGraphConstruction.constructAvatarEdges(interestGraph);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return interestGraph;
	}
	
	
	
	//does not construct any edges!
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> constructObjectInterestGraphFromPartitions
	(ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions)
	{
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		try
		{
			if(partitions.isEmpty())
				throw new Exception("The partition list is empty!");
			
			Iterator<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> itPart = partitions.iterator();
			while(itPart.hasNext())
			{
				DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition = itPart.next();
				if(!partition.vertexSet().isEmpty())
				{
					//add all avatars of this subgraph to the interestGraph
					interestGraph = Util.addAllObjectsFromSet(interestGraph, partition.vertexSet());
				}
				
			}
			//commented out for testing
			//construct edges for the interest graph
			//interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return interestGraph;
	}
	
	public static ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> getPartitionsFromPeerList(ArrayList<Peer> peers)
	{
		ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions = new ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>>();
		try
		{
			if(peers.isEmpty())
				throw new Exception("The peer list is empty!");
			
			Iterator<Peer> itPeers = peers.iterator();
			while(itPeers.hasNext())
			{
				Peer p = itPeers.next();
				if(p.getManagedObjects()!=null)
				{
					partitions.add(p.getManagedObjects());
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return partitions;
	}
	
	//adds the information of the interactions of a certain period to the interest graph
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> constructInterestGraphEdgesFromPastInteractions(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph, ArrayList<Interaction> regularInteractions, 
			ArrayList<Interaction> crossedInteractions, double interactionFactor)
	{
		try
		{
			if(interestGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("The graph you are trying to use is empty!");
			
			Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
			while(itInterestGraph.hasNext())
			{
				MMVEObject objectToProcess = itInterestGraph.next();
				interestGraph = addInteractionsForThisObject(interestGraph, objectToProcess, regularInteractions, crossedInteractions, interactionFactor);
			}
			
			return interestGraph;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addInteractionsForThisObject(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph,
			MMVEObject objectToProcess, ArrayList<Interaction> regularInteractions,ArrayList<Interaction> crossedInteractions, double interactionFactor) 
	{
		Iterator<Interaction> itRegIterations = regularInteractions.iterator();
		
		//first process the regular interactions
		while(itRegIterations.hasNext())
		{
			Interaction currentInteraction = itRegIterations.next();
			
			if(currentInteraction.getSource().equals(objectToProcess))
			{
				//may be not necessary, because the regualar interaction should have an edge
				if(interestGraph.containsEdge(objectToProcess, currentInteraction.getTarget()))
				{
					DefaultWeightedEdge edgeToModify = interestGraph.getEdge(objectToProcess, currentInteraction.getTarget());
					interestGraph.setEdgeWeight(edgeToModify, interestGraph.getEdgeWeight(edgeToModify) + interactionFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				else
				{
					DefaultWeightedEdge addedEdge = interestGraph.addEdge(objectToProcess, currentInteraction.getTarget());
					interestGraph.setEdgeWeight(addedEdge, interestGraph.getEdgeWeight(addedEdge) + interactionFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				
			}
		}
		
		Iterator<Interaction> itCrossedInteractions = crossedInteractions.iterator();
		//then process crossed interactions
		while(itCrossedInteractions.hasNext())
		{
			Interaction currentInteraction = itCrossedInteractions.next();
			
			if(currentInteraction.getSource().equals(objectToProcess))
			{
				//may be not necessary, because the regualar interaction should have an edge
				if(interestGraph.containsEdge(objectToProcess, currentInteraction.getTarget()))
				{
					DefaultWeightedEdge edgeToModify = interestGraph.getEdge(objectToProcess, currentInteraction.getTarget());
					interestGraph.setEdgeWeight(edgeToModify, interestGraph.getEdgeWeight(edgeToModify) + interactionFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				else
				{
					DefaultWeightedEdge addedEdge = interestGraph.addEdge(objectToProcess, currentInteraction.getTarget());
					interestGraph.setEdgeWeight(addedEdge, interactionFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				
			}
			
		}
		
		return interestGraph;
	}

	//iterates through all the avatars within a graph and returns the avatars whose aoi intersects with the one of the supplied avatar
	private static ArrayList<Avatar> getAllAvatarsWithIntersectingAoIs(Avatar a, DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph)
	{
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		Iterator<Avatar> itOrg = originalGraph.vertexSet().iterator();
		
		while(itOrg.hasNext())
		{
			Avatar b = itOrg.next();
			//if the aois intersect add to list
			if(!a.equals(b))
			{
				if(a.getAoi().intersects(b.getAoi()))
				{
					list.add(b);
				}
				
			}			
		}
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
	//changed to public because the interaction simulator needs this.
	public static ArrayList<MMVEObject> getAllObjectsWithIntersectingAoIs(MMVEObject o, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph)
	{
		//cast object to avatar, because only avatars have AoIs
		Avatar a = (Avatar)o;
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		Iterator<MMVEObject> itOrg = originalGraph.vertexSet().iterator();
		
		while(itOrg.hasNext())
		{
			MMVEObject p = itOrg.next();
			
			//if both objects are avatars, check for matching aois
			if (p instanceof Avatar)
			{
				Avatar b = (Avatar)p;
				//if the aois intersect add to list
				if(!a.equals(b))
				{
					if(a.getAoi().intersects(b.getAoi()))
					{
						list.add(b);
					}
					
				}
							
			}
			else
			{
				//p is an MMVEObject, so only add it if As AoI encompasses it
			
				if(!o.equals(p))
				{
					if(a.getAoi().intersectes(p.getPosition()))
					{
						list.add(p);
					}
				}
			}
			
			
		}
		
		if(list.isEmpty())
			return null;
		
		return list;
		
	}
	//overloaded to work with the geographic approach
	public static ArrayList<MMVEObject> getAllObjectsWithIntersectingAoIs(MMVEObject o, ArrayList<Avatar> avatarList, ArrayList<MMVEObject> objectList)
	{
		//cast object to avatar, because only avatars have AoIs
		Avatar a = (Avatar)o;
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		Iterator<Avatar> itAvatars = avatarList.iterator();
		
		while(itAvatars.hasNext())
		{
			MMVEObject p = itAvatars.next();
			
			//if both objects are avatars, check for matching aois
			if (p instanceof Avatar)
			{
				Avatar b = (Avatar)p;
				//if the aois intersect add to list
				if(!a.equals(b))
				{
					if(a.getAoi().intersects(b.getAoi()))
					{
						list.add(b);
					}
					
				}
							
			}
			else
			{
				//p is an MMVEObject, so only add it if As AoI encompasses it
			
				if(!o.equals(p))
				{
					if(a.getAoi().intersectes(p.getPosition()))
					{
						list.add(p);
					}
				}
			}
		
		}
		
		Iterator<MMVEObject> itObjects = objectList.iterator();
		while(itObjects.hasNext())
		{
			MMVEObject p = itObjects.next();
			
			//if both objects are avatars, check for matching aois
			if (p instanceof Avatar)
			{
				Avatar b = (Avatar)p;
				//if the aois intersect add to list
				if(!a.equals(b))
				{
					if(a.getAoi().intersects(b.getAoi()))
					{
						list.add(b);
					}
					
				}
							
			}
			else
			{
				//p is an MMVEObject, so only add it if As AoI encompasses it
			
				if(!o.equals(p))
				{
					if(a.getAoi().intersectes(p.getPosition()))
					{
						list.add(p);
					}
				}
			}
		
		}
		
		if(list.isEmpty())
			return null;
		
		return list;
		
	}
	
	private static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> doAvatarEdgeConstruction(
			DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph,
			double auraFactor, double interestedInFactor) 
	{
		try
		{
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("The graph you are trying to use is empty!");
			
			Iterator<Avatar> itOrg = originalGraph.vertexSet().iterator();
			
			while(itOrg.hasNext())
			{
				Avatar a = itOrg.next();
				//find all avatars with intersecting aois
				ArrayList<Avatar> inAoI = getAllAvatarsWithIntersectingAoIs(a, originalGraph);
				if(inAoI != null)
				{
					//add edges if the AoI is not null
					originalGraph = Util.addAllEdgesAvatar(a, inAoI, originalGraph, auraFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				//add the interestedIn edges
				originalGraph = addInterestedInEdgesAvatar(a, originalGraph, interestedInFactor);
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return originalGraph;
	}
	
	private static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> doObjectEdgeConstruction(
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph,
			double auraFactor, double interestedInFactor) 
	{

		try
		{
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("The graph you are trying to use is empty!");
			
			Iterator<MMVEObject> itOrg = originalGraph.vertexSet().iterator();
			
			while(itOrg.hasNext())
			{
				MMVEObject o = itOrg.next();
				//check whether the object is an Avatar
				if (o instanceof Avatar)
				{
					//find all Objects with intersecting aois
					ArrayList<MMVEObject> inAoI = getAllObjectsWithIntersectingAoIs(o, originalGraph);
					if(inAoI != null)
					{
						//add edges if the AoI is not null
						originalGraph = Util.addAllEdgesObject(o, inAoI, originalGraph, auraFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
					}
					
				}
				
				//now that all the AoI edges have been created, we add the edges from the interested in list
				originalGraph = addInterestedInEdges(o, originalGraph, interestedInFactor);
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return originalGraph;
	}
	
	private static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addInterestedInEdges(MMVEObject objectToWorkWith,
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph, double interestedInFactor) 
	{
		if(!objectToWorkWith.getInterestedIn().isEmpty())
		{
			Iterator<MMVEObject> itInterestedIn = objectToWorkWith.getInterestedIn().iterator();
			while(itInterestedIn.hasNext())
			{
				MMVEObject target = itInterestedIn.next();
				//if the edge already existes, increase its weight by one, else add a new edge
				if(originalGraph.containsEdge(objectToWorkWith, target))
				{
					DefaultWeightedEdge edgeToModify = originalGraph.getEdge(objectToWorkWith, target);
					originalGraph.setEdgeWeight(edgeToModify, originalGraph.getEdgeWeight(edgeToModify)+ interestedInFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				else
				{
					DefaultWeightedEdge e = originalGraph.addEdge(objectToWorkWith, target);
					originalGraph.setEdgeWeight(e, interestedInFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				
			}
		}
		return originalGraph;
	}
	
	private static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> addInterestedInEdgesAvatar(Avatar avatarToWorkWith,
			DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph, double interestedInFactor) 
	{
		if(!avatarToWorkWith.getInterestedIn().isEmpty())
		{
			Iterator<MMVEObject> itInterestedIn = avatarToWorkWith.getInterestedIn().iterator();
			while(itInterestedIn.hasNext())
			{
				Avatar target = (Avatar)itInterestedIn.next();
				//if the edge already existes, increase its weight by one, else add a new edge
				if(originalGraph.containsEdge(avatarToWorkWith, target))
				{
					DefaultWeightedEdge edgeToModify = originalGraph.getEdge(avatarToWorkWith, target);
					originalGraph.setEdgeWeight(edgeToModify, 
							originalGraph.getEdgeWeight(edgeToModify) + interestedInFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				else
				{
					DefaultWeightedEdge e = originalGraph.addEdge(avatarToWorkWith, target);
					originalGraph.setEdgeWeight(e, interestedInFactor * WeightedGraph.DEFAULT_EDGE_WEIGHT);
				}
				
			}
		}
		return originalGraph;
	}
	
}
