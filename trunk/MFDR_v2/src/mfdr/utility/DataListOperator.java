package mfdr.utility;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;

public class DataListOperator {

	/**
	 * This Class provides basic <Data> LinkedList merge and information
	 * extraction methods.
	 */
	private static Log logger = LogFactory.getLog(DataListOperator.class);
	private static DataListOperator self = new DataListOperator();
	// TODO Remove the use of these two final variables
	private final short TIME = 0;
	private final short VALUE = 1;

	private DataListOperator() {

	}

	public static DataListOperator getInstance() {
		return self;
	}

	// TODO currently use TimeSeries as return object, change to LinkedList<Data> with proper settings.
	/**
	 * This function combines the input linked lists into a single list.
	 * <p>
	 * @param linkedlist
	 * @param windowsize
	 * @return LinkedList<LinkedList<Data>>
	 */
	public TimeSeries linkedListCombinition(LinkedList<TimeSeries> inputlist){
		TimeSeries output = new TimeSeries();
		for(int i = 0 ; i < inputlist.size() ; i++){
			LinkedList<Data> temp = inputlist.get(i);
			for(int j = 0 ; j < temp.size() ; j++){
				output.add(temp.get(j));
			}
		}
		return output;
	}
	
	// TODO currently use TimeSeries as return object, change to LinkedList<Data> with proper settings.
	/**
	 * This function divided the input linked list into multiple lists with
	 * regard of a given window size.
	 * <p>
	 * @param linkedlist
	 * @param windowsize
	 * @return LinkedList<LinkedList<Data>>
	 */
	public LinkedList<TimeSeries> linkedListDivision(
			LinkedList<Data> linkedlist, double windowsize) {
		LinkedList<TimeSeries> outputlist = new LinkedList<TimeSeries>();
		double inittime;
		try {
			inittime = linkedlist.get(0).time();
		} catch (Exception e) {
			logger.info("the size of input linked list is 0" + e);
			return null;
		}
		int i = 0;
		while (i < linkedlist.size()) {
			TimeSeries temp = new TimeSeries();
			inittime = linkedlist.get(i).time();
			while (i < linkedlist.size() && (linkedlist.get(i).time() - inittime) < windowsize) {
				temp.add(linkedlist.get(i));
				if(i < linkedlist.size()){
					i++;
				}else{
					logger.info("The input length (" + linkedlist.size()
							+ ") does not perfectly match the window size("
							+ windowsize + ")");
					outputlist.add(temp);
					break;
				}
			}
			outputlist.add(temp);
		}
		return outputlist;
	}

	/**
	 * This function calculate the sum of two linkedList<Data>
	 * <ul>
	 * output = linkedlist_1 + linkedlist_2. 
	 * </ul>
	 * The two linked lists must be properly aligned; otherwise, return null.
	 * <p>
	 * @param linkedlist_1
	 * @param linkedlist_2
	 * @return output
	 */
	public TimeSeries linkedListSum(LinkedList<Data> linkedlist_1 , LinkedList<Data> linkedlist_2){
		TimeSeries outputlist = new TimeSeries();
		// Check if the lengths of the two linked list are the same.
		if (linkedlist_1.size() != linkedlist_2.size()) {
			logger.info("The lengths of two input linkedlist<Data> are not comparable. List[1]:"
					+ linkedlist_1.size() + "  List[2]:" + linkedlist_2.size());
			return null;
		}
		// Check if the time stamp of the two linked lists are perfectly match.
		for (int i = 0; i < linkedlist_1.size(); i++) {
			if (linkedlist_1.get(i).time() == linkedlist_2.get(i).time()) {
				double sum = linkedlist_1.get(i).value()
						+ linkedlist_2.get(i).value();
				outputlist
						.add(new Data(linkedlist_1.get(i).time(), sum));
			} else {
				logger.info("The two input linkedlist<Data> are not aligned at the "
						+ i
						+ "th object. time[1]: "
						+ linkedlist_1.get(i).time()
						+ " time[2]:"
						+ linkedlist_2.get(i).time());
				return null;
			}
		}
		return outputlist;
	}
	
	/**
	 * This function calculate the subtraction between two linkedList<Data>
	 * <ul>
	 * output = linkedlist_1 - linkedlist_2. 
	 * </ul>
	 * The two linkedlist must be properly aligned; otherwise, return null.
	 * <p>
	 * @param linkedlist_1
	 * @param linkedlist_2
	 * @return output
	 */
	public TimeSeries linkedtListSubtraction(
			LinkedList<Data> linkedlist_1, LinkedList<Data> linkedlist_2) {
		TimeSeries outputlist = new TimeSeries();
		// Check if the lengths of the two linked list are the same.
		if (linkedlist_1.size() != linkedlist_2.size()) {
			logger.info("The lengths of two input linkedlist<Data> are not comparable. List[1]:"
					+ linkedlist_1.size() + "  List[2]:" + linkedlist_2.size());
			return null;
		}

		// Check if the time stamp of the two linked lists are perfectly match.
		for (int i = 0; i < linkedlist_1.size(); i++) {
			if (linkedlist_1.get(i).time() == linkedlist_2.get(i).time()) {
				double difference = linkedlist_1.get(i).value()
						- linkedlist_2.get(i).value();
				outputlist
						.add(new Data(linkedlist_1.get(i).time(), difference));
			} else {
				logger.info("The two input linkedlist<Data> are not aligned at the "
						+ i
						+ "th object. time[1]: "
						+ linkedlist_1.get(i).time()
						+ " time[2]:"
						+ linkedlist_2.get(i).time());
				return null;
			}
		}
		return outputlist;
	}

	/**
	 * Convert <Data> LinkedList to mono-property Array. 
	 * The parameter "option" controls which information is converted 
	 * <li> TIME(0): convert the time information into array. 
	 * <li> VALUE(1): convert the time information into array.
	 */
	public double[] linkedListToArray(LinkedList<Data> linkedlist, short option) {
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

	/**
	 * Convert <Double> LinkedList to a double Array.
	 */
	public double[] linkedDoubleListToArray(LinkedList<Double> linkedlist) {
		double[] array = new double[linkedlist.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = Double.valueOf(linkedlist.get(i));
		return array;
	}

	/**
	 * Convert Data LinkedList to a 2-D array containing both time and value
	 * reference NO input parameter is needed.
	 * <ul> 
	 * <li> Array[i][0]: TIME 
	 * <li> Array[i][1]: VALUE
	 * </ul>
	 */
	public double[][] linkedDataListToArray(LinkedList<Data> linkedlist) {
		double[][] array = new double[2][linkedlist.size()];
		for (int i = 0; i < linkedlist.size(); i++) {
			array[0][i] = Double.valueOf(linkedlist.get(i).time());
			array[1][i] = Double.valueOf(linkedlist.get(i).value());
		}
		return array;
	}

	/**
	 * Merge two <Data> List according to the time stamps of each <Data>.
	 */
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

	/**
	 * Normalize a time series
	 * 
	 * @param input
	 *            time series
	 * @param normalization
	 *            base
	 * @return normalized time series
	 */
	public TimeSeries normalize(TimeSeries dataset, double base) {
		TimeSeries norm_dataset = new TimeSeries();
		// Calculate normalized dataset
		Iterator<Data> it = dataset.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			norm_dataset.add(new Data(data.time(), (data.value() / base)));
		}
		return norm_dataset;
	}

	/**
	 * Extract value information from input LinkedList<Data>
	 * 
	 * @param LinkedList
	 *            <Data>
	 * @return LinkedList<Double>
	 */
	public LinkedList<Double> getTimeList(LinkedList<Data> linkedlist) {
		LinkedList<Double> timeref = new LinkedList<Double>();
		Iterator<Data> it = linkedlist.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			timeref.add(data.time());
		}
		return timeref;
	}

	/**
	 * Extract time information from input LinkedList<Data>
	 * 
	 * @param LinkedList<Data>
	 * @return LinkedList<Double>
	 */
	public LinkedList<Double> getValueList(LinkedList<Data> linkedlist) {
		LinkedList<Double> timeref = new LinkedList<Double>();
		Iterator<Data> it = linkedlist.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			timeref.add(data.value());
		}
		return timeref;
	}

}
