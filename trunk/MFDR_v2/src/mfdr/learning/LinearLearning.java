package mfdr.learning;

import java.util.LinkedList;

import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.distance.Distance;
import mfdr.learning.datastructure.TrainingSet;

//TODO create a validation class to choose learning model: Either constant or LR
public abstract class LinearLearning {

	/**
	 * Get the Learning Type Name
	 * @return
	 */
	public abstract String getType();
	
	/**
	 * Train and learn the angle with TrainingSets
	 * @return
	 */
	public abstract LinearLearningResults trainingParameters(LinkedList<TrainingSet> ts);
	
	
	public abstract LinearLearningResults trainingParameters(TimeSeries[] ts , MFDR mfdr, Distance d);
	

	public abstract LinearLearningResults trainingParameters(LinkedList<TimeSeries> ts,	MFDR mfdr, Distance d);
	
		
	public abstract LinkedList<TrainingSet> getTrainingSet(TimeSeries[] ts, MFDR mfdr, Distance d);
	
	public abstract LinkedList<TrainingSet> getTrainingSet(LinkedList<TimeSeries> ts, MFDR mfdr, Distance d);
	
	/*
	 * Theorem of triangulation:
	 * angle = arccos((a^2+b^2-c^2)/2ab)
	 */
	protected double angleEquation(double a, double b, double c){
		double numerator = Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2);
		double denominator = 2*a*b;
		double angle = Math.acos(numerator/denominator);
		return  angle;
	}

	
	
}
