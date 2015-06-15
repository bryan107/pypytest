package experiment.utility;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.jtransforms.fft.DoubleFFT_1D;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.Wave;
import mfdr.math.trigonometric.Triangle;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import junit.framework.TestCase;

public class DFTTest extends TestCase {

	public void testWave() {
		int length = 2000;
		Wave w = new Wave(5, 0, 2);
		double[] value = new double[length];
		value = generateSeries(4, 0, 10, length);
		File.getInstance().saveArrayToFile("Original", value,
				"C:\\Programming\\TEST\\DDT.csv");
		DoubleFFT_1D fft = new DoubleFFT_1D(value.length);
		fft.realForward(value);

		LinkedList<Wave> wavelist = new LinkedList<Wave>();
		extractWaveList(value, wavelist);

		double[] real = new double[length];
		Iterator<Wave> it = wavelist.iterator();
		while (it.hasNext()) {
			Wave wave = it.next();
			for (int i = 0; i < length; i++) {
				real[i] += wave.getCosValue(i, length);
			}
		}

		File.getInstance().saveArrayToFile("Result", real,
				"C:\\Programming\\TEST\\DDT.csv");

	}

	private void extractWaveList(double[] value, LinkedList<Wave> wavelist) {
		Map<Integer, Double> cosmap = new HashMap<Integer, Double>();
		Map<Integer, Double> sinmap = new HashMap<Integer, Double>();
		for (int i = 0; i < value.length; i+=2) {
			if (value[i] > 0.00001)
				cosmap.put(i, value[i]);
		}
		for (int i = 1; i < value.length; i+=2) {
			if (value[i] > 0.00001)
				sinmap.put(i, value[i]);
		}
		Iterator<Integer> it = cosmap.keySet().iterator();
		while (it.hasNext()) {
			int index = it.next();
			double cos = cosmap.get(index);
			double sin = 0;
			if (sinmap.containsKey(index+1)) {
				sin = sinmap.get(index+1);
				sinmap.remove(index+1);
			}
			double phasedelay = Triangle.getInstance().getPhaseDelay(cos, sin);
			double energy = Triangle.getInstance().getAmplitude(cos, sin)
					/ (value.length / 2);
			double freq = index / 2;
			wavelist.add(new Wave(energy, phasedelay, freq));
		}
		it = sinmap.keySet().iterator();
		while (it.hasNext()) {
			int index = it.next();
			double sin = sinmap.get(index);
			double cos = 0;
			if (cosmap.containsKey(index-1)) {
				cos = cosmap.get(index-1);
				cosmap.remove(index-1);
			}
			double phasedelay = Triangle.getInstance().getPhaseDelay(cos, sin);
			double energy = Triangle.getInstance().getAmplitude(cos, sin)
					/ (value.length / 2);
			double freq = index / 2;
			wavelist.add(new Wave(energy, phasedelay, freq));
		}
	}

	public void testDFTlibrary() {
		// LinkedList<Double> tsvalues;
		// double shift1 = 128, shift2 = 640;
		// for (int i = 1; i < 2; i ++) {
		// tsvalues = generateTimeSeries(i, shift1, 10, 1024);
		// saveAndPrint(i, shift1, tsvalues);
		// tsvalues = generateTimeSeries(i, shift2, 10, 1024);
		// saveAndPrint(i, shift2, tsvalues);
		// }
		// //
		// tsvalues = generateTimeSeries(1.5, 0, 10, 1024);
		// saveAndPrint(1.5, 0, tsvalues);
		// tsvalues = generateTimeSeries(6, 0, 10, 1024);
		// saveAndPrint(6, 0, tsvalues);
		//
		//
		//
		//
		// double[] values = new double[1024];
		// DoubleFFT_1D fft = new DoubleFFT_1D(values.length);
		// for(int i = 510 ; i < 521 ; i++){
		// for(int j = 0 ; j < 1024 ; j++){
		// if(j == i){
		// values[j] = 5120;
		// } else{
		// values[j] = 0;
		// }
		// }
		// File.getInstance().saveArrayToFile(
		// "Freq[" + i + "]", values,
		// "C:\\TEST\\MDFR\\Data\\SmallExp\\ReverseDFT.csv");
		// fft.realInverse(values, true);
		// File.getInstance().saveArrayToFile(
		// "Reverse[" + i + "]", values,
		// "C:\\TEST\\MDFR\\Data\\SmallExp\\ReverseDFT.csv");
		// }
		//
		// // Math.pow(signallength/2, 0.5);
		// }
		//
		// public void saveAndPrint(double freq, double shift,
		// LinkedList<Double> tsvalues) {
		// DecimalFormat format = new DecimalFormat("0.00");
		// double[] valuearray = DataListOperator.getInstance()
		// .linkedDoubleListToArray(tsvalues);
		// double valueenergy = 0;
		// for (int i = 0; i < valuearray.length; i++) {
		// valueenergy += Math.pow(valuearray[i], 2);
		// }
		// File.getInstance().saveArrayToFile(
		// "Original[" + freq + "|" + shift + "]", valuearray,
		// "C:\\TEST\\MDFR\\Data\\SmallExp\\DFT.csv");
		//
		// DoubleFFT_1D fft = new DoubleFFT_1D(valuearray.length);
		// fft.realForward(valuearray);
		// File.getInstance().saveArrayToFile(
		// "Forward[" + freq + "|" + shift + "]", valuearray,
		// "C:\\TEST\\MDFR\\Data\\SmallExp\\DFT.csv");
		//
		// TEST Recovery
		// Map<Integer, Double> dftma = new HashMap<Integer, Double>();
		// for(int i = 0 ; i < valuearray.length ; i++){
		// if(valuearray[i]>0)
		// dftma.put(i, valuearray[i]);
		// }
		// Iterator<Integer> it = dftma.keySet().iterator();
		// while(it.hasNext()){
		// double cos=0,sin=0;
		// int index = it.next();
		// if(index % 2 == 0){
		// cos = dftma.get(index);
		// if(dftma.containsKey(index+1)){
		// sin = dftma.get(index+1);
		// dftma.remove(index+1);
		// }
		// } else{
		// sin = dftma.get(index);
		// if(dftma.containsKey(index-1)){
		// cos = dftma.get(index-1);
		// dftma.remove(index-1);
		// }
		// }
		// double delay = Triangle.getInstance().getPhaseDelay(cos, sin);
		// double energy = Triangle.getInstance().getEnergy(cos, sin);
		// }
		//
		//
		// // Energy
		// double freqenergy = 0;
		// for (int i = 0; i < valuearray.length; i++) {
		// freqenergy += Math.pow(valuearray[i], 2);
		// }
		// System.out.println("[" + freq + "|" + shift + "] E(Value):"
		// + format.format((valueenergy / valuearray.length)) + "  E(Freq):"
		// + format.format((freqenergy / valuearray.length)) + " E(Norm Freq):"
		// + format.format((freqenergy /
		// valuearray.length)/(valuearray.length/2)));
	}

	private LinkedList<Double> generateTimeSeries(double freq, double shift,
			double amplitide, long size) {
		LinkedList<Double> ts = new LinkedList<Double>();
		for (double i = 0; i < size; i += 1) {
			java.util.Random r = new java.util.Random();
			double noise = 0;
			noise = r.nextGaussian() * Math.sqrt(5);
			double trend = 1 * Math.pow(i, 0.5);
			if (i % 200 == 0) {
				trend = trend + r.nextGaussian() * Math.sqrt(50);
			}
			// double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 *
			// Math.cos(i*Math.PI / 6) + noise;
			double value = amplitide
					* Math.cos((freq * 2 * (i + shift) * Math.PI / size));
			ts.add(value);
		}
		return ts;
		// return 1/(2*Math.PI*3);
	}

	private double[] generateSeries(double freq, double shift,
			double amplitide, int size) {
		double[] ts = new double[size];
		for (int i = 0; i < size; i += 1) {
			java.util.Random r = new java.util.Random();
			double noise = 0;
			noise = r.nextGaussian() * Math.sqrt(5);
			double trend = 1 * Math.pow(i, 0.5);
			if (i % 200 == 0) {
				trend = trend + r.nextGaussian() * Math.sqrt(50);
			}
			double value = amplitide
					* Math.cos((freq * 2 * (i + shift) * Math.PI / size))
					+ amplitide
					* Math.cos((freq/2 * 2 * (i + 2) * Math.PI / size));
			;
			ts[i] = value;
		}
		return ts;
		// return 1/(2*Math.PI*3);
	}

}
