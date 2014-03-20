package gad.core;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import flanagan.analysis.Stat;

import junit.framework.TestCase;

public class TestPatternGenerator extends TestCase {
	private final short GD = 3;
	DecimalFormat df = new DecimalFormat("0.000");
	final double mean = 20;
	final double sd = 0.5;
	final double ndv = 3;
	
	public void testGetEstimate(){
		// Setup Database
		int windowsize = 30;
		int[] nodeids = {1,2,3,4};
		SMDB.getInstance().setupWindowSize(windowsize);
		for(int i = 0 ; i < windowsize ; i++){
			for(int j = 1 ; j <= nodeids.length ; j++)
			SMDB.getInstance().putReading(j, Stat.gaussianInverseCDF(mean, sd, Math.random()), GD);
		}
		// Setup Reading
		Map<Integer, Double> reading = new HashMap<Integer, Double>();
		for(int i = 1 ; i <= nodeids.length ; i++){
			reading.put(i, Stat.gaussianInverseCDF(mean, sd, Math.random()));
		}
		
		// STEP2: Pattern Generation
		PatternGeneratorPCA pg = new PatternGeneratorPCA(windowsize);
		Map<Integer, Map<Integer, EstimatedVariancePCA>> e = pg.getEstimation(reading);
		printPCAResult(nodeids, e);

		// STEP3: Correlation Generation
		CorrelationAsessment ce = new CorrelationAsessment(ndv);
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

	private void printPCAResult(int[] nodeids,
			Map<Integer, Map<Integer, EstimatedVariancePCA>> e) {
		for(int i = 0 ; i < nodeids.length ; i++){
			for(int j = 0 ; j < nodeids.length ; j++){
				
				if(nodeids[i] == nodeids[j])
					continue;
				double[][] direction =  e.get(nodeids[i] ).get(nodeids[j]).direction();
				double[] deviation = e.get(nodeids[i] ).get(nodeids[j]).deviation();
				
				System.out.println("Pair[" + nodeids[i] + "][" + nodeids[j] +"]:");
				System.out.print("Direction:");
				System.out.print("[" + df.format(direction[0][0]) +" "+ df.format(direction[0][1]) + "] [" + df.format(direction[1][0]) +" "+ df.format(direction[1][1]) + "] ");
				System.out.print("  Deviation:");
				System.out.println( df.format(deviation[0]) + " , " + df.format(deviation[1]));
			}
		}
	}
}
