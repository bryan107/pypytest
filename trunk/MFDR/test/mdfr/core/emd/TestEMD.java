package mdfr.core.emd;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.core.IMFAnalysis;
import mdfr.math.emd.DataListOperator;
import mdfr.math.emd.EMD;
import mdfr.math.emd.datastructure.Data;
import mdfr.math.emd.datastructure.IMFs;
import mdfr.utility.File;
import mdfr.utility.Print;
import mdfr.utility.StatTool;
import mdr.file.FileAccessAgent;
import junit.framework.TestCase;

public class TestEMD extends TestCase {
	
	private static Log logger = LogFactory.getLog(TestEMD.class);
	private double zerocrossingaccuracy = 0.0001;
	private long datasize = 100;  
	private double[] IFparamaters = {4,2,1}; 
	private final int MAXLEVEL = 10;
	private double percentilesvalue = 5; // p-value 0.01
	private double FTRatio = 0.50; 
	private double t_threshold = 6.2;
	DecimalFormat df = new DecimalFormat("0.0");
	
	public void testGetIMFs(){
		LinkedList<Data> residual = new LinkedList<Data>();
		double realfre = generateResidual(residual, datasize);
		residual = DataListOperator.getInstance().normalize(residual);
//		residual = normalise(residual);
		logger.info("RESIDUAL:");
		Print.getInstance().printDataLinkedList(residual, 100);
//		IMFAnalysis residualanalysis = new IMFAnalysis(percentilesvalue, FTRatio, t_threshold);
//		residualanalysis.isSignal(residual);
		
		/*
		 *  EMD Operation
		 */
		// Create EMD service object
		EMD emd = new EMD(residual, zerocrossingaccuracy, IFparamaters[0], IFparamaters[1], IFparamaters[2]);
		// Create IMF analysis object
		IMFAnalysis analysis = new IMFAnalysis(percentilesvalue, FTRatio, t_threshold);
		// Calculate IMF with EMD
		IMFs imfs = emd.getIMFs(MAXLEVEL);
		
		/*
		 *  Store Results
		 */
		// Save Time References
//		File.getInstance().saveTimeToFile(residual, "C:\\TEST\\MDFR\\IMFTest_Norm.csv");
		
		// Save Original Signal
//		File.getInstance().saveLinkedListToFile(residual, "C:\\TEST\\MDFR\\IMFTest_Norm.csv");
				
		// Save IMFs
//		for(int i = 0 ; i < imfs.size() ; i++){
//			File.getInstance().saveLinkedListToFile(imfs.getIMF(i).getDataList(), "C:\\TEST\\MDFR\\IMFTest_Norm.csv");
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
//			// TODO: handle exception
//		}
		
		// Save instant
		File.getInstance().saveTimeToFile(residual, "C:\\TEST\\MDFR\\IMFTest_Norm_AutoCorr.csv");
		try {
			for(int i = 0 ; i < imfs.size() ; i++){
				System.out.print("IF[" + StatTool.getInstance().autoCorrCoeff(imfs.getIMF(i).instFreqFullResol(residual)) + "]: ");
				File.getInstance().saveArrayToFile(StatTool.getInstance().autoCorr(imfs.getIMF(i).instFreqFullResol(residual)), "C:\\TEST\\MDFR\\IMFTest_Norm_AutoCorr.csv");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		
//		for (int i = 0; i < imfs.size(); i++) {
//			try {
//				System.out.println("IMF[" + i + "]: ");
//				System.out.print("IF[" + imfs.getIMF(i).instantFrequency().size() + "]: ");
//				Print.getInstance().printDataLinkedList(imfs.getIMF(i).instantFrequency(), 100);
//				System.out.print("Full resolution IF[" + imfs.getIMF(i).instFreqFullResol(residual).size() + "]: ");
//				Print.getInstance().printDataLinkedList(imfs.getIMF(i).instFreqFullResol(residual), 100);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//
//		}
		
		
		/*
		 *  Print Results
		 */
//		for(int i = 0 ; i < imfs.size() ; i++){
//			System.out.println();
//			System.out.println("IMF[" + i + "]: DataSize-" + imfs.getIMF(i).size() + " FrequencySIZE-" + imfs.getIMF(i).instantFrequency().size());
//			if(analysis.isWhiteNoise(imfs.getIMF(i))){
//				System.out.println("WHITENOISE");
//			}else{
//				System.out.println("SIGNAL");
//			}
////		    Print IMF[i]
//			try {
//				double average = average(imfs.getIMF(i).getDataList());
//				System.out.println("IMF[" + i + "]:" + average);
//				Print.getInstance().printDataLinkedList(imfs.getIMF(i).getDataList(), 100);
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//			try {
//				System.out.println("Current Signal Frequency:" + realfre);
//				System.out.println("IMF Average Frequency:" + imfs.getIMF(i).averageFrequency());
//				System.out.println("Instant Frequency[" + i + "]" + "Error:" + calcL2Error(imfs.getIMF(i).instantFrequency(), realfre/(i+1)));
////				Print.getInstance().printDataLinkedList(IFs.get(i).getFrequency());
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//
//		}
	}
	
	public void testGetInstantFrequency(){
		
	}
	
	private double generateResidual(LinkedList<Data> residual, long size) {
		for (double i = 0; i < size; i+=0.2) {
			java.util.Random r = new java.util.Random();
			double noise = 0; 
			noise = r.nextGaussian() * Math.sqrt(5);
//			double value = noise;
			double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 * Math.cos(i*Math.PI / 6)  + noise + 10*Math.pow(i, 0.5);
			
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
