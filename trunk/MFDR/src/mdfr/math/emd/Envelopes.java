package mdfr.math.emd;

import java.util.LinkedList;

public class Envelopes {
	private LinkedList<Data> upperenvelope, lowerenvelope; 
	
	public Envelopes(LinkedList<Data> upperenvelope, LinkedList<Data> lowerenvelope){
		updateUpperEnvelope(upperenvelope);
		updateLowerEnvelope(lowerenvelope);
	}
	
	public void updateUpperEnvelope(LinkedList<Data> upperenvelope){
		this.upperenvelope = upperenvelope;
	}
	
	public void updateLowerEnvelope(LinkedList<Data> lowerenvelope){
		this.lowerenvelope = lowerenvelope;
	}
	
	public LinkedList<Data> upperEnvelope(){
		return upperenvelope;
	} 
	
	public LinkedList<Data> lowerEnvelope(){
		return lowerenvelope;
	}
}
