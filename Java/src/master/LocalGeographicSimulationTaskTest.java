package master;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.junit.Test;


public class LocalGeographicSimulationTaskTest 
{
	@Test
	public void testRegionDivision()
	{
		ArrayList<Region> result = new ArrayList<Region>();
		
		Rectangle gameWorld = new Rectangle(0, 0, 500, 500);
		int peerNumber = 10;
		//calculate size of each region
		double regionArea = (gameWorld.getSize().width * gameWorld.getSize().height) / peerNumber;
		int regionHeight = (int)Math.sqrt(regionArea);
		int regionWidth = (int)Math.sqrt(regionArea);
	
		int regionsPerRow = 0;
		//determine how many regions fit in one column
		Point p = new Point(regionWidth, 0);
		while(gameWorld.contains(p))
		{
			regionsPerRow++;
			p.setLocation(p.x + regionWidth, p.y);
		}
		
		assertEquals(3,regionsPerRow);
		
		p.setLocation(0, regionHeight);
		int regionsPerColumn = 0;
		while(gameWorld.contains(p))
		{
			regionsPerColumn++;
			p.setLocation(p.x, p.y + regionHeight);
		}
		
		assertEquals(3,regionsPerColumn);
		
		//add the regions to the result
		for(int i=0; i<regionsPerRow; i++)
		{
			
			for(int j=0;j<regionsPerColumn; j++)
			{
				Region regionToAdd = new Region(i * regionWidth, j * regionHeight, regionWidth, regionHeight);
				result.add(regionToAdd);
			}
			
		}
		
		//check for any unassigned parts of the map
		if(result.size() != peerNumber)
		{
			//y position is on top of the highest row
			int topYPosition = regionsPerColumn * regionHeight;
			//width is equal to the total width of all regions in a row
			int topRemainingWidth = regionsPerRow * regionWidth;
			//the height is the border of the gameWorld minus the maximum height of the regions
			int topRemainingHeight = (int)gameWorld.getHeight()- regionsPerColumn * regionHeight;
			Region remainingTop = new Region(0, topYPosition , topRemainingWidth,  topRemainingHeight);
			remainingTop.setRemainingRegion(true);
			result.add(remainingTop);
			//x position is next to the last region in a row
			int bottomXPostion = regionsPerRow * regionWidth;
			//the width is the border of the gameWorld minus the maximum width of the regions
			int bottomRemainingWidth = (int)gameWorld.getWidth() - regionsPerRow * regionWidth;
			//height is equal to the total height of the gameworld
			int bottomRemainingHeight = (int)gameWorld.getHeight();
			Region remainingBottom = new Region(bottomXPostion, 0, bottomRemainingWidth, bottomRemainingHeight);
			remainingBottom.setRemainingRegion(true);
			result.add(remainingBottom);
			
		}
		
		assertEquals(11, result.size());
	}

}
