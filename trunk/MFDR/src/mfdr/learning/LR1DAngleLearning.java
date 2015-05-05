package mfdr.learning;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Regression;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.distance.Distance;
import mfdr.learning.datastructure.TrainingSet;

public class LR1DAngleLearning extends AngleLearning {
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
	public double getAngle() {
		return angle;
	}

	@Override
	public double getAngle(double trendlength, double freqlength) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void trainingParameters(LinkedList<TrainingSet> ts) {
		// TODO Use AR model.
		// This is can be extended to stream version with ARIMA model
	}

	@Override
	public void trainingParameters(TimeSeries[] ts, MFDR mfdr, Distance d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedList<TrainingSet> getTrainingSet(TimeSeries[] ts , MFDR mfdr, Distance d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
