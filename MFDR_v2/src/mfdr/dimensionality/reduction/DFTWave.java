package mfdr.dimensionality.reduction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.jtransforms.fft.DoubleFFT_1D;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTWaveData;
import mfdr.distance.Distance;
import mfdr.math.trigonometric.Triangle;
import mfdr.utility.DataListOperator;
import mfdr.utility.ValueComparator;

public class DFTWave extends DimensionalityReduction {

	public DFTWave(int NoC) {
		setNoC(NoC);
	}

	@Override
	public LinkedList<DFTWaveData> getDR(TimeSeries ts) {
		return getDR(converTSToFrequency(ts));
	}

	/**
	 * This function get DFTData from hilb array
	 * 
	 * @param hilb
	 * @return
	 */
	public LinkedList<DFTWaveData> getDR(double[] hilb) {
		LinkedList<DFTWaveData> wavelist = new LinkedList<DFTWaveData>();
		Map<Integer, DFTWaveData> wavemap = new HashMap<Integer, DFTWaveData>();
		// Get Sorted Hilb
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		ValueComparator bvc = new ValueComparator(map);
		TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(bvc);
		for (int i = 0; i < hilb.length; i += 2) {
			double cos = hilb[i];
			double sin = hilb[i + 1];
			double phasedelay = Triangle.getInstance().getPhaseDelay(cos, sin);
			double amplitude = Triangle.getInstance().getAmplitude(cos, sin)
					/ (hilb.length / 2);
			double freq = i / 2;
			wavemap.put(i, new DFTWaveData(amplitude, phasedelay, freq));
			map.put(i, amplitude);
		}
		sorted_map.putAll(map);
		// Store results to DFTData Map

		Iterator<Integer> it = sorted_map.keySet().iterator();
		for (int i = 0; i < NoC; i++) {
			int index = it.next();
			wavelist.add(wavemap.get(index));
		}
		/* **************************************** */
		return wavelist;
	}

	public double[] converTSToFrequency(TimeSeries ts) {
		LinkedList<Double> tsvalues = DataListOperator.getInstance()
				.getValueList(ts);
		double[] valuearray = DataListOperator.getInstance()
				.linkedDoubleListToArray(tsvalues);

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
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		
		LinkedList<DFTWaveData> wavelist = getDR(ts);
		return getFullResolutionDR(wavelist, ts);
	}
	
	public TimeSeries getFullResolutionDR(LinkedList<DFTWaveData> wavelist, TimeSeries ref) {
		TimeSeries tsreduced = new TimeSeries();
		double[] real = new double[ref.size()];
		Iterator<DFTWaveData> it = wavelist.iterator();
		while (it.hasNext()) {
			DFTWaveData wave = it.next();
			for (int i = 0; i < ref.size(); i++) {
				real[i] += wave.getWaveValue(i, ref.size());
			}
		}
		for (int i = 0; i < ref.size(); i++) {
			tsreduced.add(new Data(ref.get(i).time(), real[i]));
		}
		return tsreduced;
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		LinkedList<DFTWaveData> wavelist1 = getDR(ts1);
		LinkedList<DFTWaveData> wavelist2 = getDR(ts2);
		return getDistance(wavelist1, wavelist2, distance, ts1.size());
	}

	public double getDistance(LinkedList<DFTWaveData> wavelist1 , LinkedList<DFTWaveData> wavelist2 , Distance distance, int signallength){
		Map<Double, DFTWaveData> dr1map = new HashMap<Double, DFTWaveData>();
		Map<Double, DFTWaveData> dr2map = new HashMap<Double, DFTWaveData>();
		// Save to maps
		Iterator<DFTWaveData> it = wavelist1.iterator();
		while (it.hasNext()) {
			DFTWaveData wave = it.next();
			dr1map.put(wave.freq(), wave);
		}
		it = wavelist2.iterator();
		while (it.hasNext()) {
			DFTWaveData wave = it.next();
			dr2map.put(wave.freq(), wave);
		}
		
		LinkedList<Double> list1 = new LinkedList<Double>();
		LinkedList<Double> list2 = new LinkedList<Double>();
		// Iterate through dr1map
				Iterator<Double> it2 = dr1map.keySet().iterator();
				while (it2.hasNext()) {
					double index = it2.next();
					list1.add(dr1map.get(index).getCosAmplitude()*(signallength/2));
					list1.add(dr1map.get(index).getSinAmplitude()*(signallength/2));
					if(dr2map.containsKey(index)){
						list2.add(dr2map.get(index).getCosAmplitude()*(signallength/2));
						list2.add(dr2map.get(index).getSinAmplitude()*(signallength/2));
						dr2map.remove(index);
					}else{
						list2.add(0.0);
						list2.add(0.0);
					}
				}
				
				// Iterate through dr2map
				it2 = dr2map.keySet().iterator();
				while (it2.hasNext()) {
					double index = it2.next();
					list2.add(dr2map.get(index).getCosAmplitude()*(signallength/2));
					list2.add(dr2map.get(index).getSinAmplitude()*(signallength/2));
					list1.add(0.0);
					list1.add(0.0);
				}
		
				double[][] drarray = new double[2][list1.size()];
				for (int i = 0; i < list1.size(); i++) {
				    drarray[0][i] = list1.get(i);
				    drarray[1][i] = list2.get(i);
				}
		return distance.calDistance(drarray[0], drarray[1])/Math.pow(signallength/2, 0.5);
	}
}
