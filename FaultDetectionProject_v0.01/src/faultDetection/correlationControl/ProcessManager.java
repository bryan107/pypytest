package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProcessManager {
	// ---------------------Private variables------------------------
	private ReadingBuffer readingbuffer = new ReadingBuffer();
	// private IntervalControl intervalcontrol;
	private CorrelationManager manager;
	private Map<Integer, double[]> markedreading = new HashMap<Integer, double[]>();

	// --------------------------------------------------------------
	// ----------------------Constructor-----------------------------
	public ProcessManager(int samplesize, int eventpower, double maxfaultratio) {
		// updateIntervalController(intervalControl);
		// TODO Temporarily initiate correlation manager manually
		manager = new CorrelationManager(samplesize, eventpower, maxfaultratio);
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

	}

	// TODO To complete the Process
	public Map<Integer, Double[]> MarkReadings(Map<Integer, Double> readingpack) {
		//Input data to local buffer
		putReading(readingpack);
		//SETP 1: Put readings to correlation manager
		manager.putReading(readingbuffer.getBufferData());
		//SETP 2: Correlation Manager Setup: 1.device condition 2.correlation table 3.correlation trend table
		Map<Integer, Boolean> devicecondition = manager.getDeviceCondition();
		Map<Integer, Map<Integer, Double>> correlationtable = manager
				.getCorrelationTable();
		Map<Integer, Map<Integer, Double>> correlationtrendtable = manager
				.getCorrelationTrendTable();
		//SETP 3 : Correlation Strength Manager setup: correlation strength table
		Map<Integer, Map<Integer, Double>> correlationstrengthtable = CorrelationStrengthManager
				.getInstance().getCorrelationStrengthTable(correlationtable,
						correlationtrendtable);
		//SETP 4 : DFD Engine marks the readings
		Map<Integer, Short> readingfaultcondition = DFDEngine.getInstance().faultConditionalMarking(
				correlationstrengthtable, devicecondition);
		//SETP 5 : Update device condition from the result of DFD
		manager.updateCorrelations(readingfaultcondition);
		//Return result
		//TODO return process
		devicecondition = manager.getDeviceCondition();
		Map<Integer, Double[]> markedreadingpack = new HashMap<Integer, Double[]>();
		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int nodeid = iterator.next();
			double[] temp = new double[3];
			temp[0] = readingpack.get(nodeid);
//			temp[1] = correlations
		}
		return null;
	}

	public void putReading(int nodeid, double reading) {
		readingbuffer.putBufferData(nodeid, reading);
	}

	public void putReading(Map<Integer, Double> readingpack) {
		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		for (double reading : readingpack.values()) {
			int nodeid = iterator.next();
			putReading(nodeid, reading);
		}
	}

	public Map<Integer, double[]> getMarkedReading() {
		return markedreading;
	}

	public double[] getMarkedReading(int nodeid) {
		return markedreading.get(nodeid);
	}

	// -----------------------------------------------------------------
	// ----------------------Private Functions--------------------------

	// TODO Require a buffer interval controller to control the correlation
	// table updating mechanism & updating rate
	// private void intervalControl(){
	//
	// }
}
