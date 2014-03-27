package dmga.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Matching {
	private double minlinkweight;
	
	public Matching(double minlinkweight) {
		updateMinLinkWeight(minlinkweight);
	}
	
	public void updateMinLinkWeight(double minlinkweight){
		this.minlinkweight = minlinkweight;
	}

	public HyperGraph doMatching(HyperGraph g) {
		Map<Integer, LinkedList<Integer>> matchednodes = new HashMap<Integer, LinkedList<Integer>>();
		int matchednodesindex = 0;
		int mapsize = g.hyperLinks().size()+1;
		// Iterate until all hyper-nodes are matched or no nodes can be matched
		while(g.hyperLinks().size() > 0 && g.hyperLinks().size() < mapsize){
			mapsize = g.hyperLinks().size();
			HyperLink bestlink = new HyperLink(0, 0, minlinkweight);
			// Iterate through link map to find max-weighted pair.
			matchIteration(g.hyperLinks(), bestlink);
			// Remove the best match from link map.
			removeMatchedPair(g.hyperLinks(), bestlink);
			matchednodesindex = updateMatchedNodes(g.hyperNodes(), matchednodes, matchednodesindex, bestlink);
		}
		g.updateHyperNode(matchednodes);
		return g;
	}

	private int updateMatchedNodes(
			Map<Integer, LinkedList<Integer>> hypernodes,
			Map<Integer, LinkedList<Integer>> matchednodes,
			int matchednodesindex, HyperLink bestlink) {
		LinkedList<Integer> matchedpair = new LinkedList<Integer>();
		matchedpair.addAll(hypernodes.get(bestlink.node()[0]));
		matchedpair.addAll(hypernodes.get(bestlink.node()[1]));
		matchednodes.put(matchednodesindex, matchedpair);
		matchednodesindex++;
		return matchednodesindex;
	}

	private void matchIteration(Map<Integer, Map<Integer, Double>> hyperlinks,
			HyperLink bestlink) {
		Iterator<Integer> it = hyperlinks.keySet().iterator();
		while(it.hasNext()){
			int nodeid_i = it.next();
			Iterator<Integer> it2 = hyperlinks.get(nodeid_i).keySet().iterator();
			while(it2.hasNext()){
				int nodeid_j = it2.next();
				double weight = hyperlinks.get(nodeid_i).get(nodeid_j);
				if(weight > bestlink.weight())
					bestlink.updateLink(nodeid_i, nodeid_j, weight);
			}
		}
	}

	private void removeMatchedPair(
			Map<Integer, Map<Integer, Double>> hyperlinks, HyperLink bestlink) {
		Iterator<Integer> it;
		hyperlinks.remove(bestlink.node()[0]);
		hyperlinks.remove(bestlink.node()[1]);
		it = hyperlinks.keySet().iterator();
		while(it.hasNext()){
			int nodeid_i = it.next();
			hyperlinks.get(nodeid_i).remove(bestlink.node()[0]);
			hyperlinks.get(nodeid_i).remove(bestlink.node()[1]);
		}
	}

}
