package mfdr.learning;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Regression;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.distance.Distance;
import mfdr.learning.datastructure.TrainingSet;

public class LR3DAngleLearning extends AngleLearning {
	private static Log logger = LogFactory.getLog(LR3DAngleLearning.class);
	private double[] coeff;

	
	public LR3DAngleLearning() {
	}
	
//	public LR3DAngleLearning(LinkedList<TrainingSet> ts, Distance d) {
//		trainingParameters(ts);
//	}
//	
//	public LR3DAngleLearning(TimeSeries[] ts, MFDR mfdr, Distance d) {
//		trainingParameters(ts, mfdr);
//	}

	@Override
	public String getType() {
		logger.info("Using 3 dimentional LR Angle Learning");
		return "LR3DAngle";
	}

	/*
	 * This class does not provide null input function. (non-Javadoc)
	 * 
	 * @see mdfr.learning.Learning#getAngle()
	 */
	@Override
	public double getAngle() {
		logger.info("Please specify trendlength and freqlength");
		return 0;
	}

	@Override
	public double getAngle(double trendlength, double freqlength) {
		return coeff[0] + coeff[1] * trendlength + coeff[2] * freqlength;
	}

	@Override
	public void trainingParameters(TimeSeries[] ts, MFDR mfdr, Distance d) {
		LinkedList<TrainingSet> trainingset = getTrainingSet(ts, mfdr, d);
		trainingParameters(trainingset);
	}

	@Override
	public void trainingParameters(LinkedList<TrainingSet> ts) {
		double[][] traininginput = new double[2][ts.size()];
		double[] trainingoutput = new double[ts.size()];
		// Prepare training data structure
		for (int i = 0; i < ts.size(); i++) {
			traininginput[0][i] = ts.get(i).trendDist();
			traininginput[1][i] = ts.get(i).freqDist();
			trainingoutput[i] = ts.get(i).originDist();
//			trainingoutput[i] = angleEquation(ts.get(i).trendDist(), ts.get(i)
//					.freqDist(), ts.get(i).originDist());
		}
		// multidimentional input Linear regression
		Regression reg = new Regression(traininginput, trainingoutput);
		reg.linear();
		this.coeff = reg.getBestEstimates();
	}

	@Override
	public LinkedList<TrainingSet> getTrainingSet(TimeSeries[] ts, MFDR mfdr, Distance d) {
		LinkedList<TrainingSet> trainingset = new LinkedList<TrainingSet>();
		for (int i = 0; i < ts.length - 1; i++) {
			for(int j = i+1 ; j < ts.length ; j++){
				TimeSeries t1 = mfdr.getTrend(ts[i]);
				TimeSeries t2 = mfdr.getTrend(ts[j]);
				double trenddist = d.calDistance(t1, t2, ts[i]);
				double freqdist = d.calDistance(mfdr.getResidual(ts[i]), mfdr.getResidual(ts[j]), ts[i]);
				double oridist = d.calDistance(ts[i], ts[j], ts[i]);
				trainingset.add(new TrainingSet(trenddist, freqdist, oridist));
			}
		}
		return trainingset;
	}

	public double[] getParameters() {
		if (this.coeff == null) {
			logger.info("Please run traininParameters first");
		}
		return this.coeff;
	}

}
