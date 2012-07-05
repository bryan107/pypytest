package correlationControl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import faultDetection.correlationControl.CorrelationManager;
//import faultDetection.correlationControl.CorrelationStrengthManager;
//import faultDetection.correlationControl.DFDEngine;
import faultDetection.correlationControl.MarkedReading;
import faultDetection.correlationControl.ProcessManager;
import junit.framework.TestCase;

public class IntegratedTest extends TestCase {
	private final short FT = 0;
	private final short LF = 1;
	private final short LG = 2;
	private final short GD = 3;
	private final String[] faultcondition = { "FT", "LF", "LG", "GD", "UNKNOWN" };

//	private static Log logger = LogFactory.getLog(IntegratedTest.class);

	private Map<Integer, Map<Integer, Double>> readingpack;

	// public void testReadingPack(){
	// System.out.println("Reading Pack:");
	// readingpack = setupReadingPack(0.1, 4, 0.1, 100, 100, 25, 5, 5);
	// Set<Integer> key = readingpack.keySet();
	// Iterator<Integer> iterator = key.iterator();
	// while(iterator.hasNext()){
	// int packid = iterator.next();
	// System.out.println("Pack " + packid );
	// Set<Integer> key2 = readingpack.get(packid).keySet();
	// Iterator<Integer> iterator2 = key2.iterator();
	// while(iterator2.hasNext()){
	// int nodeid = iterator2.next();
	// DecimalFormat df = new DecimalFormat("0.00");
	// System.out.print("Node[" + nodeid + "]:" +
	// df.format(readingpack.get(packid).get(nodeid)) + " ");
	// }
	// System.out.println();
	// }
	// }
	// TODO Fix the problem occurring when new nodes are added into the system,
	// which causes the 0 correlation trend and be declared as faulty nodes.
//	public void testCorrelationManager() {
//		// define variables of correlation manager (User's settings)
//		int samplesize = 40; // Critical (Can influence system performance very
//								// much)
//		int correlationpower = 1;
//		double maxfaultratio = 0.293; // Static
//		double cstrcorrelationerrortolerance = 0.25; // Critical
//		double dfdtreshold = 0.7; // Critical
//		// Setup reading package (Auto data generating)
//		int readingpacksize = 300;
//		double noise = 0.05;
//		int errornodeid = 2;
//		double error = 1.0;
//		int errorround = 120;
//		int newnoderound = 60;
//		double readingbaseline = 25;
//		int nodenumber = 4;
//
//		// Define Objects
//		Map<Integer, Short> devicecondition;
//		Map<Integer, Map<Integer, Double>> correlationtable;
//		Map<Integer, Map<Integer, Double>> correlationtrendtable;
//		Map<Integer, Map<Integer, Double>> correlationstrengthtable;
//		Map<Integer, Short> readingfaultcondition;
//		Map<Integer, Double> readingtrustworthiness;
//		Map<Integer, Integer> devicefaultround = new HashMap<Integer, Integer>();
//		// Set up Parameters
//		DFDEngine.getInstance().updateThreshold(dfdtreshold);
//		CorrelationStrengthManager.getInstance().updateErrorTolerance(
//				cstrcorrelationerrortolerance);
//		CorrelationManager manager = new CorrelationManager(samplesize,
//				correlationpower, maxfaultratio);
//		readingpack = setupReadingPack(noise, errornodeid, error, errorround,
//				newnoderound, readingbaseline, readingpacksize, nodenumber);
//
//		Set<Integer> key = readingpack.keySet();
//		Iterator<Integer> iterator = key.iterator();
//		while (iterator.hasNext()) {
//			int packid = iterator.next();
//			manager.putReading(readingpack.get(packid));
//			devicecondition = manager.getDeviceCondition();
//			correlationtable = manager.getCorrelationTable();
//			correlationtrendtable = manager.getCorrelationTrendTable();
//			correlationstrengthtable = CorrelationStrengthManager.getInstance()
//					.getCorrelationStrengthTable(correlationtable,
//							correlationtrendtable);
//			readingfaultcondition = DFDEngine.getInstance()
//					.faultConditionalMarking(correlationstrengthtable,
//							devicecondition);
//			manager.updateCorrelations(readingfaultcondition);
//			readingtrustworthiness = CorrelationStrengthManager.getInstance()
//					.getReadingTrustworthiness(correlationstrengthtable,
//							readingfaultcondition);
//			devicecondition = manager.getDeviceCondition();
//
//			logDeviceFaultRound(devicecondition, devicefaultround, packid);
//			DecimalFormat df = new DecimalFormat("0.0");
//			// System.out.println("The marking process has finished " +
//			// df.format(((double)packid/readingpacksize)*100) + "%");
//			System.out.println("Round " + packid + " has completed..........");
//			outPutReadingPack(packid);
//
//			outPutCorrelationTable(correlationtable);
//			//
//			outPutCorrelationTrendTable(correlationtrendtable);
//			//
//			outPutCorrelationStrengthTable(correlationstrengthtable);
//			//
//			outPutReadingFaultCondition(readingfaultcondition);
//			//
//			outPutReadingTrustwhorthiness(readingtrustworthiness);
//			//
//			outPutDeviceCondition(devicecondition);
//
//		}
//		outPutDeviceFaulyRound(devicefaultround);
//
//	}

	public void testProcessManager() {
		// define variables of correlation manager (User's settings)
		int samplesize = 40; // Critical (Can influence system performance very
								// much)
		int eventpower = 1;
		double maxfaultratio = 0.293; // Static
		double cstrcorrelationerrortolerance = 0.25; // Critical
		double DFDtreshold = 0.7; // Critical
		// Setup reading package (Auto data generating)
		int readingpacksize = 300;
		double noise = 0.05;
		int errornodeid = 2;
		double error = 1.0;
		int errorround = 120;
		int newnoderound = 60;
		double readingbaseline = 25;
		int nodenumber = 4;

//		ProcessManager processmanager = new ProcessManager(samplesize,
//				eventpower, maxfaultratio, DFDtreshold,
//				cstrcorrelationerrortolerance);
//		readingpack = setupReadingPack(noise, errornodeid, error, errorround,
//				newnoderound, readingbaseline, readingpacksize, nodenumber);
//
//		Set<Integer> key = readingpack.keySet();
//		Iterator<Integer> iterator = key.iterator();
//		while (iterator.hasNext()) {
//			int packid = iterator.next();
//			Map<Integer, MarkedReading> markedreadigpack = processmanager
//					.markReadings(readingpack.get(packid));
//			System.out.println("Round: " + packid);
//			Set<Integer> key2 = markedreadigpack.keySet();
//			Iterator<Integer> iterator2 = key2.iterator();
//			while (iterator2.hasNext()) {
//				int nodeid = iterator2.next();
//				System.out.println(markedreadigpack.get(nodeid).toSrting());
		
		
		
//				DecimalFormat df = new DecimalFormat("00.00");
//				System.out.print(" ["
//						+ nodeid
//						+ "] R:"
//						+ df.format(markedreadigpack.get(nodeid).reading())
//						+ " T:"
//						+ df.format(markedreadigpack.get(nodeid).trustworthiness()*100)
//						+ "% FC:"
//						+ faultcondition[markedreadigpack.get(nodeid)
//								.readingContidion()] + " DF:"
//						+ markedreadigpack.get(nodeid).nodeIsFaulty());

		
//			}
//			System.out.println();
//
//		}
//
	}

	private void outPutDeviceFaulyRound(Map<Integer, Integer> devicefaultround) {
		Set<Integer> key2 = devicefaultround.keySet();
		Iterator<Integer> iterator2 = key2.iterator();
		System.out.println("There are " + key2.size()
				+ " devices are decalred faulty during this test:");
		while (iterator2.hasNext()) {
			int nodeid = iterator2.next();
			System.out.print("[" + nodeid + "] in round "
					+ devicefaultround.get(nodeid) + "; ");
		}
	}

	private void logDeviceFaultRound(Map<Integer, Short> devicecondition,
			Map<Integer, Integer> devicefaultround, int packid) {
		Set<Integer> key2 = devicecondition.keySet();
		Iterator<Integer> iterator2 = key2.iterator();
		while (iterator2.hasNext()) {
			int nodeid = iterator2.next();
			if (devicecondition.get(nodeid) == FT
					&& devicefaultround.get(nodeid) == null) {
				devicefaultround.put(nodeid, packid);
			}
		}
	}

	private void outPutReadingTrustwhorthiness(
			Map<Integer, Double> readingtrustworthiness) {
		System.out.println("Reading Trustworthiness:");
		Set<Integer> key = readingtrustworthiness.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			DecimalFormat df = new DecimalFormat("0.00");
			System.out.print("[" + nodeid + "] "
					+ df.format(readingtrustworthiness.get(nodeid)) + " ");
		}
		System.out.println();
		System.out.println();
	}

	private void outPutReadingFaultCondition(
			Map<Integer, Short> readingfaultcondition) {
		System.out.println("DFD Reading Fault Condition:");
		Set<Integer> key = readingfaultcondition.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			switch (readingfaultcondition.get(nodeid)) {
			case FT:
				System.out.print("[" + nodeid + "] FT ");
				break;
			case LF:
				System.out.print("[" + nodeid + "] LF ");
				break;
			case LG:
				System.out.print("[" + nodeid + "] LG ");
				break;
			case GD:
				System.out.print("[" + nodeid + "] GD ");
			default:
				break;
			}
		}
		System.out.println();
		System.out.println();
	}

	private void outPutReadingPack(int packid) {
		System.out.println("Reading Pack:");
		Set<Integer> key = readingpack.get(packid).keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			DecimalFormat df = new DecimalFormat("0.00");
			System.out.print("Node[" + nodeid + "]:"
					+ df.format(readingpack.get(packid).get(nodeid)) + " ");
		}
		System.out.println();
		System.out.println();
	}

	private void outPutCorrelationStrengthTable(
			Map<Integer, Map<Integer, Double>> correlationstrengthtable) {
		System.out.println("Correlation Strength Table : ");
		Set<Integer> key = correlationstrengthtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int rawid = iterator.next();
			System.out.print("[" + rawid + "] ");
			Set<Integer> key2 = correlationstrengthtable.get(rawid).keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			while (iterator2.hasNext()) {
				int coloumnid = iterator2.next();
				if (correlationstrengthtable.get(rawid).containsKey(coloumnid)) {
					DecimalFormat df = new DecimalFormat("0.00");
					System.out.print(df.format(correlationstrengthtable.get(
							rawid).get(coloumnid))
							+ " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private void outPutDeviceCondition(Map<Integer, Short> devicecondition) {
		System.out.println("Device Condition : ");
		Set<Integer> key = devicecondition.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int nodeid = iterator.next();
			System.out.print("[" + nodeid + "] "
					+ faultcondition[devicecondition.get(nodeid)] + " ");
		}
		System.out.println();
		System.out.println();
	}

	private void outPutCorrelationTable(
			Map<Integer, Map<Integer, Double>> correlationtable) {
		System.out.println("Correlation Table : ");
		Set<Integer> key = correlationtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int rawid = iterator.next();
			System.out.print("[" + rawid + "] ");
			Set<Integer> key2 = correlationtable.get(rawid).keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			while (iterator2.hasNext()) {
				int coloumnid = iterator2.next();
				if (correlationtable.get(rawid).containsKey(coloumnid)) {
					DecimalFormat df = new DecimalFormat("0.00");
					System.out.print(df.format(correlationtable.get(rawid).get(
							coloumnid))
							+ " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private void outPutCorrelationTrendTable(
			Map<Integer, Map<Integer, Double>> correlationtrendtable) {
		System.out.println("Correlation Trend Table : ");
		Set<Integer> key = correlationtrendtable.keySet();
		Iterator<Integer> iterator = key.iterator();
		while (iterator.hasNext()) {
			int rawid = iterator.next();
			System.out.print("[" + rawid + "] ");
			Set<Integer> key2 = correlationtrendtable.get(rawid).keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			while (iterator2.hasNext()) {
				int coloumnid = iterator2.next();
				if (correlationtrendtable.get(rawid).containsKey(coloumnid)) {
					DecimalFormat df = new DecimalFormat("0.00");
					System.out.print(df.format(correlationtrendtable.get(rawid)
							.get(coloumnid)) + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private Map<Integer, Short> setupReadingFaultCondition() {
		Map<Integer, Short> readingfaultcondition = new HashMap<Integer, Short>();
		for (int i = 0; i < 5; i++) {
			readingfaultcondition.put(i, GD);
		}
		return readingfaultcondition;
	}

	private Map<Integer, Map<Integer, Double>> setupReadingPack(double noise,
			int errornodeid, double error, double errorround,
			long newnoderound, double readingbaseline, long round,
			int nodenumber) {
		Map<Integer, Map<Integer, Double>> readingpack = new HashMap<Integer, Map<Integer, Double>>();

		for (int i = 0; i < round; i++) {
			Map<Integer, Double> readingpackentry = new HashMap<Integer, Double>();
			for (int j = 0; j < nodenumber; j++) {
				if (Math.random() < 0.5) {
					readingpackentry.put(j, (readingbaseline + (readingbaseline
							* noise * Math.random())));
				} else {
					readingpackentry.put(j, (readingbaseline - (readingbaseline
							* noise * Math.random())));
				}

			}
			if (i > errorround) {
				if (Math.random() < 0.5) {
					readingpackentry.put(errornodeid, readingbaseline
							+ (readingbaseline * error * Math.random()));
				} else {
					readingpackentry.put(errornodeid, readingbaseline
							- (readingbaseline * error * Math.random()));
				}

			}
			if (i > newnoderound) {
				readingpackentry.put(nodenumber,
						(readingbaseline + (readingbaseline * noise * Math
								.random())));
			}
			readingpack.put(i, readingpackentry);

		}
		return readingpack;
	}
}
