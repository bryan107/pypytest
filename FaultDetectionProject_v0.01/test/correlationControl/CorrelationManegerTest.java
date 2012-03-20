package correlationControl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultDetection.correlationControl.CorrelationManager;
import junit.framework.TestCase;

public class CorrelationManegerTest extends TestCase {
	CorrelationManager cm1 = new CorrelationManager(3,2);
	public void testGetCorrelationTable(){
		cm1.putReading(1, 1);
		cm1.putReading(2, 4);
		cm1.putReading(3, 16);
		cm1.updateCorrelations();
		cm1.putReading(1, 1);
		cm1.putReading(2, 4);
		cm1.putReading(3, 16);
		cm1.updateCorrelations();
		cm1.putReading(1, 1);
		cm1.putReading(2, 4);
		cm1.putReading(3, 16);
		cm1.updateCorrelations();
		cm1.putReading(1, 16);
		cm1.putReading(2, 4);
		cm1.putReading(3, 1);
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
	}
}
