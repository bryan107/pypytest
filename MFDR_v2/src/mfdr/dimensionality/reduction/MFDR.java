package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.datastructure.DWTData;
import mfdr.dimensionality.datastructure.MFDRData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.distance.Distance;
import mfdr.utility.DataListOperator;

public class MFDR extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(MFDR.class);
	private int NoC_t, NoC_s;
	private double noise_freq;
//	private double angle;
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

	public void updateSeasonalComponent(int NoC_s, double noise_freq) {
		this.NoC_s = NoC_s;
		this.noise_freq = noise_freq;
		this.dft = new DFT(NoC_s, noise_freq);
	}

	/*
	 * Read Parameters
	 */
	public int NoCTrend() {
		return this.NoC_t;
	}

	public int NoCSeasonal(){
		return this.NoC_s;
	}
	
	public double whiteNoiseFrequency(){
		return this.noise_freq;
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries output = new TimeSeries();
		LinkedList<MFDRData> mdfrlist = (LinkedList<MFDRData>) getDR(ts);
		LinkedList<PLAData> plalist = new LinkedList<PLAData>();
		LinkedList<DWTData> dwtlist = new LinkedList<DWTData>();
		for (int i = 0; i < mdfrlist.size(); i++) {
			plalist.add(mdfrlist.get(i).pla());
			dwtlist.add(mdfrlist.get(i).dwt());
		}
		TimeSeries plafull = this.pla.getFullResolutionDR(plalist, ts);
		TimeSeries dwtfull = reconstructFullResolutionSeasonal(ts, dwtlist);
		output = DataListOperator.getInstance().linkedListSum(plafull, dwtfull);
		return output;
	}
	
	public TimeSeries getFullResolutionTrend(TimeSeries ts){
		LinkedList<PLAData> plalist = new LinkedList<PLAData>();
		LinkedList<MFDRData> mdfrlist = (LinkedList<MFDRData>) getDR(ts);
		for (int i = 0; i < mdfrlist.size(); i++) {
			plalist.add(mdfrlist.get(i).pla());
		}
		return this.pla.getFullResolutionDR(plalist, ts);
	}
	
	public TimeSeries getFullResolutionSeasonal(TimeSeries ts){
		LinkedList<MFDRData> mdfrlist = (LinkedList<MFDRData>) getDR(ts);
		LinkedList<DWTData> dwtlist = new LinkedList<DWTData>();
		for (int i = 0; i < mdfrlist.size(); i++) {
			dwtlist.add(mdfrlist.get(i).dwt());
		}
		return reconstructFullResolutionSeasonal(ts, dwtlist);
	}

	public TimeSeries reconstructFullResolutionSeasonal(TimeSeries ts,
			LinkedList<DWTData> dwtlist) {
		// Stores each DWT segment with full resolution.
		LinkedList<TimeSeries> dwttemp = new LinkedList<TimeSeries>();
		// Time reference for DWT
		LinkedList<TimeSeries> sub_ref = DataListOperator.getInstance()
				.linkedListDivision(ts, this.windowsize_trend);
		for (int i = 0; i < dwtlist.size(); i++) {
			dwttemp.add(this.dwt.getFullResolutionDR(dwtlist.get(i),
					sub_ref.get(i)));
		}
		return (TimeSeries) DataListOperator.getInstance()
				.linkedListCombinition(dwttemp);
	}

	/**
	 * MFDR is NOT comparable to this function, please use another one provided.
	 */
	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts, double windowsize) {
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
	public TimeSeries getFullResolutionDR(TimeSeries ts,
			double windowsize_trend, double windowsize_freq) {
		updateTrendWindowsize(windowsize_trend);
		updateFrequencyWindowsize(windowsize_freq);
		return getFullResolutionDR(ts);
	}

	@Override
	public LinkedList<MFDRData> getDR(TimeSeries ts) {
		LinkedList<MFDRData> mdfrlist = new LinkedList<MFDRData>();

		// STEP 1: Calculate Trend Series
		LinkedList<PLAData> trend = this.pla.getDR(ts);
		// STEP 1-1: Prepare Residual Series
		TimeSeries trendfull = this.pla.getFullResolutionDR(ts);
		TimeSeries residualfull = DataListOperator.getInstance()
				.linkedtListSubtraction(ts, trendfull);
		// STEP 2: Calculate Seasonal Components
		DFTData seasonal = this.dft.getDR(ts);
		// STEP 2-2: Prepare Residual 2 Series
		TimeSeries seasonalfull = this.dft.getFullResolutionDR(ts);
		TimeSeries residualfull2 = DataListOperator.getInstance()
				.linkedtListSubtraction(ts, seasonalfull);
		// STEP 3: Calculate Residual Energy Density
					
		
		// Save results to MDFRDatas
		for (int i = 0; i < trend.size(); i++) {
			mdfrlist.add(new MFDRData(trend.get(i).time(), trend.get(i), freq
					.get(i)));
		}
		return mdfrlist;
	}
	// ************** TEST ZONE ***********************
	
	public TimeSeries getResidual(TimeSeries ts) {
		TimeSeries trendfull = this.pla.getFullResolutionDR(ts);
		return getResidual(ts, trendfull);
	}
	
	public TimeSeries getResidual(TimeSeries ts, TimeSeries trendfull ) {
		return DataListOperator.getInstance().linkedtListSubtraction(ts, trendfull);
	}
	
	public TimeSeries getTrend(TimeSeries ts) {
		return this.pla.getFullResolutionDR(ts);
	}
	
	//**************************************************
	/**
	 * MFDR is NOT comparable to this function, please use another one provided.
	 */
	@Override
	public Object getDR(TimeSeries ts, double windowsize) {
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
	public Object getDR(TimeSeries ts, double windowsize_trend,
			double windowsize_freq) {
		updateTrendWindowsize(windowsize_trend);
		updateFrequencyWindowsize(windowsize_freq);
		return getDR(ts);
	}

	@Override
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		logger.info("Please specify combining paramaters");
		return 0;
	}

	/**
	 * Get the combining distance with angle
	 * @param ts1
	 * @param ts2
	 * @param distance
	 * @param angle
	 * @return
	 */
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance, double angle) {
		LinkedList<MFDRData> mfdr1 = getDR(ts1);
		LinkedList<MFDRData> mfdr2 = getDR(ts2);
		// ts1 is used as the sampling reference
		return getDistance(mfdr1, mfdr2, ts1, distance, angle);
	}
	
	public double getDistance(LinkedList<MFDRData> mfdr1,
			LinkedList<MFDRData> mfdr2, TimeSeries ref, Distance distance, double angle) {
		MFDRDistanceDetails details = getDistanceDetails(mfdr1, mfdr2, ref,
				distance);
		return getDistance(details, angle);
	}

//	public double getDistance(MFDRDistanceDetails details){
//		return getDistance(details, this.angle);
//	}
	
	public double getDistance(MFDRDistanceDetails details, double angle){
		try {
			// Law of cosines: c^2 = a^2 + b^2 - 2ab*cos(angle)
			double dist_square = Math.pow(details.pla(), 2)
					+ Math.pow(details.dwt(), 2) - 2 * details.pla()
					* details.dwt() * Math.cos(angle);
			return Math.pow(dist_square, 0.5);
		} catch (Exception e) {
			logger.info("Distance cannot be calculated, Please the formats and lengths of the input datas."
					+ e);
			return 0;
		}
	}
	
	public double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance, double[] w) {
		LinkedList<MFDRData> mfdr1 = getDR(ts1);
		LinkedList<MFDRData> mfdr2 = getDR(ts2);
		// ts1 is used as the sampling reference
		return getDistance(mfdr1, mfdr2, ts1, distance, w);
	}
	
	public double getDistance(LinkedList<MFDRData> mfdr1,
			LinkedList<MFDRData> mfdr2, TimeSeries ref, Distance distance, double[] w) {
		MFDRDistanceDetails details = getDistanceDetails(mfdr1, mfdr2, ref,
				distance);
		return getDistance(details, w);
	}
	
	public double getDistance(MFDRDistanceDetails details, double[] w){
		return w[0] + w[1]*details.pla() + w[2]*details.dwt();
	}
	
	public MFDRDistanceDetails getDistanceDetails(TimeSeries ts1, TimeSeries ts2, Distance distance) {
		LinkedList<MFDRData> mfdr1 = getDR(ts1);
		LinkedList<MFDRData> mfdr2 = getDR(ts2);
		return getDistanceDetails(mfdr1, mfdr2, ts1, distance);
	}
	
	public MFDRDistanceDetails getDistanceDetails(LinkedList<MFDRData> mfdr1,
			LinkedList<MFDRData> mfdr2, TimeSeries ref, Distance distance) {
		if (mfdr1.size() != mfdr2.size()) {
			logger.info("Input MFDR lists must have the same length");
			return null;
		}
		// Prepare PLA and DWT data structure
		LinkedList<PLAData> pla1 = new LinkedList<PLAData>();
		LinkedList<PLAData> pla2 = new LinkedList<PLAData>();
		LinkedList<DWTData> dwt1 = new LinkedList<DWTData>();
		LinkedList<DWTData> dwt2 = new LinkedList<DWTData>();
		for (int i = 0; i < mfdr1.size(); i++) {
			pla1.add(mfdr1.get(i).pla());
			pla2.add(mfdr2.get(i).pla());
			dwt1.add(mfdr1.get(i).dwt());
			dwt2.add(mfdr2.get(i).dwt());
		}
		// Calculate distances of low frequency pla and high frequency dwt
		double dist_pla = pla.getDistance(pla1, pla2, ref, distance);
		double dist_dwt = dwt.getDistance(dwt1, dwt2, distance);
		return new MFDRDistanceDetails(dist_pla, dist_dwt);
	}

	
}
