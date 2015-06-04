package mfdr.learning;

import java.util.LinkedList;

import mfdr.learning.datastructure.TrainingSet;
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
//	public VarienceLearning(LinkedList<TrainingSet> trainingset, double angle,
//			double tolerancevarience) {
//		updateToleranceVarience(tolerancevarience);
//		updateTrainingData(trainingset, angle);
//	}

	/**
	 * This constructor is comparable with linear regression learning.
	 * 
	 * @param trainingset
	 * @param alearn
	 * @param tolerancevarience
	 */
	public VarienceLearning(LinkedList<TrainingSet> trainingset,
			AngleLearning alearn, double tolerancevarience) {
		updateToleranceVarience(tolerancevarience);
		updateTrainingData(trainingset, alearn);
	}

	public void updateToleranceVarience(double tolerancevarience) {
		this.tolerancevarience = tolerancevarience;
	}

	/*
	 * This function serve static angle solution.
	 */
//	public void updateTrainingData(LinkedList<TrainingSet> trainingset,
//			double angle) {
//		double[] error = new double[trainingset.size()];
//		for (int i = 0; i < trainingset.size(); i++) {
//			// Here is error is defined as the differences with regard of the
//			// original distance..
//			double computed = edgeEquation(trainingset.get(i).trendDist(),
//					trainingset.get(i).freqDist(), angle);
//			double original = trainingset.get(i).originDist();
//			error[i] = computed - original;
//		}
//		stat = new Stat(error);
//	}
	
	/*
	 * This function serve static angle solution.
	 */
	public void updateTrainingData(LinkedList<TrainingSet> trainingset,
			AngleLearning alearn) {
		double[] error = new double[trainingset.size()];
		for (int i = 0; i < trainingset.size(); i++) {
			// Here is error is defined as the differences with regard of the
			// original distance..
			
			/*
			 * Codes here are for the direct learning version.
			 */
			double computed = alearn.getParameters()[0] + alearn.getParameters()[1]*trainingset.get(i).trendDist() + alearn.getParameters()[2]*trainingset.get(i).freqDist();
			/*
			 * Codes here are for the angle version.
			 */
//			double computed = edgeEquation(trainingset.get(i).trendDist(),
//					trainingset.get(i).freqDist(), alearn.getAngle(
//							trainingset.get(i).trendDist(), trainingset
//									.get(i).freqDist()));
			double original = trainingset.get(i).originDist();
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
		double mean = getMean();
		double standarddeviation = getStandardDeviation();
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
