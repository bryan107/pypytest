package mfdr.math.statistic;

public class StatisticalBoundsWhiteNoise implements StatisticalBounds {
	
	private double k_value, n_value;

	public StatisticalBoundsWhiteNoise(double k_value, double n_value) {
		updatePercentiles(k_value);
		updateNumber(n_value);
	}

	// K_value is determined by the Percentiles
	public void updatePercentiles(double k_value) {
		this.k_value = k_value;
	}
	
	// N_value is determined by the size of time series
	public void updateNumber(double N) {
		this.n_value = N;
	}

	@Override
	public double upperbound(double x) {
		double y = -x + (k_value) * Math.pow((2 / n_value), 0.5) * Math.exp(x / 2);
		return y;
	}

	@Override
	public double lowerbound(double x) {
		double y = -x - (k_value) * Math.pow((2 / n_value), 0.5) * Math.exp(x / 2);
		return y;
	}

}
