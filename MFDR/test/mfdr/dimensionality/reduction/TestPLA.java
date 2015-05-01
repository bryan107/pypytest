package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.*;
import junit.framework.TestCase;

public class TestPLA extends TestCase {

	private long datasize = 8;  
	TimeSeries ts1 = new TimeSeries();
	TimeSeries ts2 = new TimeSeries();
	PLA pla = new PLA(4);
	
	public void testGetDR(){

//		generateResidual(ts1, datasize, 0);
//		generateResidual(ts2, datasize, 1);
		generateResidual(ts1, 10, 1, datasize);
		generateResidual(ts2, 10, 1, datasize);
		PLA pla = new PLA(4);
		LinkedList<PLAData> dr1 = pla.getDR(ts1);
		LinkedList<PLAData> dr2 = pla.getDR(ts2);
		System.out.println("DR 1: ");
		Print.getInstance().printPLADataLinkedList(dr1, 100);
		System.out.println("DR 2: ");
		Print.getInstance().printPLADataLinkedList(dr2, 100);
		TimeSeries dr1full = pla.getFullResolutionDR(ts1);
		TimeSeries dr2full = pla.getFullResolutionDR(ts2);
		System.out.println("DR 1 FULL: ");
		Print.getInstance().printDataLinkedList(dr1full, 100);
		System.out.println("DR 2 FULL: ");
		Print.getInstance().printDataLinkedList(dr2full, 100);
	}
	
	public void testGetFullResolutionDR(){
		
	}
	
	public void testDistance(){
		// TestDist
		generateResidual(ts1, 10, 1, datasize);
		generateResidual(ts2, 10, 1, datasize);
		Distance dist = new EuclideanDistance();
		double[][] ts1array = DataListOperator.getInstance().linkedDataListToArray(ts1);
		double[][] ts2array = DataListOperator.getInstance().linkedDataListToArray(ts2);
		System.out.println("Distance Original: " + dist.calDistance(ts1array[1], ts2array[1]));
		System.out.println("Distance PLA: " + pla.getDistance(ts1, ts2, dist));
		
//		File.getInstance().saveTimeToFile(ts1, "C:\\TEST\\MDFR\\PLA.csv");
//		File.getInstance().saveLinkedListToFile(ts1, "C:\\TEST\\MDFR\\PLA.csv");
//		File.getInstance().saveLinkedListToFile(ts2, "C:\\TEST\\MDFR\\PLA.csv");
//		File.getInstance().saveLinkedListToFile(dr1full, "C:\\TEST\\MDFR\\PLA.csv");
//		File.getInstance().saveLinkedListToFile(dr2full, "C:\\TEST\\MDFR\\PLA.csv");
	}
	
	private void generateResidual(LinkedList<Data> residual, double trendvariation, double noisevariation , long size) {
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			double trend = trendvariation * Math.pow(i, 0.5);
			if(i > size/2){
				trend = -trend;
			}
//			double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 * Math.cos(i*Math.PI / 6)  + noise + trend;
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
