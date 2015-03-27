package mdfr.learning;

import java.util.LinkedList;

import mdfr.learning.datastructure.TrainingSet;

public abstract class Learning {

	/**
	 * Get angle
	 * @return
	 */
	public abstract double getAngle();

	/**
	 * Get angle with respect to the given inputs
	 * @param trendlength
	 * @param freqlength
	 * @return
	 */
	public abstract double getAngle(double trendlength, double freqlength);
	
	/**
	 * Train and learn the angle with TrainingSets
	 * @return
	 */
	public abstract void trainingParameters(LinkedList<TrainingSet> ts);
	
	/*
	 * Theorem of triangulation:
	 * angle = arccos((a^2+b^2-c^2)/2ab)
	 */
	protected double angleEquation(double a, double b, double c){
		double numerator = Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2);
		double denominator = 2*a*b;
		return  Math.acos(numerator/denominator);
	}
	
}
