package master;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CSVAggregator 
{
	public static void createAggregatedCSVFile(String aggregatedFileName, String originalFileName, String directoryName, String variedValue, 
			double startingValue, double endValue, double iterationSteps )
	{
		//append a \\ if necessary
		if(!directoryName.endsWith("\\"))
		{
			directoryName = directoryName + "\\";
		}
		File originalFile = new File(LocalSimulationTask.getCsvfilepath() + directoryName + originalFileName);
		if(originalFile.exists())
		{
			int numberOfIterationsRequired = (int)((endValue-startingValue) / iterationSteps);
			int counter = 0;
			String csvFileStem = originalFileName.substring(originalFileName.indexOf("csv"), originalFileName.length());
			//check whether there is already a aggregated file, if yes, delete it, because we append later in this function
			File aggregatedFile = new File(LocalSimulationTask.getCsvfilepath() + directoryName + aggregatedFileName);
			if(aggregatedFile.exists())
			{
				aggregatedFile.delete();
			}
			while(counter <= numberOfIterationsRequired)
			{
				double currentValue = startingValue + counter * iterationSteps;
				String currentFileName = variedValue + (int)currentValue + csvFileStem;
				writeCondensedValuesToFile(aggregatedFileName, currentFileName, directoryName, variedValue, currentValue);
				counter++;
			}
		}
		else
		{
			System.out.println("Can't find specified file!");
		}
		
		
	}
	
	public static void createAggregatedCSVFile(String aggregatedFileName, String originalFileName, String variedValue, 
			double startingValue, double endValue, double iterationSteps )
	{
		
		File originalFile = new File(LocalSimulationTask.getCsvfilepath() + originalFileName);
		if(originalFile.exists())
		{
			int numberOfIterationsRequired = (int)((endValue-startingValue) / iterationSteps);
			int counter = 0;
			String csvFileStem = originalFileName.substring(originalFileName.indexOf("csv"), originalFileName.length());
			//check whether there is already a aggregated file, if yes, delete it, because we append later in this function
			File aggregatedFile = new File(LocalSimulationTask.getCsvfilepath() +  aggregatedFileName);
			if(aggregatedFile.exists())
			{
				aggregatedFile.delete();
			}
			while(counter <= numberOfIterationsRequired)
			{
				double currentValue = startingValue + counter * iterationSteps;
				String currentFileName = variedValue + (int)currentValue + csvFileStem;
				writeCondensedValuesToFile(aggregatedFileName, currentFileName, variedValue, currentValue);
				counter++;
			}
		}
		else
		{
			System.out.println("Can't find specified file!");
		}
		
		
	}

	private static void writeCondensedValuesToFile(String aggregatedFileName,
			String currentFileName, String directoryName, String variedValue, double currentValue) 
	{
		try
		{
			CsvReader csvReader = new CsvReader(LocalSimulationTask.getCsvfilepath() + directoryName + currentFileName);
			int communicationCost = 0;
			float distributionFactor = 0;
			int executionCost = 0;
			int interactions = 0;
			int overloaded = 0;
			int reassignments = 0;
			int rounds = 0;
			boolean headerLine = true;
			while(csvReader.readRecord())
			{
				String[]currentValues = csvReader.getValues();
				//old files without distribution factor
				if(currentValues.length == 6)
				{
					//skip the first line, because it describes the columns and processing it destroys the code
					if(headerLine)
					{
						headerLine = false;
					}
					else
					{
						rounds++;
						interactions = interactions + Integer.parseInt(currentValues[1]);
						executionCost = executionCost + Integer.parseInt(currentValues[2]);
						communicationCost = communicationCost + Integer.parseInt(currentValues[3]);
						reassignments = reassignments + Integer.parseInt(currentValues[4]);
						overloaded = overloaded + Integer.parseInt(currentValues[5]);
					}
					
				}
				
				if(currentValues.length == 7)
				{
					//skip the first line, because it describes the columns and processing it destroys the code
					if(headerLine)
					{
						headerLine = false;
					}
					else
					{
						rounds++;
						interactions = interactions + Integer.parseInt(currentValues[1]);
						executionCost = executionCost + Integer.parseInt(currentValues[2]);
						communicationCost = communicationCost + Integer.parseInt(currentValues[3]);
						reassignments = reassignments + Integer.parseInt(currentValues[4]);
						overloaded = overloaded + Integer.parseInt(currentValues[5]);
						distributionFactor = distributionFactor + Float.parseFloat(currentValues[6]);
					}
					
				}
				
			}
			communicationCost = communicationCost / rounds;
			interactions = interactions / rounds;
			executionCost = executionCost / rounds;
			reassignments = reassignments / rounds;
			overloaded = overloaded / rounds;
			distributionFactor = distributionFactor / rounds;
			
			//round distribution factor up to two decimal values
			DecimalFormat df = new DecimalFormat("0.00");
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(dfs);
			String temp = df.format(distributionFactor);
			distributionFactor = Float.parseFloat(temp);
			if(distributionFactor > 1)
				distributionFactor = 1;
			
			File aggregatedFile = new File(LocalSimulationTask.getCsvfilepath() + directoryName + aggregatedFileName);
			boolean writeHeader = false;
			
			if(!aggregatedFile.exists())
			{
				aggregatedFile.createNewFile();
				writeHeader = true;
			}
			
			CsvWriter csvWriter = new CsvWriter(new FileOutputStream(aggregatedFile, true),',', Charset.forName("ISO-8859-1"));
			if(writeHeader)
			{
				String[] header = new String[7];
				header[0] = variedValue;
				header[1] = "interactions between peers";
				header[2] = "execution cost (ms)";
				header[3] = "communication cost";
				header[4] = "reassignments";
				header[5] = "overloaded peers";
				header[6] = "distribution factor";
				csvWriter.writeRecord(header);
			}
			
			String[] row = new String[7];
			row[0] = String.valueOf(currentValue);
			row[1] = String.valueOf(interactions);
			row[2] = String.valueOf(executionCost);
			row[3] = String.valueOf(communicationCost);
			row[4] = String.valueOf(reassignments);
			row[5] = String.valueOf(overloaded);
			row[6] = String.valueOf(distributionFactor);
			csvWriter.writeRecord(row);
			csvWriter.flush();
			csvWriter.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	private static void writeCondensedValuesToFile(String aggregatedFileName,
			String currentFileName, String variedValue, double currentValue) 
	{
		try
		{
			CsvReader csvReader = new CsvReader(LocalSimulationTask.getCsvfilepath() + currentFileName);
			int communicationCost = 0;
			double distributionFactor = 0;
			int executionCost = 0;
			int interactions = 0;
			int overloaded = 0;
			int reassignments = 0;
			int rounds = 0;
			boolean headerLine = true;
			while(csvReader.readRecord())
			{
				rounds++;
				String[]currentValues = csvReader.getValues();
				//old files without distribution factor
				if(currentValues.length == 6)
				{
					//skip the first line, because it describes the columns and processing it destroys the code
					if(headerLine)
					{
						headerLine = false;
					}
					else
					{
						interactions = interactions + Integer.parseInt(currentValues[1]);
						executionCost = executionCost + Integer.parseInt(currentValues[2]);
						communicationCost = communicationCost + Integer.parseInt(currentValues[3]);
						reassignments = reassignments + Integer.parseInt(currentValues[4]);
						overloaded = overloaded + Integer.parseInt(currentValues[5]);
					}
					
				}
				
				if(currentValues.length == 7)
				{
					//skip the first line, because it describes the columns and processing it destroys the code
					if(headerLine)
					{
						headerLine = false;
					}
					else
					{
						interactions = interactions + Integer.parseInt(currentValues[1]);
						executionCost = executionCost + Integer.parseInt(currentValues[2]);
						communicationCost = communicationCost + Integer.parseInt(currentValues[3]);
						reassignments = reassignments + Integer.parseInt(currentValues[4]);
						overloaded = overloaded + Integer.parseInt(currentValues[5]);
						distributionFactor = distributionFactor + Double.parseDouble(currentValues[6]);
					}
					
				}
				
			}
			communicationCost = communicationCost / rounds;
			interactions = interactions / rounds;
			executionCost = executionCost / rounds;
			reassignments = reassignments / rounds;
			overloaded = overloaded / rounds;
			distributionFactor = distributionFactor / rounds;
			
			File aggregatedFile = new File(LocalSimulationTask.getCsvfilepath() + aggregatedFileName);
			boolean writeHeader = false;
			
			if(!aggregatedFile.exists())
			{
				aggregatedFile.createNewFile();
				writeHeader = true;
			}
			
			CsvWriter csvWriter = new CsvWriter(new FileOutputStream(aggregatedFile, true),',', Charset.forName("ISO-8859-1"));
			if(writeHeader)
			{
				String[] header = new String[7];
				header[0] = variedValue;
				header[1] = "interactions between peers";
				header[2] = "execution cost (ms)";
				header[3] = "communication cost";
				header[4] = "reassignments";
				header[5] = "overloaded peers";
				header[6] = "distribution factor";
				csvWriter.writeRecord(header);
			}
			
			String[] row = new String[7];
			row[0] = String.valueOf(currentValue);
			row[1] = String.valueOf(interactions);
			row[2] = String.valueOf(executionCost);
			row[3] = String.valueOf(communicationCost);
			row[4] = String.valueOf(reassignments);
			row[5] = String.valueOf(overloaded);
			row[6] = String.valueOf(distributionFactor);
			csvWriter.writeRecord(row);
			csvWriter.flush();
			csvWriter.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static void writeCondensedValuesToFile(String aggregatedFileName,
			String currentFileName, String directoryName, String variedValue, double currentValue, int numberOfRoundsPerTest, int startingRound) 
	{
		try
		{
			CsvReader csvReader = new CsvReader(LocalSimulationTask.getCsvfilepath() + directoryName + currentFileName);
			int communicationCost = 0;
			float distributionFactor = 0;
			int executionCost = 0;
			int interactions = 0;
			int overloaded = 0;
			int reassignments = 0;
			int rounds = 0;
			boolean headerLine = true;
			while(csvReader.readRecord() && rounds < startingRound + numberOfRoundsPerTest)
			{
				String[]currentValues = csvReader.getValues();
				//old files without distribution factor
				if(currentValues.length == 6)
				{
					if(rounds >= startingRound && rounds <= startingRound+numberOfRoundsPerTest)
					{
						//skip the first line, because it describes the columns and processing it destroys the code
						if(headerLine)
						{
							headerLine = false;
						}
						else
						{
							rounds++;
							interactions = interactions + Integer.parseInt(currentValues[1]);
							executionCost = executionCost + Integer.parseInt(currentValues[2]);
							communicationCost = communicationCost + Integer.parseInt(currentValues[3]);
							reassignments = reassignments + Integer.parseInt(currentValues[4]);
							overloaded = overloaded + Integer.parseInt(currentValues[5]);
						}
					}
					else
					{
						rounds++;
					}
					
					
				}
				
				if(currentValues.length == 7)
				{
					if(rounds >= startingRound && rounds <= startingRound+numberOfRoundsPerTest)
					{
						//skip the first line, because it describes the columns and processing it destroys the code
						if(headerLine)
						{
							headerLine = false;
						}
						else
						{
							rounds++;
							interactions = interactions + Integer.parseInt(currentValues[1]);
							executionCost = executionCost + Integer.parseInt(currentValues[2]);
							communicationCost = communicationCost + Integer.parseInt(currentValues[3]);
							reassignments = reassignments + Integer.parseInt(currentValues[4]);
							overloaded = overloaded + Integer.parseInt(currentValues[5]);
							distributionFactor = distributionFactor + Float.parseFloat(currentValues[6]);
						}
						
					}
					else
					{
						rounds++;
					}
					
					
				}
				
			}
			communicationCost = communicationCost / numberOfRoundsPerTest;
			interactions = interactions / numberOfRoundsPerTest;
			executionCost = executionCost / numberOfRoundsPerTest;
			reassignments = reassignments / numberOfRoundsPerTest;
			overloaded = overloaded / numberOfRoundsPerTest;
			distributionFactor = distributionFactor / numberOfRoundsPerTest;
			
			//round distribution factor up to two decimal values
			DecimalFormat df = new DecimalFormat("0.00");
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(dfs);
			String temp = df.format(distributionFactor);
			distributionFactor = Float.parseFloat(temp);
			if(distributionFactor > 1)
				distributionFactor = 1;
			
			File aggregatedFile = new File(LocalSimulationTask.getCsvfilepath() + directoryName + aggregatedFileName);
			boolean writeHeader = false;
			
			if(!aggregatedFile.exists())
			{
				aggregatedFile.createNewFile();
				writeHeader = true;
			}
			
			CsvWriter csvWriter = new CsvWriter(new FileOutputStream(aggregatedFile, true),',', Charset.forName("ISO-8859-1"));
			if(writeHeader)
			{
				String[] header = new String[7];
				header[0] = variedValue;
				header[1] = "interactions between peers";
				header[2] = "execution cost (ms)";
				header[3] = "communication cost";
				header[4] = "reassignments";
				header[5] = "overloaded peers";
				header[6] = "distribution factor";
				csvWriter.writeRecord(header);
			}
			
			String[] row = new String[7];
			row[0] = String.valueOf(currentValue);
			row[1] = String.valueOf(interactions);
			row[2] = String.valueOf(executionCost);
			row[3] = String.valueOf(communicationCost);
			row[4] = String.valueOf(reassignments);
			row[5] = String.valueOf(overloaded);
			row[6] = String.valueOf(distributionFactor);
			csvWriter.writeRecord(row);
			csvWriter.flush();
			csvWriter.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void createAggregatedFileForScalabilityTest(String aggregatedFileName, String originalFileName, String directoryName, 
			double startingValue, double endValue, double iterationSteps, int numberOfRoundsPerTest )
	{
		
		//append a \\ if necessary
		if(!directoryName.endsWith("\\"))
		{
			directoryName = directoryName + "\\";
		}
		File originalFile = new File(LocalSimulationTask.getCsvfilepath() + directoryName + originalFileName);
		if(originalFile.exists())
		{
			int numberOfIterationsRequired = (int)((endValue-startingValue) / iterationSteps);
			int counter = 0;
			int currentRound = 0;
			
			//check whether there is already a aggregated file, if yes, delete it, because we append later in this function
			File aggregatedFile = new File(LocalSimulationTask.getCsvfilepath() + directoryName + aggregatedFileName);
			if(aggregatedFile.exists())
			{
				aggregatedFile.delete();
			}
			while(counter <= numberOfIterationsRequired)
			{
				double currentValue = startingValue + counter * iterationSteps;
				writeCondensedValuesToFile(aggregatedFileName, originalFileName, directoryName, "object number", currentValue, 
						numberOfRoundsPerTest, currentRound);
				counter++;
				currentRound = currentRound + numberOfRoundsPerTest + 1;
			}
		}
		else
		{
			System.out.println("Can't find specified file!");
		}
		
	}

}
