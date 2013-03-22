package gad.core;

public class EstimatedVariance {
	private double[][] direction;
	private double[] deviation;
	private double[] referencereading;
	public EstimatedVariance(double[][] direction, double[] deviation, double[] referencereading){
		this.direction = direction;
		this.deviation = deviation;
		this.referencereading = referencereading;
	}
	
	public double[][] direction(){
		return direction;
	}
	
	public double[] deviation(){
		return deviation;
	}
	
	public double[] previousReading(){
		return referencereading;
	}
	
	
}
