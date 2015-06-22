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
import mfdr.dimensionality.datastructure.DFTWaveData;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DFTWave;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.math.trigonometric.Theta;
import mfdr.math.trigonometric.Triangle;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import mfdr.math.Sum;
import junit.framework.TestCase;

public class DFTTest extends TestCase {

	public void testPLA(){
		TimeSeries ts1 = generatePLASeries(0, 1, 256);
		TimeSeries ts2 = generatePLASeries(0, 0, 256);
		
		Distance dist = new EuclideanDistance();
		
		PLA pla = new PLA(2);
		double full = pla.getBruteForceDistance(ts1, ts2, dist);
		double cal = pla.getDistance(ts1, ts2, dist);
		
		System.out.println("FULL:" + full);
		System.out.println("REAL" + cal);
	}
	
	private TimeSeries generatePLASeries(double trendamplidute, double constant, int size) {
		TimeSeries ts = new TimeSeries();
		for (double i = 0; i < size; i += 1) {
			double value = trendamplidute * i + constant;
			ts.add(new Data(i, value));
		}
		return ts;
		// return 1/(2*Math.PI*3);
	}
	
	public void testAll(){
//		TimeSeries ts1 = generateTimeSeries(1, 8, 0, 5, 256);
//		TimeSeries ts2 = generateTimeSeries(2, 4, 0, 10, 256);
//		File.getInstance().saveLinkedListToFile("TS_1", ts1 , "C:\\Programming\\TEST\\___FINAL.csv");
//		File.getInstance().saveLinkedListToFile("TS_2", ts2 , "C:\\Programming\\TEST\\___FINAL.csv");
//
//		
//		Distance distance = new EuclideanDistance();
//		
//		MFDRWave mfdr = new MFDRWave(2, 2);
//		MFDRWaveData data1 = mfdr.getDR(ts1);
//		MFDRWaveData data2 = mfdr.getDR(ts2);
//		DFT dft = new DFT(2);
//		DFTWave dftwave = new DFTWave(2);
//		PLA pla = new PLA(2);
//
//		File.getInstance().saveLinkedListToFile("MFDR_1", mfdr.getFullResolutionDR(ts1) , "C:\\Programming\\TEST\\___FINAL.csv");
//		File.getInstance().saveLinkedListToFile("MFDR_2", mfdr.getFullResolutionDR(ts2) , "C:\\Programming\\TEST\\___FINAL.csv");
//		
//		
//		
//		double dist_o = mfdr.getDistanceBruteForce(data1, data2, ts1, distance);
//		double dist_of = mfdr.getCrossBruteForceDistance(data1, data2, ts1, distance);
//		double dist_f = mfdr.getDistance(data1, data2, ts1, distance);
//		
//		
//		System.out.println("DIST:" + distance.calDistance(ts1, ts2, ts1));
//		System.out.println("DIST_Brute_Force:" + dist_o);
//		System.out.println("DIST_Cross_Brute_Froce:" + dist_of);
//		System.out.println("DIST_MFDR:" + dist_f);
//		
//		double dist_dft = dft.getDistance(ts1, ts2, distance);
//		double dist_pla = pla.getDistance(ts1, ts2, distance);
//		double dist_dftwave = dftwave.getDistance(ts1, ts2, distance);
//		System.out.println();
//		
//		TimeSeries wave1 = dftwave.getFullResolutionDR(ts1);
//		TimeSeries wave2 = dftwave.getFullResolutionDR(ts2);
//		
//		System.out.println();
//		System.out.println("DIST_PLA:" + dist_pla);
//		System.out.println("DIST_DFT:" + dist_dft);
//		System.out.println("DIST_DFTWave:" + dist_dftwave);
////		System.out.println("DIST_DFTWaveFull:" + distance.calDistance(wave1, wave2, wave1));
//		System.out.println("All Done");
	}
	
	public void testTest() {
//		double realvalue = getCellValue(2, 1, 10, 5, 128);
//		System.out.println("Real Cell Value:" + realvalue);
//		double fastvalue = getFaseCellValue(2, 1, 10, 5, 128);
//		System.out.println("Fast Cell Value:" + fastvalue);
		
	}

	private double getFaseCellValue(double a3, double b3, double c1, double c2,
			int windowsize) {
		double sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
		Theta theta1 = new Theta(4, 0, 128, 1, windowsize);
		Theta theta2 = new Theta(4, 0, 128, 1, windowsize);
		sum1 = a3 * c1
				* Sum.getInstance().xCos(theta1.g(), theta1.k(), windowsize)
				+ a3 * c1
				* Sum.getInstance().cos(theta1.g(), theta1.k(), windowsize);
		sum2 = b3 * c1
				* Sum.getInstance().cos(theta1.g(), theta1.k(), windowsize);
//		sum3 = a3 * c2
//				* Sum.getInstance().xCos(theta2.g(), theta2.k(), windowsize)
//				+ a3 * c2
//				* Sum.getInstance().cos(theta2.g(), theta2.k(), windowsize);
//		sum4 = b3 * c2
//				* Sum.getInstance().cos(theta2.g(), theta2.k(), windowsize);
		System.out.println("S1:" + sum1);
		System.out.println("S2:" + sum2);
		System.out.println("S3:" + sum3);
		System.out.println("S4:" + sum4);
		return sum1 + sum2 - sum3 - sum4;
	}

	private double getCellValue(double a3, double b3, double c1, double c2,
			int windowsize) {
		double sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
		Theta theta1 = new Theta(4, 0, 128, 1, windowsize);
		Theta theta2 = new Theta(4, 0, 128, 1, windowsize);
		for (int x = 0; x <= windowsize; x++) {
			sum1 += a3 * (x + 1) * c1 * Math.cos(theta1.getAngle(x));
			sum2 += b3 * c1 * Math.cos(theta1.getAngle(x));
			sum3 += a3 * (x + 1) * c2 * Math.cos(theta2.getAngle(x));
			sum4 += b3 * c2 * Math.cos(theta2.getAngle(x));
		}

		System.out.println("S1:" + sum1);
		System.out.println("S2:" + sum2);
		System.out.println("S3:" + sum3);
		System.out.println("S4:" + sum4);
		return sum1 + sum2 - sum3 - sum4;
	}

	public void testNew() {
//		double freq = 4, length = 128;
//		// double g = 2 * Math.PI * freq / length;
//		double g = 1;
//		int n = 3;
//		double sum = 0;
//		TimeSeries list = new TimeSeries();
//
//		for (int k = 1; k <= n; k++) {
//			sum += (k - 1) * Math.cos(g * (k - 1));
//			list.add(new Data(k, (k - 1) * Math.cos(g * (k - 1))));
//		}
//		// ------------------------------
//		// File.getInstance().saveLinkedListToFile("Original", list ,
//		// "C:\\TEST\\MDFR\\Data\\GGG.csv");
//
//		System.out.println("REAL:" + sum);
//		sum = (1 / Math.sin(g / 2))
//				* (n / 2 - Math.sin(g * n / 2) / Math.sin(g / 2))
//				* Math.sin(g * (n - 1));
//		System.out.println("GG:" + sum);
	}

	// public void testMFDRWave() {
	// int i = 1;
	// double a = 2, cc = 10;
	// double freq = 4, length = 64, phasedelay = Math.PI / 2;
	// double g = 2 * Math.PI * freq / length;
	// double c = 1, d = length;
	// double sum = 0;
	// for (int x = 0; x < 128; x++) {
	// sum += x * Math.cos(g * x + phasedelay);
	// }
	// double result = sum;
	// System.out.println("Full_1:" + result);
	//
	// sum = 0;
	// for (int x = 0; x < 128; x++) {
	// sum += x
	// * (Math.cos(g * x) * Math.cos(phasedelay) - Math.sin(g * x)
	// * Math.sin(phasedelay));
	// }
	// result = sum;
	// System.out.println("Full_2:" + result);
	//
	// sum = 0;
	// for (int x = 0; x < 128; x++) {
	// sum += x
	// * (Math.cos(g * x) * Math.cos(phasedelay) - Math.sin(g * x)
	// * Math.sin(phasedelay));
	// }
	// result = sum;
	// System.out.println("Full_3:" + result);
	//
	// sum = 0;
	// for (int x = 0; x < 128; x++) {
	// sum += Math.cos(phasedelay) * x * Math.cos(g * x)
	// - Math.sin(phasedelay) * Math.sin(g * x) * x;
	// }
	// result = sum;
	// System.out.println("Full_4:" + result);
	//
	// sum = 0;
	// for (int x = 0; x < 128; x++) {
	// sum += Math.cos(phasedelay) * x * Math.cos(g * x)
	// - Math.sin(phasedelay) * Math.sin(g * x) * x;
	// }
	// result = sum;
	// System.out.println("Full_4:" + result);
	//
	// // ---------
	// double part1_1 = Math.cos(phasedelay)
	// * (Math.cos(g * d) - Math.cos(g * c) + g * d * Math.sin(g * d) - g
	// * c * Math.sin(g * c)) / (Math.pow(g, 2));
	// double part1_2 = Math.sin(phasedelay)
	// * (Math.sin(g * d) - Math.sin(g * c) + g * d * Math.cos(g * d) - g
	// * c * Math.cos(g * c)) / (Math.pow(g, 2));
	// double part2 = length
	// * (i - 1)
	// * (Math.cos(phasedelay) * (Math.sin(g * d) - Math.sin(g * c)) + Math
	// .sin(phasedelay) * (Math.cos(g * d) - Math.cos(g * c)))
	// / g;
	// double resultmfdr = part1_1 - part1_2 + part2;
	// System.out.println("MFDR:" + resultmfdr);
	// System.out.println("P1_1:" + part1_1);
	// System.out.println("P1_2:" + part1_2);
	// System.out.println("P2:" + part2);
	// }

	public void testWave() {
		// int length = 2000;
		// Distance d= new EuclideanDistance();
		// // Wave w = new Wave(5, 0, 2);
		// TimeSeries[] ts = new TimeSeries[2];
		// ts[0] = generateTimeSeries(4, 0, 10, length);
		// ts[1] = generateTimeSeries(8, Math.PI, 10, length);
		// File.getInstance().saveLinkedListToFile("Original_1", ts[0],
		// "C:\\TEST\\MDFR\\Data\\DDT.csv");
		// File.getInstance().saveLinkedListToFile("Original_2", ts[1],
		// "C:\\TEST\\MDFR\\Data\\DDT.csv");
		// System.out.println("Original Distance:" + d.calDistance(ts[0], ts[1],
		// ts[0]));
		// // DFT
		// DFT dft = new DFT(6);
		// TimeSeries dftfull1 = dft.getFullResolutionDR(ts[0]);
		// TimeSeries dftfull2 = dft.getFullResolutionDR(ts[1]);
		// File.getInstance().saveLinkedListToFile("DFT_1", dftfull1,
		// "C:\\TEST\\MDFR\\Data\\DDT.csv");
		// File.getInstance().saveLinkedListToFile("DFT_2", dftfull2 ,
		// "C:\\TEST\\MDFR\\Data\\DDT.csv");
		// System.out.println("DFT Distance:" + dft.getDistance(ts[0], ts[1],
		// d));
		//
		// // DFT Wave
		// DFTWave dftwave = new DFTWave(6);
		// TimeSeries dftwavefull1 = dft.getFullResolutionDR(ts[0]);
		// TimeSeries dftwavefull2 = dft.getFullResolutionDR(ts[1]);
		// File.getInstance().saveLinkedListToFile("DFTWave_1", dftwavefull1,
		// "C:\\TEST\\MDFR\\Data\\DDT.csv");
		// File.getInstance().saveLinkedListToFile("DFTWave_2", dftwavefull2 ,
		// "C:\\TEST\\MDFR\\Data\\DDT.csv");
		// System.out.println("DFTWave Distance:" + dftwave.getDistance(ts[0],
		// ts[1], new EuclideanDistance()));

		// ----------------------------------------

		// LinkedList<Wave> wavelist = new LinkedList<Wave>();
		// extractWaveList(value[0], wavelist);
		//
		// double[] real = new double[length];
		// Iterator<Wave> it = wavelist.iterator();
		// while (it.hasNext()) {
		// Wave wave = it.next();
		// for (int i = 0; i < length; i++) {
		// real[i] += wave.getCosValue(i, length);
		// }
		// }

		// File.getInstance().saveArrayToFile("Result", real,
		// "C:\\Programming\\TEST\\DDT.csv");

	}

	private void extractWaveList(double[] value, LinkedList<DFTWaveData> wavelist) {
		Map<Integer, Double> cosmap = new HashMap<Integer, Double>();
		Map<Integer, Double> sinmap = new HashMap<Integer, Double>();
		for (int i = 0; i < value.length; i += 2) {
			if (value[i] > 0.00001)
				cosmap.put(i, value[i]);
		}
		for (int i = 1; i < value.length; i += 2) {
			if (value[i] > 0.00001)
				sinmap.put(i, value[i]);
		}
		Iterator<Integer> it = cosmap.keySet().iterator();
		while (it.hasNext()) {
			int index = it.next();
			double cos = cosmap.get(index);
			double sin = 0;
			if (sinmap.containsKey(index + 1)) {
				sin = sinmap.get(index + 1);
				sinmap.remove(index + 1);
			}
			double phasedelay = Triangle.getInstance().getPhaseDelay(cos, sin);
			double energy = Triangle.getInstance().getAmplitude(cos, sin)
					/ (value.length / 2);
			double freq = index / 2;
			wavelist.add(new DFTWaveData(energy, phasedelay, freq));
		}
		it = sinmap.keySet().iterator();
		while (it.hasNext()) {
			int index = it.next();
			double sin = sinmap.get(index);
			double cos = 0;
			if (cosmap.containsKey(index - 1)) {
				cos = cosmap.get(index - 1);
				cosmap.remove(index - 1);
			}
			double phasedelay = Triangle.getInstance().getPhaseDelay(cos, sin);
			double energy = Triangle.getInstance().getAmplitude(cos, sin)
					/ (value.length / 2);
			double freq = index / 2;
			wavelist.add(new DFTWaveData(energy, phasedelay, freq));
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

	private TimeSeries generateTimeSeries(double trendamplidute, double freq, double shift,
			double amplitide, long size) {
		TimeSeries ts = new TimeSeries();
		for (double i = 0; i < size; i += 1) {
			java.util.Random r = new java.util.Random();
			double noise = 0;
//			noise = r.nextGaussian() * Math.sqrt(5);
			double trend = trendamplidute * Math.pow(i, 0.5);
//			if (i != 0 && i % (size/2) == 0) {
//				trend = trend + r.nextGaussian() * Math.sqrt(50);
//			}
			// double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 *
			// Math.cos(i*Math.PI / 6) + noise;
			double value = trend + amplitide
					* Math.cos((freq * 2 * i * Math.PI / size + shift)) + noise;
			ts.add(new Data(i, value));
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
//			noise = r.nextGaussian() * Math.sqrt(5);
			double trend = 10 * Math.pow(i, 0.5);
			if (i % 200 == 0) {
				trend = trend + r.nextGaussian() * Math.sqrt(50);
			}
			double value = amplitide
					* Math.cos((freq * 2 * i * Math.PI / size + shift))
					+ amplitide * Math.cos((freq / 2 * 2 * i * Math.PI / size));
			;
			ts[i] = value;
		}
		return ts;
		// return 1/(2*Math.PI*3);
	}

}
