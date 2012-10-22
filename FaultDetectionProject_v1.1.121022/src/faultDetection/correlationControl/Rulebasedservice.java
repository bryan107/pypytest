package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Rulebasedservice {
	
	// Thresholds for Rule-based service-----------------------
	// Stuck-at Rule
	short STUCKmaxround;
	double STUCKminvariation;
	// NLDR Rule
	double NLDRupperbound, NLDRlowerbound;
	
	//--------------------------------------------------------
	// Fault Conditions
	private final short FT = 0;
//	private final short LF = 1;
//	private final short LG = 2;
//	private final short GD = 3;

	private static Log logger = LogFactory.getLog(Rulebasedservice.class);
	
	private Map<Integer, Double> previousreading = new HashMap<Integer, Double>();
	private Map<Integer, Short> stuckround = new HashMap<Integer, Short>();

	public Rulebasedservice(short round, double minvariation, double upperbound, double lowerbound) {
		updateMaxStuckRound(round);
		updateMinVariation(minvariation);
		updateNLDR(upperbound, lowerbound);
	}
	
	public void updateMinVariation(double minvariation){
		this.STUCKminvariation = minvariation;
	}

	public void updateMaxStuckRound(short round) {
		STUCKmaxround = round;
	}

	public void updateNLDR(double upperbound, double lowerbound) {
		NLDRupperbound = upperbound;
		NLDRlowerbound = lowerbound;
	}

	//TODO modify rules as objects
	public Map<Integer, Short> faultConditionMarking(
			Map<Integer, Double> readingpack,
			Map<Integer, Short> readingfaultcondition) {
		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> it = key.iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			checkStuckAt(readingpack, readingfaultcondition, nodeid);
			checkNLDR(readingpack, readingfaultcondition, nodeid);
		}
		return readingfaultcondition;
	}
	
	public void resetNodeCondition(int nodeid){
		try {
			stuckround.put(nodeid, (short) 0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void checkNLDR(Map<Integer, Double> readingpack,
			Map<Integer, Short> readingfaultcondition, int nodeid) {
		if(readingpack.get(nodeid) > NLDRupperbound || readingpack.get(nodeid) < NLDRlowerbound){
			logger.warn("NLDR Fault Occurs on node [" + nodeid + "] :" + readingpack.get(nodeid));
			readingfaultcondition.put(nodeid, FT);
		}
	}

	private void checkStuckAt(Map<Integer, Double> readingpack,
			Map<Integer, Short> readingfaultcondition, int nodeid) {
		if (previousreading.containsKey(nodeid)) {
			if (Math.abs(readingpack.get(nodeid)- previousreading.get(nodeid)) < STUCKminvariation ) {
				if(stuckround.get(nodeid) < STUCKmaxround)
					stuckround.put(nodeid, (short) (stuckround.get(nodeid) + 1));
			}
			else{
				previousreading.put(nodeid, readingpack.get(nodeid));
				stuckround.put(nodeid, (short) 1);
			}

		} else {
			previousreading.put(nodeid, readingpack.get(nodeid));
			stuckround.put(nodeid, (short) 1);
		}
		if(stuckround.get(nodeid) >= STUCKmaxround){
			logger.warn("Stuck-at Fault Occurs on node [" + nodeid + "] :" + readingpack.get(nodeid));
			readingfaultcondition.put(nodeid, FT);
		}
	}

}
