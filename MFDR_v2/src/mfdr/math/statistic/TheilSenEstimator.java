package mfdr.math.statistic;

import mfdr.dimensionality.reduction.PLA;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;

public class TheilSenEstimator implements LinearRegression {

	private double[][] datapoints;
	private static Log logger = LogFactory.getLog(TheilSenEstimator.class);

	public TheilSenEstimator(double[] x, double[] y) {
	}


	// private void calcLinear(double[] x, double[] y){
	//
	// }

	// TODO Test this function
	@Override
	public double[] getEstimates(double[] x, double[] y) {
		// Here save datapoints
				if (x.length == y.length) {
					this.datapoints = new double[x.length][2];
					for (int i = 0; i < x.length; i++) {
						this.datapoints[i][0] = x[i];
						this.datapoints[i][1] = y[i];
					}
				} else {
					logger.info("The sizes of input arraies do not match");
				}
		double[] coeff = new double[2]; 
		// Prepare Linear coeffs
		coeff[0] = datapoints[0][1];
		coeff[1] = Stat.median(getBruteForceSlopes());
		return coeff;
	}

	private double[] getBruteForceSlopes() {
		int num = datapoints.length * (datapoints.length - 1) / 2;
		double[] slopes = new double[num];
		// Get slopes
		int index = 0;
		for (int i = 0; i < datapoints.length - 1; i++) {
			for (int j = i + 1; j < datapoints.length; j++) {
				// if index fit array size
				if (index < num) {
					slopes[index] = (datapoints[j][1] - datapoints[i][1])
							/ (datapoints[j][0] - datapoints[i][0]);
				} else {
					logger.info("The sizes of slope arraies do not match");
				}
			}
		}
		return slopes;
	}

	// TODO implement fast Theil-Sen Estimator.
	// http://ac.els-cdn.com/S0925772197000254/1-s2.0-S0925772197000254-main.pdf?_tid=8f4af354-ec47-11e4-b854-00000aacb361&acdnat=1430075461_339437cbc48009ef708d9b591ff3791c
	public double[] getFastEstimates() {
		return null;
	}

}
