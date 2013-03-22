package gad.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConditionDiagnosis {
	// faulty conditions

	private final short GD = 3;
	private final short UN = 4;
	private double MAR;
	private double MER;
	private double windowsize;
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
		checkAbnormality(reading, anomalycondition ,d);
		checkDeviceCondition(reading, d);
		
		
		Queue<Boolean> eq = SMDB.getInstance().getEventConditions();
		double eo = 0;
		for(boolean event : eq){
			if(event == true){
				eo++;
			}
		}
		if((eo / windowsize) >= MER){
			d.putEventOccurrence(true);
			//TODO Reset SMDB
		}
		
		
		SMDB.getInstance().putEventConditions(d.eventOccurrence());

		return d;
	}

	private void checkDeviceCondition(Map<Integer, Double> reading, Diagnosis d) {
		Iterator<Integer> it = reading.keySet().iterator();
		while (it.hasNext()) {
			int nodeid = it.next();
			try {

				Queue<Reading> rq = SMDB.getInstance().getReadings(nodeid);

				double acount = 0;
				for (Reading r : rq) {
					if (!r.isValid()) {
						acount++;
					}
				}
				if ((acount / windowsize) >= MAR) {
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
			if(SMDB.getInstance().getReadings().containsKey(nodeid)){ // node exist in SMDB
				try {
					if (SMDB.getInstance().getDeviceCondition(nodeid)) { //Good Device
						if (anomalycondition.get(nodeid) == GD) {
							d.putReadingCondition(nodeid, true);
							SMDB.getInstance().putReading(nodeid, reading.get(nodeid), true);
						} else {
							d.putReadingCondition(nodeid, false);
							SMDB.getInstance().putReading(nodeid, reading.get(nodeid), false);
						}
					} else {	//Bad Device
						d.putReadingCondition(nodeid, false);
						SMDB.getInstance().putReading(nodeid, reading.get(nodeid), false);
					}
				} catch (Exception e) {
					logger.error("Condition Marking Error" + e);
				}
			}
			else{	// New node
				d.putReadingCondition(nodeid, true);
				SMDB.getInstance().putReading(nodeid, reading.get(nodeid), true);
			}
		}
	}

	private boolean checkEventCondition(Map<Integer, Short> readingcondition) {
		boolean eventcondition = false;
		int size = readingcondition.size();
		int UNcount = 0;
		for (short condition : readingcondition.values()) {
			if (condition == UN) {
				UNcount++;
			}
		}
		if ((UNcount / size) > 0.5) {
			eventcondition = true;
		}
		SMDB.getInstance().putEventConditions(eventcondition);
		return eventcondition;
	}
}
