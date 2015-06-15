package mfdr.dimensionality.reduction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.jtransforms.fft.DoubleFFT_1D;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.datastructure.Wave;
import mfdr.distance.Distance;
import mfdr.math.trigonometric.Triangle;
import mfdr.utility.DataListOperator;
import mfdr.utility.ValueComparator;

public class DFTWave extends DimensionalityReduction {

	public DFTWave(int NoC){
		setNoC(NoC);
	}
	
	@Override
	public LinkedList<Wave> getDR(TimeSeries ts) {
		return getDR(converTSToFrequency(ts));
	}
	
	/**
	 * This function get DFTData from hilb array
	 * @param hilb
	 * @return
	 */
	public LinkedList<Wave> getDR(double[] hilb){
		LinkedList<Wave> wavelist = new LinkedList<Wave>();
		Map<Integer, Wave> wavemap = new HashMap<Integer, Wave>();
		// Get Sorted Hilb
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(bvc);
		for(int i = 0 ; i < hilb.length ; i+=2){
			double cos = hilb[i];
			double sin = hilb[i+1];
			double phasedelay = Triangle.getInstance().getPhaseDelay(cos, sin);
			double amplitude = Triangle.getInstance().getAmplitude(cos, sin)
					/ (hilb.length / 2);
			double freq = i / 2;
			wavemap.put(i, new Wave(amplitude, phasedelay, freq));
			map.put(i, amplitude);
		}
		sorted_map.putAll(map);
		// Store results to DFTData Map
		
		Iterator<Integer> it = sorted_map.keySet().iterator();
		for(int i = 0 ; i < NoC ; i++){
			int index = it.next();
			wavelist.add(wavemap.get(index));
		}
		/* *****************************************/ 
		return wavelist;
	}
	
	private double[] converTSToFrequency(TimeSeries ts){
		LinkedList<Double> tsvalues = DataListOperator.getInstance().getValueList(ts);
		double[] valuearray = DataListOperator.getInstance().linkedDoubleListToArray(tsvalues);
		
		// Conver value array into frequency domain
		DoubleFFT_1D fft = new DoubleFFT_1D(valuearray.length);
		fft.realForward(valuearray);
		return valuearray;
	}
	
	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries tsreduced = new TimeSeries();
		LinkedList<Wave> wavelist = getDR(ts);
		double[] real = new double[ts.size()];
		Iterator<Wave> it = wavelist.iterator();
		while (it.hasNext()) {
			Wave wave = it.next();
			for (int i = 0; i < ts.size(); i++) {
				real[i] += wave.getCosValue(i, ts.size());
			}
		}
		for(int i = 0 ; i < ts.size() ; i++){
			ts.add(new Data(ts.get(i).time(), real[i]));
		}
		
		return tsreduced;
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		LinkedList<Wave> wavelist1 = getDR(ts1);
		LinkedList<Wave> wavelist2 = getDR(ts2);
		return getDistance(wavelist1, wavelist2, distance);
	}
	
	public double getDistance(LinkedList<Wave> wavelist1 , LinkedList<Wave> wavelist2 , Distance distance){
		Map<Double, Wave> map1 = new HashMap<Double, Wave>();
		Map<Double, Wave> map2 = new HashMap<Double, Wave>();
		// Save to maps
		Iterator<Wave> it = wavelist1.iterator();
		while (it.hasNext()) {
			Wave wave = it.next();
			map1.put(wave.freq(), wave);
		}
		it = wavelist2.iterator();
		while (it.hasNext()) {
			Wave wave = it.next();
			map2.put(wave.freq(), wave);
		}
		
		double total = 0;
		Iterator<Double> it2 = map1.keySet().iterator();
		while (it2.hasNext()) {
			double freq = it2.next();
			if(map2.containsKey(freq)){
				total += Math.pow(map1.get(freq).energy() - map2.get(freq).energy(), 2);
				map2.remove(freq);
			} else{
				total += Math.pow(map1.get(freq).energy(), 2);
			}
		}
		it2 = map2.keySet().iterator();
		while (it2.hasNext()) {
			double freq = it2.next();
			total += Math.pow(map2.get(freq).energy(), 2);
		}
		return Math.sqrt(total);
	}

}
