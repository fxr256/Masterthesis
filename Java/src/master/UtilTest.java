package master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class UtilTest {

	
	@Test
	public void testSortAvatars() 
	{
		//test with empty set
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(Util.sortAvatars(h.vertexSet()));
		
		//test with correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
			
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a3);
		g.addEdge(a3, a1);
		g.addEdge(a1, a4);
		g.addEdge(a4, a1);
		g.addEdge(a2, a4);
		
		ArrayList<Avatar> list = Util.sortAvatars(g.vertexSet());
		assertEquals(1,list.get(0).getID());
		assertEquals(2,list.get(1).getID());
		assertEquals(3,list.get(2).getID());
		assertEquals(4,list.get(3).getID());
	}
	
	@Test
	public void testSortObjects()
	{
		//test with empty set
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(Util.sortObjects(h.vertexSet()));
		
		//test with correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
			
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a3);
		g.addEdge(a3, a1);
		g.addEdge(a1, a4);
		g.addEdge(a4, a1);
		g.addEdge(a2, a4);
		
		ArrayList<MMVEObject> list = Util.sortObjects(g.vertexSet());
		assertEquals(1,list.get(0).getID());
		assertEquals(2,list.get(1).getID());
		assertEquals(3,list.get(2).getID());
		assertEquals(4,list.get(3).getID());
		
	}

	
	@Test
	public void testMetisEdgeNumberAvatar() 
	{
		//check with empty graph
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertEquals(0, Util.metisEdgeNumberAvatar(h));
		
		//check with correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
	
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a3);
		g.addEdge(a3, a1);
		g.addEdge(a1, a4);
		g.addEdge(a4, a1);
		g.addEdge(a2, a4);
		
		assertEquals(3,Util.metisEdgeNumberAvatar(g));
	}
	
	@Test
	public void testMetisEdgeNumberObject() 
	{
		//check with empty graph
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertEquals(0, Util.metisEdgeNumberObject(h));
		
		//check with correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
	
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a3);
		g.addEdge(a3, a1);
		g.addEdge(a1, a4);
		g.addEdge(a4, a1);
		g.addEdge(a2, a4);
		
		assertEquals(3,Util.metisEdgeNumberObject(g));
	}

	

	@Test
	public void testGetAvatarWithID() 
	{
		//check with empty graph
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(Util.getAvatarWithID(h, 1));
		
		Avatar.counter = 0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		g.addVertex(a1);
		
		//check for valid graph, but vertex not found
		assertNull(Util.getAvatarWithID(g, a2.getID()));
		//check for valid graph and valid vertex
		assertEquals(a1, Util.getAvatarWithID(g, a1.getID()));
	}
	
	@Test
	public void testsetObjectListWeight()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		
		assertNull(Util.setObjectListWeight(list,1));
		list.add(a1);
		list.add(a2);
		Util.setObjectListWeight(list,1);
		
		assertEquals(1, list.get(0).getWeight());
		assertEquals(1, list.get(1).getWeight());
	
	}
	
	@Test
	public void testsetAvatarListWeight()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		
		assertNull(Util.setAvatarListWeight(list,1));
		list.add(a1);
		list.add(a2);
		Util.setAvatarListWeight(list,1);
		
		assertEquals(1, list.get(0).getWeight());
		assertEquals(1, list.get(1).getWeight());
	
	}
	
	@Test
	public void testaddAllAvatars()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		assertNull(Util.addAllAvatars(g,list));
		list.add(a1);
		list.add(a2);
		
		g = Util.addAllAvatars(g, list);
		assertEquals(2, g.vertexSet().size());
		
	}
	
	@Test
	public void testaddAllMMVEObjects()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		assertNull(Util.addAllMMVEObjects(g,list));
		list.add(a1);
		list.add(a2);
		
		g = Util.addAllMMVEObjects(g, list);
		assertEquals(2, g.vertexSet().size());
		
	}
	
	@Test
	public void testaddAllAvatarsToMMVEGraph()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		assertNull(Util.addAllAvatarsToMMVEGraph(g,list));
		list.add(a1);
		list.add(a2);
		
		g = Util.addAllAvatarsToMMVEGraph(g, list);
		assertEquals(2, g.vertexSet().size());
	}
	
	@Test
	public void testaddAllEdgesAvatar()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//test with empty graph and list
		assertNull(Util.addAllEdgesAvatar(a1, list, g));
		list.add(a2);
		//test with empty graph
		assertNull(Util.addAllEdgesAvatar(a1, list, g));
		g.addVertex(a1);
		g.addVertex(a2);
		
		g = Util.addAllEdgesAvatar(a1, list, g);
		assertEquals(1,g.edgeSet().size());
		
	}
	
	@Test
	public void testaddAllEdgesObject()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//test with empty graph and list
		assertNull(Util.addAllEdgesObject(a1, list, g));
		list.add(a2);
		//test with empty graph
		assertNull(Util.addAllEdgesObject(a1, list, g));
		g.addVertex(a1);
		g.addVertex(a2);
		
		g = Util.addAllEdgesObject(a1, list, g);
		assertEquals(1,g.edgeSet().size());
	}
	
	@Test
	public void testsetAvatarAoIListSize()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		
		assertNull(Util.setAvatarAoIListSize(list,1));
		list.add(a1);
		list.add(a2);
		Util.setAvatarAoIListSize(list,1);
		
		assertEquals(1, list.get(0).getAoi().getRadius(),0);
		assertEquals(1, list.get(1).getAoi().getRadius(),0);		
	}
	
	@Test
	public void testaddAllAvatarsFromSet()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(Util.addAllAvatarsFromSet(h, g.vertexSet()));
		g.addVertex(a1);
		g.addVertex(a2);
		
		h = Util.addAllAvatarsFromSet(h, g.vertexSet());
		assertEquals(2, h.vertexSet().size());
		
		
	}
	
	@Test
	public void testassignAvatarToPeerWithMatchingID()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		ArrayList<Peer> peers = new ArrayList<Peer>();
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Test for empty list and graph
		assertNull(Util.assignAvatarToPeerWithMatchingID(peers, g));
		//test for filled list and empty graph
		peers = Util.getStandardPeerList(2);
		assertNull(Util.assignAvatarToPeerWithMatchingID(peers, g));
		//test for valid input
		g.addVertex(a1);
		g.addVertex(a2);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, g);
		assertEquals(a1, peers.get(0).getOwnAvatar());
		assertEquals(a2, peers.get(1).getOwnAvatar());
	}
	
	@Test
	public void testaddObjectGraphToObjectGraph()
	{
		Avatar.counter = 0;
		MMVEObject.counter = 0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> originalGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		originalGraph.addVertex(a1);
		originalGraph.addVertex(a2);
		originalGraph.addEdge(a1, a2);
		originalGraph.addEdge(a2, a1);
		
		assertEquals(originalGraph, Util.addObjectGraphToObjectGraph(new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class), originalGraph));
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> GraphToAdd = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		GraphToAdd.addVertex(o1);
		GraphToAdd.addVertex(o2);
		GraphToAdd.addEdge(o1, o2);
		GraphToAdd.addEdge(o2, o1);
		
		originalGraph = Util.addObjectGraphToObjectGraph(GraphToAdd, originalGraph);
		assertEquals(4, originalGraph.vertexSet().size());
		assertEquals(4, originalGraph.edgeSet().size());
	}
	
	@Test
	public void testcopyPeerList()
	{
		Peer.counter = 0;
		ArrayList<Peer> peers = new ArrayList<Peer>();
		assertNull(Util.copyPeerList(peers));
		Peer p1 = new Peer();
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		p1.getManagedObjects().addVertex(o1);
		p1.getManagedObjects().addVertex(a1);
		p1.getManagedObjects().addEdge(o1, a1);
		p1.getManagedObjects().addEdge(a1, o1);
		
		Peer p2 = new Peer();
		p2.getManagedObjects().addVertex(o2);
		p2.getManagedObjects().addVertex(a2);
		p2.getManagedObjects().addEdge(o2, a2);
		p2.getManagedObjects().addEdge(a2, o2);
		
		peers.add(p1);
		peers.add(p2);
		
		ArrayList<Peer> copied = Util.copyPeerList(peers);
		assertEquals(2, copied.size());
		assertEquals(1, copied.get(0).getPeerCopyID());
	}
	
	@Test
	public void testobjectsInSamePartition()
	{
		
		Peer.counter = 0;
		ArrayList<Peer> peers = new ArrayList<Peer>();
		Peer p1 = new Peer();
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		p1.getManagedObjects().addVertex(o1);
		p1.getManagedObjects().addVertex(a1);
		p1.getManagedObjects().addEdge(o1, a1);
		p1.getManagedObjects().addEdge(a1, o1);
		
		Peer p2 = new Peer();
		p2.getManagedObjects().addVertex(o2);
		p2.getManagedObjects().addVertex(a2);
		p2.getManagedObjects().addEdge(o2, a2);
		p2.getManagedObjects().addEdge(a2, o2);
		
		assertFalse(Util.objectsInSamePartition(peers, o1, a1));
		peers.add(p1);
		peers.add(p2);
		
		assertTrue(Util.objectsInSamePartition(peers, o1, a1));
		assertFalse(Util.objectsInSamePartition(peers, o2, a1));
	}
	
	@Test
	public void testremoveIncomingEdgesFromEdgesOf()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		Avatar a1 = new Avatar();
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(o3);
		graph.addVertex(a1);
		graph.addEdge(o1, o2);
		graph.addEdge(o1, o3);
		graph.addEdge(o3, o1);
		//test with isolated object
		assertEquals(0, Util.removeIncomingEdgesFromEdgesOf(graph.edgesOf(a1), graph, a1).size());
		//test with proper input
		assertEquals(2, Util.removeIncomingEdgesFromEdgesOf(graph.edgesOf(o1), graph, o1).size());
		
	}
	
	@Test
	public void testCalculateGraphWeigth()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		Avatar a1 = new Avatar();
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertEquals(0, Util.calculateGraphWeigth(graph));
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(o3);
		graph.addVertex(a1);
		assertEquals(4, Util.calculateGraphWeigth(graph));
	}
	
	@Test
	public void testCalculateTotalRegionWeigth()
	{
		
		MMVEObject.counter = 0;
		Region.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		Avatar a1 = new Avatar();
		
		ArrayList<Region> regions = new ArrayList<Region>();
		Region r1 = new Region(10,10, 20, 20);
		r1.getObjectsInRegion().add(o1);
		r1.getObjectsInRegion().add(o2);
		Region r2 = new Region(100,100,30,30);
		r2.getObjectsInRegion().add(o3);
		r2.getObjectsInRegion().add(a1);
		assertEquals(0, Util.calculateTotalRegionWeigth(regions));
		regions.add(r1);
		regions.add(r2);
		assertEquals(4, Util.calculateTotalRegionWeigth(regions));
		
	}

}
