package faultSymptom;

import flanagan.analysis.Stat;

public class NoisyReadingFault implements FaultSymptom {
	private double attribute;
	
	public NoisyReadingFault(double attribute){
		updateFaultAttribute(attribute);
	}
	
	@Override
	public void updateFaultAttribute(double attribute){
		this.attribute = attribute;
	}
	
	public double getValue(double value) {
		return Stat.gaussianInverseCDF(value, attribute, Math.random());
	}
	
	public String getKey(){
		return "Noisy";
	}
}
