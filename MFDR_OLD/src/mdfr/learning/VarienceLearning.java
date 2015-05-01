package mdfr.learning;

import java.util.LinkedList;

import mdfr.learning.datastructure.TrainingSet;
import flanagan.analysis.Stat;

public class VarienceLearning {

	private double tolerancevarience;
	private Stat stat;

	/**
	 * This constructor is comparable with static angle.
	 * 
	 * @param trainingset
	 * @param angle
	 * @param tolerancevarience
	 */
	public VarienceLearning(LinkedList<TrainingSet> trainingset, double angle,
			double tolerancevarience) {
		updateToleranceVarience(tolerancevarience);
		updateTrainingData(trainingset, angle);
	}

	/**
	 * This constructor is comparable with linear regression learning.
	 * 
	 * @param trainingset
	 * @param alearn
	 * @param tolerancevarience
	 */
	public VarienceLearning(LinkedList<TrainingSet> trainingset,
			LRAngleLearning alearn, double tolerancevarience) {
		updateToleranceVarience(tolerancevarience);
		updateTrainingData(trainingset, alearn);
	}

	public void updateToleranceVarience(double tolerancevarience) {
		this.tolerancevarience = tolerancevarience;
	}

	/*
	 * This function serve static angle solution.
	 */
	public void updateTrainingData(LinkedList<TrainingSet> trainingset,
			double angle) {
		double[] error = new double[trainingset.size()];
		for (int i = 0; i < trainingset.size(); i++) {
			// Here is error is defined as the differences with regard of the
			// original distance..
			double computed = edgeEquation(trainingset.get(i).trendLength(),
					trainingset.get(i).freqLength(), angle);
			double original = trainingset.get(i).originLength();
			error[i] = computed - original;
		}
		stat = new Stat(error);
	}
	
	/*
	 * This function serve static angle solution.
	 */
	public void updateTrainingData(LinkedList<TrainingSet> trainingset,
			LRAngleLearning alearn) {
		double[] error = new double[trainingset.size()];
		for (int i = 0; i < trainingset.size(); i++) {
			// Here is error is defined as the differences with regard of the
			// original distance..
			double computed = edgeEquation(trainingset.get(i).trendLength(),
					trainingset.get(i).freqLength(), alearn.getAngle(
							trainingset.get(i).trendLength(), trainingset
									.get(i).freqLength()));
			double original = trainingset.get(i).originLength();
			error[i] = computed - original;
		}
		stat = new Stat(error);
	}

	private double edgeEquation(double a, double b, double angle) {
		return Math.pow(
				Math.pow(a, 2) + Math.pow(b, 2) + -2 * a * b * Math.cos(angle),
				0.5);
	}

	public double toleranceVarience() {
		return this.tolerancevarience;
	}

	public double getStandardError() {
		return stat.standardError();
	}

	public double getStandardDeviation() {
		return stat.standardDeviation();
	}

	public double getMean() {
		return stat.mean();
	}

	/**
	 * This is the main function that provide a value that guaranteed the value
	 * of MFDR that has to be deducted. Directly add it to MFDR raw results
	 */
	public double getGuaranteedCompensation() {
		return getMean() + this.tolerancevarience * getStandardDeviation();
	}

	/**
	 * Return fianl MFDR values corrected with guaranteed compensation.
	 * 
	 * @param MFDRvalue
	 * @return
	 */
	public double getGuaranteedCompensation(double MFDRvalue) {
		return MFDRvalue - getGuaranteedCompensation();
	}

}
