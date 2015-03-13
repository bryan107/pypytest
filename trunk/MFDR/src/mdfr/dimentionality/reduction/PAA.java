package mdfr.dimentionality.reduction;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.distance.Distance;

public class PAA implements DimentionalityReduction {
	private static Log logger = LogFactory.getLog(PAA.class);
	private double windowsize;
	
	@Override
	public void setWindowSize(double windowsize) {
		this.windowsize = windowsize;
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries paafull = new TimeSeries();
		TimeSeries paa = (TimeSeries) getDR(ts);
		// set it_paa
		Data data_paa_front, data_paa_rare;
		Iterator<Data> it_paa = paa.iterator();
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
				paafull.add(new Data(data_ts.time(), data_paa_front.value()));
			}
			// If at the middle of PAA
			else if((data_ts.time() >= data_paa_front.time()) && data_ts.time() < data_paa_rare.time()){
				paafull.add(new Data(data_ts.time(), data_paa_front.value()));
			}
			// If iterate to the next window of PAA
			else if(data_ts.time() >= data_paa_rare.time()){
				paafull.add(new Data(data_ts.time(), data_paa_rare.value()));
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
	public TimeSeries getFullResolutionDR(TimeSeries ts, double windowsize) {
		setWindowSize(windowsize);
		return getFullResolutionDR(ts);
	}

	@Override
	public Object getDR(TimeSeries ts) {
		TimeSeries paa = new TimeSeries();
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
			double end_time = data.time();
			while((end_time - init_time) < this.windowsize ){
				// Add data to temp
				sum += data.value();
				count++;
				// If no next item.
				if(!it.hasNext())
					break;
				// iterate the next item.
				data = it.next();
				end_time = data.time();
			}
			try {
				// Add PAA result to dr
				paa.add(new Data(init_time, sum/count));		
			} catch (Exception e) {
				logger.info("Zero count when calculate PAA" + e);
			}
		}
		return paa;
	}

	@Override
	public Object getDR(TimeSeries ts, double windowsize) {
		setWindowSize(windowsize);
		return getDR(ts);
	}

	@Override
	public TimeSeries getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		// TODO return distance
		return null;
	}

}
