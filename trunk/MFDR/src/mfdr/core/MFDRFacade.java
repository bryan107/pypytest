package mfdr.core;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;
import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.distance.Distance;
import mfdr.learning.AngleLearning;
import mfdr.learning.LR3DAngleLearning;
import mfdr.learning.VarienceLearning;
import mfdr.learning.datastructure.TrainingSet;
import mfdr.math.emd.EMD;
import mfdr.math.emd.datastructure.IMFS;

public class MFDRFacade {
	private static Log logger = LogFactory.getLog(MFDRFacade.class);
//	private MFDR mfdr;
	// ****** Post processing flags ******
//	private boolean windowsizetrain = false;
//	private boolean parametertrain = false;
	
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
//	private WindowSize windowsize;
//	private AngleLearning alearn;
//	private VarienceLearning vlearn;

	// ************************************

	public MFDRFacade(double white_noise_level, double white_noise_threshold, double min_NSratio, double FTratio, int motif_k, double motif_threshold) {
//		this.mfdr = new MFDR();
		updateWhiteNoiseFilter(white_noise_level, white_noise_threshold, min_NSratio);
		updateTrendFilter(FTratio, motif_k, motif_threshold);
	}

	public void updateWhiteNoiseFilter(double white_noise_level, double white_noise_threshold, double min_NSratio){
		wfilter = new WhiteNoiseFilter(white_noise_level, white_noise_threshold, min_NSratio);
	}
	
	public void updateTrendFilter(double FTratio, int motif_k, double motif_threshold){
		tfilter = new TrendFilter(FTratio, motif_k, motif_threshold);
	}
	
	
	/*
	 * PHASE 1: Learn Window Size
	 */

	/**
	 * Learn window size with LinkedList input TimeSeries 
	 * @param ts
	 * @return WindowSize
	 */
	public WindowSize learnWindowSizes(LinkedList<TimeSeries> ts) {
		double[] windowsize_noises = new double[ts.size()];
		double[] windowsize_trends = new double[ts.size()];
		WindowSize ws;
		// Learn Window sizes of with the training data set
		for(int i = 0 ; i < ts.size() ; i++){
			ws = learnWindowSizes(ts.get(i));
			windowsize_noises[i] = ws.noise();
			windowsize_trends[i] = ws.trend();
		}
		// Take the medians as final results
		double windowsize_noise = Stat.median(windowsize_noises);
		double windowsize_trend = Stat.median(windowsize_trends);
		// Adjust window sizes to fit the sampling rate of input time series. 
		windowsize_noise = (int) (windowsize_noise / ts.peek().timeInterval()) * ts.peek().timeInterval();
		windowsize_trend = (int) (Math.log((windowsize_trend / windowsize_noise))/ Math.log(2));
		windowsize_trend = Math.pow(2, windowsize_trend) *windowsize_noise;
		return new WindowSize(windowsize_noise,windowsize_trend);
	}
	
	/**
	 * Learn window size with Array input TimeSeries 
	 * @param ts
	 * @return
	 */
	public WindowSize learnWindowSizes(TimeSeries[] ts) {
		double[] windowsize_noises = new double[ts.length];
		double[] windowsize_trends = new double[ts.length];
		WindowSize ws;
		// Learn Window sizes of with the training data set
		for(int i = 0 ; i < ts.length ; i++){
			ws = learnWindowSizes(ts[i]);
			windowsize_noises[i] = ws.noise();
			windowsize_trends[i] = ws.trend();
		}
		// Take the medians as final results
		double windowsize_noise = Stat.median(windowsize_noises);
		double windowsize_trend = Stat.median(windowsize_trends);
		// Adjust window sizes to fit the sampling rate of input time series. 
		windowsize_noise = (int) (windowsize_noise / ts[0].timeInterval()) * ts[0].timeInterval();
		windowsize_trend = (int) (Math.log((windowsize_trend / windowsize_noise))/ Math.log(2));
		windowsize_trend = Math.pow(2, windowsize_trend) *windowsize_noise;
		return new WindowSize(windowsize_noise,windowsize_trend);
	}
	
	/**
	 * This function learns the window sizes from a time series
	 * @param ts
	 * @return
	 */
	public WindowSize learnWindowSizes(TimeSeries ts) {
		// STEP 1 : EMD
		// EMD service object
		EMD emd = new EMD(ts, zerocrossingaccuracy, IFparamaters[0],
				IFparamaters[1], IFparamaters[2]);
		// Calculate IMF with EMD
		IMFS imfs = emd.getIMFs(MAXLEVEL);
		
		for(int i = 0 ; i < imfs.size() ; i++){
			try {
				System.out.println("IMF[" + i + "]: " + imfs.get(i).averageWavelength());
			} catch (Exception e) {
				System.out.println("IMF[" + i + "]: Infinit");
			}
		}
		
		// STEP 2: AYALYZE IMFs
		double windowsize_noise = wfilter.getWhiteNoiseWindowSize(imfs, ts);
		double windowsize_trend = tfilter.getTrendWindowSize(imfs, windowsize_noise);
		
		// STEP 4: Set training result;
		return new WindowSize(windowsize_noise, windowsize_trend);
	}

	
	// PHASE 2: Learn Angle and Variations
	public LearningResults learnParameters(LinkedList<TimeSeries> ts, double tolerancevarience, Distance d, WindowSize ws) {
			
		AngleLearning alearn = new LR3DAngleLearning();		// STEP 1: Setup training set

		// This function trains with original distances
		// It provides less lower bound violation
//		 LinkedList<TrainingSet> trainingset = prepareTrainingSet(ts,
//		 windowsize.noise(), windowsize.trend(), d);
		/*
		 * This function trains with reduced distance (should be more accurate)
		 * It provides higher lower bound results and make for sense in terms of
		 * learning. While inputs are at the same form as when use.
		 * 
		 * **** IT DOES NOT WORK......IT VIOLATES the TRIANGLE INEQUALITY
		 */
		LinkedList<TrainingSet> trainingset = prepareReducedTrainingSet(ts,
				ws.noise(), ws.trend(), d);

		alearn = new LR3DAngleLearning();
		// STEP 2: Train Alearn before use.
		alearn.trainingParameters(trainingset);

		// STEP 3: Vlearn is trained as soon as it initiated.
		VarienceLearning vlearn = new VarienceLearning(trainingset, alearn,
				tolerancevarience);
		// STEP 4: Set training resutls
		return new LearningResults(alearn, vlearn);
	}

	/**
	 * Return Distance between two time series with trained parameters.
	 * @param ts1
	 * @param ts2
	 * @param d
	 * @return
	 */
	public double getDistance(TimeSeries ts1 , TimeSeries ts2, Distance d, WindowSize ws, LearningResults results) {
//		if (this.windowsizetrain == false){
//			logger.info("Window sizes have yet been trained");
//			return 0;
//		} else if (this.parametertrain == false){
//			logger.info("Paramaters have yet been trained");
//			return 0;
//		}
		// Calculate distance details with regard of ts1 and ts2
		MFDR mfdr = new MFDR(ws.trend(), ws.noise());
		MFDRDistanceDetails details = mfdr.getDistanceDetails(ts1, ts2, d);
		// Here we use PLA to model trends while dwt to model seasonal components
//		mfdr.updateAngle(this.alearn.getAngle(details.pla(), details.dwt()));
		
		return results.vlearn().getGuaranteedCompensation(mfdr.getDistance(details, results.alearn().getParameters()));
	}


	/*
	 * Prepare training set for learning. The input set is calculated with
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
	
	/*
	 * Prepare training set for learning
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
}
