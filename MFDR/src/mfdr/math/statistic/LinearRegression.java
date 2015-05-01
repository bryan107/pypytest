package mfdr.math.statistic;

public interface LinearRegression {
	public void updateData(double[] x, double[] y);
	public double[] getEstimates();
}
