package experiment_cores;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fileAccessInterface.FileAccessAgent;
//TODO TEST
public abstract class DataParsers {
	protected Log logger = LogFactory.getLog(DataParsers.class);
	protected String line;
	public DataParsers(){
		
	}
	
	public String proceedHeader(FileAccessAgent agent) {
		return null;
	}
	public Map<Integer, Double> getReadingPack() {
		return null;
	}
	public boolean reachFileEnd(FileAccessAgent agent, String key) {
		return false;
	}
}
