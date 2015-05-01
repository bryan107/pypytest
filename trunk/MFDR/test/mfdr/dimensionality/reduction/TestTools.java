package mfdr.dimensionality.reduction;

import math.jwave.Transform;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.wavelets.haar.*;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.Print;
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
	
	public void testDWT(){
		Transform t = new Transform( new FastWaveletTransform( new Haar1( ) ) );
		Distance dist = new EuclideanDistance();
		
		double[ ] arrTime = { 1, 2, 3, 4, 0, 0, 0, 0};
		double[ ] arrTime2 = { 2, 2, 4, 4, 0, 0, 0, 0};
		
		double[ ] arrTimexx = { 1, 2, 3, 4};
		double[ ] arrTimexx2 = { 2, 2, 4, 4};
		
		double[ ] arrHilb = t.forward( arrTime ); // 1-D FWT Haar forward
		double[ ] arrHilb2 = t.forward( arrTime2 ); // 1-D FWT Haar forward
		System.out.println("Hilb1");
		Print.getInstance().printArray(arrHilb, 8);
		System.out.println("Hilb2");
		Print.getInstance().printArray(arrHilb2, 8);
		
		System.out.println("Dist Original: " + dist.calDistance(arrTime, arrTime2));
//		System.out.println("Dist Original Trimmed: " + dist.calDistance(arrTimexx, arrTimexx2));
		System.out.println("Dist DWT: " + dist.calDistance(arrHilb, arrHilb2));
//		System.out.println("TEST: " + 5.6%1);
	}
	
	
}
