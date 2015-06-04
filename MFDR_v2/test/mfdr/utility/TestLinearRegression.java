package mfdr.utility;

import mfdr.math.statistic.LinearEstimator;
import mfdr.math.statistic.LinearRegression;
import flanagan.analysis.Regression;
import junit.framework.TestCase;

public class TestLinearRegression extends TestCase {
	
	public void testee(){
		double[] x = {1,3};
		double[] y = {0.07,2.38};
//		double[] y = {0.07,1.52,2.238};
		LinearRegression lr = new LinearEstimator();
		double[] es = lr.getEstimates(x, y);
		
		System.out.println("GG");
	}
}
