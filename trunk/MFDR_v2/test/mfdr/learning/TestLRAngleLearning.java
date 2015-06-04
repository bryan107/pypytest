package mfdr.learning;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.DWT;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.learning.LR3DAngleLearning;
import mfdr.learning.VarienceLearning;
import mfdr.learning.datastructure.TrainingSet;
import junit.framework.TestCase;

public class TestLRAngleLearning extends TestCase {
	private final int datalength = 4096;
	private int setsize = 5;
	private final double T1 = 512;
	private final double T2 = 16;

	public void testTrainingParameters() {
		PLA pla= new PLA(T1);
		DWT dtw= new DWT(T1);
		MFDR mfdr = new MFDR(T1, T2);
		Distance d = new EuclideanDistance();
		TimeSeries[][] ts = new TimeSeries[setsize][2];
		LinkedList<TrainingSet> trainingset = new LinkedList<TrainingSet>();
		System.out.println();
		
		// ****** DATA GENERATION ******* //
		long starttime = System.currentTimeMillis();
		for (int i = 0; i < setsize; i++) {
			ts[i] = generateTimeSeries();
		}
		System.out.println("Data Generation Time: " + (System.currentTimeMillis() - starttime));
		// ****************************** //
		
		// ********** DISTANCE DECOMPOSITION ************ //
		long sstarttime = System.currentTimeMillis();
		for (int i = 0; i < setsize; i++) {
			
			// TODO replace getdistanceDetails with a return object.
			TimeSeries t1 = mfdr.getTrend(ts[i][0]);
			TimeSeries t2 = mfdr.getTrend(ts[i][1]);
			
			starttime = System.currentTimeMillis();
			double trenddist = d.calDistance(t1, t2, ts[i][0]);
			System.out.println("P1: " + (System.currentTimeMillis() - starttime));
			
			starttime = System.currentTimeMillis();
			double freqdist = d.calDistance(mfdr.getResidual(ts[i][0]), mfdr.getResidual(ts[i][1]), ts[i][0]);
			System.out.println("P2: " + (System.currentTimeMillis() - starttime));
			
			starttime = System.currentTimeMillis();
			double oridist = d.calDistance(ts[i][0], ts[i][1], ts[i][0]);
			System.out.println("P3: " + (System.currentTimeMillis() - starttime));
			
			trainingset.add(new TrainingSet(trenddist, freqdist, oridist));
		}
		System.out.println("Prepare Training Time: " + (System.currentTimeMillis() - sstarttime));
		// ********************************************** //
		
		// ***************** ANGLE LEARNING ************** //
		starttime = System.currentTimeMillis();
		LR3DAngleLearning alearn = new LR3DAngleLearning();
		alearn.trainingParameters(trainingset);
		System.out.println("Angle Training Time: " + (System.currentTimeMillis() - starttime));
		// *********************************************** //
		
		// ************* TEST EXAMPLE ************ //
		double pladist = mfdr.getDistanceDetails(ts[0][0], ts[0][1], d).pla();
		double dwtdist = mfdr.getDistanceDetails(ts[0][0], ts[0][1], d).dwt();
		System.out.println("=TEST EXAMPLE=");
		System.out.println("PLA:" + pladist + "  DWT:" + dwtdist);
		System.out.println("Angle:" + alearn.getAngle(pladist, dwtdist));
		System.out.println();
		// *************************************** //
		
		// Error Training
		TimeSeries[] gg = new TimeSeries[2];
		for (int i = 0; i < 2; i++) {
			gg = generateTimeSeries();
		}
		
		starttime = System.currentTimeMillis();
		VarienceLearning vlearn = new VarienceLearning(trainingset, alearn, 3);
		System.out.println("Varience Training Time: " + (System.currentTimeMillis() - starttime));
		
		// *************   PRINT RESTULS *************//
		starttime = System.currentTimeMillis();
		MFDRDistanceDetails mdfrdetails = mfdr.getDistanceDetails(gg[0], gg[1], d);
		pladist = mdfrdetails.pla();
		dwtdist = mdfrdetails.dwt();
		starttime = System.currentTimeMillis();
		double d1 = pla.getDistance(gg[0], gg[1], d);
		System.out.println("D1: " + (System.currentTimeMillis() - starttime));
		double d2 = dtw.getDistance(gg[0], gg[1], d);
		System.out.println("D2: " + (System.currentTimeMillis() - starttime));
		System.out.println("PLA:" + pla.getDistance(gg[0], gg[1], d) + "  DWT:" + dtw.getDistance(gg[0], gg[1], d));
		
		double angle = alearn.getAngle(pladist, dwtdist);
//		mfdr.updateAngle(angle);
		starttime = System.currentTimeMillis();
		double mfdrdistance = mfdr.getDistance(gg[0], gg[1], d);
		System.out.println("E2: " + (System.currentTimeMillis() - starttime));
		System.out.println("Origin:" + d.calDistance(gg[0], gg[1], gg[0]));
		System.out.println("MFDR:" + mfdrdistance);
		System.out.println("MFDR_Corrected:" + vlearn.getGuaranteedCompensation(mfdrdistance));
		System.out.println("Compensation:" + vlearn.getGuaranteedCompensation());
		System.out.println("End Time: " + (System.currentTimeMillis() - starttime));
	}

	private TimeSeries[] generateTimeSeries() {
		TimeSeries ts1 = new TimeSeries();
		generateResidual(ts1, 10, 1, datalength);
		TimeSeries ts2 = new TimeSeries();
		generateResidual(ts2, 5, 2, datalength);
		TimeSeries[] tsarray = { ts1, ts2 };
		return tsarray;
	}

	private void generateResidual(LinkedList<Data> residual,
			double trendvariation, double noisevariation, long size) {
		for (double i = 0; i < size; i += 1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			double trend = trendvariation * Math.pow(i, 0.5);
			// if (i > size / 2) {
			// trend = -trend;
			// }
			double value = trend * Math.sin(i * Math.PI / 12) + trendvariation
					* 21 + noise;
			residual.add(new Data(i, value));
		}
	}
}
