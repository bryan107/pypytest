package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Map;

public final class CorrelationStrengthManager {
	private static CorrelationStrengthManager self = new CorrelationStrengthManager(0.8); 
	//------------------Private Variables---------------------
	private Map<Integer, Map<Integer, Double>> correlationstrengthtable = new HashMap<Integer, Map<Integer,Double>>();
	private double errortolerance;
	//--------------------Constructor-------------------------
	public CorrelationStrengthManager(double errortolerance){
		updateErrorTolerance(errortolerance);
	}
	
	public void updateErrorTolerance(double errortolerance){
		this.errortolerance = errortolerance;
	}
	public void updateCorrelationStrengthTable(Map<Integer, Map<Integer, Double>> correlationtable, Map<Integer, Map<Integer, Double>> correlationtrendtable){
		
	}
	public Map<Integer, Map<Integer, Double>> getCorrelationStrength(){
		
		return correlationstrengthtable;
	}
	public double getCorrelationStrength(int node1 , int node2){
		
		return correlationstrengthtable.get(node1).get(node2);
	}
}
