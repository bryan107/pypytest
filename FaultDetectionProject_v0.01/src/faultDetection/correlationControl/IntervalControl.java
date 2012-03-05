package faultDetection.correlationControl;

public interface IntervalControl {
	public double aggregateReadings();
	public void slotControl();
}
