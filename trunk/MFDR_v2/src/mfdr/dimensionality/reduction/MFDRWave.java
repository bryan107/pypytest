package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTWaveData;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.distance.Distance;
import mfdr.math.Sum;
import mfdr.math.emd.utility.DataListCalculator;
import mfdr.math.trigonometric.Theta;
import mfdr.utility.DataListOperator;
import mfdr.utility.File;

public class MFDRWave extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(MFDRWave.class);
	private int NoC_t, NoC_s;
	// private double angle;
	private PLA pla;
	private DFTWave dft;

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
	public MFDRWave(int NoC_t, int NoC_s) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s);
	}

	/*
	 * Setting up parameters
	 */
	public void updateTrendComponent(int NoC_t) {
		this.NoC_t = NoC_t;
		this.pla = new PLA(NoC_t);
	}

	public void updateSeasonalComponent(int NoC_s) {
		this.NoC_s = NoC_s;
		this.dft = new DFTWave(NoC_s);
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


	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries output = new TimeSeries();
		MFDRWaveData mfdrdata = getDR(ts);
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
	public TimeSeries getFullResolutionDR(TimeSeries ts, int NoC_t, int NoC_s) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s);
		return getFullResolutionDR(ts);
	}

	/**
	 * Default setting does not extract noises.
	 */
	@Override
	public MFDRWaveData getDR(TimeSeries ts) {
		// STEP 1: Calculate Trend Series
		LinkedList<PLAData> trends = this.pla.getDR(ts);
		// STEP 1-1: Prepare Residual Series
		TimeSeries trendfull = this.pla.getFullResolutionDR(ts);
		TimeSeries residualfull = DataListOperator.getInstance()
				.linkedtListSubtraction(ts, trendfull);
		// STEP 2: Calculate Seasonal Components
		LinkedList<DFTWaveData> seasonal;
		double noise_energy_density;
		seasonal = dft.getDR(residualfull);
		noise_energy_density = 0;

		// Save results to MDFRData and return
		return new MFDRWaveData(trends, seasonal, noise_energy_density);
	}

	/**
	 * getDR with frequency filter
	 * @param ts
	 * @param lowestperiod
	 * @return
	 */
	public MFDRWaveData getDR(TimeSeries ts, double lowestperiod) {
		// STEP 1: Calculate Trend Series
		LinkedList<PLAData> trends = this.pla.getDR(ts);
		// STEP 1-1: Prepare Residual Series
		TimeSeries trendfull = this.pla.getFullResolutionDR(ts);
		TimeSeries residualfull = DataListOperator.getInstance()
				.linkedtListSubtraction(ts, trendfull);
		// STEP 2: Calculate Seasonal Components
		LinkedList<DFTWaveData> seasonal;
		double noise_energy_density;
		double[] freq = dft.converTSToFrequency(residualfull);
		double[] noise = dft.extractHighFrequency(freq, lowestperiod, residualfull.timeInterval());
		seasonal = dft.getDR(freq);
		noise_energy_density = noiseEnergyDensity(noise);

		// Save results to MDFRData and return
		return new MFDRWaveData(trends, seasonal, noise_energy_density);
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
	public Object getDR(TimeSeries ts, int NoC_t, int NoC_s) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s);
		return getDR(ts);
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		MFDRWaveData mfdrdata1 = getDR(ts1);
		MFDRWaveData mfdrdata2 = getDR(ts2);
		// ts1 is used as the sampling reference
		return getDistance(mfdrdata1, mfdrdata2, ts1, distance);
	}

	public double getDistance(MFDRWaveData mfdrdata1, MFDRWaveData mfdrdata2,
			TimeSeries ts1, Distance distance) {
		MFDRDistanceDetails dist_details = getDistanceDetails(mfdrdata1, mfdrdata2, ts1, distance);
		double crossdistance = getTotalCrossDistance(mfdrdata1, mfdrdata2, ts1.size());
		return Math.sqrt(Math.pow(dist_details.trend(), 2) + Math.pow(dist_details.seasonal(), 2) + crossdistance);
	}
	
	public double getCrossBruteForceDistance(MFDRWaveData mfdrdata1, MFDRWaveData mfdrdata2,
			TimeSeries ts1, Distance distance) {
		MFDRDistanceDetails dist_details = getDistanceDetails(mfdrdata1, mfdrdata2, ts1, distance);
		double crossdistance = getTotalCellValue(mfdrdata1, mfdrdata2, ts1.size());
		return Math.sqrt(Math.pow(dist_details.trend(), 2) + Math.pow(dist_details.seasonal(), 2) + crossdistance);
	}

	
	private double getTotalCellValue(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, int tslength){
		double total = 0;
		for (int j = 0; j < NoC_t; j++) {
			double a3 = mfdrdata1.trends().get(j).a1()
					- mfdrdata2.trends().get(j).a1();
			double b3 = mfdrdata1.trends().get(j).a0()
					- mfdrdata2.trends().get(j).a0();
			for (int i = 0; i < NoC_s; i++) {
				total += getCellValue(j+1 ,a3, b3, mfdrdata1.seasonal().get(i), mfdrdata2.seasonal().get(i), tslength);
			}
		}
		return total;
	}
	private double getCellValue(int windownum, double a3, double b3, DFTWaveData w1, DFTWaveData w2,
			int tslength) {
		int windowsize = tslength / NoC_t;
		double sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
		for (int x = 0; x <= windowsize; x++) {
			sum1 += a3 * (x + 1) * w1.getWaveValue(x, tslength, windownum);
			sum2 += b3 * w1.getWaveValue(x, tslength, windownum);
			sum3 += a3 * (x + 1) * w2.getWaveValue(x, tslength, windownum);
			sum4 += b3 * w2.getWaveValue(x, tslength, windownum);
		}
		return sum1 + sum2 - sum3 - sum4;
	}
	
	private double getTotalCrossDistance(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, int tslength) {
		double ts1_total = 0;
		double ts2_total = 0;
		for (int j = 0; j < NoC_t; j++) {
			double a3 = mfdrdata1.trends().get(j).a1()
					- mfdrdata2.trends().get(j).a1();
			double b3 = mfdrdata1.trends().get(j).a0()
					- mfdrdata2.trends().get(j).a0();
			for (int i = 0; i < NoC_s; i++) {
				double c1 = mfdrdata1.seasonal().get(i).amplitude();
				double c2 = mfdrdata1.seasonal().get(i).amplitude();
				ts1_total += getCrossDistance(a3, b3, c1, tslength, NoC_t,
						mfdrdata1.seasonal().get(i).g(tslength), mfdrdata1
								.seasonal().get(i).k(tslength, j+1));
				ts2_total += getCrossDistance(a3, b3, c2, tslength, NoC_t,
						mfdrdata2.seasonal().get(i).g(tslength), mfdrdata2
								.seasonal().get(i).k(tslength, j+1));
			}
		}
		return ts1_total - ts2_total;
	}

	private double getCrossDistance(double a3, double b3, double c1,
			int tslength, int windownum, double g, double k) {
		double sum1 = a3 * c1
				* Sum.getInstance().xCos(g, k, tslength / windownum)
				+ (a3 + b3) * c1
				* Sum.getInstance().cos(g, k, tslength / windownum);
		return sum1;
	}

	public double getDistanceBruteForce(TimeSeries ts1, TimeSeries ts2,
			Distance distance) {
		MFDRWaveData mfdrdata1 = getDR(ts1);
		MFDRWaveData mfdrdata2 = getDR(ts2);
		// ts1 is used as the sampling reference
		return getDistanceBruteForce(mfdrdata1, mfdrdata2, ts1, distance);
	}

	public double getDistanceBruteForce(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, TimeSeries ref, Distance dist) {
		if (!checkCorrectness(mfdrdata1, mfdrdata2)) {
			return -1;
		}
		// Retrieve Full resolution Trends
		TimeSeries ts1_trend = pla.getFullResolutionDR(mfdrdata1.trends(), ref);
		TimeSeries ts2_trend = pla.getFullResolutionDR(mfdrdata2.trends(), ref);
		logger.info("Trend Dist:" + dist.calDistance(ts1_trend, ts2_trend, ref));
		// Retrieve full resolution seasonal
		TimeSeries ts1_seasonal = dft.getFullResolutionDR(mfdrdata1.seasonal(),
				ref);
		TimeSeries ts2_seasonal = dft.getFullResolutionDR(mfdrdata2.seasonal(),
				ref);

		TimeSeries ts1 = DataListCalculator.getInstance().getSum(ts1_trend,
				ts1_seasonal);
		TimeSeries ts2 = DataListCalculator.getInstance().getSum(ts2_trend,
				ts2_seasonal);
		return dist.calDistance(ts1, ts2, ts1);
	}

	private boolean checkCorrectness(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2) {
		if (mfdrdata1.trends().size() != mfdrdata2.trends().size()) {
			logger.info("Trend Size does not comparable");
			return false;
		} else if (mfdrdata1.seasonal().size() != mfdrdata2.seasonal().size()) {
			logger.info("Seasonal Size does not comparable");
			return false;
		} else {
			return true;
		}
	}

	// public double getDistance(MFDRDistanceDetails details, LearningResults
	// learn){
	// return w[0] + w[1]*details.trend() + w[2]*details.seasonal() +
	// w[3]*details.noise();
	// }
	//
	public MFDRDistanceDetails getDistanceDetails(TimeSeries ts1,
			TimeSeries ts2, Distance distance) {
		MFDRWaveData mfdr1 = getDR(ts1);
		MFDRWaveData mfdr2 = getDR(ts2);
		return getDistanceDetails(mfdr1, mfdr2, ts1, distance);
	}

	public MFDRDistanceDetails getDistanceDetails(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, TimeSeries ref, Distance distance) {
		// Prepare PLA and DWT data structure
		LinkedList<PLAData> trend1 = mfdrdata1.trends();
		LinkedList<PLAData> trend2 = mfdrdata2.trends();
		LinkedList<DFTWaveData> seasonal1 = mfdrdata1.seasonal();
		LinkedList<DFTWaveData> seasonal2 = mfdrdata2.seasonal();
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
