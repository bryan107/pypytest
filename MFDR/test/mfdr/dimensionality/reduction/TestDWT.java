package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.DWT;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.DataListOperator;
import mfdr.utility.Print;
import junit.framework.TestCase;

public class TestDWT extends TestCase {

	private final long datasize = 128;
	private final int windowsize = 8;
	
	public void testGetDR(){
		TimeSeries ts1 = new TimeSeries();
		TimeSeries ts2 = new TimeSeries();
		Distance dist = new EuclideanDistance();
		DWT dwt = new DWT(windowsize);
		
		generateResidual(ts1, 10, 1, datasize);
		generateResidual(ts2, 5, 1, datasize);
		

		TimeSeries dr1full = dwt.getFullResolutionDR(ts1);
		TimeSeries dr2full = dwt.getFullResolutionDR(ts2);
		double[] dr1 = (double[]) dwt.getDR(ts1).hilb();
		double[] dr2 = (double[]) dwt.getDR(ts2).hilb();
		
		// Print Restuls
		System.out.println("DR 1: ");
		Print.getInstance().printArray(dr1, 128);
		System.out.println("DR 2: ");
		Print.getInstance().printArray(dr2, 128);
		System.out.println("DR 1 FULL: ");
		Print.getInstance().printDataLinkedList(dr1full, 128);
		System.out.println("DR 2 FULL: ");
		Print.getInstance().printDataLinkedList(dr2full, 128);


		double[][] ts1array = DataListOperator.getInstance().linkedDataListToArray(ts1);
		double[][] ts2array = DataListOperator.getInstance().linkedDataListToArray(ts2);
		System.out.println("Distance Original: " + dist.calDistance(ts1array[1], ts2array[1]));
		System.out.println("Distance DWT: " + dwt.getDistance(ts1, ts2, dist));
		System.out.println("Distance DWT Reverse: " + dwt.getDistanceTest(ts1, ts2, dist));
		
		
		// Save Restuls
//		File.getInstance().saveTimeToFile(ts1, "C:\\TEST\\MDFR\\DWT.csv");
//		File.getInstance().saveLinkedListToFile(ts1, "C:\\TEST\\MDFR\\DWT.csv");
//		File.getInstance().saveLinkedListToFile(ts2, "C:\\TEST\\MDFR\\DWT.csv");
//		File.getInstance().saveLinkedListToFile(dr1full, "C:\\TEST\\MDFR\\DWT.csv");
//		File.getInstance().saveLinkedListToFile(dr2full, "C:\\TEST\\MDFR\\DWT.csv");
	}
	
	public void testGetFullResolutionDR(){
		
	}
	
	
	private void generateResidual(LinkedList<Data> residual, double trendvariation, double noisevariation , long size) {
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			double trend = trendvariation * Math.pow(i, 0.5);
			if(i > size/2){
				trend = -trend;
			}
//			double value = trend * Math.sin(i*Math.PI / 3);
			double value = noise + trend;
			residual.add(new Data(i, value));
		}
	}
	
//	private void generateResidual(LinkedList<Data> residual, double size, int option) {
//		double[ ] arrTime = { 1, 2, 3, 4, 0, 0, 0, 0};
//		double[ ] arrTime2 = { 2, 2, 4, 4, 0, 0, 0, 0};
//		for (int i = 0; i < size; i+=1) {
//			if(option == 0){
//				residual.add(new Data(i,arrTime[i]));
//			}else{
//				residual.add(new Data(i,arrTime2[i]));
//			}
//		}
//	}
}
