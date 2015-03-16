package mdfr.dimensionality.reduction;

import flanagan.analysis.Regression;
import junit.framework.TestCase;

public class TestTools extends TestCase {

	public void testRegression(){
		double[] x = new double[100];
		double[] y = new double[100];
		// Prepare data
		for(int i = 0 ; i < 100 ; i++){
			x[i] = i;
			y[i] = i - 2 + Math.random()*4;
		}
		Regression reg = new Regression(x, y);
		reg.linear();
		double[] e = reg.getBestEstimates();
		System.out.println(e.length);
	}
	
}
