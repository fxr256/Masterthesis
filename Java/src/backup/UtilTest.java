package backup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class UtilTest {

	@Test
	public void testSortVertices() 
	{
		//test with empty vertex set
		DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(Util.sortVertices(h.vertexSet()));
		
		
		//test with correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Vertex.counter=0;
		DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		Vertex v3 = new Vertex("3");
		Vertex v4 = new Vertex("4");
		
	
		g.addVertex(v2);
		g.addVertex(v1);
		g.addVertex(v3);
		g.addVertex(v4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(v1, v3);
		g.addEdge(v3, v1);
		g.addEdge(v1, v4);
		g.addEdge(v4, v1);
		g.addEdge(v2, v4);
		
		ArrayList<Vertex> list = Util.sortVertices(g.vertexSet());
		assertEquals(1,list.get(0).getID());
		assertEquals(2,list.get(1).getID());
		assertEquals(3,list.get(2).getID());
		assertEquals(4,list.get(3).getID());
	}

	

	@Test
	public void testMetisEdgeNumberVertex() 
	{
		//check with empty graph
		DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertEquals(0, Util.metisEdgeNumberVertex(h));
		
		//check with correct values
		//reset vertex counter, otherwise this fucks up the testcases
		Vertex.counter=0;
		DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//The vertex class might have to be extend to reflect the avatars within a cell
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		Vertex v3 = new Vertex("3");
		Vertex v4 = new Vertex("4");
		
	
		g.addVertex(v2);
		g.addVertex(v1);
		g.addVertex(v3);
		g.addVertex(v4);
		
		//because default edgeweight is 1 we don't have to change this
		//in this testmethod
		g.addEdge(v1, v3);
		g.addEdge(v3, v1);
		g.addEdge(v1, v4);
		g.addEdge(v4, v1);
		g.addEdge(v2, v4);
		
		assertEquals(3,Util.metisEdgeNumberVertex(g));
	}

	

	@Test
	public void testGetVertixWithID() 
	{
		
		//check with empty graph
		DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertNull(Util.getVertixWithID(h, 1));
		
		Vertex.counter = 0;
		Vertex v1 = new Vertex();
		Vertex v2 = new Vertex();
		
		DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g = new DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		g.addVertex(v1);
		
		//check for valid graph, but vertex not found
		assertNull(Util.getVertixWithID(g, v2.getID()));
		//check for valid graph and valid vertex
		assertEquals(v1, Util.getVertixWithID(g, v1.getID()));
		
		
	}

	

}
