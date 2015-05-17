package master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class MetisInOutTest {

	

	@Test
	public void testexecuteAvatarPartition()
	{
		Avatar.counter=0;
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		String FileName = "test3.graph";
		
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> list = new ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>>();
		
		//test for empty graph
		assertNull(MetisInOut.executeAvatarPartition(FileName, 4, g));
		
		
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
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		g.addEdge(a2, a3);
		g.addEdge(a3, a2);
		g.addEdge(a3, a4);
		g.addEdge(a4, a3);
		
		//test for non exiting file
		assertNull(MetisInOut.executeAvatarPartition("this file does not exist", 4, g));
		//test for dumb partition number
		assertNull(MetisInOut.executeAvatarPartition(FileName, 0, g));
		
		//test for valid input
		list = MetisInOut.executeAvatarPartition(FileName, 2, g);
		
		assertEquals(2, list.size());
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> h = list.get(0);
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> i = list.get(1);
		
		assertEquals(2,h.vertexSet().size());
		assertEquals(2,i.vertexSet().size());
		
		
	}
	@Test
	public void testConvertAvatarGraphToGraphFile() {
		//test for empty graph
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertFalse(MetisInOut.convertAvatarGraphToGraphFile(h, "empty.graph"));
		
		//test for correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
		a1.setWeight(1);
		a2.setWeight(1);
		a3.setWeight(1);
		a4.setWeight(1);
		
	
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
		
		String FileName = "test.graph";
		
		MetisInOut.convertAvatarGraphToGraphFile(g, FileName);
		try
		{
			
			BufferedReader reader = new BufferedReader(new FileReader (FileName));
			
			String input = reader.readLine();
			assertEquals("4 3 11", input);
			input = reader.readLine();
			assertEquals("1 3 1 4 1", input);
			
			File graphfile = new File (FileName);
			
			if(graphfile.exists())
			{
				graphfile.delete();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Test for valid graph, but bogus filename
		assertTrue(MetisInOut.convertAvatarGraphToGraphFile(g, "bla.bla"));
		
		File graphfile = new File ("bla.bla");
		if(graphfile.exists())
		{
			graphfile.delete();
		}
	}
	
	@Test
	public void testconvertObjectGraphToGraphFile()
	{
		
		//test for empty graph
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertFalse(MetisInOut.convertObjectGraphToGraphFile(h, "empty.graph"));
		
		//test for correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
		a1.setWeight(1);
		a2.setWeight(1);
		a3.setWeight(1);
		a4.setWeight(1);
		
	
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
		
		String FileName = "test.graph";
		
		MetisInOut.convertObjectGraphToGraphFile(g, FileName);
		try
		{
			
			BufferedReader reader = new BufferedReader(new FileReader (FileName));
			
			String input = reader.readLine();
			assertEquals("4 3 11", input);
			input = reader.readLine();
			assertEquals("20 3 1 4 1", input);
			
			File graphfile = new File (FileName);
			
			if(graphfile.exists())
			{
				graphfile.delete();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Test for valid graph, but bogus filename
		assertTrue(MetisInOut.convertObjectGraphToGraphFile(g, "bla.bla"));
		
		File graphfile = new File ("bla.bla");
		if(graphfile.exists())
		{
			graphfile.delete();
		}
		
	}


	

	@Test
	public void testConvertAvatarGraphToSimpleGraphFile() 
	{
		//test for empty graph file
		DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertFalse(MetisInOut.convertAvatarGraphToSimpleGraphFile(h, "empty.graph"));
		
		//test for valid input
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
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		g.addEdge(a2, a3);
		g.addEdge(a3, a2);
		g.addEdge(a3, a4);
		g.addEdge(a4, a3);
	
		
		String FileName = "testsimple.graph";
		
		MetisInOut.convertAvatarGraphToSimpleGraphFile(g, FileName);
		
		try
		{
			
			BufferedReader reader = new BufferedReader(new FileReader (FileName));
			
			String input = reader.readLine();
			assertEquals("4 3 1", input);
			input = reader.readLine();
			assertEquals("2 1", input);
			
			File graphfile = new File (FileName);
			
			if(graphfile.exists())
			{
				graphfile.delete();
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Test for valid graph, but bogus filename
		assertTrue(MetisInOut.convertAvatarGraphToGraphFile(g, "bla.bla"));
		//delete file afterwards
		File graphfile = new File ("bla.bla");
		
		if(graphfile.exists())
		{
			graphfile.delete();
		}
	}

	

	@Test
	public void testConvertPartitionstoAvatarGraphs() 
	{
		
		//test for valid input
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
		
		a1.setWeight(1);
		a2.setWeight(1);
		a3.setWeight(1);
		a4.setWeight(1);
	
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		g.addEdge(a2, a3);
		g.addEdge(a3, a2);
		g.addEdge(a3, a4);
		g.addEdge(a4, a3);
		
		//test for non existing file:
		assertNull(MetisInOut.convertPartitionstoAvatarGraphs("thisFileDoesNotExist",g));
		
		String FileName = "testavatarpartition.graph.part.2";
		


		//test for valid graphfile, but a empty graph
		assertNull(MetisInOut.convertPartitionstoAvatarGraphs(FileName, new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class)));
		
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> list = MetisInOut.convertPartitionstoAvatarGraphs(FileName, g);
		
				
		assertEquals(2,list.size());
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> h = list.get(0);
		assertEquals(2,h.vertexSet().size());
		assertEquals(2,h.edgeSet().size());
		assertTrue(h.containsVertex(Util.getAvatarWithID(g, 1)));
		assertTrue(h.containsVertex(Util.getAvatarWithID(g, 2)));
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> i = list.get(1);
		assertEquals(2,i.vertexSet().size());
		assertEquals(2,i.edgeSet().size());
		assertTrue(i.containsVertex(Util.getAvatarWithID(g, 3)));
		assertTrue(i.containsVertex(Util.getAvatarWithID(g, 4)));
		}
		
		
	
	
	@Test
	public void testConvertPartitionstoObjectGraphs()
	{
		
		int partitionnumber = 2;
		
		
		//test for valid input
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
		
		a1.setWeight(1);
		a2.setWeight(1);
		a3.setWeight(1);
		a4.setWeight(1);
	
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		g.addEdge(a2, a3);
		g.addEdge(a3, a2);
		g.addEdge(a3, a4);
		g.addEdge(a4, a3);
		
		//test for non existing file:
		assertNull(MetisInOut.convertPartitionstoObjectGraphs("thisFileDoesNotExist",g));
		
		String FileName = "testObjectPartition.graph";
		
		MetisInOut.convertObjectGraphToGraphFile(g, FileName);
	

		//test for valid graphfile, but a empty graph
		assertNull(MetisInOut.convertPartitionstoAvatarGraphs(FileName + ".part." + 
				partitionnumber, new DirectedWeightedMultigraph<Avatar, DefaultWeightedEdge>(DefaultWeightedEdge.class)));
		
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> list = MetisInOut.convertPartitionstoObjectGraphs(FileName + ".part." + 
				partitionnumber, g);
		
		
		
		assertEquals(2,list.size());
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> h = list.get(0);
		assertEquals(2,h.vertexSet().size());
		assertEquals(2,h.edgeSet().size());
		assertTrue(h.containsVertex(Util.getObjectWithID(g, 1)));
		assertTrue(h.containsVertex(Util.getObjectWithID(g, 2)));
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> i = list.get(1);
		assertEquals(2,i.vertexSet().size());
		assertEquals(2,i.edgeSet().size());
		assertTrue(i.containsVertex(Util.getObjectWithID(g, 3)));
		assertTrue(i.containsVertex(Util.getObjectWithID(g, 4)));
	
	}

	

	@Test
	public void testGetCrossedEdgesAvatar() 
	{
		
		int partitionnumber = 2;
				
		//test for valid input
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
		a1.setWeight(1);
		a2.setWeight(1);
		a3.setWeight(1);
		a4.setWeight(1);
	
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		g.addEdge(a2, a3);
		g.addEdge(a3, a2);
		g.addEdge(a3, a4);
		g.addEdge(a4, a3);
		
		
		String FileName = "test3.graph";
		
		MetisInOut.convertAvatarGraphToGraphFile(g, FileName);
					
			
		
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> list = MetisInOut.executeAvatarPartition(FileName, partitionnumber, g);
		
					
		//check for empty list
		assertNull(MetisInOut.getCrossedEdgesAvatar(new ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>>(), g));
		//check for valid list, but empty graph
		assertNull(MetisInOut.getCrossedEdgesAvatar(list, new DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class)));
		
		//check for valid input
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesAvatar(list, g);
		assertEquals(2,crossedEdges.size());
					
	
	
	}
	
	@Test
	public void testGetCrossedEdgesObject()
	{
		
		int partitionnumber = 2;
		
		
		//test for valid input
		//reset vertex counter, otherwise this fucks up the testcases
		Avatar.counter=0;
		DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Avatar a1 = new Avatar("1");
		Avatar a2 = new Avatar("2");
		Avatar a3 = new Avatar("3");
		Avatar a4 = new Avatar("4");
		
		a1.setWeight(1);
		a2.setWeight(1);
		a3.setWeight(1);
		a4.setWeight(1);
	
		g.addVertex(a2);
		g.addVertex(a1);
		g.addVertex(a3);
		g.addVertex(a4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		g.addEdge(a2, a3);
		g.addEdge(a3, a2);
		g.addEdge(a3, a4);
		g.addEdge(a4, a3);
		
		
		String FileName = "test3.graph";
		
		MetisInOut.convertObjectGraphToGraphFile(g, FileName);
		
		ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>> list = MetisInOut.executeObjectPartition(FileName, partitionnumber, g);
		
					
		//check for empty list
		assertNull(MetisInOut.getCrossedEdgesObject(new ArrayList<DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>>(), g));
		//check for valid list, but empty graph
		assertNull(MetisInOut.getCrossedEdgesObject(list, new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class)));
		
		//check for valid input
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesObject(list, g);
		assertEquals(2,crossedEdges.size());
	
	}

	
	@Test
	public void testGetCrossedEdgesBetweenObjectGraphs()
	{
		Avatar.counter = 0;
		MMVEObject.counter = 0;
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		MMVEObject o = new MMVEObject();
		
		interestGraph.addVertex(a1);
		interestGraph.addVertex(a2);
		interestGraph.addVertex(o);
		interestGraph.addEdge(a1, a2);
		interestGraph.addEdge(a2, a1);
		DefaultWeightedEdge e1 = interestGraph.addEdge(a2, o);
		DefaultWeightedEdge e2 = interestGraph.addEdge(o, a2);
		
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> subGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		subGraph.addVertex(a2);
		subGraph.addVertex(o);
		subGraph.addEdge(a2, o, e1);
		subGraph.addEdge(o, a2, e2);
		
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesBetweenObjectGraphs(subGraph, interestGraph);
		assertEquals(2,crossedEdges.size());
		
		//test for empty values: 
		assertNull(MetisInOut.getCrossedEdgesBetweenObjectGraphs(new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class), interestGraph));
		assertNull(MetisInOut.getCrossedEdgesBetweenObjectGraphs(subGraph, new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class)));
	}
	

	@Test
	public void testGetPartitionsOfACrossedEdgeAvatar() 
	{
		
		int partitionnumber = 2;
				
		//test for valid input
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
		g.addEdge(a1, a2);
		g.addEdge(a2, a1);
		g.addEdge(a2, a3);
		g.addEdge(a3, a2);
		g.addEdge(a3, a4);
		g.addEdge(a4, a3);
		
		
		String FileName = "test3.graph";
		
		MetisInOut.convertAvatarGraphToGraphFile(g, FileName);
		
		ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>> list = MetisInOut.executeAvatarPartition(FileName, partitionnumber, g);		
			list = MetisInOut.convertPartitionstoAvatarGraphs(FileName + ".part." + 
					partitionnumber, g);
			
			
		
		ArrayList<DefaultWeightedEdge> crossedEdges = MetisInOut.getCrossedEdgesAvatar(list, g);
		
		//check for empty list
		assertNull(MetisInOut.getPartitionsOfACrossedEdgeAvatar(crossedEdges.get(0),
				new ArrayList<DirectedWeightedMultigraph<Avatar,DefaultWeightedEdge>>(), g));
		//check for empty graph
		assertNull(MetisInOut.getPartitionsOfACrossedEdgeAvatar(crossedEdges.get(0), list, new DirectedWeightedMultigraph
				<Avatar,DefaultWeightedEdge>(DefaultWeightedEdge.class)));
		//check for non valid edge
		assertNull(MetisInOut.getPartitionsOfACrossedEdgeAvatar(new DefaultWeightedEdge(), list, g));
		
		//check for valid input
		ArrayList<Integer> crossedInfo = MetisInOut.getPartitionsOfACrossedEdgeAvatar(crossedEdges.get(0), list, g);
		assertEquals(0,(int)crossedInfo.get(0));
		assertEquals(1,(int)crossedInfo.get(1));
					
			
		}
		
	
	
	

	
	
}
