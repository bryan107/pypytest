package mfdr.math.emd.utility;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.math.emd.InstantFrequency;
import mfdr.math.emd.datastructure.LocalExtremas;
import mfdr.utility.DataListOperator;
import mfdr.utility.Print;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.interpolation.CubicSpline;

public class DataListPropertyExtractor {


	private static Log logger = LogFactory.getLog(DataListPropertyExtractor.class);
	private static DataListPropertyExtractor self = new DataListPropertyExtractor();
	// *********************
	DecimalFormat df = new DecimalFormat("0.0");

	// *********************
	private DataListPropertyExtractor() {

	}

	public static DataListPropertyExtractor getInstance() {
		return self;
	}

	/*
	 * The main function to Get INSTANT FREQUENCIES with raw imf data
	 */
	public LinkedList<Data> getInstantFrequency(LinkedList<Data> imf,
			double accuracy, InstantFrequency frequency) {
		LinkedList<Data> instantfrequency = new LinkedList<Data>();
		LinkedList<Data> extremas = DataListPropertyExtractor.getInstance().getSortedLocalExtremas(
				imf);
		LinkedList<Data> zerocrossings = DataListPropertyExtractor.getInstance().getZeroCrossings(
				imf, extremas, accuracy);

		instantfrequency = getInstantFrequency(extremas, zerocrossings,
				frequency);
		if (instantfrequency.isEmpty()) {
			logger.warn("This IMF does not has enough points to calculate Instant Frequency");
			System.out.print("IMF:");
			Print.getInstance().printDataLinkedList(imf, 100);
		}
		return instantfrequency;
	}

	// Get INSTANT FREQUENCIES with prepared input variables.
	public LinkedList<Data> getInstantFrequency(LinkedList<Data> extremas,
			LinkedList<Data> zerocrossings, InstantFrequency frequency) {
		LinkedList<Data> instantfrequency = new LinkedList<Data>();
		LinkedList<Data> critpoints = DataListOperator.getInstance().mergeLinkedLists(extremas, zerocrossings);
		int index = 7; // To calculate instant frequency, at least 8 points are
						// required.
		try {
			while (index < critpoints.size()) {
				double T4 = critpoints.get(index - 3).time()
						- critpoints.get(index - 4).time();
				double T2_1 = critpoints.get(index - 3).time()
						- critpoints.get(index - 5).time();
				double T2_2 = critpoints.get(index - 2).time()
						- critpoints.get(index - 4).time();
				double T1_1 = critpoints.get(index - 3).time()
						- critpoints.get(index - 7).time();
				double T1_2 = critpoints.get(index - 2).time()
						- critpoints.get(index - 6).time();
				double T1_3 = critpoints.get(index - 1).time()
						- critpoints.get(index - 5).time();
				double T1_4 = critpoints.get(index - 0).time()
						- critpoints.get(index - 4).time();
				double localfrequency = frequency.calFrequency(T4, T2_1, T2_2,
						T1_1, T1_2, T1_3, T1_4);
				// Memorize the start point of the local instant frequency.
				instantfrequency.add(new Data(critpoints.get(index - 4).time(),
						localfrequency));
				index++;
			}
			// Add the end point of the local instant frequency.
			instantfrequency.add(new Data(critpoints.get(index - 4).time(), 0));
		} catch (Exception e) {

		}
		return instantfrequency;
	}

	/*
	 * Get all ZERO-CROSSINGs <DATA>, of a given dataset.
	 * The information of EXTREMAs is needed.
	 */
	public LinkedList<Data> getZeroCrossings(LinkedList<Data> data, LinkedList<Data> sortedextremas,
			double accuracy) {
		LinkedList<Data> zerocrossings = new LinkedList<Data>();
		double[] datapoints = new double[data.size()];
		double[] datavalues = new double[data.size()];

		/*
		 *  Prepare data structures for CubicSpline
		 */
		Iterator<Data> it = data.iterator();
		try {
			for (int i = 0; it.hasNext(); i++) {
				Data d = it.next();
				datapoints[i] = d.time();
				datavalues[i] = d.value();
			}
		} catch (Exception e) {
			logger.error("Error when prepare Cublic Spine Interpolation" + e);
		}

		/*
		 *  Calculate and store zero crossings
		 */
		CubicSpline CS = new CubicSpline(datapoints, datavalues);
		it = sortedextremas.iterator();
		Data a, b;
		try {
			a = it.next();
			while (it.hasNext()) {
				b = it.next();
				zerocrossings.add(new Data(getLocalZeroCrossing(CS, a.time(),
						b.time(), accuracy), 0));
				a = b;
			}
		} catch (Exception e) {
			logger.error("Does not have enough extremas to calculate zerocrossings"
					+ e);
		}
		return zerocrossings;
	}
	
	/*
	 * Get all ZERO-CROSSINGs <DATA>, of a given dataset.
	 * The information of EXTREMAs is NOT needed.
	 */
	public LinkedList<Data> getZeroCrossings(LinkedList<Data> data, double accuracy) {
		LinkedList<Data> zerocrossings = new LinkedList<Data>();
		double[] datapoints = new double[data.size()];
		double[] datavalues = new double[data.size()];

		/*
		 *  Prepare data structures for CubicSpline
		 */
		Iterator<Data> it = data.iterator();
		try {
			for (int i = 0; it.hasNext(); i++) {
				Data d = it.next();
				datapoints[i] = d.time();
				datavalues[i] = d.value();
			}
		} catch (Exception e) {
			logger.error("Error when prepare Cublic Spine Interpolation" + e);
		}

		/*
		 *  Calculate and store zero crossings
		 */
		CubicSpline CS = new CubicSpline(datapoints, datavalues);
		// Get SORTED EXTREMAs
		it = getSortedLocalExtremas(data).iterator();
		Data a, b;
		try {
			a = it.next();
			while (it.hasNext()) {
				b = it.next();
				zerocrossings.add(new Data(getLocalZeroCrossing(CS, a.time(),
						b.time(), accuracy), 0));
				a = b;
			}
		} catch (Exception e) {
			logger.error("Does not have enough extremas to calculate zerocrossings"
					+ e);
		}
		return zerocrossings;
	}
	
	/*
	 * This is a recursive function to get the time stamp of zero-crossing point between [a] and [b].
	 * CubicSpline is used here and is needed to be passed in by the calling function. 
	 * An accuracy parameter is needed to by passed in by the calling function.
	 */
	public double getLocalZeroCrossing(CubicSpline CS, double a, double b,
			double zeroapproximation) {
		// Check if the two points has a zero crossing point.
		if (sign(CS.interpolate(a)) == sign(CS.interpolate(b))) {
			logger.error("Input A[" + df.format(a) + "]:" + CS.interpolate(a)
					+ " and B [" + df.format(b) + "]" + CS.interpolate(b)
					+ " has the same sign, no zero crossing can be acquired");
			return (a + b) / 2;
		}
		double middle = (a + b) / 2;
		double result = middle;
		if (Math.abs(a - b) > zeroapproximation) {
			if (sign(CS.interpolate(middle)) == sign(CS.interpolate(a))) {
				result = getLocalZeroCrossing(CS, middle, b, zeroapproximation);
			} else {
				result = getLocalZeroCrossing(CS, a, middle, zeroapproximation);
			}
		}
		return result;
	}

	/*
	 * Retrieve local extremas from data.
	 */
	public LocalExtremas getLocalExtremas(LinkedList<Data> data) {

		LocalExtremas le = new LocalExtremas();
		// Exam first data point
		if(data.get(0).value() > data.get(1).value()){
			le.localMaxima().add(data.get(0));
		}else if (data.get(0).value() < data.get(1).value()){
			le.localMinima().add(data.get(0));
		}
		// Iterate through the middles
		for (int i = 1; i < (data.size() - 1); i++) {
			// If a point is a local maximum
			if ((data.get(i).value() > data.get(i - 1).value())
					&& (data.get(i).value() > data.get(i + 1).value()))
				le.localMaxima().add(data.get(i));
			// If a point is a local minimum
			else if ((data.get(i).value() < data.get(i - 1).value())
					&& (data.get(i).value() < data.get(i + 1).value()))
				le.localMinima().add(data.get(i));
		}
		// Exam the final data point
		if(data.get(data.size()-1).value() > data.get(data.size()-2).value()){
			le.localMaxima().add(data.get(data.size()-1));
		}else if(data.get(data.size()-1).value() < data.get(data.size()-2).value()){
			le.localMinima().add(data.get(data.size()-1));
		}
		return le;
	}

	/*
	 * Retrieve local extremas from data. Return in a sorted order.
	 */
	public LinkedList<Data> getSortedLocalExtremas(LinkedList<Data> data) {
		LinkedList<Data> sortle = new LinkedList<Data>();
		LocalExtremas le = getLocalExtremas(data);
		
		// Remove EXTREMAs that have non-zero-crossing signs.
		removeNonZeroCrossingExtremas(le);
		
		// Preparing data structures
		Iterator<Data> itmaxima = le.localMaxima().iterator();
		Iterator<Data> itminima = le.localMinima().iterator();
		// initiate current time of search.
		double currenttime = 0;
		Data max, min;
		// Check if search is valid
		if(!itmaxima.hasNext() || !itminima.hasNext()){
			return sortle;
		}
		max = itmaxima.next();
		min = itminima.next();
		/*
		 * FIRST INSERTION: No data is in sortle.
		 */
		if (max.time() < min.time()) {
			sortle.add(max);
			currenttime = max.time();
		} else {
			sortle.add(min);
			currenttime = min.time();
		}
		
		/*
		 * AFTER THE FIRST INSERTION: consider the last EXTREMA in sortle.
		 */
		while (itmaxima.hasNext() && itminima.hasNext()) {
			currenttime = sortle.peekLast().time();
			// Remove past data
			while (max.time() <= currenttime) {
				max = itmaxima.next();
			}
			// Remove past data
			while (min.time() <= currenttime) {
				min = itminima.next();
			}
			/*
			 *  If the previous EXTREMA is a MAXIMA
			 */
			if (sortle.peekLast().value() > 0) {
				// Smooth local noises in the MAXIMA region.
				if (max.time() < min.time()) {
					if (max.value() > sortle.peekLast().value()) {
						sortle.removeLast();
						sortle.add(max);
						currenttime = max.time();
					}
					max = itmaxima.next();
				} 
				// Insert new MINIMA
				else {
					sortle.add(min);
					currenttime = min.time();
					min = itminima.next();
				}
			}
			/*
			 *  If the previous EXTREMA is a MINIMA
			 */
			else {
				// Smooth local noises in the MINIMA region
				if (min.time() < max.time()) {
					if (min.value() < sortle.peekLast().value()) {
						sortle.removeLast();
						sortle.add(min);
						currenttime = min.time();
					}
					min = itminima.next();
				} 
				// Insert new MAXIMA
				else {
					sortle.add(max);
					currenttime = max.time();
					max = itmaxima.next();
				}
			}
		}
		/*
		 * When contains ONLY one type of EXTREMAs 
		 * Recycle the rest of the EXTREMAs
		 */
		recycleRestExtremas(sortle, itmaxima, itminima, max, min);
		logger.warn("Sortle Size: " + sortle.size());
		return sortle;
	}

	/*
	 * Recycle the rest of the EXTREMAS
	 */
	private void recycleRestExtremas(LinkedList<Data> sortle,
			Iterator<Data> itmaxima, Iterator<Data> itminima, Data max, Data min) {
		// IF there exists MAXIMAs
		if (itmaxima.hasNext()) {
			while (itmaxima.hasNext()) {
				// Select the MAXIMA with the largest value.
				Data tempmax = itmaxima.next();
				if (tempmax.value() > max.value()) {
					max = tempmax;
				}
			}
			sortle.add(max);
		}
		// IF there exists MINIMAs
		if (itminima.hasNext()) {
			while (itminima.hasNext()) {
				// Select the MINIMA with the smallest value.
				Data tempmin = itminima.next();
				if (tempmin.value() < min.value()) {
					min = tempmin;
				}
			}
			sortle.add(min);
		}
	}

	/*
	 * Remove NON-ZEROCROSSING EXTREMAs	
	 */
	private void removeNonZeroCrossingExtremas(LocalExtremas le) {
		// Remove MAXIMAs having negative values.
		for (int i = 0; i < le.localMaxima().size(); i++) {
			if (le.localMaxima().get(i).value() <= 0) {
				le.localMaxima().remove(i);
				i--;
			}
		}
		// Remove MINIMAs having positive values
		for (int i = 0; i < le.localMinima().size(); i++) {
			if (le.localMinima().get(i).value() >= 0) {
				le.localMinima().remove(i);
				i--;
			}
		}
	}

	/*
	 * Return the SIGN of the given value.
	 */
	private int sign(double x) {
		return x >= 0 ? 1 : -1;
	}



}
