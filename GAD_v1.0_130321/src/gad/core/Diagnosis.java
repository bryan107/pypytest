package gad.core;

import java.util.HashMap;
import java.util.Map;

public class Diagnosis {
	private boolean eventoccurrence;
	private Map<Integer, Boolean> readingcondition = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> devicecondition = new HashMap<Integer, Boolean>();
	public Diagnosis(){
		
	}
	
	public void putEventOccurrence(boolean eventoccurrence){
		this.eventoccurrence = eventoccurrence;		
	}
	
	public void putReadingCondition(int nodeid, boolean readingcondition){
		this.readingcondition.put(nodeid, readingcondition);
	}
	
	public void putDeviceContidion(int nodeid, boolean devicecondition){
		this.devicecondition.put(nodeid, devicecondition);
	}	
	
	public boolean eventOccurrence(){
		return eventoccurrence;
	}
	
	public boolean readingCondition(int nodeid){
		return readingcondition.get(nodeid);
	}
	
	public boolean deviceCondition(int nodeid){
		return devicecondition.get(nodeid);
	}
	
	public Map<Integer, Boolean> readingCondition(){
		return readingcondition;
	}
		
}
