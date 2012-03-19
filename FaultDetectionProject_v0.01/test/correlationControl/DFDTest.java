package correlationControl;

import java.util.HashMap;
import java.util.Map;

import faultDetection.correlationControl.DFDEngine;
import junit.framework.TestCase;

public class DFDTest extends TestCase {
	public void testgetdata(){
		Map<Integer, Map<Integer, Double>> correlationstrengthtable= new HashMap<Integer, Map<Integer, Double>>();
		
		Map<Integer, Double> temp = new HashMap<Integer, Double>();
		for(int i = 1 ; i < 4 ; i++){
			
			for(int j = 1 ; j < 4 ; j++){
				if(i == j){
					
				}
				else
				temp.put(j, 0.9);
			}
			//TODO Test.....
//			correlationstrengthtable.put(i, );
			System.out.println("temp size:" + temp.size());
			System.out.println("pmet size:" + correlationstrengthtable.get(i).size());
//			temp.clear();
		}
		System.out.println("correlation size: " + correlationstrengthtable.size());
		System.out.println("sub 1 size : " + correlationstrengthtable.get(1).size());
		System.out.println("sub 2 size : " + correlationstrengthtable.get(2).size());
		System.out.println("sub 3 size : " + correlationstrengthtable.get(3).size());
		DFDEngine.getInstance().updateLeastLGNumber(0.5);
		DFDEngine.getInstance().updateThreshold(0.8);
		Map<Integer, Short> faultlcondition = DFDEngine.getInstance().faultConditionalMarking(correlationstrengthtable);
		int n = 1;
		for(int i : faultlcondition.values()){
			System.out.println("node " + n + ": " + i);
		}
	}
	
}
