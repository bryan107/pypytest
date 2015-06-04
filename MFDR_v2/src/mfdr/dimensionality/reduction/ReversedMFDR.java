package mfdr.dimensionality.reduction;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DWTData;
import mfdr.dimensionality.datastructure.MFDRData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.dimensionality.datastructure.ReversedMDFRData;
import mfdr.distance.Distance;
import mfdr.utility.DataListOperator;

public class ReversedMFDR extends DimensionalityReduction {
	private static Log logger = LogFactory.getLog(ReversedMFDR.class);
	private double windowsize_trend, windowsize_freq;
	private double weight_trend, weight_freq;
	private PLA pla;
	private DWT dwt;

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
	public ReversedMFDR(double windowsize_trend, double windowsize_freq,
			double weight_trend, double weight_freq) {
		updateTrendWindowsize(windowsize_trend);
		updateFrequencyWindowsize(windowsize_freq);
		updateTrendWeight(weight_trend);
		updateFrequencyWeight(weight_freq);
		this.pla = new PLA(this.windowsize_freq);
		this.dwt = new DWT(this.windowsize_trend);
	}

	public void updateTrendWindowsize(double windowsize_trend) {
		this.windowsize_trend = windowsize_trend;
		this.dwt = new DWT(this.windowsize_trend);
	}

	public void updateFrequencyWindowsize(double windowsize_freq) {
		this.windowsize_freq = windowsize_freq;
		this.pla = new PLA(this.windowsize_freq);
	}

	public void updateTrendWeight(double weight_trend) {
		this.weight_trend = weight_trend;
	}

	public void updateFrequencyWeight(double weight_freq) {
		this.weight_freq = weight_freq;
	}

	public double windowSizeTrend() {
		return this.windowsize_trend;
	}

	public double windowSizeFreq() {
		return this.windowsize_freq;
	}

	public double weightTrend() {
		return this.weight_trend;
	}

	public double weightFreq() {
		return this.weight_freq;
	}

	@Override
	public TimeSeries getFullResolutionDR(TimeSeries ts) {
		TimeSeries output = new TimeSeries();
		ReversedMDFRData rmfdr= getDR(ts);
		LinkedList<PLAData> plalist = rmfdr.pla();
		DWTData dwtdata = rmfdr.dwt();
		TimeSeries plafull = this.pla.getFullResolutionDR(plalist, ts);
		TimeSeries dwtfull = this.dwt.getFullResolutionDR(dwtdata, ts);
		output = DataListOperator.getInstance().linkedListSum(plafull, dwtfull);
		return output;
	}

	public TimeSeries reconstructFullResolutionDWT(TimeSeries ts,
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
		return (TimeSeries) DataListOperator.getInstance().linkedListCombinition(dwttemp);
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

	/**
	 * This function provides a reversed solution of MFDR.
	 * It uses DWT to describe low frequent signals while 
	 * using PLA to pick up details in the high frequency band.
	 */
	@Override
	public ReversedMDFRData getDR(TimeSeries ts) {
		// Prepare DWTs
		DWTData trend = this.dwt.getDR(ts);
		// Prepare PLA = TS - DWT
		TimeSeries trendfull = this.dwt.getFullResolutionDR(ts);
		TimeSeries freqfull = DataListOperator.getInstance().linkedtListSubtraction(ts, trendfull);
		LinkedList<PLAData> freq= this.pla.getDR(freqfull);
		return new ReversedMDFRData(trend, freq);
	}

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
		// TODO Auto-generated method stub
		return 0;
	}

}
