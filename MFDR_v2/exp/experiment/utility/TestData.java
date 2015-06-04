package experiment.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DimensionalityReduction;
import mfdr.dimensionality.reduction.PAA;
import mfdr.dimensionality.reduction.PLA;
import mfdr.file.FileAccessAgent;
import mfdr.math.emd.EMD;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.math.emd.utility.DataListCalculator;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import mfdr.utility.Print;
import junit.framework.TestCase;

public class TestData extends TestCase {

	private double zerocrossingaccuracy = 0.0001;
	private double[] IFparamaters = {4,2,1}; 
//	
	public void testTrend(){
		TimeSeries ts = new TimeSeries();
//		TimeSeries t = new TimeSeries();
//		TimeSeries s = new TimeSeries();
//		TimeSeries n = new TimeSeries();
		for(int i = 0 ; i < 128 ; i++){
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(0.5);
			double seasonal = 10*Math.sin(0.05*i*Math.PI);
			double trend = i*0.1;
//			double trend = 1*Math.pow(i, 0.5);
			double data = trend + seasonal + noise;
			ts.add(new Data(i, data));
//			t.add(new Data(i,trend));
//			s.add(new Data(i, seasonal));
//			n.add(new Data(i, noise));
		}
		TimeSeries tt = (TimeSeries) File.getInstance().readTimeSeriesFromFile("C:\\TEST\\MDFR\\Data\\trend_2.csv");
//		TimeSeries hilb = (TimeSeries) File.getInstance().readTimeSeriesFromFile("C:\\TEST\\MDFR\\Data\\HLIB.csv");
//		double[] hilbarray = DataListOperator.getInstance().linkedDataListToArray(hilb)[1];
		
		

//		File.getInstance().saveLinkedListToFile("Original" ,ts , "C:\\TEST\\MDFR\\Data\\Example.csv");
		
		DFT dft = new DFT(1,2);
		PLA pla = new PLA(64);
		PAA paa = new PAA(64);
		
		double[] pla_residuals = new double[128];
		double[] paa_residuals = new double[128];
		double[] dft_residuals = new double[128];
		double ts_engergy_density = ts.energyDensity();
		for(int i = 0 ; i < 128 ; i++ ){
			System.out.println("I:" + i);
			pla.setWindowSize(128/(i+1));
			paa.setWindowSize(128/(i+1));
			dft.setNOC(i+1);
			TimeSeries pla_r = DataListCalculator.getInstance().getDifference(ts, pla.getFullResolutionDR(ts));
			TimeSeries paa_r = DataListCalculator.getInstance().getDifference(ts, paa.getFullResolutionDR(ts));
			TimeSeries dft_r = DataListCalculator.getInstance().getDifference(ts, dft.getFullResolutionDR(ts));
			pla_residuals[i] = pla_r.energyDensity()/ts_engergy_density;
			paa_residuals[i] = paa_r.energyDensity()/ts_engergy_density;
			dft_residuals[i] = dft_r.energyDensity()/ts_engergy_density;
		}
		
		File.getInstance().saveLinkedListToFile("Origin" ,ts, "C:\\TEST\\MDFR\\Data\\Error_Coefficient_Ratio.csv");
		
		File.getInstance().saveArrayToFile("PLA", pla_residuals, "C:\\TEST\\MDFR\\Data\\Error_Coefficient_Ratio.csv");
		File.getInstance().saveArrayToFile("PAA", paa_residuals, "C:\\TEST\\MDFR\\Data\\Error_Coefficient_Ratio.csv");
		File.getInstance().saveArrayToFile("DFT", dft_residuals, "C:\\TEST\\MDFR\\Data\\Error_Coefficient_Ratio.csv");
		//		for(int i = 128 ; i>8 ; i=i/2 ){
//			pla.setWindowSize(i);
//			File.getInstance().saveLinkedListToFile("PLA["+ i + "]" ,pla.getFullResolutionDR(ts), "C:\\TEST\\MDFR\\Data\\Example.csv");		
//		}
//		File.getInstance().saveLinkedListToFile("DFT[2]" ,dft.getFullResolutionDR(ts), "C:\\TEST\\MDFR\\Data\\Example.csv");
//		File.getInstance().saveLinkedListToFile("PAA" ,paa.getFullResolutionDR(ts), "C:\\TEST\\MDFR\\Data\\Example.csv");
		
		
		
		
		
//		TimeSeries ttback = dr.getFullResolutionDR(new DFTData(hilbarray), tt);
//		TimeSeries ttback = dr.getFullResolutionDR(data, tt);
//		for(int i = 0 ; i < data.hilb().length ; i++){
//			hilb.add(new Data(i,data.hilb()[i]));
//		}
//		File.getInstance().saveLinkedListToFile("Original" ,ts, "C:\\TEST\\MDFR\\Data\\Example.csv");
//		File.getInstance().saveLinkedListToFile("TrendTT" ,t, "C:\\TEST\\MDFR\\Data\\Example.csv");
//		File.getInstance().saveLinkedListToFile("Seasonal" ,s, "C:\\TEST\\MDFR\\Data\\Example.csv");
//		File.getInstance().saveLinkedListToFile("Noise" ,n, "C:\\TEST\\MDFR\\Data\\Example.csv");
//		File.getInstance().saveLinkedListToFile("HLIB" ,hilb, "C:\\TEST\\MDFR\\Data\\Example.csv");
//		File.getInstance().saveLinkedListToFile("TTBACK" ,ttback, "C:\\TEST\\MDFR\\Data\\Example.csv");
		
	}
	
	
	
//	public void test(){
//    	Calendar cal = Calendar.getInstance();
//    	cal.getTime();
//    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//    	System.out.println("Start Time " + sdf.format(cal.getTime()) );
//    	
//		FileAccessAgent fagent = new FileAccessAgent("C:\\TEST\\MDFR\\Data\\NULL.txt", "C:\\TEST\\MDFR\\Data\\power_data.txt");
//		DataParser parser = new DataParser(new OneLineOneData(), fagent);
//		TimeSeries ts = parser.getTimeSeries(300,4500);
//		EMD emd = new EMD(ts, zerocrossingaccuracy, IFparamaters[0], IFparamaters[1], IFparamaters[2]);
//		System.out.println("EMD Init " + sdf.format(cal.getTime()) );
//		IMFS imfs = emd.getIMFs(100);
//		System.out.println("IMF Extraction " + sdf.format(cal.getTime()) );
//		
//		/*
//		 *  Store Results
//		 */
//		// Save Time References
//		File.getInstance().saveTimeToFile(ts, "C:\\TEST\\MDFR\\_EMD_Example2.csv");
//		
//		// Save Original Signal
//		File.getInstance().saveLinkedListToFile("Original" ,ts, "C:\\TEST\\MDFR\\_EMD_Example2.csv");
//				
//		// Save IMFs
//		for(int i = 0 ; i < imfs.size() ; i++){
//			File.getInstance().saveLinkedListToFile("IMF" + i ,imfs.get(i), "C:\\TEST\\MDFR\\_EMD_Example2.csv");
//		}
//		System.out.println("Store Complete " + sdf.format(cal.getTime()) );
//		
//	}
	
	
//	public void testwhiteNoise(){
//		double z = 5;
//		LinkedList<Data> y = new LinkedList<Data>();
//		LinkedList<Data> y_upper = new LinkedList<Data>();
//		LinkedList<Data> y_lower = new LinkedList<Data>();
//		
//		for(double x = 0 ; x < 10 ; x+=0.1){
//			y.add(new Data(x,-x));
//			y_upper.add(new Data(x,-x + 3*Math.sqrt((double)2/1000)*Math.pow(Math.E, x/2)));
//			y_lower.add(new Data(x,-x - 3*Math.sqrt((double)2/1000)*Math.pow(Math.E, x/2)));
//		}
//		File.getInstance().saveTimeToFile(y, "C:\\TEST\\MDFR\\Data\\white_noise.csv");
//		File.getInstance().saveLinkedListToFile("Original" ,y, "C:\\TEST\\MDFR\\Data\\white_noise.csv");
//		File.getInstance().saveLinkedListToFile("Upper" ,y_upper, "C:\\TEST\\MDFR\\Data\\white_noise.csv");
//		File.getInstance().saveLinkedListToFile("Lower" ,y_lower, "C:\\TEST\\MDFR\\Data\\white_noise.csv");
//	}
}
