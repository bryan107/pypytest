package mfdr.core;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.distance.Distance;
import mfdr.math.emd.datastructure.IMF;
import mfdr.math.emd.datastructure.IMFS;
import mfdr.math.emd.utility.DataListCalculator;
import mfdr.math.motif.Motif;

/**
 * FREQUENCY/TREND DISCRIMINATION FUNCTIONS
 * 
 * The functions are implemented with the consideration of Energy containing in
 * the K-Motifs If the energy of an IMF concentrates in certain number of motifs
 * (which repeat recursively), The given IMF is considered as a frequency
 * intense signal; otherwise trend intense.
 **/

public class TrendFilterForMFDRWave {
	/**
	 * Use this constructor for old K 1-motif solution
	 */
	private static Log logger = LogFactory.getLog(TrendFilterForMFDRWave.class);

	public TrendFilterForMFDRWave() {

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
		MFDRWave mfdr = new MFDRWave(candidateNoCs[0], candidateNoCs[1]);
		TimeSeries residual = DataListCalculator.getInstance().getDifference(ts, mfdr.getFullResolutionDR(ts));
		double candidateError = residual.energyDensity();
		for(int NoC_t = 1 ; NoC_t <=NoC ; NoC_t++){
			int NoC_s = NoC - NoC_t;
			mfdr = new MFDRWave(NoC_t, NoC_s);
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
