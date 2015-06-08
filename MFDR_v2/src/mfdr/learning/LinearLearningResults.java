package mfdr.learning;

public class LinearLearningResults {

	private final double w_trend, w_seasonal, w_noise, constant;
	
	public LinearLearningResults(double w_trend, double w_seasonal, double w_noise, double constant){
		this.w_trend = w_trend;
		this.w_seasonal = w_seasonal;
		this.w_noise = w_noise;
		this.constant = constant;
	}
	
	public double trendWeight(){
		return this.w_trend;
	}
	
	public double seasonalWeight(){
		return this.w_seasonal;
	}

	public double noiseWeight(){
		return this.w_noise;
	}
	
	public double constant(){
		return constant;
	}
}
