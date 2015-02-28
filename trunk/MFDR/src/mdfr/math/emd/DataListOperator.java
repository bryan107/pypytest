package mdfr.math.emd;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.math.emd.datastructure.IMF;
import mdfr.utility.StatTool;

public class DataListOperator {
	
	/*
	 * This Class provides basic <Data> LinkedList merge and information extraction methods. 
	 */
	private static Log logger = LogFactory.getLog(DataListOperator.class);
	private static DataListOperator self = new DataListOperator();
	// TODO Remove the use of these two final variables
	private final short TIME = 0;
	private final short VALUE = 1;
	
	private DataListOperator(){
		
	}
	
	public static DataListOperator getInstance(){
		return self;
	}
	
	/*
	 * Convert <Data> LinkedList to mono-property Array. 
	 * The parameter "option" controls which information is converted
	 * TIME(0): convert the time information into array.
	 * VALUE(1): convert the time information into array. 
	 */
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
	
	/*
	 * Convert Data LinkedList to a 2-D array containing both time and value reference
	 * NO input parameter is needed.
	 * Array[i][0]: TIME
	 * Array[i][1]: VALUE
	 */
	public double[][] LinkedListToArray(LinkedList<Data> linkedlist){
		double[][] array = new double[linkedlist.size()][2] ;
		for (int i = 0 ; i < array.length ; i++){
			array[i][0] = Double.valueOf(linkedlist.get(i).time());
			array[i][1] = Double.valueOf(linkedlist.get(i).value());
		}
		return array;
	}

	/*
	 * Merge two <Data> List according to the time stamps of each <Data>.
	 * */
	public LinkedList<Data> mergeLinkedLists(LinkedList<Data> a,
			LinkedList<Data> b) {
		TimeSeries newlist = new TimeSeries();
		int index_a = 0;
		int index_b = 0;
		// When both lists have contents.
		while (index_a < a.size() && index_b < b.size()) {
			if (a.get(index_a).time() < b.get(index_b).time()) {
				newlist.add(a.get(index_a));
				index_a++;
			} else {
				newlist.add(b.get(index_b));
				index_b++;
			}
		}
		// Add the rest of lists to newlist.
		for (; index_a < a.size(); index_a++) {
			newlist.add(a.get(index_a));
		}
		for (; index_b < b.size(); index_b++) {
			newlist.add(b.get(index_b));
		}
		return newlist;
	}
	
	public TimeSeries normalize(TimeSeries dataset, double base){
		TimeSeries norm_dataset = new TimeSeries();
		// Calculate normalized dataset
		Iterator<Data> it = dataset.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			norm_dataset.add(new Data(data.time(),(data.value()/base)));
		}
		return norm_dataset;
	}
	
	public LinkedList<Double> getTimeList(LinkedList<Data> linkedlist){
		LinkedList<Double> timeref = new LinkedList<Double>();
		Iterator<Data> it = linkedlist.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			timeref.add(data.time());
		}
		return timeref;
	}
	
	public LinkedList<Double> getValueList(LinkedList<Data> linkedlist){
		LinkedList<Double> timeref = new LinkedList<Double>();
		Iterator<Data> it = linkedlist.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			timeref.add(data.value());
		}
		return timeref;
	}
	
}
