package faultSymptom;

public class DeviationReadingFault implements FaultSymptom {

	@Override
	public double getValue(double value, double deviation) {
		return value * (1 + deviation);
	}

	public String getKey(){
		return "Deviation";
	}
}
