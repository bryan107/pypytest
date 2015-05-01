package mfdr.core;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.distance.Distance;
import mfdr.learning.AngleLearning;
import mfdr.learning.VarienceLearning;
import mfdr.learning.datastructure.TrainingSet;
import mfdr.math.emd.EMD;
import mfdr.math.emd.datastructure.IMFS;

public class MFDRFacade {
	private static Log logger = LogFactory.getLog(MFDRFacade.class);
	private MFDR mfdr;
	// ****** Post processing flags ******
	private boolean windowsizetrain = false;
	private boolean parametertrain = false;
	
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
	private WindowSize windowsize;
	private AngleLearning alearn;
	private VarienceLearning vlearn;

	// ************************************

	public MFDRFacade(double white_noise_level, double white_noise_threshold, double FTratio, int motif_k, double motif_threshold) {
		this.mfdr = new MFDR();
		updateWhiteNoiseFilter(white_noise_level, white_noise_threshold);
		updateTrendFilter(FTratio, motif_k, motif_threshold);
	}

	public void updateWhiteNoiseFilter(double white_noise_level, double white_noise_threshold){
		wfilter = new WhiteNoiseFilter(white_noise_level, white_noise_threshold);
	}
	
	public void updateTrendFilter(double FTratio, int motif_k, double motif_threshold){
		tfilter = new TrendFilter(FTratio, motif_k, motif_threshold);
	}
	
	
	/*
	 * PHASE 1: Learn Window Size
	 */

	public WindowSize learnWindowSizes(LinkedList<TimeSeries> ts){
		return null;
	}
	
	/*
	 * TTT
	 */
	public WindowSize learnWindowSizes(TimeSeries ts) {
		// STEP 1 : EMD
		// EMD service object
		EMD emd = new EMD(ts, zerocrossingaccuracy, IFparamaters[0],
				IFparamaters[1], IFparamaters[2]);
		// Calculate IMF with EMD
		IMFS imfs = emd.getIMFs(MAXLEVEL);

		// STEP 2: AYALYZE IMFs
		double windowsize_noise = wfilter.getWhiteNoiseWindowSize(imfs, ts);
		// TODO TREND Filter has BUG!!! FIX!!!!!
		double windowsize_trend = tfilter.getTrendWindowSize(imfs, windowsize_noise);
		this.windowsize = new WindowSize(windowsize_noise, windowsize_trend);
		
		// STEP 3: Set training result;
		mfdr.updateFrequencyWindowsize(windowsize_noise);
		mfdr.updateTrendWindowsize(windowsize_trend);
		this.windowsizetrain = true;
		return new WindowSize(windowsize_noise, windowsize_trend);
	}

	// PHASE 2: Learn Angle and Variations
	public LearningResults learnParameters(LinkedList<TimeSeries> ts, AngleLearning alearn,
			VarienceLearning vlearn, double tolerancevarience, Distance d) {
		this.alearn = alearn;
		// STEP 1: Setup training set

		// This function trains with original distances
		// It provides less lower bound violation
		// LinkedList<TrainingSet> trainingset = prepareTrainingSet(ts,
		// windowsize.noise(), windowsize.trend(), d);
		/*
		 * This function trains with reduced distance (should be more accurate)
		 * It provides higher lower bound results and make for sense in terms of
		 * learning. While inputs are at the same form as when use.
		 */
		LinkedList<TrainingSet> trainingset = prepareReducedTrainingSet(ts,
				this.windowsize.noise(), this.windowsize.trend(), d);

		// STEP 2: Train Alearn before use.
		this.alearn.trainingParameters(trainingset);

		// STEP 3: Vlearn is trained as soon as it initiated.
		this.vlearn = new VarienceLearning(trainingset, alearn,
				tolerancevarience);
		// STEP 4: Set training resutls
		this.parametertrain = true;
		return new LearningResults(this.alearn, this.vlearn);
	}

	public double getDistance(TimeSeries ts1 , TimeSeries ts2, Distance d) {
		if (this.windowsizetrain == false){
			logger.info("Window sizes have yet been trained");
			return 0;
		} else if (this.parametertrain == false){
			logger.info("Paramaters have yet been trained");
			return 0;
		}
		// Calculate distance details with regard of ts1 and ts2
		MFDRDistanceDetails details = mfdr.getDistanceDetails(ts1, ts2, d);
		// Here we use PLA to model trends while dwt to model seasonal components
		mfdr.updateAngle(this.alearn.getAngle(details.pla(), details.dwt()));
		return this.vlearn.getGuaranteedCompensation(mfdr.getDistance(details));
	}

	/*
	 * Prepare training set for angle learning
	 * This function prepares FULL RESOLUTION signals as inputs for training
	 * Which does not fully reflex the needs
	 */
//	private LinkedList<TrainingSet> prepareTrainingSet(
//			LinkedList<TimeSeries> ts, double windowsize_freq,
//			double windowsize_trend, Distance d) {
//		LinkedList<TrainingSet> trainingset = new LinkedList<TrainingSet>();
//		MFDR mfdr = new MFDR(windowsize_trend, windowsize_freq);
//		double trainingsize = (ts.size() - 1) * (ts.size()) / 2;
//		// ********** DISTANCE DECOMPOSITION ************ //
//		for (int i = 0; i < trainingsize; i++) {
//			for (int j = i + 1; j < ts.size(); j++) {
//				// Prepare training time series
//				TimeSeries ts1 = ts.get(i);
//				TimeSeries ts2 = ts.get(j);
//				TimeSeries trend1 = mfdr.getTrend(ts1);
//				TimeSeries trend2 = mfdr.getTrend(ts2);
//				TimeSeries residual1 = mfdr.getResidual(ts1, trend1);
//				TimeSeries residual2 = mfdr.getResidual(ts2, trend2);
//				// calculate training distances
//				double origindist = d.calDistance(ts1, ts2, ts1);
//				double trenddist = d.calDistance(trend1, trend2, ts1);
//				double freqdist = d.calDistance(residual1, residual2, ts1);
//				// store results
//				trainingset
//						.add(new TrainingSet(trenddist, freqdist, origindist));
//			}
//		}
//		return trainingset;
//	}

	/*
	 * Prepare training set for angle learning. The input set is calculated with
	 * MFDR distance.
	 */
	private LinkedList<TrainingSet> prepareReducedTrainingSet(
			LinkedList<TimeSeries> ts, double windowsize_freq,
			double windowsize_trend, Distance d) {
		LinkedList<TrainingSet> trainingset = new LinkedList<TrainingSet>();
		MFDR mfdr = new MFDR(windowsize_trend, windowsize_freq);
		double trainingsize = (ts.size() - 1) * (ts.size()) / 2;
		// ********** DISTANCE DECOMPOSITION ************ //
		for (int i = 0; i < trainingsize; i++) {
			for (int j = i + 1; j < ts.size(); j++) {
				// Prepare training time series
				TimeSeries ts1 = ts.get(i);
				TimeSeries ts2 = ts.get(j);
				// calculate training distances
				double origindist = d.calDistance(ts1, ts2, ts1);
				MFDRDistanceDetails distdetails = mfdr.getDistanceDetails(ts1,
						ts2, d);
				double trenddist = distdetails.pla();
				double freqdist = distdetails.dwt();
				// store results
				trainingset
						.add(new TrainingSet(trenddist, freqdist, origindist));
			}
		}
		return trainingset;
	}

}
