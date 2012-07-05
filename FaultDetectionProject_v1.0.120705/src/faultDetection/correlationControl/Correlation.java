package faultDetection.correlationControl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import faultDetection.tools.Calculator;
import fileAccessInterface.PropertyAgent;

public class Correlation {
	private double[][] pair;
	private int mappointer;
	private int samplesize; // Max number of pairs
	private boolean mapenable; // Fulfill the requirement
	private static Log logger = LogFactory.getLog(Correlation.class);

	public Correlation(int samplesize) {
		resetCorrelation(samplesize);
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
			return;
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

	public double getCorrelation(int regressiontype, double maxtolerableerror) {
		double[] slopes;
		//TODO Not yet test
		synchronized (Calculator.getInstance()) {
			if (mapenable == true) {
				switch(regressiontype){
				case 0://original linear regression
					return Calculator.getInstance().getRegressionSlope(pair[0],pair[1]);
				case 1://quantile regression + Expected value
					slopes = Calculator.getInstance().getQuantileArray(pair[0], pair[1], maxtolerableerror);
					return Calculator.getInstance().getaverage(slopes);
				case 2://quantile regression + Median
					slopes = Calculator.getInstance().getQuantileArray(pair[0], pair[1], maxtolerableerror);
					return Calculator.getInstance().getMedian(slopes);
				case 3://quantile regression + original linear regression
					//TODO Calculator's quantile + least square regression
//					return
				default:
					logger.error("Invalid Regression Type");
					return 0;
				}
			} else {
//				logger.warn("Not enough inputs for getCorrelation");
				return 0;
			}
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
