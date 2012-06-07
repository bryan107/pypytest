package dataGenerator;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class EventSourceManager {

	Map<Integer, EventSource> sourcepack = new HashMap<Integer, EventSource>();
	public EventSourceManager(){
		
	}
	
	public EventSource getEventSource(int sourceid){
		return sourcepack.get(sourceid);
	}
	
	public void addNewSource(int sourceid, int xlocation, int ylocation){
		sourcepack.put(sourceid, new EventSource(sourceid));
		DeployMap.getInstance().addEventSource(sourceid, xlocation, ylocation);
	}
	public boolean updateSourceLocation(int sourceid, int xlocation, int ylocation){
		if(sourcepack.containsKey(sourceid) == true){
			DeployMap.getInstance().addEventSource(sourceid, xlocation, ylocation);
			return true;
		}
		else{
			return false;	
		}
			
	}
	
	public void removeSource(int sourceid){
		sourcepack.remove(sourceid);
		DeployMap.getInstance().removeEventSource(sourceid);
	}
	
	public void clear(){
		sourcepack.clear();
		DeployMap.getInstance().removeEventSource();
	}
	
	public Event[] getEventSet(long sectionnumber){
		Event[] eventpack = new Event[sourcepack.size()];
		int packpointer = 0;
		for(EventSource source : sourcepack.values()){
			eventpack[packpointer] = source.getEvent(sectionnumber);
			packpointer++;
		}
		return eventpack;
	}
}
