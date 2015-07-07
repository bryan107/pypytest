package mfdr.dimensionality.reduction;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.distance.Distance;

public class PLA extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(PLA.class);

	public PLA(int NoC) {
		setNoC(NoC);
	}
	
	@Override
	public String name() {
		return "PLA";
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
		int windowsize = ts.size()/pla.size();
		Iterator<Data> it = ts.iterator();
		int winnum = 0;
		int plaindex = 0;
		while(it.hasNext()){
			Data data = it.next();
			double value = pla.get(plaindex).getValue(data.time()-(plaindex*windowsize));
			plafull.add(new Data(data.time(), value));
			winnum++;
			if(winnum % windowsize == 0){
				plaindex++;
				if(plaindex >= pla.size()){
					break;
				}
			}
		}
		for(;winnum < ts.size();winnum++){
			double value = pla.getLast().getValue(ts.get(winnum).time()-((pla.size()-1)*windowsize));
			plafull.add(new Data(ts.get(winnum).time(), value));
		}
	}

	@Override
	public LinkedList<PLAData> getDR(TimeSeries ts) {
		LinkedList<PLAData> pla = new LinkedList<PLAData>();
		if (NoC == 0) {
			pla.add(new PLAData(ts.peek().time(), 0, 0));
			return pla;
		}
		// n = window size
		double l = ts.size() / NoC;
		double j = 1;
		int i = 1;
		double asum = 0, bsum = 0;
		while (j <= ts.size()) {
			asum += (j - (i - 1)*l - (l + 1)/2) * ts.get((int) j - 1).value();
			bsum += (j - (i - 1)*l - (2*l+1)/3) * ts.get((int) j - 1).value();
			if (j % l == 0) {
				double a = 12 * asum / (l * (l + 1) * (l - 1));
				double b = 6 * bsum / (l * (1 - l));
				pla.add(new PLAData(ts.get((int) (j - l)).time(), b, a));
				asum = 0;
				bsum = 0;
				i++;
			}
			j++;
		}
		return pla;
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
			double part1 = ((l * (l + 1) * (2*l + 1))/6) * Math.pow(a3, 2); 
			double part2 = l * (l + 1) * a3 * b3;
			double part3 = l * Math.pow(b3, 2);
			dist_total +=  part1 + part2 + part3;
		}
		return Math.sqrt(dist_total);
	}
}
