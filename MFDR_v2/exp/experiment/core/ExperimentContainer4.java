package experiment.core;

import java.util.LinkedList;

import experiment.utility.DataParser;
import experiment.utility.UCRData;
import mfdr.core.MFDRParameterFacade;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.*;
import mfdr.file.FileAccessAgent;
import junit.framework.TestCase;

public class ExperimentContainer4 extends TestCase {

//	private final int NoC = 4;
	private final String readaddress = "C:\\TEST\\MDFR\\Data\\dataset\\";
	private final String writeaddress = "C:\\TEST\\MDFR\\Data\\Experiment\\ClosinessOfDistance_4.csv";
	private final String listaddress = "C:\\TEST\\MDFR\\Data\\dataset\\dataset_list4.txt";
	
	public void test(){
//		RepresentationErrorExpCore core = new RepresentationErrorExpCore();
//		core.runDFT(readaddress,writeaddress ,listaddress, 2,2,10);
		
		ClosenessOfDistanceCore dist_core = new ClosenessOfDistanceCore();
		dist_core.run(readaddress, writeaddress, listaddress, 2,2,10);
	}
}
