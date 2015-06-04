package mfdr.utility;

import java.util.Iterator;
import java.util.LinkedList;

import mfdr.datastructure.Data;

public class SignalProcessing {

	private static SignalProcessing self = new SignalProcessing();
	
	private SignalProcessing(){
		
	}
	
	public static SignalProcessing getInstance(){
		return self;
	}
			
	
    /**
    * This is a "wrapped" signal processing-style autocorrelation. 
    * For "true" autocorrelation, the data must be zero padded.  
    */
	/*
	 * The implementation for double array
	 */
   public double[] autoCorr(double [] x) {
       double[] ac = new double[x.length]; 
	   int n = x.length;
       for (int j = 0; j < n; j++) {
           for (int i = 0; i < n; i++) {
               ac[j] += x[i] * x[(n + i - j) % n];
           }
       }
       return ac;
   }
   
   public double autoCorrCoeff(double [] x) {
       double acc = 0;
	   double[] ac = new double[x.length]; 
	   int n = x.length;
       for (int j = 0; j < n; j++) {
           for (int i = 0; i < n; i++) {
               ac[j] += x[i] * x[(n + i - j) % n];
           }
       }
       acc = maxValue(ac);
       return acc;
   }
      
   /*
    * The implementation for LinkedList<Data>
    */
   public LinkedList<Double> autoCorr(LinkedList<Data> dataset){
       LinkedList<Double> ac = new LinkedList<Double>(); 
	   int n = dataset.size();
       for (int j = 0; j < n; j++) {
    	   double temp = 0;
           for (int i = 0; i < n; i++) {
               temp += dataset.get(i).value() * dataset.get((n + i - j) % n).value();
           }
           ac.add(j, temp);
       }
       return ac;
   }
   
   public double autoCorrCoeff(LinkedList<Data> dataset){
	   double acc = 0;
	   LinkedList<Double> ac = new LinkedList<Double>(); 
	   int n = dataset.size();
       for (int j = 0; j < n; j++) {
    	   double temp = 0;
           for (int i = 0; i < n; i++) {
               temp += dataset.get(i).value() * dataset.get((n + i - j) % n).value();
           }
           ac.add(j, temp);
       }
       acc = maxValue(ac);
       return acc;
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
   
   public double maxValue(LinkedList<Double> dataset){
		double max = 0;
		Iterator<Double> it = dataset.iterator();
		while(it.hasNext()){
			Double data = it.next();
			if(Math.abs(data) > max){
				max = Math.abs(data);
			}
		}
		return max;
   }
   
   public double maxValue(double[] x){
		double max = 0;
		for(int i = 0 ; i < x.length ; i++){
			if(Math.abs(x[i]) > max){
				max = Math.abs(x[i]);
			}
		}
		return max;
   }
	
}
