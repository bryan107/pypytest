package faultDetection.correlationControl;

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

public class CorrelationManager {
	// ------------------------Private Variables------------------------------
	private Map<Integer, Map<Integer, Correlation>> correlationmap = new HashMap<Integer, Map<Integer, Correlation>>();
	private List<Integer> nodeindex = new ArrayList<Integer>();
	private Map<Integer, Queue<Boolean>> deviceconditioncount = new HashMap<Integer, Queue<Boolean>>();
	// A count logging the counts of faulty readings before the node is declared to be faulty when reaching the threshold.
	private Map<Integer, Boolean> devicecondition = new HashMap<Integer, Boolean>();
	// Node faulty condition.
	private Map<Integer, Map<Integer, Double>> correlationtrendtable = new HashMap<Integer, Map<Integer, Double>>();
	// Correlation Trend Table is the estimate correlation calculated with regression analysis
	private Map<Integer, Map<Integer, Double>> correlationtable = new HashMap<Integer, Map<Integer, Double>>();
	// Correlation Table is the correlation between readings in the reading buffer
	private int samplesize;
	private int correlationpower;
	private double maxfaultratio;
	private ReadingBuffer buffer = new ReadingBuffer();
	// Definition of reading conditions
	private final short FT = 0;
	private final short LF = 1;
	private final short LG = 2;
	private final short GD = 3;
	private static Log logger = LogFactory.getLog(CorrelationManager.class);

	// ----------------------------------------------------------------------

	// ----------------------------Constructor-------------------------------
	public CorrelationManager(int samplesize, int correlationpower,
			double maxfaultratio) {
		updateSampleSize(samplesize);
		updateCorrelationPower(correlationpower);
		updateMaxFaultTimes(maxfaultratio);
	}

	// ----------------------------------------------------------------------
	// --------------------------Public Functions----------------------------
	public void resetNodeCondition(int nodeid){
		devicecondition.put(nodeid, true);
		deviceconditioncount.get(nodeid).clear();
		resetCorrelation(nodeid);
		logger.info("Node [" + nodeid + "] Condition has been reset");
	}
	
	private void resetCorrelation(int nodeid){
		// Reset node in column
		Map<Integer, Correlation> nodeicolumn = correlationmap.get(nodeid);
		for(Correlation i : nodeicolumn.values()){
			i.resetCorrelation(samplesize);
		}
		// Reset node in raws
		for(int key : nodeindex){
			if(key != nodeid){
				for(Map<Integer, Correlation> raw : correlationmap.values()){
					raw.get(nodeid).resetCorrelation(samplesize);
				}
			}
		}
	}
	
	public void updateSampleSize(int samplesize) {
		this.samplesize = samplesize;
	}

	public void updateCorrelationPower(int correlationpower) {
		this.correlationpower = correlationpower;
	}

	public void updateMaxFaultTimes(double maxfaultratio) {
		this.maxfaultratio = maxfaultratio;
	}

	public void putReading(int nodeid, double reading) {
		putReadingProcess(nodeid, reading);
	}

	public void putReading(Map<Integer, Double> reading) {
		Set<Integer> key = reading.keySet();
		Iterator<Integer> iterator = key.iterator();
		for (double i : reading.values()) {
			int nodeid = iterator.next();
			putReadingProcess(nodeid, i);
		}
	}

	public Map<Integer, Map<Integer, Double>> getCorrelationTable() {
		Map<Integer, Double> reading = buffer.getBufferData();
		Set<Integer> key = reading.keySet();
		Iterator<Integer> iterator = key.iterator();
		for (double i : reading.values()) {
			int nodei = iterator.next();
			Set<Integer> key2 = reading.keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			Map<Integer, Double> temp = new HashMap<Integer, Double>();
			for (double j : reading.values()) {
				int nodej = iterator2.next();

				if (nodei != nodej) {
					temp.put(nodej, (j / i));
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

	public void removeNode(int nodeid) {
		correlationmap.remove(nodeid);
		for (int i = 0; i < correlationmap.size(); i++) {
			correlationmap.get(nodeindex.get(i)).remove(nodeid);
		}
	}

	public Map<Integer, Map<Integer, Double>> getCorrelationTrendTable() {
		// bufferToCorrelationt();
		updateCorrelationTable();
		if (correlationtrendtable == null) {
			logger.warn("Warn: Null correlation trend table");
			return null;
		}
		return correlationtrendtable;
	}

	public void updateCorrelations(Map<Integer, Short> readingfaultcondition) {
		Map<Integer, Boolean>readingcondition = new HashMap<Integer, Boolean>();
		Set<Integer> key = readingfaultcondition.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int nodeid = iterator.next();
			if(readingfaultcondition.get(nodeid) == FT){
				readingcondition.put(nodeid, false);
			}else{
				readingcondition.put(nodeid, true);
			}
		}
		checkDeviceCondition(readingcondition); // Update node condition
		bufferToCorrelation(); // buffer to correlation if node condition is normal.
	}
	
	public Map<Integer, Boolean> getDeviceCondition() {
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
				buffer.putBufferData(nodeid, Math.pow(reading, (1.0 / correlationpower)));
//				logger.info("Node[" + nodeid + "] has been update");
				return;
			}
		}
		logger.info("New Node[" + nodeid + "] has been added");
		addNewNode(nodeid);
		logger.info("Index Size = " + nodeindex.size());
		// logger.info("Reading = " + reading + " Correlation = " +
		// Math.pow(reading, (1.0/correlationpower)) + "Power" +
		// correlationpower);
		buffer.putBufferData(nodeid,
				Math.pow(reading, (1.0 / correlationpower)));
	}
	
	private void checkDeviceCondition(Map<Integer, Boolean> readingfaultcondition) {
		Set<Integer> key = readingfaultcondition.keySet();
		Iterator<Integer> iterator = key.iterator();
		for (boolean i : readingfaultcondition.values()) {
			int nodeid = iterator.next();
			try {
				if (deviceconditioncount.get(nodeid).size() == 0) {
					deviceconditioncount.get(nodeid).add(i);
				} else if (deviceconditioncount.get(nodeid).size() < samplesize) {
					deviceconditioncount.get(nodeid).add(i);
					if (deviceconditioncount.get(nodeid).size() == samplesize) {
						updateDeviceCondition(nodeid);
					}
				} else {// If the reading reach the required number.
					if (devicecondition.get(nodeid) == true) {
						deviceconditioncount.get(nodeid).remove();
						deviceconditioncount.get(nodeid).add(i);
						updateDeviceCondition(nodeid);
					}
				}
			} catch (Exception e) {
				logger.error("Node[" + nodeid + "] Does not exist");
			}
		}
	}

	private void updateDeviceCondition(int nodeid) {
		int count = 0;
		try {
			for (boolean j : deviceconditioncount.get(nodeid)) {
				if (j == false) {
					count++;
				}
			}
			if ((double) count / samplesize >= maxfaultratio) {
				devicecondition.put(nodeid, false);
			} else {
				devicecondition.put(nodeid, true);
			}
		} catch (Exception e) {
			logger.error("The Node[" + nodeid + "] does not exist!!");
		}

	}

	private void bufferToCorrelation() {
		for (int i : nodeindex) {
			for (int j : nodeindex) {
				try {
					if (j != i && devicecondition.get(i) != false && devicecondition.get(j) != false) {
//						logger.info("readfrombuffer: " + buffer.getBufferData(i) + " " + buffer.getBufferData(j));
						correlationmap.get(i).get(j).addPair(buffer.getBufferData(i),buffer.getBufferData(j));
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
		deviceconditioncount.put(nodeid, new LinkedList<Boolean>());
		devicecondition.put(nodeid, true);
//		logger.info("Has addd node[" + nodeid + "] to devicecondition");
		nodeindex.add(nodeid); // put last to avoid self-reference
		// devicecondition.put(nodeid, value)
	}

	private void newCorrelationMapEntry(int nodeid) {
		for (int i = 0; i < correlationmap.size(); i++) {
			correlationmap.get(nodeindex.get(i)).put(nodeid,
					new Correlation(samplesize));
		}
		correlationmap.put(nodeid, newCorrelationMapList());
	}

	private void newCorrelationTableEntry(int nodeid) {
		for (int i = 0; i < correlationtrendtable.size(); i++) {
			correlationtrendtable.get(nodeindex.get(i)).put(nodeid, (double) 0);
		}
		correlationtrendtable.put(nodeid, newCorrelationTableList());
	}

	private Map<Integer, Correlation> newCorrelationMapList() {
		Map<Integer, Correlation> newnodecorrelation = new HashMap<Integer, Correlation>();
		for (int i = 0; i < nodeindex.size(); i++) {
			newnodecorrelation.put(nodeindex.get(i),
					new Correlation(samplesize));
		}
		return newnodecorrelation;
	}

	private Map<Integer, Double> newCorrelationTableList() {
		Map<Integer, Double> newnodecorrelationlist = new HashMap<Integer, Double>();
		for (int i = 0; i < nodeindex.size(); i++) {
			newnodecorrelationlist.put(nodeindex.get(i), (double) 0);
		}
		return newnodecorrelationlist;
	}

	// Update the newest correlation strengths to correlation table
	private void updateCorrelationTable() {
		for (int i : nodeindex) {
			for (int j : nodeindex) {
				if (j != i) {
					double correlation = correlationmap.get(i).get(j)
							.getCorrelation();
					correlationtrendtable.get(i).put(j, correlation);
					// correlationtable.get(j).put(i, correlation);
				}
			}
		}
	}

	// -----------------------------------------------------------------------

}
