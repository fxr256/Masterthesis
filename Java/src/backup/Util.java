package backup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class Util 
{
	//sorts the vertices as defined by the comparable interface in vertex.java
	//returns an ArrayList, because Sets can't be sorted
	//returns null if the set is empty
	public static ArrayList<Vertex> sortVertices(Set<Vertex> s)
	{
		if(s.isEmpty())
			return null;
		ArrayList<Vertex> list = new ArrayList<Vertex>();
		list.addAll(s);
		Collections.sort(list);
		
		return list;
	}
	
	
	//purpose of this function is to eliminate all "double edges" so Metis can work with the result
	//Metis does count the the edge e(u,v) and e(v,u) as one
	//returns 0 for an empty graph
	public static int metisEdgeNumberVertex(DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g)
	{
		Set<DefaultWeightedEdge> s = g.edgeSet();
		ArrayList<DefaultWeightedEdge> l = new ArrayList<DefaultWeightedEdge>();
		Iterator<DefaultWeightedEdge> itS = s.iterator();
		
		while (itS.hasNext())
		{
			DefaultWeightedEdge e = itS.next(); 
			Vertex source = g.getEdgeSource(e);
			Vertex target = g.getEdgeTarget(e);
			if (g.containsEdge(target, source))
			{
				DefaultWeightedEdge f = g.getEdge(target, source);
				if(!l.contains(f))
					l.add(e);
			}
			else
			{
				l.add(e);
			}
		}
		
		return l.size();
	}
	
	
	
	
	//searches the vertices of a graph for a vertex with a certain id, returns null if nothing is found
	public static Vertex getVertixWithID(DirectedWeightedMultigraph<Vertex,DefaultWeightedEdge> g, int ID)
	{
		Set<Vertex> vertices = g.vertexSet();
		Iterator<Vertex> itG = vertices.iterator();
		Vertex u = null;
		while (itG.hasNext())
		{
			Vertex v = itG.next();
			if(v.getID()==ID)
				u=v;
		}
		
		return u;
	}
	
	
	
	

	
	
	
}
