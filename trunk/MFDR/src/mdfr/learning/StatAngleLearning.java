package mdfr.learning;

import java.util.LinkedList;

import flanagan.analysis.Stat;
import mdfr.learning.datastructure.TrainingSet;

public class StatAngleLearning extends AngleLearning {

	private double angle;
	
	@Override
	public double getAngle() {
		return this.angle;
	}

	@Override
	public double getAngle(double trendlength, double freqlength) {
		return getAngle();
	}

	/*
	 * This function use simple median as learning result.
	 * (non-Javadoc)
	 * @see mdfr.learning.Learning#trainingParameters(java.util.LinkedList)
	 */
	
	@Override
	public void trainingParameters(LinkedList<TrainingSet> ts) {
		double[] angles = new double[ts.size()];
		for(int i = 0 ; i < ts.size() ; i++){
			angles[i] = angleEquation(ts.get(i).trendLength(), ts.get(i).freqLength(), ts.get(i).originLength());
		}
		this.angle = Stat.median(angles);
	}

}
