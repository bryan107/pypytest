package faultDetection.tools;

public class LeastSquareEstimator implements RegressionEstimator {
	// Standard Linear Regression with Least Square Estimator
	@Override
	public double getEstimatedValue(double[] x, double[] y, double maxtolerableerror) {
		synchronized(Calculator.getInstance()){
			return Calculator.getInstance().getRegressionSlope(x,y);
		}
		
	}
	@Override
	public boolean isFreeFromOutliers(){
		return false;
	}
}
