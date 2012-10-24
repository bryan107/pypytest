package smartgrid.component;

import fileAccessInterface.PropertyAgent;

public class FaultDeviationConstant implements Fault {

	private double impactvalue;
	
	public FaultDeviationConstant(double impactvalue){
		updateImpactValue(impactvalue);
	}
	
	public void updateImpactValue(double impactvalue){
		this.impactvalue = impactvalue;
	}
	
	@Override
	public double getValue(double value) {
		return value + impactvalue;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public String name() {
		return "ConstantDeviation";
	}

}
