package master;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class InteractionSimulatorTest {

	@Test
	public void testGenerateInteractionFileDirectedWeightedMultigraphOfMMVEObjectDefaultWeightedEdgeIntString() {
		
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(a1);
		graph.addVertex(a2);
		
		graph.addEdge(o1, o2);
		graph.addEdge(o2, o1);
		graph.addEdge(o1, a1);
		graph.addEdge(o1, a2);
		graph.addEdge(o2, a1);
		graph.addEdge(a1, o2);
		
		InteractionSimulator.generateInteractionFile(graph, 10, "nonRegularinteractionTest.txt");
		assertEquals(10, InteractionSimulator.readInteractionFile("nonRegularinteractionTest.txt").size());
	}

	@Test
	public void testGenerateInteractionFileComplex() 
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(a1);
		graph.addVertex(a2);
		
		graph.addEdge(o1, o2);
		graph.addEdge(o2, o1);
		graph.addEdge(o1, a1);
		graph.addEdge(o1, a2);
		graph.addEdge(o2, a1);
		graph.addEdge(a1, o2);
		
		InteractionSimulator.generateInteractionFile(graph, 10, "interactionTest.txt", 30);
		assertEquals(10, InteractionSimulator.readInteractionFile("interactionTest.txt").size());
		
		
		
	}
	
	@Test
	public void testgenerateMultiRoundComplexInterestFile()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(a1);
		graph.addVertex(a2);
		
		graph.addEdge(o1, o2);
		graph.addEdge(o2, o1);
		graph.addEdge(o1, a1);
		graph.addEdge(o1, a2);
		graph.addEdge(o2, a1);
		graph.addEdge(a1, o2);
		
		InteractionSimulator.generateMultiRoundComplexInterestFile(graph, 10, "multiroundInteractionTest.txt", 30, 10);
		assertEquals(100, InteractionSimulator.readInteractionFile("multiroundInteractionTest.txt").size());
	}

	@Test
	public void testReadInteractionFile() 
	{
		//test with wrong filename
		assertNull(InteractionSimulator.readInteractionFile("diese datei existiert nicht"));
		//test with proper input
		ArrayList<String> result = InteractionSimulator.readInteractionFile("interactionJUnitTest.txt");
		assertEquals(10, result.size());
		assertEquals("0 1 3", result.get(0));
		assertEquals("0 1 2", result.get(6));
	}
	
	@Test
	public void testLocalityBasedInteractionCreation()
	{
		MMVEObject.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject(new Point(1,1));
		Avatar a1 = new Avatar(new Point(2,2), 3);
		Avatar a2 = new Avatar(new Point(3,3), 2);
		
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		graph.addVertex(o1);
		graph.addVertex(o2);
		graph.addVertex(a1);
		graph.addVertex(a2);
		
		//test with expected values
		InteractionSimulator.generateComplexInteractionFile(graph, 10, "localityInteractionTest.txt", 40);
		ArrayList<String> result = InteractionSimulator.readInteractionFile("localityInteractionTest.txt");
		assertEquals(10, result.size());
		assertEquals("0 3 1", result.get(0));
		
		//test with too many expected local interactions
		InteractionSimulator.generateComplexInteractionFile(graph, 10, "tooLittleLocalityTest.txt", 50);
		result = InteractionSimulator.readInteractionFile("tooLittleLocalityTest.txt");
		assertEquals(10, result.size());
		
		
	}

}
