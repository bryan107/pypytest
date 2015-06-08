//package mfdr.core;
//
//import java.util.LinkedList;
//
//import mfdr.datastructure.Data;
//import mfdr.datastructure.TimeSeries;
//import mfdr.dimensionality.reduction.DWT;
//import mfdr.dimensionality.reduction.MFDR;
//import mfdr.dimensionality.reduction.PLA;
//import mfdr.distance.Distance;
//import mfdr.distance.EuclideanDistance;
//import mfdr.learning.AngleLearning;
//import mfdr.learning.LR3DAngleLearning;
//import mfdr.learning.VarienceLearning;
//import junit.framework.TestCase;
//
//public class TestMFDRFacade extends TestCase {
//	private double white_noise_level = 5;
//	private double white_noise_threshold = 6.2;
//	private double min_NSratio = 0.5;
//	private double motif_FTratio = 0.5;
//	private int motif_k = 3;
//	private double motif_threshold = 0.1;
//	private double tolerancevarience = 3;
//	// ************** Used variables **************
//	private MFDRFacade facade = new MFDRFacade(white_noise_level,
//			white_noise_threshold, min_NSratio, motif_FTratio, motif_k, motif_threshold);
//	private LinkedList<TimeSeries> ts = new LinkedList<TimeSeries>();
//	private MFDR mfdr;
//	// **************** Test Cases ****************
//	public void testLearnWindowSize() {
//		for(int i = 0 ; i < 10 ; i++){
//			ts.add(generateTimeSeries(2048));
//		}
//		
//		// STEP 1
//		WindowSize ws= facade.learnWindowSizes(ts);
//		System.out.println("TREND: " + ws.trend() + " NOISE: " + ws.noise());
//		mfdr = new MFDR(ws.trend(), ws.noise());
//		
//		//-------------
//		
//		// STEP 2
//		LearningResults results = facade.learnParameters(ts,tolerancevarience , new EuclideanDistance(), ws);
//		System.out.print("A Learn:");
//		for(int i = 0; i < 3 ; i++){
//			System.out.print("[" + i + "]" + results.alearn().getParameters()[i]);
//		}
//		System.out.println();
//		System.out.println("V Learn:" + results.vlearn().getGuaranteedCompensation());
//		
//		//-------------
//		
//		LinkedList<TimeSeries> test = new LinkedList<TimeSeries>();
//		for(int i = 0 ; i < 2 ; i++){
//			test.add(generateTimeSeries(2048));
//		}
//		Distance d = new EuclideanDistance();
//		PLA pla = new PLA(ws.noise());
//		DWT dwt = new DWT(ws.noise());
//		// STEP 3
//		double distance = facade.getDistance(test.peekFirst(), test.peekLast(), d, ws, results);
//		
//		// Print Restuls
//		System.out.println("Original Distance:" + d.calDistance(test.peekFirst(), test.peekLast(), test.peekFirst()));
//		System.out.println("PLA:" + pla.getDistance(test.peekFirst(), test.peekLast(), d));
//		pla = new PLA(ws.trend());
//		System.out.println("PLA2:" + pla.getDistance(test.peekFirst(), test.peekLast(), d));
//		System.out.println("DWT:" + dwt.getDistance(test.peekFirst(), test.peekLast(), d));
//		dwt = new DWT(ws.trend());
//		System.out.println("DWT2:" + dwt.getDistance(test.peekFirst(), test.peekLast(), d));
//		System.out.println("Reduced Distance:" + distance);
//		
//		// TODO Solution does not work....change the algorithm to train with distance not angle
////		1. DWT can now deal with various lengths of time series.
////		2. MFDR does not work with original training, must train with reduced data
////		    Reduced data violates triangle inequality, must train with original distance rather than angle
//	}
//
//	public void testLearnParameters() {
////		LearningResults results = facade.learnParameters(ts,tolerancevarience , new EuclideanDistance());
////		System.out.print("A Learn:");
////		for(int i = 0; i < 3 ; i++){
////			System.out.print("[" + i + "]" + results.alearn().getParameters()[i]);
////		}
////		System.out.println();
////		System.out.println("V Learn:" + results.vlearn().getGuaranteedCompensation());
//	}
//
//	public void testGetDistance() {
//
//	}
//	
//	private TimeSeries generateTimeSeries(long size) {
//		TimeSeries ts = new TimeSeries();
//		for (double i = 0; i < size; i+=1) {
//			java.util.Random r = new java.util.Random();
//			double noise = 0; 
//			noise = r.nextGaussian() * Math.sqrt(5);
//			double trend = 1*Math.pow(i, 0.5);
//			if(i%200 == 0){
//				trend = trend + r.nextGaussian() * Math.sqrt(50);
//			}
////			double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 * Math.cos(i*Math.PI / 6)  + noise;
//			double value = 9.5 * Math.sin(i*Math.PI / 64) + trend + noise;
//			ts.add(new Data(i, value));
//		}
//		return ts;
////		return 1/(2*Math.PI*3);
//	}
//}
