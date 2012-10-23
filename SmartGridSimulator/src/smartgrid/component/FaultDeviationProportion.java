package smartgrid.component;

import fileAccessInterface.PropertyAgent;

public class FaultDeviationProportion implements Fault {

	@Override
	public double getValue(double value) {
		PropertyAgent prop = new PropertyAgent("conf");
		double deviation = Double.valueOf(prop.getProperties("SET", "Fault.DeviationProportion"));
		return value * (1 + deviation);
	}

	@Override
	public boolean isNull() {
		return false;
	}
}
