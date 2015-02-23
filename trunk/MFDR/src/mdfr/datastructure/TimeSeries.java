package mdfr.datastructure;

import java.util.Iterator;
import java.util.LinkedList;

import mdfr.math.emd.DataListOperator;
import mdfr.utility.Print;

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
	
	// Calculate the energy density of the time series
	public double energyDensity() {
		return energy() / size();
	}
	
	public double normalizedEnergyDensity(){
		// Normalize IMF to acquire normalized energy density 
		LinkedList<Data> norm_datapoints = DataListOperator.getInstance().normalize(this);
		logger.info("NORMALISED:" );
		Print.getInstance().printDataLinkedList(norm_datapoints, 100);
		double energy = 0;
		Iterator<Data> it = norm_datapoints.iterator();
		while (it.hasNext()) {
			Data data = (Data) it.next();
			energy += Math.pow(data.value(), 2);
		}
		logger.info("Energy:" + energy + "  Data Size:" + size());
		return energy / size();
	}
}
