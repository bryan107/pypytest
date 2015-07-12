package experiment.core;

import junit.framework.TestCase;

public class ExperimentContainer1 extends TestCase {

	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\ClosinessOfDistance_1000_FULL_1_NEW_FIX.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\dataset_list1.txt";
	
	public void test(){
		ClosenessOfDistanceCoreWithNewMFDR core = new ClosenessOfDistanceCoreWithNewMFDR();
		core.runRandom1000(readaddress, writeaddress, listaddress, 2, 2, 10);
	}
}
