package mfdr.math.trigonometric;

import mfdr.utility.DataListOperator;

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
	
	public double getEnergy(double cos, double sin){
		return Math.sqrt(Math.pow(cos, 2)+Math.pow(sin, 2));
	}
	
}
