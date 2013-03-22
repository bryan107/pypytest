package gad.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

public class TestPatternGenerator extends TestCase {
	private final short GD = 3;
	
	public void testGetEstimate(){
		int windowsize = 30;
		int[] nodeids = {1,2,3,4,5};
		SMDB.getInstance().setupWindowSize(windowsize);
		for(int i = 0 ; i < windowsize ; i++){
			for(int j = 1 ; j <= nodeids.length ; j++)
			SMDB.getInstance().putReading(j, 20 * (1 + 0.05 * Math.random()), GD);
		}
		
		Map<Integer, Double> reading = new HashMap<Integer, Double>();
		for(int i = 1 ; i <= 5 ; i++){
			reading.put(i, 20 * (1 + 0.05 * Math.random()));
		}
		
		PatternGenerator pg = new PatternGenerator(windowsize);
		
		Map<Integer, Map<Integer, EstimatedVariance>> e = pg.getEstimation(reading);

		for(int i = 0 ; i < nodeids.length ; i++){
			for(int j = 0 ; j < nodeids.length ; j++){
				
				if(nodeids[i] == nodeids[j])
					continue;
				double[][] direction =  e.get(nodeids[i] ).get(nodeids[j]).direction();
				double[] deviation = e.get(nodeids[i] ).get(nodeids[j]).deviation();
				
				System.out.println("Direction [" + nodeids[i] + "][" + nodeids[j] +"]:");
				System.out.print("[" + direction[0][0] +" "+ direction[0][1] + "] [" + direction[1][0] +" "+ direction[1][1] + "] ");
				System.out.println();
				System.out.println("Deviation [" + nodeids[i] + "][" + nodeids[j] +"]:");
				System.out.println( deviation[0] + " , " + deviation[1]);
			}
		}


	
		CorrelationEstimator ce = new CorrelationEstimator(3);
		Map<Integer, Map<Integer, Boolean>> correlation = ce.assessCorrelation(reading,  e);
		
		Iterator<Integer> itc = correlation.keySet().iterator();
		while(itc.hasNext()){
			int nodeid = itc.next();
			System.out.println("Node[" + nodeid + "]:");
			Map<Integer, Boolean> content = correlation.get(nodeid);
			Iterator<Integer> itd = content.keySet().iterator();
			while(itd.hasNext()){
				int nodeid2 = itd.next();
				System.out.print(" [" + nodeid2 + "]" + content.get(nodeid2) + "");
			}
			System.out.println();
		}
		
		DFDEngine dfd = new DFDEngine();
		Map<Integer, Boolean> devicecondition = new HashMap<Integer, Boolean>();
		for(int i = 1 ; i < 6 ; i++){
			devicecondition.put(i, true);
		}
		Map<Integer, Short> readingcondition = dfd.markCondition(correlation);
		System.out.println("Reading Conditions: ");
		Iterator<Integer> itrc = readingcondition.keySet().iterator();
		while(itrc.hasNext()){
			int nodeid = itrc.next();
			System.out.println(" [" + nodeid + "]:" + readingcondition.get(nodeid));
		}
	}
}
