package dmga.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Matching {
	private static Log logger = LogFactory.getLog(Matching.class);
	public Matching() {
		
	}

	public HyperGraph doMatching(HyperGraph g) {
		Map<Integer, LinkedList<Integer>> matchednodes = new HashMap<Integer, LinkedList<Integer>>();
		int matchednodesindex = 0;
		int mapsize = g.hyperLinks().size()+1;
		// Iterate until all hyper-nodes are matched or no nodes can be matched
		while(g.hyperLinks().size() > 0 && g.hyperLinks().size() < mapsize){
			mapsize = g.hyperLinks().size();
			HyperLink bestlink = new HyperLink(0, 0, -1);
			// Iterate through link map to find max-weighted pair.
			matchIteration(g.hyperLinks(), bestlink);
			// If no link exist.
			if(bestlink.weight() < 0){
				continue;
			}
			// Add matched pair to final output
			matchednodesindex = updateMatchedNodes(g.hyperNodes(), matchednodes, matchednodesindex, bestlink);
			// Remove the best match from link map.
			removeMatchedPair(g, bestlink);
		}
		// TODO collecetResidues at the very end of all
		if(!g.hyperNodes().isEmpty()){ // Collect residues
			collectResidues(g);
		}
		g.updateHyperNode(matchednodes);
		return g;
	}

	public HyperGraph residueJoin(HyperGraph g){
		if(g.hyperNodes().isEmpty()){
			logger.warn("No Valid Hyper Node to Join");
			return g;
		}
		
		while(!g.residues().isEmpty()){
			HyperLink bestlink = new HyperLink(0, 0, -1);
			Iterator<Integer> it = g.residues().iterator();
			while(it.hasNext()){ // Iterate through each residue
				int residue = it.next();
				Iterator<Integer> it2 = g.hyperNodes().keySet().iterator();
				while(it2.hasNext()){ // Iterate through each hyper node
					int hypernodeid = it2.next();
					Iterator<Integer> it3 = g.hyperNodes().get(hypernodeid).iterator();
					// Accumulate the total weight between the residue and the hyper node.
					double weight = 0;
					int count = 0;
					while(it3.hasNext()){
						int nodeid = it3.next();
						weight += g.originalLinks().get(residue).get(nodeid);
						count++;
					}
					if((weight/count) > bestlink.weight()){
						bestlink.updateLink(residue, hypernodeid, (weight/count));
					}
				}
			}
			// Join the best pair.
			g.hyperNodes().get(bestlink.node()[1]).add(bestlink.node()[0]);
			// Remove paired residue
			int index = 0;
			it = g.residues().iterator();
			while(it.hasNext()){
				int residue = it.next();
				if(residue == bestlink.node()[0]){
					g.residues().remove(index);
					break;
				}
				index++;
			}
			g.updateHyperNode(g.hyperNodes());
		}
		
		return g;
	}
	
	// VLDB version.
	private void collectResidues(HyperGraph g) {
		Iterator<Integer> it = g.hyperNodes().keySet().iterator();
		while(it.hasNext()){
			try {
				int hypernodeid = it.next();
				g.residues().addAll(g.hyperNodes().get(hypernodeid));
			} catch (Exception e) {
				logger.error("GG" + e);
			}
		}
	}
	
	// Old version. Residues forms group
//	private void collectResidues(HyperGraph g,
//			Map<Integer, LinkedList<Integer>> matchednodes,
//			int matchednodesindex) {
//		Iterator<Integer> it = g.hyperNodes().keySet().iterator();
//		while(it.hasNext()){
//			try {
//				int hypernodeid = it.next();
//				matchednodes.put(matchednodesindex, g.hyperNodes().get(hypernodeid));
//				matchednodesindex++;
//			} catch (Exception e) {
//				logger.error("GG" + e);
//			}
//		}
//	}

	private int updateMatchedNodes(
			Map<Integer, LinkedList<Integer>> hypernodes,
			Map<Integer, LinkedList<Integer>> matchednodes,
			int matchednodesindex, HyperLink bestlink) {
		LinkedList<Integer> matchedpair = new LinkedList<Integer>();
		try {
			matchedpair.addAll(hypernodes.get(bestlink.node()[0]));
			matchedpair.addAll(hypernodes.get(bestlink.node()[1]));
			matchednodes.put(matchednodesindex, matchedpair);
			matchednodesindex++;
		} catch (Exception e) {
			logger.info("No Match found");
		}
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
				if(nodeid_i == nodeid_j){
					continue;
				}
				double weight = hyperlinks.get(nodeid_i).get(nodeid_j);
				if(weight > bestlink.weight())
					bestlink.updateLink(nodeid_i, nodeid_j, weight);
			}
		}
	}

	private void removeMatchedPair(HyperGraph g , HyperLink bestlink) {
		g.hyperNodes().remove(bestlink.node()[0]);
		g.hyperNodes().remove(bestlink.node()[1]);
		Iterator<Integer> it;
		g.hyperLinks().remove(bestlink.node()[0]);
		g.hyperLinks().remove(bestlink.node()[1]);
		it = g.hyperLinks().keySet().iterator();
		while(it.hasNext()){
			int nodeid_i = it.next();
			g.hyperLinks().get(nodeid_i).remove(bestlink.node()[0]);
			g.hyperLinks().get(nodeid_i).remove(bestlink.node()[1]);
		}
	}

}
