package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DFDEngine {
	// Set up final Instance
	private static DFDEngine self = new DFDEngine(0.2, 0.5);
	// Private variables
	//faulty conditions
	private short FT = 0;
	private short LF = 1;
	private short LG = 2;
	private short GD = 3;
	//
	private double threshold; 
	private double leastLGnumber;
	private double faultneighbourthreshold = 0.5;
	private Map<Integer, Map<Integer, Double>> correlationstrengthtable;
	private Map<Integer, Short> faultycondition = new HashMap<Integer, Short>();
	private Map<Integer, Short> finalfaultycondition = new HashMap<Integer, Short>();
	
	private static Log logger = LogFactory.getLog(DFDEngine.class);
	
	// Constructor
	public DFDEngine(double threshold, double leastLGnumber){
		updateThreshold(threshold);
		updateLeastLGNumber(leastLGnumber);
	}
	// Public Functions
	public static DFDEngine getInstance(){
		return self;
	}
	public void updateLeastLGNumber(double leastLGnumber){
		this.leastLGnumber = leastLGnumber;
	}
	public void updateThreshold(double threshold){
		this.threshold = threshold;
	}
	public Map<Integer, Short> faultConditionalMarking(Map<Integer, Map<Integer, Double>> correlationstrengthtable){
		this.correlationstrengthtable = correlationstrengthtable;
		firstRoundVoting();
		secondRoundVoting();
		return finalfaultycondition;
	}
	//Private Functions
	private void firstRoundVoting(){
		logger.info("Start first voting / size = " + correlationstrengthtable.size());
		logger.info("ttt 1 : " + correlationstrengthtable.get(1).size());
		faultycondition.clear();
		Set<Integer> key = correlationstrengthtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(Map<Integer, Double> i : correlationstrengthtable.values()){
			int e = iterator.next();
			logger.info("first marking : " + e + " size : " + i.size());
			int totalneighbournumber = 0;
			int goodneighbournumber = 0;
			double goodratio = 0;
			//iterate all its correlation strength
			for(double j : i.values()){
				totalneighbournumber++;
				if(j >= threshold){
					goodneighbournumber++;
				}
			}
			//setup up first voting result
			try {
				goodratio = goodneighbournumber/totalneighbournumber;
			} catch (Exception e2) {
				logger.error("divide zero error");
			}
			
			
			if(goodratio > faultneighbourthreshold){
				logger.info("mark " + e);
				faultycondition.put(e, LG);
				logger.info("mark " + e);
			}
			else{
				faultycondition.put(e, LF);
				logger.warn("mark " + e);
			}
		}
		logger.info("First voting complete");
	}
	
	private void secondRoundVoting(){
		logger.info("Second voting start");
		Set<Integer> key = correlationstrengthtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(Map<Integer, Double> i : correlationstrengthtable.values()){
			int totalneighbournumber = 0;
			int goodneighbournumber = 0;
			int LGneighbournumber = 0;
			//iterate all its correlation strength
			Set<Integer> key2 = i.keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			for(double j : i.values()){
				totalneighbournumber++;
				if(j >= threshold && faultycondition.get(iterator2.next()) ==LG){ // only count LG neighbour's reference
					goodneighbournumber++;
					LGneighbournumber++;
				}
			}
			//setup up second voting result
			if((goodneighbournumber/totalneighbournumber) >= faultneighbourthreshold){
				if(LGneighbournumber >= leastLGnumber){
					finalfaultycondition.put(iterator.next(), GD);
				}
				else{
					finalfaultycondition.put(iterator.next(), LG);
				}
			}
			else{
				if(LGneighbournumber >= leastLGnumber){
					finalfaultycondition.put(iterator.next(), FT);
				}
				else{
					finalfaultycondition.put(iterator.next(), LF);
				}
			}
		}
		logger.info("Second voting Finish");
	}
}
