package gad.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import flanagan.analysis.Stat;

import junit.framework.TestCase;

public class TestGAD extends TestCase {

	double mean = 20;
	double sd = 0.5;
	int number = 4;
//	Stat stat = new Stat();
	
	public void testGAD(){
		//System.out.println(Stat.gaussianInverseCDF(mean, sd, 0.001));
		GAD gad = new GAD();
		Map<Integer, Double> reading = new HashMap<Integer, Double>();
		for(int i = 0 ; i < 130 ; i++){
			System.out.println("Round" + i);
			if(i == 90){
				mean = 15;
			}
			reading.clear();
			for(int j = 0 ; j < number ; j++){
				reading.put(j, Stat.gaussianInverseCDF(mean, sd, Math.random()));
			}
			if(i > 50){
				reading.put(3, Stat.gaussianInverseCDF(mean, 4, Math.random()));
			}
			
			
			ProcessedReadingPack pp = gad.markReading(reading);
			if(pp.newEventOccurs()){
				System.out.println("EVENT!!!!");
			}
			
			for(MarkedReading mr : pp.markedReadingPack().values()){
				System.out.println(" " + mr.toFormat());
			}
		}
		
		
		
	}
	
	
}
