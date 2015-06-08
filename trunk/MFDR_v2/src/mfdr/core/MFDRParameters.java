package mfdr.core;

public class MFDRParameters {
	
	private final int NoC_t, NoC_s;
	private final double lowestperiod;
	
	public MFDRParameters(int NoC_t, int NoC_s, double lowestperiod){
		this.NoC_t = NoC_t;
		this.NoC_s = NoC_s;
		this.lowestperiod = lowestperiod;
	} 
	
	public int trendNoC(){
		return NoC_t;
	}
	
	public int seasonalNoC(){
		return NoC_s;
	}
	
	public double lowestPeriod(){
		return lowestperiod;
	}
	
}
