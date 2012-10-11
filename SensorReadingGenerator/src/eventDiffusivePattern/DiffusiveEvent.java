package eventDiffusivePattern;


public class DiffusiveEvent implements EventType {

	@Override
	public double getValue(double eventvalue, double diffusion, double constant, double distance) {
		return eventvalue/Math.pow((constant*distance + 1), diffusion);
	}
	public String toString(){
		return "Diffusive_Event";
	}
}
