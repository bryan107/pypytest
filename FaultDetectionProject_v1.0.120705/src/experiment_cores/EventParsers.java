package experiment_cores;

import java.util.HashMap;
import java.util.Map;

import fileAccessInterface.FileAccessAgent;

public class EventParsers extends DataParsers {

	@Override
	public String proceedHeader(FileAccessAgent agent) {
		line = agent.readLineFromFile();
		while (!line.equals("Readings")) {
			logger.info(line);
			line = agent.readLineFromFile();
		}
		return line;
	}

	@Override
	public Map<Integer, Double> getReadingPack() {
		Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
		String[] readingstringpack = line.split("\t");
		for (int i = 0; i < readingstringpack.length; i++) {
			String[] reading = readingstringpack[i].split(":");
			// logger.info("T:" + reading.length);
			if (reading.length == 2)
				readingpack.put(Integer.valueOf(reading[0]),
						Double.valueOf(reading[1]));
			else{
				logger.error("Error Data Structure with String length: " + reading.length);
				for(int w = 0 ; w < reading.length; w ++){
					System.out.println(reading[w]);
				}
			}	
		}
		return readingpack;
	}
	
	@Override
	public boolean reachFileEnd(FileAccessAgent agent ,String key){
		line = agent.readLineFromFile();
		if(!line.equals(key) && line != null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public int getEventCount(){
		int eventcount = 0;
		
		return eventcount;
	}
}
