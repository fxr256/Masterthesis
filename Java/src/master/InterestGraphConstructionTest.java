package master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class InterestGraphConstructionTest {

	@Test
	public void testConstructAvatarEdges() 
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		a1.setAoi(new AoI(a1.getPosition(),2));
		Avatar a2 = new Avatar(new Point(1,1));
		a2.setAoi(new AoI(a2.getPosition(),2));
		Avatar a3 = new Avatar(new Point(10,10));
		//test add interestedIn functionality
		a3.addInterest(a1);
		a1.addInterest(a2);
		
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(InterestGraphConstruction.constructAvatarEdges(g));
		g.addVertex(a1);
		g.addVertex(a2);
		g.addVertex(a3);
		
		g = InterestGraphConstruction.constructAvatarEdges(g);
		assertEquals(3,g.edgeSet().size());
		assertEquals(2,g.getEdgeWeight(g.getEdge(a1, a2)), 0);
	}
	
	@Test
	public void testConstructObjectEdges()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		a1.setAoi(new AoI(a1.getPosition(),2));
		Avatar a2 = new Avatar(new Point(1,1));
		a2.setAoi(new AoI(a2.getPosition(),2));
		Avatar a3 = new Avatar(new Point(10,10));
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(InterestGraphConstruction.constructObjectEdges(g));
		g.addVertex(a1);
		g.addVertex(a2);
		g.addVertex(a3);
		
		g = InterestGraphConstruction.constructObjectEdges(g);
		assertEquals(2,g.edgeSet().size());
		
	}
	
	@Test
	public void testconstructAvatarInterestGraphFromPartitions()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar(new Point(1,1));
		a1.setAoi(new AoI(a1.getPosition(), 3));
		a2.setAoi(new AoI(a2.getPosition(), 13));
		Avatar a3 = new Avatar(new Point(10,10));
		a3.setAoi(new AoI(a3.getPosition(), 13));
		Avatar a4 = new Avatar(new Point(12,12));
		a4.setAoi(new AoI(a4.getPosition(), 5));
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		g.addVertex(a1);
		g.addVertex(a2);
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		h.addVertex(a3);
		h.addVertex(a4);
		h.addEdge(a3, a4);
		h.addEdge(a4, a3);
		
		ArrayList<DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>> partitions = new ArrayList<DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>>();
		
		//test for empty graph list
		assertNull(InterestGraphConstruction.constructAvatarInterestGraphFromPartitions(partitions));
		
		partitions.add(g);
		partitions.add(h);
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> i = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		i.addVertex(a2);
		i.addVertex(a3);
		
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> result = InterestGraphConstruction.constructAvatarInterestGraphFromPartitions(partitions);
		result = InterestGraphConstruction.constructAvatarEdges(result);
		
		GraphAnalysis.analyseAvatarGraph(result);
		assertEquals(4, result.vertexSet().size());
		assertEquals(6, result.edgeSet().size());
		assertTrue(result.containsEdge(a3, a4));
		assertTrue(result.containsEdge(a4, a3));
		
		
	}
	
	@Test
	public void testInterestedInEdgeConstruction()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		
		o1.addInterest(o2);
		o1.addInterest(o3);
		o2.addInterest(o3);
		o3.addInterest(o1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(o3);
		
		//create edge to test edgeweight increase
		graph.addEdge(o1, o2);
		
		graph = InterestGraphConstruction.constructObjectEdges(graph);
		assertEquals(4, graph.edgeSet().size());
		assertEquals(2, graph.getEdgeWeight(graph.getEdge(o1, o2)),0);
	}
	
	@Test
	public void testconstructObjectInterestGraphFromPartitions()
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar(new Point(1,1));
		a1.setAoi(new AoI(a1.getPosition(), 3));
		a2.setAoi(new AoI(a2.getPosition(), 13));
		Avatar a3 = new Avatar(new Point(10,10));
		a3.setAoi(new AoI(a3.getPosition(), 13));
		Avatar a4 = new Avatar(new Point(12,12));
		a4.setAoi(new AoI(a4.getPosition(), 5));
		
		//add an interest to test interestedIn edge creation
		a4.addInterest(a1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		g.addVertex(a1);
		g.addVertex(a2);
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		h.addVertex(a3);
		h.addVertex(a4);
		h.addEdge(a3, a4);
		h.addEdge(a4, a3);
		
		ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions = new ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>>();
		
		//test for empty graph list
		assertNull(InterestGraphConstruction.constructObjectInterestGraphFromPartitions(partitions));
		
		partitions.add(g);
		partitions.add(h);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> i = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		i.addVertex(a2);
		i.addVertex(a3);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> result = InterestGraphConstruction.constructObjectInterestGraphFromPartitions(partitions);
		result = InterestGraphConstruction.constructObjectEdges(result);
		GraphAnalysis.analyseObjectGraph(result);
		assertEquals(4, result.vertexSet().size());
		assertEquals(7, result.edgeSet().size());
		assertTrue(result.containsEdge(a3, a4));
		assertTrue(result.containsEdge(a4, a3));
	}
	
	@Test
	public void testFactors()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject(new Point(20,20));
		MMVEObject o2 = new MMVEObject(new Point(20,20));
		MMVEObject o3 = new MMVEObject(new Point(20,20));
		
		o1.addInterest(o2);
		o1.addInterest(o3);
		o2.addInterest(o3);
		o3.addInterest(o1);
		
		Avatar a1 = new Avatar();
		a1.setAoi(new AoI(a1.getPosition(),2));
		Avatar a2 = new Avatar(new Point(1,1));
		a2.setAoi(new AoI(a2.getPosition(),2));
		Avatar a3 = new Avatar(new Point(10,10));
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(o3);
		
		graph.addVertex(a1);
		graph.addVertex(a2);
		graph.addVertex(a3);
		a1.addInterest(a3);
		a3.addInterest(a1);
		
		graph.addEdge(o1, o2);
		graph = InterestGraphConstruction.constructObjectEdges(graph);
		GraphAnalysis.analyseObjectGraph(graph);
		
		assertEquals(8, graph.edgeSet().size());
		assertEquals(2, graph.getEdgeWeight(graph.getEdge(o1, o2)),0);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph2 = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph2.addVertex(o1);
		graph2.addVertex(o2);
		graph2.addVertex(o3);
		
		graph2.addVertex(a1);
		graph2.addVertex(a2);
		graph2.addVertex(a3);
		
		assertEquals(0, graph2.edgeSet().size());
		graph = InterestGraphConstruction.constructObjectEdges(graph2, 0.5, 2);
		assertEquals(8, graph2.edgeSet().size());
		assertEquals(2, graph2.getEdgeWeight(graph.getEdge(o1, o2)),0);
		assertEquals(0.5, graph2.getEdgeWeight(graph.getEdge(a1, a2)),0);
	}
	
	@Test
	public void testgetPartitionsFromPeerList()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		g.addVertex(o1);
		g.addVertex(o2);
		g.addEdge(o1, o2);
		h.addVertex(a1);
		h.addVertex(a2);
		h.addEdge(a1, a2);
		ArrayList<Peer> peers = Util.getStandardPeerList(2);
		peers.get(0).setManagedObjects(g);
		peers.get(1).setManagedObjects(h);
		assertEquals(2, InterestGraphConstruction.getPartitionsFromPeerList(peers).size());
		assertTrue(InterestGraphConstruction.getPartitionsFromPeerList(peers).get(0).equals(g));
		assertTrue(InterestGraphConstruction.getPartitionsFromPeerList(peers).get(1).equals(h));
		
		
	}
	
	@Test
	public void testConstructInterestGraphEdgesFromPastInteractions()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		ArrayList<Interaction> regularInteractions = new ArrayList<Interaction>();
		ArrayList<Interaction> crossedInteractions = new ArrayList<Interaction>();
		Interaction i1 = new Interaction(o1, o2);
		Interaction i2 = new Interaction(o1, a1);
		Interaction i3 = new Interaction(a1, a2);
		Interaction i4 = new Interaction(a2, a1);
		regularInteractions.add(i1);
		regularInteractions.add(i2);
		crossedInteractions.add(i3);
		crossedInteractions.add(i4);
		assertNull(InterestGraphConstruction.constructInterestGraphEdgesFromPastInteractions(interestGraph, regularInteractions, crossedInteractions, 1.0));
		
		interestGraph.addVertex(o1);
		interestGraph.addVertex(o2);
		interestGraph.addVertex(a1);
		interestGraph.addVertex(a2);
		
		interestGraph.addEdge(o1, o2);
		interestGraph.addEdge(o2, o1);
		interestGraph.addEdge(o1, a1);
		
		interestGraph = InterestGraphConstruction.constructInterestGraphEdgesFromPastInteractions(interestGraph, regularInteractions, crossedInteractions, 1.0);
		
		assertEquals(5, interestGraph.edgeSet().size());
		assertEquals(2, interestGraph.getEdgeWeight(interestGraph.getEdge(o1, o2)), 0);
		assertEquals(1, interestGraph.getEdgeWeight(interestGraph.getEdge(a1, a2)), 0);
		
	}

}
