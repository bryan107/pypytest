package experiment.thread;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import experiment.core.ClosenessOfDistanceCoreWithNewMFDR;
import experiment.core.NoiseMFDRCore;
import experiment.utility.DataParser;
import experiment.utility.UCRData;
import mfdr.core.MFDRWaveParameterFacade;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.*;
import mfdr.file.FileAccessAgent;
import mfdr.utility.File;
import junit.framework.TestCase;

public class ExperimentContainer6 extends TestCase {

//	private final int NoC = 4;

	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\Noise_TIME_ALL_1.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\dataset_list1.txt";
	
	public void test(){
		NoiseMFDRCore core = new NoiseMFDRCore();
		core.runDistance(readaddress, writeaddress, listaddress, 2, 2, 10, 1, 1);
	}
}
