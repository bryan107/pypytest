package faultSymptom;

public class ProportionalDeviationFault implements FaultSymptom {
	
	private double deviation;
	
	public ProportionalDeviationFault(double deviation){
		updateFaultAttribute(deviation);
	}
	
	@Override
	public void updateFaultAttribute(double deviation){
		this.deviation = deviation;
	}
	
	public double getValue(double value) {
		return value * (1 + deviation);
	}

	public String getKey(){
		return "Proportional_Deviation";
	}
}
