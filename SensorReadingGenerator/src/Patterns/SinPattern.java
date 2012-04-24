package Patterns;

public class SinPattern implements Pattern {

	@Override
	public double getCorrespondReading(double variation, double attribute, long sections, long sectionnumber) {		
		return  variation * Math.sin((double)(2 * Math.PI)*sectionnumber/sections);
	}

}
