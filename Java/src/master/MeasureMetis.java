package master;



import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

public class MeasureMetis 
{
	
	@Test
	public void measureAvgProcessingTime()
	{
		final String metispath;
		int partitionnumber = 20;
		if (partitionnumber > 8)
		{
			metispath  = "D:\\Downloads\\metis3\\kmetis.exe";
		}				
		else
		{
			metispath  = "D:\\Downloads\\metis3\\pmetis.exe";
		}
		
		String FileName = "D:\\Downloads\\metis3\\Graphs\\4elt.graph";
		
		ArrayList<Long> runtimes = new ArrayList<Long>();
		try
		{			
			
			Runtime rt = Runtime.getRuntime();
			//execute this graph partitioning 40 times and add the runtimes to an arraylist
			for(int i=0; i<40;i++)
			{
				//start measure execution time
				Stopwatch timer = new Stopwatch().start();
				Process metis = rt.exec(metispath + " "  + FileName + " " + partitionnumber);
			
				//get the output of metis and print it on the console
				BufferedReader in = new BufferedReader(new InputStreamReader(metis.getInputStream()));
				String str = null;
			
				while ((str = in.readLine()) != null)
				{
				 System.out.println(str);
				}
			 
				in.close();
				///this causes the java program to wait until metis is done
				//if this is missing, the partition file may not be found
				metis.waitFor();
				//stop the timer
				timer.stop();
				runtimes.add(timer.getElapsedTime());
			
			
				//delete file because we dont need it anymore
				File graphfile = new File (FileName + ".part." + partitionnumber);
				
				if(graphfile.exists())
				{
					graphfile.delete();
				}
			}
			
			Iterator<Long> itRuntimes = runtimes.iterator();
			long averageRuntime = 0;
			while(itRuntimes.hasNext())
			{
				averageRuntime = averageRuntime + itRuntimes.next();
				
			}
			
			averageRuntime = averageRuntime/partitionnumber;
			
			assertTrue(averageRuntime > 0);
			
			System.out.println("Average Runtime: " + averageRuntime + "ms");
			
					
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

}
