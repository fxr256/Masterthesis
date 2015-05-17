package master;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class CrowdingMovementFileGenerator 
{
	
	public static void generateCrowdingMovementFile(String fileName, String mobiSimFileForInitialPositions, Point destination,
			int minSpeed, int maxSpeed, int roundNumber)
	{
		try
		{
			ArrayList<MMVEObject> objectList = MobiSim.getObjectsAndInitialPositions(mobiSimFileForInitialPositions);
			File crowdingFile = new File(MobiSim.mobiSimOutputPath + fileName);
			
			if(crowdingFile.exists())
			{
				crowdingFile.delete();
			}
			
			if(!crowdingFile.exists())
			{
				crowdingFile.createNewFile();
			}
			
			int counter = 1;
			Random randomGenerator = new Random();
			PrintWriter out = new PrintWriter(new FileWriter(crowdingFile));
			out.println("destination: " + destination + " minSpeed: " + minSpeed + " maxSpeed " + maxSpeed 
					+ " roundNumber " + roundNumber);
			out.println("round nodenumber, x, y");
			
			while(counter<=roundNumber)
			{
				Iterator<MMVEObject> itObjectList = objectList.iterator();
				while(itObjectList.hasNext())
				{
					MMVEObject objectToMove = itObjectList.next();
					int speed = randomGenerator.nextInt(maxSpeed+1);
					if(speed < minSpeed)
					{
						speed = minSpeed;
					}
					objectToMove.setPosition(getNewPosition(objectToMove.getPosition(), speed, destination));
					out.println(counter + "	" + objectToMove.getMobiSimID() + "	" + 
							objectToMove.getPosition().x + "	" +objectToMove.getPosition().y);
				}
				out.flush();
				counter++;
			}
			
			out.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static Point getNewPosition(Point position, int speed,
			Point destination) 
	{
		if(speed == 0)
			return position;
	
		
		//okay, we are going to try this with vectors
		//first of all create a vector between the current position and the destination
		Point vector = new Point(destination.x - position.x, destination.y - position.y);
		//we want the point to move a certain distance (= speed), however, we use vectors here so we can only move in percentages
		//of the vector connecting the current position and the destination
		//therefore we calculate the division of the desired speed and the vector length
		double vectorLength = Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
		double percentageToMove = speed / vectorLength;
		if(speed >= vectorLength)
		{
			percentageToMove = 1;
		}
		//now multiplay the vector with how far we want to move => speed
		vector.x = (int)Math.ceil(vector.x * percentageToMove);
		vector.y = (int)Math.ceil(vector.y * percentageToMove);
		
		//add the vector to the current position to form the new one
		int newX = position.x + vector.x;
		int newY = position.y + vector.y;
		
		if(position.x < destination.x && newX >= destination.x)
		{
			newX = destination.x;
				
		}
		
		if(position.x > destination.x && newX <= destination.x)
		{
			newX = destination.x;
				
		}
		
		if(position.y < destination.y && newY >= destination.y)
		{
			newY = destination.y;
				
		}
		
		if(position.y > destination.y && newY <= destination.y)
		{
			newY = destination.y;
				
		}
		
		
//		//we are going to use the easy way here, I can not into geometry
//		int newX = 0;
//		int newY = 0;
//		if(position.x < destination.x && (position.x + speed)<= destination.x)
//		{
//			newX = position.x + speed;
//			
//		}
//		else
//		{
//			if(position.x > destination.x && (position.x - speed)>= destination.x)
//			{
//				newX = position.x - speed;
//			}
//			else
//			{
//				//this should only be called, if the point has either already reached its position
//				//or if the distance is too small => set newX = destination.X
//				newX = destination.x;
//			}
//			
//		}
//		
//		if(position.y < destination.y && (position.y + speed)<= destination.y)
//		{
//			newY = position.y + speed;
//			
//		}
//		else
//		{
//			if(position.y > destination.y && (position.y - speed)>= destination.y)
//			{
//				newY = position.y - speed;
//			}
//			else
//			{
//				//this should only be called, if the point has either already reached its position
//				//or if the distance is too small => set newY = destination.Y
//				newY = destination.y;
//			}
//			
//		}
		
		return new Point(newX,newY);
	}

}
