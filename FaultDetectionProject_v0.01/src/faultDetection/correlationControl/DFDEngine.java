package faultDetection.correlationControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public final class DFDEngine {
	// Predefined variable
	private final static double predefinedthreshold = 0.8;
	private final static double predefinedleastLGnumber = 0.5;
	// Set up final Instance
	private static DFDEngine self = new DFDEngine(predefinedthreshold, predefinedleastLGnumber);
	//---------------------Private variables--------------------------
	//faulty conditions
	private final short FT = 0;
	private final short LF = 1;
	private final short LG = 2;
	private final short GD = 3;
	//
	private double threshold; 
	private double leastLGnumber;
	private double faultneighbourthreshold = 0.5;
	private Map<Integer, Map<Integer, Double>> correlationstrengthtable = new HashMap<Integer, Map<Integer, Double>>();
	private Map<Integer, Short> faultycondition = new HashMap<Integer, Short>();
	private Map<Integer, Short> finalfaultycondition = new HashMap<Integer, Short>();
	
	private static Log logger = LogFactory.getLog(DFDEngine.class);
	//---------------------------------------------------------------
	//-------------------------Constructor---------------------------
	public DFDEngine(double threshold, double leastLGnumber){
		updateThreshold(threshold);
		updateLeastLGNumber(leastLGnumber);
	}
	//---------------------------------------------------------------
	//------------------------Public Functions-----------------------
	public static DFDEngine getInstance(){
		return self;
	}
	public void updateLeastLGNumber(double leastLGnumber){
		this.leastLGnumber = leastLGnumber;
	}
	public void updateThreshold(double threshold){
		this.threshold = threshold;
	}
	public Map<Integer, Short> faultConditionalMarking(Map<Integer, Map<Integer, Double>> correlationstrengthtable, Map<Integer, Short> devicecondition){
		
		setupLocalCorrelationStrengthTable(correlationstrengthtable, devicecondition);
		firstRoundVoting();
		secondRoundVoting();
		return finalfaultycondition;
	}
	//----------------------------------------------------------------
	//------------------------Private Functions-----------------------
	private void setupLocalCorrelationStrengthTable(
			Map<Integer, Map<Integer, Double>> correlationstrengthtable,
			Map<Integer, Short> devicecondition) {
		if(this.correlationstrengthtable.isEmpty() != true){
			this.correlationstrengthtable.clear();
		}
		List<Integer> nonGDnodelist = new ArrayList<Integer>();
		Set<Integer> key1 = devicecondition.keySet();
		Iterator<Integer> iterator1= key1.iterator();
		while(iterator1.hasNext()){
			int nodeid = iterator1.next();
			if(devicecondition.get(nodeid) != GD){
				nonGDnodelist.add(nodeid);
			}
		}
						
		Set<Integer> key2 = correlationstrengthtable.keySet();
		Iterator<Integer> iterator2= key2.iterator();
		for(Map<Integer, Double> raw : correlationstrengthtable.values()){
			int nodeid = iterator2.next();
			if(devicecondition.get(nodeid) != FT){
				for(int id : nonGDnodelist){
					raw.remove(id);
				}
				try {
					this.correlationstrengthtable.put(nodeid, raw);
				} catch (Exception e) {
					logger.error("Error:" + e.toString());
				}
				
			}
		}
	}

	private void firstRoundVoting(){
//		logger.info("Start first voting / size = " + correlationstrengthtable.size());
		faultycondition.clear();
		Set<Integer> key = correlationstrengthtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(Map<Integer, Double> i : correlationstrengthtable.values()){;
			int totalneighbournumber = 0;
			int goodneighbournumber = 0;
			//Count good neighbour number through all its correlation strength
			for(double j : i.values()){
				totalneighbournumber++;
				if(j >= threshold){
					goodneighbournumber++;
				}
			}
			//setup up first voting result
			try {
				if ((double)goodneighbournumber/(double)totalneighbournumber >= faultneighbourthreshold) {//more then 1/2 neighbours think claim you are normal
					faultycondition.put(iterator.next(), LG);
				} else {
					faultycondition.put(iterator.next(), LF);
				}
			} catch (Exception e2) {
				logger.error("Error: No neighbour error in DFD process");
			}

		}
//		logger.info("First voting complete");
	}

	private void secondRoundVoting(){
//		logger.info("Second voting start");
		Set<Integer> key = correlationstrengthtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(Map<Integer, Double> i : correlationstrengthtable.values()){
			int iteratenumber = iterator.next();
			int totalneighbournumber = 0;
			int goodneighbournumber = 0;
			int LGneighbournumber = 0;
			//iterate all its correlation strength
			Set<Integer> key2 = i.keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			for(double j : i.values()){
				totalneighbournumber++;
				int neighbour = iterator2.next();
				try {
					
					if (faultycondition.get(neighbour) == LG) { 
						LGneighbournumber++;
						if (j >= threshold) {
							goodneighbournumber++;
						}
					}
				} catch (Exception e) {
					logger.error("Error: Correlation strength table does not symmetrically paired on Node[" + iteratenumber + "] with unknown Node[" + neighbour + "]");
				}

			}
			//setup up second voting result
			try {
				if ((double)goodneighbournumber / (double)totalneighbournumber >= faultneighbourthreshold) {														
					if (LGneighbournumber >= leastLGnumber) {
						finalfaultycondition.put(iteratenumber, GD);
					} else {
						finalfaultycondition.put(iteratenumber, LG);
					}
				} 
				else {
					if (LGneighbournumber >= leastLGnumber) {
						finalfaultycondition.put(iteratenumber, FT);
					} else {
						finalfaultycondition.put(iteratenumber, LF);
					}
				}
			} catch (Exception e) {
				logger.error("Error: divide zero error");
			}
			
		}
//		logger.info("Second voting Finish");
	}
}