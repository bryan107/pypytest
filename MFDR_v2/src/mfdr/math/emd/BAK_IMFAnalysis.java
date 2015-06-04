package mfdr.math.emd;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.TimeSeries;
import mfdr.math.emd.datastructure.IMF;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.math.motif.Motif;
import mfdr.math.statistic.StatisticalBounds;
import mfdr.math.statistic.StatisticalBoundsWhiteNoise;
import mfdr.math.statistic.StatisticalProperty;

public class BAK_IMFAnalysis {
	// Logger
	private static Log logger = LogFactory.getLog(BAK_IMFAnalysis.class);
	
	/*
	 *  Datasets
	 */
	private TimeSeries ts; 
	private IMFS imfs;
	
	/*
	 * Parameters for Noise/Signal Analysis
	 */
	private double noise_whitenoiselevel, noise_threshold;
	 //  noise_threshold is used to separate signals whose wavelength are longer than t_threshold.
	 //  As the error of our approximation solution grow significantly after this threshold.
	
	/*
	 * Parameters for Frequency/Trend Analysis
	 */
	private int motif_k; 
	private double motif_threshold;
	private double FTratio;
	
	/*
	 * Constructor
	 * */
	public BAK_IMFAnalysis(TimeSeries ts, IMFS imfs, double noise_whitenoiselevel, double noise_threshold, double FTRatio, int motif_k, double motif_threshold){
		updateTimeSeries(ts);
		updateIMFS(imfs);
		setWhiteNoiseLevel(noise_whitenoiselevel);
		setNoiseThreshold(noise_threshold);
		setFTRatio(FTRatio);
		setMotifK(motif_k);
		setMotifThreshold(motif_threshold);
	}
	
	public void updateTimeSeries(TimeSeries ts){
		this.ts = ts;
	}
	
	public void updateIMFS(IMFS imfs){
		this.imfs = imfs;
	}
	
	/*
	 * Private parameters for Window extraction
	 */
	public void setWhiteNoiseLevel(double whitenoiselevel){
		this.noise_whitenoiselevel = whitenoiselevel;
	}
	
	public void setNoiseThreshold(double t_threshold){
		this.noise_threshold = t_threshold;
	}
	
	
	/*
	 * Parameter Settings for K-Motifs extraction 
	 */
	public void setFTRatio(double FTratio){
		this.FTratio = FTratio;
	}
	
	public void setMotifK(int motif_k){
		this.motif_k = motif_k;
	}
	
	public void setMotifThreshold(double motif_threshold){
		this.motif_threshold = motif_threshold;
	}
	
	
	/**
	 * WHITE NOISE DISCRIMINATION FUNCTIONS
	 * 
	 * As all IMFs of a white noise follows normal distribution, its energy should follows Chi-square distribution
	 * Here we exploit this signal charactier to validate the statistical significance of IMFs
	 * If an the energy of an IMF has similar probability distribution as white noises, this IMF is regarded as a noise;
	 * otherwise, regarded as a signal
	 **/
	
	public boolean isSignal(IMF imf){
		/*
		 *  SETP1: Calculate the normalized energy density of the given IMF.
		 */
		
		// TODO Fix with either input parameter choosing the function or remove one of them
		// OPTOIN1: Normalization regarding the energy density of the original signal.
//		double ed = imf.normalizedEnergyDensity(ts.energyNormalizedFactor());
		
		// OPTOIN2: Normalization regarding the sum of all IMFs.
		double ed = imf.energyDensity() / imfs.totalEnergyDensity();
		
		/*
		 * STEP 2: Calculate the normalized wave length of the given IMF.
		 */
		
		
		// T is normalised as half of a wavelength, which similar to the count of zero-crossings.
		double T = imf.averageWavelength()/ts.normalisedWhiteNoiseWaveLength();
		
		/*
		 * STEP 3: Calculate the upper and lower bounds with regard to T.
		 */
		// If ed does not lie in the bound than is a signal, other wise white noise
		StatisticalBounds sb = new StatisticalBoundsWhiteNoise(noise_whitenoiselevel, imf.size());
		if(T >= imf.size()){
			logger.info("No Instant Frequency exist for this IMF");
			return true;
		}
		// Here is the standard solution which consider both upper bound and lower bound.
		else if(StatisticalProperty.getInstance().isStatisticalSignificance(sb, T, ed, noise_threshold)){
			return true;
		}
//		// Here only consider upper bound. This is to fix the energy normalization error.
//		else if(StatisticalProperty.getInstance().isUpperBoundStatisticalSignificance(sb, T, ed, noise_threshold)){
//			return true;
//		}
		else{
			return false;
		}
	}
	
	/*
	 *  Mirror function of isSignal()
	 */
	public boolean isWhiteNoise(IMF imf){
		return !isSignal(imf);
	}

	
	/**
	 * FREQUENCY/TREND DISCRIMINATION FUNCTIONS
	 * 
	 * The functions are implemented with the consideration of Energy containing in the K-Motifs
	 * If the energy of an IMF concentrates in certain number of motifs (which repeat recursively), 
	 * The given IMF is considered as a frequency intense signal; otherwise trend intense.
	 **/

	/*
	 * Public function of Frequency/Trend discrimination
	 */
	public boolean isFreq(IMF imf){
		double FTratio = getKMotifsEnergyRatio(imf);
		logger.info("FTRatio: " + FTratio);
		if(FTratio >= this.FTratio)
			return true;
		else
			return false;
	}
	// Mirror function of isFreq()
	public boolean isTrend(IMF imf){
		return !isFreq(imf);
	}
	
	/*
	 * Private function used in isFreq()
	 */
	// The window size can be calculated from the average wave length of an IMF
	private double getKMotifsEnergyRatio(IMF imf){
		double kenergy = 0;
		// Get K-Motifs
		Motif motif = new Motif(imf.normalisedValues(imf.maxValue()), imf.averageWavelength());
		LinkedList<LinkedList<Integer>> kmotifs = motif.getKMotifs(motif_k, motif_threshold);
		// Loop all K-Motifs
		Iterator<LinkedList<Integer>> it = kmotifs.iterator();
		while (it.hasNext()) {
			LinkedList<Integer> linkedList = (LinkedList<Integer>) it.next();
			// The energy of the given motif
			kenergy += getMotifEnergy(motif, linkedList);
			// Store the energy of the Kth-Motif in kenergy
		}
		// return the energy ratio between K-Motifs and whole IMF
		return kenergy/imf.normalizedEnergy(imf.maxValue());
	}

	private double getMotifEnergy(Motif motif,
			LinkedList<java.lang.Integer> linkedList) {
		double energy = 0;
		// Loop all Members in the given motif
		Iterator<Integer> it2 = linkedList.iterator();
		while (it2.hasNext()) {
			Integer index = (Integer) it2.next();
			double subsignalenergy = motif.getSubSignal(index).energy();
			energy += subsignalenergy;
		}
		return energy;
	}
	

	
	
}
