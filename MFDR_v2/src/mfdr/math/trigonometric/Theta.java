package mfdr.math.trigonometric;

public class Theta {

	private final double freq, phasedelay;
	private final int series_length, window_num, windowsize;
	
	public Theta(double freq, double phasedelay, int series_length, int window_num, int windowsize){
		this.freq = freq;
		this.phasedelay = phasedelay;
		this.series_length = series_length;
		this.window_num = window_num;
		this.windowsize = windowsize;
	}
	
	public double getAngle(double x){
		return g()*x + k();
	}
	
	public double g(){
		return 2*Math.PI*freq/series_length;
	}
	
	public double k(){
		return phasedelay + g()*windowsize*window_num - g()*windowsize;
	}
	
}
