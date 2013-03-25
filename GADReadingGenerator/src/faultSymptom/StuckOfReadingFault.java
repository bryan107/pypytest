package faultSymptom;

public class StuckOfReadingFault implements FaultSymptom {
	private double previousreading;
	private boolean flag = false;
	@Override
	public double getValue(double value, double attribute) {
		if(flag){
			return previousreading;
		}
		flag = true;
		previousreading = value;
		return previousreading;
	}

	public String getKey(){
		return "Stuck";
	}
}
