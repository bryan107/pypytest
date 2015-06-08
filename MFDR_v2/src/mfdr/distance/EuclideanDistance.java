package mfdr.distance;

public class EuclideanDistance extends Distance {

	public EuclideanDistance() {

	}

	@Override
	public double calDistance(double[] xx, double[] yy) {
		double sum = 0;
		for (int i = 0; i < xx.length; i++) {
			// Accumulate the sum of the power of 2 of their distances
			sum += Math.pow(xx[i] - yy[i], 2);
		}
		// Return the square of the sum
		return Math.pow(sum, 0.5);
	}

	@Override
	public double calWhiteNoiseDistance(double e1, double e2, int length) {
		return Math.sqrt(length*e1+length*e2);
	}

	



}
