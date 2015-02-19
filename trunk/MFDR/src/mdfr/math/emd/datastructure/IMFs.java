package mdfr.math.emd.datastructure;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IMFs {
	private static Log logger = LogFactory.getLog(IMFs.class);
	private LinkedList<IMF> imfs = new LinkedList<IMF>();
	
	/*
	 * Constructor
	 * */
	
	public IMFs(){
	}
	
	/*
	 * IMF operations
	 * */
	public void addIMF(IMF imf){
		imfs.add(imf);
	}
	
	public int size(){
		return imfs.size();
	}
	
	public LinkedList<IMF> getIMFs(){
		return imfs;
	}
	
	public IMF getIMF(int level){
		try {
			return imfs.get(level);
		} catch (Exception e) {
			logger.error("No such imf as level " + level);
			e.printStackTrace();
		}
		return null;
	}
	
}
