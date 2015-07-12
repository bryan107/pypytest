package experiment.core;

import junit.framework.TestCase;

public class ExperimentContainer3 extends TestCase {

	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\ClosinessOfDistance_1000_FULL_3_NEW_FIX.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\dataset_list3.txt";
	
	public void test(){
		ClosenessOfDistanceCoreWithNewMFDR core = new ClosenessOfDistanceCoreWithNewMFDR();
		core.runRandom1000(readaddress, writeaddress, listaddress, 2, 2, 10);
	}
}
