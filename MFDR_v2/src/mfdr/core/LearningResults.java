package mfdr.core;

import mfdr.learning.AngleLearning;
import mfdr.learning.VarienceLearning;

public class LearningResults {
	final private AngleLearning alearn;
	final private VarienceLearning vlearn;
	
	public LearningResults(AngleLearning alearn, VarienceLearning vlearn){
		this.alearn = alearn;
		this.vlearn = vlearn;
	}
	
	public AngleLearning alearn(){
		return alearn;
	}
	
	public VarienceLearning vlearn(){
		return vlearn;
	}
}
