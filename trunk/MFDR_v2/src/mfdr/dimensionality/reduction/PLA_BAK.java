//package mfdr.dimensionality.reduction;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import flanagan.analysis.Regression;
//import mfdr.datastructure.Data;
//import mfdr.datastructure.TimeSeries;
//import mfdr.dimensionality.datastructure.PLAData;
//import mfdr.distance.Distance;
//
//public class PLA_BAK extends DimensionalityReduction {
//	private static Log logger = LogFactory.getLog(PLA_BAK.class);
//	
//	public PLA_BAK(double windowsize){
//	}
//	
//	@Override
//	public TimeSeries getFullResolutionDR(TimeSeries ts) {
//		TimeSeries plafull = new TimeSeries();
//		LinkedList<PLAData> pla = getDR(ts);
//		calFullResolutionDR(ts, plafull, pla);
//		return plafull;
//	}
//	
//	public TimeSeries getFullResolutionDR(LinkedList<PLAData> pla, TimeSeries ref){
//		TimeSeries plafull = new TimeSeries();
//		calFullResolutionDR(ref, plafull, pla);
//		return plafull;
//	}
//
//	private void calFullResolutionDR(TimeSeries ts, TimeSeries plafull,
//			LinkedList<PLAData> pla) {
//		double init_time = -1;
//		double end_time = -1;
//		try {
//			end_time = pla.get(0).time();
//		} catch (Exception e) {
//			logger.info("Empty PLA retuls" + e);
//			return;
//		}
//		Iterator<Data> it = ts.iterator();
//		Data data = (Data) it.next();
//		// When pla has end point windows.
//		for(int index = 1 ; index < pla.size() ; index++){
//			init_time = end_time;
//			end_time = pla.get(index).time();
//			while(data.time() >= init_time && data.time() < end_time){
//				double value = pla.get(index-1).getValue(data.time());
//				plafull.add(new Data(data.time(), value));
//				if(it.hasNext()){
//					data = it.next();
//				}
//			}
//		}
//		double value = pla.get(pla.size()-1).getValue(data.time());
//		plafull.add(new Data(data.time(), value));
//		while(it.hasNext()){
//			data = it.next();
//			value = pla.get(pla.size()-1).getValue(data.time());
//			plafull.add(new Data(data.time(), value));
//		}
//	}
//
//	@Override
//	public LinkedList<PLAData> getDR(TimeSeries ts) {
//		LinkedList<PLAData> pla = new LinkedList<PLAData>();
//		boolean isfirstround = true;
//		Data data = new Data(0, 0);
//		Iterator<Data> it = ts.iterator();
//		while (it.hasNext()) {
//			Map<Double, Double> temp = new HashMap<Double, Double>();
//			// If first round, initiate data.
//			if(isfirstround){
//				data = (Data) it.next();
//				isfirstround = false;
//			}
//			double init_time = data.time();
//			double end_time = data.time();
//			while((end_time - init_time) < this.windowsize ){
//				// Add data to temp
//				temp.put(data.time(), data.value());
//				// If no next item.
//				if(!it.hasNext())
//					break;
//				// iterate the next item.
//				data = it.next();
//				end_time = data.time();
//			}
//			try {
//				// Add PLA result to dr
//				double[] x = new double[temp.size()];
//				double[] y = new double[temp.size()];
//				// TODO This can be extracted to Utility
//				convertMapToArray(temp, x, y);
//				Regression reg = new Regression(x, y);
//				reg.linear();
//				double[] coeff = reg.getBestEstimates();
//				pla.add(new PLAData(init_time, coeff[0], coeff[1]));		
//			} catch (Exception e) {
//				logger.info("PLAData Conversion Error" + e);
//			}
//		}
//		return pla;
//	}
//
//	private void convertMapToArray(Map<Double, Double> temp, double[] x, double[] y) {
//		int index = 0;
//		Iterator<Double> it2 = temp.keySet().iterator(); 
//		while (it2.hasNext()) {
//			Double time = (Double) it2.next();
//			x[index] = time;
//			y[index] = temp.get(time);
//			index++;
//		}
//	}
//
//	@Override
//	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
//		TimeSeries dr1full = getFullResolutionDR(ts1);
//		TimeSeries dr2full = getFullResolutionDR(ts2);
//		return distance.calDistance(dr1full, dr2full, dr1full);
//	}
//	
//	public double getDistance(LinkedList<PLAData> dr1, LinkedList<PLAData> dr2, TimeSeries ref, Distance distance) {
//		TimeSeries dr1full = getFullResolutionDR(dr1, ref);
//		TimeSeries dr2full = getFullResolutionDR(dr2, ref);
//		return distance.calDistance(dr1full, dr2full, dr1full);
//	}
//	// TODO These are only temperate distance functions, need to implement a real one
//	public double getDistance(LinkedList<PLAData> dr1, LinkedList<PLAData> dr2, Distance distance) {
////		TimeSeries dr1full = getFullResolutionDR(dr1, ref);
////		TimeSeries dr2full = getFullResolutionDR(dr2, ref);
////		return distance.calDistance(dr1full, dr2full, dr1full);
//		return 0;
//	}
//}
