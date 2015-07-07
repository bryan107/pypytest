package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import flanagan.analysis.Stat;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.APCAData;
import mfdr.distance.Distance;
import mfdr.utility.DataListOperator;

public class APCA extends DimensionalityReduction {

	public APCA(){
		
	}
	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries drfull = new TimeSeries();
		LinkedList<APCAData> dr = getDR(ts);
		int count = 0;
		for(int i = 0 ; i < dr.size() ; i++){
			APCAData data = dr.get(i);
			for(int j = 0 ; j < data.length() ; j++){
				drfull.add(new Data(ts.get(count).time(), data.average()));
				count++;
			}
		}
		return drfull;
	}
	
	@Override
	public String name() {
		return "APCA";
	}

	@Override
	public LinkedList<APCAData> getDR(TimeSeries ts) {
		LinkedList<APCAData> datalist = new LinkedList<APCAData>(); 
		double[] data = DataListOperator.getInstance().linkedListToArray(ts, (short)1);
		// Stat
		double mean = Stat.mean(data);
		double deviation = Stat.standardDeviation(data);
		//InitW
		double init_time = ts.get(0).time();
		int count = 1;
		double sum = ts.get(0).value();
		int previouswindow = (int) ((ts.get(0).value()-mean) / deviation);
		for(int i = 0 ; i < ts.size() ; i++){
			int currentwindow = (int) ((ts.get(i).value()-mean) / deviation);
			if(currentwindow == previouswindow){
				sum+= ts.get(i).value();
				count++;
				continue;
			}else{
				// Store
				datalist.add(new APCAData(init_time, sum/count, count));
				// Init
				init_time = ts.get(i).time();
				count = 1;
				sum = ts.get(i).value();
				previouswindow = currentwindow;
			}
		}
		//Store
		datalist.add(new APCAData(init_time, sum/count, count));
		return datalist;
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		// TODO Auto-generated method stub
		return 0;
	}



}
