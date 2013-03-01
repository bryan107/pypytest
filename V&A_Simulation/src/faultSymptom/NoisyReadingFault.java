package faultSymptom;

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
		if(Math.random() < 0.5){
			return value + value * Math.random() * attribute;
		}else{
			return value - value * Math.random() * attribute;
		}
	}
	
	public String getKey(){
		return "Noisy";
	}
}
