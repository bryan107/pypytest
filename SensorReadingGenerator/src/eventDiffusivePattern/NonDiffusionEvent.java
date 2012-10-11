package eventDiffusivePattern;

public class NonDiffusionEvent implements EventType {

	@Override
	public double getValue(double eventvalue, double diffusion,
			double constant, double distance) {
		return eventvalue;
	}
	public String toString(){
		return "Non-Diffusive_Event";
	}
}
