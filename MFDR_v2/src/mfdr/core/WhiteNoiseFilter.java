package mfdr.core;

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
	private double white_noise_level, white_noise_threshold, min_NSratio;
	private TimeSeries original_time_series;

	public WhiteNoiseFilter(double white_noise_level,
			double white_noise_threshold, double min_NTratio) {
		setWhiteNoiseLevel(white_noise_level);
		setWhiteNoiseThreshold(white_noise_threshold);
		setMinNSRatio(min_NTratio);
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

	public void setMinNSRatio(double min_NSratio){
		this.min_NSratio = min_NSratio;
	}
	
	public void setOriginalTimeSeries(TimeSeries original_time_series) {
		this.original_time_series = original_time_series;
	}

	
	/**
	 * Return the white noise related window size
	 * This solution uses the IMF set with highest proportion of white noise to define window size
	 * 
	 * @param imfs
	 * @param original_time_series
	 * @return white_noise_window
	 */
	public double getWhiteNoisePeriod(IMFS imfs,
			TimeSeries original_time_series) {
		setOriginalTimeSeries(original_time_series);
		double highestrank = this.min_NSratio;
		int index = -1;
		IMFS imfs_temp = (IMFS) imfs.clone();

		for (int i = imfs.size() - 1; i >= 0; i--) {
			logger.info("IMF NUM:" + imfs_temp.size());
			// If an (trimmed) IMF is regarded as a white noise, return the windowsize of its frequency,
			// Otherwise remove the last IMF with lowest frequency (which is potentially a signal).
			double noiserank = isWhiteNoise(imfs_temp);
			logger.info("W_Rank[" + i + "]:" + noiserank + "  C_Rank[" + index
					+ "]:" + highestrank);
			if (noiserank > highestrank) {
				highestrank = noiserank;
				index = i;
			}
			imfs_temp.removeLast();
			System.out.println();
		}
		// No IMF combination is white noise.
		if(index == -1){
			return 2;
		}
		
		return imfs.get(index).averageWavelength();
	}
	
	/**
	 * Return the white noise related window size
	 * 
	 * This is a naive implementation which defines the window size using the
	 * first IMF set with 50% more white noise IMFs 
	 * 
	 * @param imfs
	 * @param original_time_series
	 * @return white_noise_window
	 */
	public double getWhiteNoiseWindowSizeNaive(IMFS imfs,
			TimeSeries original_time_series) {
		setOriginalTimeSeries(original_time_series);
		IMFS imfs_temp = (IMFS) imfs.clone();

		while (!imfs_temp.isEmpty()) {
			logger.info("IMF NUM:" + imfs_temp.size());
			// If an (trimmed) IMF is regarded as a white noise, return the
			// windowsize of its frequency,
			// Otherwise remove the last IMF with lowest frequency (which is
			// potentially a signal).
			if (isWhiteNoiseNaive(imfs_temp)) {
				return imfs_temp.peekLast().averageWavelength();
			} else { // If
				imfs_temp.removeLast();
			}
			System.out.println();
		}
		// No IMF combination is white noise.
		return 0;
	}

	/*
	 * This function exam whether a set of IMFs is a combination of a white
	 * noise. If 50% more IMF is signal than the combination of IMFS is regarded
	 * as a signal, otherwise white noise.
	 */
	public boolean isSignalNaive(IMFS imfs) {
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
	public boolean isWhiteNoiseNaive(IMFS imfs) {
		return !isSignalNaive(imfs);
	}

	/*
	 * This function exam whether a set of IMFs is a combination of a white
	 * noise. Return rank of white noise.
	 */
	public double isSignal(IMFS imfs) {
		double signal_num = 0;
		for (int i = 0; i < imfs.size(); i++) {
			System.out.print("IMF[" + i + "]:");
			if (isSignal(imfs, imfs.get(i))) {
				signal_num++;
			}
		}
		return signal_num / imfs.size();
	}

	/*
	 * Mirror function of isSignalRank(IMFS imfs)
	 */
	public double isWhiteNoise(IMFS imfs) {
		return 1 - isSignal(imfs);
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
		// double ed =
		// imf.normalizedEnergyDensity(original_time_series.energyNormalizedFactor());

		// OPTOIN2: Normalization regarding the sum of all IMFs.
		double ed = imf.energyDensity() / imfs.totalEnergyDensity();

		/*
		 * STEP 2: Calculate the normalized wave length of the given IMF.
		 */

		// T is normalised as half of a wavelength, which similar to the count
		// of zero-crossings.
		// The normalisation is done regarding the highest possible frequency in
		// the original time series
		double T;
		try {
			// If count zerocorssings
			// OPTION 1: use zerocrossingcount to calculate average period (This can be more robust)
			double T2 = imf.timeLength()/(imf.getZeroCrossingCount()/2)/(this.original_time_series.normalisedWhiteNoiseWaveLength());
			// OPTION 2: use instant frequency to calculate average period
//			double T2 = imf.averageWavelength()/(this.original_time_series.normalisedWhiteNoiseWaveLength());
//			logger.info("T2:" + T2);
			// If use instant frequencies
			T = imf.averageWavelength()
					/ this.original_time_series.normalisedWhiteNoiseWaveLength();
//			logger.info("T:" + T);
			
		} catch (Exception e) {
			logger.info("No Instant Frequency exist for this IMF");
			return true;
		}

		/*
		 * STEP 3: Calculate the upper and lower bounds with regard to T.
		 */
		// If ed does not lie in the bound than is a signal, other wise white
		// noise
		StatisticalBounds sb = new StatisticalBoundsWhiteNoise(
				this.white_noise_level, imf.size());
		// Here is the standard solution which consider both upper bound and
		// lower bound.
		if (StatisticalProperty.getInstance().isStatisticalSignificance(
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
