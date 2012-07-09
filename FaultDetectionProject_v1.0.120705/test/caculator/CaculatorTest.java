package caculator;

import faultDetection.tools.Calculator;
import flanagan.analysis.Regression;
import junit.framework.TestCase;

public class CaculatorTest extends TestCase {
	double[] x = {20.1,20.2,20.3,20.4,20.5};
	double[] y = {20.3,20.8,20.6,20.7,20.1};
	double[] z = {1,2,3,10,10,9,8,4,5,7};
	public void testCaculatorRegression(){
	
		Regression reg = new Regression(x, y);
		reg.linearGeneral();
		System.out.println();
		System.out.println("Regression Test:");
		assertEquals(Calculator.getInstance().getRegressionSlope(x, y), reg.getBestEstimates()[0]);
		System.out.println("Slope: " + Calculator.getInstance().getRegressionSlope(x, y));
		assertEquals(Calculator.getInstance().getRegressionError(x, y), reg.getBestEstimatesErrors()[0]);
		System.out.println("Error: " + Calculator.getInstance().getRegressionError(x, y));
		
		System.out.println("Theil-Sen Regression: " + Calculator.getInstance().getTheilSenRegressionSlope(x, y));
		
		System.out.println("Median: " + Calculator.getInstance().getMedian(z));

		System.out.println("Average: " + Calculator.getInstance().getaverage(z));
		
		
	}
	
	
	public void testCaculatorCorrelationStrength(){
		System.out.println();
		System.out.println("Correlaion Strength Tests:");
		System.out.println(Calculator.getInstance().correlationStrength(1.09, 1, 2));
		System.out.println(Calculator.getInstance().correlationStrength(0.65, 1, 1.2));
	}
}
