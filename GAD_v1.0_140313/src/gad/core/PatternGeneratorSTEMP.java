package gad.core;
// TODO Patter Generator rebuild
import flanagan.analysis.Stat;
import gad.calc.PCA;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PatternGeneratorSTEMP implements PatternGenerator {
	private static Log logger = LogFactory.getLog(PatternGeneratorSTEMP.class);
	private int windowsize;
	private final short GD = 3;
	public PatternGeneratorSTEMP(int windowsize) {
		this.windowsize = windowsize;
	}
	@Override
	public Map<Integer, Map<Integer, EstimatedVariancePCA>> getEstimation(Map<Integer, Double> reading) {
		Map<Integer, Map<Integer, EstimatedVariancePCA>> E = new HashMap<Integer, Map<Integer, EstimatedVariancePCA>>();
		Iterator<Integer> it1 = reading.keySet().iterator();
		while(it1.hasNext()) {
			int nodeid_i = it1.next();
			Map<Integer, EstimatedVariancePCA> content = new HashMap<Integer, EstimatedVariancePCA>();
			Iterator<Integer> it2 = reading.keySet().iterator();
			while(it2.hasNext()) {
				int nodeid_j = it2.next();
				if(nodeid_i == nodeid_j){
					continue;
				}
				try {
					boolean dc1 = SMDB.getInstance().getDeviceCondition(nodeid_i);
					boolean dc2 = SMDB.getInstance().getDeviceCondition(nodeid_j);
					Queue<Reading> q1 = SMDB.getInstance().getReadings(nodeid_i);
					Queue<Reading> q2 = SMDB.getInstance().getReadings(nodeid_j);
					if(q1.size() < windowsize || q2.size() < windowsize || dc1 == false || dc2 == false){
						content.put(nodeid_j, new EstimatedVariancePCA(null, null, null, false));
						continue;
					}
					LinkedList<Double> q1r = new LinkedList<Double>() , q2r = new LinkedList<Double>();
					extractValids(q1, q2, q1r, q2r);
					double[] slopes = new double[q1r.size()];
					int i = 0;
					while(!q1r.isEmpty()){
						slopes[i] = (q2r.remove()/q1r.remove());
					}
					double estimation[] = new double[0];
					estimation[0] = Stat.median(slopes);
//					content.put(nodeid_j, new EstimatedVariance(null, null, estimation, true));
					
				} catch (Exception e2) {	// Without enough data for generate estimations
					logger.warn("No data for estimation (" + e2 +")");
					content.put(nodeid_j, new EstimatedVariancePCA(null, null, null, false));
					continue;
				}
			}
			E.put(nodeid_i, content);
		}
		return null;
	}

	
	private void extractValids(Queue<Reading> q1, Queue<Reading> q2, LinkedList<Double> q1r, LinkedList<Double> q2r) {
		Iterator<Reading> it1 = q1.iterator();
		Iterator<Reading> it2 = q2.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			Reading reading1 = it1.next();
			Reading reading2 = it2.next();
			if (reading1.isValid() == GD && reading2.isValid() == GD) {
				q1r.add(reading1.value());
				q2r.add(reading2.value());
			}
		}
	}
}
