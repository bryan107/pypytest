package eventDiffusivePattern;

public class LogEvent implements EventType {

	@Override
	public double getValue(double eventvalue, double diffusion, double constant, double distance) {
		return eventvalue/Math.log(constant*distance + Math.E);
	}
	public String toString(){
		return "Log_Event";
	}
}
