package master;


import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class Logger 
{
	private PrintWriter logFileWriter;
	private boolean ready = true;
	private final String logFilePath = "D:\\Dokumente\\My Dropbox\\Uni\\Semester 4\\Logging\\";
	
	public Logger(String logFileName)
	{
		try
		{
			File logFile = new File(logFilePath+ logFileName);
			if(!logFile.exists())
				logFile.createNewFile();
			
			logFileWriter = new PrintWriter(new FileWriter(logFile, false));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ready = false;
		}
		
	}

	

	public PrintWriter getLogFileWriter() {
		return logFileWriter;
	}



	public void setLogFileWriter(PrintWriter logFileWriter) {
		this.logFileWriter = logFileWriter;
	}



	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}



	public void doneLogging() 
	{
		if(ready)
		{
			logFileWriter.println("Done Simulating!");
			logFileWriter.close();
			this.ready = false;
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}



	public void simulationStarted(int iterations) 
	{
		if(ready)
		{
			logFileWriter.println("Simulation Started");
			logFileWriter.println("Simulation will continue for " + iterations + " rounds");
			logFileWriter.println("No movement happens in round 1");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
		
	}
	
	public void geographicSimulationStarted(int iterations) 
	{
		if(ready)
		{
			logFileWriter.println("Geographic Simulation Started");
			logFileWriter.println("Simulation will continue for " + iterations + " rounds");
			logFileWriter.println("No movement happens in round 1");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
		
	}



	public void analyzeGraph(DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph) 
	{
		if(ready)
		{
			logFileWriter.println("Graph analysis started!");
			//look for isolated vertices
			Iterator<MMVEObject> itG = interestGraph.vertexSet().iterator();
			ArrayList<MMVEObject> isolated = new ArrayList<MMVEObject>();
			int totalWeightOfThisPartition = 0;
			
			while(itG.hasNext())
			{
				MMVEObject o = itG.next();
				totalWeightOfThisPartition = totalWeightOfThisPartition + o.getWeight();
				if(interestGraph.edgesOf(o).isEmpty())
				{
					isolated.add(o);
					if(o instanceof Avatar)
						logFileWriter.println("Node " + o.getID() + " is an Avatar and is isolated!");
					else
						logFileWriter.println("Node " + o.getID() + " is an Object and is isolated!");
				}
				else
				{
					Iterator<DefaultWeightedEdge> itE = interestGraph.edgesOf(o).iterator();
					String neighbors = "";
					while(itE.hasNext())
					{
						DefaultWeightedEdge e = itE.next();
						if(interestGraph.getEdgeSource(e).equals(o))
						{
							if(!neighbors.contains(String.valueOf(interestGraph.getEdgeTarget(e).getID())))
								neighbors = neighbors +  " " + interestGraph.getEdgeTarget(e).getID() + 
								" (weight: " + interestGraph.getEdgeWeight(e) + ")";
						}
											
						
					}
					if(neighbors.isEmpty())
					{
						if(o instanceof Avatar)
							logFileWriter.println("Node " + o.getID() + " is an Avatar and does not have any outgoing edges!");
						else
							logFileWriter.println("Node " + o.getID() + " is an Object and does not have any outgoing edges!");	
					}
					else
					{
						if(o instanceof Avatar)
							logFileWriter.println("Node " + o.getID() + " is an Avatar and is connected to: " + neighbors);
						else
							logFileWriter.println("Node " + o.getID() + " is an Object and is connected to: " + neighbors);					
					}
					
				}
			}
			logFileWriter.println("This graph contains " + interestGraph.vertexSet().size() + " nodes");
			logFileWriter.println("There are " + isolated.size() + " isolated nodes in this graph");
			logFileWriter.println("The total weight of this graph is: " + totalWeightOfThisPartition);
			logFileWriter.println("Done analyzing graph!");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}



	public void analyzePeerList(ArrayList<Peer> peers) 
	{
		if(ready)
		{
			logFileWriter.println("Analyzing graphs of " + peers.size() + " peers");
			Iterator<Peer> itPeers = peers.iterator();
			while(itPeers.hasNext())
			{
				Peer p = itPeers.next();
				analyzeGraph(p.getManagedObjects());
			}
			logFileWriter.println("Peer Analysis done!");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
	}



	public void analyzeCrossedEdges(ArrayList<DefaultWeightedEdge> crossedEdges, DirectedWeightedMultigraph<MMVEObject, DefaultWeightedEdge> interestGraph) 
	{
		if(ready)
		{
			Iterator<DefaultWeightedEdge> itCrossed = crossedEdges.iterator();
			logFileWriter.println("Analyzing crossed edges");
			while(itCrossed.hasNext())
			{
				DefaultWeightedEdge e = itCrossed.next();
				logFileWriter.println("There is a crossed edge between node " + interestGraph.getEdgeSource(e).getID() + " and node " + interestGraph.getEdgeTarget(e).getID()
						+ " with weight " + interestGraph.getEdgeWeight(e));
			}
			logFileWriter.println("Done analyzing crossed edges");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}



	public void newRound(int counter) 
	{
		
		if(ready)
		{
			logFileWriter.println("A new round has started!");
			logFileWriter.println("Current round number is " + counter);
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}



	public void objectMovement(Point oldPosition, MMVEObject objectToWorkWith) 
	{
		if(ready)
		{
			logFileWriter.println("Object " + objectToWorkWith.getID() +  " moved from position " +
					oldPosition.toString() + " to " + objectToWorkWith.getPosition().toString());
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}



	public void logInteraction(MMVEObject interactingObject, MMVEObject objectToInterActWith, boolean edgePresent) 
	{
		if(ready)
		{
			
			if(edgePresent)
			{				
				logFileWriter.println("A regular interaction occured between object " + interactingObject.getID() 
						+ " and object " + objectToInterActWith.getID());
			}
			else
			{
				logFileWriter.println("A nonregular interaction occured between object " + interactingObject.getID() 
						+ " and object " + objectToInterActWith.getID());
			}
			
			
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}
	
	public void logInteraction(MMVEObject interactingObject, MMVEObject objectToInterActWith) 
	{
		if(ready)
		{
			
			logFileWriter.println("An interaction occured between object " + interactingObject.getID() 
						+ " and object " + objectToInterActWith.getID());
						
			
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}
	
	public void printNewSection()
	{
		if(ready)
		{
			logFileWriter.println(".....................................................................................");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
	}
	
	public void printNewRound()
	{
		if(ready)
		{
			logFileWriter.println("====================================================================================");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
	}
	
	public void noExecution()
	{
		if(ready)
		{
			logFileWriter.println("No partitioning will be done in this round!");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
	}
	
	public void newInteractions()
	{
		if(ready)
		{
			logFileWriter.println("New Interactions were calculated!");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}
	
	public void stableInteractions()
	{
		
		if(ready)
		{
			logFileWriter.println("Interactions are stable!");
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}



	public void objectTransfer(MMVEObject movingObject,
			Region oldRegion, Region newRegion) 
	{
		if(ready)
		{
			logFileWriter.println("Object " + movingObject.getID() + " moved from " + oldRegion.getID() + " to " + newRegion.getID());
		}
		else
		{
			System.out.println("Logging not ready!");
		}
		
	}
	
	

}
