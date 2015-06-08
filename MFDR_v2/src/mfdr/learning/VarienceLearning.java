package mfdr.learning;

import java.util.LinkedList;

import mfdr.learning.datastructure.TrainingSet;
import flanagan.analysis.Stat;

public class VarienceLearning {

	private double guaranteed_compensation;
	
	/**
	 * This constructor is comparable with linear regression learning.
	 * 
	 * @param trainingset
	 * @param alearn
	 * @param tolerancevarience
	 */
	public VarienceLearning() {

	}

	/**
	 * This is the main function that provide a value that guaranteed the value
	 * of MFDR that has to be deducted. Directly add it to MFDR raw results
	 */
	public double getGuaranteedCompensation(LinkedList<TrainingSet> trainingset,
			LinearLearningResults lresults, double tolerancevarience) {
		Stat stat =  updateTrainingData(trainingset, lresults);
		return stat.mean() + tolerancevarience * stat.standardDeviation();
	}

	/**
	 * Return fianl MFDR values corrected with guaranteed compensation.
	 * 
	 * @param MFDRvalue
	 * @return
	 */
	public double getGuaranteedCompensation(double MFDRvalue) {
		return MFDRvalue - this.guaranteed_compensation;
	}

	private Stat updateTrainingData(LinkedList<TrainingSet> trainingset,
			LinearLearningResults lresults) {
		double[] error = new double[trainingset.size()];
		for (int i = 0; i < trainingset.size(); i++) {
			// Here is error is defined as the differences with regard of the
			// original distance..

			/*
			 * Codes here are for the direct learning version.
			 */
			double computed = lresults.constant()
					+ lresults.trendWeight() * trainingset.get(i).trendDist()
					+ lresults.seasonalWeight() * trainingset.get(i).seasonalDist()
					+ lresults.noiseWeight() * trainingset.get(i).noiseDist();
			/*
			 * Codes here are for the angle version.
			 */
			// double computed = edgeEquation(trainingset.get(i).trendDist(),
			// trainingset.get(i).freqDist(), alearn.getAngle(
			// trainingset.get(i).trendDist(), trainingset
			// .get(i).freqDist()));
			double original = trainingset.get(i).originalDist();
			error[i] = computed - original;
		}
		return new Stat(error);
	}

	
}
