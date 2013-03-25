package gad.core;

public class EstimatedVariance {
	private double[][] direction;
	private double[] deviation;
	private double[] referencereading;
	private boolean estimationready;
	public EstimatedVariance(double[][] direction, double[] deviation, double[] referencereading, boolean estimationready){
		this.direction = direction;
		this.deviation = deviation;
		this.referencereading = referencereading;
		this.estimationready = estimationready;
	}
	
	public boolean isValid(){
		return estimationready;
	}
	
	public double[][] direction(){
		return direction;
	}
	
	public double[] deviation(){
		return deviation;
	}
	
	public double[] estimatedReading(){
		return referencereading;
	}
	
	
}
