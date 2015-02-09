package mdfr.math.emd;

import java.util.Iterator;
import java.util.LinkedList;

import mdfr.develop.toos.Print;
import mdfr.math.emd.datastructure.Data;
import mdfr.math.emd.datastructure.LocalExtremas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.interpolation.CubicSpline;

// TODO reallocate functions in reasonable objects.
public class Tools {
	
	private final short TIME = 0;
	private final short VALUE = 1;
	private static Log logger = LogFactory.getLog(Tools.class);
	private static Tools self = new Tools();
	
	private Tools(){
		
	}
	
	public static Tools getInstance(){
		return self;
	}
	
	/*
	 * Get Instant Frequencies
	 * */
	
	public LinkedList<Data> getInstantFrequency(LinkedList<Data> imf, double accuracy, InstantFrequency frequency){
		LinkedList<Data> instantfrequency = new LinkedList<Data>();
		LinkedList<Data> extremas = Tools.getInstance().getSortedLocalExtremas(imf);
		LinkedList<Data> zerocrossings =  Tools.getInstance().getZeroCrossings(imf, accuracy);
		instantfrequency = getInstantFrequency(extremas, zerocrossings, frequency);
		if(instantfrequency.isEmpty()){
			logger.warn("This IMF does not has enough points to calculate ");
			Print.getInstance().printDataLinkedList(imf);
		}
 		return instantfrequency;
	}
	
	public LinkedList<Data> getInstantFrequency(LinkedList<Data> extremas, LinkedList<Data> zerocrossings, InstantFrequency frequency){
		LinkedList<Data> instantfrequency = new LinkedList<Data>();
		LinkedList<Data> critpoints = mergeLinkedLists(extremas, zerocrossings);
		int index = 7; // To calculate instant frequency, at least 8 points are required.
		try {
			while(index < critpoints.size()){
				double T4 = critpoints.get(index-3).time() - critpoints.get(index-4).time();
				double T2_1 = critpoints.get(index-3).time() - critpoints.get(index-5).time();
				double T2_2 = critpoints.get(index-2).time() - critpoints.get(index-4).time();
				double T1_1 = critpoints.get(index-3).time() - critpoints.get(index-7).time();
				double T1_2 = critpoints.get(index-2).time() - critpoints.get(index-6).time();
				double T1_3 = critpoints.get(index-1).time() - critpoints.get(index-5).time();
				double T1_4 = critpoints.get(index-0).time() - critpoints.get(index-4).time();
				double localfrequency = frequency.calFrequency(T4, T2_1, T2_2, T1_1, T1_2, T1_3, T1_4);
				// Memorize the start point of the local instant frequency.
				instantfrequency.add(new Data(critpoints.get(index-4).time(), localfrequency));
				index++;
			}
			// Add the end point of the local instant frequency. 
			instantfrequency.add(new Data(critpoints.get(index-4).time(), 0));
		} catch (Exception e) {
			
		}
		return instantfrequency;
	}
	

	
	/*
	 * Get Zero Crossings
	 * */
	
	public LinkedList<Data> getZeroCrossings(LinkedList<Data> data, double accuracy){
		LinkedList<Data> zerocrossings = new LinkedList<Data>();
		double[] datapoints = new double[data.size()];
		double[] datavalues = new double[data.size()];
		
		// Prepare data structures for CubicSpline
		Iterator<Data> it = data.iterator();
		try {
			for(int i = 0 ; it.hasNext(); i++){
				Data d = it.next();
				datapoints[i] = d.time();
				datavalues[i] = d.value();
			}
		} catch (Exception e) {
			logger.error("Error when prepare Cublic Spine Interpolation" + e );
		}
		
		// Calculate zero crossings
		CubicSpline CS = new CubicSpline(datapoints, datavalues);
		LinkedList<Data> extremas = getSortedLocalExtremas(data);
		it = extremas.iterator();
		Data a,b;
		try {
			a = it.next();
			while(it.hasNext()){
				b = it.next();
				zerocrossings.add(new Data(getLocalZeroCrossing(CS, a.time(), b.time(), accuracy), 0));
				a = b;
			}
		} catch (Exception e) {
			logger.error("Does not have enough extremas to calculate zerocrossings" + e);
		}
		return zerocrossings;
	}
	
	public double getLocalZeroCrossing(CubicSpline CS , double a, double b, double zeroapproximation){
		//System.out.print("+");
		if(sign(CS.interpolate(a)) == sign(CS.interpolate(b))){
			logger.error("Input A[" + a + "] and B [" + b + "] has the same sign, no zero crossing can be acquired");
			
			return (a + b) / 2;
		}
		double middle = (a + b) / 2;
		double result = middle;
		if (Math.abs(a-b) > zeroapproximation){
			if(sign(CS.interpolate(middle)) == sign(CS.interpolate(a))){
				result = getLocalZeroCrossing(CS, middle, b, zeroapproximation);
			}
			else{
				result = getLocalZeroCrossing(CS, a, middle, zeroapproximation);
			}
		}
		return result;
	}
	
	private int sign(double x){
		return x >= 0 ? 1 : -1;
	}
	
	/*
	 * Find local extremas
	 * */
	
	/*
	 * Retrieve local extremas from data.
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
	 * Retrieve local extremas from data.
	 * Return in a sorted order.
	 * */
	public LinkedList<Data> getSortedLocalExtremas(LinkedList<Data> data){
		LinkedList<Data> sortle = new LinkedList<Data>();
		for(int i = 1 ; i < (data.size() -1) ; i++){
			// If a point is a local maximum
			if((data.get(i).value() > data.get(i-1).value()) && (data.get(i).value() > data.get(i+1).value()))
				sortle.add(data.get(i));
			// If a point is a local minimum
			else if((data.get(i).value() < data.get(i-1).value()) && (data.get(i).value() < data.get(i+1).value()))
				sortle.add(data.get(i));
		}
		return sortle;
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
	
	/*
	 * Get sums
	 * */
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
	
	
	/*
	 * TOOLS SECTION
	 * */
	/* 
	 * Convert LinkedList to Array
	 */
	// TODO remove the use of two constants: TIME and VALUE
	public double[] LinkedListToArray(LinkedList<Data> linkedlist, short option) {
		double[] array = new double[linkedlist.size()];
		switch (option) {
		case TIME:
			for (int i = 0; i < array.length; i++)
				array[i] = Double.valueOf(linkedlist.get(i).time());
			break;
		case VALUE:
			for (int i = 0; i < array.length; i++)
				array[i] = Double.valueOf(linkedlist.get(i).value());
			break;
		default:
			break;
		}
		return array;
	}

	public LinkedList<Data> mergeLinkedLists(LinkedList<Data> a, LinkedList<Data> b){
		LinkedList<Data> newlist = new LinkedList<Data>();
		int index_a = 0;
		int index_b = 0;
		// When both lists have contents.
		while(index_a < a.size()  && index_b < b.size()){
			if(a.get(index_a).time() < b.get(index_b).time()){
				newlist.add(a.get(index_a));
				index_a++;
			}
			else{
				newlist.add(b.get(index_b));
				index_b++;
			}
		}
		// Add the rest of lists to newlist.
		for(; index_a < a.size(); index_a++){
			newlist.add(a.get(index_a));
		}
		for(; index_b < b.size(); index_b++){
			newlist.add(b.get(index_b));
		}
		return newlist;
	}
}
