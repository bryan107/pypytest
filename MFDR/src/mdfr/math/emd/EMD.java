package mdfr.math.emd;

import java.util.LinkedList;

public class EMD {
	private LinkedList<Double> residual = new LinkedList<Double>();
	public EMD(LinkedList<Double> residual){
		this.residual = residual;
	}
	
	public IMFs operates(int maxnumber){
		IMFs imfs = new IMFs(maxnumber);
		
		// This is one iteration.
		LocalExtremas le = Tools.getInstance().getLocalExtremas(residual);
		Envelopes envelope = CalculateEnvelopes.getInstance().getEnvelopes(residual, le);
		LinkedList<Double> mean = Tools.getInstance().getMean(envelope.upperEnvelope(), envelope.lowerEnvelope()); 
		LinkedList<Double> difference = Tools.getInstance().getDifference(residual, mean);
		imfs.addIMF(difference);
		this.residual = mean;
		//TODO complete multiple iteration function.
		//TODO test functionality.
		
		return imfs;
	}
	
	

	
	
}
