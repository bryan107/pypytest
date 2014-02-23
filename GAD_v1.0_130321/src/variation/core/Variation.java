package variation.core;

import java.util.Map;


public class Variation {

private double threshold;
	
	public Variation(double threshold){
		this.threshold = threshold;
	}
	/*
	public Map<Integer, Short> markReading(Map<Integer, Double> reading){
		//CorrelationEstimation ce = new CorrelationEstimation(threshold);
		//DFDEngine dfd = new DFDEngine();
		
		return dfd.markCondition(ce.as sessCorrelation(reading));
	}
	*/
}
