package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.core.LearningResults;
import mfdr.datastructure.Data;
import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.MFDRData;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.distance.Distance;
import mfdr.learning.LinearLearningResults;
import mfdr.utility.DataListOperator;

public class MFDR extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(MFDR.class);
	private int NoC_t, NoC_s;
	private double lowestperiod;
	// private double angle;
	private PLA pla;
	private DFT dft;

	/**
	 * MFDR provides a dimensionality reduction function that combines: <li>
	 * IPLA: captures low frequent trends <li>DWT: captures frequently occurred
	 * frequency patterns.
	 * <p>
	 * Two windowsizes are required when using MFDR
	 * <li>windowsize_trend: defines the window size of IPLA and the series size
	 * of DWT
	 * <li>windowsize_freq: defines the window size of DWT
	 * <p>
	 * To avoid breaking the distance lower bound property of IPLA and DWT, MFDR
	 * requires two weights (weight_trend, weight_freq) to moderates its results
	 * when calculate distances between Time Series. The formula is illustrated
	 * as the following:
	 * <ul>
	 * Dist_MDFR(ts1, ts2) = weight_trend * Dist_IPLA(ts1,ts2) + weight_freq *
	 * Dist_DWT(ts1', ts2')
	 * </ul>
	 * where ts1' = ts1-IPLA(ts1), ts2' = ts2 - IPLA(ts2)
	 * <p>
	 * 
	 * @param windowsize_trend
	 * @param windowsize_freq
	 * @param weight_trend
	 * @param weight_freq
	 */
	public MFDR(int NoC_t, int NoC_s, double noise_freq) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s, noise_freq);
	}

	/*
	 * Setting up parameters
	 */
	public void updateTrendComponent(int NoC_t) {
		this.NoC_t = NoC_t;
		this.pla = new PLA(NoC_t);
	}

	public void updateSeasonalComponent(int NoC_s, double lowestperiod) {
		this.NoC_s = NoC_s;
		this.lowestperiod = lowestperiod;
		this.dft = new DFT(NoC_s);
	}

	/*
	 * Read Parameters
	 */
	public int NoCTrend() {
		return this.NoC_t;
	}

	public int NoCSeasonal() {
		return this.NoC_s;
	}

	public double whiteNoiseLowestPeriod() {
		return this.lowestperiod;
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries output = new TimeSeries();
		MFDRData mfdrdata = getDR(ts);
		TimeSeries trendfull = this.pla.getFullResolutionDR(mfdrdata.trends(),
				ts);
		TimeSeries seasonalfull = this.dft.getFullResolutionDR(
				mfdrdata.seasonal(), ts);
		TimeSeries noisefull = getFullResolutionNoise(
				mfdrdata.noiseEnergyDensity(), ts);
		output = DataListOperator.getInstance().linkedListSum(trendfull,
				seasonalfull);
		output = DataListOperator.getInstance()
				.linkedListSum(output, noisefull);
		return output;
	}

	public TimeSeries getFullResolutionNoise(double noise_energy_density,
			TimeSeries ref) {
		TimeSeries noisefull = new TimeSeries();
		// Energy Density = Varience
		double standard_deviation = Math.pow(noise_energy_density, 0.5);
		System.out.println("Standard_Deviation:" + standard_deviation);
		java.util.Random r = new java.util.Random();
		for (int i = 0; i < ref.size(); i++) {
			noisefull.add(new Data(ref.get(i).time(), r.nextGaussian()
					* standard_deviation));
		}
		return noisefull;
	}

	// public TimeSeries getFullResolutionTrend(TimeSeries ts){
	// LinkedList<PLAData> plalist = new LinkedList<PLAData>();
	// LinkedList<MFDRData> mdfrlist = (LinkedList<MFDRData>) getDR(ts);
	// for (int i = 0; i < mdfrlist.size(); i++) {
	// plalist.add(mdfrlist.get(i).pla());
	// }
	// return this.pla.getFullResolutionDR(plalist, ts);
	// }

	// public TimeSeries getFullResolutionSeasonal(TimeSeries ts){
	// LinkedList<MFDRData> mdfrlist = (LinkedList<MFDRData>) getDR(ts);
	// LinkedList<DWTData> dwtlist = new LinkedList<DWTData>();
	// for (int i = 0; i < mdfrlist.size(); i++) {
	// dwtlist.add(mdfrlist.get(i).dwt());
	// }
	// return reconstructFullResolutionSeasonal(ts, dwtlist);
	// }

	// public TimeSeries reconstructFullResolutionSeasonal(TimeSeries ts,
	// LinkedList<DWTData> dwtlist) {
	// // Stores each DWT segment with full resolution.
	// LinkedList<TimeSeries> dwttemp = new LinkedList<TimeSeries>();
	// // Time reference for DWT
	// LinkedList<TimeSeries> sub_ref = DataListOperator.getInstance()
	// .linkedListDivision(ts, this.windowsize_trend);
	// for (int i = 0; i < dwtlist.size(); i++) {
	// dwttemp.add(this.dwt.getFullResolutionDR(dwtlist.get(i),
	// sub_ref.get(i)));
	// }
	// return (TimeSeries) DataListOperator.getInstance()
	// .linkedListCombinition(dwttemp);
	// }

	/**
	 * MFDR is NOT comparable to this function, please use another one provided.
	 */
	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts, int NoC) {
		logger.info("MFDR is NOT comparable to this function, please use another one provided");
		return null;
	}

	/**
	 * This is an alternative getFullResolutionDR with the two required sizes.
	 * <p>
	 * 
	 * @parameter windowsize_trend, window_freq
	 * @return LinkedList<MDFRData>
	 */
	public TimeSeries getFullResolutionDR(TimeSeries ts, int NoC_t, int NoC_s,
			double noise_freq) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s, noise_freq);
		return getFullResolutionDR(ts);
	}

	@Override
	public MFDRData getDR(TimeSeries ts) {

		// STEP 1: Calculate Trend Series
		LinkedList<PLAData> trends = this.pla.getDR(ts);
		// STEP 1-1: Prepare Residual Series
		TimeSeries trendfull = this.pla.getFullResolutionDR(ts);
		TimeSeries residualfull = DataListOperator.getInstance()
				.linkedtListSubtraction(ts, trendfull);
		// STEP 2: Calculate Seasonal Components
		double[] freq = dft.converTSToFrequency(residualfull);
		double[] noise = dft.extractHighFrequency(freq, this.lowestperiod,
				residualfull.timeInterval());
		DFTData seasonal = dft.getDR(freq);

		// STEP 3: Calculate Noise Energy Density
		double noise_energy_density = noiseEnergyDensity(noise);

		// Save results to MDFRData and return
		return new MFDRData(trends, seasonal, noise_energy_density);
	}

	private double noiseEnergyDensity(double[] noise) {
		if (noise.length == 0) {
			return 0;
		}
		double sum = 0;
		for (int i = 0; i < noise.length; i++) {
			sum += Math.pow(noise[i], 2);
		}
		return Math.pow(sum, 0.5) / noise.length;
	}

	// ************** TEST ZONE ***********************

	public TimeSeries getResidual(TimeSeries ts) {
		TimeSeries trendfull = this.pla.getFullResolutionDR(ts);
		return getResidual(ts, trendfull);
	}

	public TimeSeries getResidual(TimeSeries ts, TimeSeries trendfull) {
		return DataListOperator.getInstance().linkedtListSubtraction(ts,
				trendfull);
	}

	public TimeSeries getTrend(TimeSeries ts) {
		return this.pla.getFullResolutionDR(ts);
	}

	// **************************************************
	/**
	 * MFDR is NOT comparable to this function, please use another one provided.
	 */
	@Override
	public Object getDR(TimeSeries ts, int NoC) {
		logger.info("MFDR is NOT comparable to this function, please use another one");
		return null;
	}

	/**
	 * This is an alternative getDR with the two required window sizes.
	 * <p>
	 * 
	 * @parameter windowsize_trend, windowsize_freq.
	 * @return LinkedList<MDFRData>
	 */
	public Object getDR(TimeSeries ts, int NoC_t, int NoC_s, double noise_freq) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s, noise_freq);
		return getDR(ts);
	}

	@Override
	/**
	 * This function does not valid in MFDR. Please use 
	 * getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance, double[] w)
	 * 
	 * @parameter windowsize_trend, windowsize_freq.
	 * @return LinkedList<MDFRData>
	 */
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		logger.info("Please specify combining paramaters");
		return 0;
	}

	// /**
	// * Get the combining distance with angle
	// * @param ts1
	// * @param ts2
	// * @param distance
	// * @param angle
	// * @return
	// */
	// public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance
	// distance, double angle) {
	// MFDRData mfdrdata1 = getDR(ts1);
	// MFDRData mfdrdata2 = getDR(ts2);
	// // ts1 is used as the sampling reference
	// return getDistance(mfdrdata1, mfdrdata1, ts1, distance, angle);
	// }

	// public double getDistance(MFDRData mfdr1,
	// MFDRData mfdr2, TimeSeries ref, Distance distance, double angle) {
	// MFDRDistanceDetails details = getDistanceDetails(mfdr1, mfdr2, ref,
	// distance);
	// return getDistance(details, angle);
	// }

	// public double getDistance(MFDRDistanceDetails details){
	// return getDistance(details, this.angle);
	// }

	// public double getDistance(MFDRDistanceDetails details, double angle){
	// try {
	// // Law of cosines: c^2 = a^2 + b^2 - 2ab*cos(angle)
	// double dist_square = Math.pow(details.pla(), 2)
	// + Math.pow(details.dwt(), 2) - 2 * details.pla()
	// * details.dwt() * Math.cos(angle);
	// return Math.pow(dist_square, 0.5);
	// } catch (Exception e) {
	// logger.info("Distance cannot be calculated, Please the formats and lengths of the input datas."
	// + e);
	// return 0;
	// }
	// }

	public double getDistance(TimeSeries ts1, TimeSeries ts2,
			Distance distance, LinearLearningResults weight, double compensation) {
		MFDRData mfdrdata1 = getDR(ts1);
		MFDRData mfdrdata2 = getDR(ts2);
		// ts1 is used as the sampling reference
		return getDistance(mfdrdata1, mfdrdata2, ts1, distance, weight, compensation);
	}

	public double getDistance(MFDRData mfdrdata1, MFDRData mfdrdata2,
			TimeSeries ref, Distance distance, LinearLearningResults weight, double compensation) {
		MFDRDistanceDetails details = getDistanceDetails(mfdrdata1, mfdrdata2,
				ref, distance);
		return getDistance(details, weight, compensation);
	}

	public double getDistance(MFDRDistanceDetails details, LinearLearningResults weight, double compensation) {
		return weight.constant()
				+ weight.trendWeight() * details.trend()
				+ weight.seasonalWeight() * details.seasonal() 
				+ weight.noiseWeight() * details.noise()
				-compensation;
	}

	// public double getDistance(MFDRDistanceDetails details, LearningResults
	// learn){
	// return w[0] + w[1]*details.trend() + w[2]*details.seasonal() +
	// w[3]*details.noise();
	// }
	//
	public MFDRDistanceDetails getDistanceDetails(TimeSeries ts1,
			TimeSeries ts2, Distance distance) {
		MFDRData mfdr1 = getDR(ts1);
		MFDRData mfdr2 = getDR(ts2);
		return getDistanceDetails(mfdr1, mfdr2, ts1, distance);
	}

	public MFDRDistanceDetails getDistanceDetails(MFDRData mfdrdata1,
			MFDRData mfdrdata2, TimeSeries ref, Distance distance) {
		// Prepare PLA and DWT data structure
		LinkedList<PLAData> trend1 = mfdrdata1.trends();
		LinkedList<PLAData> trend2 = mfdrdata2.trends();
		DFTData seasonal1 = mfdrdata1.seasonal();
		DFTData seasonal2 = mfdrdata2.seasonal();
		double e1 = mfdrdata1.noiseEnergyDensity();
		double e2 = mfdrdata2.noiseEnergyDensity();
		// Calculate distances of low frequency pla and high frequency dwt
		double dist_trend = pla.getDistance(trend1, trend2, ref, distance);
		double dist_seasonal = dft.getDistance(seasonal1, seasonal2, distance,
				ref.size());
		double dist_noise = distance.calWhiteNoiseDistance(e1, e2, ref.size());
		return new MFDRDistanceDetails(dist_trend, dist_seasonal, dist_noise);
	}

}
