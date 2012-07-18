package faultDetection.correlationControl;

import java.rmi.dgc.Lease;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.jmx.Agent;

import faultDetection.tools.LeastSquareEstimator;
import faultDetection.tools.RegressionEstimator;
import flanagan.analysis.Regression;

public class CorrelationManager {
	// ------------------------Private Variables------------------------------
	private Map<Integer, Map<Integer, Correlation>> correlationmap = new HashMap<Integer, Map<Integer, Correlation>>();
	private List<Integer> nodeindex = new ArrayList<Integer>();
	private Map<Integer, Queue<Short>> deviceconditioncount = new HashMap<Integer, Queue<Short>>();
	// A count logging the counts of faulty readings before the node is declared
	// to be faulty when reaching the threshold.
	private Map<Integer, Short> devicecondition = new HashMap<Integer, Short>();
	// Node faulty condition.
	
	
	//---------------------------Correlation Tables---------------------------
	private Map<Integer, Map<Integer, Double>> correlationtrendtable = new HashMap<Integer, Map<Integer, Double>>();
	// Correlation Trend Table is the estimate correlation calculated with
	// regression analysis
	private Map<Integer, Map<Integer, Double>> correlationtable = new HashMap<Integer, Map<Integer, Double>>();
	// Correlation Table is the correlation between readings in the reading
	// buffer
	
	//-------------------------Global Variables-----------------------------
	private boolean eventoccurence = false;
	private int samplesize;
	private int correlationpower;
	private double maxfaultratio;
	private double eventLFratio;
	private ReadingBuffer buffer = new ReadingBuffer();
	private RegressionEstimator regressionestimator;

	// -----------------Definition of reading conditions--------------------
	private final short FT = 0;
	private final short LF = 1;
	// private final short LG = 2;
	private final short GD = 3;
	private final short UNKNOWN = 4;
	
	//----------------------------------------------------------------------
	private final double eventLFrate = 0.5;
	private final Short devicedefaultcondition = UNKNOWN;
	private int eventLFcount = 0;
	private int samplecount = 0;
	//------------------------ Other Objects ------------------------------
	private static Log logger = LogFactory.getLog(CorrelationManager.class);
	// ----------------------------------------------------------------------

	// ----------------------------Constructor-------------------------------
	public CorrelationManager(int samplesize, int correlationpower,
			double maxfaultratio, double eventLFratio, RegressionEstimator regressionestimator) {
		updateSampleSize(samplesize);
		updateEventPower(correlationpower);
		updateMaxFaultRatio(maxfaultratio);
		updateEventLFRatio(eventLFratio);
		updateRegressionEstimator(regressionestimator);
	}

	// ----------------------------------------------------------------------
	// --------------------------Variable Setting Functions----------------------------
	public void resetNodeCondition(int nodeid) {
		devicecondition.put(nodeid, devicedefaultcondition);
		deviceconditioncount.get(nodeid).clear();
		resetCorrelation(nodeid);
		logger.info("Node [" + nodeid + "] Condition has been reset");
	}

	private void resetCorrelation(int nodeid) {
		// Reset node in column
		int id = 333;
		try {
			Map<Integer, Correlation> nodeicolumn = correlationmap.get(nodeid);
			for (Correlation i : nodeicolumn.values()) {
				i.resetCorrelation(samplesize);
			}
			// Reset node in raws
			for (int key : nodeindex) {
				id = key;
				if (key != nodeid) {
					correlationmap.get(key).get(nodeid)
							.resetCorrelation(samplesize);
				}
			}
		} catch (Exception e) {
			logger.warn("Node ID:" + nodeid + "  Key ID:" + id + e);
		}

	}

	public void updateEventLFRatio(double eventLFratio) {
		this.eventLFratio = eventLFratio;
	}

	public void updateSampleSize(int samplesize) {
		this.samplesize = samplesize;
	}

	public void updateEventPower(int correlationpower) {
		if (correlationpower == 0) {
			logger.error("Correlation Power cannot be " + correlationpower
					+ "and has been set as 1");
			this.correlationpower = 1;
			return;
		}
		this.correlationpower = correlationpower;
	}

	public void updateMaxFaultRatio(double maxfaultratio) {
		this.maxfaultratio = maxfaultratio;
	}
	
	public void updateRegressionEstimator(RegressionEstimator regressionestimator){
		this.regressionestimator = regressionestimator;
	}
	// --------------------------Public Functions----------------------------
	public void putReading(int nodeid, double reading) {
		putReadingProcess(nodeid, reading);
	}

	public void putReading(Map<Integer, Double> reading) {
		resetEventOccurence();//reset eventoccurence when new readings arrives
		Set<Integer> key = reading.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			putReadingProcess(nodeid, reading.get(nodeid));
		}

	}

	private void resetEventOccurence() {
		eventoccurence = false;
	}

	public void removeNode(int nodeid) {
		correlationmap.remove(nodeid);
		for (int i = 0; i < correlationmap.size(); i++) {
			correlationmap.get(nodeindex.get(i)).remove(nodeid);
		}
	}

	public boolean getEventOccurence(){
		return eventoccurence;
	}
	
	public Map<Integer, Map<Integer, Double>> getCorrelationTable() {
		Map<Integer, Double> reading = buffer.getBufferData();
		Set<Integer> key = reading.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodei = iterator.next();
			Set<Integer> key2 = reading.keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			Map<Integer, Double> temp = new HashMap<Integer, Double>();
			while (iterator2.hasNext()) {
				int nodej = iterator2.next();
				if (nodei != nodej) {
					temp.put(nodej, (reading.get(nodej) / reading.get(nodei)));
				}
			}
			correlationtable.put(nodei, temp);
		}
		if (correlationtable.size() == 0) {
			logger.warn("Warn: Null correlation table");
			return null;
		}
		return correlationtable;
	}

	public Map<Integer, Map<Integer, Double>> getCorrelationTrendTable() {
		// bufferToCorrelationt();
		updateCorrelationTrendTable();
		if (correlationtrendtable == null) {
			logger.warn("Warn: Null correlation trend table");
			return null;
		}
		return correlationtrendtable;
	}

	public boolean updateCorrelations(Map<Integer, Short> readingfaultcondition) {
		if (samplecount < samplesize) {
			samplecount++;
		} else {
			checkEventOccurence(readingfaultcondition);
			// Check if an new event occurs. Yes, reset CT.
		}
		checkDeviceCondition(readingfaultcondition); // Update node condition
		bufferToCorrelation(); // buffer to correlation if node condition is normal.
		return getEventOccurence();
	}

	public Map<Integer, Short> getDeviceCondition() {
		return devicecondition;
	}

	public void updateCorrelations() {// From buffer to correlation Map
		bufferToCorrelation();
	}

	// ----------------------------------------------------------------------
	// --------------------------Private Functions---------------------------
	private void putReadingProcess(int nodeid, double reading) {
		for (int i : nodeindex) {
			if (i == nodeid) {
				// logger.info("GetData=> Node: " + nodeid + " Reading:" +
				// reading);
				buffer.putBufferData(nodeid,
						Math.pow(reading, (1.0 / correlationpower)));
				// logger.info("Node[" + nodeid + "] has been update");
				return;
			}
		}
		logger.info("New Node[" + nodeid + "] has been added");
		addNewNode(nodeid);
		logger.info("Index Size = " + nodeindex.size());
		// TODO TEST CODE--------------------------------------------
		// logger.info("Reading = " + reading + " Correlation = " +
		// Math.pow(reading, (1.0/correlationpower)) + "Power" +
		// correlationpower);
		// ----------------------------------------------------------
		buffer.putBufferData(nodeid,
				Math.pow(reading, (1.0 / correlationpower)));
	}

	private void checkEventOccurence(Map<Integer, Short> readingfaultcondition) {
		int LFcount = 0;
		// -----TODO TEST CODE----
		// Set<Integer> k = readingfaultcondition.keySet();
		// Iterator<Integer> i = k.iterator();
		// while(i.hasNext()){
		// int id = i.next();
		// System.out.print("[" + id + "] RC: " + readingfaultcondition.get(id)
		// + " ");
		// }
		// System.out.println();
		// -----------------------
		for (short condition : readingfaultcondition.values()) {
			if (condition == LF) {
				LFcount++;
			}
		}
		if (((double) LFcount / readingfaultcondition.size()) > eventLFrate) {
			eventLFcount++;
		}
		if (((double) eventLFcount / samplesize) > eventLFratio) {
			logger.warn("New event occurs! Correlation reseting");
			eventoccurence = true;
			eventLFcount = 0;
			samplecount = 0;
			// TODO should get key set from readingfaultcondition -> nodeindex
			// whose DC is not FT readinfaultcnodition should only contain the
			// RC of non FT sensors; however, it seems that there is something wrong
			// FT devices are still reset in the process by using readingfaultcondition as references
			Set<Integer> key = readingfaultcondition.keySet();
			Iterator<Integer> it = key.iterator();
			while (it.hasNext()) {// Only GD devices will be reset
				int nodeid = it.next();
				if (devicecondition.get(nodeid) != FT)// To make sure only
														// non-FT can be reset
					resetNodeCondition(nodeid);
			}
		}
	}

	private void checkDeviceCondition(Map<Integer, Short> readingfaultcondition) {
		Set<Integer> key = readingfaultcondition.keySet();
		Iterator<Integer> iterator = key.iterator();
		for (Short condition : readingfaultcondition.values()) {
			int nodeid = iterator.next();
			try {
				if (deviceconditioncount.get(nodeid).size() < samplesize) {
					deviceconditioncount.get(nodeid).add(GD);// Assume new node
																// are normal in
																// the learning
																// stage.
					if (deviceconditioncount.get(nodeid).size() == samplesize) {
						updateDeviceCondition(nodeid);
					}
				} else {// If the reading reach the required number.
					if (devicecondition.get(nodeid) != FT) {
						deviceconditioncount.get(nodeid).remove();
						deviceconditioncount.get(nodeid).add(condition);
						updateDeviceCondition(nodeid);
					}
				}
			} catch (Exception e) {
				logger.error("Node[" + nodeid + "] Does not exist");
			}
		}
	}

	private void updateDeviceCondition(int nodeid) {
		int FTcount = 0;
		try {

			for (Short condition : deviceconditioncount.get(nodeid)) {
				if (condition == FT) {
					FTcount++;
				}
			}
			// TODO TEST CODE-----------------------------------------------
			// logger.info("[" + nodeid + "] samplesize = " + samplesize +
			// " errorcount:" + count);
			// logger.info("[" + nodeid + "]fault ratio:" + (double)count /
			// samplesize + " max:" + maxfaultratio);
			// -------------------------------------------------------------
			if ((double) FTcount / samplesize >= maxfaultratio) {
				logger.info("device fault");
				devicecondition.put(nodeid, FT);
			} else {
				devicecondition.put(nodeid, GD);
			}
		} catch (Exception e) {
			logger.error("The Node[" + nodeid + "] does not exist!!" + e);
		}

	}

	private void bufferToCorrelation() {
		for (int i : nodeindex) {
			for (int j : nodeindex) {
				try {
					if (j != i && devicecondition.get(i) != FT
							&& devicecondition.get(j) != FT) {
						// TODO TEST CODE-----------------------------------
						// logger.info("readfrombuffer: "
						// + buffer.getBufferData(i) + " "
						// + buffer.getBufferData(j));
						// -------------------------------------------------
						correlationmap
								.get(i)
								.get(j)
								.addPair(buffer.getBufferData(i),
										buffer.getBufferData(j));
					}
				} catch (Exception e) {
					logger.error("Node[" + i + "][" + j + "] Does not exist");
				}

			}
		}
		buffer.refreshflag();
	}

	private void addNewNode(int nodeid) {
		newCorrelationMapEntry(nodeid);
		newCorrelationTableEntry(nodeid);
		deviceconditioncount.put(nodeid, new LinkedList<Short>());
		devicecondition.put(nodeid, devicedefaultcondition);
		// TODO TEST CODE-------------------------------------------
		// logger.info("Has addd node[" + nodeid + "] to devicecondition");
		// ---------------------------------------------------------
		nodeindex.add(nodeid); // put last to avoid self-reference
		// devicecondition.put(nodeid, value)
	}

	private void newCorrelationMapEntry(int nodeid) {
		for (int i = 0; i < correlationmap.size(); i++) {
			correlationmap.get(nodeindex.get(i)).put(nodeid,
					new Correlation(samplesize, regressionestimator, maxfaultratio));
		}
		correlationmap.put(nodeid, newCorrelationMapList());
	}

	private Map<Integer, Correlation> newCorrelationMapList() {
		Map<Integer, Correlation> newnodecorrelation = new HashMap<Integer, Correlation>();
		for (int i = 0; i < nodeindex.size(); i++) {
			newnodecorrelation.put(nodeindex.get(i),
					new Correlation(samplesize, regressionestimator, maxfaultratio));
		}
		return newnodecorrelation;
	}
	
	private void newCorrelationTableEntry(int nodeid) {
		for (int i = 0; i < correlationtrendtable.size(); i++) {
			correlationtrendtable.get(nodeindex.get(i)).put(nodeid, (double) 0);
		}
		correlationtrendtable.put(nodeid, newCorrelationTableList());
	}

	private Map<Integer, Double> newCorrelationTableList() {
		Map<Integer, Double> newnodecorrelationlist = new HashMap<Integer, Double>();
		for (int i = 0; i < nodeindex.size(); i++) {
			newnodecorrelationlist.put(nodeindex.get(i), (double) 0);
		}
		return newnodecorrelationlist;
	}

	// Update the newest correlation strengths to correlation trend table
	private void updateCorrelationTrendTable() {
		for (int i : nodeindex) {
			for (int j : nodeindex) {
				if (j != i) {
					double correlation = correlationmap.get(i).get(j)
							.getEstimatedCorrelation();
					correlationtrendtable.get(i).put(j, correlation);
					// correlationtable.get(j).put(i, correlation);
				}
			}
		}
	}

	// -----------------------------------------------------------------------

}
