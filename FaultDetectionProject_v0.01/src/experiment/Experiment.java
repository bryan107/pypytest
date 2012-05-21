package experiment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.jmx.Agent;

import faultDetection.correlationControl.MarkedReading;
import faultDetection.correlationControl.ProcessManager;
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

public class Experiment {
	private static Log logger = LogFactory.getLog(Experiment.class);
	private static FileAccessAgent agent = new FileAccessAgent(
			"C:\\TEST\\NULL.txt",
			"C:\\TEST\\source_1__NUM_10__0.txt");

	private static int round = 10800;
	private static int count = 0;
	private static int totalfaultcount = 0;
	private static Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private static Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// ProcessManager manager = new ProcessManager();
		agent.setFileReader();
		Map<Integer, Double> readingpack = new HashMap<Integer, Double>();

		int num = 5;
		for (double CSErrorTolerance = 0.10; CSErrorTolerance <= 0.15; CSErrorTolerance += 0.005) {
			PropertyAgent.getInstance().setProperties("FDC","CSErrorTolerance", CSErrorTolerance + "");
			runRoundSet(readingpack, num, CSErrorTolerance);
		}
		num = 7;
		for (double CSErrorTolerance = 0.10; CSErrorTolerance <= 0.15; CSErrorTolerance += 0.005) {
			PropertyAgent.getInstance().setProperties("FDC","CSErrorTolerance", CSErrorTolerance + "");
			runRoundSet(readingpack, num, CSErrorTolerance);
		}
		num = 10;
		for (double CSErrorTolerance = 0.10; CSErrorTolerance < 0.15; CSErrorTolerance += 0.05) {
			PropertyAgent.getInstance().setProperties("FDC","CSErrorTolerance", CSErrorTolerance + "");
			runRoundSet(readingpack, num, CSErrorTolerance);
		}
		num = 15;
		for (double CSErrorTolerance = 0.10; CSErrorTolerance < 0.15; CSErrorTolerance += 0.05) {
			PropertyAgent.getInstance().setProperties("FDC","CSErrorTolerance", CSErrorTolerance + "");
			runRoundSet(readingpack, num, CSErrorTolerance);
		}
		num = 20;
		for (double CSErrorTolerance = 0.10; CSErrorTolerance < 0.15; CSErrorTolerance += 0.05) {
			PropertyAgent.getInstance().setProperties("FDC","CSErrorTolerance", CSErrorTolerance + "");
			runRoundSet(readingpack, num, CSErrorTolerance);
		}

	}

	private static void runRoundSet(Map<Integer, Double> readingpack, int num,
			double CSErrorTolerance) {
		agent.updatewritingpath("C:\\TEST\\Result_1__NUM_" + num + "__CSET_"
				+ CSErrorTolerance + ".txt");
		agent.writeLineToFile("CSET = " + CSErrorTolerance);
		for (int x = 0; x < 30; x++) {
			// ---------------Round Setup-------------------
			count = 0;
			DC.clear();
			DCFaultround.clear();
			ProcessManager manager = new ProcessManager();
			String readingpath = "C:\\TEST\\source_1__NUM_" + num + "__" + x
					+ ".txt";
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
					proceedLine(readingpack, line, manager);
				} else
					break;
				DecimalFormat df = new DecimalFormat("00.00");
				count++;
				System.out.println("Round[" + x + "] proceding: "
						+ df.format((double) count * 100 / round) + "%");
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
		}
		agent.writeLineToFile("Total Fault Ratio:" + (double) totalfaultcount
				* 100 / (10 * 30) + "%");
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
