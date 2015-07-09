package experiment.core;

import junit.framework.TestCase;

public class ExperimentContainer3 extends TestCase {

	// private final int NoC = 4;
	private final String readaddress = "C:\\TEST\\MDFR\\Data\\dataset\\";
	private final String writeaddress = "C:\\TEST\\MDFR\\Data\\Experiment\\ClosinessOfDistance_3.csv";
	private final String listaddress = "C:\\TEST\\MDFR\\Data\\dataset\\dataset_list3.txt";

	public void test(){
//		RepresentationErrorExpCore core = new RepresentationErrorExpCore();
//		core.runDFT(readaddress,writeaddress ,listaddress, 2,2,10);
		
		ClosenessOfDistanceCore dist_core = new ClosenessOfDistanceCore();
		dist_core.run(readaddress, writeaddress, listaddress, 2,2,10);
	}
}
