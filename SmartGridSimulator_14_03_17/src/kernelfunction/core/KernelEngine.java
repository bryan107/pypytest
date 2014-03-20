package kernelfunction.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

public class KernelEngine {
	private double threshold_r;// the threshold distance r
	private double threshold_p;// the threshold CDF in distance r
	
	private int maxEval = 50000; //Maximum number of evaluations
	private static Log logger = LogFactory.getLog(KernelEngine.class);
	//faulty conditions
	private final short FT = 0;
//	private final short LF = 1;
//	private final short LG = 2;
	private final short GD = 3;
	private final short UN = 4;
	private SMDB smdb;

	public KernelEngine(double threshold_r, double threshold_p, SMDB smdb) {
		updateThreshold(threshold_r, threshold_p);
		this.smdb =smdb;
	}

	public void updateThreshold(double threshold_r, double threshold_p) {
		this.threshold_r = threshold_r;
		this.threshold_p = threshold_p;
	}
	
	public void updateMaxEval(int maxEval){
		this.maxEval = maxEval;
	}

//	public void updateSMDB(Map<Integer, Double> reading, Map<Integer, Short> condition){
//		Iterator<Integer> it = reading.keySet().iterator();
//		while(it.hasNext()){
//			int nodeid = it.next();
//			// New node added
//			if(!smdb.getReadings().containsKey(nodeid)){
////				smdb.putEsimatedReading(nodeid, reading.get(nodeid)); // Unused function
//				smdb.putReading(nodeid, reading.get(nodeid), GD);
//			}
//			else{
//				// Does not have enough reading for estimation
//				if(smdb.getReadings(nodeid) == null){
//					smdb.putReading(nodeid, reading.get(nodeid), GD);
////					smdb.putEsimatedReading(nodeid, reading.get(nodeid)); // Unused function
//				}
//				// Has enough reading for estimation
//				else{
//					smdb.putReading(nodeid, reading.get(nodeid), condition.get(nodeid));
////					if(condition.get(nodeid) == GD){ // Unused function
////						smdb.putEsimatedReading(nodeid, EWMA(reading.get(nodeid), smdb.getEstimatedReading(nodeid)));
////					}
//				}
//			}
//		}
//	}
	
	public Map<Integer, Short> markCondition(Map<Integer, Double> reading) {
		Map<Integer, Short> rc = new HashMap<Integer, Short>();
		Iterator<Integer> it = reading.keySet().iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			try {
				Queue<Reading> pastreading = smdb.getReadings(nodeid);
				boolean dc = smdb.getDeviceCondition(nodeid);
				// If device if false, set rc unknown and continue to the next.
				if(dc == false) {
					rc.put(nodeid, UN);
					continue;
				}
				// If new device dose not have enough reading, set rc = GD.
				if (pastreading == null) {
					rc.put(nodeid, GD);
					continue;
				}
				// Calculate and put evaluated condition.
				rc.put(nodeid, markInidividualCondition(nodeid, reading.get(nodeid)));
			} catch (Exception e) {
				logger.error("SMDB Access Error");
			}
			
		}  
		return rc;
	}

	private short markInidividualCondition(int nodeid, double reading){
		short rc = GD;
		UnivariateIntegrator integrator = new RombergIntegrator();
		// Read past data from SMDB
		Queue<Reading> readings = smdb.getReadings(nodeid);
		// Calculate the relative distances between current reading and past GD readings.
		Queue<Double> rdq = getRelativeDistanceQueue(reading, readings);
		// Generate a univariance function
		KernelFunction k = new KernelFunction(rdq);
		// Calculate the probability CDF from 0 to threshold_r
		try {
			double f = integrator.integrate(maxEval, k, 0, threshold_r);
			
			logger.info("INT:" + f + " R:" + threshold_r + " P:" + threshold_p);
			if(f > threshold_p){
				rc = GD;
			}
			else{
				rc = FT;
			}
		} catch (Exception e) {
			logger.error("Integrator Error" + e);
		}
		return rc; 
	}
	
	private Queue<Double> getRelativeDistanceQueue(double value,
			Queue<Reading> readings) {
		Queue<Double> rdq = new LinkedList<Double>();
		Iterator<Reading> it = readings.iterator();
		while (it.hasNext()) {
			Reading r = it.next();
			if (r.isValid() == GD) { // Only extract valid GD readings
				rdq.add(Math.abs(value - r.value()));
			}
		}
		return rdq;
	}
	
	
}
