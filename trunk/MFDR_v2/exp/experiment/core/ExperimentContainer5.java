package experiment.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import experiment.utility.DataParser;
import experiment.utility.UCRData;
import mfdr.core.MFDRWaveParameterFacade;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.*;
import mfdr.file.FileAccessAgent;
import mfdr.utility.File;
import junit.framework.TestCase;

public class ExperimentContainer5 extends TestCase {


	private final String readaddress = "C:\\TEST\\MFDR\\dataset\\";
	private String writeaddress = "C:\\TEST\\MFDR\\Experiment\\ClosinessOfDistance_1000_2_FIX.csv";
	private final String listaddress = "C:\\TEST\\MFDR\\dataset\\Reduced_File_2.txt";
	
	public void test(){
		ClosenessOfDistanceCoreWithNewMFDR core = new ClosenessOfDistanceCoreWithNewMFDR();
		core.runRandom1000(readaddress, writeaddress, listaddress, 2, 2, 10);
		
//		KNNExpCoreWithNewMFDR KNN_core = new KNNExpCoreWithNewMFDR();
//		KNN_core.runMFDR(readaddress, writeaddress, listaddress, 2,2,10,1);
//		KNN_core.runMFDR(readaddress, writeaddress, listaddress, 2,2,10,3);
//		KNN_core.runMFDR(readaddress, writeaddress, listaddress, 2,2,10,5);
//		KNN_core.runMFDR(readaddress, writeaddress, listaddress, 2,2,10,7);
	}
}
