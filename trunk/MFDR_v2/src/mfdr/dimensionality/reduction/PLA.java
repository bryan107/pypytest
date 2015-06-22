package mfdr.dimensionality.reduction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.distance.Distance;
import mfdr.math.statistic.LinearEstimator;
import mfdr.math.statistic.LinearRegression;

public class PLA extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(PLA.class);

	public PLA(int NoC) {
		setNoC(NoC);
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries plafull = new TimeSeries();
		LinkedList<PLAData> pla = getDR(ts);
		calFullResolutionDR(ts, plafull, pla);
		return plafull;
	}

	public TimeSeries getFullResolutionDR(LinkedList<PLAData> pla,
			TimeSeries ref) {
		TimeSeries plafull = new TimeSeries();
		calFullResolutionDR(ref, plafull, pla);
		return plafull;
	}

	private void calFullResolutionDR(TimeSeries ts, TimeSeries plafull,
			LinkedList<PLAData> pla) {
		if (pla == null) {
			getNullTimeSeries(ts, plafull);
			return;
		}
		double init_time = -1;
		double end_time = -1;
		try {
			end_time = pla.get(0).time();
		} catch (Exception e) {
			logger.info("Empty PLA retuls" + e);
			return;
		}
		Iterator<Data> it = ts.iterator();
		Data data = (Data) it.next();
		// When pla has end point windows.
		for (int index = 1; index < pla.size(); index++) {
			init_time = end_time;
			end_time = pla.get(index).time();
			while (data.time() >= init_time && data.time() < end_time) {
				double value = pla.get(index - 1).getValue(data.time());
				plafull.add(new Data(data.time(), value));
				if (it.hasNext()) {
					data = it.next();
				}
			}
		}
		double value = pla.get(pla.size() - 1).getValue(data.time());
		plafull.add(new Data(data.time(), value));
		while (it.hasNext()) {
			data = it.next();
			value = pla.get(pla.size() - 1).getValue(data.time());
			plafull.add(new Data(data.time(), value));
		}
	}

	@Override
	public LinkedList<PLAData> getDR(TimeSeries ts) {
		if (NoC == 0) {
			return null;
		}
		LinkedList<PLAData> pla = new LinkedList<PLAData>();
		// n = window size
		int l = ts.size() / NoC;
		int j = 1;
		int i = 1;
		double asum = 0, bsum = 0;
		while (j <= ts.size()) {
			asum += (j - (i - 1)*l - (l+1)/2) * ts.get(j - 1).value();
			bsum += (j - (i - 1)*l - (2*l+1)/3) * ts.get(j - 1).value();
			if (j % l == 0) {
				double a = 12 * asum / (l * (l + 1) * (l - 1));
				double b = 6 * bsum / (l * (1 - l));
				pla.add(new PLAData(ts.get(j - l).time(), b, a));
				asum = 0;
				bsum = 0;
				i++;
			}
			j++;
		}
		return pla;
	}

	private void getNullTimeSeries(TimeSeries ts, TimeSeries plafull) {
		for (int i = 0; i < ts.size(); i++) {
			plafull.add(new Data(ts.get(i).time(), 0));
		}
	}

	private void convertMapToArray(Map<Double, Double> temp, double[] x,
			double[] y) {
		int index = 0;
		Iterator<Double> it2 = temp.keySet().iterator();
		while (it2.hasNext()) {
			Double time = (Double) it2.next();
			x[index] = time;
			y[index] = temp.get(time);
			index++;
		}
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		LinkedList<PLAData> dr1 = getDR(ts1);
		LinkedList<PLAData> dr2 = getDR(ts2);
		return getDistance(dr1, dr2, ts1, distance);
	}
	
	public double getBruteForceDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		TimeSeries dr1full = getFullResolutionDR(ts1);
		TimeSeries dr2full = getFullResolutionDR(ts2);
		return distance.calDistance(dr1full, dr2full, dr1full);
	}

	public double getDistanceOld(LinkedList<PLAData> dr1,
			LinkedList<PLAData> dr2, TimeSeries ref, Distance distance) {
		TimeSeries dr1full = getFullResolutionDR(dr1, ref);
		TimeSeries dr2full = getFullResolutionDR(dr2, ref);
		return distance.calDistance(dr1full, dr2full, dr1full);
	}

	// TODO These are only temperate distance functions, need to implement a
	// real one
	public double getDistance(LinkedList<PLAData> dr1, LinkedList<PLAData> dr2,
			TimeSeries ref, Distance distance) {
		if (dr1.size() != dr2.size()) {
			logger.info("PLA inputs are at different lengths");
			return 0;
		}
		double dist_total = 0;
		double l = ref.size() / dr1.size();
		for (int i = 0; i < dr1.size(); i++) {
			PLAData pla_1 = dr1.get(i);
			PLAData pla_2 = dr2.get(i);
			double a3 = pla_1.a1() - pla_2.a1();
			double b3 = pla_1.a0() - pla_2.a0();
			double part1 = (l * (l + 1) * (2l + 1)) / 6 * Math.pow(a3, 2); 
			double part2 = l * (l + 1) * a3 * b3;
			double part3 = l * Math.pow(b3, 2);
			dist_total +=  part1 + part2 + part3;
		}
		return Math.sqrt(dist_total);
	}
}
