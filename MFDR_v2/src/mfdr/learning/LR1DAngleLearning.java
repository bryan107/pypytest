package mfdr.learning;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Regression;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.distance.Distance;
import mfdr.learning.datastructure.TrainingSet;

public class LR1DAngleLearning extends LinearLearning {
	private static Log logger = LogFactory.getLog(LR1DAngleLearning.class);
	private double angle;
	
	public LR1DAngleLearning(LinkedList<TrainingSet> ts) {
		trainingParameters(ts);
	}
	
	@Override
	public String getType() {
		logger.info("Using 1 dimentional LR Angle Learning");
		return "LR3DAngle";
	}

	@Override
	public LinearLearningResults trainingParameters(LinkedList<TrainingSet> ts) {
		return null;
		// TODO Use AR model.
		// This is can be extended to stream version with ARIMA model
	}

	@Override
	public LinearLearningResults trainingParameters(TimeSeries[] ts, MFDRWave mfdr, Distance d) {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedList<TrainingSet> getTrainingSet(TimeSeries[] ts , MFDRWave mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public LinkedList<TrainingSet> getTrainingSet(LinkedList<TimeSeries> ts,
			MFDRWave mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinearLearningResults trainingParameters(LinkedList<TimeSeries> ts,
			MFDRWave mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}

}
