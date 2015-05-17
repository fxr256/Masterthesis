package master;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Test;

public class RegionAssignmentTest {

	@Test
	public void testFindRegionContainingObjectByPosition() 
	{
		MMVEObject.counter = 0;
		Region.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		Avatar a1 = new Avatar();
		
		ArrayList<Region> regions = new ArrayList<Region>();
		Region r1 = new Region(10,10, 20, 20);
		o1.setPosition(new Point(11,11));
		o2.setPosition(new Point(15,15));
		Region r2 = new Region(100,100,30,30);
		o3.setPosition(new Point(200,200));
		a1.setPosition(new Point(110,110));
		
		assertNull(RegionAssignment.findRegionContainingObjectByPosition(o1, regions));
		regions.add(r1);
		assertNull(RegionAssignment.findRegionContainingObjectByPosition(o3, regions));
		regions.add(r2);
		assertEquals(1, RegionAssignment.findRegionContainingObjectByPosition(o1, regions).getID());
		assertNull(RegionAssignment.findRegionContainingObjectByPosition(o3, regions));
	}

	@Test
	public void testObjectsInSameRegion() 
	{
		
		MMVEObject.counter = 0;
		Region.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		Avatar a1 = new Avatar();
		
		ArrayList<Region> regions = new ArrayList<Region>();
		
		Region r1 = new Region(10,10, 20, 20);
		o1.setPosition(new Point(11,11));
		o2.setPosition(new Point(15,15));
		Region r2 = new Region(100,100,30,30);
		o3.setPosition(new Point(200,200));
		a1.setPosition(new Point(110,110));
		
		assertFalse(RegionAssignment.objectsInSameRegion(regions, o1, o2));
		regions.add(r1);
		assertFalse(RegionAssignment.objectsInSameRegion(regions, o1, a1));
		regions.add(r2);
		assertFalse(RegionAssignment.objectsInSameRegion(regions, o1, o3));
		assertFalse(RegionAssignment.objectsInSameRegion(regions, o1, a1));
		assertTrue(RegionAssignment.objectsInSameRegion(regions, o1, o2));
	}

	@Test
	public void testGetRegionWeight() 
	{
		MMVEObject.counter = 0;
		Region.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		Avatar a1 = new Avatar();
		Region r = new Region(10,10, 20 ,20);
		assertEquals(0, RegionAssignment.getRegionWeight(r));
		r.getObjectsInRegion().add(o1);
		r.getObjectsInRegion().add(o2);
		r.getObjectsInRegion().add(o3);
		r.getObjectsInRegion().add(a1);
		assertEquals(4, RegionAssignment.getRegionWeight(r));
	}

	@Test
	public void testGetRegionsManagedByPeer() 
	{
		MMVEObject.counter = 0;
		Region.counter = 0;
		MMVEObject o1 = new MMVEObject();
		MMVEObject o2 = new MMVEObject();
		MMVEObject o3 = new MMVEObject();
		Avatar a1 = new Avatar();
		Region r1 = new Region(10,10, 20 ,20);
		Region r2 = new Region(100,100,10,10);
		Peer p = new Peer();
		r1.setManagingPeer(p);
		r2.setManagingPeer(p);		
		r1.getObjectsInRegion().add(o1);
		r1.getObjectsInRegion().add(o2);
		r2.getObjectsInRegion().add(o3);
		r2.getObjectsInRegion().add(a1);
		ArrayList<Region> regions = new ArrayList<Region>();
		assertEquals(0, RegionAssignment.getRegionsManagedByPeer(p, regions).size());
		regions.add(r1);
		regions.add(r2);
		assertEquals(2, RegionAssignment.getRegionsManagedByPeer(p, regions).size());
		
	}

}
