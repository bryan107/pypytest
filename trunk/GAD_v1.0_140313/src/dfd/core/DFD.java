package dfd.core;

import java.util.Map;

public class DFD {

	private double threshold;
	
	public DFD(double threshold){
		this.threshold = threshold;
	}
	
	public Map<Integer, Short> markReading(Map<Integer, Double> reading){
		CorrelationEstimation ce = new CorrelationEstimation(threshold);
		DFDEngine dfd = new DFDEngine();
		return dfd.markCondition(ce.assessCorrelation(reading));
	}
	
}
