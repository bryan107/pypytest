package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import faultDetection.tools.LeastSquareEstimator;
import faultDetection.tools.MedianEstimator;
import faultDetection.tools.QuantilesEEstimator;
import faultDetection.tools.RegressionEstimator;
import fileAccessInterface.PropertyAgent;

public class ProcessManager {
	// ---------------------Private variables------------------------
	private ReadingBuffer readingbuffer = new ReadingBuffer();
	private CorrelationManager manager =  new CorrelationManager(30, 2, 0.293,
			0.03, null);
	DFDEngine DFDengine = new DFDEngine(0.8, 0.5);
	CorrelationStrengthManager CSmanager = new CorrelationStrengthManager(0.02, 0.8);
	//---------------File Access Interface---------------------
	private PropertyAgent propagent = new PropertyAgent("conf");
	//----------------------------------------------------------------------------
	private static Log logger = LogFactory.getLog(ProcessManager.class);
	// --------------------------------------------------------------
	// ----------------------Constructor-----------------------------
	public ProcessManager() {
		updateSettingsFromConfig();
	}

	private void updateSettingsFromConfig() {
		int samplesize = Integer.valueOf(propagent.getProperties("FDC",
				"SampleSize"));
		int eventpower = Integer.valueOf(propagent.getProperties("FDC",
				"EventPower"));
		double maxfaultratio = Double.valueOf(propagent.getProperties("FDC",
				"MaxFaultRatio"));
		double eventLFratio = Double.valueOf(propagent.getProperties("FDC",
				"EventLFRatio"));
		double DFDthreshold = Double.valueOf(propagent.getProperties("FDC",
				"DFDThreshold"));
		double tolerablenoise = Double.valueOf(propagent.getProperties("FDC",
				"CSTolerableNoise"));	
		RegressionEstimator regressionestimator = initailRegressionEstimator();
		
		updateCorrelationSampleSize(samplesize);
		updateEventPower(eventpower);
		updateMaxFaultRatio(maxfaultratio);
		updateEventLFRatio(eventLFratio);
		updateRegressionEstimator(regressionestimator);
		updateDFDThreshold(DFDthreshold);
		updateCSTolerableNoise(tolerablenoise);
	}

	// --------------------------------------------------------------
	// ---------------------Variable Settings--------------------------
	// public void updateIntervalController(IntervalControl intervalControl){
	// this.intervalcontrol = intervalControl;
	// }

	public void updateEventLFRatio(double eventLFratio) {
		manager.updateEventLFRatio(eventLFratio);
	}

	public void updateEventPower(int power) {
		manager.updateEventPower(power);
	}

	public void updateMaxFaultRatio(double maxfaultratio){
		manager.updateMaxFaultRatio(maxfaultratio);
	}
	
	public void updateCorrelationSampleSize(int size) {
		manager.updateSampleSize(size);
	}

	public void updateDFDThreshold(double threshold) {
		DFDengine.updateThreshold(threshold);
		CSmanager.updateMinimumCorrelationStrength(threshold);
		
	}

	public void updateCSTolerableNoise(double tolerablenoise) {
		CSmanager.updateTolerableNoise(tolerablenoise);
	}

	public void updateRegressionEstimator(RegressionEstimator regressionestimator) {
		manager.updateRegressionEstimator(regressionestimator);
	}

	
	// ---------------------Public Functions--------------------------
	public ProcessedReadingPack markReadings(
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
		boolean eventoccurence = manager.updateCorrelations(readingfaultcondition);
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
		ProcessedReadingPack processedreadingpack = new ProcessedReadingPack(markedreadingpack, eventoccurence);
		return processedreadingpack;
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
	//
	// public double[] getMarkedReading(int nodeid) {
	// return markedreading.get(nodeid);
	// }

	// -----------------------------------------------------------------
	// ----------------------Private Functions--------------------------
	
	
	private RegressionEstimator initailRegressionEstimator() {
		RegressionEstimator regressionestimator;
		int regressiontype = Integer.valueOf(propagent.getProperties("FDC",
				"RegressionType"));
		switch (regressiontype) {
		case 0:
			regressionestimator = new LeastSquareEstimator();
			break;
		case 1:
			regressionestimator = new QuantilesEEstimator();
			break;
		case 2:
			regressionestimator = new MedianEstimator();
			break;
		default:
			regressionestimator = null;
			logger.error("Non-defined regression type");
			break;
		}
		return regressionestimator;
	}

	// table updating mechanism & updating rate
	// private void intervalControl(){
	//
	// }
}
