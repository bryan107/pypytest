package mfdr.distance;

public class L1Distance extends Distance {

	@Override
	public double calDistance(double[] xx, double[] yy) {
		double sum = 0;
		for(int i = 0 ; i < xx.length ; i++){
			// Accumulate the sum of the power of 2 of their distances
			sum = xx[i] - yy[i];
		}
		// Return the square of the sum
		return sum;
	}

	@Override
	public double calWhiteNoiseDistance(double e1, double e2, int length) {
		// TODO Auto-generated method stub
		return 0;
	}

}
