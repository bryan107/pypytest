package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DFDEngine {
	// Set up final Instance
	private static DFDEngine self = new DFDEngine(0.8, 0.5);
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
		logger.info("First voting complete");
	}

	private void secondRoundVoting(){
		logger.info("Second voting start");
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
		logger.info("Second voting Finish");
	}
}
