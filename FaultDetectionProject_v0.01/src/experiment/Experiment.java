package experiment;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import faultDetection.correlationControl.ProcessManager;
import fileAccessInterface.FileAccessAgent;

public class Experiment {
	private static Log logger = LogFactory.getLog(Experiment.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileAccessAgent agent = new FileAccessAgent("E:\\Result.txt", "E:\\Source.txt");
		ProcessManager manager = new ProcessManager();
		String line = agent.readLineFromFile();
		if(line != null){
			
		}
		
		Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
		
		String[] readingstringpack = line.split("\t");
		for(int i = 0 ; i < readingstringpack.length ; i++){
			String[] reading = readingstringpack[i].split(":");
			if(reading.length == 2)
				readingpack.put(Integer.valueOf(reading[0]), Double.valueOf(reading[1]));
			else
				logger.error("Error Data Structure");
		}
		
		
	}

}
