package variation.core;

import flanagan.analysis.Stat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VariationEngine {
	private double threshold;
	private static Log logger = LogFactory.getLog(VariationEngine.class);
	//faulty conditions
	private final short FT = 0;
//	private final short LF = 1;
//	private final short LG = 2;
	private final short GD = 3;
//	private final short UN = 4;

	public VariationEngine(double threshold) {
		updateThreshold(threshold);
	}

	public void updateThreshold(double threshold) {
		this.threshold = threshold;
	}

	public void updateSMDB(Map<Integer, Double> reading, Map<Integer, Short> condition){
		Iterator<Integer> it = reading.keySet().iterator();
		while(it.hasNext()){
			int nodeid = it.next();
			// New node added
			if(!SMDB.getInstance().getReadings().containsKey(nodeid)){
				SMDB.getInstance().putEsimatedReading(nodeid, reading.get(nodeid));
				SMDB.getInstance().putReading(nodeid, reading.get(nodeid), GD);
			}
			else{
				// Does not have enough reading for estimation
				if(SMDB.getInstance().getReadings(nodeid) == null){
					SMDB.getInstance().putReading(nodeid, reading.get(nodeid), GD);
					SMDB.getInstance().putEsimatedReading(nodeid, EWMA(reading.get(nodeid), SMDB.getInstance().getEstimatedReading(nodeid)));
				}
				// Has enough reading for estimation
				else{
					SMDB.getInstance().putReading(nodeid, reading.get(nodeid), condition.get(nodeid));
					if(condition.get(nodeid) == GD){
						SMDB.getInstance().putEsimatedReading(nodeid, EWMA(reading.get(nodeid), SMDB.getInstance().getEstimatedReading(nodeid)));
					}
				}
			}
		}
	}
	
	public Map<Integer, Short> markCondition(Map<Integer, Double> reading) {
		Map<Integer, Short> rc = new HashMap<Integer, Short>();
		Iterator<Integer> it = reading.keySet().iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			try {
				Queue<Reading> pastreading = SMDB.getInstance().getReadings(nodeid);
				boolean dc = SMDB.getInstance().getDeviceCondition(nodeid);
				if (pastreading == null || dc == false) {
					continue;
				}
				double standarddeviation = getStandardDeviation(pastreading);
				double distance = reading.get(nodeid)- SMDB.getInstance().getEstimatedReading(nodeid);
				if (Math.abs(distance) < threshold*standarddeviation){
					rc.put(nodeid, GD);
				}
				else{
					rc.put(nodeid, FT);
				}
//				logger.info("SD:" + standarddeviation);
//				logger.info("DT:" + distance);
			} catch (Exception e) {
				logger.error("SMDB Access Error");
			}
			
		}
		return rc;
	}

	// Clac standard deviation of each sensor with past readings.
	private double getStandardDeviation(Queue<Reading> pastreading) {
		int index = 0;
		Queue<Double> GDreading = new LinkedList<Double>();
		Iterator<Reading> it = pastreading.iterator();
		while (it.hasNext()) {
			Reading r = it.next();
			if (r.isValid() != GD) {
				continue;
			}
			GDreading.add(r.value());
		}
		double[] values = new double[GDreading.size()];
		Iterator<Double> it2 = GDreading.iterator();
		while(it2.hasNext()){
			values[index] = it2.next();
			index++;
		}
//		StdStats.stddev();
//		logger.info("SD:" + Stat.standardDeviation(values));
		return Stat.standardDeviation(values);
	}

	private double EWMA(double newreading, double estimatedreading) {
		return (newreading + estimatedreading) / 2;
	}
}
