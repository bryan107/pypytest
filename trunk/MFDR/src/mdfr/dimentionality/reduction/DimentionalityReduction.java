package mdfr.dimentionality.reduction;

import mdfr.datastructure.TimeSeries;
import mdfr.distance.Distance;

public interface DimentionalityReduction {
	public void setWindowSize(double windowsize);
	
	// Returns DRs with the same resolution as the input Time series.
	public TimeSeries getFullResolutionDR(TimeSeries ts);
	
	// Returns DRs with the same resolution as the input Time series with window size.
	public TimeSeries getFullResolutionDR(TimeSeries ts, double windowsize);
	
	// Return DRs with a specific data structure.
	public Object getDR(TimeSeries ts);
	
	// Return DRs with a specific data structure with windowsize.
	public Object getDR(TimeSeries ts, double windowsize);
	
	// Returns the distance between ts1 and ts2 with the given DR.
	public TimeSeries getDistance(TimeSeries ts1, TimeSeries ts2, Distance distance);
}
