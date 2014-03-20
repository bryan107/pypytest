package kernelfunction.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kernelfunction.fileaccessinterface.PropertyAgent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Kernel {
	private static Log logger = LogFactory.getLog(Kernel.class);
	//	File Access Agent
	private PropertyAgent propagent = new PropertyAgent("conf");
	//
	KernelEngine ke;
	ConditionDiagnosis cd;
	//Parameters
	private int windowsize;
	private double threshold_r, threshold_p;
	private double MAR, MER;
	private SMDB smdb;

	public Kernel() {
		loadConfig();
		smdb = new SMDB();
		smdb.resetSMDB();
		smdb.setupWindowSize(windowsize);
		this.ke = new KernelEngine(threshold_r, threshold_p, smdb);
		this.cd = new ConditionDiagnosis(MAR, MER, windowsize, smdb);
	}
	// ** BE AWARE, EWMA share the same property file as GAD **
	public void loadConfig() {
		// Retrieve properties from property file
		int windowsize = Integer.valueOf(propagent.getProperties("Kernel",
				"WindowSize"));
		double threshold_r = Double.valueOf(propagent.getProperties("Kernel", "Threshold_r"));
		double threshold_p = Double.valueOf(propagent.getProperties("Kernel", "Threshold_p"));
		double MAR = Double.valueOf(propagent.getProperties("Kernel",
				"MaxAbnormalRatio"));
		double MER = Double.valueOf(propagent.getProperties("Kernel",
				"MaxEventRatio"));
		// Load values to variables
		updateWindowSize(windowsize);
		updateThresholds(threshold_r, threshold_p);
		updateMAR(MAR);
		updateMER(MER);
	}

	public void updateWindowSize(int windowsize) {
		this.windowsize = windowsize;
		logger.info("Load Windowsize: " + windowsize);
	}

	public void updateThresholds(double threshold_r, double threshold_p) {
		this.threshold_r = threshold_r;
		this.threshold_p = threshold_p;
		logger.info("Load Threshold_r: " + threshold_r);
		logger.info("Load Threshold_p: " + threshold_p);
	}

	public void updateMAR(double MAR) {
		this.MAR = MAR;
		logger.info("Load MAR: " + MAR);
	}

	public void updateMER(double MER) {
		this.MER = MER;
		logger.info("Load MER: " + MER);
	}
	
	public ProcessedReadingPack markReading(Map<Integer, Double> reading) {
		//STEP 1: Mark anomaly condition to each reading
		Map<Integer, Short> condition = ke.markCondition(reading);
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
		smdb.resetSMDB();
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
		smdb.resetnode(nodeid);
		logger.info("Node [" + nodeid + "] has been reset");
	}
}
