package mdfr.math.motif;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.distance.Distance;
import mdfr.distance.EuclideanDistance;
import mdfr.utility.DataListOperator;

public class Motif {
	private static Log logger = LogFactory.getLog(Motif.class);
	private LinkedList<TimeSeries> subsignals = new LinkedList<TimeSeries>();
	private double windowsize;
	private TimeSeries ts;
	private boolean lastsubsigavailable = true;
	private final short VALUE = 1;
	
	// Constructor
	public Motif(TimeSeries ts, double windowsize){
		this.ts = ts;
		this.windowsize = windowsize;
		updateSubSignals();
	}
	
	/** Initial function
	 * Here the input time series (can be an IMF) is separated into several signals at a given window size
	 * and stored as a Linkedlist of TimeSeries objects.
	 */ 
	// update Time Series
	public void updateTimeSeries(TimeSeries ts){
		this.ts = ts;
		updateSubSignals();
	}
	
	// update Window Size
	public void updateWindowSize(double windowsize){
		this.windowsize = windowsize;
		updateSubSignals();
	}
	
	// update subsignals
	private void updateSubSignals(){
		double subsignallength = windowsize / ts.timeInterval();
		// Check if the last sub-signal has enough length as its comparable counterparts.
		if(windowsize % ts.timeInterval() != 0){
			logger.info("Window size does not match time interval");
			lastsubsigavailable = false;
		}
		// Move values in Time Series ts into subsignals 
		Iterator<Data> it = ts.iterator();
		while (it.hasNext()) {
			TimeSeries temp = new TimeSeries();
			for(int i = 0 ; i < subsignallength ; i++){
				// Check if the last sub-signal has enough length as its comparable counterparts.
				if(!it.hasNext()){
					logger.info("The length of input time series does not perfectly match the window size");
					break;
				}
				temp.add((Data) it.next());
			}
			subsignals.add(temp);
		}
	}
	
	/**
	 * Supported public functions
	 */
	public TimeSeries getSubSignal(int index){
		return subsignals.get(index);
	}
	
	public int getSubSignalNum(){
		return subsignals.size();
	}
	
	/**
	 * This is a brute-force solution of standard K-Motifs algorithm
	 **/
	public LinkedList<LinkedList<Integer>> getKMotifs(int k, double threshold){
		LinkedList<LinkedList<Integer>> kmotif = new LinkedList<LinkedList<Integer>>();
		LinkedList<LinkedList<Integer>> matchpool = new LinkedList<LinkedList<Integer>>();
		Distance d = new EuclideanDistance();
		// 1. Initiate pool instances.
		initMatchPool(matchpool);
		// 2. Generate match pool
		establishMatchPool(threshold, matchpool, d);
		// 3. Extract K-Motif
		extractKMotif(k, kmotif, matchpool);
		return kmotif;
	}

	
	/* 
	 * These are implementations that serve getKMotifs()
	 */
	private void extractKMotif(int k, LinkedList<LinkedList<Integer>> kmotif,
			LinkedList<LinkedList<Integer>> matchpool) {
		boolean flag = false;
		int compensation = 1;
		if(lastsubsigavailable)
			compensation = 0;
		for(int i = 0 ; i < k && i < subsignals.size() - compensation; i++){
			Integer current_best_location = 0;
			Integer current_best_num = 0;
			// Get current level 1-Motif
			current_best_location = findBestMatchMotif(matchpool,
					current_best_location, current_best_num);
			// If not motif can be extracted.
			if(current_best_location == 0){
				if(flag){
					break;
				}
				flag = true;
			}
			// Save the 1-Motif matchs in the map
			matchpool = saveBestMatchMotif(kmotif, matchpool, current_best_location);
		}
	}

	private void establishMatchPool(double threshold,
			LinkedList<LinkedList<Integer>> matchpool, Distance d) {
		int compensation = 1;
		if(lastsubsigavailable)
			compensation = 0;
		for(int i = 0 ; i < subsignals.size() - compensation ; i++){
			for(int j = i + 1 ; j < subsignals.size() - 1 ; j++){
				double[] xx = DataListOperator.getInstance().linkedListToArray(subsignals.get(i), VALUE);
				double[] yy = DataListOperator.getInstance().linkedListToArray(subsignals.get(j), VALUE);
				// Here distance is normalised by motif length
				double distance = d.calDistance(xx, yy)/xx.length;
				if(distance <= threshold){
					matchpool.get(i).add(j);
					matchpool.get(j).add(i);
				}
			}
		}
	}

	private void initMatchPool(LinkedList<LinkedList<Integer>> matchpool) {
		// Ensure that the last motif (which may not contain enough data) is not included.
		for(int i = 0 ; i < subsignals.size() - 1 ; i++){
			matchpool.add(new LinkedList<Integer>());
		}
	}

	/*
	 * These are implementations that serve extractKMotif()
	 */
	private Integer findBestMatchMotif(
			LinkedList<LinkedList<Integer>> matchpool,
			Integer current_best_location, Integer current_best_num) {
		for(int j = 0 ; j < matchpool.size() ; j++){
			if(matchpool.get(j).size() > current_best_num){
				current_best_location = j;
				current_best_num = matchpool.get(j).size();
			}
		}
		return current_best_location;
	}

	private LinkedList<LinkedList<Integer>> saveBestMatchMotif(LinkedList<LinkedList<Integer>> kmotif,
			LinkedList<LinkedList<Integer>> matchpool,
			Integer current_best_location) {
		LinkedList<Integer> motifs = new LinkedList<Integer>();
		motifs.add(current_best_location);
		Iterator<Integer> it = matchpool.get(current_best_location).iterator();
		while (it.hasNext()) {
			Integer index = (Integer) it.next();
			// Add match to motifs
			motifs.add(index);
			
			/*
			 * K-Motif selection, can only use one of them
			 */
			
			// 1.Naive K-Motif
			//   Remove object current_best_location from others
//			matchpool.get(index).remove(current_best_location);
			
			// 2.Standard K-Motif proposed in KDD 02'
			//   Remove object that has been included in cluster of current_best_location.
			//   This ensures that there is no overlap between clusters.
			matchpool.get(index).clear();
		}
		matchpool.get(current_best_location).clear();
		kmotif.add(motifs);
		return matchpool;
	}
	
	

	
}
