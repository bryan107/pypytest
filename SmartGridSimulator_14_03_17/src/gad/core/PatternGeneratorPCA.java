package gad.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gad.calc.PCA;

public class PatternGeneratorPCA {
	private static Log logger = LogFactory.getLog(PatternGeneratorPCA.class);
	private int windowsize;
	private final short GD = 3;
	private SMDB smdb;
	public PatternGeneratorPCA(int windowsize, SMDB smdb) {
		this.windowsize = windowsize;
		this.smdb = smdb;
	}

	public Map<Integer, Map<Integer, EstimatedVariance>> getEstimation(Map<Integer, Double> reading) {
		Map<Integer, Map<Integer, EstimatedVariance>> E = new HashMap<Integer, Map<Integer, EstimatedVariance>>();
		Iterator<Integer> it1 = reading.keySet().iterator();
		while(it1.hasNext()) {
			int nodeid_i = it1.next();
			Map<Integer, EstimatedVariance> content = new HashMap<Integer, EstimatedVariance>();
			Iterator<Integer> it2 = reading.keySet().iterator();
			while(it2.hasNext()) {
				int nodeid_j = it2.next();
				if(nodeid_i == nodeid_j){
					continue;
				}	
				try {
					boolean dc1 = smdb.getDeviceCondition(nodeid_i);
					boolean dc2 = smdb.getDeviceCondition(nodeid_j);
					Queue<Reading> q1 = smdb.getReadings(nodeid_i);
					Queue<Reading> q2 = smdb.getReadings(nodeid_j);
					if(q2 == null  || q2 == null  || dc1 == false || dc2 == false){
						content.put(nodeid_j, new EstimatedVariance(null, null, null, false));
						continue;
					}
					setPCA(q1, q2);
				} catch (Exception e2) {	// Without enough data for generate estimations
//					logger.warn("No data for estimation (" + e2 +")");
					content.put(nodeid_j, new EstimatedVariance(null, null, null, false));
					continue;
				}
				double[][] direction = PCA.getInstance().getEigenVector();
				double[] deviation = PCA.getInstance().getDeviations();		
				besselCorrection(deviation);
				double[] estimatedreading = new double[2];
				setEstimatedReading(nodeid_i, nodeid_j, estimatedreading);
				content.put(nodeid_j, new EstimatedVariance(direction, deviation, estimatedreading, true));
			}
			E.put(nodeid_i, content);
		}
		return E;
	}

	private void setEstimatedReading(int nodeid_i, int nodeie_j,
			double[] estimatedreading) {
		estimatedreading[0] = smdb.getEstimatedReading(nodeid_i);
		estimatedreading[1] = smdb.getEstimatedReading(nodeie_j);
	}

	private void besselCorrection(double[] deviation) {
		deviation[0] = deviation[0] * windowsize/(windowsize-1);
		deviation[1] = deviation[1] * windowsize/(windowsize-1);
	}


	private void setPCA(Queue<Reading> q1, Queue<Reading> q2) {
		LinkedList<Double> q1r = new LinkedList<Double>() , q2r = new LinkedList<Double>();
		extractValids(q1, q2, q1r, q2r);
		double[][] reading = new double[2][q1r.size()];
		setupReading(reading, q1r, q2r);
		PCA.getInstance().setReading(reading);
	}

	private void setupReading(double[][] reading, LinkedList<Double> q1r, LinkedList<Double> q2r) {
		int i = 0;
		while(q1r.size()!=0){
			reading[0][i] = q1r.remove();
			reading[1][i] = q2r.remove();
			i++;
		}
	}

	private void extractValids(Queue<Reading> q1, Queue<Reading> q2, LinkedList<Double> q1r, LinkedList<Double> q2r) {
		Iterator<Reading> it1 = q1.iterator();
		Iterator<Reading> it2 = q2.iterator();
		
//		TODO remove debug code
//		System.out.println("Q1:");
//		for(Reading r: q1){
//			System.out.print(" R:" + r.value() + "C:" + r.isValid());
//		}
//		System.out.println();
//		System.out.println("Q2:");
//		for(Reading r: q2){
//			System.out.print(" R:" + r.value() + "C:" + r.isValid());
//		}
//		System.out.println();
//		
		while (it1.hasNext() && it2.hasNext()) {
			Reading reading1 = it1.next();
			Reading reading2 = it2.next();
			if (reading1.isValid() == GD && reading2.isValid() == GD) {
				q1r.add(reading1.value());
				q2r.add(reading2.value());
			}
		}
//		 TODO remove debug code
//		System.out.println("Q1R:");
//		for(double r : q1r){
//			System.out.print(" " + r);
//		}
//		System.out.println();
//		System.out.println("Q2R:");
//		for(double r : q2r){
//			System.out.print(" " + r);
//		}
//		System.out.println();
	}
}
