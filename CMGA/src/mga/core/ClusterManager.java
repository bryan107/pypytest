package mga.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

//TODO TEST FUNCTIONS
public class ClusterManager {

	// --------------------- Variables ---------------------------/
	private NodeManager nmanager;
	private CorrelationEstimator correlation;
	private double mincorrelation;
	// --------------------- Constructor -------------------------/
	public ClusterManager(NodeManager nmanager, CorrelationEstimator cestimator, double mincorrelation){
		updateCManager(nmanager);
		updateCorrelation(cestimator);
	}
	// --------------------- Update Variables --------------------/
	
	public void updateCManager(NodeManager nmanager){
		this.nmanager = nmanager;
	}
	
	public void updateCorrelation(CorrelationEstimator correlation){
		this.correlation = correlation;
	}
	
	public void updateMinCorrelation(double mincorrelation){
		this.mincorrelation = mincorrelation;
	}
	// -------------------- Public Function ----------------------/
	public Map<Integer, LinkedList<Integer>> getClusterGrade(
			Map<Integer, LinkedList<Integer>> cluster) {
		// ---------- Data structures ----------
		Map<Integer, Map<Integer, Double>> clustercmap = new HashMap<Integer, Map<Integer,Double>>();
		Map<Integer, LinkedList<Integer>> clustergrade = new HashMap<Integer, LinkedList<Integer>>();
		// --------------------------------------
		//----------- Generates Cluster Correlation Map ------------- 
		Iterator<Integer> it1 = cluster.keySet().iterator();
		while (it1.hasNext()) {
			int id1 = it1.next();
			Map<Integer, Double> subcmap = generateCorrelations(cluster, id1);
			clustercmap.put(id1, subcmap);
		}
		// ----------------------------------------------------------
		// ------------- Cluster Correlation Grading ----------------
		Iterator<Integer> it = clustercmap.keySet().iterator();
		while(it.hasNext()){
			int id =  it.next();
			clustergrade.put(id, sortGrade(clustercmap.get(id)));
		}
		// ----------------------------------------------------------
		return clustergrade;
	}
	
	// -------------------- Private Function ---------------------/
	private Map<Integer, Double> generateCorrelations(
			Map<Integer, LinkedList<Integer>> cluster, int id1) {
		Iterator<Integer> it2 = cluster.keySet().iterator();
		Map<Integer, Double> subcmap = new HashMap<Integer, Double>();
		while(it2.hasNext()){
			int id2 = it2.next();
			if(id1 != id2){
				subcmap.put(id2, calcCorrelation(cluster.get(id1), cluster.get(id2)));
			}
		}
		return subcmap;
	}
	
	// Simply accumulate the correlation between each nodes in the two clusters.
	// TODO May need to include further requirements.


	// Sort Map in a Decent order
	private LinkedList<Integer> sortGrade(Map<Integer, Double> correlations) {
		LinkedList<Integer> clustergrade = new LinkedList<Integer>();
		Map<Double, Integer> invertcorrelations = new HashMap<Double, Integer>();
		// invert grade exchanges the keys & values of clustergrade
		double[] correlation = new double[correlations.size()];
		int i = 0;
		Iterator<Integer> it = correlations.keySet().iterator();
		while (it.hasNext()) {
			int clusterid = it.next();
			invertcorrelations.put(correlations.get(clusterid), clusterid);
			correlation[i] = correlations.get(clusterid);
			i++;
		}
		// Sort the correlation array in an ascent order
		Arrays.sort(correlation);
		// Put node id with regard of the value of correlation in a decent order
		for (int j = (correlation.length - 1); j >= 0; j--) {
			clustergrade.add(invertcorrelations.get(correlation[j]));
		}
		return clustergrade;
	}
	
	private double calcCorrelation(LinkedList<Integer> cluster1, LinkedList<Integer> cluster2){
		return correlation.calcCorrelation(nmanager, cluster1, cluster2, mincorrelation);
	}
}
