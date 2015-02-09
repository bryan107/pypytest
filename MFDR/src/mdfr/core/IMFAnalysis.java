package mdfr.core;

import java.util.LinkedList;

import mdfr.math.emd.datastructure.IMFs;

public class IMFAnalysis {
	IMFs imfs = new IMFs();
	double whitenoise, FTratio;
	
	/*
	 * Constructor
	 * */
	public IMFAnalysis(IMFs imfs, double whitenoise, double FTratio){
		setIMFS(imfs);
		setWhiteNoiseLevel(whitenoise);
		setFTRatio(FTratio);
	}
	
	public void setIMFS(IMFs imfs){
		if(imfs.size() == 0){throw new NullPointerException("IMFs is empty.");}
		this.imfs = imfs;
	}
	
	/*
	 * White noises discrimination functions
	 * */
	
	public void setWhiteNoiseLevel(double whitenoise){
		this.whitenoise = whitenoise;
	}
	
	public double getNoise(int level){
		double noise = 0;
		if(imfs.getIMF(level) == null){throw new NullPointerException("IMFs is empty.");}
		// TODO calculate noise;
		return noise;
	}	
	
	public LinkedList<Double> getAllNoise(){
		LinkedList<Double> noise = new LinkedList<Double>();
		for(int i = 0 ; i < imfs.size(); i++){
			noise.add(getNoise(i));
		}
		return noise;
	}
	
	public boolean isWhiteNoise(double noise){
		if(noise <= whitenoise)
			return true;
		else
			return false;
	}
	
	public boolean isWhiteNoise(int level){
		return isWhiteNoise(getNoise(level));
	}

	public boolean isSignal(int level){
		return !isWhiteNoise(getNoise(level));
	}
	
	/*
	 * Frequency/Trend discrimination functions
	 * */

	public void setFTRatio(double FTratio){
		this.FTratio = FTratio;
	}
	
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
