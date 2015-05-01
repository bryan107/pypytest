package mfdr.math.emd;

import mfdr.datastructure.TimeSeries;
import mfdr.math.emd.datastructure.Envelopes;
import mfdr.math.emd.datastructure.IMF;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.math.emd.datastructure.LocalExtremas;
import mfdr.math.emd.utility.DataListCalculator;
import mfdr.math.emd.utility.DataListEnvelopCalculator;
import mfdr.math.emd.utility.DataListPropertyExtractor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EMD {
	private static Log logger = LogFactory.getLog(EMD.class);
	private TimeSeries residual = new TimeSeries();
	private InstantFrequency IF;
	private double zerocrossingaccuracy;
	
	public EMD(TimeSeries residual, double zerocrossingaccuracy, double W4, double W2, double W1){
		updateRedisual(residual);
		updateZeroCorssingAccuracy(zerocrossingaccuracy);
		updateInstantFrequency(W4, W2, W1);
	}
	
	public void updateRedisual(TimeSeries residual){
		this.residual = residual;
	}
	
	public void updateInstantFrequency(double W4, double W2, double W1){
		this.IF = new InstantFrequencyWeighted(W4, W2, W1);
	}
	
	public void updateZeroCorssingAccuracy(double zerocorssingaccuracy){
		this.zerocrossingaccuracy = zerocorssingaccuracy;
	}
	
	public IMFS getIMFs(int maxnumber){
		// Great a new IMFs object which contains IMFs.
		IMFS imfs = new IMFS();
		for(int iteration = 0 ; iteration < maxnumber ; iteration++){
			/*
			 * This is one iteration.
			 * */
			// 1. Extract local extremas from the residual signal
			LocalExtremas le = DataListPropertyExtractor.getInstance().getLocalExtremas(residual);
			
			// 2. Get the upper and lower envelope with the same resolution of the original signal (residual).
			// 2-1. If residual is monotonic, where no upper and lower envelopes can be extracted, break.
			if(le.isMonotonic()){
				imfs.add(new IMF(residual, zerocrossingaccuracy, IF));
				logger.info("EMD terminates at level [" + imfs.size() + "].  No further level can be extracted.");
				break;
			}
			Envelopes envelope = DataListEnvelopCalculator.getInstance().getEnvelopes(residual, le);
			
			// 3. Get the mean (remaining residual) of the two envelope
			TimeSeries mean = DataListCalculator.getInstance().getMean(envelope.upperEnvelope(), envelope.lowerEnvelope()); 
			
			// 4. Extract a IMF using the current mean.
			TimeSeries difference = DataListCalculator.getInstance().getDifference(residual, mean);
			IMF imf = new IMF(difference, zerocrossingaccuracy, IF);
		    // 5. Save the extracted IMF and make the mean become the new residual.
			imfs.add(imf);
			this.residual = mean;
		}
		// Return IMFs
		return imfs;
	}

//	public Frequency getInstantFrequency(LinkedList<Data> imf){
//		try {
//			LinkedList<Data> freq = DataListPropertyExtractor.getInstance().getInstantFrequency(imf, zerocrossingaccuracy, IF);
//			return new Frequency(freq);
//		} catch (Exception e) {
//			logger.error("EMD parameters are not properly setted" + e);
//		}
//		return null;
//	}
	
}
