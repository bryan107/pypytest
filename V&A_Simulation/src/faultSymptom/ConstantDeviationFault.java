package faultSymptom;

public class ConstantDeviationFault implements FaultSymptom {
	private double deviation;
	
	public ConstantDeviationFault(double deviation){
		updateFaultAttribute(deviation);
	}
	
	@Override
	public void updateFaultAttribute(double deviation){
		this.deviation = deviation;
		
	}
	
	public double getValue(double value) {
		return (value + deviation);
	}

	public String getKey(){
		return "Constant_Deviation";
	}
}
