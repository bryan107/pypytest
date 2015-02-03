package mdfr.math.emd;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tools {
	private static Log logger = LogFactory.getLog(Tools.class);
	private static Tools self = new Tools();
	
	private Tools(){
		
	}
	
	public static Tools getInstance(){
		return self;
	}
	
	/*
	 * Find local extremas
	 * */
	public LocalExtremas getLocalExtremas(LinkedList<Double> data){
		LocalExtremas le = new LocalExtremas();
		for(int i = 1 ; i < (data.size() -1) ; i++){
			// If a point is a local maximum
			if((data.get(i) > data.get(i-1)) && (data.get(i) > data.get(i+1)))
				le.localMaxima().add(i);
			// If a point is a local minimum
			else if((data.get(i) < data.get(i-1)) && (data.get(i) < data.get(i+1)))
				le.localMinima().add(i);
		}
		return le;
	}	
	
	/*
	 * Get the mean values between envelopes with the same length.
	 * */
	public LinkedList<Double> getMean(LinkedList<Double> list1, LinkedList<Double> list2){
		LinkedList<Double> mean = new LinkedList<Double>();
		if(list1.size() != list2.size()){
			logger.error("list size not match");
			return null;
		}
		Iterator<Double> value1 = list1.iterator();
		Iterator<Double> value2 = list2.iterator();
		while(value1.hasNext() && value2.hasNext()){
			// Here defines the mean operation.
			mean.add((value1.next() + value2.next())/2);
		}
		return mean;
	}
	
	/*
	 * Get differences
	 * */
	public LinkedList<Double> getDifference(LinkedList<Double> list1 , LinkedList<Double> list2){
		LinkedList<Double> difference = new LinkedList<Double>();
		if(list1.size() != list2.size()){
			logger.error("list size not match");
			return null;
		}
		Iterator<Double> value1 = list1.iterator();
		Iterator<Double> value2 = list2.iterator();
		while(value1.hasNext() && value2.hasNext()){
			// Here defines the differencing operation
			difference.add(value1.next() - value2.next());
		}
		return difference;
	}
	
}
