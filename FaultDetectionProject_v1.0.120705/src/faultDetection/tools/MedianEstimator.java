package faultDetection.tools;

public class MedianEstimator implements RegressionEstimator {

	//Simple Median Estimator
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

}
