package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Map;
//TODO CorrelationStrengthManager is under constructing
public final class CorrelationStrengthManager {
	private static CorrelationStrengthManager self = new CorrelationStrengthManager(0.8); 
	//------------------Private Variables---------------------
	private Map<Integer, Map<Integer, Double>> correlationstrengthtable = new HashMap<Integer, Map<Integer,Double>>();
	private double errortolerance;
	//--------------------Constructor-------------------------
	public CorrelationStrengthManager(double errortolerance){
		updateErrorTolerance(errortolerance);
	}
	
	public static CorrelationStrengthManager getInstance(){
		return self;
	}
	
	public void updateErrorTolerance(double errortolerance){
		this.errortolerance = errortolerance;
	}
	public Map<Integer, Map<Integer, Double>> getCorrelationStrengthTable(Map<Integer, Map<Integer, Double>> correlationtable, Map<Integer, Map<Integer, Double>> correlationtrendtable){
		return correlationstrengthtable;
	}
	//TODO generate reading strength calculated after DFD
	public Map<Integer, Double> getReadingTrustworthiness(Map<Integer, Short> readingfaultcondition){
		Map<Integer, Double> readingtrustworthiness = new HashMap<Integer, Double>();
		//use correlation strength table & reading fault condition to generate correlation
		return readingtrustworthiness;
	}
	

	public Map<Integer, Map<Integer, Double>> getCorrelationStrength(){
		return correlationstrengthtable;
	}

	
	public double getCorrelationStrength(int node1 , int node2){
		
		return correlationstrengthtable.get(node1).get(node2);
	}
}
