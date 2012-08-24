package experiment_cores;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import faultDetection.correlationControl.MarkedReading;
import faultDetection.correlationControl.ProcessManager;
import faultDetection.correlationControl.ProcessedReadingPack;
import fileAccessInterface.FileAccessAgent;
//TODO NEED TEST
public class CopyOfExperiment_Faults {
	private static Log logger = LogFactory.getLog(CopyOfExperiment_Faults.class);
	private int round = 10800;
	private int count = 0;
	private Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();
	private LinkedList<Integer> eventoccurenceround = new LinkedList<Integer>();
	private Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
	private String regressiontype = "";
	private String faulttype;

	private int latedetectioncount = 0;
	private int falsenegetivecount = 0;
	private int successdetectcount = 0;
	private int falsepositivecount = 0;
	private int totalrealfaultcount = 0;

	private String filewritelocation = "FaultType\\Noisy\\";
	private String filereadlocation = "FaultType\\Noisy\\";

	private int samplesize = 30;

	public CopyOfExperiment_Faults(String filewritelocation, String filereadlocation,
			String faulttype) {
		updateFilewriteLocation(filewritelocation);
		updateFileReadLocation(filereadlocation);
		updateFaultType(faulttype);
	}

	public void updateFilewriteLocation(String filewritelocation) {
		this.filewritelocation = filewritelocation;
	}

	public void updateFileReadLocation(String filereadlocation) {
		this.filereadlocation = filereadlocation;
	}

	public void updateFaultType(String faulttype) {
		this.faulttype = faulttype;
	}

	public void runSet(int num, double lowerbound, double upperbound) {
		for (double faultratio = lowerbound; faultratio < upperbound + 0.05; faultratio += 0.1) {
			runRoundSet(readingpack, num, faultratio, regressiontype);
		}
	}

	private void runRoundSet(Map<Integer, Double> readingpack, int num,
			double faultratio, String regressiontype) {
		DecimalFormat df = new DecimalFormat("0.0");
		FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\"
				+ filewritelocation + "Result_1_" + regressiontype + "__"
				+ faulttype + "__NUM_" + num + "__FaultRatio_"
				+ df.format(faultratio) + ".txt", "C:\\TEST\\NULL.txt");
		agent.writeLineToFile("Fault Ratio = " + faultratio);
		System.out.println(num);
		resetCounts();
		for (int x = 0; x < 30; x++) {
			ProcessManager manager = roundSetup(num, faultratio, df, agent, x);
			// ---------------Header Setup------------------
			String line = agent.readLineFromFile();
			while (!line.equals("FaultCondition")) {
				logger.info(line);
				line = agent.readLineFromFile();
			}
			line = agent.readLineFromFile();
			Map<Integer, Integer> faultroundMap = fileHeaderProcessing(line);
			// ---------------------
			while (!line.equals("Readings")) {
				logger.info(line);
				line = agent.readLineFromFile();
			}

			while (true) {
				line = agent.readLineFromFile();
				if (line != null) {
					proceedLine(readingpack, line, manager, faultroundMap);
					count++;
				} else
					break;
			}
			outputRealFaultRound(agent, faultroundMap);
			outputDCFaultRound(agent);
			calResults(faultroundMap);
			System.out
					.println("Num[" + num + "] at Round[" + x
							+ "] procceding: "
							+ df.format((double) x * 100 / 30) + "%");
		}
		outputPerformance(agent);
	}

	private Map<Integer, Integer> fileHeaderProcessing(String line) {
		String[] faultynodeinfo = line.split("\t");
//		totalrealfaultcount += faultynodeinfo.length;
//		only count those faults that are detectable.
//		TODO TEST NEW CODE
		int[] faultround = new int[faultynodeinfo.length];
		for(int i = 0 ; i < faultynodeinfo.length ; i++){
			faultround[i] = Integer.valueOf(faultynodeinfo[i]);
		}
		for(int value : faultround){
			if(value <= (round - 30)){
				totalrealfaultcount ++;
			}
		}
//
		Map<Integer, Integer> faultroundMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < faultynodeinfo.length; i++) {
			String[] reading = faultynodeinfo[i].split(":");
			if (reading.length == 2)
				faultroundMap.put(Integer.valueOf(reading[0]),
						Integer.valueOf(reading[1]));
		}
		return faultroundMap;
	}

	private void resetCounts() {
		latedetectioncount = 0;
		falsenegetivecount = 0;
		successdetectcount = 0;
		falsepositivecount = 0;
		totalrealfaultcount = 0;
	}

	private ProcessManager roundSetup(int num, double faultratio,
			DecimalFormat df, FileAccessAgent agent, int x) {
		count = 0;
		eventoccurenceround.clear();
		DC.clear();
		DCFaultround.clear();
		ProcessManager manager = new ProcessManager();

		String readingpath = "C:\\TEST\\" + filereadlocation + "source_1__"
				+ faulttype + "__" + df.format(faultratio) + "__NUM_" + num
				+ "__" + x + ".txt";
		agent.updatereadingpath(readingpath);
		agent.setFileReader();
		agent.writeLineToFile("Set number[" + x + "]");
		return manager;
	}

	private void calResults(Map<Integer, Integer> faultroundMap) {
		Set<Integer> key = faultroundMap.keySet();
		Iterator<Integer> it = key.iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			if (DCFaultround.containsKey(nodeid)) {
				if (Math.abs(faultroundMap.get(nodeid)
						- DCFaultround.get(nodeid)) <= samplesize)
					successdetectcount++;
				else if (faultroundMap.get(nodeid) > DCFaultround.get(nodeid))
					latedetectioncount++;
				else
					falsenegetivecount++;
				DCFaultround.remove(nodeid);
			} else {
				falsenegetivecount++;
			}

		}
		falsepositivecount += DCFaultround.size(); // System detect fault
													// while no fault exists
	}

	private void outputPerformance(FileAccessAgent agent) {
		agent.writeLineToFile("Total Fault Detection Accuracy:");
		agent.writeLineToFile("Successful Detection rate: " + (double) successdetectcount
				* 100 / totalrealfaultcount + " %");
		agent.writeLineToFile("False-Positive Rate: "
				+ (double) falsepositivecount * 100 / totalrealfaultcount
				+ " %");
		agent.writeLineToFile("False Negative Rate: "
				+ (double) falsenegetivecount * 100 / totalrealfaultcount
				+ " %");
		agent.writeLineToFile("Flase Positive Event Detection: "
				+ (double) eventoccurenceround.size() * 100
				/ (round - 30 * eventoccurenceround.size() - 30) + " %");
		agent.writeLineToFile("Late Detection Rate: "
				+ (double) latedetectioncount * 100 / totalrealfaultcount
				+ " %");
	}

	private void outputRealFaultRound(FileAccessAgent agent,
			Map<Integer, Integer> faultroundMap) {
		Set<Integer> key = faultroundMap.keySet();
		Iterator<Integer> iterator = key.iterator();
		agent.writeLineToFile("Real Fault Round:");
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			agent.writeLineToFile("[" + nodeid + "] "
					+ faultroundMap.get(nodeid));
		}
	}

	private void outputDCFaultRound(FileAccessAgent agent) {
		Set<Integer> key = DCFaultround.keySet();
		Iterator<Integer> iterator = key.iterator();
		agent.writeLineToFile("Device Fault Round:");
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			agent.writeLineToFile("[" + nodeid + "] "
					+ DCFaultround.get(nodeid));
		}
	}

	private void proceedLine(Map<Integer, Double> readingpack, String line,
			ProcessManager manager, Map<Integer, Integer> faultroundMap) {
		String[] readingstringpack = line.split("\t");
		for (int i = 0; i < readingstringpack.length; i++) {
			String[] reading = readingstringpack[i].split(":");
			// logger.info("T:" + reading.length);
			if (reading.length == 2)
				readingpack.put(Integer.valueOf(reading[0]),
						Double.valueOf(reading[1]));
			else
				logger.error("Error Data Structure");
		}

		ProcessedReadingPack processedreadingpack = manager
				.markReadings(readingpack);
		Map<Integer, MarkedReading> markedreading = processedreadingpack
				.markedReadingPack();

		Set<Integer> key = markedreading.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			try {
				if (markedreading.get(nodeid).deviceCondition() == 0
						&& DC.get(markedreading.get(nodeid).id()) == 3) {
					DCFaultround.put(markedreading.get(nodeid).id(), count);

				}
			} catch (Exception e) {
			}
			DC.put(markedreading.get(nodeid).id(), markedreading.get(nodeid)
					.deviceCondition());
			// agent.writeLineToFile(message.toFormat());
		}

		if (processedreadingpack.newEventOccurs()) {
			eventoccurenceround.add(count);
		}

		// agent.writeLineToFile("\n");
		readingpack.clear();
	}
}
