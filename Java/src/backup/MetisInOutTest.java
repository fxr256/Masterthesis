package backup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;

public class MetisInOutTest {

	@Test
	public void testConvertVertexGraphToGraphFile() {

		//test for empty graph
		DirectedWeightedMultigraph<Vertex, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertFalse(MetisInOut.convertVertexGraphToGraphFile(h, "D:\\Downloads\\metis3\\Graphs\\empty.graph"));
		
		//test for correct values
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
		
		String FileName = "D:\\Downloads\\metis3\\Graphs\\test.graph";
		
		MetisInOut.convertVertexGraphToGraphFile(g, FileName);
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
		assertTrue(MetisInOut.convertVertexGraphToGraphFile(g, "bla.bla"));
		//delete file afterwards
		File graphfile = new File ("bla.bla");
		
		if(graphfile.exists())
		{
			graphfile.delete();
		}
		
	
	}

	


	@Test
	public void testConvertVertexGraphToSimpleGraphFile() 
	{
		//test for empty graph file
		DirectedWeightedMultigraph<Vertex, DefaultWeightedEdge> h = new DirectedWeightedMultigraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		assertFalse(MetisInOut.convertVertexGraphToSimpleGraphFile(h, "D:\\Downloads\\metis3\\Graphs\\empty.graph"));
		
		//test for valid input
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
		g.addEdge(v1, v2);
		g.addEdge(v2, v1);
		g.addEdge(v2, v3);
		g.addEdge(v3, v2);
		g.addEdge(v3, v4);
		g.addEdge(v4, v3);
	
		
		String FileName = "D:\\Downloads\\metis3\\Graphs\\testsimple.graph";
		
		MetisInOut.convertVertexGraphToSimpleGraphFile(g, FileName);
		
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
		assertTrue(MetisInOut.convertVertexGraphToGraphFile(g, "bla.bla"));
		//delete file afterwards
		File graphfile = new File ("bla.bla");
		
		if(graphfile.exists())
		{
			graphfile.delete();
		}
		
		
	}

	
	
}
