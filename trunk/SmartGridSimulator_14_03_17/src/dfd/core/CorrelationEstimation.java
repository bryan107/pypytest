package dfd.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CorrelationEstimation {
	
	private double threshold;
	
	public CorrelationEstimation(double threshold){
		this.threshold = threshold;
		
	}
	
	public Map<Integer, Map<Integer, Boolean>> assessCorrelation(Map<Integer, Double> reading){
		Map<Integer, Map<Integer, Boolean>> correlationtable = new HashMap<Integer, Map<Integer,Boolean>>();
		
		Iterator<Integer> it = reading.keySet().iterator();
		while(it.hasNext()){
			int nodeidi = it.next();
			Map<Integer,Boolean> temp = new HashMap<Integer, Boolean>();
			Iterator<Integer> it2 = reading.keySet().iterator();
			while(it2.hasNext()){
				int nodeidj = it2.next();
				if(nodeidi == nodeidj){
					continue;
				}
				if(Math.abs(reading.get(nodeidi)-reading.get(nodeidj)) > threshold){
					temp.put(nodeidj, false);
				} else{
					temp.put(nodeidj, true);
				}
			}
			correlationtable.put(nodeidi, temp);
		}
		return correlationtable;
	}
	
}
