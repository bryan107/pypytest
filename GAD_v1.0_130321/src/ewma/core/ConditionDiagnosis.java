package ewma.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConditionDiagnosis {
	// faulty conditions
	private final short FT = 0;
	private final short GD = 3;
	private final short UN = 4;
	private double MAR;
	private double MER;
	private double windowsize;
	private int eventcount = 0;
	private static Log logger = LogFactory.getLog(ConditionDiagnosis.class);

	public ConditionDiagnosis(double MAR, double MER, int windowsize) {
		setMAR(MAR);
		setMER(MER);
		setWindowSize(windowsize);
	}

	public void setMAR(double MAR) {
		this.MAR = MAR;
	}

	public void setMER(double MER) {
		this.MER = MER;
	}

	public void setWindowSize(int windowsize) {
		this.windowsize = windowsize;
	}

	public Diagnosis diagnose(Map<Integer, Double> reading,
			Map<Integer, Short> anomalycondition) {
		Diagnosis d = new Diagnosis();

		checkEventCondition(anomalycondition);
		checkEventOccurrence(d);	// If Event occurs reset SMDB
		checkAbnormality(reading, anomalycondition, d);
		checkDeviceCondition(reading, d);
		return d;
	}

	private void checkEventOccurrence(Diagnosis d) {
//		Queue<Boolean> eq = SMDB.getInstance().getEventConditions();
//		if(eq.size() < windowsize){
//			d.putEventOccurrence(false);
//			return;
//		}
//		double eo = 0;
//		for (boolean event : eq) {
//			if (event == true) {
//				eo++;
//			}
//		}
		if ((eventcount / windowsize) > MER) {
			// TODO FIX resetSMDb dcFT
			d.putEventOccurrence(true);
			LinkedList<Integer> dcFT= new LinkedList<Integer>();
			findAbnormalDC(dcFT);
			SMDB.getInstance().resetSMDB();
			SMDB.getInstance().getDeviceCondition(3);
			return;
		}
//		if ((eo / windowsize) > MER) {
//			d.putEventOccurrence(true);
//			LinkedList<Integer> dcFT= new LinkedList<Integer>();
//			findAbnormalDC(dcFT);
//			SMDB.getInstance().resetSMDB(dcFT);
//			SMDB.getInstance().getDeviceCondition(3);
//			return;
//		}
		d.putEventOccurrence(false);
	}

	private void findAbnormalDC(LinkedList<Integer> dcFT) {
		Map<Integer, Queue<Reading>> reading = SMDB.getInstance().getReadings();
		Iterator<Integer> it = reading.keySet().iterator();
		while(it.hasNext()){
			int nodeid = it.next();
			boolean dc = SMDB.getInstance().getDeviceCondition(nodeid);
			if(dc == false){
				dcFT.add(nodeid);
			}
		}
	}

	private void checkDeviceCondition(Map<Integer, Double> reading, Diagnosis d) {
		Iterator<Integer> it = reading.keySet().iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			try {
				Queue<Reading> rq = SMDB.getInstance().getReadings(nodeid);
				boolean dc = SMDB.getInstance().getDeviceCondition(nodeid);
				if(rq == null){	// If no reading is in queue
					d.putDeviceContidion(nodeid, dc);
					SMDB.getInstance().putDeviceCondition(nodeid, dc);
					continue;
				}
				double acount = 0;
				for (Reading r : rq) {	
					if (r.isValid() == FT) {
						acount++;
					}
				}
				
				if ((acount / windowsize) > MAR) {
					d.putDeviceContidion(nodeid, false);
					SMDB.getInstance().putDeviceCondition(nodeid, false);
				} else {
					d.putDeviceContidion(nodeid, true);
					SMDB.getInstance().putDeviceCondition(nodeid, true);
				}

			} catch (Exception e) {
				logger.error("reading and condition list does not match:"
						+ e.toString());
			}
		}
	}

	private void checkAbnormality(Map<Integer, Double> reading,
			Map<Integer, Short> anomalycondition, Diagnosis d) {
		
		Iterator<Integer> it = reading.keySet().iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			// CASE: node does not exist in SMDB
			if (!SMDB.getInstance().getReadings().containsKey(nodeid)) { 
				d.putReadingCondition(nodeid, GD);
				SMDB.getInstance().putReading(nodeid, reading.get(nodeid), GD);
				SMDB.getInstance().putEsimatedReading(nodeid,reading.get(nodeid));
				continue;
			}
			// CASE: node exists in SMDB
			// CASE:Good Device
			if (SMDB.getInstance().getDeviceCondition(nodeid)) { 
				if (anomalycondition.get(nodeid) == GD) {// Good Reading
					d.putReadingCondition(nodeid, GD);
					SMDB.getInstance().putReading(nodeid, reading.get(nodeid),GD);
					double estimatedreading = EWMA(reading.get(nodeid), SMDB.getInstance().getEstimatedReading(nodeid));
					SMDB.getInstance().putEsimatedReading(nodeid, estimatedreading);
				}else if(anomalycondition.get(nodeid) == UN){// Potential Event detected
					d.putReadingCondition(nodeid, UN);
					SMDB.getInstance().putReading(nodeid, reading.get(nodeid),UN);
				}else {	// Bad Reading
					d.putReadingCondition(nodeid, FT);
					SMDB.getInstance().putReading(nodeid,reading.get(nodeid), FT);
				}
			} else { // Device Condition Error (for backup)
				d.putReadingCondition(nodeid, FT);
				SMDB.getInstance().putReading(nodeid, reading.get(nodeid), FT);
			}
		}
	}

	private double EWMA(double newreading, double estimatedreading){
		return (newreading + estimatedreading)/2;
	}
	
	private boolean checkEventCondition(Map<Integer, Short> readingcondition) {
		boolean eventcondition = false;
		int size = readingcondition.size();
		if(size < 3){
			SMDB.getInstance().putEventConditions(eventcondition);
			return false;
		}
		int UNcount = 0;
		for (short condition : readingcondition.values()) {
			if (condition == UN) {
				UNcount++;
			}
		}
		if ((UNcount / size) > 0.5) {
			eventcondition = true;
			eventcount++;
		}
		else{
			eventcount = 0;
		}
		SMDB.getInstance().putEventConditions(eventcondition);
		return eventcondition;
	}
}
