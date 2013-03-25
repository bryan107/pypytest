package eventDiffusivePattern;

public interface EventType {
	public double getValue(double eventvalue, double diffusion, double constant, double distance);
	public String toString();
}
