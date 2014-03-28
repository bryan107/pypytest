package dmga.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class HyperGraph {
	private Map<Integer, LinkedList<Integer>> hypernodes;
	private Map<Integer, Map<Integer, Double>> hyperlinks;
	private Map<Integer, Map<Integer, Double>> originallinks;
	private LinkedList<Integer> residues;
	private double minlinkweight;

	public HyperGraph(Map<Integer, LinkedList<Integer>> hypernodes,
			Map<Integer, Map<Integer, Double>> originallinks, double minlinkweight) {
		hyperlinks = new HashMap<Integer, Map<Integer, Double>>();
		residues = new LinkedList<Integer>();
		updateOriginalLinks(originallinks);
		updateMinLinkeWeight(minlinkweight);
		updateHyperNode(hypernodes);
	}

	public void updateOriginalLinks(
			Map<Integer, Map<Integer, Double>> originallinks) {
		this.originallinks = originallinks;
	}

	public void updateHyperNode(Map<Integer, LinkedList<Integer>> hypernodes) {
		this.hypernodes = hypernodes;
		updateHyperLinks();
	}
	
	public void updateMinLinkeWeight(double minlinkweight){
		this.minlinkweight = minlinkweight;
	}

	public void addResidues(int nodeid){
		residues.add(nodeid);
	}
	
	public LinkedList<Integer> residues(){
		return residues;
	}
	
	public Map<Integer, Map<Integer, Double>> originalLinks(){
		return originallinks;
	}	
	private void updateHyperLinks(){
		hyperlinks.clear();
		Iterator<Integer> it = hypernodes.keySet().iterator();
		while(it.hasNext()){
			int hypernodeid_1 = it.next();
			Map<Integer, Double> temp = new HashMap<Integer, Double>();
			Iterator<Integer> it2 = hypernodes.keySet().iterator();
			while(it2.hasNext()){
				double weight = 0;
				int count = 0;
				int hypernodeid_2 = it2.next();
				if(hypernodeid_1 == hypernodeid_2){ // Skip the same hypernode
					continue;
				}
				accumulateLinkWeight(hypernodeid_1,temp, weight, count, hypernodeid_2);
			}
			hyperlinks.put(hypernodeid_1, temp);
		}
	}

	private void accumulateLinkWeight(int hypernodeid_1,
			Map<Integer, Double> temp, double weight, int count,
			int hypernodeid_2) {
		Iterator<Integer> itsub1 = hypernodes.get(hypernodeid_1).iterator(); // Accumulate weights
		while(itsub1.hasNext()){
			int nodeid_1 = itsub1.next();
			Iterator<Integer> itsub2 = hypernodes.get(hypernodeid_2).iterator();
			while(itsub2.hasNext()){
				int nodeid_2 = itsub2.next();
				if(originallinks.get(nodeid_1).get(nodeid_2) >= minlinkweight){
					weight+= originallinks.get(nodeid_1).get(nodeid_2);
					count++;
				} else{
					weight = -1; // link does not exist.
					break;
				}
			}
			if(weight < 0) // If link does node exist.
				break;
		}
		// Add weight to hyperlink between hypernode 1 and hypernode 2.
		// If weight < 0, not all original links are > than minlinkweight, than weight will < 0.
		if(weight < 0){
			temp.put(hypernodeid_2, weight);
		} else{
			temp.put(hypernodeid_2, (weight/count));
		}
		
	}

	public Map<Integer, LinkedList<Integer>> hyperNodes() {
		return hypernodes;
	}

	public Map<Integer, Map<Integer, Double>> hyperLinks() {
		return hyperlinks;
	}
}
