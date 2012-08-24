package experiment_cores;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import faultDetection.correlationControl.MarkedReading;
import faultDetection.correlationControl.ProcessedReadingPack;
import fileAccessInterface.FileAccessAgent;

public abstract class PerformanceAnalyzer {

	// -----------------Definition of reading conditions--------------------
	private final short FT = 0;
//	private final short LF = 1;
	// private final short LG = 2;
	private final short GD = 3;
//	private final short UNKNOWN = 4;
	// --------------------------------------
	public Map<Integer, Integer> detectDCFaultround = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> realDCFaulttround = new HashMap<Integer, Integer>();
	public Queue<Integer> detecteventround = new LinkedList<Integer>();
	public Queue<Integer> realeventround = new LinkedList<Integer>();

	private Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private int fpeventcount, detecteventcount, realeventcount;
	private int fpDCFaultcount, fnDCFaultcount, detectDCFaultcount,
			realDCFaultcount, lateDCFaultcount;
	private int samplesize;

	public PerformanceAnalyzer(int samplesize) {
		this.samplesize = samplesize;

		fpeventcount = 0;
		detecteventcount = 0;
		realeventcount = 0;

		fpDCFaultcount = 0;
		fnDCFaultcount = 0;
		detectDCFaultcount = 0;
		realDCFaultcount = 0;
		lateDCFaultcount = 0;
	}

	// Accumulate result every round
	public void resultAccumulation(ProcessedReadingPack processedreadingpack,
			int round) {
		Map<Integer, MarkedReading> markedreading = processedreadingpack
				.markedReadingPack();
		Set<Integer> key = markedreading.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			try {
				if (markedreading.get(nodeid).deviceCondition() == FT
						&& DC.get(markedreading.get(nodeid).id()) == GD) {
					detectDCFaultround.put(markedreading.get(nodeid).id(),
							round);
				}
			} catch (Exception e) {
			}
			DC.put(markedreading.get(nodeid).id(), markedreading.get(nodeid)
					.deviceCondition());
		}
		if (processedreadingpack.newEventOccurs()) {
			detecteventround.add(round);
		}
	}

	public void outputDCFaultRound(FileAccessAgent agent) {
		Set<Integer> key = detectDCFaultround.keySet();
		Iterator<Integer> iterator = key.iterator();
		agent.writeLineToFile("Device Fault Round:");
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			agent.writeLineToFile("[" + nodeid + "] "
					+ detectDCFaultround.get(nodeid));
		}
	}

	//Calculate & accumulate detection performance per file
	public void calcPerformance() {
		calcEventPerformance();
		calcDCFaultPerformance();
	}

	private void calcDCFaultPerformance() {
		realDCFaultcount += realDCFaulttround.size();
		Set<Integer> key = realDCFaulttround.keySet();
		Iterator<Integer> it = key.iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			if (detectDCFaultround.containsKey(nodeid)) {
				int difference = detectDCFaultround.get(nodeid)
						- realDCFaulttround.get(nodeid);
				if (difference <= samplesize && difference >= 0)
					detectDCFaultcount++;
				else if (difference > samplesize)
					lateDCFaultcount++;
				else
					fpDCFaultcount++;
				detectDCFaultround.remove(nodeid);
			} else {
				fnDCFaultcount++;
			}
		}
		fpDCFaultcount += detectDCFaultround.size(); // System detect fault
														// while no fault exists
	}

	private void calcEventPerformance() {
		realeventcount += realeventround.size();
		for (int reround : realeventround) {
			for (int deround : detecteventround) {
				int difference = deround - reround;
				if (difference <= samplesize && difference >= 0) {
					detecteventcount++;
					detecteventround.remove(deround);
					break;
				}
			}
		}
		fpeventcount += detecteventround.size();
	}

	public void resetRound() {
		detectDCFaultround.clear();
		realDCFaulttround.clear();
		detecteventround.clear();
		realeventround.clear();
	}

	public void outputPerformance(FileAccessAgent agent) {
		agent.writeLineToFile("---------Total Detection Accuracy--------");
		agent.writeLineToFile("Fault::");
		agent.writeLineToFile("Successful Detection : "
				+ (double) detectDCFaultcount * 100 / realDCFaultcount + " %");
		agent.writeLineToFile("Late Detecrtion : " + (double) lateDCFaultcount
				* 100 / realDCFaultcount + " %");
		agent.writeLineToFile("False-Positive Detecrtion : "
				+ (double) fpDCFaultcount * 100 / realDCFaultcount + " %");
		agent.writeLineToFile("False-Negative Detecrtion : "
				+ (double) fnDCFaultcount * 100 / realDCFaultcount + " %");

		agent.writeLineToFile("Event::");
		agent.writeLineToFile("Successful Detection : "
				+ (double) detecteventcount * 100 / realeventcount + "%");
		agent.writeLineToFile("False-Positive Detecrtion : "
				+ (double) fpeventcount * 100 / realeventcount + "%");
	}
}
