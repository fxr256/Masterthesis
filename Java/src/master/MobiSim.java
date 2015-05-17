package master;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MobiSim 
{
	public static final String mobiSimOutputPath =  "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\mobisim\\output\\";
	//extracts all starting positions out of a trace file and creates avatars with these positions
	//returns null if the file could not be found or if the input is corrupted
	public static ArrayList<Avatar> getAvatarsAndInitialPositions(String FileName)
	{
		
		ArrayList<Avatar> list = new ArrayList<Avatar>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader (mobiSimOutputPath + FileName));
			
			//first two lines are of no interest for us, so skip them
			reader.readLine();
			reader.readLine();
			
			String line = "";
			int round = 0;
			do
			{
				line = reader.readLine();
				if(line == null)
					break;
				String[] content = new String[7];
				//this should convert the line into an array, using tab to seperate the values
				content = line.split("	");
				round = Integer.parseInt(content[0]);
				//the position of an avatar is the 3 and 4. value in a line. this lines creates a new avatar with these values
				//checks whether round == 1, because otherwise we also read the first line of the next round (do-while ftw)
				if(round==1)
				{
					Avatar avatarToAdd = new Avatar(new Point(Integer.parseInt(content[2]),Integer.parseInt(content[3])));
					avatarToAdd.setMobiSimID(Integer.parseInt(content[1]));
					list.add(avatarToAdd);
				}
				
				
			}
			while (round == 1);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return list;
	}

	//returns the position of an avatar given a tracefile,a certain avatar and a certain round
	//return null if the file does not exists or a matching entry could not be found
	public static Point getAvatarOrObjectPosition(String FileName, int objectID, int round)
	{
		Point result = null;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader (mobiSimOutputPath + FileName));
			String line = "";
			
			//again skip the first two lines
			reader.readLine();
			reader.readLine();
			
			while((line = reader.readLine())!= null)
			{
				String[] content = line.split("	");
				//check whether we are in the matching round
				if(Integer.parseInt(content[0])== round)
				{
					
					if(Integer.parseInt(content[1])== objectID)
					{
						//we found our position and terminate the loop to achieve better performance
						result = new Point(Integer.parseInt(content[2]),Integer.parseInt(content[3]));
						
						break;
					}
					
				}
				
				if (Integer.parseInt(content[0]) > round)
				{
					//we passed the round and did not find any matching value
					//therefore we terminate the loop to increase performance
					break;
				}
				
			}
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Point getAvatarOrObjectPosition(ArrayList<Movement> movementList, int objectID, int round)
	{
		Iterator<Movement> itMovementList = movementList.iterator();
				
		while(itMovementList.hasNext())
		{
			Movement movementToProcess = itMovementList.next();
			if(movementToProcess.getMovingObjectID() == objectID && movementToProcess.getRound() == round)
			{
				return movementToProcess.getNewPosition();
			}
		}
		return null;
		
	}
	
	public static Movement getAvatarOrObjectMovement(ArrayList<Movement> movementList, int objectID, int round)
	{
		Iterator<Movement> itMovementList = movementList.iterator();
				
		while(itMovementList.hasNext())
		{
			Movement movementToProcess = itMovementList.next();
			if(movementToProcess.getMovingObjectID() == objectID && movementToProcess.getRound() == round)
			{
				return movementToProcess;
			}
		}
		return null;
		
	}
	
	public static ArrayList<MMVEObject> getObjectsAndInitialPositions(String FileName)
	{
		
		ArrayList<MMVEObject> list = new ArrayList<MMVEObject>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader (mobiSimOutputPath + FileName));
			
			//first two lines are of no interest for us, so skip them
			reader.readLine();
			reader.readLine();
			
			String line = "";
			int round = 0;
			do
			{
				line = reader.readLine();
				if(line == null)
					break;
				String[] content = new String[7];
				//this should convert the line into an array, using tab to seperate the values
				content = line.split("	");
				round = Integer.parseInt(content[0]);
				//the position of an object is the 3 and 4. value in a line. this lines creates a new avatar with these values
				//checks whether round == 1, because otherwise we also read the first line of the next round (do-while ftw)
				if(round==1)
				{
					MMVEObject objectToAdd = new MMVEObject(new Point(Integer.parseInt(content[2]),Integer.parseInt(content[3])));
					objectToAdd.setMobiSimID(Integer.parseInt(content[1]));
					list.add(objectToAdd);
				}
				
				
			}
			while (round == 1);
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return list;
		
	}
	
	public static ArrayList<Movement> processMovementFile(String movementFile)
	{
		ArrayList<Movement> result = new ArrayList<Movement>();
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader (mobiSimOutputPath + movementFile));
			
			//first two lines are of no interest for us, so skip them
			reader.readLine();
			reader.readLine();
			
			String line = "";
			int round = 0;
			
			while((line = reader.readLine())!= null)
			{
				String[] content = line.split("	");
				round = Integer.parseInt(content[0]);
				//we skip the first round because it was already processed when creating the objects
				if(round > 1)
				{
					int ID = Integer.parseInt(content[1]);
					Point position = new Point(Integer.parseInt(content[2]),Integer.parseInt(content[3]));
					result.add(new Movement(round, ID, position));
				}
				
			}
			
			reader.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public static HashMap<Integer, ArrayList<Movement>> processMovementFileToHashMap(String movementFile)
	{
		HashMap<Integer, ArrayList<Movement>> result = new HashMap<Integer, ArrayList<Movement>>();
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader (mobiSimOutputPath + movementFile));
			
			//first two lines are of no interest for us, so skip them
			reader.readLine();
			reader.readLine();
			
			String line = "";
			int round = 0;
			int lastRound = 2;
			ArrayList<Movement> movementInThisRound = new ArrayList<Movement>();
			while((line = reader.readLine())!= null)
			{
				String[] content = line.split("	");
				round = Integer.parseInt(content[0]);
				
				
				//we skip the first round because it was already processed when creating the objects
				if(round > 1)
				{
					int ID = Integer.parseInt(content[1]);
					Point position = new Point(Integer.parseInt(content[2]),Integer.parseInt(content[3]));
					if(round > lastRound)
					{
						result.put(lastRound, movementInThisRound);
						movementInThisRound = new ArrayList<Movement>();
						lastRound++;
						movementInThisRound.add(new Movement(round, ID, position));
					}
					else
					{
						movementInThisRound.add(new Movement(round, ID, position));
					}
					
				}
				
			}
			
			reader.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public static boolean testIfInputFileExists(String fileName)
	{
		try
		{
			String filePath = mobiSimOutputPath + fileName;
			BufferedReader reader = new BufferedReader(new FileReader (filePath));
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
