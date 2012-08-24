package experiment_cores;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import faultDetection.correlationControl.MarkedReading;
import faultDetection.correlationControl.ProcessManager;
import faultDetection.correlationControl.ProcessedReadingPack;
import fileAccessInterface.FileAccessAgent;

public class Experiment_Events {
	private int round = 10800;
	private int count = 0;
	private Map<Integer, Short> DC = new HashMap<Integer, Short>();
	private Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
	private String regressiontype = "EventChange";
	private DataParsers dataparser = new EventParsers();
	private PerformanceAnalyzer pfanalyzer;

	private String readingpath, writingpath;

	public Experiment_Events(String writingpath, String readingpath, DataParsers dataparser){
		updateFilewriteLocation(writingpath);
		updateFileReadLocation(readingpath);
		updateDataParser(dataparser);
	}

	public void updateDataParser(DataParsers dataparser){
		this.dataparser = dataparser;
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
		FileAccessAgent agent = new FileAccessAgent( writingpath
				+ "Result_1_" + regressiontype + "__NUM_"
				+ num + "__EventChangeRatio_" + df.format(eventchangeratio) + "__"+ eventLFratio +".txt",
				"C:\\TEST\\NULL.txt");
		agent.writeLineToFile("Fault Ratio = " + eventchangeratio);
		System.out.println(num);

		for (int x = 0; x < 30; x++) {
			// ---------------Round Setup-------------------
			count = 0;
			DC.clear();
			pfanalyzer.resetRound();
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
			dataparser.proceedHeader(agent);
			// ---------------------------------------------
			while (!dataparser.reachFileEnd(agent, "Event Change round:")) {
				proceedLine(readingpack, manager);
			}
			outputEventRound(agent);
			pfanalyzer.outputDCFaultRound(agent);			
			pfanalyzer.calcPerformance();
			System.out.println("Num[" + num + "] at Round[" + x	+ "] procceding: "+ df.format((double) x * 100 / 30) + "%");
		}
		 pfanalyzer.outputPerformance(agent);
	}

	private void outputEventRound(FileAccessAgent agent) {
		String line = agent.readLineFromFile();
		while(line != null){
			agent.writeLineToFile(line);
			if(Integer.valueOf(line) < round){
				pfanalyzer.realeventround.add(Integer.valueOf(line));
			}
			line = agent.readLineFromFile();
		}
		agent.writeLineToFile("Event Detected Round:");
		String eventdetectround = "";
		for(int round : pfanalyzer.detecteventround){
			eventdetectround += round + " ";
		}
		agent.writeLineToFile(eventdetectround);
	}


	private void proceedLine(Map<Integer, Double> readingpack, ProcessManager manager) {
		readingpack = dataparser.getReadingPack();
		ProcessedReadingPack processedreadingpack = manager.markReadings(readingpack);
		pfanalyzer.resultAccumulation(processedreadingpack, count);
		readingpack.clear();
		count++;
	}

}
