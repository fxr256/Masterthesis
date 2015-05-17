package master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RegionAssignment 
{
	public static ArrayList<Region> assignRegionsToPeersRandomly(ArrayList<Region> regions, ArrayList<Peer> peers)
	{
		Iterator<Region> itRegions = regions.iterator();
		ArrayList<Peer> availablePeers = new ArrayList<Peer>();
		Iterator<Peer> itPeers = peers.iterator();
		while(itPeers.hasNext())
		{
			availablePeers.add(itPeers.next());
		}
		Random randomGenerator = new Random();
		Peer peerManagingTheRemainingRegions = null;
		while(itRegions.hasNext())
		{
			Region regionToProcess = itRegions.next();
			Peer peerToAssignTheRegionTo = null;
			if(availablePeers.size() > 0)
			{
				peerToAssignTheRegionTo = availablePeers.get(randomGenerator.nextInt(availablePeers.size()));
			}
			
			
			if(regionToProcess.isRemainingRegion() && peerManagingTheRemainingRegions != null)
			{
				peerToAssignTheRegionTo = peerManagingTheRemainingRegions;
			}
			
			if(regionToProcess.isRemainingRegion() && peerManagingTheRemainingRegions == null)
			{
				peerManagingTheRemainingRegions = peerToAssignTheRegionTo;
			}
			
			if(peerToAssignTheRegionTo != null)
			{
				regionToProcess.setManagingPeer(peerToAssignTheRegionTo);
				availablePeers.remove(peerToAssignTheRegionTo);
			}
			
			
		}
		return regions;
	}
	
	public static ArrayList<Region> assignRegionsToPeersDependingOnOwnAvatar(ArrayList<Region> regions, ArrayList<Peer> peers, 
			boolean homogeneousPeers)
	{
		ArrayList<Region> result = new ArrayList<Region>();
		try
		{
			if(peers.isEmpty())
				throw new Exception("The peer list is empty!");
			if(regions.isEmpty())
				throw new Exception("The region list is empty!");
			
			
			Iterator<Region> itRegions = regions.iterator();
			//add each partition to one peer in the peer list
			Iterator<Peer> itPeers = peers.iterator();
			ArrayList<Region> done =  new ArrayList<Region>();
			ArrayList<Peer> servedPeers = new ArrayList<Peer>();
			Peer peerManagingRemainingRegions = null;
			
			//check first whether we can find a partition which contains the own peers avatar
			while(itPeers.hasNext())
			{
				Peer p = itPeers.next();
				Region regionContainingAvatar = findRegionContainingObjectByPosition(p.getOwnAvatar(), regions);
				//keep the remaining regions together
				if(regionContainingAvatar.isRemainingRegion() && peerManagingRemainingRegions != null
						&& !done.contains(regionContainingAvatar))
				{
					regionContainingAvatar = assignPeerToRegion(regionContainingAvatar, peerManagingRemainingRegions, homogeneousPeers);
					if(regionContainingAvatar!=null)
					{
						done.add(regionContainingAvatar);
						if(!servedPeers.contains(peerManagingRemainingRegions))
							servedPeers.add(p);
						result.add(regionContainingAvatar);
					}
					
				}
				else
				{
					//if the partition is not null and was not already assigned, assign it to the peer
					if(regionContainingAvatar!=null && done.contains(regionContainingAvatar)==false)
					{
						regionContainingAvatar = assignPeerToRegion(regionContainingAvatar, p, homogeneousPeers);
						if(regionContainingAvatar!=null)
						{
							done.add(regionContainingAvatar);
							servedPeers.add(p);
							result.add(regionContainingAvatar);
							
							if(regionContainingAvatar.isRemainingRegion() && peerManagingRemainingRegions == null)
							{
								peerManagingRemainingRegions = p;
							}
						}
						
					}
				}
				
			}
			
			
			//now iterate through all the regions and assign them if they were not already assigned
			while(itRegions.hasNext())
			{
				Region regionToAssign = itRegions.next();
				//check whether partition was already assigned
				if(done.contains(regionToAssign)==false)
				{
					if(regionToAssign.isRemainingRegion() && peerManagingRemainingRegions!=null)
					{
						regionToAssign = assignPeerToRegion(regionToAssign, peerManagingRemainingRegions, homogeneousPeers);
						if(regionToAssign!=null)
						{
							done.add(regionToAssign);
							if(!servedPeers.contains(peerManagingRemainingRegions))
								servedPeers.add(peerManagingRemainingRegions);
							result.add(regionToAssign);
						}
					}
					else
					{
						//refresh peer iterator
						itPeers = peers.iterator();
						while(itPeers.hasNext())
						{
							Peer p = itPeers.next();
							//check whether the peer was already served
							if(servedPeers.contains(p)==false && done.contains(regionToAssign)==false)
							{
								regionToAssign = assignPeerToRegion(regionToAssign, p, homogeneousPeers);
								if(regionToAssign!=null)
								{
									done.add(regionToAssign);
									servedPeers.add(p);
									result.add(regionToAssign);
									
									if(regionToAssign.isRemainingRegion() && peerManagingRemainingRegions == null)
									{
										peerManagingRemainingRegions = p;
									}
								}
									
							}
						}
					}
					
					
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
	
	private static Region assignPeerToRegion(Region regionContainingAvatar,	Peer target, boolean homogenPeers) 
	{
		try
		{
	
			//if we got homogenous peers, we always assign the partition to the graph, even if it gets overloaded
			if(homogenPeers)
			{
				regionContainingAvatar.setManagingPeer(target);
				//if the weight of all avatars within the partition is bigger than the peer's capacity
				//the peer will be marked as overloaded.
				if(getRegionWeight(regionContainingAvatar) > target.getCapacity())
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
				if(getRegionWeight(regionContainingAvatar) < target.getCapacity())
				{
					regionContainingAvatar.setManagingPeer(target);
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
		return regionContainingAvatar;
	}

	public static Region findRegionContainingObjectByPosition(MMVEObject objectToPlace, ArrayList<Region> regions) 
	{
		Iterator<Region> itRegions = regions.iterator();
		while(itRegions.hasNext())
		{
			Region regionToCheck = itRegions.next();
			//check whether the regions contains the object
			if(regionToCheck.getGraphicalRepresentation().contains(objectToPlace.getPosition()))
			{
				return regionToCheck;
			}
		}
		return null;
	}
	
	public static boolean objectsInSameRegion(ArrayList<Region> regions, MMVEObject source, MMVEObject target)
	{
		Region regionContainingObject1 = findRegionContainingObjectByPosition(source, regions);
		Region regionContainingObject2 = findRegionContainingObjectByPosition(target, regions);
		
		if(regionContainingObject1 != null && regionContainingObject2!=null)
		{
			if(regionContainingObject1.equals(regionContainingObject2))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public static int getRegionWeight(Region regionToEvaluate)
	{
		int weight = 0;
		Iterator<MMVEObject> itObjectsOfTheRegion = regionToEvaluate.getObjectsInRegion().iterator();
		while(itObjectsOfTheRegion.hasNext())
		{
			weight = weight + itObjectsOfTheRegion.next().getWeight();
		}
		return weight;
	}

	public static ArrayList<Region> getRegionsManagedByPeer(Peer peerToProcess,
			ArrayList<Region> regions) 
	{
		ArrayList<Region> regionsManagedByPeer = new ArrayList<Region>();
		if(peerToProcess == null || regions == null)
			return regionsManagedByPeer;
		Iterator<Region> itRegions = regions.iterator();
		while(itRegions.hasNext())
		{
			Region regionToProcess = itRegions.next();
			Peer peerToCompareWith = regionToProcess.getManagingPeer();
			if(peerToCompareWith != null)
			{
				if(peerToCompareWith.equals(peerToProcess))
				{
					regionsManagedByPeer.add(regionToProcess);
				}
			}
			else
			{
				System.out.println("This should not happen!");
			}
			
		}
		return regionsManagedByPeer;
	}
}
