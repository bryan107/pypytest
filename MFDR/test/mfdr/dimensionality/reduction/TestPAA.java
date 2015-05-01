package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.PAAData;
import mfdr.dimensionality.reduction.PAA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import mfdr.utility.Print;
import junit.framework.TestCase;

public class TestPAA extends TestCase {
	private long datasize = 100;  
	TimeSeries ts1 = new TimeSeries();
	TimeSeries ts2 = new TimeSeries();
	PAA paa = new PAA(10);
	
	public void testGetDR(){
		generateResidual(ts1, 10, 1, datasize);
		generateResidual(ts2, 10, 1, datasize);
		LinkedList<PAAData> dr1 = paa.getDR(ts1);
		LinkedList<PAAData> dr2 = paa.getDR(ts2);
		System.out.println("DR 1: ");
		Print.getInstance().printPAADataLinkedList(dr1, 100);
		System.out.println("DR 2: ");
		Print.getInstance().printPAADataLinkedList(dr2, 100);
		TimeSeries dr1full = paa.getFullResolutionDR(ts1);
		TimeSeries dr2full = paa.getFullResolutionDR(ts2);
		System.out.println("DR 1 FULL: ");
		Print.getInstance().printDataLinkedList(dr1full, 100);
		System.out.println("DR 2 FULL: ");
		Print.getInstance().printDataLinkedList(dr2full, 100);
	}
	
	public void testGetFullResolutionDR(){
		
	}
	
	public void testDistance(){
		generateResidual(ts1, 10, 1, datasize);
		generateResidual(ts2, 10, 1, datasize);
		// TestDist
		Distance dist = new EuclideanDistance();
		double[][] ts1array = DataListOperator.getInstance().linkedDataListToArray(ts1);
		double[][] ts2array = DataListOperator.getInstance().linkedDataListToArray(ts2);
		System.out.println("TS1");
		Print.getInstance().printArray(ts1array[1], 100);
		System.out.println("TS2");
		Print.getInstance().printArray(ts2array[1], 100);

		TimeSeries dr1full = paa.getFullResolutionDR(ts1);
		TimeSeries dr2full = paa.getFullResolutionDR(ts2);
		
		System.out.println("DR 1 FULL: ");
		Print.getInstance().printDataLinkedList(dr1full, 100);
		System.out.println("DR 2 FULL: ");
		Print.getInstance().printDataLinkedList(dr2full, 100);

		System.out.println("Distance Original: " + dist.calDistance(ts1array[1], ts2array[1]));
		System.out.println("Distance PLA: " + paa.getDistance(ts1, ts2, dist));
		
		File.getInstance().saveTimeToFile(ts1, "C:\\TEST\\MDFR\\PAA.csv");
//		File.getInstance().saveLinkedListToFile(ts1, "C:\\TEST\\MDFR\\PAA.csv");
//		File.getInstance().saveLinkedListToFile(ts2, "C:\\TEST\\MDFR\\PAA.csv");
//		File.getInstance().saveLinkedListToFile(dr1full, "C:\\TEST\\MDFR\\PAA.csv");
//		File.getInstance().saveLinkedListToFile(dr2full, "C:\\TEST\\MDFR\\PAA.csv");
	}
	
	private double generateResidual(LinkedList<Data> residual, double trendvariation, double noisevariation , long size) {
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			double trend = trendvariation * Math.pow(i, 0.5);
			if(i > size/2){
				trend = -trend;
			}
			double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 * Math.cos(i*Math.PI / 6)  + noise + trend;
//			double value = noise + trend;
			residual.add(new Data(i, value));
		}
		return (double)1/6;
//		return 1/(2*Math.PI*3);
	}
}
