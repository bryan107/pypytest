package gad.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;
import gad.calc.PCA;

public class PatternGenerator {
	private static Log logger = LogFactory.getLog(PatternGenerator.class);
	private double[] referencereading = new double[2];
	private int windowsize;
	public PatternGenerator(int windowsize) {
		this.windowsize = windowsize;
	}

	public Map<Integer, Map<Integer, EstimatedVariance>> getEstimation(
			int[] nodeids) {
		Map<Integer, Map<Integer, EstimatedVariance>> E = new HashMap<Integer, Map<Integer, EstimatedVariance>>();
		for (int i = 0; i < nodeids.length; i++) {
			Map<Integer, EstimatedVariance> content = new HashMap<Integer, EstimatedVariance>();
			for (int j = 0; j < nodeids.length; j++) {
				if(nodeids[i] == nodeids[j]){
					continue;
				}	
				try {
					setPCA(nodeids[i], nodeids[j]);
				} catch (Exception e2) {
					logger.error(e2);
					continue;
				}
				double[][] direction = PCA.getInstance().getEigenVector();
				double[] deviation = PCA.getInstance().getDeviations();		
				besselCorrection(deviation);
				content.put(nodeids[j], new EstimatedVariance(direction, deviation, referencereading));
			}
			E.put(nodeids[i], content);
		}
		return E;
	}

	private void besselCorrection(double[] deviation) {
		deviation[0] = deviation[0] * windowsize/(windowsize-1);
		deviation[1] = deviation[1] * windowsize/(windowsize-1);
	}


	private void setPCA(int nodei, int nodej) {
		Queue<Reading> q1 = SMDB.getInstance().getReadings(nodei);
		Queue<Reading> q2 = SMDB.getInstance().getReadings(nodej);
		LinkedList<Double> q1r = new LinkedList<Double>() , q2r = new LinkedList<Double>();
		extractValids(q1, q2, q1r, q2r);
		double[][] reading = new double[2][q1r.size()];
		setupReading(reading, q1r, q2r);
		PCA.getInstance().setReading(reading);
	}

	private void setupReading(double[][] reading, LinkedList<Double> q1r, LinkedList<Double> q2r) {
		int i = 0;
		// TODO Rewrite referencereading use EWXX
		referencereading[0] = q1r.getLast();
		referencereading[1] = q2r.getLast();
		while(q1r.size()!=0){
			reading[0][i] = q1r.remove();
			reading[1][i] = q2r.remove();
			i++;
		}
	}

	private void extractValids(Queue<Reading> q1, Queue<Reading> q2, LinkedList<Double> q1r, LinkedList<Double> q2r) {
		Iterator<Reading> it1 = q1.iterator();
		Iterator<Reading> it2 = q2.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			Reading reading1 = it1.next();
			Reading reading2 = it2.next();
			if (reading1.isValid() && reading2.isValid()) {
				q1r.add(reading1.value());
				q2r.add(reading2.value());
			}
		}
	}

}
