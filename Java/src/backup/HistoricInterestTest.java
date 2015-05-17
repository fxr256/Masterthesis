package backup;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;

import master.AoI;
import master.Avatar;
import master.InterestGraphConstruction;
import master.MMVEObject;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class HistoricInterestTest {

	@Test
	public void testSaveInterestPatternToFile() 
	{
		MMVEObject.counter = 0;
		Avatar a1 = new Avatar();
		a1.setAoi(new AoI(a1.getPosition(),2));
		Avatar a2 = new Avatar(new Point(1,1));
		a2.setAoi(new AoI(a2.getPosition(),2));
		MMVEObject o1 = new MMVEObject(new Point(10,10));
		o1.addInterest(a1);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertFalse(HistoricInterest.saveInterestPatternToFile(interestGraph, "this will not work"));
		interestGraph.addVertex(a1);
		interestGraph.addVertex(a2);
		interestGraph.addVertex(o1);
		interestGraph = InterestGraphConstruction.constructObjectEdges(interestGraph,1,2);
		assertEquals(3, interestGraph.edgeSet().size());
		HistoricInterest.saveInterestPatternToFile(interestGraph, "testHistoricSave.txt");
		String filePath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\GraphInput\\HistoricInterest\\testHistoricSave.txt";
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader (filePath));
			assertEquals("3 3", reader.readLine());
			assertEquals("1 2 1.0", reader.readLine());
			assertEquals("2 1 1.0", reader.readLine());
			assertEquals("3 1 2.0", reader.readLine());
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testReadHistoricInterestFromFile() 
	{
		MMVEObject.counter = 0;
		Avatar a1 = new Avatar();
		a1.setAoi(new AoI(a1.getPosition(),2));
		Avatar a2 = new Avatar(new Point(1,1));
		a2.setAoi(new AoI(a2.getPosition(),2));
		MMVEObject o1 = new MMVEObject(new Point(10,10));
		o1.addInterest(a1);
		DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph = new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		interestGraph.addVertex(a1);
		interestGraph.addVertex(a2);
		interestGraph.addVertex(o1);
		interestGraph.addEdge(o1, a1);
		interestGraph = HistoricInterest.readHistoricInterestFromFile(interestGraph, "testHistoricSave.txt", 0.5);
		assertEquals(3, interestGraph.edgeSet().size());
		assertEquals(0.5, interestGraph.getEdgeWeight(interestGraph.getEdge(a1, a2)),0);
		assertEquals(0.5, interestGraph.getEdgeWeight(interestGraph.getEdge(a2, a1)),0);
		assertEquals(1.0, interestGraph.getEdgeWeight(interestGraph.getEdge(o1, a1)),0);
	}

}
