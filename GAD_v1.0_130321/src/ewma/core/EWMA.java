package ewma.core;

import fileAccessInterface.PropertyAgent;
import gad.core.SMDB;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EWMA {
	private static Log logger = LogFactory.getLog(EWMA.class);
	//	File Access Agent
	private PropertyAgent propagent = new PropertyAgent("conf");
	//
	EWMAEngine ve;
	ConditionDiagnosis cd;
	//Parameters
	private int windowsize;
	private double numberofdeviation;
	private double MAR, MER;
	

	public EWMA() {
		loadConfig();
		this.ve = new EWMAEngine(numberofdeviation);
		this.cd = new ConditionDiagnosis(MAR, MER, windowsize);
	}
	// ** BE AWARE, EWMA share the same property file as GAD **
	public void loadConfig() {
		// Retrieve properties from property file
		int windowsize = Integer.valueOf(propagent.getProperties("GAD",
				"WindowSize"));
		double numberofdeviation = Double.valueOf(propagent.getProperties(
				"GAD", "NumberofDeviation"));
		double MAR = Double.valueOf(propagent.getProperties("GAD",
				"MaxAbnormalRatio"));
		double MER = Double.valueOf(propagent.getProperties("GAD",
				"MaxEventRatio"));
		// Load values to variables
		updateWindowSize(windowsize);
		updateNumberofDeviation(numberofdeviation);
		updateMAR(MAR);
		updateMER(MER);
	}

	public void updateWindowSize(int windowsize) {
		this.windowsize = windowsize;
		logger.info("Load Windowsize: " + windowsize);
	}

	public void updateNumberofDeviation(double numberofdeviation) {
		this.numberofdeviation = numberofdeviation;
		logger.info("Load Number of Deviation: " + numberofdeviation);
	}

	public void updateMAR(double MAR) {
		this.MAR = MAR;
		logger.info("Load MAR: " + MAR);
	}

	public void updateMER(double MER) {
		this.MER = MER;
		logger.info("Load MER: " + MER);
	}
	
	public void updateThreshold(double threshold){
		ve.updateThreshold(threshold);
	}

	public ProcessedReadingPack markReading(Map<Integer, Double> reading) {
		//STEP 1: Mark anomaly condition to each reading
		Map<Integer, Short> condition = ve.markCondition(reading);
		//STEP 2: Get diagnosis and update SMDB with new observations
		Diagnosis diagnosis = cd.diagnose(reading, condition);
		//STEP 3: Make output marked reading pack
		Map<Integer, MarkedReading> markedreadingpack = makeMarkedReadingPack(
				reading, diagnosis);
		// STEP 6. Make the final output file with a Novelty indicator
		ProcessedReadingPack processedreadingpack = new ProcessedReadingPack(
				markedreadingpack,diagnosis.eventOccurrence());
		return processedreadingpack;
	}

	public void resetSMDB() {
		SMDB.getInstance().resetSMDB();
	}
	
	private Map<Integer, MarkedReading> makeMarkedReadingPack(Map<Integer, Double> reading, Diagnosis diagnosis) {
		Map<Integer, MarkedReading> markedreadingpack = new HashMap<Integer, MarkedReading>();
		Iterator<Integer> it = reading.keySet().iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			markedreadingpack.put(
					nodeid,
					new MarkedReading(nodeid, reading.get(nodeid), diagnosis
							.readingCondition(nodeid), diagnosis
							.deviceCondition(nodeid)));
		}
		return markedreadingpack;
	}
	
	public void resetNode(int nodeid){
		SMDB.getInstance().resetnode(nodeid);
		logger.info("Node [" + nodeid + "] has been reset");
	}
}
