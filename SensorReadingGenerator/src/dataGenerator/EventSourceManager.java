package dataGenerator;

import java.util.HashMap;
import java.util.Map;

public class EventSourceManager {

	Map<Integer, EventSource> sourcepack = new HashMap<Integer, EventSource>();
	public EventSourceManager(){
		
	}
	
	public void addNewSource(int sourceid, int xlocation, int ylocation){
		sourcepack.put(sourceid, new EventSource(sourceid));
		DeployMap.getInstance().addEventSource(sourceid, xlocation, ylocation);
	}
	
	public void removeSource(int sourceid){
		sourcepack.remove(sourceid);
		DeployMap.getInstance().removeEventSource(sourceid);
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
