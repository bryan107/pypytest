package faultDetection.tools;

public interface RegressionEstimator {
	double getEstimatedValue(double[] x, double[] y, double maxtolerableerror);
}
