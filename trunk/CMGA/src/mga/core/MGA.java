package mga.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import flanagan.analysis.Stat;

public class MGA {

	private Map<Integer, Node> nodemap = new HashMap<Integer, Node>();
	private Map<Integer, Map<Integer, Double>> correlationmap = new HashMap<Integer, Map<Integer, Double>>();

	public MGA(Map<Integer, Node> nodemap) {
		updateNodeMap(nodemap);
	}

	public void updateNodeMap(Map<Integer, Node> nodemap) {
		this.nodemap = nodemap;
	}

	public void generateCS() {
		Iterator<Integer> it1 = nodemap.keySet().iterator();
		while (it1.hasNext()) {
			int id1 = it1.next();
			Map<Integer, Double> subcsmap = new HashMap<Integer, Double>();
			Iterator<Integer> it2 = nodemap.keySet().iterator();
			while (it2.hasNext()) {
				int id2 = it2.next();
				if (id1 == id2) {
					continue;
				}
				double[] values1 = linkedListToDoubleArray(nodemap.get(id1)
						.values());
				double[] values2 = linkedListToDoubleArray(nodemap.get(id2)
						.values());
				double correlation = Stat.corrCoeff(values1, values2);
				subcsmap.put(id2, correlation);
			}
			correlationmap.put(id1, subcsmap);
		}
	}

	public void clustering() {

	}

	public Map<Integer, LinkedList<Integer>> pairing(
			Map<Integer, LinkedList<Integer>> cluster,
			Map<Integer, LinkedList<Integer>> clustergrade) {
		Map<Integer, LinkedList<Integer>> pairings = new HashMap<Integer, LinkedList<Integer>>();
		int index = 0;
		while (clustergrade.size() != 0) {
			serachPair(cluster, clustergrade, pairings, index);
		}
		return pairings;
	}

	private void serachPair(Map<Integer, LinkedList<Integer>> cluster,
			Map<Integer, LinkedList<Integer>> clustergrade,
			Map<Integer, LinkedList<Integer>> pairings, int index) {
		Iterator<Integer> it = clustergrade.keySet().iterator();
		while (it.hasNext()) {
			int id = it.next();
			if (clustergrade.get(id).peek() == null) {
				clustergrade.get(id).remove();
				continue;
			}
			int id_2 = clustergrade.get(id).peek();
			if (clustergrade.get(id_2).peek() == null) {
				continue;
			}
			// Check whether the first priority of nodeid_1 is successfully
			// paired.
			if (id == clustergrade.get(id_2).peek()) {
				LinkedList<Integer> temp = new LinkedList<Integer>();
				temp.addAll(cluster.get(id));
				temp.addAll(cluster.get(id_2));
				pairings.put(index, temp);
				clustergrade.remove(id);
				clustergrade.remove(id_2);
			}
		}
	}

	// Sort each node with their own priority

	// TODO the grading function
	private Map<Integer, LinkedList<Integer>> getClusterGrade(
			Map<Integer, LinkedList<Integer>> cluster) {
		// ---------- Data structures ----------
		Map<Integer, Map<Integer, Double>> clustercorrelation = new HashMap<Integer, Map<Integer,Double>>();
		Map<Integer, LinkedList<Integer>> clustergrade = new HashMap<Integer, LinkedList<Integer>>();
		// --------------------------------------
		//----------- Generates Cluster Correlation Map ------------- 
		Iterator<Integer> it1 = cluster.keySet().iterator();
		while (it1.hasNext()) {
			int cluster1 = it1.next();
			Iterator<Integer> it2 = cluster.keySet().iterator();
			Map<Integer, Double> subcmap = new HashMap<Integer, Double>();
			while(it2.hasNext()){
				int cluster2 = it2.next();
				if(cluster1 != cluster2){
					subcmap.put(cluster2, calcCorrelation(cluster.get(cluster1), cluster.get(cluster2)));
				}
			}
			clustercorrelation.put(cluster1, subcmap);
		}
		// ----------------------------------------------------------
		// ------------- Cluster Correlation Grading ----------------
		
		// ----------------------------------------------------------
		return clustergrade;
	}
	
	// Simply accumulate the correlation between each nodes in the two clusters.
	// TODO May need to include further requirements.
	private double calcCorrelation(LinkedList<Integer> cluster1, LinkedList<Integer> cluster2){
		double result = 0;
		Iterator<Integer> it1 = cluster1.iterator();
		while(it1.hasNext()){
			int nodeid1 = it1.next();
			Iterator<Integer> it2 = cluster2.iterator();
			while(it2.hasNext()){
				int nodeid2 = it2.next();
				result += correlationmap.get(nodeid1).get(nodeid2);
			}
		}
		return result;
	}

	// Sort Map in a Decent order
	private LinkedList<Integer> sortGrade(Map<Integer, Double> clustergrade) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		Map<Double, Integer> invertgrade = new HashMap<Double, Integer>();
		// invert grade exchanges the keys & values of clustergrade
		double[] correlation = new double[clustergrade.size()];
		int i = 0;
		Iterator<Integer> it = clustergrade.keySet().iterator();
		while (it.hasNext()) {
			int clusterid = it.next();
			invertgrade.put(clustergrade.get(clusterid), clusterid);
			correlation[i] = clustergrade.get(clusterid);
			i++;
		}
		// Sort the correlation array in an ascent order
		Arrays.sort(correlation);
		// Put node id with regard of the value of correlation in a decent order
		for (int j = (correlation.length - 1); j >= 0; j--) {
			list.add(invertgrade.get(correlation[j]));
		}
		return list;
	}

	private double[] linkedListToDoubleArray(LinkedList<Double> list) {
		double[] array = new double[list.size()];
		for (int index = 0; index < list.size(); index++) {
			array[index] = list.get(index);
		}
		return array;
	}

}
