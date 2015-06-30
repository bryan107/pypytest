package experiment.core;

import java.util.LinkedList;

import mfdr.core.MFDRParameterFacade;
import mfdr.core.MFDRParameters;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.reduction.DFTWave;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.dimensionality.reduction.PAA;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;
import junit.framework.TestCase;

public class ErrorEeperiment extends TestCase {
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
	
	public void testError(){
		for(int i = 1 ; i <4 ; i++ ){
			test(i, 4-i, "C:\\TEST\\MDFR\\Data\\GOOD.csv");
		}
	}
	
	private void test(int NoC_t, int NoC_s, String readingaddress){
		TimeSeries ts1 = (TimeSeries) File.getInstance().readTimeSeriesFromFile(readingaddress);
		
		MFDRParameters p = new MFDRParameters(NoC_t, NoC_s, 0);
		System.out.println("NoC_t:" + p.trendNoC() + "  NoC_s:" + p.seasonalNoC() + "  Noise Period:" + p.lowestPeriod());
		mfdr = new MFDRWave(p.trendNoC(), p.seasonalNoC());
		TimeSeries mfdrfull = mfdr.getFullResolutionDR(ts1);
		MFDRWaveData data = mfdr.getDR(ts1);
		PLA pla = new PLA(p.trendNoC());
		DFTWave dft = new DFTWave(p.seasonalNoC());
		TimeSeries trendfull = pla.getFullResolutionDR(data.trends(), ts1);
		TimeSeries seasonalfull = dft.getFullResolutionDR(data.seasonal(), ts1);
//		TimeSeries noisefull = mfdr.getFullResolutionNoise(data.noiseEnergyDensity(), ts1);
		TimeSeries mfdrfullnonoise = DataListOperator.getInstance().linkedListSum(trendfull, seasonalfull);
		File.getInstance().saveTimeToFile(ts1, writeaddress[0]);
		File.getInstance().saveLinkedListToFile("Original", ts1, writeaddress[0]);
	//	File.getInstance().saveLinkedListToFile("MFDRFull", mfdrfull, writeaddress[0]);
		File.getInstance().saveLinkedListToFile("MFDRFullNoNoise", mfdrfullnonoise, writeaddress[0]);
	//	File.getInstance().saveLinkedListToFile("Trend", trendfull, writeaddress[0]);
	//	File.getInstance().saveLinkedListToFile("Seasonal", seasonalfull, writeaddress[0]);
	//	File.getInstance().saveLinkedListToFile("Nosie", noisefull, writeaddress[0]);
		
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
	//	File.getInstance().saveLinkedListToFile("PLA_E", pla_error, writeaddress[0]);
	//	File.getInstance().saveLinkedListToFile("DFT_E", dft_error, writeaddress[0]);
	//	File.getInstance().saveLinkedListToFile("MFDR_E", mfdr_error, writeaddress[0]);
	//	File.getInstance().saveLinkedListToFile("MFDR_N_E", mfdr_no_error, writeaddress[0]);
		System.out.println("PLA:" + pla_error.energyDensity()/ts1.energyDensity());
		System.out.println("DFT:" + dft_error.energyDensity()/ts1.energyDensity());
		System.out.println("MFDR:" + mfdr_error.energyDensity()/ts1.energyDensity());
		System.out.println("MFDR_NO:" + mfdr_no_error.energyDensity()/ts1.energyDensity());
	}
}
