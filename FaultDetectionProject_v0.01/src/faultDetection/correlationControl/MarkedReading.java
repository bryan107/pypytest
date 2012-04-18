package faultDetection.correlationControl;

public class MarkedReading {
	private double reading;
	private double trustworthiness;
	private short devicecondition;
	private final short FT = 0;
	private final short GD = 3;
	private final short UNKNOWN = 4;
	
	
	public MarkedReading(double reading, double trustworthiness, short devicecondition){
		this.reading = reading;
		this.trustworthiness = trustworthiness;
		this.devicecondition = devicecondition;
	}
	
	public double value(){
		return reading;
	}
	
	public double trustworthiness(){
		return trustworthiness;
	}
	public boolean isFaulty(){
		switch(devicecondition){
		case FT:
			return true;
		case GD:
		case UNKNOWN:
		default:
			return false;
		}
	}
	
}
