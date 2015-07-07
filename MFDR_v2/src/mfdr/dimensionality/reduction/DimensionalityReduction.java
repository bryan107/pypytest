package mfdr.dimensionality.reduction;

import mfdr.datastructure.TimeSeries;
import mfdr.distance.Distance;

public abstract class DimensionalityReduction {
	protected int NoC;
	public void setNoC(int NoC){
		this.NoC = NoC;
	}
	
	public abstract String name();

	// Returns DRs with the same resolution as the input Time series.
	public abstract TimeSeries getFullResolutionDR(TimeSeries ts);
	
	// Returns DRs with the same resolution as the input Time series with window size.
	public TimeSeries getFullResolutionDR(TimeSeries ts, int NoC){
		setNoC(NoC);
		return getFullResolutionDR(ts);
	}
	
	// Return DRs with a specific data structure.
	public abstract Object getDR(TimeSeries ts);
	
	// Return DRs with a specific data structure with windowsize.
	public Object getDR(TimeSeries ts, int NoC){
		setNoC(NoC);
		return getDR(ts);
	}
	
	/**
	 * Returns the distance between ts1 and ts2 with the given DR.
	 * @param ts1, ts2, distance object
	 * @return distance in double form
	 */
	public abstract double getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance);
}
