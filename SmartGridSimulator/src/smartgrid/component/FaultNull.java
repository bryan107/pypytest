package smartgrid.component;

public class FaultNull implements Fault {

	
	@Override
	public double getValue(double value) {
		return value;
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public String name() {
		return "Null";
	}

}
