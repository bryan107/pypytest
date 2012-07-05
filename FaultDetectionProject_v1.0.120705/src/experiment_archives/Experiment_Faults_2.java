package experiment_archives;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import faultDetection.correlationControl.MarkedReading;
import faultDetection.correlationControl.ProcessManager;
import fileAccessInterface.FileAccessAgent;

public class Experiment_Faults_2 {
	private static Log logger = LogFactory.getLog(Experiment_Faults_2.class);
	private static int round = 10800;
	private static int count = 0;
	private static Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private static Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();
	private static Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
	private static String regressiontype = "";
	private static String faulttype;

	private static String filename = "FaultType\\Noisy\\";
	private static int samplesize = 30;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		faulttype = "Stuck";
		filename = "FaultType\\"+ faulttype +"\\";
		runSet(10, 0.1, 0.6);
//		runSet(7, 0.1, 0.6);

	}

	private static void runSet(int num, double lowerbound, double upperbound) {
		for (double faultratio = lowerbound; faultratio < upperbound + 0.05; faultratio += 0.1) {
			runRoundSet(readingpack, num, faultratio, regressiontype);
		}
	}

	private static void runRoundSet(Map<Integer, Double> readingpack, int num,
			double faultratio, String regressiontype) {
		DecimalFormat df = new DecimalFormat("0.0");
		FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\" + filename
				+ "Result_1_" + regressiontype + "__" + faulttype + "__NUM_"
				+ num + "__FaultRatio_" + df.format(faultratio) + ".txt",
				"C:\\TEST\\NULL.txt");
		agent.writeLineToFile("Fault Ratio = " + faultratio);
		System.out.println(num);
		int falsepositivecount = 0;
		int falsenegetivecount = 0;
		int successdetectcount = 0;
		int totalrealfaultcount = 0;
		for (int x = 0; x < 30; x++) {
			// ---------------Round Setup-------------------
			count = 0;
			DC.clear();
			DCFaultround.clear();
			ProcessManager manager = new ProcessManager();

			String readingpath = "C:\\TEST\\" + filename + "source_1__"
					+ faulttype + "__" + df.format(faultratio) + "__NUM_" + num
					+ "__" + x + ".txt";
			agent.updatereadingpath(readingpath);
			agent.setFileReader();
			agent.writeLineToFile("Set number[" + x + "]");
			// ---------------------------------------------
			// ---------------Header Setup------------------
			String line = agent.readLineFromFile();
			while (!line.equals("FaultCondition")) {
				logger.info(line);
				line = agent.readLineFromFile();
			}
			line = agent.readLineFromFile();
			String[] faultynodeinfo = line.split("\t");
			totalrealfaultcount += faultynodeinfo.length;
			Map<Integer, Integer> faultroundMap = new HashMap<Integer, Integer>();
			for (int i = 0; i < faultynodeinfo.length; i++) {
				String[] reading = faultynodeinfo[i].split(":");
				if (reading.length == 2)
					faultroundMap.put(Integer.valueOf(reading[0]),
							Integer.valueOf(reading[1]));
			}
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
			
			Set<Integer> key = faultroundMap.keySet();
			Iterator<Integer> it = key.iterator();
			while (it.hasNext()) {
				int nodeid = it.next();
				if (DCFaultround.containsKey(nodeid)) {
					if (Math.abs(faultroundMap.get(nodeid) - DCFaultround.get(nodeid)) <= samplesize)
						successdetectcount++;
					else if(faultroundMap.get(nodeid) > DCFaultround.get(nodeid))
						falsepositivecount++;
					else
						falsenegetivecount++;
					DCFaultround.remove(nodeid);
				} else{
					falsenegetivecount++;
				}
			}
			falsepositivecount += DCFaultround.size(); //System detect fault while no fault exists
			

			System.out.println("Num[" + num + "] at Round[" + x	+ "] procceding: "+ df.format((double) x * 100 / 30) + "%");
		}
		 outputPerformance(agent, falsepositivecount, falsenegetivecount,
				successdetectcount, totalrealfaultcount);
	}

	private static void outputPerformance(FileAccessAgent agent,
			int falsepositivecount, int falsenegetivecount,
			int successdetectcount, int totalrealfaultcount) {
		agent.writeLineToFile("Total Detection Accuracy:");
		 agent.writeLineToFile("Detection rate" + (double)successdetectcount * 100 / totalrealfaultcount + "%");
		 agent.writeLineToFile("False Positive rate" + (double)falsepositivecount * 100 / totalrealfaultcount + "%");
		 agent.writeLineToFile("False Negative rate" + (double)falsenegetivecount * 100 / totalrealfaultcount + "%");
	}

	private static void outputRealFaultRound(FileAccessAgent agent,
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

	private static void outputDCFaultRound(FileAccessAgent agent) {
		Set<Integer> key = DCFaultround.keySet();
		Iterator<Integer> iterator = key.iterator();
		agent.writeLineToFile("Device Fault Round:");
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			agent.writeLineToFile("[" + nodeid + "] "
					+ DCFaultround.get(nodeid));
		}
	}

	private static void proceedLine(Map<Integer, Double> readingpack,
			String line, ProcessManager manager,
			Map<Integer, Integer> faultroundMap) {
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

		Map<Integer, MarkedReading> markedreading = manager
				.markReadings(readingpack);

		Set<Integer> key = markedreading.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			try {
				if (markedreading.get(nodeid).deviceCondition() != DC
						.get(markedreading.get(nodeid).id())
						&& DC.get(markedreading.get(nodeid).id()) == 3) {
					DCFaultround.put(markedreading.get(nodeid).id(), count);

				}
			} catch (Exception e) {
			}
			DC.put(markedreading.get(nodeid).id(), markedreading.get(nodeid)
					.deviceCondition());
			// agent.writeLineToFile(message.toFormat());
		}
		// agent.writeLineToFile("\n");
		readingpack.clear();
	}
}
