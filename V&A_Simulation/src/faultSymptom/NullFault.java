package faultSymptom;

public class NullFault implements FaultSymptom {
	
	public NullFault(){
		
	}
	
	@Override
	public void updateFaultAttribute(double attribute){
	}
	
	
	public double getValue(double value) {
		return value;
	}

	public String getKey(){
		return "Null";
	}
}
