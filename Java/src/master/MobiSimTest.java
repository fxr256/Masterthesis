package master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;

public class MobiSimTest {

	@Test
	public void testGetAvatarsAndInitialPositions() 
	{
		//test function with non existing file
		assertNull(MobiSim.getAvatarsAndInitialPositions("bla"));
		//test function with empty file
		assertEquals(0, MobiSim.getAvatarsAndInitialPositions("empty.txt").size());
		//test function with file containing rubbish
		assertNull(MobiSim.getAvatarsAndInitialPositions("rubbish.txt"));
		
		//test function with correct input
		Avatar.counter=0;
		
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		String filename = "trace.txt";
		
		list = MobiSim.getAvatarsAndInitialPositions(filename);
		
		assertEquals(20,list.size());
		assertEquals(230, list.get(0).getPosition().x);
		assertEquals(141, list.get(0).getPosition().y);
		assertEquals(219, list.get(19).getPosition().x);
		assertEquals(286, list.get(19).getPosition().y);
	}

	@Test
	public void testGetAvatarPosition() 
	{
		//test function with non existing file
		assertNull(MobiSim.getAvatarOrObjectPosition("bla", 0, 1));
		//test function with empty file
		assertNull(MobiSim.getAvatarOrObjectPosition("empty.txt", 0, 1));
		//test function with file containing rubbish
		assertNull(MobiSim.getAvatarOrObjectPosition("rubbish.txt", 0, 1));
		//test function with a valid file, but the avatar does not exist
		assertNull(MobiSim.getAvatarOrObjectPosition("trace.txt", 21, 1));
		//test function with a valid file, but the round does not exist
		assertNull(MobiSim.getAvatarOrObjectPosition("trace.txt", 21, 0));
		//test function with a valid file
		
		String filename = "trace.txt";
		Avatar.counter=0;
		Avatar a1 = new Avatar();
		a1.setPosition(MobiSim.getAvatarOrObjectPosition(filename, a1.getMobiSimID(), 10));
		
		assertEquals(190, a1.getPosition().x);
		assertEquals(172, a1.getPosition().y);
	}
	
	@Test
	public void testGetObjectsAndInitialPositions() 
	{
		//test function with non existing file
		assertNull(MobiSim.getObjectsAndInitialPositions("bla"));
		//test function with empty file
		assertEquals(0, MobiSim.getObjectsAndInitialPositions("empty.txt").size());
		//test function with file containing rubbish
		assertNull(MobiSim.getObjectsAndInitialPositions("rubbish.txt"));
		
		//test function with correct input
		MMVEObject.counter=0;
		
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		String filename = "objects.txt";
		
		list = MobiSim.getObjectsAndInitialPositions(filename);
		
		assertEquals(15,list.size());
		assertEquals(16, list.get(0).getPosition().x);
		assertEquals(353, list.get(0).getPosition().y);
		assertEquals(389, list.get(14).getPosition().x);
		assertEquals(154, list.get(14).getPosition().y);
	}
	
	@Test
	public void testProcessMovementFile()
	{
		//test function with non existing file
		assertNull(MobiSim.processMovementFile("this file does not exist"));
		//test for correct input
		ArrayList<Movement> result = MobiSim.processMovementFile("processMovementFileTest.txt");
		assertEquals(9990, result.size());
		Movement movementToCheck = result.get(0);
		assertEquals(2, movementToCheck.getRound());
		assertEquals(0, movementToCheck.getMovingObjectID());
		assertEquals(374, movementToCheck.getNewPosition().x);
		assertEquals(159, movementToCheck.getNewPosition().y);
		
	}

}
