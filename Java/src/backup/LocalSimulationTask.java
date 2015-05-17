package backup;



public class LocalSimulationTask 
{
	//old interaction code, commented out due to massive amounts of compiler errors because it is in a new package.
	/*
	private void simulateInteraction() 
	{
		//iterate through the interest Graph and determine for each object with what object it interacts
		Iterator<MMVEObject> itInterestGraph = this.interestGraph.vertexSet().iterator();
		Random randomGenerator = new Random();
		
		while(itInterestGraph.hasNext())
		{
			MMVEObject interactingObject = itInterestGraph.next();
			//roll to find the amount of objects this objects interacts with
			int numberOfInteractions = randomGenerator.nextInt(this.interestGraph.vertexSet().size()+1);
			if(numberOfInteractions > 0)
			{
				executeInteractions(interactingObject, numberOfInteractions);
			}
			
		}
		
	}

	private void executeInteractions(MMVEObject interactingObject,
			int numberOfInteractions) 
	{
		Random randomGenerator = new Random();
		int interactionsWithObjectsWithoutConnectingEdge = numberOfInteractions * nonRegularInteractionFactor/100;
		int regularinteractions = numberOfInteractions - interactionsWithObjectsWithoutConnectingEdge;
		
		int regularInteractionCount = 0;
		while(regularInteractionCount < regularinteractions)
		{
			
			int objectToGet = randomGenerator.nextInt(interestGraph.edgesOf(interactingObject).size());
			
			DefaultWeightedEdge edgeToWorkWith = (DefaultWeightedEdge)interestGraph.edgesOf(interactingObject).toArray()[objectToGet];
			
			MMVEObject objectToInterActWith = null;
			
			if(interestGraph.getEdgeSource(edgeToWorkWith).equals(interactingObject))
			{
				objectToInterActWith = interestGraph.getEdgeTarget(edgeToWorkWith);
			}
			else
			{
				objectToInterActWith = interestGraph.getEdgeSource(edgeToWorkWith);
			}
			 
			if(objectToInterActWith != null)
			{
				logger.logInteraction(interactingObject, objectToInterActWith ,true);
				regularInteractionCount++;
			}
		}
		logger.printNewSection();
		int interactionCount = 0;
		while(interactionCount < interactionsWithObjectsWithoutConnectingEdge)
		{
			//grab a random object of the graph to interact with it
			int objectToGet = randomGenerator.nextInt(this.interestGraph.vertexSet().size());
			if(objectToGet != interactingObject.getID())
			{
				MMVEObject objectToInterActWith = Util.getObjectWithID(interestGraph, objectToGet);
				if(objectToInterActWith!= null)
				{
					if(!interestGraph.containsEdge(interactingObject, objectToInterActWith))
					{
						logger.logInteraction(interactingObject, objectToInterActWith,false);
						interactionCount++;
					}
					
				}
			}
		}
		logger.printNewSection();
	}
	*/
}
