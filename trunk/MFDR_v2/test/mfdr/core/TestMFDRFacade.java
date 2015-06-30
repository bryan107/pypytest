package mfdr.core;

import java.text.DecimalFormat;
import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.MFDRData;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DFTWave;
import mfdr.dimensionality.reduction.DWT;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.dimensionality.reduction.PAA;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.file.FileAccessAgent;
import mfdr.learning.LR4DLearning;
import mfdr.learning.LinearLearning;
import mfdr.learning.LinearLearningResults;
import mfdr.learning.VarienceLearning;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import junit.framework.TestCase;

public class TestMFDRFacade extends TestCase {
	private final String[] writeaddress = { "C:\\TEST\\MDFR\\Data\\FacadeExample2.csv",
											"C:\\TEST\\MDFR\\Data\\OOO.csv" };
	private double white_noise_level = 3;
	private double white_noise_threshold = 6.2;
	private double min_NSratio = 0.5;
	private double tolerancevarience = 3;
	private Distance dist = new EuclideanDistance();
	// ************** Used variables **************
	private MFDRParameterFacade facade = new MFDRParameterFacade(white_noise_level, white_noise_threshold, min_NSratio, dist);
	private LinkedList<TimeSeries> ts = new LinkedList<TimeSeries>();
	private MFDRWave mfdr;
	private final int datanum = 10;
	private final int NoC = 4;
	// **************** Test Cases ****************
	
	DecimalFormat format = new DecimalFormat("0.0000");
	public void testLearnWindowSize() {
		LinkedList<TimeSeries> tslist = File.getInstance().readreadTimeSeriesFromFile("C:\\TEST\\MDFR\\Data\\DATA\\ECG200.csv", datanum );
		MFDRParameters p = facade.learnMFDRParameters(tslist, NoC, false);
		MFDRWave mfdr = new MFDRWave(p.trendNoC(), p.seasonalNoC());
		LinkedList<MFDRWaveData> mfdrdata = new LinkedList<MFDRWaveData>();
		for(int i = 0 ; i < datanum ; i++){
			mfdrdata.add(mfdr.getDR(tslist.get(i)));
		}
		PLA pla = new PLA(NoC);
		DFT dft = new DFT(NoC);
		
		LearningResults dist_parameters = facade.learnParameters(tslist, mfdr, tolerancevarience, dist);
		
		double error = 0;
		int count = 0;
		for(int i = 0 ; i < datanum -1 ; i++){
			for(int j = i+1 ; j < datanum ; j++){
				double originaldist = dist.calDistance(tslist.get(i), tslist.get(j), tslist.get(i));
//				originaldists.add(originaldist);
				double mfdrdist = mfdr.getDistance(mfdrdata.get(i), mfdrdata.get(j), tslist.get(i), dist);
				System.out.println("N["+ i +"] and N[" + j + "]-" 
				+ "  Original:" + format.format(originaldist) 
				+ "  MFDR:" + format.format(mfdrdist));
				count++;
			}
			
		}
		
		System.out.println("Average Error:" + (error/count));
		for(int i = 2 ; i <9 ; i+=2 ){
			test(i, "C:\\TEST\\MDFR\\Data\\GOOD.csv");
		}
	}
	
	private void test(int NoC, String readingaddress){
		TimeSeries ts1 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readingaddress);
		
		MFDRParameters p = facade.learnMFDRParameters(ts1, NoC, false);
		System.out.println("NoC_t:" + p.trendNoC() + "  NoC_s:" + p.seasonalNoC() + "  Noise Period:" + p.lowestPeriod());
		mfdr = new MFDRWave(p.trendNoC(), p.seasonalNoC());
		TimeSeries mfdrfull = mfdr.getFullResolutionDR(ts1);
		MFDRWaveData data = mfdr.getDR(ts1);
		PLA pla = new PLA(p.trendNoC());
		DFTWave dft = new DFTWave(p.seasonalNoC());
		TimeSeries trendfull = pla.getFullResolutionDR(data.trends(), ts1);
		TimeSeries seasonalfull = dft.getFullResolutionDR(data.seasonal(), ts1);
		TimeSeries noisefull = mfdr.getFullResolutionNoise(data.noiseEnergyDensity(), ts1);
		TimeSeries mfdrfullnonoise = DataListOperator.getInstance().linkedListSum(trendfull, seasonalfull);
		File.getInstance().saveTimeToFile(ts1, writeaddress[0]);
		File.getInstance().saveLinkedListToFile("Original", ts1, writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDRFull", mfdrfull, writeaddress[0]);
		File.getInstance().saveLinkedListToFile("MFDRFullNoNoise", mfdrfullnonoise, writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("Trend", trendfull, writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("Seasonal", seasonalfull, writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("Nosie", noisefull, writeaddress[0]);
		
		pla = new PLA(NoC);
		dft = new DFTWave(NoC);
		PAA paa = new PAA(NoC);
		TimeSeries trenddd = pla.getFullResolutionDR(ts1);
		TimeSeries seasonaldd = dft.getFullResolutionDR(ts1);
		TimeSeries paadd = paa.getFullResolutionDR(ts1);
		File.getInstance().saveLinkedListToFile("PLA", trenddd, writeaddress[0]);
		File.getInstance().saveLinkedListToFile("DFT", seasonaldd , writeaddress[0]);
		File.getInstance().saveLinkedListToFile("PAA", paadd , writeaddress[0]);

		TimeSeries pla_error = DataListOperator.getInstance().linkedtListSubtraction(ts1, trenddd);
		TimeSeries paa_error = DataListOperator.getInstance().linkedtListSubtraction(ts1, paadd);
		TimeSeries dft_error = DataListOperator.getInstance().linkedtListSubtraction(ts1, seasonaldd);
		TimeSeries mfdr_error = DataListOperator.getInstance().linkedtListSubtraction(ts1, mfdrfull);
		TimeSeries mfdr_no_error = DataListOperator.getInstance().linkedtListSubtraction(ts1, mfdrfullnonoise);
//		File.getInstance().saveLinkedListToFile("PLA_E", pla_error, writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("DFT_E", dft_error, writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR_E", mfdr_error, writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR_N_E", mfdr_no_error, writeaddress[0]);
		System.out.println("PLA:" + pla_error.energyDensity()/ts1.energyDensity());
		System.out.println("DFT:" + dft_error.energyDensity()/ts1.energyDensity());
		System.out.println("MFDR:" + mfdr_error.energyDensity()/ts1.energyDensity());
		System.out.println("MFDR_NO:" + mfdr_no_error.energyDensity()/ts1.energyDensity());
	}

	public void testLearnParameters() {
//		LearningResults results = facade.learnParameters(ts,tolerancevarience , new EuclideanDistance());
//		System.out.print("A Learn:");
//		for(int i = 0; i < 3 ; i++){
//			System.out.print("[" + i + "]" + results.alearn().getParameters()[i]);
//		}
//		System.out.println();
//		System.out.println("V Learn:" + results.vlearn().getGuaranteedCompensation());
	}

	public void testGetDistance() {

	}
	
	private TimeSeries generateTimeSeries(long size) {
		TimeSeries ts = new TimeSeries();
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
			ts.add(new Data(i, value));
		}
		return ts;
//		return 1/(2*Math.PI*3);
	}
}
