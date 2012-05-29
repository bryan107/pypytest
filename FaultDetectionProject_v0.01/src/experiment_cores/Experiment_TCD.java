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
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

public class Experiment_TCD {
	private Log logger = LogFactory.getLog(Experiment_TCD.class);
	private int round = 10800;
	private int count = 0;
	private int eventcount = 0;
	private int LFcount = 0;
	private int totalfaultcount = 0;
	private int normalcount = 0;
	private int normalsectioncount = 1;
	private int renewcountdown = 0;
	private Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();
	private Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
	private String regressiontype = "";
	private double noise;
	private String filename;

	public Experiment_TCD(String filename){
		updatefileLocation(filename);
	}

	public void updatefileLocation(String filename){
		this.filename = filename;
	}


	public void runSet(int num, double lowerbound, double upperbound, double noise) {
		this.noise = noise;
		for (double CSErrorTolerance = lowerbound; CSErrorTolerance < (upperbound + 0.004); CSErrorTolerance += 0.005) {
			runRoundSet(readingpack, num, CSErrorTolerance, regressiontype);
		}
	}

	private void runRoundSet(Map<Integer, Double> readingpack, int num,
			double CSErrorTolerance, String regressiontype) {
		DecimalFormat df = new DecimalFormat("0.000");
		FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\"+ filename +"Result_1_"
				+ regressiontype + "__" + noise + "__NUM_" + num + "__CSET_"
				+ df.format(CSErrorTolerance) + ".txt", "C:\\TEST\\NULL.txt");
		agent.writeLineToFile("CSET = " + CSErrorTolerance);
		System.out.println(num);
		totalfaultcount = 0;
		for (int x = 0; x < 30; x++) {
			// ---------------Round Setup-------------------
			renewcountdown = 30;
			count = 0;
			DC.clear();
			DCFaultround.clear();
			ProcessManager manager = new ProcessManager();

			manager.updateCSErrorTolerance(CSErrorTolerance);
			String readingpath = "C:\\TEST\\" + filename + "source_1__" + noise + "__NUM_" + num + "__"
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
					proceedLine(readingpack, line, manager);
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
		
//		agent.writeLineToFile("Total Fault Count:" + (totalfaultcount) + " Normalseccount: "+ normalsectioncount);
		agent.writeLineToFile("Total Fault Ratio:" + (double)(totalfaultcount)
				* 100 / (num * 30) + "%");
		agent.writeLineToFile("Total Event Ratio:" + (double)(eventcount)
				* 100 / normalcount + "%");
	}

	private void proceedLine(Map<Integer, Double> readingpack,
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
				if (message.deviceCondition() == 0
						&& DC.get(message.id()) == 3) {
					DCFaultround.put(message.id(), count);
				}
				else if(message.deviceCondition() == 4 && DC.get(markedreading.get(message.id()).id()) == 3){
					LFcount++;
				}
			} catch (Exception e) {
			}
			DC.put(message.id(), message.deviceCondition());
			// agent.writeLineToFile(message.toFormat());
		}
		if(LFcount > (double)markedreading.size()/2){
			eventcount++;
			renewcountdown = 30;
		}
		else if(renewcountdown == 0){
			normalcount++;
		}
		else{
			renewcountdown--;
			if(renewcountdown == 0){
				normalsectioncount++;
			}
		}
		LFcount = 0;
		// agent.writeLineToFile("\n");
		readingpack.clear();
	}
}
