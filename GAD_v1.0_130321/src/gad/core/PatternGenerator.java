package gad.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gad.calc.PCA;

public class PatternGenerator {

	private static Log logger = LogFactory.getLog(PatternGenerator.class);

	public PatternGenerator() {
		logger.info("PatternGenerator Start");
	}

	public Map<Integer, Map<Integer, EstimatedVariance>> getEstimation(
			int[] nodeids) {
		Map<Integer, Map<Integer, EstimatedVariance>> E = new HashMap<Integer, Map<Integer, EstimatedVariance>>();
		for (int i = 0; i < nodeids.length; i++) {
			Map<Integer, EstimatedVariance> content = new HashMap<Integer, EstimatedVariance>();
			for (int j = 0; j < nodeids.length; j++) {
				setPCA(nodeids[i], nodeids[j]);
				double[][] direction = PCA.getInstance().getEigenVector();
				double[] deviation = PCA.getInstance().getDeviations();
				content.put(j, new EstimatedVariance(direction, deviation));
			}
			E.put(i, content);
		}
		return E;
	}

	@SuppressWarnings("null")
	private void setPCA(int nodei, int nodej) {
		Queue<Reading> q1 = SMDB.getInstance().getReadings(nodei);
		Queue<Reading> q2 = SMDB.getInstance().getReadings(nodej);
		Queue<Double> q1r = new LinkedList<Double>() , q2r = new LinkedList<Double>();
		extractValids(q1, q2, q1r, q2r);
		double[][] reading = new double[2][q1r.size()];
		setupReading(q1r, q2r, reading);
		PCA.getInstance().setReading(reading);
	}

	private void setupReading(Queue<Double> q1r, Queue<Double> q2r,
			double[][] reading) {
		int i = 0;
		while(q1r.size()!=0){
			reading[0][i] = q1r.remove();
			reading[1][i] = q2r.remove();
			i++;
		}
	}

	private void extractValids(Queue<Reading> q1, Queue<Reading> q2,
			Queue<Double> q1r, Queue<Double> q2r) {
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
