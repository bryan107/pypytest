package mfdr.math.emd.datastructure;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.math.emd.InstantFrequency;
import mfdr.math.emd.utility.DataListPropertyExtractor;
import mfdr.utility.DataListOperator;

public class IMF extends TimeSeries {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1467628417547634012L;
	private static Log logger = LogFactory.getLog(IMF.class);
	private LinkedList<Data> freq;

	public IMF(TimeSeries datapoints, double zerocrossingaccuracy,InstantFrequency IF) {
		updateIMF(datapoints, zerocrossingaccuracy, IF);
	}

	public void updateIMF(TimeSeries datapoints, double zerocrossingaccuracy, InstantFrequency IF) {
		Iterator<Data> it = datapoints.iterator();
		while (it.hasNext()) {
			this.add((Data) it.next());
		}
		calcInstantFrequency(zerocrossingaccuracy, IF);
	}

	/*
	 * Retrieve Data from IMF
	 */
	

	public Data getDataPoint(double time) {
		Iterator<Data> it = iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			if (data.time() == time) {
				return data;
			}
		}
		logger.warn("No such data point at time: " + time + " in this IMF");
		return null;
	}

	/*
	 * Retrieve IMF properties
	 */
	
	/**
	 * Get the total time length of this IMF
	 * @return
	 */
	public double timeLength() {
		return peekLast().time() - peekFirst().time();
	}

	
	/*
	 * Frequency related functions
	 */

	// Calculate instant frequencies and stored as this imf's private variable
	// (freq)
	private void calcInstantFrequency(double zerocrossingaccuracy,
			InstantFrequency IF) {
		try {
			freq = DataListPropertyExtractor.getInstance().getInstantFrequency(this, zerocrossingaccuracy, IF);
		} catch (Exception e) {
			logger.error("EMD parameters are not properly setted" + e);
		}
	}

	// if exist frequency info
	public boolean hasInstantFrequency() {
		if (freq.isEmpty())
			return false;
		return true;
	}

	// Get instant frequency of this IMF
	public LinkedList<Data> instantFrequency() {
		if (!hasInstantFrequency())
			logger.warn("No instant frequency for this IMF");
		return freq;
	}

	// Get instant frequency of this IMF at time x
	public double instantFrequency(double x) {
		Iterator<Data> it = freq.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			if (x >= data.time()) {
				return data.value();
			}
		}
		logger.warn("Cannot retreive Frequency with given time point: " + x);
		return 0;
	}

	/*
	 * Return the frequency information of instFreq() in full resolution provided by input parameter: ref.
	 */
	public LinkedList<Data> instFreqFullResol(LinkedList<Data> ref){
		LinkedList<Data> fullfreq = new LinkedList<Data>();
		if (!hasInstantFrequency()){
			logger.warn("No instant frequency for this IMF");
			return null;
		}
		if (ref.isEmpty()){
			logger.warn("Reference Data list is empty");
			return null;
		}
		// Extract Time
		LinkedList<Double> timeref = DataListOperator.getInstance().getTimeList(ref);
		
		// Set iterator
		Iterator<Double> it_timeref = timeref.iterator();
		Iterator<Data> itfreq = freq.iterator();
		Data freq_front = null;
		Data freq_rare = itfreq.next();
		// Loop to add all freq into fullfreq
		double time = it_timeref.next();
		while (itfreq.hasNext()) {
			// Initiate the window
			freq_front = freq_rare;
			freq_rare = itfreq.next();
			// Loop when there is reference time point 
			while(it_timeref.hasNext()){
				// If the current time point is lying in the given window.
				if(time < freq_rare.time()){
					// If the current time point is before the begin of the time
					if(time < freq_front.time()){
						fullfreq.add(new Data(time, 0));
					}else{ // Add the frequency in this window size to the list of current time point.
						
						fullfreq.add(new Data(time, freq_front.value()));
					}

					time = it_timeref.next();
					continue;
				}
				break;
			}
		}
		// Pick up the frequencies for the rest reference time points
		while(it_timeref.hasNext()){
			fullfreq.add(new Data(time, freq_rare.value()));
			time = it_timeref.next();
		}
		return fullfreq;
	}
	
	// Get average frequency of this IMF
	public double averageFrequency() {
		double accumulatefreq = 0;
		Data freq_start = null, freq_end = null;
		Iterator<Data> it = freq.iterator();
		if (freq.size() < 1) {
			logger.warn("No freq can be retrieved");
			return 0;
		}
		while (it.hasNext()) {
			// If this is the first round
			if (freq_end == null) {
				// Set start point as the first point.
				freq_start = it.next();
			} else {
				// Set start point = previous end point.
				freq_start = freq_end;
			}
			// Set end point = new end point.
			try {
				freq_end = it.next();
			} catch (Exception e) {
				logger.warn("No freq End can be retrieved" + e);
				return 0;
			}
			accumulatefreq += (freq_start.value() * (freq_end.time() - freq_start
					.time()));
		}
		double totaltime = freq.peekLast().time() - freq.peekFirst().time();
		return accumulatefreq / totaltime;
	}

	/**
	 * Retrieve average wavelength of this IMF
	 * @return
	 * @throws ArithmeticException
	 */
	public double averageWavelength() throws ArithmeticException{
//		double freq = averageFrequency();
		double freq = this.timeLength()/(this.getZeroCrossingCount()/2);
		if(freq == 0)
			throw new ArithmeticException("Division by zero!");
		return 1 / freq;
	}
	
	
	public boolean hasAverageWavelength(){
		if(averageFrequency() == 0){
			return false;
		}
		return true;
	}
	
	
	public double getZeroCrossingCount(){
		double count = 0;
		Iterator<Data> it = this.iterator();
		double previous = it.next().value();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			if(previous*data.value() <= 0)
				count ++;
			previous = data.value();
		}
		return count;
	}
	
}
