package smartgrid.component;

import fileAccessInterface.PropertyAgent;

class FaultDeviationProportion implements Fault {

	@Override
	public double getValue(double value) {
		PropertyAgent prop = new PropertyAgent("conf");
		double deviation = Double.valueOf(prop.getProperties("SET", "Fault.DeviationProportion"));
		return value * (1 + deviation);
	}
}
