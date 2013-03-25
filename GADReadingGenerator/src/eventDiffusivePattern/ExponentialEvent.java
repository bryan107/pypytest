package eventDiffusivePattern;

public class ExponentialEvent implements EventType {

	@Override
	public double getValue(double eventvalue, double diffusion,
			double constant, double distance) {
		return eventvalue * Math.pow(Math.E, -distance);
	}
	public String toString(){
		return "Exponential_Event";
	}
}
