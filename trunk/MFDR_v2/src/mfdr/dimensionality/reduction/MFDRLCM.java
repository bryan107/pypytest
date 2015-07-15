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
import mfdr.utility.DataListOperator;
import mfdr.utility.Print;

public class MFDRLCM extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(MFDRLCM.class);
	private int NoC_t, NoC_s;
	// private double angle;
	private PLA pla;
	private DFTForMFDR dft;

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
	public MFDRLCM(int NoC_t, int NoC_s) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s);
	}

	@Override
	public String name() {
		return "MFDR";
	}
	
	
	/*
	 * Setting up parameters
	 */
	
	public void updateParameters(int NoC_t,int NoC_s) {
		updateTrendComponent(NoC_t);
		updateSeasonalComponent(NoC_s);
	}
	
	public void updateTrendComponent(int NoC_t) {
		this.NoC_t = NoC_t;
		this.pla = new PLA(NoC_t);
	}

	public void updateSeasonalComponent(int NoC_s) {
		this.NoC_s = NoC_s;
		this.dft = new DFTForMFDR(NoC_s);
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
	
	public TimeSeries getFullResolutionDR(TimeSeries ts, double lowestperiod) {
		TimeSeries output = new TimeSeries();
		MFDRWaveData mfdrdata = getDR(ts,lowestperiod);
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
//		System.out.println("Standard_Deviation:" + standard_deviation);
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
	 * 
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
		double[] noise = dft.extractHighFrequency(freq, lowestperiod,
				residualfull.timeInterval());
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
		return getDistance(mfdrdata1, mfdrdata2, ts1.size(), distance);
	}

	public double getDistance(MFDRWaveData mfdrdata1, MFDRWaveData mfdrdata2,
			int size, Distance distance) {
		MFDRDistanceDetails dist_details = getDistanceDetails(mfdrdata1,
				mfdrdata2, size, distance);
		double trend_distance = dist_details.trend();
		
		double seasonal_distance = dist_details.seasonal();
		double cross_distance = getTotalCrossDistance(mfdrdata1, mfdrdata2, size);
//		logger.info("MFDR");
//		logger.info("Trend Dist:" + trend_distance);
//		logger.info("Seasonal Dist:" + seasonal_distance);
//		logger.info("Cross Error:" + cross_distance);
		double noiseenergy = mfdrdata1.noiseEnergyDensity()+mfdrdata2.noiseEnergyDensity();
		return Math.sqrt(Math.pow(trend_distance, 2) + Math.pow(seasonal_distance, 2) + cross_distance+size*noiseenergy);
	}

	public double getCrossBruteForceDistance(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, TimeSeries ts1, Distance distance) {
		
		MFDRDistanceDetails dist_details = getDistanceDetails(mfdrdata1,
				mfdrdata2, ts1.size(), distance);
		double trend_distance = dist_details.trend();
		double seasonal_distance = dist_details.seasonal();
		double cross_distance = getTotalCellDistance(mfdrdata1, mfdrdata2,
				ts1.size());
		logger.info("Cross Brute");
		logger.info("Trend Dist:" + trend_distance);
		logger.info("Seasonal Dist:" + seasonal_distance);
		logger.info("Brute Cross Error:" + cross_distance);
		return Math.sqrt(Math.pow(trend_distance, 2) + Math.pow(seasonal_distance, 2) + cross_distance);
	}

	public double getTotalCellDistance(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, int tslength) {
		double total1 = 0;
		double total2 = 0;
		// get LCM plaData lists
		int lcm = lcm(mfdrdata1.trends().size(), mfdrdata2.trends().size());
		LinkedList<LinkedList<PLAData>> trends = getLCMPLADataList(mfdrdata1.trends(), mfdrdata2.trends(), lcm, tslength);
		for (int j = 0; j < lcm; j++) {
			double a3 = trends.get(0).get(j).a1()
					- trends.get(1).get(j).a1();
			double b3 = trends.get(0).get(j).a0()
					- trends.get(1).get(j).a0();
			for (int i = 0; i < mfdrdata1.seasonal().size(); i++) {
				total1 += getCellDistance(j + 1, a3, b3, mfdrdata1.seasonal()
						.get(i), lcm, tslength);
			}
			for (int i = 0; i < mfdrdata2.seasonal().size(); i++) {
				total2 += getCellDistance(j + 1, a3, b3, mfdrdata2.seasonal()
						.get(i), lcm,tslength);
			}
		}
		return 2 * (total1-total2);
	}
	
	private double getCellDistance(int j, double a3, double b3,
			DFTWaveData w1, int lcm, int tslength) {
		int windowsize = tslength / lcm;
		double sum1 = 0, sum2 = 0;
		for (int x = 0; x < windowsize; x++) {
			sum1 += a3 * (x + 1) * w1.getWaveValue(x, tslength, j, windowsize);
			sum2 += b3 * w1.getWaveValue(x, tslength, j, windowsize);
		}
		return sum1 + sum2;
	}

	public double getTotalCrossDistance(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, int tslength) {
		double ts1_total = 0;
		double ts2_total = 0;
		// get LCM plaData lists
		int lcm = lcm(mfdrdata1.trends().size(), mfdrdata2.trends().size());
		LinkedList<LinkedList<PLAData>> trends = getLCMPLADataList(mfdrdata1.trends(), mfdrdata2.trends(), lcm, tslength);
		// Iteratino
		for (int j = 0; j < lcm; j++) {
			double a3 = trends.get(0).get(j).a1()
					- trends.get(1).get(j).a1();
			double b3 = trends.get(0).get(j).a0()
					- trends.get(1).get(j).a0();
			// MFDR_1
			for (int i = 0; i < mfdrdata1.seasonal().size(); i++) {
				double c1 = mfdrdata1.seasonal().get(i).amplitude();
				int windowsize = tslength / lcm;
				ts1_total += getCrossDistance(a3, b3, c1, windowsize, 
						mfdrdata1.seasonal().get(i).g(tslength), mfdrdata1
								.seasonal().get(i).k(tslength, j+1, windowsize));
	
			}
			// MFDR_2
			for(int i = 0; i < mfdrdata2.seasonal().size(); i++){
				double c2 = mfdrdata2.seasonal().get(i).amplitude();
				int windowsize = tslength / lcm;
				ts2_total += getCrossDistance(a3, b3, c2, windowsize, 
						mfdrdata2.seasonal().get(i).g(tslength), mfdrdata2
								.seasonal().get(i).k(tslength, j+1, windowsize));
			}
		}
		return 2*(ts1_total - ts2_total);
	}
	
	private double getCrossDistance(double a3, double b3, double c1,
			int windowsize, double g, double k) {
		double sum = 0;
//		double sum1 = a3 * c1* Sum.getInstance().xCos(g, k, windowsize-1);
//		double sum2 = b3 * c1 * Sum.getInstance().cos(g, k, windowsize-1);
		if(g==0){
			sum = a3*c1*(windowsize-1) + (a3 + b3) * c1;
		} else{
			sum = a3 * c1 * Sum.getInstance().xCos(g, k, windowsize-1)	
					+ (a3 + b3) * c1 * Sum.getInstance().cos(g, k, windowsize-1);
		}
		return sum;
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
//		if (!checkCorrectness(mfdrdata1, mfdrdata2)) {
//			return -1;
//		}
		// Retrieve Full resolution Trends
		TimeSeries ts1_trend = pla.getFullResolutionDR(mfdrdata1.trends(), ref);
		TimeSeries ts2_trend = pla.getFullResolutionDR(mfdrdata2.trends(), ref);
		// Retrieve full resolution seasonal
		TimeSeries ts1_seasonal = dft.getFullResolutionDR(mfdrdata1.seasonal(),
				ref);
		TimeSeries ts2_seasonal = dft.getFullResolutionDR(mfdrdata2.seasonal(),
				ref);
		logger.info("ALL Brute");
		logger.info("Dist Trend:" + dist.calDistance(ts1_trend, ts2_trend, ref));
		logger.info("Dist Seasonal:"
				+ dist.calDistance(ts1_seasonal, ts2_seasonal, ref));
		TimeSeries ts1 = DataListCalculator.getInstance().getSum(ts1_trend,
				ts1_seasonal);
		// TimeSeries ts11 =
		// DataListOperator.getInstance().linkedListSum(ts1_trend,
		// ts1_seasonal);
		TimeSeries ts2 = DataListCalculator.getInstance().getSum(ts2_trend,
				ts2_seasonal);
		// ***********
		double total_dist = dist.calDistance(ts1, ts2, ts1);
		double seasonal_dist = dist
				.calDistance(ts1_seasonal, ts2_seasonal, ref);
		double trend_dist = dist.calDistance(ts1_trend, ts2_trend, ref);
		logger.info("Error:"
				+ (Math.pow(total_dist, 2) - Math.pow(seasonal_dist, 2) - Math
						.pow(trend_dist, 2)));
		// ***************
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
		return getDistanceDetails(mfdr1, mfdr2, ts1.size(), distance);
	}

	public MFDRDistanceDetails getDistanceDetails(MFDRWaveData mfdrdata1,
			MFDRWaveData mfdrdata2, int size, Distance distance) {
		// Prepare PLA and DWT data structure
		LinkedList<PLAData> trend1 = mfdrdata1.trends();
		LinkedList<PLAData> trend2 = mfdrdata2.trends();
		LinkedList<DFTWaveData> seasonal1 = mfdrdata1.seasonal();
		LinkedList<DFTWaveData> seasonal2 = mfdrdata2.seasonal();
		double e1 = mfdrdata1.noiseEnergyDensity();
		double e2 = mfdrdata2.noiseEnergyDensity();
		// Calculate distances of low frequency pla and high frequency dwt
        int lcm = lcm(trend1.size(), trend2.size());
        LinkedList<LinkedList<PLAData>> lcmtrends = getLCMPLADataList(trend1, trend2, lcm, size);
//        print(lcmtrends.get(0));
//        print(lcmtrends.get(1));
		double dist_trend = pla.getDistance(lcmtrends.get(0), lcmtrends.get(1), size, distance);
		double dist_seasonal = dft.getDistance(seasonal1, seasonal2, distance,size);
		double dist_noise = distance.calWhiteNoiseDistance(e1, e2, size);
		return new MFDRDistanceDetails(dist_trend, dist_seasonal, dist_noise);
	}
	private void print(LinkedList<PLAData> list){
		for(int i =0 ; i < list.size() ; i++){
           System.out.print("["+ i + "]" + " T:" + list.get(i).time() + " A0:" + list.get(i).a0()+ " A1:" + list.get(i).a1());
		}
		System.out.println();
	}
	
	public LinkedList<LinkedList<PLAData>> getLCMPLADataList(LinkedList<PLAData> trend1, LinkedList<PLAData> trend2, int lcm, double tslength){
		LinkedList<LinkedList<PLAData>> list = new LinkedList<LinkedList<PLAData>>();
		int division1 = lcm/trend1.size();
		int division2 = lcm/trend2.size();
		double windowsize = tslength/lcm;
		LinkedList<PLAData> trend_lcm_1 = new LinkedList<PLAData>();
		LinkedList<PLAData> trend_lcm_2 = new LinkedList<PLAData>();
		for(int i = 0 ; i < lcm ; i++){
			double time = trend1.get(0).time() + windowsize*i;
			trend_lcm_1.add(new PLAData(time, trend1.get(i / division1).a0()+trend1.get(i / division1).a1()*(i%division1)*windowsize, trend1.get(i / division1).a1()));
			trend_lcm_2.add(new PLAData(time, trend2.get(i / division2).a0()+trend2.get(i / division2).a1()*(i%division2)*windowsize, trend2.get(i / division2).a1()));
		}
		list.add(trend_lcm_1);
		list.add(trend_lcm_2);
		return list;
	}
	
	
	public int gcd(int a, int b)
	{
	    while (b > 0)
	    {
	        int temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}
	
	public int lcm(int a, int b)
	{
	    return a * (b / gcd(a, b));
	}

	
}
