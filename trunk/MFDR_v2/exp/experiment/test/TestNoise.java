package experiment.test;

import java.util.LinkedList;

import mfdr.core.MFDRParameterFacade;
import mfdr.core.MFDRParameters;
import mfdr.core.MFDRWaveParameterFacade;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDRLCM;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import mfdr.utility.Print;
import junit.framework.TestCase;

public class TestNoise extends TestCase {

	
	public void testnoise(){
		LinkedList<TimeSeries> tslist = File.getInstance().readreadTimeSeriesFromFile("C:\\TEST\\MFDR\\EXAMPLE.csv");
		MFDRWaveParameterFacade facade = new MFDRWaveParameterFacade(3, 6.5, 0.5);
		for(int i = 0 ; i < tslist.size() ; i++){
			TimeSeries full = tslist.get(i);
			MFDRParameters p = facade.learnMFDRParameters(full, 4, false);
			MFDRLCM mfdr = new MFDRLCM(p.trendNoC(), p.seasonalNoC());
			TimeSeries reduced = mfdr.getFullResolutionDR(full);
			File.getInstance().saveLinkedListToFile("Full", full, "C:\\TEST\\MFDR\\Experiment\\EXAMPLE.csv");
			File.getInstance().saveLinkedListToFile("DR", reduced, "C:\\TEST\\MFDR\\Experiment\\EXAMPLE.csv");
		}
	}
}
