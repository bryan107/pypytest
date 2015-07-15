package mfdr.dimensionality.reduction;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.PAAData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.distance.Distance;

public class PAA extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(PAA.class);
	
	public PAA(int NoC){
		setNoC(NoC);
	}
	
	@Override
	public String name() {
		return "PAA";
	}
	
	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries paafull = new TimeSeries();
		LinkedList<PAAData> paa = getDR(ts);
		// set it_paa
		PAAData data_paa_front, data_paa_rare;
		Iterator<PAAData> it_paa = paa.iterator();
		if(paa.size() >1){
			data_paa_front = it_paa.next();
			data_paa_rare = it_paa.next();
		} else if(paa.size() == 1){
			data_paa_front = it_paa.next();
			data_paa_rare = null;
		} else{
			logger.info("No PAA can be acquired");
			return null;
		}
		// Iterate through ts
		Iterator<Data> it = ts.iterator();
		while (it.hasNext()) {
			Data data_ts = (Data) it.next();
			// If at the last window of PAA
			if(data_paa_rare == null){
				paafull.add(new Data(data_ts.time(), data_paa_front.average()));
			}
			// If at the middle of PAA
			else if((data_ts.time() >= data_paa_front.time()) && data_ts.time() < data_paa_rare.time()){
				paafull.add(new Data(data_ts.time(), data_paa_front.average()));
			}
			// If iterate to the next window of PAA
			else if(data_ts.time() >= data_paa_rare.time()){
				paafull.add(new Data(data_ts.time(), data_paa_rare.average()));
				data_paa_front = data_paa_rare;
				// Exam whether or not achieve the last window.
				if(it_paa.hasNext()){
					data_paa_rare = it_paa.next();
				}else{
					data_paa_rare = null;
				}
			}else{
				logger.info("Exception occurs, please check the correction of input TimeSeries");
			}
		}
		return paafull;
	}

	@Override
	public LinkedList<PAAData> getDR(TimeSeries ts) {
		LinkedList<PAAData> paa = new LinkedList<PAAData>();
		int PAAwindowsize = ts.size()/NoC;
		boolean isfirstround = true;
		Data data = new Data(0, 0);
		Iterator<Data> it = ts.iterator();
		while (it.hasNext()) {
			// If first round, initiate data.
			if(isfirstround){
				 data = (Data) it.next();
				isfirstround = false;
			}
			double sum = 0;
			int count = 0;
			double init_time = data.time();
			while(count < PAAwindowsize){
				// Add data to temp
				sum += data.value();
				count++;
				// If no next item.
				if(!it.hasNext())
					break;
				// iterate the next item.
				data = it.next();
			}
			try {
				// Add PAA result to dr
				double average = sum/count;
				paa.add(new PAAData(init_time, average));		
			} catch (Exception e) {
				logger.info("Zero count when calculate PAA" + e);
			}
		}
		return paa;
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		TimeSeries dr1 = getFullResolutionDR(ts1);
		TimeSeries dr2 = getFullResolutionDR(ts2);
		return distance.calDistance(dr1, dr2, ts1);
	}
	
	public double getDistance(LinkedList<PAAData> dr1, LinkedList<PAAData> dr2,int size, Distance distance) {
		if (dr1.size() != dr2.size()) {
			logger.info("PAA inputs are at different lengths");
			return 0;
		}
		double dist_total = 0;
		double l = size / dr1.size();
		for (int i = 0; i < dr1.size(); i++) {
			PAAData paa_1 = dr1.get(i);
			PAAData paa_2 = dr2.get(i);
			dist_total += Math.pow(paa_1.average()-paa_2.average(), 2)*l;
		}
		return Math.sqrt(dist_total);
	}

}
