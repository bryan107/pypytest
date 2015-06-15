package mfdr.math.trigonometric;

import mfdr.dimensionality.datastructure.Wave;

public class Triangle {

	private static Triangle self = new Triangle();
	
	private Triangle(){

	}
	
	public static Triangle getInstance() {
		return self;
	}
	
	public double getPhaseDelay(double cos, double sin){
			return Math.atan2(sin, cos);
	}
	
	public double getAmplitude(double cos, double sin){
		return Math.sqrt(Math.pow(cos, 2)+Math.pow(sin, 2));
	}
	
	public double[] getHilb(Wave wave){
		double cos = wave.energy() * Math.cos(wave.phaseDelay());
		double sin = wave.energy() * Math.sin(wave.phaseDelay());
		double[] hilb = {cos, sin};
		return hilb;
	}
	
}
