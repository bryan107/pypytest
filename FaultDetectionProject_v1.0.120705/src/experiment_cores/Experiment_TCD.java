package experiment_cores;

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
import faultDetection.correlationControl.ProcessedReadingPack;
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;
//TODO NEED TEST
public class Experiment_TCD {
	private Log logger = LogFactory.getLog(Experiment_TCD.class);
	private int totalround = 10800;
	private int count = 0;
	private int eventcount = 0;
	private int totalfaultcount = 0;
	private Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();
	private Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
	private String regressiontype = "";
	private double noise;
	private String writingpath;
	private String readingpath;

	private final short FT = 0;
	// private final short LF = 1;
	// private final short LG = 2;
	private final short GD = 3;
	private final short UNKNOWN = 4;

	public Experiment_TCD(String writingpath, String readingpath) {
		updateWritingPath(writingpath);
		updateReadingPath(readingpath);
	}

	public void updateReadingPath(String readingpath) {
		this.readingpath = readingpath;
	}

	public void updateWritingPath(String writingpath) {
		this.writingpath = writingpath;
	}

	public void runSet(int nodenumber, double lowerbound, double upperbound,
			double noise) {
		this.noise = noise;
		for (double CSErrorTolerance = lowerbound; CSErrorTolerance < (upperbound + 0.004); CSErrorTolerance += 0.005) {
			runRoundSet(nodenumber, CSErrorTolerance, regressiontype);
		}
	}

	private void runRoundSet(int nodenumber,
			double CSTolerableNoise, String regressiontype) {
		DecimalFormat df = new DecimalFormat("0.000");
		FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\" + writingpath
				+ "Result_1_" + regressiontype + "__" + noise + "__NUM_" + nodenumber
				+ "__CSET_" + df.format(CSTolerableNoise) + ".txt",
				"C:\\TEST\\NULL.txt");
		agent.writeLineToFile("CSET = " + CSTolerableNoise);
		System.out.println(nodenumber);
		totalfaultcount = 0;
		for (int round = 0; round < 30; round++) {
			// ---------------Round Setup-------------------
			count = 0;
			DC.clear();
			DCFaultround.clear();
			ProcessManager manager = new ProcessManager();
			manager.updateCSTolerableNoise(CSTolerableNoise);
			String readingpath = "C:\\TEST\\" + this.readingpath + "source_1__"
					+ noise + "__NUM_" + nodenumber + "__" + round + ".txt";
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
					proceedLine(line, manager);
					count++;
				} else
					break;
			}

			printRoundResult(nodenumber, df, agent, round);
		}
		printResult(nodenumber, agent);
	}

	private void printRoundResult(int nodenumber, DecimalFormat df,
			FileAccessAgent agent, int round) {
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
				.println("Num[" + nodenumber + "] at Round[" + round
						+ "] procceding: "
						+ df.format((double) round * 100 / 30) + "%");
	}

	private void printResult(int nodenumber, FileAccessAgent agent) {
		// agent.writeLineToFile("Total Fault Count:" + (totalfaultcount) +
		// " Normalseccount: "+ normalsectioncount);
		agent.writeLineToFile("Total Fault Ratio:" + (double) (totalfaultcount)
				* 100 / (nodenumber * 30) + "%");
		agent.writeLineToFile("Total Event Ratio:" + (double) (eventcount)
				* 100 / (totalround - eventcount*30 - 30) + "%");
	}

	private void proceedLine(String line,
			ProcessManager manager) {
		generateReadingPack(line);
		ProcessedReadingPack processedreadingpack = manager
				.markReadings(readingpack);
		checkFaultOccurence(processedreadingpack);
		checkEventOccurence(processedreadingpack);
		// agent.writeLineToFile("\n");
		readingpack.clear();
	}

	private void generateReadingPack(String line) {
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
	}

	private void checkFaultOccurence(ProcessedReadingPack processedreadingpack) {
		for (MarkedReading message : processedreadingpack.markedReadingPack()
				.values()) {
			try {
				if (message.deviceCondition() == FT
						&& DC.get(message.id()) == GD) {
					DCFaultround.put(message.id(), count);
				}
			} catch (Exception e) {
			}
			DC.put(message.id(), message.deviceCondition());
			// agent.writeLineToFile(message.toFormat());
		}
	}

	private void checkEventOccurence(ProcessedReadingPack processedreadingpack) {
		if (processedreadingpack.newEventOccurs()) {
			eventcount++;
		}
	}
}
