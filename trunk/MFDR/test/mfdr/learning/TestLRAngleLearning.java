package mfdr.learning;

import java.util.LinkedList;

import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.dimensionality.reduction.MFDR;
import mdfr.dimensionality.reduction.PLA;
import mdfr.distance.Distance;
import mdfr.distance.EuclideanDistance;
import mdfr.learning.LRAngleLearning;
import mdfr.learning.VarienceLearning;
import mdfr.learning.datastructure.TrainingSet;
import junit.framework.TestCase;

public class TestLRAngleLearning extends TestCase {
	private final int datalength = 1024;
	private int setsize = 5;
	private final double T1 = 64;
	private final double T2 = 16;
	private final double ANGLE = Math.PI;

	public void testTrainingParameters() {
		PLA pla= new PLA(T1);
		PLA dtw= new PLA(T1);
		MFDR mfdr = new MFDR(T1, T2);
		Distance d = new EuclideanDistance();
		TimeSeries[][] ts = new TimeSeries[setsize][2];
		LinkedList<TrainingSet> trainingset = new LinkedList<TrainingSet>();
		for (int i = 0; i < setsize; i++) {
			ts[i] = generateTimeSeries();
		}
		for (int i = 0; i < setsize; i++) {
			// TODO replace getdistanceDetails with a return object.
			double pladist = mfdr.getDistanceDetails(ts[i][0], ts[i][1], d)
					.pla();
			double dwtdist = mfdr.getDistanceDetails(ts[i][0], ts[i][1], d)
					.dwt();
			double oridist = d.calDistance(ts[i][0], ts[i][1], ts[i][0]);
			trainingset.add(new TrainingSet(pladist, dwtdist, oridist));
		}

		LRAngleLearning alearn = new LRAngleLearning(trainingset);
		double pladist = mfdr.getDistanceDetails(ts[0][0], ts[0][1], d).pla();
		double dwtdist = mfdr.getDistanceDetails(ts[0][0], ts[0][1], d).dwt();
		System.out.println("PLA:" + pladist + "  DWT:" + dwtdist);
		System.out.println("Angle:" + alearn.getAngle(pladist, dwtdist));
		double angle = alearn.getAngle(pladist, dwtdist);
		// Error Training
		TimeSeries[] gg = new TimeSeries[2];
		for (int i = 0; i < 2; i++) {
			gg = generateTimeSeries();
		}
		VarienceLearning vlearn = new VarienceLearning(trainingset, angle, 3);
		pladist = mfdr.getDistanceDetails(gg[0], gg[1], d).pla();
		dwtdist = mfdr.getDistanceDetails(gg[0], gg[1], d).dwt();
		System.out.println("PLA:" + pla.getDistance(gg[0], gg[1], d) + "  DWT:" + dtw.getDistance(gg[0], gg[1], d));
		mfdr.updateAngle(angle);
		double mfdrdistance = mfdr.getDistance(gg[0], gg[1], d);
		System.out.println("Origin:" + d.calDistance(gg[0], gg[1], gg[0]));
		System.out.println("MFDR:" + mfdrdistance);
		System.out.println("MFDR_Corrected:" + vlearn.getGuaranteedCompensation(mfdrdistance));
		System.out.println("Compensation:" + vlearn.getGuaranteedCompensation());
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
