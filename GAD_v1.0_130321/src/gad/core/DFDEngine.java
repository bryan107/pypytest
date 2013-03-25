package gad.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public final class DFDEngine {
	//---------------------Private variables--------------------------
	//faulty conditions
	private final short FT = 0;
	private final short LF = 1;
	private final short LG = 2;
	private final short GD = 3;
	private final short UN = 4;
	private final int minimalneighbournumber = 3;
	//
	
	private double leastLGratio = 0.5;
	private double faultneighbourthreshold = 0.5;
//	private Map<Integer, Map<Integer, Boolean>> correlationtable = new HashMap<Integer, Map<Integer, Boolean>>();
	private Map<Integer, Short> anomalycondition = new HashMap<Integer, Short>();
	private Map<Integer, Short> finalanomalycondition = new HashMap<Integer, Short>();
	
	private static Log logger = LogFactory.getLog(DFDEngine.class);
	//---------------------------------------------------------------
	//-------------------------Constructor---------------------------
	public DFDEngine(){
	}
	//---------------------------------------------------------------
	//------------------------Public Functions-----------------------

	public void updateLeastLGratio(double leastLGratio){
		this.leastLGratio = leastLGratio;
	}
	
	public void updateFaultNeighbourThreshold(double faultneighbourthreshold){
		this.faultneighbourthreshold = faultneighbourthreshold;
	}
	
	public Map<Integer, Short> markCondition(Map<Integer, Map<Integer, Boolean>> correlationtable){
		setupLocalCorrelationTable(correlationtable);
		if(correlationtable.size() < minimalneighbournumber){
			logger.warn("Not enough references, unable to detect");
			return new HashMap<Integer, Short>();			
		}
		firstRoundVoting(correlationtable);
		secondRoundVoting(correlationtable);
		return finalanomalycondition;
	}
	//----------------------------------------------------------------
	//------------------------Private Functions-----------------------
	private void setupLocalCorrelationTable(Map<Integer, Map<Integer, Boolean>> correlationtable) {

		// Memorize DC-fault node and remove their entries
		List<Integer> nonGDnodelist = new ArrayList<Integer>();
		Iterator<Integer> iterator1= correlationtable.keySet().iterator();
		while(iterator1.hasNext()){
			int nodeid = iterator1.next();
			if(!SMDB.getInstance().getDeviceCondition(nodeid)){
				nonGDnodelist.add(nodeid);			// memorize DC-fault node
				correlationtable.remove(nodeid); 	// remove DC-fault entry
			}
		}
		// Remove DF-fault nodes from normal ones entries
		Iterator<Integer> iterator2= correlationtable.keySet().iterator();
		while(iterator2.hasNext()){
			int nodeid = iterator2.next();
			if(SMDB.getInstance().getDeviceCondition(nodeid)){
				for(int id : nonGDnodelist){
					correlationtable.get(nodeid).remove(id);
				}
			}
		}
	}
//
	private void firstRoundVoting(Map<Integer, Map<Integer, Boolean>> correlationtable){
//		logger.info("Start first voting / size = " + correlationstrengthtable.size());
		anomalycondition.clear();
		Iterator<Integer> iterator = correlationtable.keySet().iterator();
		for(Map<Integer, Boolean> node : correlationtable.values()){;
			int totalneighbournumber = 0;
			int goodneighbournumber = 0;
			//Count good neighbor number through all its correlation strength
			for(boolean correlation : node.values()){
				totalneighbournumber++;
				if(correlation){
					goodneighbournumber++;
				}
			}
			//setup up first voting result
			try {
				if(totalneighbournumber == 0){
					continue;
				}
				if ((double)goodneighbournumber/(double)totalneighbournumber >= faultneighbourthreshold) {//more then 1/2 neighbours think claim you are normal
					anomalycondition.put(iterator.next(), LG);
				} else {
					anomalycondition.put(iterator.next(), LF);
				}
			} catch (Exception e2) {
				logger.error("Error: No neighbour error in DFD process");
			}

		}
//		logger.info("First voting complete");
	}

	private void secondRoundVoting(Map<Integer, Map<Integer, Boolean>> correlationtable){
		Iterator<Integer> iterator = correlationtable.keySet().iterator();
		finalanomalycondition.clear();
		while(iterator.hasNext()){
			int nodeid = iterator.next();
			int totalneighbournumber = 0;
			int goodneighbournumber = 0;
			int LGneighbournumber = 0;
			//iterate all its correlation strength
			Iterator<Integer> iterator2 = correlationtable.get(nodeid).keySet().iterator();
			while(iterator2.hasNext()){
				totalneighbournumber++;
				int neighbourid = iterator2.next();
				try {
					if (anomalycondition.get(neighbourid) == LG) { 
						LGneighbournumber++;
						if (correlationtable.get(nodeid).get(neighbourid)) {
							goodneighbournumber++;
						}
					}
				} catch (Exception e) {
					logger.error("Error: Correlation strength table does not symmetrically paired on Node[" + nodeid + "] with unknown Node[" + neighbourid + "]");
				}

			}
			//setup up second voting result
			try {
				if ((double)LGneighbournumber / (double)totalneighbournumber >= leastLGratio) {														
					if ((double)goodneighbournumber/(double)LGneighbournumber >= faultneighbourthreshold) {
						finalanomalycondition.put(nodeid, GD); // majority says you are normal
					} else { 
						finalanomalycondition.put(nodeid, FT); // Majority says you are fault
					}
				} else { 
						finalanomalycondition.put(nodeid, UN); // Not have enough reference, indicate events
				}
			} catch (Exception e) {
				logger.error("Error: divide zero error");
			}
			
		}
//		logger.info("Second voting Finish");
	}
}
