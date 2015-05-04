package mfdr.math.motif;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.DataListOperator;

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
		Map<Integer, LinkedList<Integer>> matchpool = new HashMap<Integer, LinkedList<Integer>>();
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
			Map<Integer, LinkedList<Integer>> matchpool) {
		int compensation = 1;
		if(lastsubsigavailable)
			compensation = 0;
		for(int i = 0 ; i < k && i < subsignals.size() - compensation; i++){
			Integer current_best_location = -1;
			Integer current_best_num = 0;
			// Get current level 1-Motif
			current_best_location = findBestMatchMotif(matchpool,
					current_best_location, current_best_num);
			// If no motif can be extracted.
			if(current_best_location == -1)
				break;
			// Save the 1-Motif matchs in the map
			matchpool = saveBestMatchMotif(kmotif, matchpool, current_best_location);
		}
	}

	private void establishMatchPool(double threshold,
			Map<Integer, LinkedList<Integer>> matchpool, Distance d) {
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

	private void initMatchPool(Map<Integer, LinkedList<Integer>> matchpool) {
		int compensation = 1;
		if(lastsubsigavailable)
			compensation = 0;
		// Ensure that the last motif (which may not contain enough data) is not included.
		for(int i = 0 ; i < subsignals.size() - compensation ; i++){
			matchpool.put(i, new LinkedList<Integer>());
		}
	}

	/*
	 * These are implementations that serve extractKMotif()
	 */
	private Integer findBestMatchMotif(
			Map<Integer, LinkedList<Integer>> matchpool,
			Integer current_best_location, Integer current_best_num) {
		Iterator<Integer> it = matchpool.keySet().iterator();
		while (it.hasNext()) {
			Integer index = (Integer) it.next();
			if(matchpool.get(index).size() > current_best_num){
				// Here use the number of matched motifs, which may be duplicated.
				current_best_location = index;
				current_best_num = matchpool.get(index).size();
			}
		}
		return current_best_location;
	}

	private Map<Integer, LinkedList<Integer>> saveBestMatchMotif(LinkedList<LinkedList<Integer>> kmotif,
			Map<Integer, LinkedList<Integer>> matchpool,
			Integer current_best_location) {
		LinkedList<Integer> motifs = new LinkedList<Integer>();
		motifs.add(current_best_location);
		
		Iterator<Integer> it = matchpool.get(current_best_location).iterator();
		// Add match segments to motifs
		while (it.hasNext()) {
			motifs.add(it.next());
		}
		kmotif.add(motifs);
		// Remove this motif from the match pool
		removeElement(matchpool, current_best_location);
		return matchpool;
	}
	
	/*
	 * Remove element from map
	 * 1. Remove each element of the given motif from other motifs.
	 * 2. Remove the lists of each element of the given motif.
	 * 3. Remove the given motif centered element list.
	 */
	private void removeElement(Map<Integer, LinkedList<Integer>> map, int index){
		// iterator through the index elements.
		Iterator<Integer> it = map.get(index).iterator();
		while (it.hasNext()) {
			// First element
			Integer index1= (Integer) it.next();
			// Remove this element from other lists.
			Iterator<Integer> it2 = map.get(index1).iterator();
			while (it2.hasNext()) {
				Integer index2 = (Integer) it2.next();
				if(index2 == index)
					continue;
				map.get(index2).removeAll(Collections.singleton(index1));
			}
			// Remove the element list from map
			map.remove(index1);
		}
		map.remove(index);
	}

	
}
