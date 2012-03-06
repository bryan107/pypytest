package caculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JustTest {
	private static List<Integer> nodeindex = new ArrayList<Integer>(); 
	private static Map<Integer, Integer> nodemap = new HashMap<Integer, Integer>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		nodemap.put(12, 12);
		nodemap.put(13, 12);
		nodemap.put(11, 12);
		nodemap.put(18, 12);
		nodemap.put(15, 12);
		System.out.println("Size = " + nodemap.size());
		System.out.println();
		for(int i : nodemap.values()){
			System.out.print(i +" ");
		}
		
		
		
		
		
		
		
		
		
		
//		for(int i = 0 ; i < 10 ; i++){
//			nodeindex.add(i, i*5);
//		}
//		nodeindex.add(50);
//		nodeindex.add(50);
//		nodeindex.remove(5);
//		
//		System.out.print("This is good: ");
//		for(int i : nodeindex){
//			System.out.print(i + " ");
//		}	
//		
//		nodeindex.add(50);
//		
//		
//		System.out.print("This is good: ");
//		for(int i = 0 ; i < 12 ; i++){
//			System.out.print(nodeindex.get(i) + " ");
//		}	
//		System.out.println("");
//		System.out.println("size:" + nodeindex.size());
		
	}
	
	public void addNode(int nodename){
		nodeindex.add(nodename);
	}
	public void removeNode(){

	}

}
