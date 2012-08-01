package faultDetection.tools;

public class PairedReading {
	private double x;
	private double y;
	
	public PairedReading(double x, double y){
		putPair(x, y);
	}
	
	public void putPair(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double x(){
		return x;
	}
	
	public double y(){
		return y;
	}	
}
