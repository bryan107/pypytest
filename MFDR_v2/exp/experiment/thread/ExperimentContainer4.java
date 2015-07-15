package experiment.thread;

import java.util.LinkedList;

import experiment.core.NoiseMFDRCore;
import experiment.core.RepresentationErrorExpCorewithNewMFDR;
import experiment.utility.DataParser;
import experiment.utility.UCRData;
import mfdr.core.MFDRWaveParameterFacade;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.*;
import mfdr.file.FileAccessAgent;
import junit.framework.TestCase;

public class ExperimentContainer4 extends TestCase {

	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\Noise_TIME_ALL_3.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\dataset_list3.txt";
	
	public void test(){
		NoiseMFDRCore core = new NoiseMFDRCore();
		core.runDistance(readaddress, writeaddress, listaddress, 2, 2, 10, 1, 1);
	}
}
