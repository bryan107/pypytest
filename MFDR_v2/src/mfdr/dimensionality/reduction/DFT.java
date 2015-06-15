package mfdr.dimensionality.reduction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jtransforms.fft.DoubleFFT_1D;

import math.jwave.Transform;
import math.jwave.transforms.AncientEgyptianDecomposition;
import math.jwave.transforms.DiscreteFourierTransform;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTDataOld;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.distance.Distance;
import mfdr.utility.DataListOperator;
import mfdr.utility.ValueComparator;

public class DFT extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(DFT.class);
	public DFT(int NoC) {
		setNoC(NoC);
	}


	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		DFTData dft = getDR(ts);
		return getFullResolutionDR(dft, ts);
	}

	public TimeSeries getFullResolutionDR(DFTData dft, TimeSeries ref) {
		TimeSeries drfull = new TimeSeries();
		double[] value = recoverFullResolutionFrequency(dft, ref.size());
		DoubleFFT_1D fft = new DoubleFFT_1D(ref.size());
		fft.realInverse(value, true);
		for(int i = 0 ; i < ref.size() ; i++){
			drfull.add(new Data(ref.get(i).time(), value[i]));
		}
		return drfull;
	}
	
	private double[] recoverFullResolutionFrequency(DFTData dft, int length){
		double[] output = new double[length];
		for(int i = 0 ; i < length ; i++){
			if(dft.getMap().containsKey(i)){
				output[i] = dft.getMap().get(i);
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
		return getDR(converTSToFrequency(ts));
	}
	
	/**
	 * This function get DFTData from hilb array
	 * @param hilb
	 * @return
	 */
	public DFTData getDR(double[] hilb){
		DFTData dftdata = new DFTData();
		// Get Sorted Hilb
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(bvc);
		for(int i = 0 ; i < hilb.length ; i++){
			map.put(i, Math.abs(hilb[i]));
		}
		sorted_map.putAll(map);
		// Store results to DFTData Map
		Iterator<Integer> it = sorted_map.keySet().iterator();
		for(int j = 0 ; j < NoC ; j++){
			int index = it.next();
			dftdata.getMap().put(index, hilb[index]);
		}
		/* *****************************************/ 
		return dftdata;
	}
	
	public double[] converTSToFrequency(TimeSeries ts){
		LinkedList<Double> tsvalues = DataListOperator.getInstance().getValueList(ts);
		double[] valuearray = DataListOperator.getInstance().linkedDoubleListToArray(tsvalues);
		
		// Conver value array into frequency domain
		DoubleFFT_1D fft = new DoubleFFT_1D(valuearray.length);
		fft.realForward(valuearray);
		return valuearray;
	}

	public double[] extractHighFrequency(double[] input, double lowestperiod, double timeinterval){
		int normalisedperiod = (int)(lowestperiod/timeinterval);
		int keeplength = 1 + (input.length-1)*2/normalisedperiod;
		double[] noisearray = new double[input.length-keeplength];
		for(int i = keeplength ; i < input.length ; i++){
			noisearray[i-keeplength] = input[i];
			input[i] = 0;
		}
		return noisearray;
	}
	
	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		DFTData dr1 = getDR(ts1);
		DFTData dr2 = getDR(ts2);
		return getDistance(dr1, dr2, distance,ts1.size());
	}
	
	public double getDistance(DFTData dr1, DFTData dr2, Distance distance, int signallength){
		// Clone Map
		Map<Integer, Double> dr1map = new HashMap<Integer, Double>(dr1.getMap());
		Map<Integer, Double> dr2map = new HashMap<Integer, Double>(dr2.getMap());
		
		LinkedList<Double> list1 = new LinkedList<Double>();
		LinkedList<Double> list2 = new LinkedList<Double>();
		
		// Iterate through dr1map
		Iterator<Integer> it = dr1map.keySet().iterator();
		while (it.hasNext()) {
			int index = it.next();
			list1.add(dr1map.get(index));
			if(dr2map.containsKey(index)){
				list2.add(dr2map.remove(index));
			}else{
				list2.add(0.0);
			}
		}
		
		// Iterate through dr2map
		it = dr2map.keySet().iterator();
		while (it.hasNext()) {
			int index = (int) it.next();
			list2.add(dr2map.get(index));
			list1.add(0.0);
		}
		
		double[][] drarray = new double[2][list1.size()];
		for (int i = 0; i < list1.size(); i++) {
		    drarray[0][i] = list1.get(i);
		    drarray[1][i] = list2.get(i);
		}
		return distance.calDistance(drarray[0], drarray[1])/Math.pow(signallength/2, 0.5);
	}
	
	/**
	 * This function calculate the distance between two dwt lists.
	 * 
	 * @param dft_list1
	 * @param dft_list2
	 * @param distance
	 * @return
	 */
	public double getDistance(LinkedList<DFTDataOld> dft_list1 , LinkedList<DFTDataOld> dft_list2 , Distance distance){
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
