package experiment.core;

public class RepresentationErrorResult {

	private final double error, variance, time;

	
	public RepresentationErrorResult(double error, double variance, double time){
		this.error = error;
		this.variance = variance;
		this.time = time;
	}
	
	public double mean(){
		return this.error;
	}
	
	public double variance(){
		return this.variance;
	}
	
	public double time(){
		return this.time;
	}
	
}
