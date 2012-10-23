package smartgrid.component;

public class FaultNull implements Fault {

	@Override
	public double getValue(double value) {
		return value;
	}

}
