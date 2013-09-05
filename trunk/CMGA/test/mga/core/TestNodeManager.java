package mga.core;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class TestNodeManager extends TestCase {

	private NodeManager nmanager;
	
	public void testNodeManager(){
		Map<Integer, Node> nodemap = new HashMap<Integer, Node>();
		generateNodeMap(nodemap);
		nmanager = new NodeManager(nodemap);
	}
	
	public void testGetCorrelatoin(){
		DecimalFormat df = new DecimalFormat("00.000");
		testNodeManager();
		for(int id1 = 1 ; id1 < 11 ; id1++){
			System.out.print("[" + id1 + "]: ");
			for(int id2 = 1 ; id2 < 11 ; id2++){
				if(id1 == id2) {
					continue;
				}
				double value = nmanager.getCorrelation(id1, id2);
				System.out.print("(" + id2 + ")" + df.format(value)  + " ");
			}
			System.out.println();
		}
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
