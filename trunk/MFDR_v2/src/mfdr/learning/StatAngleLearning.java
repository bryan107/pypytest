package mfdr.learning;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.distance.Distance;
import mfdr.learning.datastructure.TrainingSet;

public class StatAngleLearning extends LinearLearning {
	private static Log logger = LogFactory.getLog(StatAngleLearning.class);

	@Override
	public String getType() {
		logger.info("Using Static Angle Learning");
		return "StatAngle";
	}

	/*
	 * This function use simple median as learning result.
	 * (non-Javadoc)
	 * @see mdfr.learning.Learning#trainingParameters(java.util.LinkedList)
	 */
	
	@Override
	public LinearLearningResults trainingParameters(LinkedList<TrainingSet> ts) {
		double[] angles = new double[ts.size()];
//		for(int i = 0 ; i < ts.size() ; i++){
//			angles[i] = angleEquation(ts.get(i).trendDist(), ts.get(i).freqDist(), ts.get(i).originDist());
//		}
		return null;
	}

	@Override
	public LinearLearningResults trainingParameters(TimeSeries[] ts , MFDR mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<TrainingSet> getTrainingSet(TimeSeries[] ts,MFDR mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public LinkedList<TrainingSet> getTrainingSet(LinkedList<TimeSeries> ts,
			MFDR mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinearLearningResults trainingParameters(LinkedList<TimeSeries> ts,
			MFDR mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}

}
