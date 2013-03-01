package faultSymptom;

public class StuckOfReadingFault implements FaultSymptom {
	private double previousreading;
	private boolean flag = false;
	
	public StuckOfReadingFault(){
	}
	
	@Override
	public void updateFaultAttribute(double attribute){

	}
	
	public double getValue(double value) {
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
