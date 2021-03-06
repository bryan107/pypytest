package mdfr.learning;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Regression;
import mdfr.learning.datastructure.TrainingSet;

public class LRAngleLearning extends AngleLearning {
	private static Log logger = LogFactory.getLog(LRAngleLearning.class);
	private double[] coeff;
	
	public LRAngleLearning(LinkedList<TrainingSet> ts) {
		trainingParameters(ts);
	}
	
	/*
	 * This class does not provide null input function.
	 * (non-Javadoc)
	 * @see mdfr.learning.Learning#getAngle()
	 */
	@Override
	public double getAngle(){
		logger.info("Please specify trendlength and freqlength");
		return 0;
	}
	
	@Override
	public double getAngle(double trendlength, double freqlength){
		return coeff[0] + coeff[1] * trendlength + coeff[2] * freqlength;
	}
	
	@Override
	public void trainingParameters(LinkedList<TrainingSet> ts) {
		double[][] traininginput = new double[2][ts.size()];
		double[] trainingoutput = new double[ts.size()];
		//Prepare training data structure
		for(int i = 0 ; i < ts.size() ; i++){
			traininginput[0][i] = ts.get(i).trendLength();
			traininginput[1][i]	= ts.get(i).freqLength();
			trainingoutput[i] = angleEquation(ts.get(i).trendLength(), ts.get(i).freqLength(), ts.get(i).originLength());
		}
		// multidimentional input Linear regression
		Regression reg = new Regression(traininginput, trainingoutput);
		reg.linear();
		this.coeff = reg.getBestEstimates();
	}
	
	public double[] getParameters(){
		if(this.coeff == null){
			logger.info("Please run traininParameters first");
		}
		return this.coeff;
	}

}
