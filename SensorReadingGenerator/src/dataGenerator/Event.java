package dataGenerator;

public class Event {
	public int sourceid;
	public double value, diffusion, constant;
	public Event(int sourceid, double value, double diffusion, double constant){
		this.sourceid = sourceid;
		this.value = value;
		this.diffusion = diffusion;
		this.constant = constant;
	}
}
