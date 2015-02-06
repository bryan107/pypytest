package mdfr.math.emd.datastructure;

import java.util.LinkedList;

import mdfr.math.emd.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IMFs {
	private static Log logger = LogFactory.getLog(IMFs.class);
	private LinkedList<LinkedList<Data>> imfs = new LinkedList<LinkedList<Data>>();
	
	/*
	 * Constructor
	 * */
	
	public IMFs(){
	}
	
	/*
	 * IMF operations
	 * */
	public void addIMF(LinkedList<Data> imf){
		imfs.add(imf);
	}
	
	public LinkedList<LinkedList<Data>> getIMFs(){
		return imfs;
	}
	
	public LinkedList<Data> getIMF(int level){
		try {
			return imfs.get(level);
		} catch (Exception e) {
			logger.error("No such imf as level " + level);
			e.printStackTrace();
		}
		return null;
	}
}
