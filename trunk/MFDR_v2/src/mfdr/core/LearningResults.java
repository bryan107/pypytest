package mfdr.core;

import mfdr.learning.LinearLearningResults;

public class LearningResults {
	final private LinearLearningResults lresults;
	final private double guaranteed_compensation;
	
	public LearningResults(LinearLearningResults lresults, double guaranteed_compensation){
		this.lresults = lresults;
		this.guaranteed_compensation = guaranteed_compensation;
	}
	
	public LinearLearningResults combinationWeights(){
		return this.lresults;
	}
	
	public double compensation(){
		return this.guaranteed_compensation;
	}
}
