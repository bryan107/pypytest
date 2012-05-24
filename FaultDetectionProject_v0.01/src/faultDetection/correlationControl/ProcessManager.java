package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fileAccessInterface.PropertyAgent;

public class ProcessManager {
	// ---------------------Private variables------------------------
	private ReadingBuffer readingbuffer = new ReadingBuffer();
	// private IntervalControl intervalcontrol;
	private CorrelationManager manager;
	// private Map<Integer, double[]> markedreading = new HashMap<Integer,
	// double[]>();
	private static Log logger = LogFactory.getLog(ProcessManager.class);
	DFDEngine DFDengine = new DFDEngine(0.8, 0.5);
	CorrelationStrengthManager CSmanager = new CorrelationStrengthManager(0.2);
	private PropertyAgent propagent = new PropertyAgent("conf");

	// --------------------------------------------------------------
	// ----------------------Constructor-----------------------------
	public ProcessManager() {
		// updateIntervalController(intervalControl);
		// TODO Temporarily initiate correlation manager manually
		// TODO Set variables with Property Agent in the entire project

		int samplesize = Integer.valueOf(propagent.getProperties("FDC",
				"SampleSize"));
		int eventpower = Integer.valueOf(propagent.getProperties("FDC",
				"EventPower"));
		double maxfaultratio = Double.valueOf(propagent.getProperties("FDC",
				"MaxFaultRatio"));
		double DFDthreshold = Double.valueOf(propagent.getProperties("FDC",
				"DFDThreshold"));
		double CSerrortolerance = Double.valueOf(propagent.getProperties("FDC",
				"CSErrorTolerance"));
		manager = new CorrelationManager(samplesize, eventpower, maxfaultratio);
		updateDFDThreshold(DFDthreshold);
		updateCSErrorTolerance(CSerrortolerance);
	}

	// --------------------------------------------------------------
	// ---------------------Public Functions--------------------------
	// public void updateIntervalController(IntervalControl intervalControl){
	// this.intervalcontrol = intervalControl;
	// }

	public void updateCorrelationSampleSize(int size) {
		manager.updateSampleSize(size);
	}

	public void updateDFDThreshold(double threshold) {
		DFDengine.updateThreshold(threshold);
	}

	public void updateCSErrorTolerance(double errortolerance) {
		CSmanager.updateErrorTolerance(errortolerance);
	}

	public Map<Integer, MarkedReading> markReadings(
			Map<Integer, Double> readingpack) {
		// Variables
		Map<Integer, Short> devicecondition;
		Map<Integer, Map<Integer, Double>> correlationtable;
		Map<Integer, Map<Integer, Double>> correlationtrendtable;
		Map<Integer, Map<Integer, Double>> correlationstrengthtable;
		Map<Integer, Short> readingfaultcondition;
		Map<Integer, Double> readingtrustworthiness;
		Map<Integer, MarkedReading> markedreadingpack = new HashMap<Integer, MarkedReading>();
		// Input data to local buffer
		// putReading(readingpack);
		// SETP 1: Put readings to correlation manager
		manager.putReading(readingpack);
		//
		// SETP 2: Correlation Manager Setup: 1.device condition 2.correlation
		// table 3.correlation trend table
		devicecondition = manager.getDeviceCondition();
		correlationtable = manager.getCorrelationTable();
		correlationtrendtable = manager.getCorrelationTrendTable();
		//
		// SETP 3 : Correlation Strength Manager setup: correlation strength
		// table
		correlationstrengthtable = CSmanager.getCorrelationStrengthTable(
				correlationtable, correlationtrendtable);

		//
		// SETP 4 : DFD Engine marks the readings
		readingfaultcondition = DFDengine.faultConditionalMarking(
				correlationstrengthtable, devicecondition);
		//
		// SETP 5 : Update device condition from the result of DFD & generate
		// reading trustworthiness
		manager.updateCorrelations(readingfaultcondition);
		readingtrustworthiness = CSmanager.getReadingTrustworthiness(
				correlationstrengthtable, readingfaultcondition);
		// Return result

		devicecondition = manager.getDeviceCondition();
		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			MarkedReading markedreading = new MarkedReading(nodeid,
					readingpack.get(nodeid),
					readingtrustworthiness.get(nodeid),
					readingfaultcondition.get(nodeid),
					devicecondition.get(nodeid));
			markedreadingpack.put(nodeid, markedreading);

		}
		return markedreadingpack;
	}

	public void putReading(int nodeid, double reading) {
		readingbuffer.putBufferData(nodeid, reading);
	}

	public void putReading(Map<Integer, Double> readingpack) {
		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			putReading(nodeid, readingpack.get(nodeid));
		}
	}
	//
	// public Map<Integer, double[]> getMarkedReading() {
	// return markedreading;
	// }
	//
	// public double[] getMarkedReading(int nodeid) {
	// return markedreading.get(nodeid);
	// }

	// -----------------------------------------------------------------
	// ----------------------Private Functions--------------------------

	// table updating mechanism & updating rate
	// private void intervalControl(){
	//
	// }
}
