package mga.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import junit.framework.TestCase;

public class TestClusterManager extends TestCase {

	public void testGetClusterGrade(){
		NodeManager nmanager= getNodeManager();
		ClusterManager cmanager = new ClusterManager(nmanager, new SimpleCEstimator(), 0.5);
		Map<Integer, LinkedList<Integer>> clustergrade = cmanager.getClusterGrade(nmanager.getInitCluster());
		printClusterGrade(clustergrade);
	}
	
	private void printClusterGrade(Map<Integer, LinkedList<Integer>> clustergrade){
		Iterator<Integer> it = clustergrade.keySet().iterator();
		while(it.hasNext()){
			int clusterid = it.next();
			System.out.print("[" + clusterid + "]: ");
			Iterator<Integer> contant = clustergrade.get(clusterid).iterator();
			while(contant.hasNext()){
				System.out.print(contant.next() + " ");
			}
			System.out.println();
		}
	}
	
	private NodeManager getNodeManager(){
		NodeManager nmanager;
		Map<Integer, Node> nodemap = new HashMap<Integer, Node>();
		generateNodeMap(nodemap);
		nmanager = new NodeManager(nodemap);
		return nmanager;
	}
	
	private void generateNodeMap(Map<Integer, Node> nodemap) {
		for(int i = 1 ; i < 11 ; i++){
			Node node = new Node(i);
			for(int j = 0 ; j < 20 ; j++){
				node.addValue(i + j*10 + Math.random()*10);	
			}
			nodemap.put(i, node);
		}
	}
}
