package faultDetection.correlationControl;

public class MarkedReading {
	public double reading;
	public double trustworthiness;
	public boolean devicecondition;
	
	public MarkedReading(double reading, double trustworthiness, boolean devicecondition){
		this.reading = reading;
		this.trustworthiness = trustworthiness;
		this.devicecondition = devicecondition;
	}
}
