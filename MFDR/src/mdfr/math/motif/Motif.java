package mdfr.math.motif;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.datastructure.Data;
import mdfr.distance.Distance;
import mdfr.distance.EuclideanDistance;
import mdfr.math.emd.DataListOperator;

public class Motif {
	private static Log logger = LogFactory.getLog(Motif.class);
	LinkedList<LinkedList<Data>> motif = new LinkedList<LinkedList<Data>>();
	double windowsize;
	private final short VALUE = 1;
	
	public Motif(LinkedList<Data> ts, double windowsize){
		updateWindowSize(windowsize);
		updateTS(ts);
	}
	
	public void updateTS(LinkedList<Data> ts){
		double indexsize = windowsize/getTimeInterval(ts);
		if(windowsize%getTimeInterval(ts) != 0){
			logger.info("Window size does not match time interval");
		}
		Iterator<Data> it = ts.iterator();
		while (it.hasNext()) {
			LinkedList<Data> temp = new LinkedList<Data>();
			for(int i = 0 ; i < indexsize ; i++){
				if(!it.hasNext()){
					logger.info("The length of input time series does not perfectly match the window size");
					break;
				}
				temp.add((Data) it.next());
			}
			motif.add(temp);
		}
	}
	
	// TODO this is only for temporary use fix with new Time Series Object.
	private double getTimeInterval(LinkedList<Data> ts){
		Iterator<Data> it = ts.iterator();
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
	
	public void updateWindowSize(double windowsize){
		this.windowsize = windowsize;
	}
	
	/*
	 * This is a brute-force solution of standard K-Motif algorithm
	 */
	public LinkedList<LinkedList<Integer>> getKMotif(int k, double threshold){
		LinkedList<LinkedList<Integer>> kmotif = new LinkedList<LinkedList<Integer>>();
		LinkedList<LinkedList<Integer>> matchpool = new LinkedList<LinkedList<Integer>>();
		Distance d = new EuclideanDistance();
		
		// Initiate pool instances.
		for(int i = 0 ; i < motif.size() ; i++){
			matchpool.add(new LinkedList<Integer>());
		}
		
		// Generate match pool
		for(int i = 0 ; i < motif.size() ; i++){
			for(int j = i + 1 ; j < motif.size() ; j++){
				double[] xx = DataListOperator.getInstance().LinkedListToArray(motif.get(i), VALUE);
				double[] yy = DataListOperator.getInstance().LinkedListToArray(motif.get(j), VALUE);
				// If two motif is a non trivial match:
				double distance = d.calDistance(xx, yy);
				if(distance <= threshold){
					matchpool.get(i).add(j);
					matchpool.get(j).add(i);
				}
			}
		}
		
		// Extract K-Motif
		for(int i = 0 ; i < k ; i++){
			Integer current_best_location = 0;
			Integer current_best_num = 0;
			// Get current level 1-Motif
			current_best_location = findBestMatchMotif(matchpool,
					current_best_location, current_best_num);
			// Save the 1-Motif matchs in the map
			matchpool = saveBestMatchMotif(kmotif, matchpool, current_best_location);
		}
		return kmotif;
	}

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
			Integer integer = (Integer) it.next();
			// Add match to motifs
			motifs.add(integer);
			// Remove object current_best_location from others
			matchpool.get(integer).remove(current_best_location);
		}
		matchpool.get(current_best_location).clear();
		kmotif.add(motifs);
		return matchpool;
	}
	
	public LinkedList<Data> getMotif(int index){
		return motif.get(index);
	}
	
	public int getMotifNum(){
		return motif.size();
	}
	
}
