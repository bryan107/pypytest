package mdfr.core;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.math.emd.DataListOperator;
import mdfr.math.emd.datastructure.IMF;
import mdfr.math.emd.datastructure.IMFS;
import mdfr.math.motif.Motif;
import mdfr.math.statistic.StatisticalBounds;
import mdfr.math.statistic.StatisticalBoundsWhiteNoise;
import mdfr.math.statistic.StatisticalProperty;
import mdfr.utility.Print;

public class IMFAnalysis {
	
	private static Log logger = LogFactory.getLog(IMFAnalysis.class);
	IMFS imfs = new IMFS();
	/*
	 * Parameters for Noise/Signal Analysis
	 */
	//
	double noise_whitenoiselevel, noise_threshold;
	 //  t_threshold is used to separate signals whose wavelength are longer than t_threshold.
	 //  As the error of our approximation solution grow significantly after this threshold.
	
	/*
	 * Parameters for Frequency/Trend Analysis
	 */
	int motif_k; 
	double motif_threshold;
	double FTratio;
	
	/*
	 * Constructor
	 * */
	public IMFAnalysis(double noise_whitenoiselevel, double noise_threshold, double FTRatio, int motif_k, double motif_threshold){
		setWhiteNoiseLevel(noise_whitenoiselevel);
		setNoiseThreshold(noise_threshold);
		setFTRatio(FTRatio);
		setMotifK(motif_k);
		setMotifThreshold(motif_threshold);
	}
	
	
	/*
	 * Private parameters
	 */
	public void setWhiteNoiseLevel(double whitenoiselevel){
		this.noise_whitenoiselevel = whitenoiselevel;
	}
	
	public void setNoiseThreshold(double t_threshold){
		this.noise_threshold = t_threshold;
	}
	
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
	 * Here we exploit Chi-square to validate the statistical significance of IMFs
	 * If an IMF has similar probability distribution as white noises, this IMF is regarded as a noise;
	 * otherwise, regarded as a signal
	 **/
	
	/*
	 * TODO This the solution currently has problem with the energy normalization problem.
	 */

	public boolean isSignal(IMF imf){
		double averagewavelength = imf.averageWavelength();
		System.out.println("Length: " + averagewavelength);
//		double energydensity = imf.normalizedEnergyDensity();
		double energydensity = imf.energyDensity();
		logger.info("Engergy Density: " + energydensity);
		StatisticalBounds sb = new StatisticalBoundsWhiteNoise(noise_whitenoiselevel, imf.size());
		if(averagewavelength >= imf.size()){
			logger.info("No Instant Frequency exist for this IMF");
			return true;
		}else if(StatisticalProperty.getInstance().isStatisticalSignificance(sb, averagewavelength, energydensity, noise_threshold)){
			return true;
		}else{
			return false;
		}
	}

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

	// The window size can be calculated from the average wave length of an IMF
	private double getKMotifsEnergyRatio(IMF imf){
		double kenergy = 0;
		logger.info("K-Motif: WaveLength: " + imf.averageWavelength());
		Print.getInstance().printDataLinkedList(imf, 100);
		Print.getInstance().printDataLinkedList(imf.normalisedValues(), 100);
		
		Motif motif = new Motif(imf.normalisedValues(), imf.averageWavelength());
		LinkedList<LinkedList<Integer>> kmotifs = motif.getKMotifs(motif_k, motif_threshold);
		// Loop all K-Motifs
		double totalengergy = imf.normalizedEnergy();
		Iterator<LinkedList<Integer>> it = kmotifs.iterator();
		while (it.hasNext()) {
			LinkedList<Integer> linkedList = (LinkedList<Integer>) it.next();
			// The energy of the given motif
			kenergy += getMotifEnergy(motif, linkedList);
			// Store the energy of the Kth-Motif in kenergy
		}
		return kenergy/totalengergy;
	}

	private double getMotifEnergy(Motif motif,
			LinkedList<java.lang.Integer> linkedList) {
		double energy = 0;
		// Loop all Members in the given motif
		Iterator<Integer> it2 = linkedList.iterator();
		while (it2.hasNext()) {
			Integer integer = (Integer) it2.next();
			double motifenergy = motif.getMotif(integer).energy();
			int i = 0;
			energy += motifenergy;
		}
		return energy;
	}
	
	public boolean isFreq(IMF imf){
		double FTratio = getKMotifsEnergyRatio(imf);
		logger.info("FTRatio: " + FTratio);
		if(FTratio >= this.FTratio)
			return true;
		else
			return false;
	}
	
	public boolean isTrend(IMF imf){
		return !isFreq(imf);
	}
	
	
}
