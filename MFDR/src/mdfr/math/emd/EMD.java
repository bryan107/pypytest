package mdfr.math.emd;

import java.util.LinkedList;

import mdfr.math.emd.DataListPropertyExtractor;
import mdfr.math.emd.datastructure.Data;
import mdfr.math.emd.datastructure.Envelopes;
import mdfr.math.emd.datastructure.IMFs;
import mdfr.math.emd.datastructure.LocalExtremas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EMD {
	private static Log logger = LogFactory.getLog(EMD.class);
	private LinkedList<Data> residual = new LinkedList<Data>();
	private InstantFrequency IF;
	private double zerocrossingaccuracy;
	private IMFs imfs;
	
	public EMD(LinkedList<Data> residual, double zerocrossingaccuracy, double W4, double W2, double W1){
		updateRedisual(residual);
		updateZeroCorssingAccuracy(zerocrossingaccuracy);
		updateInstantFrequency(W4, W2, W1);
	}
	
	public void updateRedisual(LinkedList<Data> residual){
		this.residual = residual;
	}
	
	public void updateInstantFrequency(double W4, double W2, double W1){
		this.IF = new InstantFrequencyWeighted(W4, W2, W1);
	}
	
	public void updateZeroCorssingAccuracy(double zerocorssingaccuracy){
		this.zerocrossingaccuracy = zerocorssingaccuracy;
	}
	
	public IMFs getIMFs(int maxnumber){
		// Great a new IMFs object which contains IMFs.
		IMFs imfs = new IMFs();
		for(int iteration = 0 ; iteration < maxnumber ; iteration++){
			/*
			 * This is one iteration.
			 * */
			// 1. Extract local extremas from the residual signal
			LocalExtremas le = DataListPropertyExtractor.getInstance().getLocalExtremas(residual);
			
			// 2. Get the upper and lower envelope with the same resolution of the original signal (residual).
			// 2-1. If residual is monotonic, where no upper and lower envelopes can be extracted, break.
			if(le.isMonotonic()){
				imfs.addIMF(residual);
				logger.info("EMD terminates at level [" + imfs.getIMFs().size() + "].  No further level can be extracted.");
				break;
			}
			Envelopes envelope = DataListEnvelopCalculator.getInstance().getEnvelopes(residual, le);
			
			// 3. Get the mean (remaining residual) of the two envelope
			LinkedList<Data> mean = DataListCalculator.getInstance().getMean(envelope.upperEnvelope(), envelope.lowerEnvelope()); 
			
			// 4. Extract a IMF using the current mean.
			LinkedList<Data> difference = DataListCalculator.getInstance().getDifference(residual, mean);
			
		    // 5. Save the extracted IMF and make the mean become the new residual.
			imfs.addIMF(difference);
			this.residual = mean;
		}
		// Store at local imfs;
		this.imfs = imfs;
		// Return IMFs
		return imfs;
	}

	public LinkedList<Data> getInstantFrequency(LinkedList<Data> imf){
		try {
			return DataListPropertyExtractor.getInstance().getInstantFrequency(imf, zerocrossingaccuracy, IF);
		} catch (Exception e) {
			logger.error("EMD parameters are not properly setted" + e);
		}
		return null;
	}
	
	public LinkedList<LinkedList<Data>> getInstantFrequency(){
		LinkedList<LinkedList<Data>> IFs = new LinkedList<LinkedList<Data>>();
		for(int i = 0 ; i < imfs.size() ; i++){
			IFs.add(DataListPropertyExtractor.getInstance().getInstantFrequency(imfs.getIMF(i), zerocrossingaccuracy, IF));
		}
		if(IFs.size() == 0){
			logger.warn("Please perform getIMFs() function first before calling this function");
			return null;
		}
		return IFs;
	}
}
