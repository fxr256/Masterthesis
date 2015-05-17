package master;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.Test;


public class MovedObjectsTest 
{
	
	@Test
	public void testMovedObjectsCalculationDifferentSize()
	{
		ArrayList<Peer> currentAssignment = new ArrayList<Peer>();
		ArrayList<Peer> oldAssignment = new ArrayList<Peer>(); 
		
		Peer p1 = new Peer();
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		p1.getManagedObjects().addVertex(o1);
		p1.getManagedObjects().addVertex(a1);
		p1.getManagedObjects().addEdge(o1, a1);
		p1.getManagedObjects().addEdge(a1, o1);
		
		Peer p2 = new Peer();
		p2.getManagedObjects().addVertex(o2);
		p2.getManagedObjects().addVertex(a2);
		p2.getManagedObjects().addEdge(o2, a2);
		p2.getManagedObjects().addEdge(a2, o2);
		
		currentAssignment.add(p1);
		currentAssignment.add(p2);
		
		oldAssignment = Util.copyPeerList(currentAssignment);
		
		currentAssignment.clear();	
		p1.setManagedObjects(new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class));
		p2.setManagedObjects(new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class));
		p1.getManagedObjects().addVertex(o1);
		
		p2.getManagedObjects().addVertex(a1);
		p2.getManagedObjects().addVertex(o2);
		p2.getManagedObjects().addVertex(a2);
		p2.getManagedObjects().addEdge(o2, a2);
		p2.getManagedObjects().addEdge(a2, o2);
		currentAssignment.add(p1);
		currentAssignment.add(p2);
		
		Iterator<Peer> itCurrent = currentAssignment.iterator();
		int movingObjects = 0;
		while(itCurrent.hasNext())
		{
			Peer peerToCheck = itCurrent.next();
			//compare this to same peer from last period
			Peer oldPeer = Util.getPeerWithCopyID(oldAssignment, peerToCheck.getID());
			if(oldPeer != null)
			{
//				if(!peerToCheck.getManagedObjects().equals(oldPeer.getManagedObjects()))
//				{
//					int i = Math.abs(peerToCheck.getManagedObjects().vertexSet().size() - oldPeer.getManagedObjects().vertexSet().size());
//					movingObjects =  movingObjects + i;
//					
//										
//				}
				
				Iterator<MMVEObject> itCurrentPartition = peerToCheck.getManagedObjects().vertexSet().iterator();
				while(itCurrentPartition.hasNext())
				{
					MMVEObject objectToCheck = itCurrentPartition.next();
					if(!oldPeer.getManagedObjects().containsVertex(objectToCheck))
					{
						//this object is new so it has moved to us
						movingObjects++;
					}
				}
				
				Iterator<MMVEObject> itOldPartition = oldPeer.getManagedObjects().vertexSet().iterator();
				while(itOldPartition.hasNext())
				{
					MMVEObject objectToCheck = itOldPartition.next();
					if(!peerToCheck.getManagedObjects().containsVertex(objectToCheck))
					{
						//this object is not part of our partition anymore, so it is gone
						movingObjects++;
					}
				}
			}
		}
		
		movingObjects = movingObjects/2;
		
		assertEquals(1, movingObjects);
	}
	
	@Test
	public void testMovedObjectsCalculationSameSize()
	{

		ArrayList<Peer> currentAssignment = new ArrayList<Peer>();
		ArrayList<Peer> oldAssignment = new ArrayList<Peer>(); 
		
		Peer p1 = new Peer();
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		Avatar a1 = new Avatar();
		Avatar a2 = new Avatar();
		p1.getManagedObjects().addVertex(o1);
		p1.getManagedObjects().addVertex(a1);
		p1.getManagedObjects().addEdge(o1, a1);
		p1.getManagedObjects().addEdge(a1, o1);
		
		Peer p2 = new Peer();
		p2.getManagedObjects().addVertex(o2);
		p2.getManagedObjects().addVertex(a2);
		p2.getManagedObjects().addEdge(o2, a2);
		p2.getManagedObjects().addEdge(a2, o2);
		
		currentAssignment.add(p1);
		currentAssignment.add(p2);
		
		oldAssignment = Util.copyPeerList(currentAssignment);
		
		currentAssignment.clear();	
		p1.setManagedObjects(new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class));
		p2.setManagedObjects(new DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge>(DefaultWeightedEdge.class));
		
		//flip some objects
		p1.getManagedObjects().addVertex(o1);
		p1.getManagedObjects().addVertex(a2);
		
		p2.getManagedObjects().addVertex(a1);
		p2.getManagedObjects().addVertex(o2);
	
		currentAssignment.add(p1);
		currentAssignment.add(p2);
		
		Iterator<Peer> itCurrent = currentAssignment.iterator();
		int movingObjects = 0;
		while(itCurrent.hasNext())
		{
			Peer peerToCheck = itCurrent.next();
			//compare this to same peer from last period
			Peer oldPeer = Util.getPeerWithCopyID(oldAssignment, peerToCheck.getID());
			if(oldPeer != null)
			{

				
				Iterator<MMVEObject> itCurrentPartition = peerToCheck.getManagedObjects().vertexSet().iterator();
				while(itCurrentPartition.hasNext())
				{
					MMVEObject objectToCheck = itCurrentPartition.next();
					if(!oldPeer.getManagedObjects().containsVertex(objectToCheck))
					{
						//this object is new so it has moved to us
						movingObjects++;
					}
				}
				
				Iterator<MMVEObject> itOldPartition = oldPeer.getManagedObjects().vertexSet().iterator();
				while(itOldPartition.hasNext())
				{
					MMVEObject objectToCheck = itOldPartition.next();
					if(!peerToCheck.getManagedObjects().containsVertex(objectToCheck))
					{
						//this object is not part of our partition anymore, so it is gone
						movingObjects++;
					}
				}
			}
		}
		
		movingObjects = movingObjects/2;
		
		assertEquals(2, movingObjects);
		
	}

}
