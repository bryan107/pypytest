package experiment.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mfdr.datastructure.TimeSeries;
import mfdr.file.FileAccessAgent;
import mfdr.math.emd.EMD;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.utility.File;
import junit.framework.TestCase;

public class TestData extends TestCase {

	private double zerocrossingaccuracy = 0.0001;
	private double[] IFparamaters = {4,2,1}; 
	
	
	public void test(){
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println("Start Time " + sdf.format(cal.getTime()) );
    	
		FileAccessAgent fagent = new FileAccessAgent("C:\\TEST\\MDFR\\Data\\NULL.txt", "C:\\TEST\\MDFR\\Data\\power_data.txt");
		DataParser parser = new DataParser(new OneLineOneData(), fagent);
		TimeSeries ts = parser.getTimeSeries();
		EMD emd = new EMD(ts, zerocrossingaccuracy, IFparamaters[0], IFparamaters[1], IFparamaters[2]);
		System.out.println("EMD Init " + sdf.format(cal.getTime()) );
		IMFS imfs = emd.getIMFs(100);
		System.out.println("IMF Extraction " + sdf.format(cal.getTime()) );
		
		/*
		 *  Store Results
		 */
		// Save Time References
		File.getInstance().saveTimeToFile(ts, "C:\\TEST\\MDFR\\_EMD_Example.csv");
		
		// Save Original Signal
		File.getInstance().saveLinkedListToFile("Original" ,ts, "C:\\TEST\\MDFR\\_EMD_Example.csv");
				
		// Save IMFs
		for(int i = 0 ; i < imfs.size() ; i++){
			File.getInstance().saveLinkedListToFile("IMF" + i ,imfs.get(i), "C:\\TEST\\MDFR\\_EMD_Example.csv");
		}
		System.out.println("Store Complete " + sdf.format(cal.getTime()) );
		
	}
	
}
