package mfdr.datastructure;

import java.util.Iterator;
import java.util.LinkedList;

import mfdr.utility.DataListOperator;
import mfdr.utility.StatTool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeSeries extends LinkedList<Data> {

	/**
	 * This is an implementation of a LinkedList<Data> 
	 * with extra functions that serves our algorithm.
	 */
	private static final long serialVersionUID = 133327817106629961L;
	private static Log logger = LogFactory.getLog(TimeSeries.class);
	
	// Calculate the energy of the time series
	public double energy(){
		double energy = 0;
		Iterator<Data> it = iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			energy += Math.pow(data.value(), 2);
		}
		return energy;
	}
	
	public double normalizedEnergy(double base){
		// Normalize IMF to acquire normalized energy density 
		LinkedList<Data> norm_datapoints = normalisedValues(base);
		logger.info("NORMALISED:" );
//		Print.getInstance().printDataLinkedList(this, 100);
//		Print.getInstance().printDataLinkedList(norm_datapoints, 100);
		double energy = 0;
		Iterator<Data> it = norm_datapoints.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			energy += Math.pow(data.value(), 2);
		}
		logger.info("Energy:" + energy + "  Data Size:" + size());
		return energy;
	}
	
	public double energyNormalizedFactor(){
		return Math.pow(energyDensity(), 0.5);
	} 
	
	// Calculate the energy density of the time series
	public double energyDensity() {
		return energy() / size();
	}
	
	public double normalizedEnergyDensity(double base){
		return normalizedEnergy(base) / size();
	}
	
	
	//
	public TimeSeries normalisedValues(double base){
		return DataListOperator.getInstance().normalize(this, base);
	}
	
	public double maxValue(){
		// Find the Max value of data list
		double max = StatTool.getInstance().maxDataListAbsValue(this);
		logger.info("MAX: " + max);
		return max;
	}
	
	public double timeLength(){
		return this.peekLast().time() - this.peekFirst().time() + this.timeInterval();
	}
	
	public double timeInterval(){
		Iterator<Data> it = iterator();
		double t1 = 0, t2 = 0;
		try {
			t1 = it.next().time();
			t2 = it.next().time();
		} catch (Exception e) {
			logger.error("The input TS does not have enough length to get interval" + e);
			return 0;
		}
		return t2-t1;
	}

	// This is currently a simple solution, only considering the first instances to calculate 
	public double normalisedWhiteNoiseFreq() {
		return 1 / normalisedWhiteNoiseWaveLength();
	}

	public double normalisedWhiteNoiseWaveLength() throws ArithmeticException{
		if (this.size() < 3){
			logger.info("The Time Series is too short");
			return 0;
		}
		// The normalised wavelength is derived from two sampling points.
		return this.get(2).time() - this.get(0).time();
	}
}
