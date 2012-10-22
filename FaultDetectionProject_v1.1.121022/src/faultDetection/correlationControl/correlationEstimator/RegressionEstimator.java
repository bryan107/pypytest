package faultDetection.correlationControl.correlationEstimator;

public interface RegressionEstimator {
	double getEstimatedValue(double[] x, double[] y, double maxtolerableerror);
	boolean isFreeFromOutliers();
}
