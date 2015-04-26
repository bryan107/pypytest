package mdfr.math.statistic;

import flanagan.analysis.Regression;

public class LinearEstimator implements LinearRegression {
	private Regression reg;
	
	public LinearEstimator(double[] x, double[] y) {
		updateData(x, y);
	}
	
	@Override
	public void updateData(double[] x, double[] y) {
		reg = new Regression(x, y);
		reg.linear();
	}

	@Override
	public double[] getEstimates() {
		return reg.getBestEstimates();
	}


}
