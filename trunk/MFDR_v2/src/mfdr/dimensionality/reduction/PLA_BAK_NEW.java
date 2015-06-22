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

public class PLA_BAK_NEW extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(PLA_BAK_NEW.class);
	
	public PLA_BAK_NEW(int NoC){
		setNoC(NoC);
	}
	
	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries plafull = new TimeSeries();
		LinkedList<PLAData> pla = getDR(ts);
		calFullResolutionDR(ts, plafull, pla);
		return plafull;
	}
	
	public TimeSeries getFullResolutionDR(LinkedList<PLAData> pla, TimeSeries ref){
		TimeSeries plafull = new TimeSeries();
		calFullResolutionDR(ref, plafull, pla);
		return plafull;
	}

	private void calFullResolutionDR(TimeSeries ts, TimeSeries plafull,
			LinkedList<PLAData> pla) {
		if(pla == null){
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
		for(int index = 1 ; index < pla.size() ; index++){
			init_time = end_time;
			end_time = pla.get(index).time();
			while(data.time() >= init_time && data.time() < end_time){
				double value = pla.get(index-1).getValue(data.time());
				plafull.add(new Data(data.time(), value));
				if(it.hasNext()){
					data = it.next();
				}
			}
		}
		double value = pla.get(pla.size()-1).getValue(data.time());
		plafull.add(new Data(data.time(), value));
		while(it.hasNext()){
			data = it.next();
			value = pla.get(pla.size()-1).getValue(data.time());
			plafull.add(new Data(data.time(), value));
		}
	}

	@Override
	public LinkedList<PLAData> getDR(TimeSeries ts) {
		if(NoC == 0){
			return null;
		}
		LinkedList<PLAData> pla = new LinkedList<PLAData>();
		int PLAwindowsize = ts.size()/NoC;
		boolean isfirstround = true;
		Data data = new Data(0, 0);
		Iterator<Data> it = ts.iterator();
		while (it.hasNext()) {
			Map<Double, Double> temp = new HashMap<Double, Double>();
			// If first round, initiate data.
			if(isfirstround){
				data = (Data) it.next();
				isfirstround = false;
			}
			int count = 0;
			double init_time = data.time();
			while(count < PLAwindowsize ){
				// Add data to temp
				temp.put(data.time(), data.value());
				count++;
				// If no next item.
				if(!it.hasNext())
					break;
				// iterate the next item.
				data = it.next();
			}
			try {
				// Add PLA result to dr
				double[] x = new double[temp.size()];
				double[] y = new double[temp.size()];
				convertMapToArray(temp, x, y);
				// ********** TEST ************* //
				LinearRegression reg = new LinearEstimator() ;
				double[] coeff = reg.getEstimates(x, y);
				// ***************************** //
				pla.add(new PLAData(init_time, coeff[0], coeff[1]));		
			} catch (Exception e) {
				logger.info("PLAData Conversion Error" + e);
			}
		}
		return pla;
	}

	private void getNullTimeSeries(TimeSeries ts, TimeSeries plafull){
		for(int i = 0 ; i < ts.size() ; i++){
			plafull.add(new Data(ts.get(i).time(), 0));
		}
	}
	
	private void convertMapToArray(Map<Double, Double> temp, double[] x, double[] y) {
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
		TimeSeries dr1full = getFullResolutionDR(ts1);
		TimeSeries dr2full = getFullResolutionDR(ts2);
		return distance.calDistance(dr1full, dr2full, dr1full);
	}
	
	public double getDistanceOld(LinkedList<PLAData> dr1, LinkedList<PLAData> dr2, TimeSeries ref, Distance distance) {
		TimeSeries dr1full = getFullResolutionDR(dr1, ref);
		TimeSeries dr2full = getFullResolutionDR(dr2, ref);
		return distance.calDistance(dr1full, dr2full, dr1full);
	}
	// TODO These are only temperate distance functions, need to implement a real one
	public double getDistance(LinkedList<PLAData> dr1, LinkedList<PLAData> dr2, TimeSeries ref, Distance distance) {
		if(dr1.size() != dr2.size()){
			logger.info("PLA inputs are at different lengths");
			return 0;
		}
		double dist_total = 0;
		for(int i = 0 ; i < dr1.size() ; i++){
			PLAData pla_1 = dr1.get(i);
			PLAData pla_2 = dr2.get(i);
			double a = pla_1.a1()-pla_2.a1();
			double b = pla_1.a0()-pla_2.a0();
			double l = ref.size()/dr1.size();
			dist_total += (l*(l+1)*(2l+1))*Math.pow(a, 2)/6 + l*(l+1)*a*b + l*Math.pow(b, 2);
		}
		return Math.sqrt(dist_total);
	}
}
