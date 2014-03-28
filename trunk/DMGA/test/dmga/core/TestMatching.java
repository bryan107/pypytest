package dmga.core;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import junit.framework.TestCase;

public class TestMatching extends TestCase {

	private final double minlinkweight = 0.5;
	
	public void testDoMatching(){
		Matching m = new Matching();
		DMGA dmga = new DMGA(8, minlinkweight);
		HyperGraph g = generateGraph(32, 1);
		System.out.println("Before Matching");
		printHyperGraph(g);
		
		dmga.grouping(g);
		System.out.println("After Matching");
		printHyperGraph(g);
		
//		System.out.println("Before Matching");
//		printHyperGraph(g);
//		
//		System.out.println();
//		System.out.println("1st Matching");
//		g = m.doMatching(g);
//		printHyperGraph(g);
//		
//		System.out.println();
//		System.out.println("2st Matching");
//		g = m.doMatching(g);
//		printHyperGraph(g);
//		
//		System.out.println();
//		System.out.println("3st Matching");
//		g = m.doMatching(g);
//		printHyperGraph(g);
//	
//		g.updateMinLinkeWeight(0);
//		g = m.residueJoin(g);
//		System.out.println("Residue Join:");
//		printHyperGraph(g);
	}
	
	private HyperGraph generateGraph(int groupnumber , int nodesize){
		Map<Integer, LinkedList<Integer>> hypernodes = new HashMap<Integer, LinkedList<Integer>>();
		Map<Integer, Map<Integer, Double>> hyperlinks = new HashMap<Integer, Map<Integer, Double>>();
		// Generate Hypernodes
		for(int i = 0 ; i < groupnumber ; i++){
			LinkedList<Integer> l = new LinkedList<Integer>();
			for(int j = 0 ; j < nodesize ; j++){
				l.add(i*nodesize + j);
			}
			hypernodes.put(i, l);
		}
		
		// Generate HyperLinks
		for(int i = 0; i < groupnumber ; i++){
			Map<Integer, Double> tempmap = new HashMap<Integer, Double>();
			for(int j = 0 ; j < groupnumber ; j++){
				if(i == j){
					continue;
				}
				tempmap.put(j, Math.random()/2 + 0.48);
			}
			hyperlinks.put(i, tempmap);
		}
		// Return HyperGraph		
		return new HyperGraph(hypernodes, hyperlinks, minlinkweight);
	}
	
	private void printHyperGraph(HyperGraph g){
		DecimalFormat df = new DecimalFormat("0.000");
		System.out.println("HyperNodes:");
		// Print HyperNodes
		Iterator<Integer> it = g.hyperNodes().keySet().iterator();
		while(it.hasNext()){
			int hypernodeid = it.next();
			System.out.print("[" + hypernodeid + "]:{");
			Iterator<Integer> it2 = g.hyperNodes().get(hypernodeid).iterator();
			while(it2.hasNext()){
				int nodeid = it2.next();
				System.out.print(nodeid + ",");
			}
			System.out.print("} ");
		}
		System.out.print("[R]:");
		it = g.residues().iterator();
		while(it.hasNext()){
			System.out.print("{" + it.next() + "}");
		}
		
		
		System.out.println();
		System.out.println("HyperLinks:");
		//Print HyperLinks
		it = g.hyperLinks().keySet().iterator();
		while(it.hasNext()){
			int nodeid_i = it.next();
			System.out.print("[" + nodeid_i + "]:");
			Iterator<Integer> it2 = g.hyperLinks().get(nodeid_i).keySet().iterator();
			while(it2.hasNext()){
				int nodeid_j = it2.next();
				System.out.print("("+ nodeid_j +"):" + df.format(g.hyperLinks().get(nodeid_i).get(nodeid_j)) + " ");
			}
			System.out.println();
		}
	}
}
