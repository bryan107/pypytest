package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import math.jwave.Transform;
import math.jwave.transforms.AncientEgyptianDecomposition;
import math.jwave.transforms.DiscreteFourierTransform;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.distance.Distance;
import mfdr.utility.DataListOperator;

public class DFT extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(DFT.class);
	private int noc;
	
	public DFT(double windowsize, int noc) {
		setWindowSize(windowsize);
		setNOC(noc);
	}
	
	public void setNOC(int noc){
		this.noc = noc;
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		DFTData dft = getDR(ts);
		return getFullResolutionDR(dft, ts, this.noc);
	}

	public TimeSeries getFullResolutionDR(DFTData dft, TimeSeries ref, int noc) {
		TimeSeries drfull = new TimeSeries();
		double[] tsHilb = dft.hilb(noc);
		// TODO Fix here is a bug
//		tsHilb = recoverNullHighFrequency(tsHilb, ref.size());
		Transform t = new Transform(new AncientEgyptianDecomposition(new DiscreteFourierTransform()));
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
	 * Here we use double array as the ouput data structure to store the DFT results in the Hilbert Space.
	 * @param ts
	 * @return a double array containing the DWT result in Hilbert Space.
	 */
	@Override
	public DFTData getDR(TimeSeries ts) {
		double tslength = Math.log(ts.size())/Math.log(2);
		// TODO reconstruct this to have a more flexible solution.
		if(tslength % 1 != 0){
			logger.info("The input Time Series length does not match the windowsize");
//			return null;
		}
		LinkedList<Double> tsvalues = DataListOperator.getInstance().getValueList(ts);
		double[] valuearray = DataListOperator.getInstance().linkedDoubleListToArray(tsvalues);
		return getDR(valuearray);
	}
	
	public DFTData getDR(double[] valuearray){
		Transform t = new Transform(new AncientEgyptianDecomposition(new DiscreteFourierTransform()));
		DFTData tsHilb = new DFTData(t.forward(valuearray));
		// This calculates log_2(x).
		double resolution = Math.log(windowsize)/Math.log(2);
		for(int i = 0 ; i < resolution ; i++){
			tsHilb = removeHighestFrequency(tsHilb);
		}
		return tsHilb;
	}

	private DFTData removeHighestFrequency(DFTData input){
		DFTData output;
		double[] hilb = new double[input.size()/2];
		for(int i = 0 ; i < hilb.length; i++){
			hilb[i] = input.value(i);
		}
		output = new DFTData(hilb);
		return output;
	}
	
	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		double[] dr1 = getDR(ts1).hilb(true, ts1.size());
		double[] dr2 = getDR(ts2).hilb(true, ts1.size());
		return distance.calDistance(dr1, dr2);
	}
	
	public double getDistance(DFTData dft1, DFTData dft2, Distance distance, int signallength){
		return distance.calDistance(dft1.hilb(true, signallength), dft2.hilb(true, signallength));
	}
	
	/**
	 * This function calculate the distance between two dwt lists.
	 * 
	 * @param dft_list1
	 * @param dft_list2
	 * @param distance
	 * @return
	 */
	public double getDistance(LinkedList<DFTData> dft_list1 , LinkedList<DFTData> dft_list2 , Distance distance){
		double[] hilb1 = new double[dft_list1.peek().hilb().length * dft_list1.size()];
		double[] hilb2 = new double[dft_list2.peek().hilb().length * dft_list2.size()];
		if(hilb1.length != hilb2.length){
			logger.info("The length of input dwt LinkedList is not equal.");
			return 0;
		}
		int datalength = dft_list1.size();
		int datasize = dft_list1.peek().hilb().length;
		for(int i = 0 ; i < datalength ; i++){
			for(int j = 0 ; j < datasize ; j++){
				hilb1[i*datasize+j] = dft_list1.get(i).hilb()[j]; 
				hilb2[i*datasize+j] = dft_list2.get(i).hilb()[j]; 
			}
		}
		return distance.calDistance(hilb1, hilb2);
	}

	/**
	 * This is a redundant function that calculate distances after restore DFT into full resolution
	 * @param ts1
	 * @param ts2
	 * @param distance
	 * @return
	 */
	public double getDistanceTest(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		TimeSeries dr1full = getFullResolutionDR(ts1);
		TimeSeries dr2full = getFullResolutionDR(ts2);
		double[][] dr1fullarray = DataListOperator.getInstance().linkedDataListToArray(dr1full);
		double[][] dr2fullarray = DataListOperator.getInstance().linkedDataListToArray(dr2full);
		return distance.calDistance(dr1fullarray[1], dr2fullarray[1]);
	}

}
