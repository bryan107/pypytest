package variation.core;

import java.util.Map;


public class Variation {

private double threshold;
	
	public Variation(double threshold){
		this.threshold = threshold;
	}
	
	public Map<Integer, Short> markReading(Map<Integer, Double> reading){
		VariationEngine ve = new VariationEngine(threshold);
		Map<Integer, Short> condition = ve.markCondition(reading);
		ve.updateSMDB(reading, condition);
		return condition;
	}
	
	public void resetSMDB(){
		SMDB.getInstance().resetSMDB();
	}
}
