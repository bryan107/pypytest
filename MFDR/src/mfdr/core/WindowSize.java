package mfdr.core;

public class WindowSize {

	final private double noisewindowsize, trendwindowsize;
	
	public WindowSize(double noisewindowsize, double trendwindowsize) {
		this.noisewindowsize = noisewindowsize;
		this.trendwindowsize = trendwindowsize;
	}
	
	public double noise(){
		return noisewindowsize;
	}
	
	public double trend(){
		return trendwindowsize;
	}
	
}
