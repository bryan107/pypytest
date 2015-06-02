package mfdr.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

public class TestValueComparator extends TestCase {

	public void test(){
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(bvc);
		for(int i = 0 ; i < 10 ; i++){
			map.put(i, Math.random());
		}
		sorted_map.putAll(map);
		// Extract number of coefficient
		System.out.println("MAP:" + map);
		System.out.println("Sorted MAP:" + sorted_map);
	}
	
	
}
