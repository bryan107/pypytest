package mfdr.core;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.TimeSeries;
import mfdr.math.emd.datastructure.IMF;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.math.statistic.StatisticalBounds;
import mfdr.math.statistic.StatisticalBoundsWhiteNoise;
import mfdr.math.statistic.StatisticalProperty;

/**
 * WHITE NOISE DISCRIMINATION FUNCTIONS
 * 
 * As all IMFs of a white noise follows normal distribution, its energy should
 * follows Chi-square distribution Here we exploit this signal charactier to
 * validate the statistical significance of IMFs If an the energy of an IMF has
 * similar probability distribution as white noises, this IMF is regarded as a
 * noise; otherwise, regarded as a signal
 **/

public class WhiteNoiseFilter {
	// Logger
	private static Log logger = LogFactory.getLog(WhiteNoiseFilter.class);
	/*
	 * white_noise_level: Defines the level of tolerance standard deviation e.g.
	 * 5 -> p-value 0.01 white_noise_threshold: Defines the minimum frequency of
	 * IMF as the error of approximation sotlution adopted in IMFAnalyse grow
	 * significant after certain level. (See paper Huang 2004.)
	 */
	private double white_noise_level, white_noise_threshold;
	private TimeSeries original_time_series;

	public WhiteNoiseFilter(double white_noise_level,
			double white_noise_threshold) {
		setWhiteNoiseLevel(white_noise_level);
		setWhiteNoiseThreshold(white_noise_threshold);
		setOriginalTimeSeries(original_time_series);
	}

	/*
	 * Parameter Settings for White Noise Filter
	 */
	public void setWhiteNoiseLevel(double white_noise_level) {
		this.white_noise_level = white_noise_level;
	}

	public void setWhiteNoiseThreshold(double white_noise_threshold) {
		this.white_noise_threshold = white_noise_threshold;
	}

	public void setOriginalTimeSeries(TimeSeries original_time_series) {
		this.original_time_series = original_time_series;
	}

	/*
	 * Return the windowsize when a IMFs is consider as a white noise
	 */
	public double getWhiteNoiseWindowSize(IMFS imfs) {
		IMFS imfs_temp = new IMFS();
		Collections.copy(imfs, imfs_temp);

		for (int i = 0; i < imfs_temp.size(); i++) {
			// If an (trimmed) IMF is regarded as a white noise, return the windowsize of its frequency,
			// Otherwise remove the last IMF with lowest frequency (which is potentially a signal).
			if (isWhiteNoise(imfs_temp)) {
				return imfs_temp.peekLast().averageWavelength();	
			} else { // If
				imfs_temp.removeLast();
			}
		}
		// No IMF combination is white noise. 
		return 0;
	}

	/*
	 * This function exam whether a set of IMFs is a combination of a white
	 * noise. If 50% more IMF is signal than the combination of IMFS is regarded
	 * as a signal, otherwise white noise.
	 */
	public boolean isSignal(IMFS imfs) {
		int signal_num = 0, whitenoise_num = 0;
		for (int i = 0; i < imfs.size(); i++) {
			if (isSignal(imfs, imfs.get(i))) {
				signal_num++;
			} else {
				whitenoise_num++;
			}
		}
		if (signal_num > whitenoise_num) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Mirror function of isSignal(IMFS imfs)
	 */
	public boolean isWhiteNoise(IMFS imfs) {
		return !isSignal(imfs);
	}

	/*
	 * Exam whether a IMF in a set of IMFs behave as a white noise.
	 */
	public boolean isSignal(IMFS imfs, IMF imf) {
		/*
		 * SETP1: Calculate the normalized energy density of the given IMF.
		 */

		// TODO Fix with either input parameter choosing the function or remove
		// one of them
		// OPTOIN1: Normalization regarding the energy density of the original
		// signal.
		// double ed = imf.normalizedEnergyDensity(ts.energyNormalizedFactor());

		// OPTOIN2: Normalization regarding the sum of all IMFs.
		double ed = imf.energyDensity() / imfs.totalEnergyDensity();

		/*
		 * STEP 2: Calculate the normalized wave length of the given IMF.
		 */

		// T is normalised as half of a wavelength, which similar to the count
		// of zero-crossings.
		// The normalisation is done regarding the highest possible frequency in
		// the original time series
		double T = imf.averageWavelength()
				/ this.original_time_series.normalisedWhiteNoiseWaveLength();

		/*
		 * STEP 3: Calculate the upper and lower bounds with regard to T.
		 */
		// If ed does not lie in the bound than is a signal, other wise white
		// noise
		StatisticalBounds sb = new StatisticalBoundsWhiteNoise(
				this.white_noise_level, imf.size());
		if (T >= imf.size()) {
			logger.info("No Instant Frequency exist for this IMF");
			return true;
		}
		// Here is the standard solution which consider both upper bound and
		// lower bound.
		else if (StatisticalProperty.getInstance().isStatisticalSignificance(
				sb, T, ed, this.white_noise_threshold)) {
			return true;
		}
		// // Here only consider upper bound. This is to fix the energy
		// normalization error.
		// else
		// if(StatisticalProperty.getInstance().isUpperBoundStatisticalSignificance(sb,
		// T, ed, noise_threshold)){
		// return true;
		// }
		else {
			return false;
		}
	}

	/*
	 * Mirror function of isSignal(IMFS imfs, IMF imf)
	 */
	public boolean isWhiteNoise(IMFS imfs, IMF imf) {
		return !isSignal(imfs, imf);
	}

}
