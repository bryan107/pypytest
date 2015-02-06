package mdfr.math.emd;

import java.util.LinkedList;

import mdfr.math.emd.Tools;
import mdfr.math.emd.datastructure.Envelopes;
import mdfr.math.emd.datastructure.IMFs;
import mdfr.math.emd.datastructure.LocalExtremas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EMD {
	private static Log logger = LogFactory.getLog(EMD.class);
	private LinkedList<Data> residual = new LinkedList<Data>();
	public EMD(LinkedList<Data> residual){
		updateRedisual(residual);
	}
	
	public void updateRedisual(LinkedList<Data> residual){
		this.residual = residual;
	}
	
	public IMFs operates(int maxnumber){
		// Great a new IMFs object which contains IMFs.
		IMFs imfs = new IMFs();
		
		for(int iteration = 0 ; iteration < maxnumber ; iteration++){
			/*
			 * This is one iteration.
			 * */
			// 1. Extract local extremas from the residual signal
			LocalExtremas le = Tools.getInstance().getLocalExtremas(residual);
			System.out.println("UpperExtrema:" + le.localMaxima().size() + "  LowerExtrema:" +le.localMinima().size());
			
			// 2. Get the upper and lower envelope with the same resolution of the original signal (residual).
			// 2-1. If residual is monotonic, where no upper and lower envelopes can be extracted, break.
			if(le.isMonotonic()){
				imfs.addIMF(residual);
				logger.info("EMD terminates at level [" + imfs.getIMFs().size() + "].  No further level can be extracted.");
				break;
			}

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
