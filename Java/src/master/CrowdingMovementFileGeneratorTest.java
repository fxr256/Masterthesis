package master;

import static org.junit.Assert.assertEquals;

import java.awt.Point;

import org.junit.Test;

public class CrowdingMovementFileGeneratorTest {

	@Test
	public void testGenerateCrowdingMovementFile() 
	{
		CrowdingMovementFileGenerator.generateCrowdingMovementFile("crowdingTestVector.txt", "avatarsTiny.txt", new Point(100,100), 5, 20, 500);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 0, 50).x);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 0, 50).y);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 1, 50).x);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 1, 50).y);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 2, 50).x);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 2, 50).y);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 3, 50).x);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 3, 50).y);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 4, 50).x);
		assertEquals(100, MobiSim.getAvatarOrObjectPosition("crowdingTestVector.txt", 4, 50).y);
				
	}

}
