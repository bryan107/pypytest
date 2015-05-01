package mfdr.test;

import math.jwave.Transform;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.wavelets.haar.Haar1;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import junit.framework.TestCase;

public class TestDWT extends TestCase {

	public void test() {
		double[] arrTime1 = { 1, 0, 1, 0, 1, 1, 1, 1 };
		double[] arrTime2 = { 1, 1, 1, 1, 1, 0, 1, 0 };
		Transform t = new Transform(new FastWaveletTransform(new Haar1()));
		double[] hilb1 = t.forward(arrTime1);
		double[] hilb2 = t.forward(arrTime2);
		double[] hilb1_1 = new double[4], hilb1_2 = new double[4], hilb2_1 = new double[4], hilb2_2 = new double[4];
		for (int i = 0; i < 4; i++) {
			hilb1_1[i] = hilb1[i];
			hilb1_2[i] = hilb1[3+i];
			hilb2_1[i] = hilb2[i];
			hilb2_2[i] = hilb2[3+i];
		}
		Distance d = new EuclideanDistance();
		System.out.println("Original: " + d.calDistance(arrTime1, arrTime2));
		System.out.println("DWT ALL: " + d.calDistance(hilb1, hilb2));
		double value = d.calDistance(hilb1_1, hilb2_1) + d.calDistance(hilb1_2, hilb2_2);
		System.out.println("DWT SPLIT: " + value);
	}
}
