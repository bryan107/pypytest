package faultDetection.correlationControl;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import faultDetection.tools.Calculator;
import faultDetection.tools.PairedReading;
import faultDetection.tools.RegressionEstimator;
import fileAccessInterface.PropertyAgent;

public class Correlation {
	private double[][] pair;
	private int mappointer;
	private int samplesize; // Max number of pairs
	private boolean mapenable; // Fulfill the requirement
	private static Log logger = LogFactory.getLog(Correlation.class);
	private RegressionEstimator regressionestimator;
	private double maxtolerableerror;

	public Correlation(int samplesize, RegressionEstimator regressionestimator, double maxtolerableerror) {
		resetCorrelation(samplesize);
		updateRegressionEstimator(regressionestimator);
		updateMaxTolerableError(maxtolerableerror);
	}

	public void updateMaxTolerableError(double maxtolerableerror){
		this.maxtolerableerror = maxtolerableerror;
	}
	
	public void updateRegressionEstimator(RegressionEstimator regressionestimator){
		this.regressionestimator = regressionestimator;
	}
	
	public void resetCorrelation(int samplesize) {
		mappointer = 0;
		mapenable = false;
		this.samplesize = samplesize;
		pair = new double[2][this.samplesize];
	}

	public void addPair(double x, double y) {
		
		pair[0][mappointer] = x;
		pair[1][mappointer] = y;
		mappointer++;
		if (mappointer < samplesize) {
//			TODO !!!!!!!!!!
//			return;
		} else {
			if (mapenable == false) {
				mapenable = true;
			}
			mappointer = 0;
		}
	}

	public double[][] getPair() {
		if (mapenable == true) {
			double[][] result = new double[samplesize][2];
			for (int i = 0; i < samplesize; i++) {
				result[i][0] = pair[0][i];
				result[i][1] = pair[1][i];
			}
			return result;
		} else {
			logger.warn("Not enough inputs for data retreaving");
			return (double[][]) null;
		}

	}

	// Original linear regression
	public double getEstimatedCorrelation() {
		if (mapenable == true) {
			return regressionestimator.getEstimatedValue(pair[0], pair[1], maxtolerableerror);
		}
		else{
			 logger.warn("Not enough inputs for getCorrelation");
			return 0;
		}
	}

	public double getCorrelationError() {
		if (mapenable == true) {
			return Calculator.getInstance()
					.getRegressionError(pair[0], pair[1]);
		} else {
			logger.warn("Not enough inputs for getCorrelationError");
			return 0;
		}

	}

}
