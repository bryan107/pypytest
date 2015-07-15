package experiment.thread;

import experiment.core.NoiseMFDRCore;
import experiment.core.RepresentationErrorExpCorewithNewMFDR;
import junit.framework.TestCase;

public class ExperimentContainer3 extends TestCase {

	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\Noise_TIME_ALL_4.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\dataset_list4.txt";
	
	public void test(){
		NoiseMFDRCore core = new NoiseMFDRCore();
		core.runDistance(readaddress, writeaddress, listaddress, 2, 2, 10, 1, 1);
	}
}
