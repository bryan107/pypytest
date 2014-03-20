package gad.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import flanagan.analysis.PCA;
import flanagan.analysis.Stat;
import flanagan.math.Matrix;

import junit.framework.TestCase;

public class TestGAD extends TestCase {

	double mean = 20;
	double sd = 1;
	int number = 4;
//	Stat stat = new Stat();
	
	public void testGAD(){
//		System.out.println(Stat.gaussianCDF(mean, sd, 20.4));
		double[][] covariance = {{1.49,1.93},{1.93, 2.55}};
		Matrix m = new Matrix(covariance);
		double[][] eigenvalue = m.getEigenVector();
		System.out.println(eigenvalue[0][0] + "  " + eigenvalue[0][1]);
		System.out.println(eigenvalue[1][0] + "  " + eigenvalue[1][1]);
		double[] value = m.getEigenValues();
		System.out.println(value[0] +"  "+value[1]);
//		GAD gad = new GAD();
//		Map<Integer, Double> reading = new HashMap<Integer, Double>();
//		for(int i = 0 ; i < 130 ; i++){
//			System.out.println("Round" + i);
//			if(i == 90){
//				mean = 15;
//			}
//			reading.clear();
//			for(int j = 0 ; j < number ; j++){
//				reading.put(j, Stat.gaussianInverseCDF(mean, sd, Math.random()));
//			}
//			if(i > 50){
//				reading.put(3, Stat.gaussianInverseCDF(mean, 4, Math.random()));
//			}
//			
//			
//			ProcessedReadingPack pp = gad.markReading(reading);
//			if(pp.newEventOccurs()){
//				System.out.println("EVENT!!!!");
//			}
//			
//			for(MarkedReading mr : pp.markedReadingPack().values()){
//				System.out.println(" " + mr.toFormat());
//			}
//		}
		
		
		
	}
	
	
}
