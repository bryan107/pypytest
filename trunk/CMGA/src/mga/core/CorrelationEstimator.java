package mga.core;

import java.util.LinkedList;

public interface CorrelationEstimator {

	public boolean hasLink(NodeManager nmanager, LinkedList<Integer> cluster1,
			LinkedList<Integer> cluster2, double mincorrelation);

	public double calcCorrelation(NodeManager nmanager,
			LinkedList<Integer> cluster1, LinkedList<Integer> cluster2,
			double mincorrelation);
}
