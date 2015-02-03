package mdfr.math.emd;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IMFs {
	private static Log logger = LogFactory.getLog(IMFs.class);
	private int currentmax; 
	private int noiselevel, frequencylevel; // noiselevel is the max IMF level that include and below that are white noises.
											// frequencylevel is the max IMF level that include and below that are frequencies. 
	private LinkedList<LinkedList<Double>> imfs = new LinkedList<LinkedList<Double>>();
	
	/*
	 * Constructor
	 * */
	
	public IMFs(int maxnumber){
		currentmax = 0;
		noiselevel = 0;
		frequencylevel = 0;
	}
	
	/*
	 * Update IMF properties and parameters
	 * */	

	public void updateNoiseLevel(int noiselevel){
		this.noiselevel = noiselevel;
	}
	
	public void updateFrequencyLevel(int frequencylevel){
		this.frequencylevel = frequencylevel;
	}
	
	/*
	 * IMF operations
	 * */
	public void addIMF(LinkedList<Double> imf){
		imfs.add(imf);
	}
	
	public int size(){
		return currentmax;
	}
	
	public LinkedList<LinkedList<Double>> getIMFs(){
		return imfs;
	}
	
	public LinkedList<Double> getIMF(int level){
		try {
			return imfs.get(level);
		} catch (Exception e) {
			logger.error("No such imf as level " + level);
			e.printStackTrace();
		}
		return null;
	}
}
