package experiment.test;

import java.util.Arrays;

import junit.framework.TestCase;

public class TestSort extends TestCase {

	
	public void test(){
		double[] gg = new double[5];
		for(int i = 0 ; i < 5 ; i++){
			gg[i] = Math.random();
			System.out.println(gg[i]);
		}
		Arrays.sort(gg);
		for(int i = 0 ; i < 5 ; i++){
			System.out.println(gg[i]);
		}
	}
}
