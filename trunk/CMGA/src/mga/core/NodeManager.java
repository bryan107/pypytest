package mga.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;

// this class performs as a database of both node readings and correlation between nodes.
// The correlation estimation function can be extent to other more specific methods.

public class NodeManager {
	// ------------------- Logger---------------------------- //
	private static Log logger = LogFactory.getLog(NodeManager.class);
	// ------------------ Data Structure -------------------- //
	private Map<Integer, Node> nodemap = new HashMap<Integer, Node>();
	private Map<Integer, Map<Integer, Double>> correlationmap = new HashMap<Integer, Map<Integer, Double>>();

	// ------------------------------------------------------ //
	public NodeManager(Map<Integer, Node> nodemap){
		updateNodeMap(nodemap);
	}
	
	public void updateNodeMap(Map<Integer, Node> nodemap) {
		this.nodemap = nodemap;
		calcCorrelationMap();
		logger.info("Correlation Map updated");
	}
	
	public double getCorrelation(int id1, int id2){
		try {
			return correlationmap.get(id1).get(id2);
		} catch (Exception e) {
			logger.warn("Correlation of Node(" + id1 + ") and Node(" + id2 + ") does not exist");
			return 0;
		}
		
	}
	
	public Map<Integer, LinkedList<Integer>> getInitCluster(){
		Map<Integer, LinkedList<Integer>> cluster = new HashMap<Integer, LinkedList<Integer>>();
		Iterator<Integer> it = nodemap.keySet().iterator();
		int i = 0;
		while(it.hasNext()){
			int key = it.next();
			LinkedList<Integer> list = new LinkedList<Integer>();
			list.add(nodemap.get(key).id());
			cluster.put(i, list);
			i++;
		}
		
		return cluster;
	}
	
	private void calcCorrelationMap() {
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
	
	private double[] linkedListToDoubleArray(LinkedList<Double> list) {
		double[] array = new double[list.size()];
		for (int index = 0; index < list.size(); index++) {
			array[index] = list.get(index);
		}
		return array;
	}
	

	
	
}
