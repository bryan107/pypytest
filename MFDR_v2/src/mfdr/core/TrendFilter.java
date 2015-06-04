package mfdr.core;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.math.emd.datastructure.IMF;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.math.motif.Motif;

/**
 * FREQUENCY/TREND DISCRIMINATION FUNCTIONS
 * 
 * The functions are implemented with the consideration of Energy containing in
 * the K-Motifs If the energy of an IMF concentrates in certain number of motifs
 * (which repeat recursively), The given IMF is considered as a frequency
 * intense signal; otherwise trend intense.
 **/

public class TrendFilter {
	// Logger
	private static Log logger = LogFactory.getLog(TrendFilter.class);
	private double FTratio, motif_threshold;
	private int motif_k;

	public TrendFilter(double FTratio, int motif_k, double motif_threshold) {
		setFTRatio(FTratio);
		setMotifK(motif_k);
		setMotifThreshold(motif_threshold);
	}

	/*
	 * Parameter Settings for K-Motifs extraction
	 */
	public void setFTRatio(double FTratio) {
		this.FTratio = FTratio;
	}

	public void setMotifK(int motif_k) {
		this.motif_k = motif_k;
	}

	public void setMotifThreshold(double motif_threshold) {
		this.motif_threshold = motif_threshold;
	}

	/**
	 * Get the window size for trend / seasonal components distinguishing.
	 * @param imfs
	 * @param windowsize_noise
	 * @return trend_window_size
	 */
	public double getTrendWindowSize(IMFS imfs, double windowsize_noise) {
		for (int i = 0; i < imfs.size(); i++) {
			double imf_wavelength;
			// Catch infinite wavelength exceptions.
			try {
				imf_wavelength = imfs.get(i).averageWavelength();
			} catch (Exception e) {
				logger.info("Cannot extract wavelength from IMF[" + i + "], set IMF[" + (i - 1)+ "]'s frequency as trend window size");
				return imfs.get(i-1).averageWavelength();
			}
			// If this imf is a white noise
			if (imf_wavelength <= windowsize_noise)
				continue;
			// If this imf is a trend
			if (isTrend(imfs.get(i))){
				return imfs.get(i-1).averageWavelength();

 					
			}
		}
		// If no can be formed
		logger.info("Set Time Length as Trend window");
		return imfs.peekLast().timeLength();
	}

	/*
	 * Public function of Frequency/Trend discrimination
	 */
	public boolean isFreq(IMF imf) {
		double FTratio = getKMotifsEnergyRatio(imf);
		logger.info("FTRatio: " + FTratio);
		if (FTratio >= this.FTratio)
			return true;
		else
			return false;
	}

	// Mirror function of isFreq()
	public boolean isTrend(IMF imf) {
		return !isFreq(imf);
	}

	/*
	 * Private function used in isFreq()
	 */
	// The window size can be calculated from the average wave length of an IMF
	private double getKMotifsEnergyRatio(IMF imf) {
		double kenergy = 0;
		// Get K-Motifs
		Motif motif = new Motif(imf.normalisedValues(imf.maxValue()),
				imf.averageWavelength());
		LinkedList<LinkedList<Integer>> kmotifs = motif.getKMotifs(motif_k,
				motif_threshold);
		// Loop all K-Motifs
		Iterator<LinkedList<Integer>> it = kmotifs.iterator();
		while (it.hasNext()) {
			LinkedList<Integer> linkedList = (LinkedList<Integer>) it.next();
			// The energy of the given motif
			double engergy = getMotifEnergy(motif, linkedList);
			kenergy += getMotifEnergy(motif, linkedList);
			// Store the energy of the Kth-Motif in kenergy
		}
		// return the energy ratio between K-Motifs and whole IMF
		double all = imf.normalizedEnergy(imf.maxValue());
		return kenergy / imf.normalizedEnergy(imf.maxValue());
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
