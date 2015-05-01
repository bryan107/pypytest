package mfdr.utility;

import java.util.Iterator;
import java.util.LinkedList;

import flanagan.analysis.Stat;
import mfdr.datastructure.Data;

public class StatTool {
	private final short VALUE = 1;
	private static StatTool self = new StatTool();
	
	private StatTool(){
		
	}
	
	public static StatTool getInstance(){
		return self;
	}
			
	
    /**
    * This is a "wrapped" signal processing-style autocorrelation. 
    * For "true" autocorrelation, the data must be zero padded.  
    */
	/*
	 * The implementation for double array
	 */
   public double[] autoCorr(double [] xx) {
       double[] ac = new double[xx.length]; 
       int n = xx.length;
       for (int j = 0; j < n; j++) {
    	   double[] yy = new double[xx.length];
    	   // Set y as a time-sifting (of time j) array of x.
    	   for (int i = 0; i < n; i++) {
               yy[i] = xx[(i+j)%xx.length];
           }
    	   ac[j] = Stat.corrCoeff(xx, yy);
       }
       return ac;
   }
   
   public double maxAutoCorrCoeff(double [] xx) {
       double acc = 0;
	   double[] ac = autoCorr(xx);
       acc = maxAbsValue(ac);
       return acc;
   }
      
   /*
    * The implementation for LinkedList<Data>
    */
   public double[] autoCorr(LinkedList<Data> dataset){
	   double[] xx = DataListOperator.getInstance().linkedListToArray(dataset, VALUE);
       return autoCorr(xx);
   }
   
   public double autoCorrCoeff(LinkedList<Data> dataset){
       return maxAutoCorrCoeff(autoCorr(dataset));
   } 
   
   /**
    * These are the functions to extract the maximum absolute value from an data series. 
    * 
    */
   public double maxDataListAbsValue(LinkedList<Data> dataset){
		double max = 0;
		Iterator<Data> it = dataset.iterator();
		while(it.hasNext()){
			Data data = it.next();
			if(Math.abs(data.value()) > max){
				max = Math.abs(data.value());
			}
		}
		return max;
   }
   
//   public double maxValue(LinkedList<Double> dataset){
//		double max = 0;
//		Iterator<Double> it = dataset.iterator();
//		while(it.hasNext()){
//			Double data = it.next();
//			if(data > max){
//				max = data;
//			}
//		}
//		return max;
//   }
   
   public double maxValue(double[] x){
		double max = 0;
		for(int i = 0 ; i < x.length ; i++){
			if(x[i] > max){
				max = x[i];
			}
		}
		return max;
   }
   
   public double maxAbsValue(double[] x){
		double max = 0;
		for(int i = 0 ; i < x.length ; i++){
			if(Math.abs(x[i]) > Math.abs(max)){
				max = x[i];
			}
		}
		return max;
   }
}
