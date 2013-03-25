package faultSymptom;

public class NoisyReadingFault implements FaultSymptom {

	@Override
	public double getValue(double value, double attribute) {
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
