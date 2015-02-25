package mdfr.math.motif;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.distance.Distance;
import mdfr.distance.EuclideanDistance;
import mdfr.math.emd.DataListOperator;

public class Motif {
	private static Log logger = LogFactory.getLog(Motif.class);
	LinkedList<TimeSeries> motif = new LinkedList<TimeSeries>();
	double windowsize;
	private final short VALUE = 1;
	
	public Motif(TimeSeries ts, double windowsize){
		updateWindowSize(windowsize);
		updateTS(ts);
	}
	
	public void updateTS(TimeSeries ts){
		double indexsize = windowsize/getTimeInterval(ts);
		if(windowsize%getTimeInterval(ts) != 0){
			logger.info("Window size does not match time interval");
		}
		Iterator<Data> it = ts.iterator();
		while (it.hasNext()) {
			TimeSeries temp = new TimeSeries();
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
	public LinkedList<LinkedList<Integer>> getKMotifs(int k, double threshold){
		LinkedList<LinkedList<Integer>> kmotif = new LinkedList<LinkedList<Integer>>();
		LinkedList<LinkedList<Integer>> matchpool = new LinkedList<LinkedList<Integer>>();
		Distance d = new EuclideanDistance();
		
		// Initiate pool instances.
		// Ensure that the last motif (which may not contain enough data) is not included.
		for(int i = 0 ; i < motif.size() - 1 ; i++){
			matchpool.add(new LinkedList<Integer>());
		}
		
		// Generate match pool
		// Ensure that the last motif (which may not contain enough data) is not included.
		for(int i = 0 ; i < motif.size() - 1 ; i++){
			for(int j = i + 1 ; j < motif.size() - 1 ; j++){
				double[] xx = DataListOperator.getInstance().LinkedListToArray(motif.get(i), VALUE);
				double[] yy = DataListOperator.getInstance().LinkedListToArray(motif.get(j), VALUE);
				// If two motif is a non trivial match:
				// TODO test here distance is normalised by motif length
				double distance = d.calDistance(xx, yy)/xx.length;
				if(distance <= threshold){
					matchpool.get(i).add(j);
					matchpool.get(j).add(i);
				}
			}
		}
		// Extract K-Motif
		boolean flag = false;
		for(int i = 0 ; i < k && i < motif.size() - 1; i++){
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
	
	public TimeSeries getMotif(int index){
		return motif.get(index);
	}
	
	public int getMotifNum(){
		return motif.size();
	}
	
}
