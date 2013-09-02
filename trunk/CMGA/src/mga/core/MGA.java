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
	private NodeManager cmanager;
	private ClusterManager clustermanager;
	private PropertyAgent agent;
	private double mincorrelation;
	private int coption;
	// -------------------------------------------

	public MGA(Map<Integer, Node> nodemap) {
		// init objects
		cmanager = new NodeManager(nodemap);
		clustermanager = new ClusterManager(cmanager, null, 0);
		// load properties to objects
		loadProperties();
		updateCorrelation();
		updateMinCorrelation();
	}
	
	public void loadProperties(){
		// Setup property file location
		agent = new PropertyAgent("conf");
		// Load properties
		coption = Integer.valueOf(agent.getProperties("MGA", "Correlation"));
		mincorrelation = Double.valueOf(agent.getProperties("MGA", "MinCorrelation"));
		//
	}
	
	public void updateNodeMap(Map<Integer, Node> nodemap) {
		cmanager.updateNodeMap(nodemap);
	}

	public void updateCorrelation(){
		Correlation c;
		switch(this.coption){
			case 1:
				c = new SimpleCorrelation();
				break;
			default:
				c = new SimpleCorrelation();
				break;
		}
		clustermanager.updateCorrelation(c);
	}
	
	public void updateCorerlation(Correlation c){
		clustermanager.updateCorrelation(c);
	}
	
	public void updateMinCorrelation(){
		clustermanager.updateMinCorrelation(this.mincorrelation);
	}
	
	public void updateMinCorrelation(double mincorrelation) {
		clustermanager.updateMinCorrelation(mincorrelation);
	}

	// ------------------ Main Functions ---------------------/
	// Clustering for "level" times. The cluster size is 2^level
	public Map<Integer, LinkedList<Integer>> clustering(int level){
		Map<Integer, LinkedList<Integer>> cluster = cmanager.getInitCluster();
		for(int i = level; i >= 0 ; i--){
			cluster = clustering(cluster);
		}
		return cluster;
	}
	
	// Perform one MGA clustering algorithm. Each clustering level can be put into this function.
	public Map<Integer, LinkedList<Integer>> clustering(Map<Integer, LinkedList<Integer>> cluster) {
		return pairing(cluster, clustermanager.getClusterGrade(cluster));
	}

	// ------------------------------------------------------------ /
	// TODO current version does not consider the minimum requirement of
	// correlation strength between clusters
	private Map<Integer, LinkedList<Integer>> pairing(
			Map<Integer, LinkedList<Integer>> cluster,
			Map<Integer, LinkedList<Integer>> clustergrade) {
		Map<Integer, LinkedList<Integer>> pairings = new HashMap<Integer, LinkedList<Integer>>();
		int index = 0;
		while (clustergrade.size() > 1) {
			serachPair(cluster, clustergrade, pairings, index);
		}
		if (clustergrade.size() != 0) {
			logger.info(clustergrade.size() + " residual cluster exist");
			// TODO pair the residuals with other clustered clusters.
		}
		return pairings;
	}

	// The main function that iterate through the whole cluster until all
	// clusters are paired
	private void serachPair(Map<Integer, LinkedList<Integer>> cluster,
			Map<Integer, LinkedList<Integer>> clustergrade,
			Map<Integer, LinkedList<Integer>> pairings, int index) {
		Iterator<Integer> it = clustergrade.keySet().iterator();
		while (it.hasNext()) {
			int id = it.next();
			// If cluster 1 has no any other correlated cluster
			if (clustergrade.get(id).peek() == null) {
				clustergrade.get(id).remove();
				continue;
			}
			// If cluster 1 has no any other correlated cluster
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
}
