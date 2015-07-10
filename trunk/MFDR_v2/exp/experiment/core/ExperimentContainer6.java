package experiment.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import experiment.utility.DataParser;
import experiment.utility.UCRData;
import mfdr.core.MFDRParameterFacade;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.*;
import mfdr.file.FileAccessAgent;
import mfdr.utility.File;
import junit.framework.TestCase;

public class ExperimentContainer6 extends TestCase {

//	private final int NoC = 4;
	private final String readaddress = "C:\\TEST\\MDFR\\Data\\dataset\\";
	private final String writeaddress = "C:\\TEST\\MDFR\\Data\\Experiment\\KNN_Clustering_1.csv";
	private final String listaddress = "C:\\TEST\\MDFR\\Data\\dataset\\Reduced_File_1.txt";
	
	public void test(){
		KNNExpCore KNN_core = new KNNExpCore();
//		KNN_core.run(readaddress, writeaddress, listaddress, 2,2,10,1);
		KNN_core.run(readaddress, writeaddress, listaddress, 2,2,10,3);
		KNN_core.run(readaddress, writeaddress, listaddress, 2,2,10,5);
		KNN_core.run(readaddress, writeaddress, listaddress, 2,2,10,7);
	}
}
