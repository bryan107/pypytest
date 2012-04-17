package caculator;

import faultDetection.tools.Calculator;
import flanagan.analysis.Regression;
import junit.framework.TestCase;

public class CaculatorTest extends TestCase {
	double[] x = {50,100,100,1,1};
	double[] y = {50,50,50,50,50};
	
	public void testCaculatorRegression(){
		Calculator calc = new Calculator();
	
		Regression reg = new Regression(x, y);
		reg.linearGeneral();
		
		assertEquals(calc.getRegressionSlope(x, y), reg.getBestEstimates()[0]);
		System.out.println("Slope: " + calc.getRegressionSlope(x, y));
		assertEquals(calc.getRegressionError(x, y), reg.getBestEstimatesErrors()[0]);
		System.out.println("Error: " + calc.getRegressionError(x, y));
		
		System.out.println("Regression Test Finished");
	}
	
	
	public void testCaculatorCorrelation(){
		System.out.println(Calculator.getInstance().correlationStrength(1.09, 1, 2));
		System.out.println(Calculator.getInstance().correlationStrength(0.65, 1, 1.2));
	}
}
