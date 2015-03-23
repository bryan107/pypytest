package mdfr.dimensionality.reduction;

import java.util.LinkedList;

import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.distance.Distance;
import mdfr.distance.EuclideanDistance;
import mdfr.utility.File;
import mdfr.utility.Print;
import junit.framework.TestCase;

public class TestALL extends TestCase {
	private final double T1 = 16;
	private final double T2 = 8;		
	private final double W = 0.5;
	private final String[] readaddress = {"C:\\TEST\\MDFR\\TEST_1.csv","C:\\TEST\\MDFR\\TEST_2.csv"};
	private final String[] writeaddress = {"C:\\TEST\\MDFR\\TEST_1.csv","C:\\TEST\\MDFR\\TEST_2.csv"};
	
	public void testALL(){
		PLA pla = new PLA(T2);
		DWT dwt = new DWT(T2);
		MFDR mfdr = new MFDR(T1, T2, W, 1-W);
		// Write
		TimeSeries[]ts = storeAndGenerateTimeSeries();
		for(int i = 0 ; i < ts.length ; i++){
			// Generate DR results in full resolution
			TimeSeries plafull = pla.getFullResolutionDR(ts[i]);
			TimeSeries dwtfull = dwt.getFullResolutionDR(ts[i]);
			TimeSeries mfdrfull = mfdr.getFullResolutionDR(ts[i]);
			// Save Data into File
			File.getInstance().saveLinkedListToFile("PLA", plafull, writeaddress[i]);
			File.getInstance().saveLinkedListToFile("DWT", dwtfull, writeaddress[i]);
			File.getInstance().saveLinkedListToFile("MFDR", mfdrfull, writeaddress[i]);
		}
		
		// Read 
//		TimeSeries ts1 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[0]);
//		TimeSeries ts2 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[1]);
//		TimeSeries pla1 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[0], "PLA");
//		TimeSeries pla2 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[1], "PLA");
//		TimeSeries dwt1 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[0], "DWT");
//		TimeSeries dwt2 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[1], "DWT");
//		TimeSeries mfdr1 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[0], "MFDR");
//		TimeSeries mfdr2 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readaddress[1], "MFDR");
//		
//		// Print TIME Series
//		System.out.println("TS1:");
//		Print.getInstance().printDataLinkedList(ts1, 128);
//		System.out.println("TS2:");
//		Print.getInstance().printDataLinkedList(ts2, 128);
//		System.out.println("PLA1:");
//		Print.getInstance().printDataLinkedList(pla1, 128);
//		System.out.println("PLA2:");
//		Print.getInstance().printDataLinkedList(pla2, 128);
//		System.out.println("DWT1:");
//		Print.getInstance().printDataLinkedList(dwt1, 128);
//		System.out.println("DWT2:");
//		Print.getInstance().printDataLinkedList(dwt2, 128);
//		System.out.println("MFDR1:");
//		Print.getInstance().printDataLinkedList(mfdr1, 128);
//		System.out.println("MFDR2:");
//		Print.getInstance().printDataLinkedList(mfdr2, 128);
//		
//		
//		System.out.println("Distance Results:");
//		Distance distance = new EuclideanDistance();
//		System.out.println("TS:" + distance.calDistance(ts1, ts2, ts1));
//		System.out.println("PLA:" + distance.calDistance(pla1, pla2, ts1));
//		System.out.println("DWT:" + distance.calDistance(dwt1, dwt2, ts1));
//		System.out.println("MFDR:" + distance.calDistance(mfdr1, mfdr2, ts1));
//		
		
	}
	
	private TimeSeries[] storeAndGenerateTimeSeries(){
		TimeSeries ts1 = new TimeSeries();
		generateResidual(ts1, 10, 1, 256);
		TimeSeries ts2 = new TimeSeries();
		generateResidual(ts2, 5, 1, 256);
		
		File.getInstance().saveTimeToFile(ts1, writeaddress[0]);
		File.getInstance().saveLinkedListToFile("TS", ts1, writeaddress[0]);
		File.getInstance().saveTimeToFile(ts2, writeaddress[1]);
		File.getInstance().saveLinkedListToFile("TS", ts2, writeaddress[1]);
		TimeSeries[] tsarray = {ts1,ts2};
		return tsarray;
	}
	
	private void generateResidual(LinkedList<Data> residual, double trendvariation, double noisevariation , long size) {
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			double trend = trendvariation * Math.pow(i, 0.5);
			if(i > size/2){
				trend = -trend;
			}
//			double value = trend * Math.sin(Math.pow(i, 3))+ noise;
			double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 * Math.cos(i*Math.PI / 6)  + noise + trend;
			residual.add(new Data(i, value));
		}
	}
}
