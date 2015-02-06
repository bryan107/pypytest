package mdfr.math.emd;

import java.util.Iterator;
import java.util.LinkedList;

import mdfr.math.emd.datastructure.LocalExtremas;

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
	
	public LocalExtremas getLocalExtremas(LinkedList<Data> data){
		LocalExtremas le = new LocalExtremas();
		for(int i = 1 ; i < (data.size() -1) ; i++){
			// If a point is a local maximum
			if((data.get(i).value() > data.get(i-1).value()) && (data.get(i).value() > data.get(i+1).value()))
				le.localMaxima().add(data.get(i));
			// If a point is a local minimum
			else if((data.get(i).value() < data.get(i-1).value()) && (data.get(i).value() < data.get(i+1).value()))
				le.localMinima().add(data.get(i));
		}
		return le;
	}	
	
	/*
	 * Get the mean values between envelopes with the same length.
	 * */
	
	public LinkedList<Data> getMean(LinkedList<Data> list1, LinkedList<Data> list2){
		LinkedList<Data> mean = new LinkedList<Data>();
		if(list1.size() != list2.size()){
			logger.error("list size not match");
			return null;
		}
		Iterator<Data> value1 = list1.iterator();
		Iterator<Data> value2 = list2.iterator();
		while(value1.hasNext() && value2.hasNext()){
			Data data1 = value1.next();
			Data data2 = value2.next();
			// If two list are aligned, calculate and store mean.
			if(data1.time() == data2.time()){
				double time = data1.time();
				double value = (data1.value() + data2.value())/2;
				Data data = new Data(time,value);
				mean.add(data);
			}else{
				logger.error("list is not aligned");
			}
			
		}
		return mean;
	}
	
	/*
	 * Get differences
	 * */
	
	public LinkedList<Data> getDifference(LinkedList<Data> list1 , LinkedList<Data> list2){
		LinkedList<Data> difference = new LinkedList<Data>();
		if(list1.size() != list2.size()){
			logger.error("list size not match");
			return null;
		}
		Iterator<Data> value1 = list1.iterator();
		Iterator<Data> value2 = list2.iterator();
		while(value1.hasNext() && value2.hasNext()){
			Data data1 = value1.next();
			Data data2 = value2.next();
			// If two list are aligned, calculate and store mean.
			if(data1.time() == data2.time()){
				double time = data1.time();
				double value = data1.value() - data2.value();
				Data data = new Data(time,value);
				difference.add(data);
			} else{
				logger.error("list is not aligned");
			}
		}
		return difference;
	}
	
	public LinkedList<Data> getSum(LinkedList<Data> list1 , LinkedList<Data> list2){
		LinkedList<Data> sum = new LinkedList<Data>();
		if(list1.size() != list2.size()){
			logger.error("list size not match");
			return null;
		}
		Iterator<Data> value1 = list1.iterator();
		Iterator<Data> value2 = list2.iterator();
		while(value1.hasNext() && value2.hasNext()){
			Data data1 = value1.next();
			Data data2 = value2.next();
			// If two list are aligned, calculate and store mean.
			if(data1.time() == data2.time()){
				double time = data1.time();
				double value = data1.value() + data2.value();
				Data data = new Data(time,value);
				sum.add(data);
			} else{
				logger.error("list is not aligned");
			}
		}
		return sum;
	}
	
}
