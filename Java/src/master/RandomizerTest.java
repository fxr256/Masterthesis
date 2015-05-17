package master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class RandomizerTest {

	@Test
	public void testAddRandomInterestRelationshipToObjectList() 
	{
		MMVEObject.counter = 0;
		//grab 3 standard peers
		ArrayList<MMVEObject> list = Util.getStandardObjectList(3);
		//test with 0%
		assertNull(Randomizer.addRandomInterestRelationshipToObjectList(list, 0));
		//test with 200%
		assertNull(Randomizer.addRandomInterestRelationshipToObjectList(list, 200));
		//test with 100%
		list = Randomizer.addRandomInterestRelationshipToObjectList(list, 100);
		assertFalse(list.get(0).getInterestedIn().isEmpty());
		assertFalse(list.get(1).getInterestedIn().isEmpty());
		assertFalse(list.get(2).getInterestedIn().isEmpty());
		GraphAnalysis.analyseInterestedInObjectList(list);
		
		ArrayList<MMVEObject> secondList = Util.getStandardObjectList(15);
		secondList = Randomizer.addRandomInterestRelationshipToObjectList(secondList, 20);
		assertNotNull(secondList);
		GraphAnalysis.analyseInterestedInObjectList(secondList);
	}
	
	@Test
	public void testAddRandomInterestRelationshipToObjectGraph()
	{
		MMVEObject.counter = 0;
		//grab 3 standard peers
		ArrayList<MMVEObject> list = Util.getStandardObjectList(3);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph = Util.addAllMMVEObjects(graph, list);
		//test with 0%
		assertNull(Randomizer.addRandomInterestRelationshipToObjectGraph(graph, 0));
		//test with 200%
		assertNull(Randomizer.addRandomInterestRelationshipToObjectGraph(graph, 200));
		//test with 100%
		graph = Randomizer.addRandomInterestRelationshipToObjectGraph(graph, 100);
		assertFalse(Util.getObjectWithID(graph, 1).getInterestedIn().isEmpty());
		assertFalse(Util.getObjectWithID(graph, 2).getInterestedIn().isEmpty());
		assertFalse(Util.getObjectWithID(graph, 3).getInterestedIn().isEmpty());
		GraphAnalysis.analyseInterestedInObjectGraph(graph);
		
		/*
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> twentyPercentgraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		twentyPercentgraph = Util.addAllMMVEObjects(twentyPercentgraph, Util.getStandardObjectList(20));
		twentyPercentgraph = Randomizer.addRandomInterestRelationshipToObjectGraph(twentyPercentgraph, 20);
		GraphAnalysis.analyseInterestedInObjectGraph(twentyPercentgraph);
		*/
	}
	
	@Test
	public void testsaveInterestedInToFile()
	{
		MMVEObject.counter = 0;
		ArrayList<MMVEObject> list = Util.getStandardObjectList(4);
		list.get(0).addInterest(list.get(1));
		list.get(1).addInterest(list.get(0));
		list.get(2).addInterest(list.get(0));
		list.get(2).addInterest(list.get(1));
		String fileName ="testInterestedInWrite.txt";
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertFalse(Randomizer.saveInterestedInToFile(graph, fileName));
		graph = Util.addAllMMVEObjects(graph, list);
		assertTrue(Randomizer.saveInterestedInToFile(graph, fileName));
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader ("D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\GraphInput\\" + fileName));
			assertEquals("1 2", reader.readLine());
			assertEquals("2 1", reader.readLine());
			assertEquals("3 1 2", reader.readLine());
			assertEquals("4", reader.readLine());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testreadInterestedInInfoFromFile()
	{
		MMVEObject.counter = 0;
		ArrayList<MMVEObject> list = Util.getStandardObjectList(4);
		String fileName ="testInterestedInWrite.txt";
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(Randomizer.readInterestedInInfoFromFile(graph, fileName));
		graph = Util.addAllMMVEObjects(graph, list);
		assertNull(Randomizer.readInterestedInInfoFromFile(graph, "this file does not exist!"));
		//here we go
		graph = Randomizer.readInterestedInInfoFromFile(graph, fileName);
		//test
		assertEquals(1, Util.getObjectWithID(graph, 1).getInterestedIn().size());
		assertEquals(1, Util.getObjectWithID(graph, 2).getInterestedIn().size());
		assertEquals(2, Util.getObjectWithID(graph, 3).getInterestedIn().size());
		assertEquals(0, Util.getObjectWithID(graph, 4).getInterestedIn().size());
		
	}

}
