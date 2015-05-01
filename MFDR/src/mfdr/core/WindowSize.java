package mfdr.core;

public class WindowSize {

	final private double windowsize_noise, windowsize_trend;
	
	public WindowSize(double windowsize_noise, double windowsize_trend) {
		this.windowsize_noise = windowsize_noise;
		this.windowsize_trend = windowsize_trend;
	}
	
	public double noise(){
		return windowsize_noise;
	}
	
	public double trend(){
		return windowsize_trend;
	}
	
}
