package mdfr.core;

import mdfr.datastructure.TimeSeries;
import mdfr.math.emd.EMD;
import mdfr.math.emd.IMFAnalysis;
import mdfr.math.emd.datastructure.IMFS;

public class MFDRExicute {

	// ********  EMD Parameters *******
	// IMF Decomposition
	private double[] IFparamaters = {4,2,1}; 
	private double zerocrossingaccuracy = 0.0001;
	private final int MAXLEVEL = 10;
	
	// Parameters for Noise/Signal Analysis
	final double noise_whitenoiselevel = 5; // p-value 0.01
	final double noise_threshold = 6.2;

	// Parameters for Frequency/Trend Analysis
	final int motif_k = 2; 
	final double motif_threshold = 0.1;
	final double FTratio = 0.5;
	
	// ********************************
	
	public MFDRExicute(){
		
	}
	
	public void loadSettings(){
		
	}
	
	public void trainParameters(){
		
	}
	
	public void learnWindowSizes(TimeSeries ts){
		// Create EMD service object
		EMD emd = new EMD(ts, zerocrossingaccuracy, IFparamaters[0], IFparamaters[1], IFparamaters[2]);
		// Calculate IMF with EMD
		IMFS imfs = emd.getIMFs(MAXLEVEL);
		// Create IMF analysis object
		IMFAnalysis analysis = new IMFAnalysis(ts, imfs, noise_whitenoiselevel, noise_threshold, FTratio, motif_k, motif_threshold);
				
	}
	
	public void learnAngle(){
		
	}
	
	public void learnVariation(){
		
	}
	
	public void getDistance(TimeSeries ts1, TimeSeries ts2){
		
	}
	
}
