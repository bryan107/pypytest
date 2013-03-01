package cores;

import java.text.DecimalFormat;

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
	private double count = 0;
	private final int datacount = 34945;
	private FileAccessAgent agent;
	private String readingpath, writingpath;
	private static Log logger = LogFactory.getLog(DataParser.class);
	private String[] labels;
	private String[] temp, humidity;
	private FaultSymptom fsymptom;

	public DataParser(String writingpath, String readingpath, String[] labels) {
		this.labels = labels;
		setupFileAccess(writingpath, readingpath);
	}

	public void run() {
		processLine();
		int[] templocation = processHeader(temp);
		logger.info("Processed Temp Header");
		int[] humilocation = processHeader(humidity);
		logger.info("Processed Humi Header");

		// NULL FAULT
		fsymptom = new NullFault();
		processData(templocation, humilocation);

		// PROPORTIONAL DEVIATION

		fsymptom = new ProportionalDeviationFault(Double.valueOf(PropertyAgent
				.getInstance().getProperties("Fault",
						"Proportional.Deviation.Attribute")));
		processData(templocation, humilocation);

		// CONSTANT DEVIATION
		fsymptom = new ConstantDeviationFault(Double.valueOf(PropertyAgent
				.getInstance().getProperties("Fault",
						"Constant.Deviation.Attribute")));
		processData(templocation, humilocation);

		// NOISY
		fsymptom = new NoisyReadingFault(Double.valueOf(PropertyAgent
				.getInstance().getProperties("Fault", "Noisy.Attribute")));
		processData(templocation, humilocation);

		// STUCK
		fsymptom = new StuckOfReadingFault();
		processData(templocation, humilocation);
	}

	private int[] processHeader(String[] readings) {
		String line = "";
		int[] location = new int[labels.length];
		for (int i = 0, m = labels.length; i < m; i++) {
			for (int j = 0, n = readings.length; j < n; j++) {
				if (readings[j].contains(labels[i])) {
					location[i] = j;
					break;
				}
			}
		}

		for (String title : labels) {
			line = line + title + ",";
		}
		if (readings == temp) {
			agent.updatewritingpath(writingpath + "V&A_temp.csv");
		} else {
			agent.updatewritingpath(writingpath + "V&A_humi.csv");
		}
		agent.writeLineToFile(line);

		return location;
	}

	private void processData(int[] templocation, int[] humilocation) {
		double[] temp = new double[this.temp.length];
		double[] humidity = new double[this.humidity.length];
		DecimalFormat df = new DecimalFormat("0.00");
		while (processLine()) {
			// filtered data
			String templine = "";
			String humiline = "";
			if (count % 394 == 0) {
				System.out.println("Process..."
						+ df.format(count * 100 / datacount) + "%");
			}

			// Copy referenced data to filtered arrays
			for (int i = 0, m = templocation.length; i < m; i++) {
				templine = templine + temp[templocation[i]] + ",";
			}
			for (int i = 0, m = humilocation.length; i < m; i++) {
				humiline = humiline + humidity[humilocation[i]] + ",";
			}
			// Write Line to file
			agent.updatewritingpath(writingpath + "V&A_temp_" + fsymptom.getKey() + ".csv");
			agent.writeLineToFile(templine);
			agent.updatewritingpath(writingpath + "V&A_humi_" + fsymptom.getKey() + ".csv");
			agent.writeLineToFile(humiline);
			count++;
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
