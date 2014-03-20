package kernelfunction.core;

import flanagan.analysis.Stat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KernelEngine {
	private double threshold;
	private static Log logger = LogFactory.getLog(KernelEngine.class);
	//faulty conditions
	private final short FT = 0;
//	private final short LF = 1;
//	private final short LG = 2;
	private final short GD = 3;
	private final short UN = 4;
	private SMDB smdb;

	public KernelEngine(double threshold, SMDB smdb) {
		updateThreshold(threshold);
		this.smdb =smdb;
	}

	public void updateThreshold(double threshold) {
		this.threshold = threshold;
	}

	public void updateSMDB(Map<Integer, Double> reading, Map<Integer, Short> condition){
		Iterator<Integer> it = reading.keySet().iterator();
		while(it.hasNext()){
			int nodeid = it.next();
			// New node added
			if(!smdb.getReadings().containsKey(nodeid)){
				smdb.putEsimatedReading(nodeid, reading.get(nodeid));
				smdb.putReading(nodeid, reading.get(nodeid), GD);
			}
			else{
				// Does not have enough reading for estimation
				if(smdb.getReadings(nodeid) == null){
					smdb.putReading(nodeid, reading.get(nodeid), GD);
					smdb.putEsimatedReading(nodeid, EWMA(reading.get(nodeid), smdb.getEstimatedReading(nodeid)));
				}
				// Has enough reading for estimation
				else{
					smdb.putReading(nodeid, reading.get(nodeid), condition.get(nodeid));
					if(condition.get(nodeid) == GD){
						smdb.putEsimatedReading(nodeid, EWMA(reading.get(nodeid), smdb.getEstimatedReading(nodeid)));
					}
				}
			}
		}
	}
	
	public Map<Integer, Short> markCondition(Map<Integer, Double> reading) {
		Map<Integer, Short> rc = null;
		
		// TODO
		return rc;
	}

	private short markInidividualCondition(int nodeid, double reading){
		short rc = 0;
		smdb.putReading(nodeid, reading, GD);
		Queue<Reading> readings = smdb.getReadings(nodeid);
		
		// TODO
		return rc; 
	}
	
	private double probabilityFunction(double x, Queue<Reading> readings){
		double f = 0;
		Iterator<Reading> it = readings.iterator();
		while(it.hasNext()){
			double xp = x - it.next().value();
		}
		return f;
	}
	

	// Clac standard deviation of each sensor with past readings. (Only consider good readings)
//	private double getStandardDeviation(Queue<Reading> pastreading) {
//		int index = 0;
//		Queue<Double> GDreading = new LinkedList<Double>();
//		Iterator<Reading> it = pastreading.iterator();
//		while (it.hasNext()) {
//			Reading r = it.next();
//			if (r.isValid() != GD) {
//				continue;
//			}
//			GDreading.add(r.value());
//		}
//		double[] values = new double[GDreading.size()];
//		Iterator<Double> it2 = GDreading.iterator();
//		while(it2.hasNext()){
//			values[index] = it2.next();
//			index++;
//		}
////		StdStats.stddev();
////		logger.info("SD:" + Stat.standardDeviation(values));
//		return Stat.standardDeviation(values);
//	}

	private double EWMA(double newreading, double estimatedreading) {
		return (newreading + estimatedreading) / 2;
	}
}
