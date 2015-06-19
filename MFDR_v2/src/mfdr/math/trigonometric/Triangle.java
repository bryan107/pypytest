package mfdr.math.trigonometric;

import mfdr.dimensionality.datastructure.DFTWaveData;

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
	
	public double[] getHilb(DFTWaveData wave){
		double cos = wave.amplitude() * Math.cos(wave.phaseDelay());
		double sin = wave.amplitude() * Math.sin(wave.phaseDelay());
		double[] hilb = {cos, sin};
		return hilb;
	}
	
}
