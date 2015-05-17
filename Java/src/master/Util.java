package master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import backup.SimulationClient;


public class Util 
{
	
	
	//sorts the avatars as defined by the comparable interface in avatar.java
	//returns an ArrayList, because Sets can't be sorted
	public static ArrayList<Avatar> sortAvatars(Set<Avatar> s)
	{
		if(s.isEmpty())
			return null;
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		list.addAll(s);
		Collections.sort(list);
		
		return list;
	}
	
	//sorts the objects as defined by the comparable interface in avatar.java
	//returns an ArrayList, because Sets can't be sorted
	public static ArrayList<MMVEObject> sortObjects(Set<MMVEObject> s)
	{
		if(s.isEmpty())
			return null;
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		list.addAll(s);
		Collections.sort(list);
		
		return list;
	}
	
	
	
	//purpose of this function is to eliminate all "double edges" so Metis can work with the result
	//Metis does count the the edge e(u,v) and e(v,u) as one
	public static int metisEdgeNumberAvatar(DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g)
	{
		Set<DefaultWeightedEdge> s = g.edgeSet();
		ArrayList<DefaultWeightedEdge> l = new ArrayList<DefaultWeightedEdge>();
		Iterator<DefaultWeightedEdge> itS = s.iterator();
		
		while (itS.hasNext())
		{
			DefaultWeightedEdge e = itS.next(); 
			Avatar source = g.getEdgeSource(e);
			Avatar target = g.getEdgeTarget(e);
			if (g.containsEdge(target, source))
			{
				DefaultWeightedEdge f = g.getEdge(target, source);
				if(!l.contains(f))
					l.add(e);
			}
			else
			{
				l.add(e);
			}
		}
		
		return l.size();
	}
	
	//purpose of this function is to eliminate all "double edges" so Metis can work with the result
	//Metis does count the the edge e(u,v) and e(v,u) as one
	public static int metisEdgeNumberObject(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g)
	{
		Set<DefaultWeightedEdge> s = g.edgeSet();
		ArrayList<DefaultWeightedEdge> l = new ArrayList<DefaultWeightedEdge>();
		Iterator<DefaultWeightedEdge> itS = s.iterator();
		
		while (itS.hasNext())
		{
			DefaultWeightedEdge e = itS.next(); 
			MMVEObject source = g.getEdgeSource(e);
			MMVEObject target = g.getEdgeTarget(e);
			if (g.containsEdge(target, source))
			{
				DefaultWeightedEdge f = g.getEdge(target, source);
				if(!l.contains(f))
					l.add(e);
			}
			else
			{
				l.add(e);
			}
		}
		
		return l.size();
	}
	
	
	
	//searches the vertices of a graph for a vertex with a certain id, returns null if nothing is found
	public static Avatar getAvatarWithID(DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g, int ID)
	{
		Set<Avatar> avatars = g.vertexSet();
		Iterator<Avatar> itG = avatars.iterator();
		Avatar u = null;
		while (itG.hasNext())
		{
			Avatar a = itG.next();
			if(a.getID()==ID)
				u=a;
		}
		
		return u;
	}
	
	//searches the objects of a graph for a object with a certain id, returns null if nothing is found
	public static MMVEObject getObjectWithID(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g, int ID)
	{
		Set<MMVEObject> objects = g.vertexSet();
		Iterator<MMVEObject> itG = objects.iterator();
		MMVEObject u = null;
		while (itG.hasNext())
		{
			MMVEObject a = itG.next();
			if(a.getID()==ID)
				u=a;
		}
		
		return u;
	}
	
	public static MMVEObject getObjectWithID(ArrayList<Avatar> avatarList, ArrayList<MMVEObject> objectList, int ID)
	{
		//at first search the avatar list
		MMVEObject foundObject = getAvatarInAvatarListWithID(avatarList, ID);
		if(foundObject == null)
		{
			Iterator<MMVEObject> itObjectList = objectList.iterator();
			while(itObjectList.hasNext())
			{
				MMVEObject objectToCheck = itObjectList.next();
				if(objectToCheck.getID() == ID)
				{
					foundObject = objectToCheck;
				}
			}
		}
		
		return foundObject;
	}
	
	public static MMVEObject getObjectWithID(ArrayList<MMVEObject> mergedList,
			int ID) 
	{
		Iterator<MMVEObject> itMergedList = mergedList.iterator();
		MMVEObject foundObject = null;
		while(itMergedList.hasNext())
		{
			MMVEObject objectToCheck = itMergedList.next();
			if(objectToCheck.getID() == ID)
			{
				foundObject = objectToCheck;
			}
		}
		return foundObject;
	}
	
	//searches the objects of a graph for a object with a certain id, returns null if nothing is found
	public static Avatar getAvatarInObjectGraphWithID(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g, int ID)
	{
		Set<MMVEObject> objects = g.vertexSet();
		Iterator<MMVEObject> itG = objects.iterator();
		Avatar u = null;
		while (itG.hasNext())
		{
			MMVEObject o = itG.next();
			if(o instanceof Avatar)
			{
				Avatar a = (Avatar)o;
				if(a.getID()==ID)
					u=a;
			}
			
		}
		
		return u;
	}
	
	//searches the objects of a graph for a object with a certain id, returns null if nothing is found
	public static Avatar getAvatarInAvatarListWithID(ArrayList<Avatar> avatarList, int ID)
	{
		
		Iterator<Avatar> itAvatarList = avatarList.iterator();
		Avatar foundAvatar = null;
		while (itAvatarList.hasNext())
		{
			Avatar avatarToCheck = itAvatarList.next();
		
			if(avatarToCheck.getID()==ID)
			{
				foundAvatar=avatarToCheck;
			}
		
		}
		
		return foundAvatar;
	}
	
	//sets the weight of each mmveObject in the list to a certain value
	public static ArrayList<MMVEObject> setObjectListWeight(ArrayList<MMVEObject> source, int weight)
	{
		
		try
		{
			if(source.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			
			Iterator<MMVEObject> itSource = source.iterator();
				while(itSource.hasNext())
				{
					MMVEObject o = itSource.next();
					o.setWeight(weight);					
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;			
		}
		
		return source;
	}
	
	//sets the weight of each mmveObject in the list to a certain value
	public static ArrayList<Avatar> setAvatarListWeight(ArrayList<Avatar> source, int weight)
	{
		
		try
		{
			if(source.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			
			Iterator<Avatar> itSource = source.iterator();
				while(itSource.hasNext())
				{
					Avatar a = itSource.next();
					a.setWeight(weight);					
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;			
		}
		
		return source;
	}
	
	//add all the avatars to an avatar graph
	public static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> addAllAvatars(DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph, ArrayList<Avatar> list)
	{
		try
		{
			if(list.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			
			Iterator<Avatar> itList = list.iterator();
			
			while(itList.hasNext())
			{
				originalGraph.addVertex(itList.next());				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return originalGraph;
	}
	
	//add all the object to an object graph
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addAllMMVEObjects(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph, ArrayList<MMVEObject> list)
	{
		try
		{
			if(list.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			
			Iterator<MMVEObject> itList = list.iterator();
			
			while(itList.hasNext())
			{
				originalGraph.addVertex(itList.next());				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return originalGraph;
	}
	
	//add all the avatars to an object graph
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addAllAvatarsToMMVEGraph(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph, ArrayList<Avatar> list)
	{
		try
		{
			if(list.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			
			Iterator<Avatar> itList = list.iterator();
			
			while(itList.hasNext())
			{
				originalGraph.addVertex((MMVEObject)itList.next());				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return originalGraph;
	}
	
	public static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> addAllEdgesAvatar(Avatar a, ArrayList<Avatar> list, DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph, double edgeWeight )
	{
		try
		{
			if(list.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
			
			Iterator<Avatar> itList = list.iterator();
			
			while(itList.hasNext())
			{
				DefaultWeightedEdge e = originalGraph.addEdge(a, itList.next());
				originalGraph.setEdgeWeight(e, edgeWeight);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return originalGraph;
	}
	
	public static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> addAllEdgesAvatar(Avatar a, ArrayList<Avatar> list, DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> originalGraph)
	{
		try
		{
			if(list.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
			
			Iterator<Avatar> itList = list.iterator();
			
			while(itList.hasNext())
			{
				originalGraph.addEdge(a, itList.next());
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return originalGraph;
	}
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addAllEdgesObject (MMVEObject o, ArrayList<MMVEObject> list, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph)
	{
		try
		{
			if(list.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
			
			Iterator<MMVEObject> itList = list.iterator();
			
			while(itList.hasNext())
			{
				originalGraph.addEdge(o, itList.next());
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return originalGraph;
	}
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addAllEdgesObject (MMVEObject o, ArrayList<MMVEObject> list, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph, 
			double edgeWeight)
	{
		try
		{
			if(list.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			if(originalGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("You are using an empty graph!");
			
			Iterator<MMVEObject> itList = list.iterator();
			
			while(itList.hasNext())
			{
				DefaultWeightedEdge e = originalGraph.addEdge(o, itList.next());
				originalGraph.setEdgeWeight(e, edgeWeight);
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return originalGraph;
	}
	
	public static ArrayList<Avatar> setAvatarAoIListSize(ArrayList<Avatar> source, double size)
	{
		
		try
		{
			if(source.isEmpty())
				throw new Exception("The list you are trying to use is empty!");
			
			Iterator<Avatar> itSource = source.iterator();
				while(itSource.hasNext())
				{
					Avatar a = itSource.next();
					a.getAoi().setRadius(size);					
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;			
		}
		
		return source;
	}
	
	public static DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> addAllAvatarsFromSet(DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> graph, 
			Set<Avatar> avatars)
	{
		try
		{
			if(avatars.isEmpty())
				throw new Exception ("The set of avatars is empty!");
			Iterator<Avatar> itA = avatars.iterator();
			while (itA.hasNext())
			{
				graph.addVertex(itA.next());
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return graph;
	}
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addAllObjectsFromSet(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph, 
			Set<MMVEObject> objects)
	{
		try
		{
			if(objects.isEmpty())
				throw new Exception ("The set of avatars is empty!");
			Iterator<MMVEObject> itO = objects.iterator();
			while (itO.hasNext())
			{
				graph.addVertex(itO.next());
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return graph;
	}
	
	//used to create a certain number of standard peers
	public static ArrayList<Peer> getStandardPeerList(int peerNumber)
	{
		ArrayList<Peer> peers = new ArrayList<Peer>();
		
		while(peers.size()< peerNumber)
		{
			Peer p = new Peer();
			peers.add(p);
		}
		
		return peers;
	}
	
	//used to create a certain number of standard MMVEObjects
	public static ArrayList<MMVEObject> getStandardObjectList(int objectNumber)
	{
		ArrayList<MMVEObject> objects = new ArrayList<MMVEObject>();
		
		while(objects.size()< objectNumber)
		{
			MMVEObject o = new MMVEObject();
			objects.add(o);
		}
		
		return objects;
	}
	
	public static ArrayList<Peer> assignAvatarToPeerWithMatchingID(ArrayList<Peer> peers, DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> interestGraph)
	{
		try
		{
			if(peers.isEmpty())
				throw new Exception("The peer list is empty!");
			if(interestGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("The Graph you are trying to use is empty!");
			
			Iterator<Peer> itP = peers.iterator();
			
			while(itP.hasNext())
			{
				Peer p = itP.next();
				p.setOwnAvatar(getAvatarInObjectGraphWithID(interestGraph, p.getID()));
			}
			
				
						
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return peers;
	}
	
	public static ArrayList<Peer> assignAvatarToPeerWithMatchingID(ArrayList<Peer> peers, ArrayList<Avatar> avatarList)
	{
		try
		{
			if(peers.isEmpty())
				throw new Exception("The peer list is empty!");
			if(avatarList.isEmpty())
				throw new Exception("The avatar list you are trying to use is empty!");
			
			Iterator<Peer> itP = peers.iterator();
			
			while(itP.hasNext())
			{
				Peer p = itP.next();
				p.setOwnAvatar(getAvatarInAvatarListWithID(avatarList, p.getID()));
			}
			
				
						
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return peers;
	}
	
	//adds all vertices and edges of source to target, should be called after .copy on the original graph
	//this is called CopyAvatarGraph instead of just overloading CopyGraph, because eclipse does not accept
	//this function overload. This is possibly an eclipse bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=320514
	public static DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> CopyObjectGraph(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> source, DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> target)
	{	
		
		//add vertices
		Set<MMVEObject> vertexS = source.vertexSet();
		Iterator<MMVEObject> itS = vertexS.iterator();
		MMVEObject o = null;
		
		while (itS.hasNext())
		{
			o = itS.next();
			Assert.assertNotNull(o);
			target.addVertex(o);
		}
		
		//add edges
		Set<DefaultWeightedEdge> edgeS = source.edgeSet();
		Iterator<DefaultWeightedEdge> itES = edgeS.iterator();
		DefaultWeightedEdge e = null;
		
		while(itES.hasNext())
		{
			e = itES.next();
			target.addEdge(source.getEdgeSource(e), source.getEdgeTarget(e), e);
		}
		
		return target;
	}
	
	public static ArrayList<Peer> updatePeerInPeerList(Peer peerToUpdate, ArrayList<Peer> peers)
	{
		int index = peers.indexOf(peerToUpdate);
		if(index!=-1)
		{
			peers.remove(index);
			peers.add(index, peerToUpdate);
		}
		return peers;
	}
	
	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> constructObjectGraphFromPeerList(ArrayList<Peer> peers)
	{
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		try
		{
			if(peers.isEmpty())
				throw new Exception("The peer list is empty!");
			
			Iterator<Peer> itPeers = peers.iterator();
			
			while(itPeers.hasNext())
			{
				Peer p = itPeers.next();
				DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> subGraph = p.getManagedObjects();
				interestGraph = addObjectGraphToObjectGraph(subGraph, interestGraph);
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}				
		
		return interestGraph;
	}

	public static DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> addObjectGraphToObjectGraph(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> subGraph,
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> GraphToGetExtended) 
	{
		try
		{
			if(subGraph.vertexSet().isEmpty())
				throw new GraphEmptyException("The subgraph is empty!");
			
			Iterator<MMVEObject> itVSub = subGraph.vertexSet().iterator();
			while(itVSub.hasNext())
			{
				MMVEObject objectToAdd = itVSub.next();
				GraphToGetExtended.addVertex(objectToAdd);
			}
			
			Iterator<DefaultWeightedEdge> itEdgesSubgraph = subGraph.edgeSet().iterator();
			while(itEdgesSubgraph.hasNext())
			{
				DefaultWeightedEdge edgeToAdd = itEdgesSubgraph.next();
				if(GraphToGetExtended.containsVertex(subGraph.getEdgeSource(edgeToAdd)) && GraphToGetExtended.containsVertex(subGraph.getEdgeTarget(edgeToAdd)))
					GraphToGetExtended.addEdge(subGraph.getEdgeSource(edgeToAdd), subGraph.getEdgeTarget(edgeToAdd), edgeToAdd);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//changed this from return null, because returning null might fuck up my measurements
			return GraphToGetExtended;
		}
				
		
		return GraphToGetExtended;
	}
	
	public static void createSimulationClientThreads(int threadnumber, String coordinatorAddress, int portnumber)
	{
		int counter = 0;
		
		while(counter < threadnumber)
		{
			Thread t = new Thread(new SimulationClient(coordinatorAddress, portnumber));
			t.start();
			counter++;
		}
	}
	
	public static ArrayList<Peer> copyPeerList(ArrayList<Peer> listToCopy)
	{
		ArrayList<Peer> result = new ArrayList<Peer>();
		try
		{
			if(listToCopy.isEmpty())
				throw new Exception("The list you trying to copy is empty!");
			
			Iterator<Peer> itList = listToCopy.iterator();
			while(itList.hasNext())
			{
				Peer peerToCopy = itList.next();
				Peer peerToAdd = new Peer();
				peerToAdd.setInfo(peerToCopy.getInfo());
				peerToAdd.setOverloaded(peerToCopy.isOverloaded());
				peerToAdd.setOwnAvatar(peerToCopy.getOwnAvatar());
				peerToAdd.setManagedObjects(peerToCopy.getManagedObjects());
				peerToAdd.setPeerCopyID(peerToCopy.getID());
				peerToAdd.setCapacity(peerToCopy.getCapacity());
				result.add(peerToAdd);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public static ArrayList<Region> copyRegionList(ArrayList<Region> listToCopy)
	{
		ArrayList<Region> result = new ArrayList<Region>();
		try
		{
			if(listToCopy.isEmpty())
				throw new Exception("The list you trying to copy is empty!");
			
			Iterator<Region> itList = listToCopy.iterator();
			while(itList.hasNext())
			{
				Region regionToCopy = itList.next();
				Region regionToAdd = new Region(regionToCopy.getGraphicalRepresentation().x, regionToCopy.getGraphicalRepresentation().y,
						regionToCopy.getGraphicalRepresentation().width, regionToCopy.getGraphicalRepresentation().height);
				regionToAdd.setRemainingRegion(regionToCopy.isRemainingRegion());
				result.add(regionToAdd);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public static Peer getPeerWithCopyID(ArrayList<Peer> peersToSearch, int ID)
	{
		Peer result = null;
		
		Iterator<Peer> itList = peersToSearch.iterator();
		while(itList.hasNext())
		{
			Peer p = itList.next();
			if(p.getPeerCopyID() == ID)
			{
				result = p;
				break;
			}
		}
		return result;
	}

	public static boolean objectsInSamePartition(ArrayList<Peer> peers,
			MMVEObject source, MMVEObject target) 
	{
		Iterator<Peer> itPeers = peers.iterator();
		while(itPeers.hasNext())
		{
			Peer peerToCheck = itPeers.next();
			//check whether the peer contains one of the objects
			if(peerToCheck.getManagedObjects().containsVertex(source) || peerToCheck.getManagedObjects().containsVertex(target))
			{
				if(peerToCheck.getManagedObjects().containsVertex(source) && peerToCheck.getManagedObjects().containsVertex(target))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}
	
	
	public static ArrayList<DefaultWeightedEdge> removeIncomingEdgesFromEdgesOf(Set<DefaultWeightedEdge> edgesOf, 
			DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>interestGraph, MMVEObject matchingObject) 
	{
		ArrayList<DefaultWeightedEdge> result = new ArrayList<DefaultWeightedEdge>();
		Iterator<DefaultWeightedEdge> itEdgesOf = edgesOf.iterator();
		
		while(itEdgesOf.hasNext())
		{
			DefaultWeightedEdge currentEdge = itEdgesOf.next();
			if(interestGraph.getEdgeSource(currentEdge).equals(matchingObject))
			{
				result.add(currentEdge);
			}
		}
		return result;
	}
	
	public static ArrayList<MMVEObject> mergeAvatarAndObjectList(ArrayList<Avatar> avatarList, ArrayList<MMVEObject> objectList)
	{
		ArrayList<MMVEObject> result = new ArrayList<MMVEObject>();
		Iterator<Avatar> itAvatarList = avatarList.iterator();
		Iterator<MMVEObject> itObjectList = objectList.iterator();
		
		while(itAvatarList.hasNext())
		{
			result.add(itAvatarList.next());
		}
		
		while(itObjectList.hasNext())
		{
			result.add(itObjectList.next());
		}
		
		return result;
		
	}

	public static int calculateGraphWeigth(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph) 
	{
		int weight = 0;
		Iterator<MMVEObject> itInterestGraph = interestGraph.vertexSet().iterator();
		while(itInterestGraph.hasNext())
		{
			weight = weight + itInterestGraph.next().getWeight();
		}
		return weight;
	}

	public static int calculateTotalRegionWeigth(ArrayList<Region> regions) 
	{
		int weight = 0;
		Iterator<Region> itRegions = regions.iterator();
		while(itRegions.hasNext())
		{
			Region regionToProcess = itRegions.next();
			weight = weight + RegionAssignment.getRegionWeight(regionToProcess);
		}
		return weight;
	}

	
	

	
}
