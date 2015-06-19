package mfdr.core;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;
import flanagan.control.LowPassPassive;
import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.distance.Distance;
import mfdr.learning.LinearLearning;
import mfdr.learning.LR4DLearning;
import mfdr.learning.LinearLearningResults;
import mfdr.learning.VarienceLearning;
import mfdr.learning.datastructure.TrainingSet;
import mfdr.math.emd.EMD;
import mfdr.math.emd.datastructure.IMFS;

public class MFDRParameterFacade {
	private static Log logger = LogFactory.getLog(MFDRParameterFacade.class);
	// private MFDR mfdr;
	// ****** Post processing flags ******
	// private boolean windowsizetrain = false;
	// private boolean parametertrain = false;

	// ***********************************
	// ********** EMD Parameters *********

	// IMF Decomposition
	private double[] IFparamaters = { 4, 2, 1 };
	private double zerocrossingaccuracy = 0.0001;
	private final int MAXLEVEL = 10;

	// White Noise Filter
	private WhiteNoiseFilter wfilter;

	// Trend Filter
	private TrendFilter tfilter;

	// ************************************
	// ********* Learning Objects *********
	// private WindowSize windowsize;
	// private AngleLearning alearn;
	// private VarienceLearning vlearn;

	// ************************************

	/**
	 * This constructor provides the old function with k 1-motif solution
	 * @param white_noise_level
	 * @param white_noise_threshold
	 * @param min_NSratio
	 * @param FTratio
	 * @param motif_k
	 * @param motif_threshold
	 */
	public MFDRParameterFacade(double white_noise_level,
			double white_noise_threshold, double min_NSratio, double FTratio,
			int motif_k, double motif_threshold) {
		// this.mfdr = new MFDR();
		updateWhiteNoiseFilter(white_noise_level, white_noise_threshold,
				min_NSratio);
		updateTrendFilter(FTratio, motif_k, motif_threshold);
	}
	
	/**
	 * This is the new solution with only dist input
	 * @param white_noise_level
	 * @param white_noise_threshold
	 * @param min_NSratio
	 * @param dist
	 */
	public MFDRParameterFacade(double white_noise_level,
			double white_noise_threshold, double min_NSratio, Distance dist) {
		// this.mfdr = new MFDR();
		updateWhiteNoiseFilter(white_noise_level, white_noise_threshold,
				min_NSratio);
		updateTrendFilter(dist);
	}

	
	public void updateWhiteNoiseFilter(double white_noise_level,
			double white_noise_threshold, double min_NSratio) {
		wfilter = new WhiteNoiseFilter(white_noise_level,
				white_noise_threshold, min_NSratio);
	}

	public void updateTrendFilter(double FTratio, int motif_k,
			double motif_threshold) {
		tfilter = new TrendFilter(FTratio, motif_k, motif_threshold);
	}
	
	public void updateTrendFilter(Distance dist){
		tfilter = new TrendFilter(dist);
	}


	/**
	 * Learn window size with LinkedList input TimeSeries
	 * 
	 * @param ts
	 * @return WindowSize
	 */
	public MFDRParameters learnMFDRParameters(LinkedList<TimeSeries> ts, int NoC, boolean use_IMF_tfilter) {
		int[] NoC_t_array = new int[ts.size()];
		int[] NoC_s_array = new int[ts.size()];
		double[] lowestperiod_array = new double[ts.size()];
		MFDRParameters parameters;
		// Learn Window sizes of with the training data set
		for (int i = 0; i < ts.size(); i++) {
			parameters = learnMFDRParameters(ts.get(i), NoC, use_IMF_tfilter);
			NoC_t_array[i] = parameters.trendNoC();
			NoC_s_array[i] = parameters.seasonalNoC();
			lowestperiod_array[i] = parameters.lowestPeriod();
		}
		// Take the medians as final results
		int NoC_t = (int) Stat.median(NoC_t_array);
		int NoC_s = (int) Stat.median(NoC_s_array);
		double lowestperiod = Stat.median(lowestperiod_array);
		// Adjust window sizes to fit the sampling rate of input time series.
		lowestperiod = (int) (lowestperiod / ts.peek().timeInterval())
				* ts.peek().timeInterval();
		return new MFDRParameters(NoC_t, NoC_s, lowestperiod);
	}

	/**
	 * Learn window size with Array input TimeSeries
	 * 
	 * @param ts
	 * @return
	 */
	public MFDRParameters learnMFDRParameters(TimeSeries[] ts, int NoC, boolean use_IMF_tfilter) {
		int[] NoC_t_array = new int[ts.length];
		int[] NoC_s_array = new int[ts.length];
		double[] lowestperiod_array = new double[ts.length];
		MFDRParameters parameters;
		// Learn Window sizes of with the training data set
		for (int i = 0; i < ts.length; i++) {
			parameters = learnMFDRParameters(ts[i], NoC, use_IMF_tfilter);
			NoC_t_array[i] = parameters.trendNoC();
			NoC_s_array[i] = parameters.seasonalNoC();
			lowestperiod_array[i] = parameters.lowestPeriod();
		}
		// Take the medians as final results
		int NoC_t = (int) Stat.median(NoC_t_array);
		int NoC_s = (int) Stat.median(NoC_s_array);
		double lowestperiod = Stat.median(lowestperiod_array);
		// Adjust window sizes to fit the sampling rate of input time series.
		lowestperiod = (int) (lowestperiod / ts[0].timeInterval())
				* ts[0].timeInterval();
		return new MFDRParameters(NoC_t, NoC_s, lowestperiod);
	}

	/**
	 * This function learns the window sizes from a time series
	 * the condition use_IMF_tfilter defines whether to use IMF to boost up tfilter search.
	 * This booster will degrade performance but gain speed.
	 * @param ts, NoC, use_IMF_tfilter
	 * @return
	 */
	public MFDRParameters learnMFDRParameters(TimeSeries ts, int NoC, boolean use_IMF_tfilter) {
		// STEP 1 : EMD
		// EMD service object
		EMD emd = new EMD(ts, zerocrossingaccuracy, IFparamaters[0],
				IFparamaters[1], IFparamaters[2]);
		// Calculate IMF with EMD
		IMFS imfs = emd.getIMFs(MAXLEVEL);

		for (int i = 0; i < imfs.size(); i++) {
			try {
				System.out.println("IMF[" + i + "]: "
						+ imfs.get(i).averageWavelength());
			} catch (Exception e) {
				System.out.println("IMF[" + i + "]: Infinit");
			}
		}
		int[] NoCs;
		// STEP 2: AYALYZE IMFs
		double lowestperiod = wfilter.getWhiteNoisePeriod(imfs, ts);
		if(use_IMF_tfilter){
			NoCs = tfilter.getMFDRNoCs(ts, imfs, NoC, lowestperiod);
		} else{
			NoCs = tfilter.getMFDRNoCs(ts, NoC, lowestperiod);
		}
		int NoC_t = NoCs[0];
		int NoC_s = NoCs[1];
		// STEP 3: Set training result;
		return new MFDRParameters(NoC_t, NoC_s, lowestperiod);
	}


	/*
	 * This function trains with reduced distance (should be more accurate) It
	 * provides higher lower bound results and make for sense in terms of
	 * learning. While inputs are at the same form as when use.
	 * 
	 * **** IT DOES NOT WORK......IT VIOLATES the TRIANGLE INEQUALITY
	 */

	public LearningResults learnParameters(LinkedList<TimeSeries> ts,
			MFDRWave mfdr, double tolerancevarience, Distance d) {

		LinearLearning alearn = new LR4DLearning(); // STEP 1: Setup training
													// set
		VarienceLearning vlearn = new VarienceLearning();
		
		LinkedList<TrainingSet> trainingset = alearn.getTrainingSet(ts, mfdr, d);

		// STEP 2: Train Alearn before use.
		LinearLearningResults weights = alearn.trainingParameters(trainingset);
		// STEP 3: Vlearn is trained as soon as it initiated.
		double guaranteed_cmopensation = vlearn.getGuaranteedCompensation(
				trainingset, weights, tolerancevarience);
		// STEP 4: Set training resutls
		return new LearningResults(weights, guaranteed_cmopensation);
	}
}
