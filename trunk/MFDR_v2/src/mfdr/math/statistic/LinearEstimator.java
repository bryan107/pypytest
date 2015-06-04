package mfdr.math.statistic;

import flanagan.analysis.Regression;
import flanagan.interpolation.LinearInterpolation;

public class LinearEstimator implements LinearRegression {
	private Regression reg;
	
	public LinearEstimator() {
//		reg = new Regression(x, y);
	}
	
	@Override
	public double[] getEstimates(double[] x, double[] y) {
		if(x.length > 3){
			reg = new Regression(x, y);
			reg.linear();
			return reg.getBestEstimates();
		} else if(x.length > 1){
			double[] result = new double[2];
			double slope = 0;
			double xx = 0;
			double yy = 0;
			for(int i = 0 ; i < x.length ; i++){
				xx += x[i];
				yy += y[i];
				if(i < x.length-1)
					slope += (y[i+1]-y[i])/(x[i+1]-x[i]);
			}
			result[0] = yy/y.length - (slope/x.length * xx)/x.length;
			result[1] = slope/x.length;
			return result;
		}
		else{
			double[] result = new double[2];
			result[0] = y[0];
			result[1] = 0;
			return result;
		}
	}


}
