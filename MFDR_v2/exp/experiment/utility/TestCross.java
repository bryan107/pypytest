package experiment.utility;

import mfdr.dimensionality.datastructure.DFTWaveData;
import mfdr.math.Sum;
import junit.framework.TestCase;

public class TestCross extends TestCase {

	private int NoC_t = 2;

	public void test() {
		double a3 = 2, b3 = 1;
		DFTWaveData w1 = new DFTWaveData(10, 0, 2);
		DFTWaveData w2 = new DFTWaveData(10, 0, 1);
		int tslength = 8;
		double c1 = w1.amplitude();
		double g1 = w1.g(tslength);
		double k1 = w1.k(tslength, 1, tslength / NoC_t);
		double c2 = w2.amplitude();
		double g2 = w2.g(tslength);
		double k2 = w2.k(tslength, 1, tslength / NoC_t);
		
		double cross1 = getCrossDistance(a3, b3, c1 , tslength, g1, k1);
		double cross2 = getCrossDistance(a3, b3, c2 , tslength, g2, k2);
		double cell = getCellDistance(1, a3, b3, w1, w2, tslength);
		
		System.out.println("Cross1:" + cross1);
		System.out.println("Cross:" + (cross1-cross2));
		System.out.println("Cell:" + cell);
		System.out.println();
		
		double tt = w1.amplitude()*Sum.getInstance().xCos(g1, k1, 7);
		double gg = 0;
		double gg2 = 0;
		for(int x = 0 ; x < 8 ; x++){
			gg += x*w1.amplitude()*Math.cos(g1*x+k1);
			gg2 += x*w1.getWaveValue(x, tslength, 1, 8);
		}
		System.out.println("TT:" + tt + "   GG:" + gg + "  GG2:" + gg2);
		
		tt = w1.amplitude()*Sum.getInstance().cos(g1, k1, 7);
		gg = 0;
		gg2 = 0;
		for(int x = 0 ; x < 8 ; x++){
			gg += w1.amplitude()*Math.cos(g1*x+k1);
			gg2 += w1.getWaveValue(x, tslength, 1, 8);
		}
		System.out.println("TT:" + tt + "   GG:" + gg + "  GG2:" + gg2);
		
	}

	private double getCrossDistance(double a3, double b3, double c1,
			int tslength, double g, double k) {
		int windowsize = tslength / NoC_t;
		double sum1 = a3 * c1
				* Sum.getInstance().xCos(g, k, windowsize-1)
				+ (a3 + b3) * c1
				* Sum.getInstance().cos(g, k, windowsize-1);
		return sum1;
	}

	private double getCellDistance(int j, double a3, double b3,
			DFTWaveData w1, DFTWaveData w2, int tslength) {
		int windowsize = tslength / NoC_t;
		double sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
		for (int x = 0; x <= windowsize; x++) {
			sum1 += a3 * (x + 1) * w1.getWaveValue(x, tslength, j, windowsize);
			sum2 += b3 * w1.getWaveValue(x, tslength, j, windowsize);
			sum3 += a3 * (x + 1) * w2.getWaveValue(x, tslength, j, windowsize);
			sum4 += b3 * w2.getWaveValue(x, tslength, j, windowsize );
		}
		return sum1 + sum2 - sum3 - sum4;
	}

}
