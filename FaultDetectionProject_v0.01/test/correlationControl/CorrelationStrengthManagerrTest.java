package correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultDetection.correlationControl.CorrelationStrengthManager;
import faultDetection.tools.Calculator;
import junit.framework.TestCase;

public class CorrelationStrengthManagerrTest extends TestCase {

	//faulty conditions
	private final short FT = 0;
	private final short LF = 1;
	private final short LG = 2;
	private final short GD = 3;
	
	public void testGetCorrelationStrengthTable() {

		Map<Integer, Map<Integer, Double>> correlationtable = new HashMap<Integer, Map<Integer, Double>>();
		Map<Integer, Map<Integer, Double>> correlationtrendtable = new HashMap<Integer, Map<Integer, Double>>();
		setupTables(correlationtable, correlationtrendtable);
		
		System.out.println("Cal: " + Calculator.getInstance().correlationStrength(1.8, 1.0, 0.8));
		System.out.println("Defined Error Tolerance: " + CorrelationStrengthManager.getInstance().getErrorTolerance());
		Map<Integer, Map<Integer, Double>> correlationstrengthtable = CorrelationStrengthManager
				.getInstance().getCorrelationStrengthTable(correlationtable,
						correlationtrendtable);
		System.out.println("Correlation Strength Table:");
		for(Map<Integer, Double> entry : correlationstrengthtable.values()){
			for(double value: entry.values()){
				System.out.print(value + " ");
			}
			System.out.println();
		}
		System.out.println("Finish");
	}
	
	public void testGetReadingTrustworthiness(){
		Map<Integer, Map<Integer, Double>> correlationtable = new HashMap<Integer, Map<Integer, Double>>();
		Map<Integer, Map<Integer, Double>> correlationtrendtable = new HashMap<Integer, Map<Integer, Double>>();
		Map<Integer, Short> readingfaultcondition = new HashMap<Integer, Short>();
		setupTables(correlationtable, correlationtrendtable);
		setupFaultyCondition(readingfaultcondition);
		
		Map<Integer, Map<Integer, Double>> correlationstrengthtable = CorrelationStrengthManager
				.getInstance().getCorrelationStrengthTable(correlationtable,
						correlationtrendtable);
		Map<Integer, Double> trustworthiness = CorrelationStrengthManager.getInstance().getReadingTrustworthiness(correlationstrengthtable, readingfaultcondition);
		
		Set<Integer> key = trustworthiness.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int nodeid = iterator.next();
			System.out.println("Node[" + nodeid + "] : " + trustworthiness.get(nodeid));
		}
	}

	private void setupFaultyCondition(Map<Integer, Short> readingfaultcondition) {
		readingfaultcondition.put(1, GD);
		readingfaultcondition.put(2, GD);
		readingfaultcondition.put(3, GD);
		readingfaultcondition.put(4, GD);
	}
	
	private void setupTables(
			Map<Integer, Map<Integer, Double>> correlationtable,
			Map<Integer, Map<Integer, Double>> correlationtrendtable) {
		Map<Integer, Double> crtetry1 = new HashMap<Integer, Double>();
		Map<Integer, Double> crtetry2 = new HashMap<Integer, Double>();
		Map<Integer, Double> crtetry3 = new HashMap<Integer, Double>();
		Map<Integer, Double> crtetry4 = new HashMap<Integer, Double>();
		crtetry1.put(2, 0.95); crtetry2.put(1, 1.05263158); crtetry3.put(1, 1.0); crtetry4.put(1, 1.0);
		crtetry1.put(3, 1.0); crtetry2.put(3, 1.0); crtetry3.put(2, 1.0); crtetry4.put(2, 1.0);
		crtetry1.put(4, 1.0); crtetry2.put(4, 1.0); crtetry3.put(4, 1.0); crtetry4.put(3, 1.0);
		
		Map<Integer, Double> crttetry1 = new HashMap<Integer, Double>();
		Map<Integer, Double> crttetry2 = new HashMap<Integer, Double>();
		Map<Integer, Double> crttetry3 = new HashMap<Integer, Double>();
		Map<Integer, Double> crttetry4 = new HashMap<Integer, Double>();
				
		crttetry1.put(2, 1.0); crttetry2.put(1, 1.0); crttetry3.put(1, 1.0); crttetry4.put(1, 1.0);
		crttetry1.put(3, 1.0); crttetry2.put(3, 1.0); crttetry3.put(2, 1.0); crttetry4.put(2, 1.0);
		crttetry1.put(4, 1.0); crttetry2.put(4, 1.0); crttetry3.put(4, 1.0); crttetry4.put(3, 1.0);
		
		correlationtable.put(1, crtetry1);
		correlationtable.put(2, crtetry2);
		correlationtable.put(3, crtetry3);
		correlationtable.put(4, crtetry4);
		correlationtrendtable.put(1, crttetry1);
		correlationtrendtable.put(2, crttetry2);
		correlationtrendtable.put(3, crttetry3);
		correlationtrendtable.put(4, crttetry4);
	}
}
