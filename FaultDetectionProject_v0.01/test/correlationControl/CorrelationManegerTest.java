package correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultDetection.correlationControl.CorrelationManager;
import junit.framework.TestCase;

public class CorrelationManegerTest extends TestCase {
	CorrelationManager cm1 = new CorrelationManager(3,2,0.5);
	Map<Integer, Boolean> condition = new HashMap<Integer, Boolean>();
	private void testInput(){
//		cm1.putReading(1, 1);
//		cm1.putReading(2, 4);
//		cm1.putReading(3, 16);
//		condition.put(1, false);
//		condition.put(2, true);
//		condition.put(3, true);
//		cm1.updateCorrelations(condition);
//		cm1.putReading(1, 1);
//		cm1.putReading(2, 4);
//		cm1.putReading(3, 16);
//		condition.put(1, false);
//		condition.put(2, true);
//		condition.put(3, true);
//		cm1.updateCorrelations(condition);
//		cm1.putReading(1, 1);
//		cm1.putReading(2, 4);
//		cm1.putReading(3, 16);
//		condition.put(1, false);
//		condition.put(2, true);
//		condition.put(3, true);
//		cm1.updateCorrelations(condition);
//		cm1.putReading(1, 1);
//		cm1.putReading(2, 4);
//		cm1.putReading(3, 16);
//		condition.put(1, true);
//		condition.put(2, true);
//		condition.put(3, true);
//		cm1.updateCorrelations(condition);
//		cm1.putReading(1, 1);
//		cm1.putReading(2, 4);
//		cm1.putReading(3, 16);
//		condition.put(1, true);
//		condition.put(2, true);
//		condition.put(3, true);
//		cm1.updateCorrelations(condition);
//		cm1.putReading(1, 1);
//		cm1.putReading(2, 4);
//		cm1.putReading(3, 16);
//		condition.put(1, true);
//		condition.put(2, true);
//		condition.put(3, true);
//		cm1.updateCorrelations(condition);
//		cm1.putReading(1, 1);
//		cm1.putReading(2, 4);
//		cm1.putReading(3, 16);
//		condition.put(1, true);
//		condition.put(2, true);
//		condition.put(3, true);
//		cm1.updateCorrelations(condition);
	}
	public void testGetCorrelationTable(){
		testInput();
		Map<Integer, Map<Integer, Double>> correlationtable = cm1.getCorrelationTable();
		Set<Integer> key1 = correlationtable.keySet();
		Iterator<Integer> it1 = key1.iterator();
		for(Map<Integer, Double> i : correlationtable.values()){
			int node1 = it1.next();
			Set<Integer> key2 = i.keySet();
			Iterator<Integer> it2 = key2.iterator();
			for(double j : i.values()){
				System.out.println("Node" + node1 + " & Node" + it2.next() + " : " + j);
			}
		}
		System.out.println("TEST: " + Math.pow(4, (double)1/2));
		
		Map<Integer, Map<Integer, Double>> correlationtrendtable = cm1.getCorrelationTrendTable();
		Set<Integer> key3 = correlationtrendtable.keySet();
		Iterator<Integer> it3 = key3.iterator();
		for(Map<Integer, Double> i : correlationtrendtable.values()){
			int node1 = it3.next();
			Set<Integer> key2 = i.keySet();
			Iterator<Integer> it2 = key2.iterator();
			for(double j : i.values()){
				System.out.println("Trend: Node" + node1 + " & Node" + it2.next() + " : " + j);
			}
		}
		System.out.println("Finish");
	}
	public void testGetDeviceCondition(){
		testInput();
		Map<Integer, Boolean> devicecondition = cm1.getDeviceCondition();
		Set<Integer> key = devicecondition.keySet();
		Iterator<Integer> it = key.iterator();
		System.out.println("===Device Conditions:");
		for(boolean i : devicecondition.values()){
			int nodeid = it.next();
			System.out.println("Node[" + nodeid + "] = " + i);
		}
	}
}
