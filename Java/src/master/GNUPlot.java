package master;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GNUPlot 
{
	
	private static final String gnuPlotPath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\gnuplot\\binary\\gnuplot.exe";
	private static final String modifiedCSVfilePath = "D:\\\\Dokumente\\\\My Dropbox\\\\Uni\\\\Semester 4\\\\CSV\\\\";
	
	// double ////s are used, because gnuPlot can't deal with single \ in filenames
	// \n represents pressing enter
	public static void createGraph(String csvFileName, String graphName)
	{
		try
		{
			String csvFile = LocalSimulationTask.getCsvfilepath() + csvFileName;
			File csv = new File(csvFile);
			
			if(csv.exists())
			{
				//change csvFile so it can be used as a command in gnuplot
				String modifiedCSVFile = "'" + modifiedCSVfilePath + csvFileName + "'";
				Runtime rt = Runtime.getRuntime();
				
				Process gnuplot = rt.exec(gnuPlotPath);
				
				//get the output of metis and print it on the console
				BufferedReader in = new BufferedReader(new InputStreamReader(gnuplot.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(gnuplot.getOutputStream()));
				String str = null;
				
				out.write("set terminal png font 'Times-Roman, 14' size 800,600 \n");
				//out.write("set terminal pdf font 'Times-Roman, 9' \n" );
				out.write("set key right top \n");
				out.write("set datafile separator ','\n");
				
				out.write("set auto x\n");
				out.write("set yrange [0:] \n");
				out.write("set xlabel 'rounds'\n");
				out.write("set ylabel 'values'\n");

				
				String outputFile = "set output " + "'" + modifiedCSVfilePath + graphName + "Interactions.png' \n";
				out.write(outputFile);
				
				out.write("set style line 1 lt 1 lw 1 lc rgb 'red' \n");
				out.write("set style line 2 lt 1 lw 1 lc rgb 'forest-green' \n");
				out.write("set style line 3 lt 1 lw 1 lc rgb 'royalblue' \n");
				out.write("set style line 4 lt 1 lw 1 lc rgb 'purple'  \n");
				out.write("set style line 5 lt 1 lw 1 lc rgb 'gold' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'orange' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'dark-grey' \n");
				
//				old histogram style, not needed anymore because we only plot one value per graph
//				out.write("set style data histograms \n");
//				out.write("set style histogram clustered gap 1 \n");
//				out.write("set style fill solid border -1 \n");
//				out.write("set boxwidth -2 \n");
				
				out.write("set style boxes \n");
				out.write("set style fill solid border -1 \n");
				out.write("set boxwidth 0.5 relative \n");

				
				//plot interactions
				out.write("set ylabel 'interactions between peers'\n");
				String command = "plot " + modifiedCSVFile + " using 1:2 with boxes ls 1 notitle \n";
				
				out.write(command);
				
				//plot executions
				outputFile = "set output " + "'" + modifiedCSVfilePath + graphName + "ExecutionCost.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'execution cost(ms)'\n");
//				command = "plot " + modifiedCSVFile + " using 3 ls 2 title columnheader(3) \n";
				command = "plot " + modifiedCSVFile + " using 1:3 with boxes ls 2 notitle \n";
				out.write(command);
				
				//plot communication
				outputFile = "set output " + "'" + modifiedCSVfilePath + graphName + "CommunicationCost.png '\n";
				out.write(outputFile);	
				out.write("set ylabel 'communication cost'\n");
				command = "plot " + modifiedCSVFile + " using 1:4 with boxes ls 3 notitle \n";
				out.write(command);
				
				//plot reassignments
				outputFile = "set output " + "'" + modifiedCSVfilePath + graphName + "Reassignments.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'reassignments'\n");
				command = "plot " + modifiedCSVFile + " using 1:5 with boxes ls 4 notitle \n";
				out.write(command);
				
				//plot overloaded
				outputFile = "set output " + "'" + modifiedCSVfilePath + graphName + "Overloaded.png '\n";
				out.write(outputFile);	
				out.write("set ylabel 'overloaded peers'\n");
				command = "plot " + modifiedCSVFile + " using 1:6 with boxes ls 5 notitle \n";
				out.write(command);
				
				//plot distribution factor
				outputFile = "set output " + "'" + modifiedCSVfilePath + graphName + "Distribution.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'distribution factor'\n");
				command = "plot " + modifiedCSVFile + " using 1:7 with boxes ls 6 notitle \n";
				out.write(command);
				
				out.write("quit\n");
				out.flush();
				out.close();
				
				
				 while ((str = in.readLine()) != null)
				 {
					 System.out.println(str);
				 }
				 in.close();
		
				//this causes the java program to wait until gnuplot is done
				//if this is missing, the partition file may not be found
				int exitvalue = gnuplot.waitFor();
				System.out.println("Exitvalue :" + exitvalue);
				
			}
			
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void createGraphForAggregatedValueFile(String outputFileName, String csvFileName, String directory, String variedValue)
	{
		try
		{
			if(!directory.endsWith("\\"))
			{
				directory = directory + "\\";
			}
			String csvFile = LocalSimulationTask.getCsvfilepath() + directory + csvFileName;
			File csv = new File(csvFile);
			
			if(csv.exists())
			{
				//change csvFile so it can be used as a command in gnuplot
				String modifiedCSVFile = "'" + modifiedCSVfilePath + directory + "\\" + csvFileName + "'";
				Runtime rt = Runtime.getRuntime();
				
				Process gnuplot = rt.exec(gnuPlotPath);
				
				//get the output of metis and print it on the console
				BufferedReader in = new BufferedReader(new InputStreamReader(gnuplot.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(gnuplot.getOutputStream()));
				String str = null;
				
				out.write("set terminal png font 'Times-Roman, 14' size 800,600 \n");
				//out.write("set terminal pdf font 'Times-Roman, 9' \n" );
				out.write("set key right top \n");
				out.write("set datafile separator ','\n");
				
				out.write("set auto x\n");
				out.write("set yrange [0:] \n");
				out.write("set xlabel '" + variedValue + "'\n");
				out.write("set ylabel 'values'\n");

				
				String outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  +  outputFileName + "Interactions.png' \n";
				out.write(outputFile);
				
				out.write("set style line 1 lt 1 lw 1 lc rgb 'red' \n");
				out.write("set style line 2 lt 1 lw 1 lc rgb 'forest-green' \n");
				out.write("set style line 3 lt 1 lw 1 lc rgb 'royalblue' \n");
				out.write("set style line 4 lt 1 lw 1 lc rgb 'purple'  \n");
				out.write("set style line 5 lt 1 lw 1 lc rgb 'gold' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'orange' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'dark-grey' \n");
				
				out.write("set style boxes \n");
				out.write("set style fill solid border -1 \n");
//				out.write("set boxwidth 400 absolute \n");
				//backup for more than three bars:
				out.write("set boxwidth 0.5 relative \n");

				//plot interactions
				out.write("set ylabel 'interactions between peers'\n");
				String command = "plot " + modifiedCSVFile + " using 1:2 with boxes ls 1 notitle \n";
				out.write(command);
				
				//plot executions
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "ExecutionCost.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'execution cost(ms)'\n");
//				command = "plot " + modifiedCSVFile + " using 3 ls 2 title columnheader(3) \n";
				command = "plot " + modifiedCSVFile + " using 1:3 with boxes ls 2 notitle \n";
				out.write(command);
				
				//plot communication
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "CommunicationCost.png '\n";
				out.write(outputFile);	
				out.write("set ylabel 'communication cost'\n");
				command = "plot " + modifiedCSVFile + " using 1:4 with boxes ls 3 notitle \n";
				out.write(command);
				
				//plot reassignments
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "Reassignments.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'reassignments'\n");
				command = "plot " + modifiedCSVFile + " using 1:5 with boxes ls 4 notitle \n";
				out.write(command);
				
				//plot overloaded
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "Overloaded.png '\n";
				out.write(outputFile);	
				out.write("set ylabel 'overloaded peers'\n");
				command = "plot " + modifiedCSVFile + " using 1:6 with boxes ls 5 notitle \n";
				out.write(command);
				
				//plot distribution factor
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "Distribution.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'distribution factor'\n");
				command = "plot " + modifiedCSVFile + " using 1:7 with boxes ls 6 notitle \n";
				out.write(command);
				
				out.write("quit\n");
				out.flush();
				out.close();
				
				
				 while ((str = in.readLine()) != null)
				 {
					 System.out.println(str);
				 }
				 in.close();
		
				//this causes the java program to wait until gnuplot is done
				//if this is missing, the partition file may not be found
				int exitvalue = gnuplot.waitFor();
				System.out.println("Exitvalue :" + exitvalue);
				
			}
			
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void createGraphForAggregatedValueFile(String outputFileName, String csvFileName, String directory, String variedValue,
			int xStartingPoint, int xEndpoint, int xIterations)
	{
		try
		{
			if(!directory.endsWith("\\"))
			{
				directory = directory + "\\";
			}
			String csvFile = LocalSimulationTask.getCsvfilepath() + directory + csvFileName;
			File csv = new File(csvFile);
			
			if(csv.exists())
			{
				//change csvFile so it can be used as a command in gnuplot
				String modifiedCSVFile = "'" + modifiedCSVfilePath + directory + "\\" + csvFileName + "'";
				Runtime rt = Runtime.getRuntime();
				
				Process gnuplot = rt.exec(gnuPlotPath);
				
				//get the output of metis and print it on the console
				BufferedReader in = new BufferedReader(new InputStreamReader(gnuplot.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(gnuplot.getOutputStream()));
				String str = null;
				
				out.write("set terminal png font 'Times-Roman, 14' size 800,600 \n");
				//out.write("set terminal pdf font 'Times-Roman, 9' \n" );
				out.write("set key right top \n");
				out.write("set datafile separator ','\n");
				
				out.write("set xrange [" + (xStartingPoint - xIterations) + ":" + (xEndpoint + xIterations) + "] \n");
				out.write("set xtics " + xIterations + " \n");
				out.write("set yrange [0:] \n");
				out.write("set xlabel '" + variedValue + "'\n");
				out.write("set ylabel 'values'\n");

				
				String outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  +  outputFileName + "Interactions.png' \n";
				out.write(outputFile);
				
				out.write("set style line 1 lt 1 lw 1 lc rgb 'red' \n");
				out.write("set style line 2 lt 1 lw 1 lc rgb 'forest-green' \n");
				out.write("set style line 3 lt 1 lw 1 lc rgb 'royalblue' \n");
				out.write("set style line 4 lt 1 lw 1 lc rgb 'purple'  \n");
				out.write("set style line 5 lt 1 lw 1 lc rgb 'gold' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'orange' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'dark-grey' \n");
				
				out.write("set style boxes \n");
				out.write("set style fill solid border -1 \n");
//				out.write("set boxwidth 400 absolute \n");
				//backup for more than three bars:
				out.write("set boxwidth 0.5 relative \n");

				//plot interactions
				out.write("set ylabel 'interactions between peers'\n");
				String command = "plot " + modifiedCSVFile + " using 1:2 with boxes ls 1 notitle \n";
				out.write(command);
				
				//plot executions
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "ExecutionCost.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'execution cost(ms)'\n");
//				command = "plot " + modifiedCSVFile + " using 3 ls 2 title columnheader(3) \n";
				command = "plot " + modifiedCSVFile + " using 1:3 with boxes ls 2 notitle \n";
				out.write(command);
				
				//plot communication
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "CommunicationCost.png '\n";
				out.write(outputFile);	
				out.write("set ylabel 'communication cost'\n");
				command = "plot " + modifiedCSVFile + " using 1:4 with boxes ls 3 notitle \n";
				out.write(command);
				
				//plot reassignments
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "Reassignments.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'reassignments'\n");
				command = "plot " + modifiedCSVFile + " using 1:5 with boxes ls 4 notitle \n";
				out.write(command);
				
				//plot overloaded
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "Overloaded.png '\n";
				out.write(outputFile);	
				out.write("set ylabel 'overloaded peers'\n");
				command = "plot " + modifiedCSVFile + " using 1:6 with boxes ls 5 notitle \n";
				out.write(command);
				
				//plot distribution factor
				outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "Distribution.png '\n";
				out.write(outputFile);
				out.write("set ylabel 'distribution factor'\n");
				command = "plot " + modifiedCSVFile + " using 1:7 with boxes ls 6 notitle \n";
				out.write(command);
				
				out.write("quit\n");
				out.flush();
				out.close();
				
				
				 while ((str = in.readLine()) != null)
				 {
					 System.out.println(str);
				 }
				 in.close();
		
				//this causes the java program to wait until gnuplot is done
				//if this is missing, the partition file may not be found
				int exitvalue = gnuplot.waitFor();
				System.out.println("Exitvalue :" + exitvalue);
				
			}
			
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void createGraphForCertainCriteriaWithCertainValueRange(String outputFileName, String csvFileName, String directory, 
			String variedValue, String evaluationCriterion, double baseValue, double maxValue, double yIterations)
	{
		
		try
		{
			if(!directory.endsWith("\\"))
			{
				directory = directory + "\\";
			}
			String csvFile = LocalSimulationTask.getCsvfilepath() + directory + csvFileName;
			File csv = new File(csvFile);
			
			if(csv.exists())
			{
				//change csvFile so it can be used as a command in gnuplot
				String modifiedCSVFile = "'" + modifiedCSVfilePath + directory + "\\" + csvFileName + "'";
				Runtime rt = Runtime.getRuntime();
				
				Process gnuplot = rt.exec(gnuPlotPath);
				
				//get the output of metis and print it on the console
				BufferedReader in = new BufferedReader(new InputStreamReader(gnuplot.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(gnuplot.getOutputStream()));
				String str = null;
				
				out.write("set terminal png font 'Times-Roman, 14' size 800,600 \n");
				//out.write("set terminal pdf font 'Times-Roman, 9' \n" );
				out.write("set key right top \n");
				out.write("set datafile separator ','\n");
				
				out.write("set auto x\n");
				out.write("set yrange [" + baseValue + ":" + maxValue + "] \n");
				out.write("set xlabel '" + variedValue + "'\n");
				out.write("set ytics " + yIterations + " \n");
				out.write("set ylabel 'values'\n");

				
				String outputFile = "";
				
				
				out.write("set style line 1 lt 1 lw 1 lc rgb 'red' \n");
				out.write("set style line 2 lt 1 lw 1 lc rgb 'forest-green' \n");
				out.write("set style line 3 lt 1 lw 1 lc rgb 'royalblue' \n");
				out.write("set style line 4 lt 1 lw 1 lc rgb 'purple'  \n");
				out.write("set style line 5 lt 1 lw 1 lc rgb 'gold' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'orange' \n");
				out.write("set style line 6 lt 1 lw 1 lc rgb 'dark-grey' \n");
				
				out.write("set style boxes \n");
				out.write("set style fill solid border -1 \n");
				out.write("set boxwidth 0.5 relative \n");
				String command = "";
				
				//plot interactions
				if(evaluationCriterion.equals("interactions"))
				{
					outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  +  outputFileName + "'\n";
					out.write(outputFile);
					out.write("set ylabel 'interactions between peers'\n");
					command = "plot " + modifiedCSVFile + " using 1:2 with boxes ls 1 notitle \n";
					out.write(command);
				}
				
				
				//plot executions
				if(evaluationCriterion.equals("execution cost"))
				{
					outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "'\n";
					out.write(outputFile);
					out.write("set ylabel 'execution cost(ms)'\n");
//					command = "plot " + modifiedCSVFile + " using 3 ls 2 title columnheader(3) \n";
					command = "plot " + modifiedCSVFile + " using 1:3 with boxes ls 2 notitle \n";
					out.write(command);
				}
				
				//plot communication
				if(evaluationCriterion.equals("communication cost"))
				{
					outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "'\n";
					out.write(outputFile);	
					out.write("set ylabel 'communication cost'\n");
					command = "plot " + modifiedCSVFile + " using 1:4 with boxes ls 3 notitle \n";
					out.write(command);
				}
				
				//plot reassignments
				if(evaluationCriterion.equals("reassignments"))
				{
					outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + "'\n";
					out.write(outputFile);
					out.write("set ylabel 'reassignments'\n");
					command = "plot " + modifiedCSVFile + " using 1:5 with boxes ls 4 notitle \n";
					out.write(command);
				}
				
				
				//plot overloaded
				if(evaluationCriterion.equals("overloaded"))
				{
					outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + " '\n";
					out.write(outputFile);	
					out.write("set ylabel 'overloaded peers'\n");
					command = "plot " + modifiedCSVFile + " using 1:6 with boxes ls 5 notitle \n";
					out.write(command);
				}
				
				if(evaluationCriterion.equals("distribution factor"))
				{
					//plot distribution factor
					outputFile = "set output " + "'" + modifiedCSVfilePath + directory + "\\"  + outputFileName + " '\n";
					out.write(outputFile);
					out.write("set ylabel 'distribution factor'\n");
					command = "plot " + modifiedCSVFile + " using 1:7 with boxes ls 6 notitle \n";
					out.write(command);					
				}
				
				
				out.write("quit\n");
				out.flush();
				out.close();
				
				
				 while ((str = in.readLine()) != null)
				 {
					 System.out.println(str);
				 }
				 in.close();
		
				//this causes the java program to wait until gnuplot is done
				//if this is missing, the partition file may not be found
				int exitvalue = gnuplot.waitFor();
				System.out.println("Exitvalue :" + exitvalue);
				
			}
			
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
