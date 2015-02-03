package mdfr.math.emd;

import java.util.LinkedList;

public class Envelopes {
	private LinkedList<Double> upperenvelope, lowerenvelope; 
	
	public Envelopes(LinkedList<Double> upperenvelope, LinkedList<Double> lowerenvelope){
		updateUpperEnvelope(upperenvelope);
		updateLowerEnvelope(lowerenvelope);
	}
	
	public void updateUpperEnvelope(LinkedList<Double> upperenvelope){
		this.upperenvelope = upperenvelope;
	}
	
	public void updateLowerEnvelope(LinkedList<Double> lowerenvelope){
		this.lowerenvelope = lowerenvelope;
	}
	
	public LinkedList<Double> upperEnvelope(){
		return upperenvelope;
	} 
	
	public LinkedList<Double> lowerEnvelope(){
		return lowerenvelope;
	}
}
