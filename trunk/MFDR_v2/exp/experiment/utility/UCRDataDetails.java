package experiment.utility;

import mfdr.datastructure.TimeSeries;

public class UCRDataDetails {

	private final int clusternumber;
	private final TimeSeries ts;
	
	public UCRDataDetails(int clusternumber , TimeSeries ts){
		this.clusternumber = clusternumber;
		this.ts = ts;
	}
	
	/**
	 * Returns the cluster number of this time series
	 * @return int cluternumber
	 */
	public int ClusterNumber(){
		return this.clusternumber;
	}
	
	/**
	 * Returns the series contant of this time series
	 * @return TimeSeries ts
	 */
	public TimeSeries timeSeries(){
		return this.ts;
	}
	
}
