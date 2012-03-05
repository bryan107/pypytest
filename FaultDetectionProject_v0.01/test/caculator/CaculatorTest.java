package caculator;

import faultDetection.tools.Caculator;
import flanagan.analysis.Regression;
import junit.framework.TestCase;

public class CaculatorTest extends TestCase {
	double[] x = {50,100,100,1,1};
	double[] y = {50,50,50,50,50};
	
	public void testCaculatorRegression(){
		Caculator calc = new Caculator();
	
		Regression reg = new Regression(x, y);
		reg.linearGeneral();
		
		assertEquals(calc.getRegressionSlope(x, y), reg.getBestEstimates()[0]);
		System.out.println("Slope: " + calc.getRegressionSlope(x, y));
		assertEquals(calc.getRegressionError(x, y), reg.getBestEstimatesErrors()[0]);
		System.out.println("Error: " + calc.getRegressionError(x, y));
		
		System.out.println("Regression Test Finished");
	}
	
	
	
}
