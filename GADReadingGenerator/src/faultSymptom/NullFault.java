package faultSymptom;

public class NullFault implements FaultSymptom {

	@Override
	public double getValue(double value, double attribute) {
		return value;
	}

	public String getKey(){
		return "Null";
	}
}
