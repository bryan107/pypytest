package experiment.thread;

import experiment.core.RepresentationErrorExpCorewithNewMFDR;
import junit.framework.TestCase;

public class ExperimentContainer1 extends TestCase {

	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\Representation_FULL_1_Ultra.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\dataset_list1.txt";
	
	public void test(){
		RepresentationErrorExpCorewithNewMFDR core = new RepresentationErrorExpCorewithNewMFDR();
		core.runOptimal(readaddress, writeaddress, listaddress, 2, 2, 10);
		
//		ClosenessOfDistanceCoreWithNewMFDR core = new ClosenessOfDistanceCoreWithNewMFDR();
//		core.runRandom1000(readaddress, writeaddress, listaddress, 2, 2, 10);
	}
}
