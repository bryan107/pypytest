package experiment.test;

import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTWaveData;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.dimensionality.reduction.MFDRLCM;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.math.Sum;
import junit.framework.TestCase;

public class TestCross extends TestCase {

	private int NoC_t = 2;
	Distance dist = new EuclideanDistance();

	public void test() {
		DFTWaveData w1 = new DFTWaveData(10, 0, 2);
		DFTWaveData w2 = new DFTWaveData(10, 0, 1);
		double a3 = 2, b3 = 1;
		int tslength = 18;
		TimeSeries ref = new TimeSeries();
		TimeSeries ref2 = new TimeSeries();
		generateResidual(ref, 1, 1, 90);
		generateResidual(ref2, 1, 1, 90);
		// double g = 0.7854;
		// double k = 6.2832;
		// // double k = 0;
		// int windowsize = 8;

		//
		// double gere = a3 * Sum.getInstance().xCos(g, k, windowsize-1);
		// double gere2 = (a3 + b3) * Sum.getInstance().cos(g, k, windowsize-1);
		// double gete = 0;
		// double gete2 = 0;
		// for(int x = 0 ; x < 8 ; x++){
		// gete += a3*x*Math.cos(g*x+k);
		// gete2 += (a3 + b3)*Math.cos(g*x+k);
		// }
		//
		// System.out.println("GERE:" + gere);
		// System.out.println("GERE2:" + gere2);
		// System.out.println("GETE:" + gete);
		// System.out.println("GETE:" + gete2);

				
		MFDRLCM mfdr = new MFDRLCM(2,2);
		MFDRLCM mfdr2 = new MFDRLCM(3,1);

		MFDRWaveData mfdrdata1  = mfdr.getDR(ref);
		MFDRWaveData mfdrdata2 = mfdr2.getDR(ref2);
		
		double celldist = mfdr.getTotalCellDistance(mfdrdata1, mfdrdata2, ref.size());
		double crossdist = mfdr.getTotalCrossDistance(mfdrdata1, mfdrdata2,	ref.size());
		System.out.println("CELL_Cross:" + celldist + "     MFDR_Cross:"+ crossdist);
		double brute = mfdr.getDistanceBruteForce(mfdrdata1, mfdrdata2, ref,
				dist);
		double cell = mfdr.getCrossBruteForceDistance(mfdrdata1, mfdrdata2,
				ref, dist);
		double mdist = mfdr.getDistance(mfdrdata1, mfdrdata2, ref.size(), dist);
		System.out.println("BRUTE_TOTAL:" + brute + "      CELL_TOTAL:" + cell		+ "     MFDR_TOTAL:" + mdist);
		//

		// *************************
		// int j = 2;
		// double c1 = w1.amplitude();
		// double g1 = w1.g(tslength);
		// double k1 = w1.k(tslength, j, tslength / NoC_t);
		// double c2 = w2.amplitude();
		// double g2 = w2.g(tslength);
		// double k2 = w2.k(tslength, j, tslength / NoC_t);
		//
		// double cross1 = getCrossDistance(a3, b3, c1 , tslength, g1, k1);
		// double cross2 = getCrossDistance(a3, b3, c2 , tslength, g2, k2);
		// double cell = getCellDistance(j, a3, b3, w1, w2, tslength);
		//
		// System.out.println("Cross1:" + cross1);
		// System.out.println("Cross1:" + cross2);
		// System.out.println("Cross:" + (cross1-cross2));
		// System.out.println("Cell:" + cell);
		// System.out.println();
		//
		// double tt = a3*w1.amplitude()*Sum.getInstance().xCos(g1, k1, 7);
		// double gg = 0;
		// double gg2 = 0;
		// for(int x = 0 ; x < 8 ; x++){
		// gg += a3*x*w1.amplitude()*Math.cos(g1*x+k1);
		// gg2 += a3*x*w1.getWaveValue(x, tslength, 2, 8);
		// }
		// System.out.println("TT:" + tt + "   GG:" + gg + "  GG2:" + gg2);
		//
		// tt = (a3 + b3)*w1.amplitude()*Sum.getInstance().cos(g1, k1, 7);
		// gg = 0;
		// gg2 = 0;
		// for(int x = 0 ; x < 8 ; x++){
		// gg += (a3 + b3)*w1.amplitude()*Math.cos(g1*x+k1);
		// gg2 += (a3 + b3)*w1.getWaveValue(x, tslength, 2, 8);
		// }
		// System.out.println("TT:" + tt + "   GG:" + gg + "  GG2:" + gg2);

	}
	
	private void print(LinkedList<PLAData> list){
		for(int i =0 ; i < list.size() ; i++){
           System.out.print("["+ i + "]" + " T:" + list.get(i).time() + " A0:" + list.get(i).a0()+ " A1:" + list.get(i).a1());
		}
		System.out.println();
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
			// double value = trendvariation*noise;
//			double value = Math.sin(i * Math.PI / 12) + trendvariation* 1;
			double value = Math.sin(i * Math.PI / 12);
			// double value = trend * Math.sin(Math.pow(i, 3))+ noise;
			// double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 *
			// Math.cos(i*Math.PI / 6) + noise + trend;
			residual.add(new Data(i, value));
		}
	}

	private double getTotalCrossDistance(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, int tslength) {
		double ts1_total = 0;
		double ts2_total = 0;
		for (int j = 0; j < NoC_t; j++) {
			double a3 = mfdrdata1.trends().get(j).a1()
					- mfdrdata2.trends().get(j).a1();
			double b3 = mfdrdata1.trends().get(j).a0()
					- mfdrdata2.trends().get(j).a0();
			for (int i = 0; i < 1; i++) {
				double c1 = mfdrdata1.seasonal().get(i).amplitude();
				double c2 = mfdrdata2.seasonal().get(i).amplitude();
				int windowsize = tslength / NoC_t;
				ts1_total += getCrossDistance(a3, b3, c1, tslength, mfdrdata1
						.seasonal().get(i).g(tslength), mfdrdata1.seasonal()
						.get(i).k(tslength, j + 1, windowsize));
				ts2_total += getCrossDistance(a3, b3, c2, tslength, mfdrdata2
						.seasonal().get(i).g(tslength), mfdrdata2.seasonal()
						.get(i).k(tslength, j + 1, windowsize));
			}
		}
		return 2 * (ts1_total - ts2_total);
	}

	private double getCrossDistance(double a3, double b3, double c1,
			int tslength, double g, double k) {
		int windowsize = tslength / NoC_t;
		double sum1 = a3 * c1 * Sum.getInstance().xCos(g, k, windowsize - 1)
				+ a3 * c1 * Sum.getInstance().cos(g, k, windowsize - 1);
		double sum2 = b3 * c1 * Sum.getInstance().cos(g, k, windowsize - 1);
		double sum = a3 * c1 * Sum.getInstance().xCos(g, k, windowsize - 1)
				+ (a3 + b3) * c1 * Sum.getInstance().cos(g, k, windowsize - 1);
		return sum;
	}

	private double getTotalCellDistance(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, int tslength) {
		double total = 0;
		for (int j = 0; j < NoC_t; j++) {
			double a3 = mfdrdata1.trends().get(j).a1()
					- mfdrdata2.trends().get(j).a1();
			double b3 = mfdrdata1.trends().get(j).a0()
					- mfdrdata2.trends().get(j).a0();
			for (int i = 0; i < 1; i++) {
				total += getCellDistance(j + 1, a3, b3, mfdrdata1.seasonal()
						.get(i), mfdrdata2.seasonal().get(i), tslength);
			}
		}
		return 2 * total;
	}

	private double getCellDistance(int j, double a3, double b3, DFTWaveData w1,
			DFTWaveData w2, int tslength) {
		int windowsize = tslength / NoC_t;
		double sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
		for (int x = 0; x < windowsize; x++) {
			sum1 += a3 * (x + 1) * w1.getWaveValue(x, tslength, j, windowsize);
			sum2 += b3 * w1.getWaveValue(x, tslength, j, windowsize);
			sum3 += a3 * (x + 1) * w2.getWaveValue(x, tslength, j, windowsize);
			sum4 += b3 * w2.getWaveValue(x, tslength, j, windowsize);
		}
		return sum1 + sum2 - sum3 - sum4;
	}

}
