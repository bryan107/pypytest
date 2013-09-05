package mga.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fileAccessInterface.PropertyAgent;

// TODO TEST FUNCTIONS
public class MGA {
	private static Log logger = LogFactory.getLog(MGA.class);
	// ------------ Data Structures --------------
	private NodeManager namanager;
	private ClusterManager clustermanager;
	private PropertyAgent agent;
	private double mincorrelation;
	private int coption;
	private int index;
	// -------------------------------------------

	public MGA(Map<Integer, Node> nodemap) {
		// init objects
		namanager = new NodeManager(nodemap);
		clustermanager = new ClusterManager(namanager, null, 0);
		// load properties to objects
		loadProperties();
		updateCorrelation();
		updateMinCorrelation();
	}

	public void loadProperties() {
		// Setup property file location
		agent = new PropertyAgent("conf");
		// Load properties
		coption = Integer.valueOf(agent.getProperties("MGA",
				"CorrelationEstimator"));
		mincorrelation = Double.valueOf(agent.getProperties("MGA",
				"MinCorrelation"));
		//
	}

	public void updateNodeMap(Map<Integer, Node> nodemap) {
		namanager.updateNodeMap(nodemap);
	}

	public void updateCorrelation() {
		CorrelationEstimator c;
		switch (this.coption) {
		case 1:
			c = new SimpleCEstimator();
			break;
		default:
			c = new SimpleCEstimator();
			break;
		}
		clustermanager.updateCorrelation(c);
	}

	public void updateCorerlation(CorrelationEstimator c) {
		clustermanager.updateCorrelation(c);
	}

	public void updateMinCorrelation() {
		clustermanager.updateMinCorrelation(this.mincorrelation);
	}

	public void updateMinCorrelation(double mincorrelation) {
		clustermanager.updateMinCorrelation(mincorrelation);
	}

	// ------------------ Main Functions ---------------------/
	// Clustering for "level" times. The cluster size is 2^level
	public Map<Integer, LinkedList<Integer>> clustering(int level) {
		Map<Integer, LinkedList<Integer>> cluster = namanager.getInitCluster();
		for (int i = level; i > 0; i--) {
			cluster = clustering(cluster);
		}
		return cluster;
	}

	// Perform one MGA clustering algorithm. Each clustering level can be put
	// into this function.
	public Map<Integer, LinkedList<Integer>> clustering(
			Map<Integer, LinkedList<Integer>> cluster) {
		Map<Integer, LinkedList<Integer>> clustergrade = clustermanager
				.getClusterGrade(cluster);
		Map<Integer, LinkedList<Integer>> result = pairing(cluster,
				clustergrade);
		return result;
	}

	// ------------------------------------------------------------ /
	// TODO current version does not consider the minimum requirement of
	// correlation strength between clusters
	public Map<Integer, LinkedList<Integer>> pairing(
			Map<Integer, LinkedList<Integer>> cluster,
			Map<Integer, LinkedList<Integer>> clustergrade) {
		Map<Integer, LinkedList<Integer>> pairings = new HashMap<Integer, LinkedList<Integer>>();
		index = 0;
		while (clustergrade.size() > 1) {
			serachPair(cluster, clustergrade, pairings);
		}
		if (clustergrade.size() != 0) {
			logger.info(clustergrade.size() + " residual cluster exist");
			// TODO pair the residuals with other clustered clusters.
		}
		return pairings;
	}

	// The main function that iterate through the whole cluster until all
	// clusters are paired
	public void serachPair(Map<Integer, LinkedList<Integer>> cluster,
			Map<Integer, LinkedList<Integer>> clustergrade,
			Map<Integer, LinkedList<Integer>> pairings) {
		Iterator<Integer> it = clustergrade.keySet().iterator();
		while (it.hasNext()) {
			int id = it.next();
			// If cluster 1 has no any other correlated cluster
			if (clustergrade.get(id).isEmpty()) {
				continue;
			}
			// If cluster 2 has no any other correlated cluster
			int id_2 = clustergrade.get(id).peek();
			if (!clustergrade.containsKey(id_2)) {
				clustergrade.get(id).remove();
				continue;
			}
			
			if(clustergrade.get(id_2).isEmpty()){
				clustergrade.get(id).remove();
				continue;
			}
			// Check whether the first priority of nodeid_1 is successfully
			// paired.
			if (id == clustergrade.get(id_2).peek()) {
				LinkedList<Integer> temp = new LinkedList<Integer>();
				temp.addAll(cluster.get(id));
				temp.addAll(cluster.get(id_2));
				pairings.put(index, temp);
				index++;
				clustergrade.get(id).clear();
				clustergrade.get(id_2).clear();
			}
		}
		removeEmptyClusters(clustergrade);
	}

	private void removeEmptyClusters(Map<Integer, LinkedList<Integer>> clustergrade) {
		LinkedList<Integer> emptycluster = new LinkedList<Integer>();
		// Search for empty clusters (which has been successfully paired)
		Iterator<Integer> it = clustergrade.keySet().iterator();
		while(it.hasNext()){
			int id = it.next();
			if(clustergrade.get(id).isEmpty()){
				emptycluster.add(id);
			}
		}
		// Remove empty clusters from clustergrade (for further pairing).
		while(!emptycluster.isEmpty()){
			int id = emptycluster.remove();
			clustergrade.remove(id);
		}
	}
	// Sort each node with their own priority
}
