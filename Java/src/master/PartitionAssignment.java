package master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class PartitionAssignment 
{
	
	public static Peer assignAvatarPartitiontoPeer (DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph, 
			Peer target, boolean homogenPeers)
	{
		
		try
		{
			
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("The graph you are trying to use is empty!");
			
			//transform graph to object graph
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition = transformAvatarGraphToObjectGraph(originalGraph);
			//if we got homogenous peers, we always assign the partition to the graph, even if it gets overloaded
			if(homogenPeers)
			{
				target.setManagedObjects(partition);
				//if the weight of all avatars within the partition is bigger than the peer's capacity
				//the peer will be marked as overloaded.
				if(getObjectPartitionWeight(partition) > target.getCapacity())
				{
					target.setOverloaded(true);
				}
				else
				{
					target.setOverloaded(false);
				}
				
			}
			//if we got heterogenous peers, we will only assign the partition, if the peer can handle it
			else
			{
				if(getObjectPartitionWeight(partition) < target.getCapacity())
				{
					target.setManagedObjects(partition);
				}
				//we should be doing something smart here, currently only return null
				else
				{
					throw new Exception("This peer can not handle this workload!");
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return target;
	}
	
	public static Peer assignObjectPartitiontoPeer (DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition, 
			Peer target, boolean homogenPeers)
	{
		try
		{
			if(partition.vertexSet().isEmpty())
				throw new GraphEmptyException("The graph you are trying to use is empty!");
			
			//if we got homogenous peers, we always assign the partition to the graph, even if it gets overloaded
			if(homogenPeers)
			{
				target.setManagedObjects(partition);
				//if the weight of all avatars within the partition is bigger than the peer's capacity
				//the peer will be marked as overloaded.
				if(getObjectPartitionWeight(partition) > target.getCapacity())
				{
					target.setOverloaded(true);
				}
				else
				{
					target.setOverloaded(false);
				}
				
			}
			//if we got heterogenous peers, we will only assign the partition, if the peer can handle it
			else
			{
				if(getObjectPartitionWeight(partition) < target.getCapacity())
				{
					target.setManagedObjects(partition);
				}
				//we should be doing something smart here, currently only return null
				else
				{
					throw new Exception("This peer can not handle this workload!");
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return target;
	}
	
	public static ArrayList<Peer> assignPartitionListToPeerList(ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions, ArrayList<Peer> peers, 
			boolean homogenPeers)
	{
		ArrayList<Peer> result = new ArrayList<Peer>();
		try
		{
			if(peers.isEmpty())
				throw new Exception("The peer list is empty!");
			if(partitions.isEmpty())
				throw new Exception("The partition list is empty!");
			if(partitions.size() > peers.size())
				throw new Exception("There are not enough peers for the partitions");
			
			Iterator<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> itPart = partitions.iterator();
			//add each partition to one peer in the peer list
			Iterator<Peer> itPeers = peers.iterator();
			ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> done =  new ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>>();
			ArrayList<Peer> servedPeers = new ArrayList<Peer>();
			
			//check first whether we can find a partition which contains the own peers avatar
			while(itPeers.hasNext())
			{
				Peer p = itPeers.next();
				DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition = findPartitionContainingOwnAvatar(p, partitions);
				//if the partition is not null and was not already assigned, assign it to the peer
				if(partition!=null && done.contains(partition)==false)
				{
					p = assignObjectPartitiontoPeer(partition, p, homogenPeers);
					if(p!=null)
					{
						done.add(partition);
						servedPeers.add(p);
						result.add(p);
					}
					
				}
			}
			
			
			//now iterate through all the partitions and assign them if they were not already assigned
			while(itPart.hasNext())
			{
				DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition = itPart.next();
				//check whether partition was already assigned
				if(done.contains(partition)==false)
				{
					//refresh peer iterator
					itPeers = peers.iterator();
					while(itPeers.hasNext())
					{
						Peer p = itPeers.next();
						//check whether the peer was already served
						if(servedPeers.contains(p)==false && done.contains(partition)==false)
						{
							p = assignObjectPartitiontoPeer(partition, p, homogenPeers);
							if(p!=null)
							{
								done.add(partition);
								servedPeers.add(p);
								result.add(p);
							}
								
						}
					}
					
				}				
				
			}
			
			//to make sure we have a constant amount of peers in each iteration
			//we traverse all peers again and check for any missing ones
			itPeers = peers.iterator();
			while(itPeers.hasNext())
			{
				Peer p = itPeers.next();
				if(!result.contains(p))
				{
					p.setManagedObjects(new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class));
					result.add(p);
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
	
	
	
	//this function tries to refine partitions so that each peer manages his own avatar
	public static ArrayList<Peer> refinePartitions(ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions, ArrayList<Peer> peers, 
			boolean homogenPeers)
	{
		try
		{
			if(partitions.isEmpty())
				throw new Exception("The partitionlist is empty!");
			if(peers.isEmpty())
				throw new Exception("The peer list is empty!");
			
			//find peers whose partition don't contain their own avatar
			ArrayList<Peer> peersToRefine = getPeersWithPartitionsWithoutOwnAvatar(peers);
			
			if(peersToRefine.isEmpty())
			{
				//nothing to do here, move along
				return peers;
			}
			else
			{
				//Iterate through all peers without their own peer in their partition and try to fix it
				Iterator<Peer> itRefine = peersToRefine.iterator();
				while(itRefine.hasNext())
				{
					Peer p = itRefine.next();
					//find location of the searched avatar
					Peer locationOfSearchedAvatar = findPeerManagingSearchedAvatar(peers, p.getOwnAvatar());
					if(locationOfSearchedAvatar!=null && !p.equals(locationOfSearchedAvatar))
					{
						//try to swap an object for the searched avatar
						peers = tryObjectSwapForSearchedAvatar(p, locationOfSearchedAvatar, peers);
						
					}
					
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
			
		
		return peers;
	}
	
	
	


	//returns the sum of all avatar's weights of one partition
	public static int getObjectPartitionWeight(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition)
	{
		int weight = 0;
		Iterator<MMVEObject> itPart = partition.vertexSet().iterator();
		
		while(itPart.hasNext())
		{
			weight = weight + itPart.next().getWeight();			
		}		
		
		return weight;
	}
	
	
	//transforms an avatar graph into an objectgraph
	private static DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> transformAvatarGraphToObjectGraph(DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> source)
	{	
		
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> target = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//add vertices
		Set<Avatar> vertexS = source.vertexSet();
		Iterator<Avatar> itS = vertexS.iterator();
		Avatar v = null;
		
		while (itS.hasNext())
		{
			v = itS.next();
			target.addVertex(v);
		}
		
		//add edges
		Set<DefaultWeightedEdge> edgeS = source.edgeSet();
		Iterator<DefaultWeightedEdge> itES = edgeS.iterator();
		DefaultWeightedEdge e = null;
		
		while(itES.hasNext())
		{
			e = itES.next();
			Assert.assertNotNull(e);
			target.addEdge(source.getEdgeSource(e), source.getEdgeTarget(e), e);
		}
		
		return target;
	}
	
	private static DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> findPartitionContainingOwnAvatar(Peer peer, ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions)
	{
				
		Iterator<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> itPartitions = partitions.iterator();
		
		while(itPartitions.hasNext())
		{
			DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> subGraph = itPartitions.next();
			//check whether this partition contains the peer's avatar
			if(Util.getAvatarInObjectGraphWithID(subGraph, peer.getID())!=null)
			{
				return subGraph;
			}
		}
		
		return null;
	}
	
	private static ArrayList<Peer> getPeersWithPartitionsWithoutOwnAvatar(ArrayList<Peer> peers)
	{
		ArrayList<Peer> ownAvatarNotInPartition = new ArrayList<Peer>();
		Iterator<Peer> itPeers = peers.iterator();
		
		while(itPeers.hasNext())
		{
			Peer p = itPeers.next();
			if(!p.getManagedObjects().containsVertex(p.getOwnAvatar()))
			{
				ownAvatarNotInPartition.add(p);
			}
		}
		return ownAvatarNotInPartition;
	}
	
	private static Peer findPeerManagingSearchedAvatar(ArrayList<Peer> peers, Avatar searchedAvatar)
	{
		Iterator<Peer> itPeers = peers.iterator();
		
		while(itPeers.hasNext())
		{
			Peer p = itPeers.next();
			if(p.getManagedObjects().containsVertex(searchedAvatar))
			{
				return p;
			}
				
		}
		
		return null;
	}
	//this will swap two objects if two isolated objects can be found
	private static ArrayList<Peer> tryObjectSwapForSearchedAvatar(Peer peerToRefine, Peer locationOfSearchedAvatar, ArrayList<Peer> peers) 
	{
		Avatar searched = peerToRefine.getOwnAvatar();
		//check if the avatar is isolated in its current partition
		if(locationOfSearchedAvatar.getManagedObjects().edgesOf(searched).isEmpty())
		{
			//try to find a isolated object in the other graphs partition to swap it
			MMVEObject swapTarget = getIsolatedObject(peerToRefine.getManagedObjects());
			if(swapTarget != null)
			{
				//do the swap
				peerToRefine.getManagedObjects().removeVertex(swapTarget);
				peerToRefine.getManagedObjects().addVertex(searched);
				locationOfSearchedAvatar.getManagedObjects().removeVertex(searched);
				locationOfSearchedAvatar.getManagedObjects().addVertex(swapTarget);
							
				return peers;
			}
			else
			{
				//there is no isolated target to swap with, so we only tranfer the searched avatar
				peerToRefine.getManagedObjects().addVertex(searched);
				locationOfSearchedAvatar.getManagedObjects().removeVertex(searched);
			}
		}
		else
		{
			peers = refinementForNonIsolatedNodes(peerToRefine, locationOfSearchedAvatar, peers);
		}
		
		return peers;
	}

	//this method will make sure that each peer has 
	private static ArrayList<Peer> refinementForNonIsolatedNodes(Peer peerToRefine, Peer locationOfSearchedAvatar, ArrayList<Peer> peers) 
	{
		Avatar searched = peerToRefine.getOwnAvatar();
		//deleting the peer out of the partition should remove all edges and do the trick
		locationOfSearchedAvatar.getManagedObjects().removeVertex(searched);
		peerToRefine.getManagedObjects().addVertex(searched);
		
		return peers;
	}

	private static MMVEObject getIsolatedObject(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition) 
	{
		Iterator<MMVEObject> itPart = partition.vertexSet().iterator();
		
		while(itPart.hasNext())
		{
			MMVEObject o = itPart.next();
			if(partition.edgesOf(o).isEmpty())
			{
				return o;
			}
		}
		return null;
	}

}
