package mfdr.math.emd.datastructure;

import java.util.Iterator;
import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.math.emd.InstantFrequency;
import mfdr.math.emd.utility.DataListPropertyExtractor;
import mfdr.utility.DataListOperator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class _BAK_IMF {
	private static Log logger = LogFactory.getLog(_BAK_IMF.class);
	private TimeSeries datapoints;
	private LinkedList<Data> freq;

	public _BAK_IMF(TimeSeries datapoints, double zerocrossingaccuracy,
			InstantFrequency IF) {
		updateIMF(datapoints, zerocrossingaccuracy, IF);
	}

	public void updateIMF(TimeSeries datapoints,
			double zerocrossingaccuracy, InstantFrequency IF) {
		this.datapoints = datapoints;
		calcInstantFrequency(zerocrossingaccuracy, IF);
	}

	/*
	 * Retrieve Data from IMF
	 */
	
	public LinkedList<Data> getDataList() {
		return datapoints;
	}

	public Data getDataPoint(int index) {
		return datapoints.get(index);
	}

	public Data getDataPoint(double time) {
		Iterator<Data> it = datapoints.iterator();
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
	
	public double size() {
		return datapoints.size();
	}

	public double timeLength() {
		return datapoints.peekLast().time() - datapoints.peekFirst().time();
	}

	
	/*
	 * Frequency related functions
	 */

	// Calculate instant frequencies and stored as this imf's private variable
	// (freq)
	private void calcInstantFrequency(double zerocrossingaccuracy,
			InstantFrequency IF) {
		try {
			freq = DataListPropertyExtractor.getInstance().getInstantFrequency(
					datapoints, zerocrossingaccuracy, IF);
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
		if (freq.size() < 2) {
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

	// Get average wavelength of this IMF
	public double averageWavelength() throws ArithmeticException{
		return 1 / averageFrequency();
	}

	/*
	 * IMF property retrieving functions
	 */

//	// Get the energy Density of Average Density
//	public double energyDensity() {
//		double energy = 0;
//		Iterator<Data> it = datapoints.iterator();
//		while (it.hasNext()) {
//			Data data = (Data) it.next();
//			energy += Math.pow(data.value(), 2);
//		}
//		logger.info("Energy:" + energy + "  Data Size:" + datapoints.size());
//		return energy / datapoints.size();
//	}
//	
//	public double normalizedEnergyDensity(){
//		// Normalize IMF to acquire normalized energy density 
//		LinkedList<Data> norm_datapoints = DataListOperator.getInstance().normalize(datapoints);
//		logger.info("NORMALISED:" );
//		Print.getInstance().printDataLinkedList(norm_datapoints, 100);
//		double energy = 0;
//		Iterator<Data> it = norm_datapoints.iterator();
//		while (it.hasNext()) {
//			Data data = (Data) it.next();
//			energy += Math.pow(data.value(), 2);
//		}
//		logger.info("Energy:" + energy + "  Data Size:" + datapoints.size());
//		return energy / datapoints.size();
//	}
}
