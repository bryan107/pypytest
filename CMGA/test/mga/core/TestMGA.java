package mga.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import junit.framework.TestCase;

public class TestMGA extends TestCase {

	public void testClustering(){
		Map<Integer, Node> nodemap = generateNodeMap();
		MGA mga = new MGA(nodemap);
		System.out.println("Level 1:");
		Map<Integer, LinkedList<Integer>> cluster = mga.clustering(1);
		printCluster(cluster);
		System.out.println("Level 2:");
		cluster = mga.clustering(2);
		printCluster(cluster);
		System.out.println("Level 3:");
		cluster = mga.clustering(3);
		printCluster(cluster);
		System.out.println("Level 4:");
		cluster = mga.clustering(4);
		printCluster(cluster);

	}
	
	private void printCluster(Map<Integer, LinkedList<Integer>> cluster){
		Iterator<Integer> it = cluster.keySet().iterator();
		while(it.hasNext()){
			int clusterid = it.next();
			System.out.print("[" + clusterid + "]:");
			Iterator<Integer> contant = cluster.get(clusterid).iterator();
			while(contant.hasNext()){
				System.out.print(contant.next() + " ");
			}
			System.out.println();
		}
	}
	
	private Map<Integer, Node> generateNodeMap() {
		Map<Integer, Node> nodemap = new HashMap<Integer, Node>();
		for(int i = 1 ; i < 21 ; i++){
			Node node = new Node(i);
			for(int j = 0 ; j < 20 ; j++){
				node.addValue(i + j*10 + Math.random()*10);	
			}
			nodemap.put(i, node);
		}
		return nodemap;
	}
	
//	public void testLinkedListToDoubleArray(){
//		LinkedList<Double> list = new LinkedList<Double>();
//		for(int i = 0 ; i < 10 ; i++){
//			list.add((double)i);
//		}
//		
//		
//		double[] array = new double[list.size()];
//		for(int index = 0 ; index < list.size() ; index++){
//			array[index] = list.get(index);
//		}
//		
//		System.out.println("Array Content");
//		
//		for(int i = 1 ; i < array.length ; i++){
//			System.out.print(array[i] + " ");
//		}
//	}
	
}
