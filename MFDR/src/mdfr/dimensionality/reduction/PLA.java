package mdfr.dimensionality.reduction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Regression;
import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.dimensionality.datastructure.PLAData;
import mdfr.distance.Distance;

public class PLA implements DimentionalityReduction {
	private static Log logger = LogFactory.getLog(PLA.class);
	private double windowsize;
	
	public PLA(double windowsize){
		setWindowSize(windowsize);
	}
	
	public double windowSize(){
		return this.windowsize;
	}
	
	@Override
	public void setWindowSize(double windowsize) {
		this.windowsize = windowsize;
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts, double windowsize) {
		setWindowSize(windowsize);
		return getFullResolutionDR(ts);
	}

	@Override
	public Object getDR(TimeSeries ts) {
		LinkedList<PLAData> pla = new LinkedList<PLAData>();
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
			double init_time = data.time();
			double end_time = data.time();
			while((end_time - init_time) < this.windowsize ){
				// Add data to temp
				temp.put(data.time(), data.value());
				// If no next item.
				if(!it.hasNext())
					break;
				// iterate the next item.
				data = it.next();
				end_time = data.time();
			}
			try {
				// Add PLA result to dr
				double[] x = new double[temp.size()];
				double[] y = new double[temp.size()];
				// TODO This can be extracted to Utility
				convertMapToArray(temp, x);
				Regression reg = new Regression(x, y);
				reg.linear();
				double[] coeff = reg.getBestEstimates();
				pla.add(new PLAData(init_time, coeff[0], coeff[1]));		
			} catch (Exception e) {
				logger.info("PLAData Conversion Error" + e);
			}
		}
		return pla;
	}

	private void convertMapToArray(Map<Double, Double> temp, double[] x) {
		int index = 0;
		Iterator<Double> it2 = temp.keySet().iterator(); 
		while (it2.hasNext()) {
			Double time = (Double) it2.next();
			x[index] = time;
			x[index] = temp.get(time);
			index++;
		}
	}

	@Override
	public Object getDR(TimeSeries ts, double windowsize) {
		setWindowSize(windowsize);
		return getDR(ts);
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		// TODO Auto-generated method stub
		return 0;
	}

}
