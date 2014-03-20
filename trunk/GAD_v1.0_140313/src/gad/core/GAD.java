package gad.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fileAccessInterface.PropertyAgent;

public class GAD {
	private static Log logger = LogFactory.getLog(GAD.class);
	private PropertyAgent propagent = new PropertyAgent("conf");
	private int windowsize;
	private double numberofdeviation;
	private double MAR, MER;
	PatternGenerator pattern;
	CorrelationAsessment ce;
	DFDEngine dfd;
	ConditionDiagnosis cd;

	public GAD() {
		loadConfig();
		pattern = new PatternGeneratorPCA(windowsize);
		ce = new CorrelationAsessment(numberofdeviation);
		dfd = new DFDEngine();
		cd = new ConditionDiagnosis(MAR, MER, windowsize);
		SMDB.getInstance().resetSMDB();
		SMDB.getInstance().setupWindowSize(windowsize);
		logger.info("=GAD initiated=");
	}

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

	public void resetNode(int nodeid){
		SMDB.getInstance().resetnode(nodeid);
		logger.info("Node [" + nodeid + "] has been reset");
	}
	
	public ProcessedReadingPack markReading(Map<Integer, Double> reading) {
		// *********************** PHASE 1 ****************************//
		// STEP 1. Pattern Estimation, include estimation value , directions and
		// deviations
		Map<Integer, Map<Integer, EstimatedVariancePCA>> estimation = pattern
				.getEstimation(reading);
		// STEP 2. Correlation Estimation
		Map<Integer, Map<Integer, Boolean>> correlation = ce.assessCorrelation(
				reading, estimation);
		// ************************ PHASE 2 ****************************//
		// STEP 3. Trust-base Voting
		Map<Integer, Short> anomalycondition = dfd.markCondition(correlation);
		
		// STEP 4. Abnormality/Event Condition Diagnosis + Storing messages
		Diagnosis diagnosis = cd.diagnose(reading, anomalycondition);

		// STEP 5. Make output marked reading pack
		Map<Integer, MarkedReading> markedreadingpack = makeMarkedReadingPack(
				reading, diagnosis);
		// STEP 6. Make the final output file with a Novelty indicator
		ProcessedReadingPack processedreadingpack = new ProcessedReadingPack(
				markedreadingpack,diagnosis.eventOccurrence());
		return processedreadingpack;
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

}
