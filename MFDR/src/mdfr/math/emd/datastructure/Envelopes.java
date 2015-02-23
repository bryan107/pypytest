package mdfr.math.emd.datastructure;

import mdfr.datastructure.TimeSeries;

public class Envelopes {
	private TimeSeries upperenvelope, lowerenvelope; 
	
	public Envelopes(TimeSeries upperenvelope, TimeSeries lowerenvelope){
		updateUpperEnvelope(upperenvelope);
		updateLowerEnvelope(lowerenvelope);
	}
	
	public void updateUpperEnvelope(TimeSeries upperenvelope){
		this.upperenvelope = upperenvelope;
	}
	
	public void updateLowerEnvelope(TimeSeries lowerenvelope){
		this.lowerenvelope = lowerenvelope;
	}
	
	public TimeSeries upperEnvelope(){
		return upperenvelope;
	} 
	
	public TimeSeries lowerEnvelope(){
		return lowerenvelope;
	}
}
