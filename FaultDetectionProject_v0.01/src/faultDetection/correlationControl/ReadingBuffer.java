package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ReadingBuffer {
	
	private Map<Integer, Double> readingbuffer = new HashMap<Integer, Double>();
	private Map<Integer, Boolean> readingbufferflag = new HashMap<Integer, Boolean>();
	
	public Map<Integer, Double> getBufferData(){
		return readingbuffer;
	}

	public double getBufferData(int nodeid){
		return readingbuffer.get(nodeid);
	}
	
	public void putBufferData(int nodeid, double reading){
		readingbuffer.put(nodeid, reading);
		raiseBufferFlag(nodeid);
	}
	
	public void refreshflag(){
		Set<Integer> keys = readingbufferflag.keySet();
		Iterator<Integer> iterator = keys.iterator();
		while(iterator.hasNext()){
			readingbufferflag.put(iterator.next(), false);
		}
	}
	
	private void raiseBufferFlag(int nodeid){
		readingbufferflag.put(nodeid, true);
	}
}
