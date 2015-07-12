package mfdr.math.emd;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.core.TrendFilterForMFDRWave;
import mfdr.core.WhiteNoiseFilter;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.file.FileAccessAgent;
import mfdr.math.emd.BAK_IMFAnalysis;
import mfdr.math.emd.EMD;
import mfdr.math.emd.datastructure.IMF;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.math.emd.datastructure._BAK_IMFs;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import mfdr.utility.Print;
import mfdr.utility.StatTool;
import junit.framework.TestCase;

public class TestEMD extends TestCase {
	
	private static Log logger = LogFactory.getLog(TestEMD.class);
	private double zerocrossingaccuracy = 0.0001;
	private long datasize = 5000;  
	private double[] IFparamaters = {4,2,1}; 
	private final int MAXLEVEL = 10;
	DecimalFormat df = new DecimalFormat("0.0");
	
	
	// Parameters for Noise/Signal Analysis
	final double noise_whitenoiselevel = 5; // p-value 0.01
	final double noise_threshold = 6.2;

	// Parameters for Frequency/Trend Analysis

	final int motif_k = 2; 
	final double motif_threshold = 0.1;
	final double FTratio = 0.5;
	
	public void testGetIMFs(){
		TimeSeries residual = new TimeSeries();
		double realfre = generateResidual(residual, datasize);
//		residual = (TimeSeries) DataListOperator.getInstance().normalize(residual);
//		residual = normalise(residual);
		logger.info("RESIDUAL:");
		Print.getInstance().printDataLinkedList(residual, 100);
		logger.info("Energy: " + residual.energy());
//		IMFAnalysis residualanalysis = new IMFAnalysis(percentilesvalue, FTRatio, t_threshold);
//		residualanalysis.isSignal(residual);
		
		/*
		 *  EMD Operation
		 */
		// Create EMD service object
		EMD emd = new EMD(residual, zerocrossingaccuracy, IFparamaters[0], IFparamaters[1], IFparamaters[2]);
		// Calculate IMF with EMD
		IMFS imfs = emd.getIMFs(MAXLEVEL);
		IMFS imfs_test = new IMFS();
		for(int i = 0 ; i < 3 ; i++){
			imfs_test.add(imfs.get(i));
		}
		// Create IMF analysis object
//		BAK_IMFAnalysis analysis = new BAK_IMFAnalysis(residual, imfs, noise_whitenoiselevel, noise_threshold, FTratio, motif_k, motif_threshold);
//		WhiteNoiseFilter wfilter = new WhiteNoiseFilter(noise_whitenoiselevel, noise_threshold, FTratio);
//		TrendFilter tfilter = new TrendFilter(FTratio, motif_k, motif_threshold);
		
		
		double oimfd = 0;
		Iterator<IMF> it = imfs.iterator();
		while (it.hasNext()) {
			IMF imf = (IMF) it.next();
			oimfd += imf.normalizedEnergyDensity(residual.energyNormalizedFactor());
		}
		
		logger.info("ORIGINAL IMFS ENERGY DENSITY: " + oimfd);
		
		double imfd = 0;
		it = imfs.iterator();
		while (it.hasNext()) {
			IMF imf = (IMF) it.next();
			imfd += imf.energyDensity()/imfs.totalEnergyDensity();
		}
		
		logger.info("NEW IMFS ENERGY DENSITY: " + imfd);
		
		/*
		 *  Store Results
		 */
		// Save Time References
//		File.getInstance().saveTimeToFile(residual, "C:\\TEST\\MFDR\\_IMFTest_Norm.csv");
		
		// Save Original Signal
//		File.getInstance().saveLinkedListToFile(" " ,residual, "C:\\TEST\\MFDR\\_IMFTest_Norm.csv");
				
		// Save IMFs
//		for(int i = 0 ; i < imfs.size() ; i++){
//			File.getInstance().saveLinkedListToFile(" " ,imfs.get(i), "C:\\TEST\\MFDR\\_IMFTest_Norm.csv");
//		}
		
		// Save instant
//		File.getInstance().saveTimeToFile(residual, "C:\\TEST\\MDFR\\IMFTest_Norm_Freq.csv");
//		try {
//			for(int i = 0 ; i < imfs.size() ; i++){
//				System.out.print("IF[" + imfs.getIMF(i).instantFrequency().size() + "]: ");
//				File.getInstance().saveLinkedListToFile(imfs.getIMF(i).instantFrequency(), "C:\\TEST\\MDFR\\IMFTest_Norm_Freq.csv");
//			}
//			for(int i = 0 ; i < imfs.size() ; i++){
//				System.out.print("Full resolution IF[" + imfs.getIMF(i).instFreqFullResol(residual).size() + "]: ");
//				File.getInstance().saveLinkedListToFile(imfs.getIMF(i).instFreqFullResol(residual), "C:\\TEST\\MDFR\\IMFTest_Norm_Freq.csv");
//			}
//		} catch (Exception e) {
//		}
		
		// Save instant
//		File.getInstance().saveTimeToFile(residual, "C:\\TEST\\MDFR\\IMFTest_Norm_AutoCorr.csv");
//		try {
//			for(int i = 0 ; i < imfs.size() ; i++){
//				System.out.print("IF[" + StatTool.getInstance().autoCorrCoeff(imfs.get(i).instFreqFullResol(residual)) + "]: ");
//				File.getInstance().saveArrayToFile(StatTool.getInstance().autoCorr(imfs.get(i).instFreqFullResol(residual)), "C:\\TEST\\MDFR\\IMFTest_Norm_AutoCorr.csv");
//			}
//		} catch (Exception e) {
//		}
	
		
//		for (int i = 0; i < imfs.size(); i++) {
//			try {
//				System.out.println("IMF[" + i + "]: ");
//				System.out.print("IF[" + imfs.getIMF(i).instantFrequency().size() + "]: ");
//				Print.getInstance().printDataLinkedList(imfs.getIMF(i).instantFrequency(), 100);
//				System.out.print("Full resolution IF[" + imfs.getIMF(i).instFreqFullResol(residual).size() + "]: ");
//				Print.getInstance().printDataLinkedList(imfs.getIMF(i).instFreqFullResol(residual), 100);
//			} catch (Exception e) {
//			}
//
//		}
		
		
		/*
		 *  Print Results of each IMF
		 */
		
		for(int i = 0 ; i < imfs.size() ; i++){
			System.out.println();
			
			// Print IMF[i] values
			try {
				double average = average(imfs.get(i));
				System.out.println("IMF[" + i + "]  SIZE" + imfs.get(i).size() + " AVERAGE: " + average);
				Print.getInstance().printDataLinkedList(imfs.get(i), 100);
			} catch (Exception e) {
				System.out.println(e);
			}
			
			// Print IMF[i] frequency condition
//			System.out.println();
//			System.out.println("FrequencySIZE-" + imfs.get(i).instantFrequency().size());
//			try {
//				System.out.println("Current Signal Frequency:" + realfre);
//				System.out.println("IMF Average Frequency:" + imfs.get(i).averageFrequency());
//				System.out.println("Instant Frequency[" + i + "]" + "Error:" + calcL2Error(imfs.get(i).instantFrequency(), realfre/(i+1)));
//				Print.getInstance().printDataLinkedList(imfs.get(i).instantFrequency(), 100);
//			} catch (Exception e) {
//				System.out.println(e);
//			}
			
//			System.out.println();
//			// Print IMF[i] Noise condition
//			if(wfilter.isWhiteNoise(imfs.get(i))){
//				System.out.println("WHITENOISE");
//			}else{
//				System.out.println("SIGNAL");
//			}
//			
//			
//			//  Print IMF[i] Frequency/Trend condition
//			if(analysis.isFreq(imfs.get(i))){
//				System.out.println("FREQUENCY");
//			}else{
//				System.out.println("TREND");
//			}
		}
	}
	
	public void testGetInstantFrequency(){
		
	}
	
	private double generateResidual(LinkedList<Data> residual, long size) {
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = 0; 
			noise = r.nextGaussian() * Math.sqrt(5);
			double trend = 1*Math.pow(i, 0.5);
			if(i%200 == 0){
				trend = trend + r.nextGaussian() * Math.sqrt(50);
			}
//			double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 * Math.cos(i*Math.PI / 6)  + noise;
			double value = 9.5 * Math.sin(i*Math.PI / 64) + trend + noise;
			residual.add(new Data(i, value));
		}
		return (double)1/6;
//		return 1/(2*Math.PI*3);
	}
	
	private double calcL2Error(LinkedList<Data> data, double average){
		Iterator<Data> it = data.iterator();
		double sum = 0;
		while(it.hasNext()){
			sum += Math.pow(it.next().value() - average, 2);
		}
		return Math.pow(sum, 0.5);
	}
	
	private double average(LinkedList<Data> data){
		Iterator<Data> it = data.iterator();
		double sum = 0;
		while(it.hasNext()){
			sum += it.next().value();
		}
		return sum/data.size();
	}
}
