package mdfr.dimensionality.reduction;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import math.jwave.Transform;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.wavelets.haar.Haar1;
import mdfr.datastructure.Data;
import mdfr.datastructure.TimeSeries;
import mdfr.dimensionality.datastructure.DWTData;
import mdfr.distance.Distance;
import mdfr.utility.DataListOperator;

public class DWT extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(DWT.class);
	
	public DWT(double windowsize) {
		setWindowSize(windowsize);
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		DWTData dwt = getDR(ts);
		return getFullResolutionDR(dwt, ts);
	}

	public TimeSeries getFullResolutionDR( DWTData dwt, TimeSeries ref) {
		TimeSeries drfull = new TimeSeries();
		double[] tsHilb = dwt.hilb();
		tsHilb = recoverNullHighFrequency(tsHilb, ref.size());
		Transform t = new Transform(new FastWaveletTransform(new Haar1()));
		double[] value = t.reverse(tsHilb);
		for(int i = 0 ; i < ref.size() ; i++){
			drfull.add(new Data(ref.get(i).time(), value[i]));
		}
		return drfull;
	}
	
	private double[] recoverNullHighFrequency(double[] input, int length){
		double[] output = new double[length];
		for(int i = 0 ; i < length ; i++){
			if(i < input.length){
				output[i] = input[i];
			}else{
				output[i] = 0;
			}
		}
		return output;
	}


	/**
	 * Here we use double array as the ouput data structure to store the DWT results in the Hilbert Space.
	 * @param ts
	 * @return a double array containing the DWT result in Hilbert Space.
	 */
	@Override
	public DWTData getDR(TimeSeries ts) {
		double tslength = Math.log(ts.size())/Math.log(2);
		// TODO reconstruct this to have a more flexible solution.
		if(tslength % 1 != 0){
			logger.info("The input Time Series length does not match the windowsize");
			return null;
		}
		LinkedList<Double> tsvalues = DataListOperator.getInstance().getValueList(ts);
		double[] valuearray = DataListOperator.getInstance().linkedDoubleListToArray(tsvalues);
		return getDR(valuearray);
	}
	
	public DWTData getDR(double[] valuearray){
		Transform t = new Transform(new FastWaveletTransform(new Haar1()));
		DWTData tsHilb = new DWTData(t.forward(valuearray));
		// This calculates log_2(x).
		double resolution = Math.log(windowsize)/Math.log(2);
		for(int i = 0 ; i < resolution ; i++){
			tsHilb = removeHighestFrequency(tsHilb);
		}
		return tsHilb;
	}

	private DWTData removeHighestFrequency(DWTData input){
		DWTData output;
		double[] hilb = new double[input.size()/2];
		for(int i = 0 ; i < hilb.length; i++){
			hilb[i] = input.value(i);
		}
		output = new DWTData(hilb);
		return output;
	}
	
	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		double[] dr1 = getDR(ts1).hilb();
		double[] dr2 = getDR(ts2).hilb();
		return distance.calDistance(dr1, dr2);
	}
	
	public double getDistanceTest(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		TimeSeries dr1full = getFullResolutionDR(ts1);
		TimeSeries dr2full = getFullResolutionDR(ts2);
		double[][] dr1fullarray = DataListOperator.getInstance().linkedDataListToArray(dr1full);
		double[][] dr2fullarray = DataListOperator.getInstance().linkedDataListToArray(dr2full);
		return distance.calDistance(dr1fullarray[1], dr2fullarray[1]);
	}

}
