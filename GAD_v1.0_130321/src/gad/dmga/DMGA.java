package gad.dmga;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import flanagan.analysis.Stat;

public class DMGA {

	public DMGA(){
		
	}
	
	public void cluster(int nmin, Map<Integer, LinkedList<Double>> reading){
		Map<Integer, Map<Integer, Double>> correlationtable = new HashMap<Integer, Map<Integer, Double>>();
		Map<Integer, LinkedList<Integer>> firstcell = new HashMap<Integer, LinkedList<Integer>>();
		Iterator<Integer> it = reading.keySet().iterator();
		geneFirstCell(firstcell, it);
		getCorrTable(reading, correlationtable);
		
		
		
		
		
		
		double currmax = 0;
		
		
		
	}

	private void geneFirstCell(Map<Integer, LinkedList<Integer>> firstcell,
			Iterator<Integer> it) {
		int count = 0;
		while(it.hasNext()){
			LinkedList<Integer> temp = new LinkedList<Integer>();
			temp.add(it.next());
			firstcell.put(count,temp);
		}
	}

	private void getCorrTable(Map<Integer, LinkedList<Double>> reading,
			Map<Integer, Map<Integer, Double>> correlationtable) {
		Iterator<Integer> it1 = reading.keySet().iterator();
		while(it1.hasNext()){
			Map<Integer, Double> temp = new HashMap<Integer, Double>();
			int nodeid_i = it1.next();
			Iterator<Integer> it2 = reading.keySet().iterator();
			while(it2.hasNext()){
				int nodeid_j = it2.next();
				if(nodeid_i == nodeid_j){
					continue;
				}
				double[] r1 = listToArray(reading.get(nodeid_i));
				double[] r2 = listToArray(reading.get(nodeid_j));
				temp.put(nodeid_j, Stat.corrCoeff(r1, r2));
			}
			correlationtable.put(nodeid_i, temp);
		}
	}
	
	private double[] listToArray(LinkedList<Double> data){
		double[] array = new double[data.size()];
		for(int i = 0 , size = data.size(); i < size ; i++){
			array[i] = data.get(i);
		}
		return array;
	}
	
	
	
}
