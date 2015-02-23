package mdfr.core;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mdfr.math.emd.datastructure.IMF;
import mdfr.math.emd.datastructure.IMF_BAK;
import mdfr.math.emd.datastructure.IMFs_BAK;
import mdfr.math.statistic.StatisticalBounds;
import mdfr.math.statistic.StatisticalBoundsWhiteNoise;
import mdfr.math.statistic.StatisticalProperty;

public class IMFAnalysis {
	
	private static Log logger = LogFactory.getLog(IMFAnalysis.class);
	IMFs_BAK imfs = new IMFs_BAK();
	double whitenoiselevel, FTratio, t_threshold;
	
	/*
	 * Constructor
	 * */
	public IMFAnalysis(double whitenoiselevel, double FTratio, double t_threshold){
		setWhiteNoiseLevel(whitenoiselevel);
		setFTRatio(FTratio);
		setTThreshold(t_threshold);
	}
	
	
	/*
	 * Private parameters
	 */
	public void setWhiteNoiseLevel(double whitenoiselevel){
		this.whitenoiselevel = whitenoiselevel;
	}
	
	public void setFTRatio(double FTratio){
		this.FTratio = FTratio;
	}
	
	public void setTThreshold(double t_threshold){
		this.t_threshold = t_threshold;
	}
	
	/*
	 * White noises discrimination functions
	 * */
	

	public boolean isSignal(IMF imf){
		double averagewavelength = imf.averageWavelength();
		System.out.println("Length: " + averagewavelength);
//		double energydensity = imf.normalizedEnergyDensity();
		double energydensity = imf.energyDensity();
		logger.info("Engergy Density: " + energydensity);
		StatisticalBounds sb = new StatisticalBoundsWhiteNoise(whitenoiselevel, imf.size());
		if(averagewavelength >= imf.size()){
			logger.info("No Instant Frequency exist for this IMF");
			return true;
		}else if(StatisticalProperty.getInstance().isStatisticalSignificance(sb, averagewavelength, energydensity, t_threshold)){
			return true;
		}else{
			return false;
		}
	}

	public boolean isWhiteNoise(IMF imf){
		return !isSignal(imf);
	}

	
	/*
	 * Frequency/Trend discrimination functions
	 * */


	public double getFTRatio(int level){
		double FTratio = 0;
		// TODO implement auto-correlation-based FT detect scheme.
		return FTratio;
	}
	
	public LinkedList<Double> getAllFTRatio(){
		LinkedList<Double> FTratio = new LinkedList<Double>();
		for(int i = 0 ; i < imfs.size(); i++){
			FTratio.add(getFTRatio(i));
		}
		return FTratio;
	}

	public boolean isFreqLevel(double FTratio){
		if(FTratio >= this.FTratio)
			return true;
		else
			return false;
	}
	
	public boolean isFreqLevel(int level){
		return isFreqLevel(getFTRatio(level));
	}
	
}
