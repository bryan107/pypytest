package mfdr.test;

import java.util.LinkedList;

import org.jtransforms.fft.DoubleFFT_1D;

import math.jwave.Transform;
import math.jwave.transforms.DiscreteFourierTransform;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.wavelets.haar.Haar1;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DWTData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import mfdr.utility.Print;
import junit.framework.TestCase;

public class TestDFT extends TestCase {
	private final String[] readaddress = { "C:\\TEST\\MFDR\\TEST_FFFF.csv",
			"C:\\TEST\\MFDR\\TEST_FFFF.csv" };
	private final String[] writeaddress = { "C:\\TEST\\MFDR\\TEST_FFFF.csv",
			"C:\\TEST\\MFDR\\TEST_FFFF.csv" };

	public void testDFT() {
		TimeSeries[] ts = {new TimeSeries(), new TimeSeries()};
//		generateResidual(ts[0], 8, 0, 128);
//		generateResidual(ts[1], 4, 0, 128);

		double[] valuearray1 = {1,0,-1,0,1,0,-1,0,1,0,-1,0,1,0,-1,0};
		double[] valuearray2 = {1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1};
//		double[] valuearray2 = {1,0.7,0,-0.7,-1,-0.7,0,0.7,1,0.7,0,-0.7,-1,-0.7,0,0.7};
		
//		LinkedList<Double> tsvalues1 = DataListOperator.getInstance()
//				.getValueList(ts[0]);
//		double[] valuearray1 = DataListOperator.getInstance()
//				.linkedDoubleListToArray(tsvalues1);
//		LinkedList<Double> tsvalues2 = DataListOperator.getInstance()
//				.getValueList(ts[1]);
//		double[] valuearray2 = DataListOperator.getInstance()
//				.linkedDoubleListToArray(tsvalues2);
		
		
		
		Transform t = new Transform(new DiscreteFourierTransform());
		DWTData[] hilb = new DWTData[2];
		hilb[0] = new DWTData(t.forward(valuearray1));
		hilb[1]= new DWTData(t.forward(valuearray2));
		System.out.println("LENGTH: " + hilb[0].hilb().length);
		
		System.out.println("FWAVE");
		System.out.println("11111");
		for(int i = 0 ; i < hilb[0].hilb().length; i++){
//			if(hilb[0].hilb()[i] > 0){
				hilb[0].hilb()[i] = hilb[0].hilb()[i]*Math.pow(hilb[0].hilb().length/2, 0.5);
				System.out.print("["+ i +"]: " + hilb[0].hilb()[i] );
//			}
		}System.out.println();
				
		System.out.println("2222");
		for(int i = 0 ; i < hilb[1].hilb().length; i++){
//			if(hilb[1].hilb()[i] > 0){
				hilb[1].hilb()[i] = hilb[1].hilb()[i]*Math.pow(hilb[1].hilb().length/2, 0.5);
				System.out.print("["+ i +"]: " + hilb[1].hilb()[i] );
//			}
		}
		double[] ori1 = t.reverse(hilb[0].hilb());
		double[] ori2 = t.reverse(hilb[1].hilb());
		
		System.out.println("Ori");
		System.out.println("11111");
		for(int i = 0 ; i < ori1.length; i++){
//			if(hilb[0].hilb()[i] > 0){
				System.out.print("["+ i +"]: " + ori1[i] );
//			}
		}System.out.println();
				
		System.out.println("2222");
		for(int i = 0 ; i < ori2.length; i++){
//			if(hilb[1].hilb()[i] > 0){
				System.out.print("["+ i +"]: " + ori2[i] );
//			}
		}
		
		
		System.out.println();
		Distance d = new EuclideanDistance();
		System.out.println("Original Distance:" + d.calDistance(valuearray1,valuearray2));
		System.out.println("DFT DIStance: " + d.calDistance(hilb[0].hilb(), hilb[1].hilb()));;
		
		
		
		
		
		
		
		/////***********************
		
		
		DoubleFFT_1D dfft = new DoubleFFT_1D(valuearray1.length);
		dfft.realForward(valuearray1);
		dfft.realForward(valuearray2);
		System.out.println("1111");	
		for(int i = 0 ; i < valuearray1.length; i++){
//			if(hilb[0].hilb()[i] > 0){
			valuearray1[i] = valuearray1[i]/Math.pow(valuearray1.length/2, 0.5);
				System.out.print("["+ i +"]: " + valuearray1[i] );
//			}
		}
		System.out.println();
		System.out.println("2222");
		for(int i = 0 ; i < valuearray2.length; i++){
//			if(hilb[1].hilb()[i] > 0){
				valuearray2[i] = valuearray2[i]/Math.pow(valuearray2.length/2, 0.5);
				System.out.print("["+ i +"]: " + valuearray2[i] );
//			}
		}
		System.out.println();
		System.out.println("DDFT DIStance: " + d.calDistance(valuearray1,valuearray2));

		
		
		dfft.realInverse(valuearray1, true);
		dfft.realInverse(valuearray2, true);
		System.out.println("Ori");
		System.out.println("1111");	
		for(int i = 0 ; i < valuearray1.length; i++){
//			if(hilb[0].hilb()[i] > 0){
				System.out.print("["+ i +"]: " + valuearray1[i] );
//			}
		}System.out.println();
		
		System.out.println("2222");
		for(int i = 0 ; i < valuearray2.length; i++){
//			if(hilb[1].hilb()[i] > 0){
				System.out.print("["+ i +"]: " + valuearray2[i] );
//			}
		}
		System.out.println();
		System.out.println("DDFT RDIStance: " + d.calDistance(valuearray1,valuearray2));

	}

	private void generateResidual(LinkedList<Data> residual,
			double trendvariation, double noisevariation, long size) {
		for (double i = 0; i < size; i += 1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			// double trend = trendvariation * Math.pow(i, 0.5);
			double value = Math.sin(i * Math.PI / trendvariation);
			residual.add(new Data(i, value));
		}
	}

}
