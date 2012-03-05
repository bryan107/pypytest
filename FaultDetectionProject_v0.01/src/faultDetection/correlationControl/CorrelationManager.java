package faultDetection.correlationControl;

import java.util.ArrayList;
import java.util.List;

public class CorrelationManager {
	private IntervalControl control;
	private Correlation[][] correlationarray;
	private List<Integer> nodeindex = new ArrayList<Integer>(); 
	
		
	/*
	 * Under Working............................
	 *  
	 * 
	 */
	public CorrelationManager(IntervalControl control){
		this.control = control;
	}
	
	public boolean updateIntervalControl(IntervalControl control){
		try {
			this.control = control;
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	protected void intervalControl(){
		
	}
	
	private boolean addNewNode(){
		
		return true;
	}
	public boolean updateReading(){
		
		return true;
	}
	
	private double[] aggregateReadings(){ //for updateCorrelation()
		
		return null;
	}
	
	public boolean updateCorrelation(){
		
		return true;
	}
	

}
