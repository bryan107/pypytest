package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Map;

public class CorrelationStrengthManager {
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
	public void updateCorrelationStrengthTable(Map<Integer, Map<Integer, Double>> correlationtable, Map<Integer, Double> readings){
	//TODO Need Construct
	}
	public Map<Integer, Map<Integer, Double>> getCorrelationStrength(){
		return correlationstrengthtable;
	}
	public double getCorrelationStrength(int node1 , int node2){	
		return correlationstrengthtable.get(node1).get(node2);
	}
}
