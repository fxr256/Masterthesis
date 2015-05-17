package master;

import java.awt.Point;





public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
//				
//		CSVAggregator.createAggregatedCSVFile("avatarWeightAggregated.csv", "avatarWeight5csvMediumManyGeographic.csv", "avatarWeightGeographicDesktop", "avatarWeight", 5, 40, 5);
//		GNUPlot.createGraphForAggregatedValueFile("avatarWeightGeographic", "avatarWeightAggregated.csv", "avatarWeightGeographicDesktop", "avatar weight");
//		LocalSimulation.doLocalSimulationForXPeriods(20, "avatarsMedium.txt", "objectsMany.txt", "interestLocalityMediumMany.txt", "multiValueLogFile.txt", "interactionsMediumMany.txt", "csvMediumMany.csv", 20, 2000, 2, 10, 50, 2, 40, 1, 20, 20, 1, 1, true, true);
//		LocalSimulation.doSimulationWithValueRange("avatarWeight", 5, 40, 5, "manyMediumConfig.txt", false, "avatarWeightInterestDesktop", "csvAvatarWeightInterest.csv");
//		LocalSimulation.doLocalGeographicSimulationForXPeriods(20, "avatarsMedium.txt", "objectsMany.txt", "multiValueGeographicLogFile", "interactionsMediumMany20.txt", "csvMediumManyGeographic.csv", 1000, 2000, 2, 10, 50, 2, 40, 1, 20, true);
//		LocalSimulation.doGeographicSimulationWithValueRange("avatarWeight", 5, 40, 5, "manyMediumConfigGeographic.txt", false, "avatarWeightGeographicDesktop", "csvAvatarWeightGeographic.csv");
//		LocalSimulation.readAndExecuteConfigurationFile("manyMediumConfigMoreInteractionsMoreOW.txt");
//		CrowdingMovementFileGenerator.generateCrowdingMovementFile("crowdingAvatarsMediumV.txt", "avatarsMedium.txt", new Point(100,100), 5, 20, 500);
//		LocalSimulation.doLocalSimulationForXPeriods(40, "crowdingAvatarsMediumV.txt", "crowdingObjectsManyV.txt", "interestLocalityMediumMany.txt", "crowdingInteractionLogfile.txt", "interactionsMediumMany.txt", "csvCrowdingInterestBased.csv", 20, 2000, 2, 10, 50, 2, 1, 1, 20, 20, 1, 1, true, true);
//		LocalSimulation.doLocalGeographicSimulationForXPeriods(40, "crowdingAvatarsMediumV.txt", "crowdingObjectsManyV.txt", "crowdingGeographicLogFile.txt", "interactionsMediumMany.txt", "csvCrowdingGeographic.csv", 20, 2000, 2, 10, 50, 2, 1, 1, 20, true);
//		CSVAggregator.createAggregatedCSVFile("aggregatedAOIinterest.csv", "aoiSize20csvMediumMany.csv", "aoiSizeInterest", "aoisize", 20, 100, 10);
//		GNUPlot.createGraphForAggregatedValueFile("aoiSizeInterest", "aggregatedAOIInterest.csv", "aoiSizeInterest", "AoI size");
//		GNUPlot.createGraphForCertainCriteriaWithCertainValueRange("peerNumberGeographicInteractions.png", "aggregatedPeersgeographic.csv", "peerNumbergeographic", "number of peers", "interactions", 0, 20, 2);
//		GNUPlot.createGraphForCertainCriteriaWithCertainValueRange("aoiSizeInterestDistribution.png", "aggregatedAOIinterest.csv", "aoiSizeinterest", "aura size", "distribution factor", 0, 0.4, 0.05);
//		GNUPlot.createGraphForCertainCriteriaWithCertainValueRange("csvCrowdingInterestBasedDistribution.png", "csvCrowdingInterestBased.csv", "", "rounds", "distribution factor", 0, 1, 0.1);
		
//		String[] objectFiles = {"objects5000.txt", "objects10000.txt", "objects15000.txt", "objects20000.txt", "objects25000.txt"};
//		LocalSimulation.conductInterestBasedScalabilityTest(objectFiles, "manyMediumConfig.txt", false, "scalabilityInterestDesktopLong", "csvScalabilityInterest.csv");
//		LocalSimulation.conductGeographicScalabilityTest(objectFiles, "manyMediumConfigGeographic.txt", false, "scalabilityGeographicDesktopLong", "csvScalabilityGeographic.csv");
		
//		String[] interactionFiles = {"interactionTestRandom200.txt", "interactionTestRandom400.txt", "interactionTestRandom600.txt"};
//		int[] interactionNumbers = {200, 400, 600};
////		LocalSimulation.testInteractionBehaviourInterest(interactionFiles, interactionNumbers, "manyMediumConfigRandomOnly.txt", false, "interactionsInterestRandomOnly", "csvInteractionsInterest.csv");
//		LocalSimulation.testInteractionBehaviourGeographic(interactionFiles, interactionNumbers, "manyMediumConfigGeographicRandomOnly.txt", false, "interactionsGeographicRandomOnly", "csvInteractionsGeographic.csv");
//		CSVAggregator.createAggregatedFileForScalabilityTest("aggregatedInterestScalability.csv", "csvScalabilityInterest.csv","scalabilityInterestDesktopLong", 5000, 25000, 5000, 9);
//		GNUPlot.createGraphForAggregatedValueFile("scalabilityInterest", "aggregatedInterestScalability.csv", "scalabilityInterestDesktopLong", "number of objects", 5000, 25000, 5000);
		LocalSimulation.doLocalSimulationForXPeriods("manyMediumConfig.txt");
		
	}

}
