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
//TODO NEED TEST
public class CopyOfExperiment_Events {
	private Log logger = LogFactory.getLog(CopyOfExperiment_Events.class);
	private int round = 10800;
	private int count = 0;
	private Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private Map<Integer, Integer> DCFaultround = new HashMap<Integer, Integer>();
	private Queue<Integer> neweventround = new LinkedList<Integer>();
	private Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
	private String regressiontype = "EventChange";
//	private static String faulttype;

	private String readingpath, writingpath;

	public CopyOfExperiment_Events(String writingpath, String readingpath){
		updateFilewriteLocation(writingpath);
		updateFileReadLocation(readingpath);
	}

	public void updateFilewriteLocation(String writingpath){
		this.writingpath = writingpath;
	}
	
	public void updateFileReadLocation(String readingpath){
		this.readingpath = readingpath;
	}
	
	public void runSet(int num, double lowerbound, double upperbound, double eventLFratio) {
		for (double eventchangeratio = lowerbound; eventchangeratio < upperbound + 0.00005; eventchangeratio += 0.001) {
			runRoundSet(readingpack, num, eventchangeratio, regressiontype, eventLFratio);
		}
	}

	private void runRoundSet(Map<Integer, Double> readingpack, int num,
			double eventchangeratio, String regressiontype, double eventLFratio) {
		DecimalFormat df = new DecimalFormat("0.000");
		FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\" + writingpath
				+ "Result_1_" + regressiontype + "__NUM_"
				+ num + "__EventChangeRatio_" + df.format(eventchangeratio) + "__"+ eventLFratio +".txt",
				"C:\\TEST\\NULL.txt");
		agent.writeLineToFile("Fault Ratio = " + eventchangeratio);
		System.out.println(num);
		int falsepositivecount = 0;
		int falsenegetivecount = 0;
		int successdetectcount = 0;
		int totalrealfaultcount = 0;
		int eventcount = 0;
		for (int x = 0; x < 30; x++) {
			// ---------------Round Setup-------------------
			count = 0;
			DC.clear();
			DCFaultround.clear();
			neweventround.clear();
			ProcessManager manager = new ProcessManager();
			manager.updateEventPower(2);
			manager.updateEventLFRatio(eventLFratio);
			String readingpath = "C:\\TEST\\" + this.readingpath + "source_1__" + df.format(eventchangeratio) + "__NUM_" + num
					+ "__" + x + ".txt";
			agent.updatereadingpath(readingpath);
			agent.setFileReader();
			agent.writeLineToFile("Set number[" + x + "]");
			// ---------------------------------------------
			// ---------------Header Setup------------------
			
			String line = agent.readLineFromFile();

			while (!line.equals("Readings")) {
				logger.info(line);
				line = agent.readLineFromFile();
			}
			line = agent.readLineFromFile();
			// ---------------------
			while (!line.equals("Event Change round:") && line != null) {
//				System.out.println("Round: " + count);
				proceedLine(readingpack, line, manager);
				line = agent.readLineFromFile();
				count++;
			}
			eventcount = outputEventRound(agent, eventcount, line);
			outputDCFaultRound(agent);			
			falsepositivecount += DCFaultround.size(); //System detect fault while no fault exists
			successdetectcount += neweventround.size(); //System detect events 
			

			System.out.println("Num[" + num + "] at Round[" + x	+ "] procceding: "+ df.format((double) x * 100 / 30) + "%");
		}
		 outputPerformance(agent, falsepositivecount, falsenegetivecount,
				successdetectcount, totalrealfaultcount, num, eventcount);
	}

	private int outputEventRound(FileAccessAgent agent, int eventcount,
			String line) {
		agent.writeLineToFile(line);
		line = agent.readLineFromFile();
		while(line != null){
			agent.writeLineToFile(line);
//			TODO TEST NEW¡@CODE
			if(Integer.valueOf(line) < round){
				eventcount++;
			}
			line = agent.readLineFromFile();
		}
		agent.writeLineToFile("Event Detected Round:");
		String eventdetectround = "";
		for(int round : neweventround){
			eventdetectround += round + " ";
		}
		agent.writeLineToFile(eventdetectround);
		return eventcount;
	}

	private void outputPerformance(FileAccessAgent agent,
			int falsepositivecount, int falsenegetivecount,
			int successdetectcount, int totalrealfaultcount, int num, int eventcount) {
		 agent.writeLineToFile("Total Detection Accuracy:");
		 agent.writeLineToFile("Detection rate: " + (double)successdetectcount * 100 / eventcount + "%");
		 agent.writeLineToFile("False Positive rate: " + (double)falsepositivecount * 100 / eventcount + "%");
//		 agent.writeLineToFile("False Negative rate: " + (double)falsenegetivecount * 100 / totalrealfaultcount + "%");
	}

//	private static void outputRealFaultRound(FileAccessAgent agent,
//			Map<Integer, Integer> faultroundMap) {
//		Set<Integer> key = faultroundMap.keySet();
//		Iterator<Integer> iterator = key.iterator();
//		agent.writeLineToFile("Real Fault Round:");
//		while (iterator.hasNext()) {
//			int nodeid = iterator.next();
//			agent.writeLineToFile("[" + nodeid + "] "
//					+ faultroundMap.get(nodeid));
//		}
//	}

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

	private void proceedLine(Map<Integer, Double> readingpack,
			String line, ProcessManager manager) {
		String[] readingstringpack = line.split("\t");
		for (int i = 0; i < readingstringpack.length; i++) {
			String[] reading = readingstringpack[i].split(":");
			// logger.info("T:" + reading.length);
			if (reading.length == 2)
				readingpack.put(Integer.valueOf(reading[0]),
						Double.valueOf(reading[1]));
			else{
				logger.error("Error Data Structure at round: " + count + " String length: " + reading.length);
				for(int w = 0 ; w < reading.length; w ++){
					System.out.println(reading[w]);
				}
			}
		
			
		}

		ProcessedReadingPack processedreadingpack = manager.markReadings(readingpack);
		
		Map<Integer, MarkedReading> markedreading = processedreadingpack.markedReadingPack();

		Set<Integer> key = markedreading.keySet();
		Iterator<Integer> iterator = key.iterator();
		int LFcount = 0;
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
		if(processedreadingpack.newEventOccurs()){
			neweventround.add(count);
		}
		readingpack.clear();
	}
}
