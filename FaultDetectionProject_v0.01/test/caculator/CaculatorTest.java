package caculator;

import faultDetection.tools.Calculator;
import flanagan.analysis.Regression;
import junit.framework.TestCase;

public class CaculatorTest extends TestCase {
	double[] x = {20.1,20.2,20.3,20.4,20.5};
	double[] y = {20.3,20.8,20.6,20.7,20.1};
	
	public void testCaculatorRegression(){
		Calculator calc = new Calculator();
	
		Regression reg = new Regression(x, y);
		reg.linearGeneral();
		System.out.println("Regression Test:");
		assertEquals(calc.getRegressionSlope(x, y), reg.getBestEstimates()[0]);
		System.out.println("Slope: " + calc.getRegressionSlope(x, y));
		assertEquals(calc.getRegressionError(x, y), reg.getBestEstimatesErrors()[0]);
		System.out.println("Error: " + calc.getRegressionError(x, y));
		
		System.out.println("Theil-Sen Regression Test:");
		System.out.println("Slope:" + Calculator.getInstance().getTheilSenRegressionSlope(x, y));
	}
	
	
	public void testCaculatorCorrelation(){
		System.out.println(Calculator.getInstance().correlationStrength(1.09, 1, 2));
		System.out.println(Calculator.getInstance().correlationStrength(0.65, 1, 1.2));
	}
}
