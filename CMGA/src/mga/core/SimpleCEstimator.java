package mga.core;

import java.util.Iterator;
import java.util.LinkedList;

public class SimpleCEstimator implements CorrelationEstimator {

	@Override
	public boolean hasLink(NodeManager nmanager, LinkedList<Integer> cluster1,
			LinkedList<Integer> cluster2, double mincorrelation) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double calcCorrelation(NodeManager nmanager,
			LinkedList<Integer> cluster1, LinkedList<Integer> cluster2,
			double mincorrelation) {
		double result = 0;
		Iterator<Integer> it1 = cluster1.iterator();
		while (it1.hasNext()) {
			int nodeid1 = it1.next();
			Iterator<Integer> it2 = cluster2.iterator();
			while (it2.hasNext()) {
				int nodeid2 = it2.next();
				result += nmanager.getCorrelation(nodeid1, nodeid2);
			}
		}
		return result;
	}

}
