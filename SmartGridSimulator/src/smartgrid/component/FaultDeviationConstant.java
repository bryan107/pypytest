package smartgrid.component;

import fileAccessInterface.PropertyAgent;

class FaultDeviationConstant implements Fault {

	@Override
	public double getValue(double value) {
		PropertyAgent prop = new PropertyAgent("conf");
		double deviation = Double.valueOf(prop.getProperties("SET", "Fault.DeviationConstant"));
		return value + deviation;
	}
}
