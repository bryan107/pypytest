package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProcessManager {	
	//---------------------Private variables------------------------
	private ReadingBuffer readingbuffer = new ReadingBuffer();
//	private IntervalControl intervalcontrol;
	private CorrelationManager manager;
	private Map<Integer, double[]> markedreading = new HashMap<Integer, double[]>();
	//--------------------------------------------------------------
	//----------------------Constructor-----------------------------
	public ProcessManager(int samplesize, int eventpower){
//		updateIntervalController(intervalControl);
//		manager = new CorrelationManager(samplesize, eventpower);
	}
	//--------------------------------------------------------------
	//---------------------Public Functions--------------------------
//	public void updateIntervalController(IntervalControl intervalControl){
//		this.intervalcontrol = intervalControl;
//	}
	public void updateCorrelationSampleSize(int size){
		manager.updateSampleSize(size);
	}
	public void updateDFDThreshold(double threshold){
		
	}

	//TODO To complete the Function
	public Map<Integer, Double> MarkReadings(Map<Integer, Double> readingpack){
		
		return null;
	}
	public void putReading(int nodeid, double reading){
		readingbuffer.putBufferData(nodeid, reading);
	}
	public void putReading(Map<Integer, Double> readingpack){
		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(double reading : readingpack.values()){
			int nodeid = iterator.next();
			putReading(nodeid, reading);
		}
	}
	public Map<Integer, double[]> getMarkedReading(){
		return markedreading;
	}
	public double[] getMarkedReading(int nodeid){
		return markedreading.get(nodeid);
	}
	
	//-----------------------------------------------------------------
	//----------------------Private Functions--------------------------
	
	
	//TODO Require a buffer interval controller to control the correlation table updating mechanism & updating rate
//	private void intervalControl(){
//		
//	}
}
