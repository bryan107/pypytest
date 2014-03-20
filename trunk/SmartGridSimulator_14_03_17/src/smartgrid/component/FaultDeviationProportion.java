package smartgrid.component;

public class FaultDeviationProportion implements Fault {

	private double impactvalue;
	
	public FaultDeviationProportion(double impactvalue){
		updateImpactValue(impactvalue);
	}
	
	public void updateImpactValue(double impactvalue){
		this.impactvalue = impactvalue;
	}
	
	@Override
	public double getValue(double value) {
		return value * (1 + impactvalue);
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public String name() {
		return "ProportionalDeviation";
	}
}
