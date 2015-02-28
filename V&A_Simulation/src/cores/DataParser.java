package cores;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import faultSymptom.ConstantDeviationFault;
import faultSymptom.FaultSymptom;
import faultSymptom.NoisyReadingFault;
import faultSymptom.NullFault;
import faultSymptom.ProportionalDeviationFault;
import faultSymptom.StuckOfReadingFault;
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

public class DataParser {

	private int count = 0;
	private final int datacount = 34945;
	private FileAccessAgent agent;
	private String readingpath, writingpath;
	private static Log logger = LogFactory.getLog(DataParser.class);
	private Map<String, Integer> labels;
	private String[] temp, humidity;
	private FaultSymptom fsymptom;
	private double faultratio;
	private double[] previous_t;
	private double[] previous_h;
	private int round;
	DecimalFormat df = new DecimalFormat("0.0000");
	Map<Integer, LinkedList<Integer>> fault_t = new HashMap<Integer, LinkedList<Integer>>();
	Map<Integer, LinkedList<Integer>> fault_h = new HashMap<Integer, LinkedList<Integer>>();
	public DataParser(String writingpath, String readingpath,
			Map<String, Integer> labels, FaultSymptom fsymptom,
			double faultratio, int round) {
		this.labels = labels;
		this.fsymptom = fsymptom;
		this.faultratio = faultratio;
		this.round = round;
		previous_t = new double[labels.size()];
		previous_h = new double[labels.size()];
		
		setupFileAccess(writingpath, readingpath);
	}

	public void run() {
		generateFaultsT();
		generateFaultsH();
		processLine();
		int[] templocation = processHeader(temp);
		logger.info("Processed Temp Header");
		int[] humilocation = processHeader(humidity);
		logger.info("Processed Humi Header");
		processData(templocation, humilocation);
	}
	
	private void generateFaultsT(){
		agent.updatewritingpath(writingpath + "V&A_temp_" + fsymptom.getKey() + "__fratio__" + df.format(faultratio) + ".csv");
		agent.writeLineToFile("Start");
		// -------------------------------------------------

		// -------------Fault Node and Round ---------------
		String outputstring = "";
		for(int section = 0 ; section < round ; section++){
			LinkedList<Integer> temp = new LinkedList<Integer>();
			for(int nodeid = 0 ; nodeid < 8 ; nodeid++){
				if(Math.random() <= faultratio && section >30){
					temp.add(nodeid);
					outputstring = outputstring + section + "::" + nodeid + ",";
				}
			}
			fault_t.put(section, temp);
		}
		agent.writeLineToFile(outputstring);
		// -------------------------------------------------

		agent.writeLineToFile("Readings");
	}
	
	private void generateFaultsH(){
		agent.updatewritingpath(writingpath + "V&A_humi_" + fsymptom.getKey() + "__fratio__" + df.format(faultratio) + ".csv");
		agent.writeLineToFile("Start");
		// -------------------------------------------------

		// -------------Fault Node and Round ---------------
		String outputstring = "";
		for(int section = 0 ; section < round ; section++){
			LinkedList<Integer> temp = new LinkedList<Integer>();
			for(int nodeid = 0 ; nodeid < 8 ; nodeid++){
				if(Math.random() <= faultratio && section >30){
					temp.add(nodeid);
					outputstring = outputstring + section + "::" + nodeid + ",";
				}
			}
			fault_h.put(section, temp);
		}
		agent.writeLineToFile(outputstring);
		// -------------------------------------------------

		agent.writeLineToFile("Readings");
	}

	private int[] processHeader(String[] readings) {
		DecimalFormat df = new DecimalFormat("0.0000");
		String line = ",";
		int i = 0;
		int[] location = new int[labels.size()];
		Iterator<String> it = labels.keySet().iterator();
		while (it.hasNext()) {
			String label = it.next();
			if(readings == temp){
				label = label + " Temperature (C)";
			} else{
				label = label + " Humidity (RH(%))";
			}
			
			for (int j = 0, n = readings.length; j < n; j++) {
				if (readings[j].equals(label)) {
					location[i] = j;
					i++;
					break;
				}
			}
		}
		Iterator<String> it2 = labels.keySet().iterator();
		while (it2.hasNext()) {
			String nodelabel = it2.next();
			line = line + labels.get(nodelabel) + ",";
		}
		if (readings == temp) {
			agent.updatewritingpath(writingpath + "V&A_temp_" + fsymptom.getKey() + "__fratio__" + df.format(faultratio) + ".csv");
		} else {
			agent.updatewritingpath(writingpath + "V&A_humi_" + fsymptom.getKey() + "__fratio__" + df.format(faultratio) + ".csv");
		}
		agent.writeLineToFile(line);

		return location;
	}

	private void processData(int[] templocation, int[] humilocation) {
		DecimalFormat df = new DecimalFormat("0.0000");
		
		while (processLine()) {
			String templine = count + ",";
			String humiline = count + ",";

			// Copy referenced data to filtered arrays
			for (int i = 0, m = templocation.length; i < m; i++) {
				try {
					double t;
					if(fault_t.get(count).contains(i)){
						t = fsymptom.getValue(Double.valueOf(temp[templocation[i]]));
					} else{
						t = Double.valueOf(temp[templocation[i]]);
					}
					templine = templine + t + ",";
					previous_t[i] = t;
				} catch (Exception e) {
					templine = templine + previous_t[i] + ",";
//					logger.error("temp = null");
				}
				
			}
			for (int i = 0, m = humilocation.length; i < m; i++) {
				try {
					double t;
					if(fault_h.get(count).contains(i)){
						t = fsymptom.getValue(Double.valueOf(humidity[humilocation[i]]));
					} else{
						t = Double.valueOf(humidity[humilocation[i]]);
					}
					humiline = humiline + t + ",";
					previous_h[i] = t;
				} catch (Exception e) {
					humiline = humiline + previous_h[i] +",";
//					logger.error("Humi = null");
				}
			}
			// Write Line to file
			agent.updatewritingpath(writingpath + "V&A_temp_" + fsymptom.getKey() + "__fratio__" + df.format(faultratio) + ".csv");
			agent.writeLineToFile(templine);
			agent.updatewritingpath(writingpath + "V&A_humi_" + fsymptom.getKey() + "__fratio__" + df.format(faultratio) + ".csv");
			agent.writeLineToFile(humiline);
			
			count++;
			if (count % 394 == 0) {
				System.out.println("Process..."
						+ df.format(count * 100 / datacount) + "%");
			}
		}
	}

	private boolean processLine() {
		String line = agent.readLineFromFile();
		if (line != null) {
			String[] reading = line.split(",");

			// Variables
			int length = (reading.length - 1) / 2;

			temp = new String[length];
			humidity = new String[length];

			// Copy Arrays
			for (int i = 0, m = temp.length; i < m; i++) {
				temp[i] = reading[1 + i * 2];
				humidity[i] = reading[2 + i * 2];
			}
			return true;
		} else {
			return false;
		}

	}

	public void setupFileAccess(String writingpath, String readingpath) {
		this.writingpath = writingpath;
		this.readingpath = readingpath;
		agent = new FileAccessAgent(this.writingpath, this.readingpath);
	}
}