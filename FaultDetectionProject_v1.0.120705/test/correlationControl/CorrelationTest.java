package correlationControl;

import java.rmi.dgc.Lease;

import faultDetection.correlationControl.Correlation;
import faultDetection.tools.LeastSquareEstimator;
import faultDetection.tools.MedianEstimator;
import faultDetection.tools.QuantilesEEstimator;
import junit.framework.TestCase;

public class CorrelationTest extends TestCase {
	double[] inputX = {20,20,20,20,20,20,20,20,20,20};
	double[] inputY = {10,10,10,10,10,16,16,8,15,16};
//	final int QLSE = 3;

	public void testCorrelation(){
		double[][] input = new double[10][2];
		for (int i = 0 ; i < 10 ; i ++){
			input[i][0] = inputX[i];
			input[i][1] = inputY[i];
		}
		Correlation co1 = new Correlation(10, new LeastSquareEstimator(), 0.25);
		for(int i = 0 ; i < 10 ; i ++){
			co1.addPair(input[i][0], input[i][1]);
		}
		System.out.println("Correlation = " + co1.getEstimatedCorrelation());
		System.out.println("Error = " + co1.getCorrelationError());

		co1.updateRegressionEstimator(new QuantilesEEstimator());
		System.out.println("QE Correlation = " + co1.getEstimatedCorrelation());
		
		co1.updateRegressionEstimator(new MedianEstimator());
		System.out.println("Median Correlation = " + co1.getEstimatedCorrelation());
		
		
		
//		co1.addPair(20, 19);
//		double[][] updatedpair = co1.getPair();
//		System.out.println("Updated Correlations:");
//		for(int i = 0 ; i < 5 ; i++){
//			System.out.print(updatedpair[i][0] + " ");
//		}
//		System.out.println("");
//		for(int i = 0 ; i < 5 ; i++){
//			System.out.print(updatedpair[i][1] + " ");
//		}
//		System.out.println("");
//		System.out.println("Correlation = " + co1.getCorrelation());
//		System.out.println("Error = " + co1.getCorrelationError());
	}
}
