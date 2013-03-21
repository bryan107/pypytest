package gad.core;

import java.util.Map;

import junit.framework.TestCase;

public class TestPatternGenerator extends TestCase {
	public void testGetEstimate(){
		int[] nodeids = {1,2,3,4,5};
		SMDB.getInstance().setupWindowSize(5);
		for(int i = 0 ; i < 5 ; i++){
			for(int j = 1 ; j <= nodeids.length ; j++)
			SMDB.getInstance().putReading(j, 20 * (1 + 0.05 * Math.random()), true);
		}
		PatternGenerator pg = new PatternGenerator();
		
		Map<Integer, Map<Integer, EstimatedVariance>> e = pg.getEstimation(nodeids);

		for(int i = 0 ; i < nodeids.length ; i++){
			for(int j = 0 ; j < nodeids.length ; j++){
				
				double[][] direction =  e.get(i).get(j).Direction();
				double[] deviation = e.get(i).get(j).Deviation();
				
				System.out.println("Direction [" + i + "][" + j +"]:");
				System.out.print("[" + direction[0][0] +" "+ direction[0][1] + "] [" + direction[1][0] +" "+ direction[1][1] + "] ");
				System.out.println();
				System.out.println("Deviation [" + i + "][" + j +"]:");
				System.out.println( deviation[0] + " , " + deviation[1]);
			}
		}
	}
}
