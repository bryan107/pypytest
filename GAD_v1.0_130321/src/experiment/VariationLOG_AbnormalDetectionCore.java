package experiment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import variation.core.Variation;

import dfd.core.DFD;

import fileAccessInterface.FileAccessAgent;
import gad.core.GAD;
import gad.core.MarkedReading;
import gad.core.ProcessedReadingPack;

public class VariationLOG_AbnormalDetectionCore {
	private final short FT = 0;

	private double threshold = 4;
	private double totalcheck = 0;
	private double totalabnormal = 0;
	private double totalfalsenegetive = 0;
	private double totalfalsepositive = 0;
	private double abnormalnumber = 0;
	private LinkedList<String> checklist = new LinkedList<String>();
	private static Log logger = LogFactory.getLog(VariationLOG_AbnormalDetectionCore.class);
	private String filewritelocation, filereadlocation, abnormaltype;

	// -----------User setup--------------

	// ------------------------------------
	public VariationLOG_AbnormalDetectionCore(String filewritelocation,
			String filereadlocation, String abnormaltype) {
		updateFileWriteLocation(filewritelocation);
		updateFileReadLocation(filereadlocation);
		updateAbnormalType(abnormaltype);
	}

	public void runSet(int num, double lowerratio, double upperratio) {
		FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\NULL.txt",
				"C:\\TEST\\NULL.txt");
		agent.updatewritingpath(filewritelocation + "\\Result_1__"
				+ abnormaltype + "__NUM_"+ num +".csv");
		for (double faultratio = lowerratio; faultratio < upperratio + 0.00001; faultratio *= 10) {
			runRoundSet(num, faultratio, agent);
			DecimalFormat df = new DecimalFormat("0.0000");
			String writeline = "";
			writeline = "Fault Ratio:," + df.format(faultratio) + ",Total Check:," + df.format(totalcheck); 
			writeline = writeline + ",Total False-Negetive:," + totalfalsenegetive;
			writeline = writeline + ",Total False-Positive:," + totalfalsepositive;
			writeline = writeline + ",Total False-Negetive (%):," + totalfalsenegetive * 100/totalabnormal + ",%";
			writeline = writeline + ",Total False-Positive (%):," + totalfalsepositive* 100/(10800*10*num-totalabnormal) + ",%";
			agent.writeLineToFile(writeline);
			logger.info("Processed..." + df.format(faultratio * 100 / upperratio) + "%");
		}
	}

	public void runRoundSet(int num, double faultratio, FileAccessAgent agent) {
		totalcheck = 0;
		totalabnormal = 0;
		totalfalsenegetive = 0;
		totalfalsepositive = 0;
		DecimalFormat df = new DecimalFormat("0.0000");
//		agent.updatewritingpath(filewritelocation + "\\Result_1__"
//				+ abnormaltype + "__" + df.format(faultratio) + ".csv");

		for (int round = 0; round < 10; round++) {
			String readingpath = filereadlocation + "\\source_1__"
					+ abnormaltype + "__" + df.format(faultratio) + "__NUM_"
					+ num + "__" + round + ".csv";
			logger.info("\nProcess: " + readingpath);
			agent.updatereadingpath(readingpath);
			agent.setFileReader();
			iterateAFile(agent);
		}
		

	}

	private void iterateAFile(FileAccessAgent agent) {
		
//		DFD dfd = new DFD(threshold);
		Variation va = new Variation(threshold);
		va.resetSMDB();
		// ---------------Header Setup------------------
		String line;
		// Variables that will be used in head*
		abnormalnumber = 0;
		checklist.clear();
		
		fileHeaderProcessing(agent);
		line = agent.readLineFromFile();
		while (!line.equals("Readings")) {
			logger.info(line);
			line = agent.readLineFromFile();
		}
		line = agent.readLineFromFile();
		while (line != null) {
			int inrounds;
			Map<Integer, Double> reading = new HashMap<Integer, Double>();
			String[] split = line.split(",");
			if (split[0].equals(" ")) {
				line = agent.readLineFromFile();
				continue;
			}
			// Put reading to map
			inrounds = Integer.valueOf(split[0]);
			for (int i = 1; i < split.length; i++) {
				reading.put(i - 1, Double.valueOf(split[i]));
			}
			// Process readings
			Map<Integer, Short> rc = va.markReading(reading);
			Iterator<Integer> it = rc.keySet().iterator();
			while (it.hasNext()) {
				int nodeid = it.next();
				if (rc.get(nodeid) == FT) {
					totalcheck++;
					if(checklist.contains(inrounds + "::" + nodeid)){
						checklist.remove(inrounds + "::" + nodeid);
						abnormalnumber--;
					} else{
						totalfalsepositive++;
					}
				}
			}
			line = agent.readLineFromFile();
		}
		totalfalsenegetive += abnormalnumber;

	}

	private void fileHeaderProcessing(
			FileAccessAgent agent) {
		String line;
		line = agent.readLineFromFile();
		// Search Start info
		while (!line.equals("Start")) {
//			logger.info(line);
			line = agent.readLineFromFile();
		}
		// process info
		line = agent.readLineFromFile();
		String[] ABround = line.split(",");
		abnormalnumber = ABround.length;
		totalabnormal += abnormalnumber;
		for (int round = 0; round < ABround.length; round++) {
			checklist.add(ABround[round]);
		}
	}

	public void updateFileWriteLocation(String filewritelocation) {
		this.filewritelocation = filewritelocation;
	}

	public void updateFileReadLocation(String filereadlocation) {
		this.filereadlocation = filereadlocation;
	}

	public void updateAbnormalType(String abnormaltype) {
		this.abnormaltype = abnormaltype;
	}

}
