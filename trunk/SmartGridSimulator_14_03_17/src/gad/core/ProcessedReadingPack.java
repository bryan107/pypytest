package gad.core;

import java.util.HashMap;
import java.util.Map;

public class ProcessedReadingPack {
	private Map<Integer, MarkedReading> markedreadingpack = new HashMap<Integer, MarkedReading>();
	private boolean eventoccurence;
	
	public ProcessedReadingPack(Map<Integer, MarkedReading> markedreadingpack, boolean eventoccurence){
		this.markedreadingpack = markedreadingpack;
		this.eventoccurence = eventoccurence;
	}
	
	public Map<Integer, MarkedReading> markedReadingPack(){
		return markedreadingpack;
	}
	
	public boolean newEventOccurs(){
		return eventoccurence;
	}
	
}
