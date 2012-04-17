package correlationControl;

import faultDetection.correlationControl.Correlation;
import faultDetection.tools.Calculator;
import junit.framework.TestCase;

public class CorrelationTest extends TestCase {
	double[] inputX = {20,20,20,20};
	double[] inputY = {20,19,19,20};

	public void testCorrelation(){
		double[][] input = new double[4][2];
		for (int i = 0 ; i < 4 ; i ++){
			input[i][0] = inputX[i];
			input[i][1] = inputY[i];
		}
		Correlation co1 = new Correlation(5);
		for(int i = 0 ; i < 4 ; i ++){
			co1.addPair(input[i][0], input[i][1]);
		}
		System.out.println("Correlation = " + co1.getCorrelation());
		System.out.println("Error = " + co1.getCorrelationError());
		double[][] updatedpair1 = co1.getPair();
		
		double test = Calculator.getInstance().correlationStrength(Calculator.getInstance().generateCorrelation(30, 19), co1.getCorrelation(), 1.8);
		System.out.println("New pair Strength = " + test);
		
		co1.addPair(30, 19);
		double[][] updatedpair = co1.getPair();
		System.out.println("Updated Correlations:");
		for(int i = 0 ; i < 5 ; i++){
			System.out.print(updatedpair[i][0] + " ");
		}
		System.out.println("");
		for(int i = 0 ; i < 5 ; i++){
			System.out.print(updatedpair[i][1] + " ");
		}
		System.out.println("");
		System.out.println("Correlation = " + co1.getCorrelation());
		System.out.println("Error = " + co1.getCorrelationError());
	}
}
