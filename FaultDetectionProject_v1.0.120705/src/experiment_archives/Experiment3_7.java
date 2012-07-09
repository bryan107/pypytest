package experiment_archives;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import faultDetection.correlationControl.MarkedReading;
import faultDetection.correlationControl.ProcessManager;
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

public class Experiment3_7 {
	private static Log logger = LogFactory.getLog(Experiment3_7.class);
	private static int round = 10800;
	private static int count = 0;
	private static int totalfaultcount = 0;
	private static Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private static Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();
	private static Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
	private static String regressiontype = "";
	private static double noise = 0.03;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// agent.setFileReader();

		// runSets(readingpack , regressiontype);
		noise = 0.03;
		runSet(20, 0.18, 0.20);


	}

	private static void runSet(int num, double lowerbound, double upperbound) {
		for (double CSErrorTolerance = lowerbound; CSErrorTolerance < (upperbound + 0.004); CSErrorTolerance += 0.005) {
			runRoundSet(readingpack, num, CSErrorTolerance, regressiontype);
		}
	}

	private static void runRoundSet(Map<Integer, Double> readingpack, int num,
			double CSErrorTolerance, String regressiontype) {
		DecimalFormat df = new DecimalFormat("0.000");
		FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\Result_1_"
				+ regressiontype + "__" + noise + "__NUM_" + num + "__CSET_"
				+ df.format(CSErrorTolerance) + ".txt", "C:\\TEST\\NULL.txt");
		agent.writeLineToFile("CSET = " + CSErrorTolerance);
		System.out.println(num);
		totalfaultcount = 0;
		for (int x = 0; x < 30; x++) {
			// ---------------Round Setup-------------------
			count = 0;
			DC.clear();
			DCFaultround.clear();
			ProcessManager manager = new ProcessManager();

			manager.updateCSErrorTolerance(CSErrorTolerance);
			String readingpath = "C:\\TEST\\source_1__" + noise + "__NUM_" + num + "__"
					+ x + ".txt";
			agent.updatereadingpath(readingpath);
			agent.setFileReader();
			// ---------------------------------------------

			String line = agent.readLineFromFile();
			while (!line.equals("Start")) {
				logger.info(line);
				line = agent.readLineFromFile();
			}

			while (true) {
				line = agent.readLineFromFile();
				if (line != null) {
					proceedLine(readingpack, line , manager);
					count++;
				} else
					break;
			}

			Set<Integer> key = DCFaultround.keySet();
			Iterator<Integer> iterator = key.iterator();
			agent.writeLineToFile("Device Fault Round:");
			while (iterator.hasNext()) {
				int nodeid = iterator.next();
				agent.writeLineToFile("[" + nodeid + "] "
						+ DCFaultround.get(nodeid));
			}
			totalfaultcount += DCFaultround.size();
			System.out
					.println("Num[" + num + "] at Round[" + x
							+ "] procceding: "
							+ df.format((double) x * 100 / 30) + "%");

		}
		agent.writeLineToFile("Total Fault Ratio:" + (double) totalfaultcount
				* 100 / (num * 30) + "%");
	}

	private static void proceedLine(Map<Integer, Double> readingpack,
			String line, ProcessManager manager) {
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

		// Set<Integer> key = markedreading.keySet();
		// Iterator<Integer> iterator = key.iterator();
		for (MarkedReading message : markedreading.values()) {
			try {
				if (message.deviceCondition() != DC.get(message.id())
						&& DC.get(message.id()) == 3) {
					DCFaultround.put(message.id(), count);
				}
			} catch (Exception e) {
			}
			DC.put(message.id(), message.deviceCondition());
			// agent.writeLineToFile(message.toFormat());
		}
		// agent.writeLineToFile("\n");
		readingpack.clear();
	}
}
