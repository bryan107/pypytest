package mdfr.math.emd;

import java.util.LinkedList;

public class EMD {
	private LinkedList<Data> residual = new LinkedList<Data>();
	public EMD(LinkedList<Data> residual){
		this.residual = residual;
	}
	public IMFs operates(int maxnumber){
		// Great a new IMFs object which contains IMFs.
		IMFs imfs = new IMFs(maxnumber);
		// If the residual monotonic, which indicates no further IMF can be extracted.
		boolean residualismonotonic = false;
		
		for(int iteration = 0 ; iteration < maxnumber && residualismonotonic ; iteration++){
			/*
			 * This is one iteration.
			 * */
			// 1. Extract local extremas from the residual signal
			LocalExtremas le = Tools.getInstance().getLocalExtremas(residual);
			residualismonotonic = le.isMonotonic();
			// 2. Get the upper and lower envelope with the same resolution of the original signal (residual).
			Envelopes envelope = CalculateEnvelopes.getInstance().getEnvelopes(residual, le);
			// 3. Get the mean (remaining residual) of the two envelope
			LinkedList<Data> mean = Tools.getInstance().getMean(envelope.upperEnvelope(), envelope.lowerEnvelope()); 
			// 4. Extract a IMF using the current mean.
			LinkedList<Data> difference = Tools.getInstance().getDifference(residual, mean);
		    // 5. Save the extracted IMF and make the mean become the new residual.
			imfs.addIMF(difference);
			this.residual = mean;
		}
		
		// Return IMFs
		return imfs;
	}
}
