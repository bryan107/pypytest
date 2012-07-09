package faultDetection.tools;

public class QuantilesEEstimator implements RegressionEstimator {

	// Quantile regression + Expected value
	@Override
	public double getEstimatedValue(double[] x, double[] y, double maxtolerableerror) {
		synchronized (Calculator.getInstance()) {
			return Calculator.getInstance().getaverage(
					Calculator.getInstance().getQuantileArray(x, y,	maxtolerableerror));
		}
	}

}
