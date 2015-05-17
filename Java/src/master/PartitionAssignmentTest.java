package master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class PartitionAssignmentTest {

	@Test
	public void testAssignAvatarPartitiontoPeer() 
	{
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar(new Point(1,1));
		Avatar a3 = new Avatar(new Point(10,10));
		
		
		
		Peer p = new Peer();
		p.setCapacity(10);
		
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//test for empty graph
		assertNull(PartitionAssignment.assignAvatarPartitiontoPeer(g, p, true));
		g.addVertex(a1);
		g.addVertex(a2);
		g.addVertex(a3);
		//test for correct input
		p = PartitionAssignment.assignAvatarPartitiontoPeer(g, p, true);
		assertEquals(3, p.getManagedObjects().vertexSet().size());
		assertFalse(p.isOverloaded());
		
		Avatar a4 = new Avatar();
		a4.setWeight(20);
		g.addVertex(a4);
		//test for bigger load than capacity and homogenous peers
		p = PartitionAssignment.assignAvatarPartitiontoPeer(g, p, true);
		assertEquals(4, p.getManagedObjects().vertexSet().size());
		assertTrue(p.isOverloaded());
		
		//test for bigger load than capacity and non homogenous peers
		assertNull(PartitionAssignment.assignAvatarPartitiontoPeer(g, p, false));
		
		
		
	}
	
	@Test
	public void testassignPartitionListToPeerList()
	{
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Avatar.counter = 0;
		MMVEObject.counter = 0;
		
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		g.addVertex(o1);
		g.addVertex(o2);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		h.addVertex(a1);
		h.addVertex(a2);
		
		ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions = new ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>>();
		ArrayList<Peer> peers = new ArrayList<Peer>();
		Peer p1 = new Peer();
		Peer p2 = new Peer();
		peers.add(p1);
		peers.add(p2);
		//test for empty partitions
		assertNull(PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true));
		
		partitions.add(g);
		partitions.add(h);
		peers.clear();
		//test for empty peerlist
		assertNull(PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true));
		//test for mismatched peerlist and partitions
		peers.add(p1);
		assertNull(PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true));
		peers.add(p2);
		//test for valid input
		ArrayList<Peer> result = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		
		assertEquals(2, result.get(0).getManagedObjects().vertexSet().size());
		assertEquals(2, result.get(1).getManagedObjects().vertexSet().size());
		
		//test for heavy input
		h.removeVertex(a2);
		a2.setWeight(40);
		h.addVertex(a2);
		result = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		
		assertEquals(2, result.get(0).getManagedObjects().vertexSet().size());
		assertEquals(2, result.get(1).getManagedObjects().vertexSet().size());
		
		result = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, false);
		assertEquals(1, result.size());
		
	}
	
	@Test
	public void testRefinementWithIsolatedObjects()
	{
		
		Avatar.counter=0;
		MMVEObject.counter=0;
		Peer.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar(new Point(1,1));
		Avatar a3 = new Avatar(new Point(10,10));
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
			
		
		
		
		ArrayList<Peer> peers = Util.getStandardPeerList(2);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		interestGraph.addVertex(a1);
		interestGraph.addVertex(a2);
		interestGraph.addVertex(a3);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition1 = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		partition1.addVertex(a2);
		partition1.addVertex(a3);
		partition1.addVertex(o1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition2 = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		partition2.addVertex(a1);
		partition2.addVertex(o2);
		
		peers.get(0).setManagedObjects(partition1);
		peers.get(1).setManagedObjects(partition2);
		assertFalse(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions = new ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>>();
		partitions.add(partition1);
		partitions.add(partition2);
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		
	}
	
	@Test
	public void testRefinementWithIsolatedSearchedAvatar()
	{
		Avatar.counter=0;
		MMVEObject.counter=0;
		Peer.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar(new Point(1,1));
		Avatar a3 = new Avatar(new Point(10,10));
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		
		
		
		ArrayList<Peer> peers = Util.getStandardPeerList(2);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		interestGraph.addVertex(a1);
		interestGraph.addVertex(a2);
		interestGraph.addVertex(a3);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition1 = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		
		partition1.addVertex(a3);
		partition1.addVertex(o1);
		
		partition1.addEdge(a3, o1);
	
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition2 = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		partition2.addVertex(a1);
		partition2.addVertex(a2);
		partition2.addVertex(o2);
		
		peers.get(0).setManagedObjects(partition1);
		peers.get(1).setManagedObjects(partition2);
		assertFalse(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions = new ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>>();
		partitions.add(partition1);
		partitions.add(partition2);
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
	}
	
	@Test
	public void testRefinementWithoutIsolatedNodes()
	{
		Avatar.counter=0;
		MMVEObject.counter=0;
		Peer.counter=0;
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar(new Point(1,1));
		Avatar a3 = new Avatar(new Point(10,10));
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		
		
		
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition1 = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		partition1.addVertex(a2);
		partition1.addVertex(a3);
		partition1.addVertex(o1);
		
		partition1.addEdge(a3, o1);
		partition1.addEdge(o1, a3);
		partition1.addEdge(a2, a3);
		partition1.addEdge(a3, a2);
	
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> partition2 = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		partition2.addVertex(a1);
		partition2.addVertex(o2);
		
		partition2.addEdge(a1, o2);
		partition2.addEdge(o2, a1);
		
		ArrayList<Peer> peers = Util.getStandardPeerList(2);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		interestGraph = Util.addObjectGraphToObjectGraph(partition1, interestGraph);
		interestGraph = Util.addObjectGraphToObjectGraph(partition2, interestGraph);
		
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		peers.get(0).setManagedObjects(partition1);
		peers.get(1).setManagedObjects(partition2);
		assertFalse(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		ArrayList<DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>> partitions = new ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>>();
		partitions.add(partition1);
		partitions.add(partition2);
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		
		
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(Util.constructObjectGraphFromPeerList(peers), interestGraph);
		assertEquals(4, crossedEdges.size());
		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
	}

}
