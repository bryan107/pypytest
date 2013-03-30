package gad.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SMDB {

	private static Log logger = LogFactory.getLog(SMDB.class);
	private int windowsize = 30;
	private static SMDB self = new SMDB();
	private Queue<Boolean> eventcondition = new LinkedList<Boolean>();
	private Map<Integer, Queue<Reading>> readingDB = new HashMap<Integer, Queue<Reading>>(); // Readings
	private Map<Integer, Boolean> devicecondition = new HashMap<Integer, Boolean>();
	private Map<Integer, Double> estimatedreading = new HashMap<Integer, Double>();
	
	private SMDB() {

	}

	public void setupWindowSize(int windowsize) {
		this.windowsize = windowsize;
	}

	public static SMDB getInstance() {
		return self;
	}

	public void resetnode(int nodeid){
		readingDB.remove(nodeid);
		devicecondition.remove(nodeid);
		estimatedreading.remove(nodeid);
		logger.info("Node["+ nodeid + "] has been reset...");
	}
	
	public void resetSMDB(){
		eventcondition.clear();
		readingDB.clear();
		devicecondition.clear();
		estimatedreading.clear();
		logger.info("SMDB has been reset...");
	}
	
	public void resetSMDB(LinkedList<Integer> dcFT){
		eventcondition.clear();
		readingDB.clear();
		devicecondition.clear();
		estimatedreading.clear();
		for(int nodeid: dcFT){
			devicecondition.put(nodeid, false);
		}		
		
		logger.info("SMDB has been reset...");
	}
	
	// ***************** Reading Methods ******************//

	public Queue<Reading> getReadings(int nodeid) {
		if (readingDB.containsKey(nodeid)) {
			if (readingDB.get(nodeid).size() < windowsize) {
				//logger.warn("Node" + nodeid + " reading is not ready.");
				return null;
			}
			return readingDB.get(nodeid);
		} else {
//			logger.warn("Node" + nodeid + " does not exist");
			return null;
		}
	}

	public Queue<Boolean> getEventConditions() {
		if (eventcondition.size() < windowsize) {
//			logger.warn("Event condition not ready.");
		}
		return eventcondition;
	}
	
	public boolean getDeviceCondition(int nodeid){
		if(devicecondition.containsKey(nodeid)){
			return devicecondition.get(nodeid);
		}
		else{
//			logger.error("Device [" + nodeid + "] does not exist");
			return true;
		}
	}

	public double getEstimatedReading(int nodeid){
		if(estimatedreading.containsKey(nodeid)){
			return estimatedreading.get(nodeid);
		}
		else{
			logger.error("Device [" + nodeid + "] does not exist");
			return 0;
		}
	}
	// ********* Not recommend to return the whole DB *************//
	public Map<Integer, Queue<Reading>> getReadings() {
		return readingDB;
	}

	// *************************************************************//
	// *********************** Writing Methods *********************//
	public void putReading(int nodeid, double value, short condition) {
		// If new node added
		if (!readingDB.containsKey(nodeid)) {
			readingDB.put(nodeid, new LinkedList<Reading>());
			//putDeviceCondition(nodeid, true); // Default good device
			putEsimatedReading(nodeid, value);
		}
		// Check if queue if full
		if (readingDB.get(nodeid).size() >= windowsize) {
			readingDB.get(nodeid).remove();
		}
		// Add new reading
		readingDB.get(nodeid).add(new Reading(value, condition));
	}

	public void putEventConditions(boolean condition) {
		eventcondition.add(condition);
	}

	public void putDeviceCondition(int nodeid, boolean condition) {
		devicecondition.put(nodeid, condition);
	}
	
	public void putEsimatedReading(int nodeid, double value){
		estimatedreading.put(nodeid, value);
	}
}
