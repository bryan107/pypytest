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

public abstract class Exp2 implements Runnable {

	private Log logger = LogFactory.getLog(Experiment2.class);
	private FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\NULL.txt",
			"C:\\TEST\\source_1__NUM_10__0.txt");

	private int round = 10800;
	private int count = 0;
	private int totalfaultcount = 0;
	private Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();
	private int num;

	public void run() {

	}

	private void runRoundSet(Map<Integer, Double> readingpack, int num,
			double CSErrorTolerance, String regressiontype) {
		agent.updatewritingpath("C:\\TEST\\Result_1_" + regressiontype
				+ "_NUM_" + num + "__CSET_" + CSErrorTolerance + ".txt");
		agent.writeLineToFile("CSET = " + CSErrorTolerance);
		totalfaultcount = 0;
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
				* 100 / (num * 30) + "%");
	}

	private void proceedLine(Map<Integer, Double> readingpack, String line,
			ProcessManager manager) {
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
