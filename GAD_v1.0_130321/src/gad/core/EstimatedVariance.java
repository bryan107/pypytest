package gad.core;

public class EstimatedVariance {
	private double[][] direction;
	private double[] deviation;
	
	public EstimatedVariance(double[][] direction, double[] deviation){
		this.direction = direction;
		this.deviation = deviation;
	}
	
	public double[][] Direction(){
		return direction;
	}
	
	public double[] Deviation(){
		return deviation;
	}
	
	
}
