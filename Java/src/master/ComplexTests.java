package master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

import backup.HistoricInterest;


public class ComplexTests 
{
	
	@Test
	public void simpleCompletePass()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		Avatar.counter=0;
		Peer.counter = 0;
		
		String filename = "trace.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> list = MobiSim.getAvatarsAndInitialPositions(filename);
		
		assertNotNull(list);
		
		//set the weight of all avatars to 1:
		list = Util.setAvatarListWeight(list, 1);
		
		//set the size of the aoi for all avatars
		list = Util.setAvatarAoIListSize(list, 20);
		
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//add all avatars to the graph
		interestGraph = Util.addAllAvatars(interestGraph, list);
		assertEquals(20,interestGraph.vertexSet().size());
		
		interestGraph = InterestGraphConstruction.constructAvatarEdges(interestGraph);
		assertEquals(4,interestGraph.edgeSet().size());
		
		Peer p1 = new Peer();
		Peer p2 = new Peer();
		Peer p3 = new Peer();
		Peer p4 = new Peer();
		
		int partitionnumber = 4;
		
		String FileName = "simpleInterest.graph";
		
		GraphAnalysis.analyseAvatarGraph(interestGraph);
		
		//convert graph to graphfile
		MetisInOut.convertAvatarGraphToGraphFile(interestGraph, FileName);
		
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeAvatarPartition(FileName, partitionnumber, interestGraph);
	
			
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesAvatar(partitions, interestGraph);
		
	
		//assign partitions to peers
		p1 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(0), p1, true);
		p2 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(1), p2, true);
		p3 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(2), p3, true);
		p4 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(3), p4, true);

		
		GraphAnalysis.analyseObjectGraph(p1.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p2.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p3.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p4.getManagedObjects());
		
		assertTrue(GraphAnalysis.allPartitionsHaveTheSameWeightAvatarGraph(partitions));
		
		assertEquals(5, p1.getManagedObjects().vertexSet().size());
		assertEquals(5, p2.getManagedObjects().vertexSet().size());
		assertEquals(5, p3.getManagedObjects().vertexSet().size());
		assertEquals(5, p4.getManagedObjects().vertexSet().size());
		assertEquals(0, crossedEdges.size());
		//stop the timer
		timer.stop();
		System.out.println(timer.getElapsedTime() + "ms");
					
		
		
	}
	
	@Test
	public void simpleCompletePathwithHeaveNode()
	{
		
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		Avatar.counter=0;
		Peer.counter = 0;
		
		String filename = "trace.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> list = MobiSim.getAvatarsAndInitialPositions(filename);
		
		assertNotNull(list);
		
		//set the weight of all avatars to 1:
		list = Util.setAvatarListWeight(list, 1);
		
		//set the weight of one avatar to a very high value
		list.get(0).setWeight(5);
		System.out.println("The heave node is " + list.get(0).getID());
		
		//set the size of the aoi for all avatars
		list = Util.setAvatarAoIListSize(list, 20);
		
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//add all avatars to the graph
		interestGraph = Util.addAllAvatars(interestGraph, list);
		assertEquals(20,interestGraph.vertexSet().size());
		
		interestGraph = InterestGraphConstruction.constructAvatarEdges(interestGraph);
		assertEquals(4,interestGraph.edgeSet().size());
		
		Peer p1 = new Peer();
		Peer p2 = new Peer();
		Peer p3 = new Peer();
		Peer p4 = new Peer();
		
		
		int partitionnumber = 4;
				
		String FileName = "simpleInterest.graph";
		
		GraphAnalysis.analyseAvatarGraph(interestGraph);
		
		//convert graph to graphfile
		MetisInOut.convertAvatarGraphToGraphFile(interestGraph, FileName);
		
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeAvatarPartition(FileName, partitionnumber, interestGraph);
		
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesAvatar(partitions, interestGraph);
		
		//assign partitions to peers
		p1 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(0), p1, true);
		p2 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(1), p2, true);
		p3 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(2), p3, true);
		p4 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(3), p4, true);
		
		GraphAnalysis.analyseObjectGraph(p1.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p2.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p3.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p4.getManagedObjects());
		
		assertTrue(GraphAnalysis.allPartitionsHaveTheSameWeightAvatarGraph(partitions));
		
		assertEquals(6, p1.getManagedObjects().vertexSet().size());
		assertEquals(6, p2.getManagedObjects().vertexSet().size());
		assertEquals(6, p3.getManagedObjects().vertexSet().size());
		assertEquals(2, p4.getManagedObjects().vertexSet().size());
		assertEquals(0, crossedEdges.size());
		//stop the timer
		timer.stop();
		System.out.println(timer.getElapsedTime() + "ms");
								
		
		
	}
	
	@Test
	public void testMETISPartitioningBehaviour()
	{
		
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		Avatar.counter=0;
		Peer.counter = 0;
		
		String filename = "trace.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> list = MobiSim.getAvatarsAndInitialPositions(filename);
		
		assertNotNull(list);
		
		//set the weight of all avatars to 1:
		list = Util.setAvatarListWeight(list, 1);
		
		int testWeight = 50;
		
		//set the weight of one avatar to a very high value
		list.get(0).setWeight(testWeight);
		//try to set the weight of node 17 to a high value, to test meits
		list.get(16).setWeight(testWeight);
		
		list.get(9).setWeight(testWeight);
		list.get(14).setWeight(testWeight);
		
		System.out.println("The heave node is " + list.get(0).getID());
		
		//set the size of the aoi for all avatars
		list = Util.setAvatarAoIListSize(list, 20);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//add all avatars to the graph
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, list);
		assertEquals(20,interestGraph.vertexSet().size());
		
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
		assertEquals(4,interestGraph.edgeSet().size());
		
		
		
		
		int partitionnumber = 20;
		
		ArrayList<Peer> peers = Util.getStandardPeerList(partitionnumber);
				
		String FileName = "simpleInterest.graph";
		
		GraphAnalysis.analyseObjectGraph(interestGraph);
		
		//convert graph to graphfile
		MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
		
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeObjectPartition(FileName, partitionnumber, interestGraph);
		
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
		
		//assign partitions to peers
		peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		
		GraphAnalysis.analysePeerObjectGraphList(peers);
		
		
		
		assertEquals(4, crossedEdges.size());
		//stop the timer
		timer.stop();
		System.out.println(timer.getElapsedTime() + "ms");
								
		
		
	}
	
	
	@Test 
	public void testSimplePassWithReconstructedGraph()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		Avatar.counter=0;
		Peer.counter = 0;
		
		String filename = "trace.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> list = MobiSim.getAvatarsAndInitialPositions(filename);
		
		assertNotNull(list);
		
		//set the weight of all avatars to 1:
		list = Util.setAvatarListWeight(list, 1);
		
		//set the size of the aoi for all avatars
		list = Util.setAvatarAoIListSize(list, 20);
		
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//add all avatars to the graph
		interestGraph = Util.addAllAvatars(interestGraph, list);
		assertEquals(20,interestGraph.vertexSet().size());
		
		interestGraph = InterestGraphConstruction.constructAvatarEdges(interestGraph);
		assertEquals(4,interestGraph.edgeSet().size());
		
		Peer p1 = new Peer();
		Peer p2 = new Peer();
		Peer p3 = new Peer();
		Peer p4 = new Peer();
		
		
		int partitionnumber = 4;
			
		String FileName = "simpleInterest.graph";
		
		GraphAnalysis.analyseAvatarGraph(interestGraph);
		
		//convert graph to graphfile
		MetisInOut.convertAvatarGraphToGraphFile(interestGraph, FileName);
		
		//start the partition and get the results from the graphfile
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeAvatarPartition(FileName, partitionnumber, interestGraph);
		
		//get crossed edges
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesAvatar(partitions, interestGraph);
		
		//assign partitions to peers
		p1 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(0), p1, true);
		p2 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(1), p2, true);
		p3 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(2), p3, true);
		p4 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(3), p4, true);

		
		GraphAnalysis.analyseObjectGraph(p1.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p2.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p3.getManagedObjects());
		GraphAnalysis.analyseObjectGraph(p4.getManagedObjects());
		
		//reconstruct the interest graph
		interestGraph = InterestGraphConstruction.constructAvatarInterestGraphFromPartitions(partitions);
		interestGraph = InterestGraphConstruction.constructAvatarEdges(interestGraph);
		assertEquals(20, interestGraph.vertexSet().size());
		assertEquals(4,interestGraph.edgeSet().size());
		
		//repartition it
		partitions = MetisInOut.executeAvatarPartition(FileName, partitionnumber, interestGraph);
		
		crossedEdges = MetisInOut.getCrossedEdgesAvatar(partitions, interestGraph);
		
		//assign partitions to peers
		p1 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(0), p1, true);
		p2 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(1), p2, true);
		p3 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(2), p3, true);
		p4 = PartitionAssignment.assignAvatarPartitiontoPeer(partitions.get(3), p4, true);
		
		assertTrue(GraphAnalysis.allPartitionsHaveTheSameWeightAvatarGraph(partitions));
		
		assertEquals(5, p1.getManagedObjects().vertexSet().size());
		assertEquals(5, p2.getManagedObjects().vertexSet().size());
		assertEquals(5, p3.getManagedObjects().vertexSet().size());
		assertEquals(5, p4.getManagedObjects().vertexSet().size());
		assertEquals(0, crossedEdges.size());
		//stop the timer
		timer.stop();
		System.out.println(timer.getElapsedTime() + "ms");
			
				
	}

	
	@Test
	public void CompletePassWithObjectsAndAvatars()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		MMVEObject.counter = 0;
		Avatar.counter = 0;
		Peer.counter = 0;
		String Avatarfile = "trace.txt";
		String Objectfile = "objects.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> avatarList = MobiSim.getAvatarsAndInitialPositions(Avatarfile);
		
		assertNotNull(avatarList);
		
		//set the weight of all avatars to 1:
		avatarList = Util.setAvatarListWeight(avatarList, 1);
		
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, 20);
		
		/*
		int testWeight = 20;
		
		//set the weight of one avatar to a very high value
		avatarList.get(0).setWeight(testWeight);
		//try to set the weight of node 17 to a high value, to test meits
		avatarList.get(16).setWeight(testWeight);
		
		avatarList.get(9).setWeight(testWeight);
		avatarList.get(14).setWeight(testWeight);
		*/
		
		//get inital positions for the objects
		ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(Objectfile);
	
		assertNotNull(objectList);
		
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, 1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//add all avatars
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		//add all objects
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		
		
		assertEquals(35,interestGraph.vertexSet().size());
		
		//construct graph edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
		assertEquals(8, interestGraph.edgeSet().size());
		
		GraphAnalysis.analyseObjectGraph(interestGraph);
		GraphAnalysis.analyseObjectGraphEdgeSet(interestGraph);
		
		//convert graph to graphfile
		String FileName = "objectInterestGraph.graph";
		MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
		
		int partitionnumber = 20;
		//execute partitioning
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeObjectPartition(FileName, partitionnumber, interestGraph);
		
		//get crossed edges
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
		//make 7 peers with default settings
		ArrayList<Peer> peers = Util.getStandardPeerList(partitionnumber);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		//assign partitions to peers:
		peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		assertFalse(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		//refine it
		assertEquals(0, crossedEdges.size());
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(Util.constructObjectGraphFromPeerList(peers), interestGraph);
		assertEquals(4, crossedEdges.size());
		GraphAnalysis.analysePeerObjectGraphList(peers);
		GraphAnalysis.analyseCrossedEdges(crossedEdges, interestGraph);

		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		timer.stop();
				
	}
	
	@Test
	public void CompletePassWithObjectsAndAvatarsAndHugeAoi()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		MMVEObject.counter = 0;
		Avatar.counter = 0;
		Peer.counter = 0;
		String Avatarfile = "trace.txt";
		String Objectfile = "objects.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> avatarList = MobiSim.getAvatarsAndInitialPositions(Avatarfile);
		
		assertNotNull(avatarList);
		
		//set the weight of all avatars to 1:
		avatarList = Util.setAvatarListWeight(avatarList, 1);
		
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, 200);
		
		//get inital positions for the objects
		ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(Objectfile);
	
		assertNotNull(objectList);
		
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, 1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//add all avatars
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		//add all objects
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		
		
		assertEquals(35,interestGraph.vertexSet().size());
		
		//construct graph edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
		assertEquals(289, interestGraph.edgeSet().size());
		
		GraphAnalysis.analyseObjectGraph(interestGraph);
		GraphAnalysis.analyseObjectGraphEdgeSet(interestGraph);
		
		//convert graph to graphfile
		String FileName = "objectInterestGraph.graph";
		MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
		
		int partitionnumber = 20;
		//execute partitioning
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeObjectPartition(FileName, partitionnumber, interestGraph);
		
		//get crossed edges
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
		//make 7 peers with default settings
		ArrayList<Peer> peers = Util.getStandardPeerList(partitionnumber);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		//assign partitions to peers:
		peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		
		GraphAnalysis.analysePeerObjectGraphList(peers);
		GraphAnalysis.analyseCrossedEdges(crossedEdges, interestGraph);
		//assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		
		//refine the partition
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		
		/*
		assertEquals(5, partitions.get(0).vertexSet().size());
		assertEquals(3, partitions.get(1).vertexSet().size());
		assertEquals(4, partitions.get(2).vertexSet().size());
		assertEquals(4, partitions.get(3).vertexSet().size());
		assertEquals(6, partitions.get(4).vertexSet().size());
		assertEquals(5, partitions.get(5).vertexSet().size());
		assertEquals(8, partitions.get(6).vertexSet().size());
		*/
		assertEquals(264, crossedEdges.size());
		assertFalse(GraphAnalysis.allPartitionsHaveTheSameWeightObjectGraph(partitions));
		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		timer.stop();
	}
	
	@Test
	public void testOneAvatarToOnePeer()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		MMVEObject.counter = 0;
		Avatar.counter = 0;
		Peer.counter = 0;
		String Avatarfile = "avatarsSmall.txt";
		String Objectfile = "objects.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> avatarList = MobiSim.getAvatarsAndInitialPositions(Avatarfile);
		
		assertNotNull(avatarList);
		
		//set the weight of all avatars to 20 to test what happens:
		avatarList = Util.setAvatarListWeight(avatarList, 20);
		
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, 20);
		
		//get inital positions for the objects
		ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(Objectfile);
	
		assertNotNull(objectList);
		
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, 1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//add all avatars
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		//add all objects
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		
		
		assertEquals(25,interestGraph.vertexSet().size());
		
		//construct graph edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
		assertEquals(1, interestGraph.edgeSet().size());
		
		GraphAnalysis.analyseObjectGraph(interestGraph);
		GraphAnalysis.analyseObjectGraphEdgeSet(interestGraph);
		
		//convert graph to graphfile
		String FileName = "objectInterestGraph.graph";
		MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
		
		int partitionnumber = 10;
		//execute partitioning
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeObjectPartition(FileName, partitionnumber, interestGraph);
		
		//get crossed edges
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
		//make 10 peers with default settings
		ArrayList<Peer> peers = Util.getStandardPeerList(10);
		
		//assign to each peer the avatar with the same id
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		
		//assign partitions to peers:
		peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		GraphAnalysis.analysePeerObjectGraphList(peers);
		GraphAnalysis.analyseCrossedEdges(crossedEdges, interestGraph);
		
		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		assertFalse(GraphAnalysis.allPartitionsHaveTheSameWeightObjectGraph(partitions));
		
		assertEquals(0, crossedEdges.size());
		timer.stop();
		
	}
	
	@Test
	public void testWithAvatarsAndObjectsAndInterestedIn()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		MMVEObject.counter = 0;
		Avatar.counter = 0;
		Peer.counter = 0;
		
		String Avatarfile = "trace.txt";
		String Objectfile = "objects.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> avatarList = MobiSim.getAvatarsAndInitialPositions(Avatarfile);
		
		assertNotNull(avatarList);
		
		//set the weight of all avatars to 1:
		avatarList = Util.setAvatarListWeight(avatarList, 1);
		
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, 20);
		
			
		//get inital positions for the objects
		ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(Objectfile);
	
		assertNotNull(objectList);
		
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, 1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//add all avatars
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		//add all objects
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		
		
		assertEquals(35,interestGraph.vertexSet().size());
		
		//add random interested in values
		interestGraph = Randomizer.addRandomInterestRelationshipToObjectGraph(interestGraph, 20);
		
		//construct graph edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
		assertTrue(8 <= interestGraph.edgeSet().size());
		
		GraphAnalysis.analyseObjectGraph(interestGraph);
		GraphAnalysis.analyseObjectGraphEdgeSet(interestGraph);
		
		//convert graph to graphfile
		String FileName = "objectInterestGraph.graph";
		MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
		
		int partitionnumber = 20;
		//execute partitioning
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeObjectPartition(FileName, partitionnumber, interestGraph);
		
		//get crossed edges
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
		//make 7 peers with default settings
		ArrayList<Peer> peers = Util.getStandardPeerList(partitionnumber);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		//assign partitions to peers:
		peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		//refine it
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(Util.constructObjectGraphFromPeerList(peers), interestGraph);
		GraphAnalysis.analysePeerObjectGraphList(peers);
		GraphAnalysis.analyseCrossedEdges(crossedEdges, interestGraph);

		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		timer.stop();
		System.out.println("Elapsed Time: " + timer.getElapsedTime() + "ms");
	}
	
	@Test
	public void testWithAvatarsAndObjectsAndInterestedInAndFixedInterest()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		MMVEObject.counter = 0;
		Peer.counter = 0;
		String Avatarfile = "trace.txt";
		String Objectfile = "objects.txt";
		String Interestfile = "complexInterest.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> avatarList = MobiSim.getAvatarsAndInitialPositions(Avatarfile);
		
		assertNotNull(avatarList);
		
		//set the weight of all avatars to 1:
		avatarList = Util.setAvatarListWeight(avatarList, 1);
		
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, 20);
		
			
		//get inital positions for the objects
		ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(Objectfile);
	
		assertNotNull(objectList);
		
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, 1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//add all avatars
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		//add all objects
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		
		
		assertEquals(35,interestGraph.vertexSet().size());
		
		//add random interested in values
		if(Randomizer.readInterestedInInfoFromFile(interestGraph, Interestfile) != null)
		{
			interestGraph = Randomizer.readInterestedInInfoFromFile(interestGraph, Interestfile);
			System.out.println("Interest Info loaded!");
		}
		else
		{
			interestGraph = Randomizer.addRandomInterestRelationshipToObjectGraph(interestGraph, 20);
			Randomizer.saveInterestedInToFile(interestGraph, Interestfile);
		}
		
		
		//construct graph edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph);
		assertTrue(8 <= interestGraph.edgeSet().size());
		
		GraphAnalysis.analyseObjectGraph(interestGraph);
		GraphAnalysis.analyseObjectGraphEdgeSet(interestGraph);
		
		//convert graph to graphfile
		String FileName = "objectInterestGraph.graph";
		MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
		
		int partitionnumber = 20;
		//execute partitioning
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeObjectPartition(FileName, partitionnumber, interestGraph);
		
		//get crossed edges
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
		//make 7 peers with default settings
		ArrayList<Peer> peers = Util.getStandardPeerList(partitionnumber);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		//assign partitions to peers:
		peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		//refine it
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(Util.constructObjectGraphFromPeerList(peers), interestGraph);
		GraphAnalysis.analysePeerObjectGraphList(peers);
		GraphAnalysis.analyseCrossedEdges(crossedEdges, interestGraph);

		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		timer.stop();
		System.out.println("Elapsed Time: " + timer.getElapsedTime() + "ms");
	}
	
	@Test
	public void testMETISPartitioningWithDifferentEdgeWeightFactors()
	{
		//start measure execution time
		Stopwatch timer = new Stopwatch().start();
		MMVEObject.counter = 0;
		Peer.counter = 0;
		String Avatarfile = "trace.txt";
		String Objectfile = "objects.txt";
		String Interestfile = "complexInterestFactor.txt";
		//get initial positions for the avatars
		ArrayList<Avatar> avatarList = MobiSim.getAvatarsAndInitialPositions(Avatarfile);
		
		assertNotNull(avatarList);
		
		//set the weight of all avatars to 1:
		avatarList = Util.setAvatarListWeight(avatarList, 1);
		
		//set the size of the aoi for all avatars
		avatarList = Util.setAvatarAoIListSize(avatarList, 20);
		
			
		//get inital positions for the objects
		ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(Objectfile);
	
		assertNotNull(objectList);
		
		//set the weight of all objects to 1:
		objectList = Util.setObjectListWeight(objectList, 1);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//add all avatars
		interestGraph = Util.addAllAvatarsToMMVEGraph(interestGraph, avatarList);
		//add all objects
		interestGraph = Util.addAllMMVEObjects(interestGraph, objectList);
		
		
		assertEquals(35,interestGraph.vertexSet().size());
		
		//add random interested in values
		if(Randomizer.testIfInterestedInFileExists(Interestfile))
		{
			interestGraph = Randomizer.readInterestedInInfoFromFile(interestGraph, Interestfile);
			System.out.println("Interest Info loaded!");
		}
		else
		{
			interestGraph = Randomizer.addRandomInterestRelationshipToObjectGraph(interestGraph, 10);
			Randomizer.saveInterestedInToFile(interestGraph, Interestfile);
		}
		
		
		//construct graph edges
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph, 1, 200);
		//interestGraph.setEdgeWeight(interestGraph.getEdge(Util.getObjectWithID(interestGraph, 13), Util.getObjectWithID(interestGraph, 31)), 200);
		
		//interestGraph.setEdgeWeight(interestGraph.getEdge(Util.getObjectWithID(interestGraph, 13), Util.getObjectWithID(interestGraph, 35)), 200);
		//interestGraph.setEdgeWeight(interestGraph.getEdge(Util.getObjectWithID(interestGraph, 13), Util.getObjectWithID(interestGraph, 4)), 200);
		assertEquals(31,interestGraph.edgeSet().size());
		
		GraphAnalysis.analyseObjectGraph(interestGraph);
		GraphAnalysis.analyseObjectGraphEdgeSet(interestGraph);
		
		//convert graph to graphfile
		String FileName = "objectInterestGraphWithFactors.graph";
		MetisInOut.convertObjectGraphToGraphFile(interestGraph, FileName);
		
		int partitionnumber = 20;
		//execute partitioning
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> partitions = 
			MetisInOut.executeObjectPartition(FileName, partitionnumber, interestGraph);
		
		//get crossed edges
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(partitions, interestGraph);
		//make peers with default settings
		ArrayList<Peer> peers = Util.getStandardPeerList(partitionnumber);
		peers = Util.assignAvatarToPeerWithMatchingID(peers, interestGraph);
		//assign partitions to peers:
		peers = PartitionAssignment.assignPartitionListToPeerList(partitions, peers, true);
		
		GraphAnalysis.analysePeerObjectGraphList(peers);
		System.out.println("Number of crossed Edges is: " + crossedEdges.size());
		GraphAnalysis.analyseCrossedEdges(crossedEdges, interestGraph);
		
		//refine it
		peers = PartitionAssignment.refinePartitions(partitions, peers, true);
		crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(Util.constructObjectGraphFromPeerList(peers), interestGraph);
		

		assertTrue(GraphAnalysis.allPartitionsContainOwnAvatar(peers));
		timer.stop();
		System.out.println("Elapsed Time: " + timer.getElapsedTime() + "ms");
		
	}
	
	
}
