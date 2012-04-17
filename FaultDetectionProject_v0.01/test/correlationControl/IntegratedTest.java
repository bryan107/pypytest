package correlationControl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import faultDetection.correlationControl.CorrelationManager;
import faultDetection.correlationControl.CorrelationStrengthManager;
import faultDetection.correlationControl.DFDEngine;
import junit.framework.TestCase;

public class IntegratedTest extends TestCase {
	private final short FT = 0;
	private final short LF = 1;
	private final short LG = 2;
	private final short GD = 3;
	
	private static Log logger = LogFactory.getLog(IntegratedTest.class);
	
	private Map<Integer, Map<Integer, Double>> readingpack;
	
	public void testReadingPack(){
		System.out.println("Reading Pack:");
		readingpack = setupReadingPack();
		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int packid = iterator.next();
			System.out.println("Pack " + packid );
			Set<Integer> key2 = readingpack.get(packid).keySet();
			Iterator<Integer> iterator2 = key2.iterator();
			while(iterator2.hasNext()){
				int nodeid = iterator2.next();
				DecimalFormat df = new DecimalFormat("0.00");
				System.out.print("Node[" + nodeid + "]:" + df.format(readingpack.get(packid).get(nodeid)) + " ");
			}
			System.out.println();
		}
	}
	//TODO Fix the problem occurring when new nodes are added into the system, which causes the 0 correlation trend and be declared as faulty nodes.
	public void testCorrelationManager(){
		Map<Integer, Boolean> devicecondition;
		Map<Integer, Map<Integer, Double>> correlationtable;
		Map<Integer, Map<Integer, Double>> correlationtrendtable;
		Map<Integer, Map<Integer, Double>> correlationstrengthtable;
		Map<Integer, Short> readingfaultcondition;
		Map<Integer, Double> readingtrustworthiness;
		DFDEngine.getInstance().updateThreshold(0.7);
		//define variables of correlation manager
		int samplesize = 5;
		int correlationpower = 1;
		double errortolerance = 0.293;
		
		CorrelationManager manager = new CorrelationManager(samplesize, correlationpower, errortolerance);
	
		
		readingpack = setupReadingPack();
		

		Set<Integer> key = readingpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int packid = iterator.next();
			manager.putReading(readingpack.get(packid));
			devicecondition = manager.getDeviceCondition();
			correlationtable = manager.getCorrelationTable();
			correlationtrendtable = manager.getCorrelationTrendTable();
			correlationstrengthtable = CorrelationStrengthManager.getInstance().getCorrelationStrengthTable(correlationtable, correlationtrendtable);
			readingfaultcondition = DFDEngine.getInstance()
					.faultConditionalMarking(correlationstrengthtable,
							devicecondition);
			manager.updateCorrelations(readingfaultcondition);
			readingtrustworthiness = CorrelationStrengthManager.getInstance()
					.getReadingTrustworthiness(correlationstrengthtable,
							readingfaultcondition);
			
			System.out.println("Round "+ packid + " has completed..........");
			outPutReadingPack(packid);
			//
			outPutDeviceCondition(devicecondition);
			//
			outPutCorrelationTable(correlationtable);
			//
			outPutCorrelationTrendTable(correlationtrendtable);
			//
			outPutCorrelationStrengthTable(correlationstrengthtable);
			//
			outPutReadingFaultCondition(readingfaultcondition);
			//
			System.out.println("Reading Trustworthiness:");
			for(int i = 0 ; i < 5 ; i++){
				DecimalFormat df = new DecimalFormat("0.00");
				System.out.print("[" + i + "] " + df.format(readingtrustworthiness.get(i)) + " ");
			}
			System.out.println();System.out.println();
		}
		
	}

	private void outPutReadingFaultCondition(
			Map<Integer, Short> readingfaultcondition) {
		System.out.println("DFD Reading Fault Condition:");
		for(int i = 0 ; i < 5 ; i++){
			switch(readingfaultcondition.get(i)){
			case FT:
				System.out.print("[" + i + "] FT ");
				break;
			case LF:
				System.out.print("[" + i + "] LF ");
				break;
			case LG:
				System.out.print("[" + i + "] LG ");
				break;
			case GD:
				System.out.print("[" + i + "] GD ");
			default:
				break;
			}
		}
		System.out.println();System.out.println();
	}

	private void outPutReadingPack(int packid) {
		System.out.println("Reading Pack:");
		for(int i = 0 ; i < 5 ; i++){
			DecimalFormat df = new DecimalFormat("0.00");
			System.out.print("Node[" + i + "]:" + df.format(readingpack.get(packid).get(i)) + " ");
		}
		System.out.println();
		System.out.println();
	}

	private void outPutCorrelationStrengthTable(
			Map<Integer, Map<Integer, Double>> correlationstrengthtable) {
		System.out.println("Correlation Strength Table : ");
		for(int i = 0 ; i < 5 ; i++){
			System.out.print("[" + i + "] ");
			for(int j = 0 ; j < 5 ; j++ ){
				if(correlationstrengthtable.get(i).containsKey(j)){
					DecimalFormat df = new DecimalFormat("0.00");
					System.out.print(df.format(correlationstrengthtable.get(i).get(j)) + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private void outPutDeviceCondition(Map<Integer, Boolean> devicecondition) {
		System.out.println("Device Condition : ");
		for(int i = 0 ; i < 5 ; i++){
			System.out.print("[" + i + "] " +devicecondition.get(i) + " ");
		}
		System.out.println();
		System.out.println();
	}

	private void outPutCorrelationTable(
			Map<Integer, Map<Integer, Double>> correlationtable) {
		System.out.println("Correlation Table : ");
		for(int i = 0 ; i < 5 ; i++){
			System.out.print("[" + i + "] ");
			for(int j = 0 ; j < 5 ; j++ ){
				if(correlationtable.get(i).containsKey(j)){
					DecimalFormat df = new DecimalFormat("0.00");
					System.out.print(df.format(correlationtable.get(i).get(j)) + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private void outPutCorrelationTrendTable(
			Map<Integer, Map<Integer, Double>> correlationtrendtable) {
		System.out.println("Correlation Trend Table : ");
		for(int i = 0 ; i < 5 ; i++){
			System.out.print("[" + i + "] ");
			for(int j = 0 ; j < 5 ; j++ ){
				if(correlationtrendtable.get(i).containsKey(j)){
					DecimalFormat df = new DecimalFormat("0.00");
					System.out.print(df.format(correlationtrendtable.get(i).get(j)) + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private Map<Integer, Short> setupReadingFaultCondition(){
		Map<Integer, Short> readingfaultcondition = new HashMap<Integer, Short>();
		for(int i = 0 ; i < 5 ; i++){
			readingfaultcondition.put(i, GD);
		}
		return readingfaultcondition;
	}
	
	private Map<Integer, Map<Integer, Double>> setupReadingPack(){
		Map<Integer, Map<Integer, Double>> readingpack = new HashMap<Integer, Map<Integer, Double>>();
		double noise = 0.1;
		double readingbaseline = 25;
		
		for(int i = 0 ; i < 30 ; i++){
			Map<Integer, Double> readingpackentry = new HashMap<Integer, Double>();
			for(int j = 0 ; j < 5 ; j++){
				readingpackentry.put(j, (readingbaseline + (readingbaseline * noise * Math.random())));
			}
			if(i > 20){
				readingpackentry.put(4, readingbaseline + (readingbaseline * 1 * Math.random()));
			}
			readingpack.put(i, readingpackentry);

		}
		return readingpack;
	}
}
