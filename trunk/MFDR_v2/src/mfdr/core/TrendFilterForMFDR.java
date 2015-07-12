package mfdr.core;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DFTForMFDR;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.dimensionality.reduction.PLA;
import mfdr.math.emd.utility.DataListCalculator;

/**
 * FREQUENCY/TREND DISCRIMINATION FUNCTIONS
 * 
 * The functions are implemented with the consideration of Energy containing in
 * the K-Motifs If the energy of an IMF concentrates in certain number of motifs
 * (which repeat recursively), The given IMF is considered as a frequency
 * intense signal; otherwise trend intense.
 **/

public class TrendFilterForMFDR {
	/**
	 * Use this constructor for old K 1-motif solution
	 */
	private static Log logger = LogFactory.getLog(TrendFilterForMFDR.class);

	public TrendFilterForMFDR() {

	}
	
	/**
	 * This function uses a brute force algorithm to calculate Candidate trend NoC and Seasonal NoC.
	 * @param ts
	 * @param NoC
	 * @param lowestperiod
	 * @return
	 */
	public int[] getMFDRNoCs(TimeSeries ts, int NoC, double lowestperiod){
		int[] candidateNoCs = {0 , NoC};
		MFDR mfdr = new MFDR(candidateNoCs[0], candidateNoCs[1]);
		TimeSeries residual = DataListCalculator.getInstance().getDifference(ts, mfdr.getFullResolutionDR(ts));
		double candidateError = residual.energyDensity();
//		/*                  */
//		PLA pla = new PLA(2);
//		DFT dft = new DFT(2);
//		DFTForMFDR dft2 = new DFTForMFDR(2);
//		
//		double errorpla = DataListCalculator.getInstance().getDifference(ts, pla.getFullResolutionDR(ts)).energyDensity();
//		double errordft = DataListCalculator.getInstance().getDifference(ts, dft.getFullResolutionDR(ts)).energyDensity();
//		double errordft2= DataListCalculator.getInstance().getDifference(ts, dft2.getFullResolutionDR(ts)).energyDensity();
//		/*                  */
		for(int NoC_t = 1 ; NoC_t <=NoC ; NoC_t++){
			int NoC_s = NoC - NoC_t;
			mfdr = new MFDR(NoC_t, NoC_s);
			residual = DataListCalculator.getInstance().getDifference(ts, mfdr.getFullResolutionDR(ts));
			double error = residual.energyDensity();
			if(error < candidateError ){
				candidateError = error;
				candidateNoCs[0] = NoC_t;
				candidateNoCs[1] = NoC_s;
			} 
		}
		return candidateNoCs;
	}
	
	
}
