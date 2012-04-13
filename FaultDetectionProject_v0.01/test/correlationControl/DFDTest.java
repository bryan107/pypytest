package correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultDetection.correlationControl.DFDEngine;
import junit.framework.TestCase;

public class DFDTest extends TestCase {
	public void testgetdata(){
		Map<Integer, Map<Integer, Double>> correlationstrengthtable= new HashMap<Integer, Map<Integer, Double>>();
		Map<Integer, Double> temp1 = new HashMap<Integer, Double>();
		Map<Integer, Double> temp2 = new HashMap<Integer, Double>();
		Map<Integer, Double> temp3 = new HashMap<Integer, Double>();
		Map<Integer, Double> temp4 = new HashMap<Integer, Double>();
		Map<Integer, Boolean> devicecondition = new HashMap<Integer, Boolean>();
		
		//Where node 2 is a faulty node
		temp1.put(2, 0.6);
		temp1.put(3, 0.9);
		temp1.put(4, 0.85);
		temp2.put(1, 0.6);
		temp2.put(3, 0.4);
		temp2.put(4, 0.2);
		temp3.put(1, 0.9);
		temp3.put(2, 0.4);
		temp3.put(4, 0.95);
		temp4.put(1, 0.85);
		temp4.put(2, 0.2);
		temp4.put(3, 0.95);
		
		correlationstrengthtable.put(1, temp1);
		correlationstrengthtable.put(2, temp2);
		correlationstrengthtable.put(3, temp3);
		correlationstrengthtable.put(4, temp4);
		
		for(int i = 1 ; i < 5 ; i++){
			if(i == 3){
				devicecondition.put(i, false);
				continue;
			}
			devicecondition.put(i, true);
		}
		
//		System.out.println("temp size:" + temp.size());
//		System.out.println("pmet size:" + correlationstrengthtable.get(i).size());
//		temp.clear();

		System.out.println("correlation size: " + correlationstrengthtable.size());
		System.out.println("sub 1 size : " + correlationstrengthtable.get(1).size());
		System.out.println("sub 2 size : " + correlationstrengthtable.get(2).size());
		System.out.println("sub 3 size : " + correlationstrengthtable.get(3).size());
		
//		DFDEngine.getInstance().updateLeastLGNumber(0.4);
//		DFDEngine.getInstance().updateThreshold(0.8);
		Map<Integer, Short> faultlcondition = DFDEngine.getInstance().faultConditionalMarking(correlationstrengthtable, devicecondition);
		Set<Integer> key = faultlcondition.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(int i : faultlcondition.values()){
			System.out.println("node " + iterator.next() + ": " + i);
		}
	}
}
