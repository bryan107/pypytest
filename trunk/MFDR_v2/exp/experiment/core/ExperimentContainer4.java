package experiment.core;

import java.util.LinkedList;

import experiment.utility.DataParser;
import experiment.utility.UCRData;
import mfdr.core.MFDRWaveParameterFacade;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.*;
import mfdr.file.FileAccessAgent;
import junit.framework.TestCase;

public class ExperimentContainer4 extends TestCase {

	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\ClosinessOfDistance_1000_FULL_4_NEW_FIX.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\dataset_list4.txt";
	
	public void test(){
		ClosenessOfDistanceCoreWithNewMFDR core = new ClosenessOfDistanceCoreWithNewMFDR();
		core.runRandom1000(readaddress, writeaddress, listaddress, 2, 2, 10);
	}
}
