package mfdr.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.jtransforms.fft.DoubleFFT_1D;

import math.jwave.Transform;
import math.jwave.transforms.DiscreteFourierTransform;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.wavelets.haar.Haar1;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.datastructure.DWTData;
import mfdr.dimensionality.datastructure.NewDFTData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import mfdr.utility.Print;
import mfdr.utility.ValueComparator;
import junit.framework.TestCase;

public class TestDFT extends TestCase {
	private final String[] readaddress = { "C:\\TEST\\MFDR\\TEST_FFFF.csv",
			"C:\\TEST\\MFDR\\TEST_FFFF.csv" };
	private final String[] writeaddress = { "C:\\TEST\\MFDR\\TEST_FFFF.csv",
			"C:\\TEST\\MFDR\\TEST_FFFF.csv" };

	
	
	public void testNewDFT(){
		TimeSeries[] ts = {new TimeSeries(), new TimeSeries()};
		generateTimeSeries(ts[0], 64, 0, 512);
		generateTimeSeries(ts[1], 16, 0, 512);
		
		File.getInstance().saveLinkedListToFile("TS_0", ts[0], "C:\\Programming\\TEST\\DFT.csv");
		File.getInstance().saveLinkedListToFile("TS_1", ts[1], "C:\\Programming\\TEST\\DFT.csv");
		
		DFT dft = new DFT(8);
		NewDFTData dftdata1, dftdata2;
		dftdata1 = dft.getDR(ts[0]);
		dftdata2 = dft.getDR(ts[1]);
		TimeSeries[] drfull = {new TimeSeries(), new TimeSeries()};
		drfull[0] = dft.getFullResolutionDR(ts[0]);
		drfull[1] = dft.getFullResolutionDR(ts[1]);
				
		File.getInstance().saveLinkedListToFile("DR_0", drfull[0], "C:\\Programming\\TEST\\DFT.csv");
		File.getInstance().saveLinkedListToFile("DR_1", drfull[1], "C:\\Programming\\TEST\\DFT.csv");
		
		Distance dist = new EuclideanDistance();
		System.out.println("Original Dist:" + dist.calDistance(ts[0], ts[1], ts[0]));
		System.out.println("DR Dist:" + dft.getDistance(dftdata1, dftdata2, dist, ts[0].size()));
		
		System.out.println("DONE");
	}
	
	
	
//	public void testDFT() {
//		TimeSeries[] ts = {new TimeSeries(), new TimeSeries()};
////		generateResidual(ts[0], 8, 0, 128);
////		generateResidual(ts[1], 4, 0, 128);
//		double[] valuearray1 = new double[512];
//		double[] valuearray2 = new double[500];
//		double[] valuearray3 = new double[500];
//		java.util.Random r = new java.util.Random();
//		double noise = 0;
//		for(int i = 0 ; i < 500 ; i++){
//			valuearray1[i] = 2*Math.sin(i*Math.PI/20)+ r.nextGaussian() * Math.sqrt(0.5);
//		}
//		for(int i = 0 ; i < 500 ; i++){
//			valuearray2[i] = Math.sin(i*Math.PI/2)+Math.sin(i*Math.PI/5)+Math.sin(i*Math.PI/10)+Math.sin(i*Math.PI/50)+Math.sin(i*Math.PI/100)+Math.sin(i*Math.PI/250);
//		}
//		for(int i = 0 ; i < 500 ; i++){
//			valuearray3[i] = Math.sin(i*Math.PI/16);
//		}
//		File.getInstance().saveArrayToFile("TEST_1", valuearray1, "C:\\Programming\\TEST\\TEST.csv");
//		File.getInstance().saveArrayToFile("TEST_2", valuearray2, "C:\\Programming\\TEST\\TEST.csv");
////		double[] valuearray1 = {1,0,-1,0,1,0,-1,0,1,0,-1,0,1,0,-1,0};
////		double[] valuearray2 = {0,1,0,-1,0,1,0,-1,0,1,0,-1,0,1,0,-1};
////		double[] valuearray3 = {-1,0,1,0,-1,0,1,0,-1,0,1,0,-1,0,1,0};
////		double[] valuearray4 = {1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1};
//		double[] valuearray5 = new double[16];
//		for(int i = 0 ; i < 16 ; i++){
//			if(i<16){
//				ts[0].add(new Data(i+1, valuearray1[i]));
//				ts[1].add(new Data(i+1, valuearray2[i]));
//			}
//
//			if(i==15)
//				valuearray5[i] = 1;
//			else
//				valuearray5[i] = 0;
//		}
//
////		DFT dft = new DFT(16, 1);
//		
//
//		
//		Transform t = new Transform(new DiscreteFourierTransform());
//		DFTData[] hilb = new DFTData[3];
//		hilb[0] = new DFTData(t.forward(valuearray1));
//		hilb[1]= new DFTData(t.forward(valuearray2));
//		hilb[2]= new DFTData(t.forward(valuearray3));
//		System.out.println("LENGTH: " + hilb[0].hilb().length);
//		double[] test2 = new double[512];
//		double[] test3 = new double[512];
//		double[] test4 = new double[512];
//		double[] test1 = new double[512];
//		for(int i = 0 ; i < 512; i++){
//			test1[i] = 0;
//			test2[i] = 0;
//			test3[i] = 0;
//			test4[i] = 0;
//			if(i == 256)
//				test1[i] = 1; 
//			if(i == 257)
//				test1[i] = 1; 
//			if(i == 2)
//				test2[i] = 1; 
//			if(i == 511)
//				test2[i] = 1; 
//			if(i == 2)
//				test3[i] = 1; 
//			if(i == 511)
//				test4[i] = 1; 
//		}
//		DoubleFFT_1D fft = new DoubleFFT_1D(500);
////		fft.realForward(valuearray2);
//		double[] reverse1 = t.reverse(test1);
//		double[] reverse2 = t.reverse(test2);
//		double[] reverse3 = t.reverse(test3);
//		double[] reverse4 = t.reverse(test4);
//		File.getInstance().saveArrayToFile("HLIB_1", hilb[0].hilb(), "C:\\Programming\\TEST\\TEST.csv");
//		File.getInstance().saveArrayToFile("Com", valuearray2, "C:\\Programming\\TEST\\TEST.csv");
//		File.getInstance().saveArrayToFile("Real", valuearray1, "C:\\Programming\\TEST\\TEST.csv");
//		fft.realForward(valuearray1);
//		File.getInstance().saveArrayToFile("Real2", valuearray1, "C:\\Programming\\TEST\\TEST.csv");
//		for(int i = 29 ; i<500 ; i++ ){
//			valuearray1[i] = 0;
//		}
//		for(int i = 0 ; i<23 ; i++ ){
//			valuearray1[i] = 0;
//		}
//		fft.realInverse(valuearray1, true);
//		File.getInstance().saveArrayToFile("High Freq remove", valuearray1, "C:\\Programming\\TEST\\TEST.csv");
////		fft.realInverse(valuearray2, true);
////		File.getInstance().saveArrayToFile("Com_False", valuearray2, "C:\\Programming\\TEST\\TEST.csv");
////		fft.realForward(valuearray2);
////		fft.realInverse(valuearray2, true);
////		File.getInstance().saveArrayToFile("Com_True", valuearray2, "C:\\Programming\\TEST\\TEST.csv");
////		fft.realForward(valuearray2);
//		Distance dist = new EuclideanDistance();
//		double dist_o = dist.calDistance(valuearray2, valuearray3);
//		File.getInstance().saveArrayToFile("V2", valuearray2, "C:\\Programming\\TEST\\TEST.csv");
//		File.getInstance().saveArrayToFile("V3", valuearray3, "C:\\Programming\\TEST\\TEST.csv");
//		fft.realForward(valuearray2);
//		fft.realForward(valuearray3);
//		double dist_f = dist.calDistance(valuearray2, valuearray3)/Math.pow(valuearray2.length/2, 0.5);
//		File.getInstance().saveArrayToFile("VV2", valuearray2, "C:\\Programming\\TEST\\TEST.csv");
//		File.getInstance().saveArrayToFile("VV3", valuearray3, "C:\\Programming\\TEST\\TEST.csv");
//		
//		System.out.println("Dist_O:" + dist_o);
//		System.out.println("Dist_f:" + dist_f);
////		File.getInstance().saveArrayToFile("HLIB_1_Cut", hilb[0].hilb(), "C:\\Programming\\TEST\\TEST.csv");
////		File.getInstance().saveArrayToFile("Reverse_1", reverse, "C:\\Programming\\TEST\\TEST.csv");
////		File.getInstance().saveArrayToFile("HILB_2", hilb[1].hilb(), "C:\\Programming\\TEST\\TEST.csv");
////		
//		// TEST Transform
////		for(int j = 0 ; j < 3 ; j++){
////			System.out.println("HLIB[" + j + "]");
////			for(int i = 0 ; i < hilb[j].hilb().length; i++){
////					System.out.print("["+ i +"]: " + hilb[j].hilb()[i] );
////			}System.out.println();
////		}
////		
////		Map<Integer, Double> map = new HashMap<Integer, Double>();
////		ValueComparator bvc =  new ValueComparator(map);
////		TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(bvc);
////		LinkedList<Integer> number = new LinkedList<Integer>();
////		for(int i = 0 ; i < hilb[0].hilb().length ; i++){
////			map.put(i, hilb[0].hilb()[i]);
////		}
////		sorted_map.putAll(map);
//		
//
////		for(int j = 0 ; j < 2 ; j++){
////			System.out.println("DFT[" + j + "]");
////			for(int i = 0 ; i < dft.getDR(ts[j]).hilb().length; i++){
////					System.out.print("["+ i +"]: " + dft.getDR(ts[j]).hilb()[i] );
////			}System.out.println();
////		}
//		
//		// Test Revserse
////		double[] ori1 = t.reverse(hilb[0].hilb());
////		double[] ori2 = t.reverse(hilb[1].hilb());
////		
////		System.out.println("ORIGIN[0]");
////		for(int i = 0 ; i < ori1.length; i++){
////			System.out.print("["+ i +"]: " + ori1[i] );
////		}System.out.println();
////				
////		System.out.println("ORIGIN[1]");
////		for(int i = 0 ; i < ori2.length; i++){
////			System.out.print("["+ i +"]: " + ori2[i] );
////		}
////		
////		for(int j = 0 ; j < 2 ; j++){
////			System.out.println("DFT_ORIGIN[" + j + "]");
////			TimeSeries temp = dft.getFullResolutionDR(ts[j]);
////			for(int i = 0 ; i < temp.size(); i++){
////				System.out.print("["+ i +"]: " + temp.get(i) );
////			}System.out.println();
////		}
////		
////		
////		// TEST Distance
////		System.out.println();
////		Distance d = new EuclideanDistance();
////		System.out.println("Original Distance:" + d.calDistance(valuearray1,valuearray2));
////		System.out.println("DFT DIStance: " + d.calDistance(hilb[0].hilb(), hilb[1].hilb()));
////		System.out.println("DFT Fixed DIStance: " + d.calDistance(hilb[0].hilb(), hilb[1].hilb())*Math.pow(hilb[0].hilb().length/2, 0.5));
//		
//		
//		
//		/////***********************
////		
////		
////		DoubleFFT_1D dfft = new DoubleFFT_1D(valuearray1.length);
////		dfft.realForward(valuearray1);
////		dfft.realForward(valuearray2);
////		System.out.println("1111");	
////		for(int i = 0 ; i < valuearray1.length; i++){
//////			if(hilb[0].hilb()[i] > 0){
////			valuearray1[i] = valuearray1[i]/Math.pow(valuearray1.length/2, 0.5);
////				System.out.print("["+ i +"]: " + valuearray1[i] );
//////			}
////		}
////		System.out.println();
////		System.out.println("2222");
////		for(int i = 0 ; i < valuearray2.length; i++){
//////			if(hilb[1].hilb()[i] > 0){
////				valuearray2[i] = valuearray2[i]/Math.pow(valuearray2.length/2, 0.5);
////				System.out.print("["+ i +"]: " + valuearray2[i] );
//////			}
////		}
////		System.out.println();
////		System.out.println("DDFT DIStance: " + d.calDistance(valuearray1,valuearray2));
////
////		
////		
////		dfft.realInverse(valuearray1, true);
////		dfft.realInverse(valuearray2, true);
////		System.out.println("Ori");
////		System.out.println("1111");	
////		for(int i = 0 ; i < valuearray1.length; i++){
//////			if(hilb[0].hilb()[i] > 0){
////				System.out.print("["+ i +"]: " + valuearray1[i] );
//////			}
////		}System.out.println();
////		
////		System.out.println("2222");
////		for(int i = 0 ; i < valuearray2.length; i++){
//////			if(hilb[1].hilb()[i] > 0){
////				System.out.print("["+ i +"]: " + valuearray2[i] );
//////			}
////		}
////		System.out.println();
////		System.out.println("DDFT RDIStance: " + d.calDistance(valuearray1,valuearray2));
////
//	}

	private void generateTimeSeries(LinkedList<Data> residual,
			double seasonalvariation, double noisevariation, long size) {
		for (double i = 0; i < size; i += 1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			// double trend = trendvariation * Math.pow(i, 0.5);
			double value = Math.sin(i * Math.PI / seasonalvariation);
			residual.add(new Data(i, value));
		}
	}

}
