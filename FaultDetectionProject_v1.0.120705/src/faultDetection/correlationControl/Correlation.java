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

	public double getCorrelation() {
		synchronized (Calculator.getInstance()) {
			if (mapenable == true) {
				return Calculator.getInstance().getRegressionSlope(pair[0],
						pair[1]);
				// int regressiontype =
				// Integer.valueOf(PropertyAgent.getInstance().getProperties("FDCservice",
				// "RegressionType"));
				// if(regressiontype == 0)//Use General Linear Regression
				// return Calculator.getInstance().getRegressionSlope(pair[0],
				// pair[1]);
				// else if(regressiontype == 1)//Use Theil-Sen estimator based
				// Linear Regression
				// return
				// Calculator.getInstance().getTheilSenRegressionSlope(pair[0],
				// pair[1]);
				// else
				// logger.error("Invalid Regression Type");
				// return 0;
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
