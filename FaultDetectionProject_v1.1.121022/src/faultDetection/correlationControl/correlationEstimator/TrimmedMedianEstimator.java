package faultDetection.correlationControl.correlationEstimator;

import faultDetection.tools.Calculator;

public class TrimmedMedianEstimator implements RegressionEstimator {

	@Override
	public double getEstimatedValue(double[] x, double[] y,
			double maxtolerableerror) {
		synchronized (Calculator.getInstance()) {
			return Calculator.getInstance().getMedian(getSlopes(x, y));
		}
	}

	private double[] getSlopes(double[] x, double[] y) {
		double[] slopes = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			slopes[i] = y[i] / x[i];
		}
		return slopes;
	}

	@Override
	public boolean isFreeFromOutliers(){
		return true;
	}

}
