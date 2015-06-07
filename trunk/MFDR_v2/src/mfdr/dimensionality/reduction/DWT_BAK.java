//package mfdr.dimensionality.reduction;
//
//import java.util.LinkedList;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import math.jwave.Transform;
//import math.jwave.transforms.FastWaveletTransform;
//import math.jwave.transforms.wavelets.haar.*;
//import mfdr.datastructure.Data;
//import mfdr.datastructure.TimeSeries;
//import mfdr.dimensionality.datastructure.DWTData;
//import mfdr.distance.Distance;
//import mfdr.utility.DataListOperator;
//
//public class DWT_BAK extends DimensionalityReduction {
//	private static Log logger = LogFactory.getLog(DWT.class);
//	
//	public DWT_BAK(double windowsize) {
//		setWindowSize(windowsize);
//	}
//
//	@Override
//	public TimeSeries getFullResolutionDR(TimeSeries ts) {
//		DWTData dwt = getDR(ts);
//		return getFullResolutionDR(dwt, ts);
//	}
//
//	public TimeSeries getFullResolutionDR( DWTData dwt, TimeSeries ref) {
//		TimeSeries drfull = new TimeSeries();
//		double[] tsHilb = dwt.hilb();
//		tsHilb = recoverNullHighFrequency(tsHilb, ref.size());
//		Transform t = new Transform(new FastWaveletTransform(new Haar1()));
//		double[] value = t.reverse(tsHilb);
//		for(int i = 0 ; i < ref.size() ; i++){
//			drfull.add(new Data(ref.get(i).time(), value[i]));
//		}
//		return drfull;
//	}
//	
//	private double[] recoverNullHighFrequency(double[] input, int length){
//		double[] output = new double[length];
//		for(int i = 0 ; i < length ; i++){
//			if(i < input.length){
//				output[i] = input[i];
//			}else{
//				output[i] = 0;
//			}
//		}
//		return output;
//	}
//
//
//	/**
//	 * Here we use double array as the ouput data structure to store the DWT results in the Hilbert Space.
//	 * @param ts
//	 * @return a double array containing the DWT result in Hilbert Space.
//	 */
//	@Override
//	public DWTData getDR(TimeSeries ts) {
//		double tslength = Math.log(ts.size())/Math.log(2);
//		// TODO reconstruct this to have a more flexible solution.
//		if(tslength % 1 != 0){
//			logger.info("The input Time Series length does not match the windowsize");
//			return null;
//		}
//		LinkedList<Double> tsvalues = DataListOperator.getInstance().getValueList(ts);
//		double[] valuearray = DataListOperator.getInstance().linkedDoubleListToArray(tsvalues);
//		return getDR(valuearray);
//	}
//	
//	public DWTData getDR(double[] valuearray){
//		Transform t = new Transform(new FastWaveletTransform(new Haar1()));
//		DWTData tsHilb = new DWTData(t.forward(valuearray));
//		// This calculates log_2(x).
//		double resolution = Math.log(windowsize)/Math.log(2);
//		for(int i = 0 ; i < resolution ; i++){
//			tsHilb = removeHighestFrequency(tsHilb);
//		}
//		return tsHilb;
//	}
//
//	private DWTData removeHighestFrequency(DWTData input){
//		DWTData output;
//		double[] hilb = new double[input.size()/2];
//		for(int i = 0 ; i < hilb.length; i++){
//			hilb[i] = input.value(i);
//		}
//		output = new DWTData(hilb);
//		return output;
//	}
//	// TODO These are only temperate distance functions, need to implement a real one
//	@Override
//	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
//		double[] dr1 = getDR(ts1).hilb();
//		double[] dr2 = getDR(ts2).hilb();
//		return distance.calDistance(dr1, dr2);
//	}
//	
//	public double getDistance(DWTData dwt1, DWTData dwt2, Distance distance){
//		return distance.calDistance(dwt1.hilb(), dwt2.hilb());
//	}
//	
//	/**
//	 * This function calculate the distance between two dwt lists.
//	 * 
//	 * @param dwt_list1
//	 * @param dwt_list2
//	 * @param distance
//	 * @return
//	 */
//	public double getDistance(LinkedList<DWTData> dwt_list1 , LinkedList<DWTData> dwt_list2 , Distance distance){
//		double[] hilb1 = new double[dwt_list1.peek().hilb().length * dwt_list1.size()];
//		double[] hilb2 = new double[dwt_list2.peek().hilb().length * dwt_list2.size()];
//		if(hilb1.length != hilb2.length){
//			logger.info("The length of input dwt LinkedList is not equal.");
//			return 0;
//		}
//		int datalength = dwt_list1.size();
//		int datasize = dwt_list1.peek().hilb().length;
//		for(int i = 0 ; i < datalength ; i++){
//			for(int j = 0 ; j < datasize ; j++){
//				hilb1[i*datasize+j] = dwt_list1.get(i).hilb()[j]; 
//				hilb2[i*datasize+j] = dwt_list2.get(i).hilb()[j]; 
//			}
//		}
//		return distance.calDistance(hilb1, hilb2);
//	}
//
//	/**
//	 * This is a redundant function that calculate distances after restore DWT into full resolution
//	 * @param ts1
//	 * @param ts2
//	 * @param distance
//	 * @return
//	 */
//	public double getDistanceTest(TimeSeries ts1, TimeSeries ts2, Distance distance) {
//		TimeSeries dr1full = getFullResolutionDR(ts1);
//		TimeSeries dr2full = getFullResolutionDR(ts2);
//		double[][] dr1fullarray = DataListOperator.getInstance().linkedDataListToArray(dr1full);
//		double[][] dr2fullarray = DataListOperator.getInstance().linkedDataListToArray(dr2full);
//		return distance.calDistance(dr1fullarray[1], dr2fullarray[1]);
//	}
//
//}
