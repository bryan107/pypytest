package Patterns;

import java.util.Arrays;

public class AbruptPattern implements Pattern {
//TODO Implement this Pattern
	@Override
	public double getCorrespondReading(double variation, double abruptiontimes, long sections,
			long sectionnumber) {
		double[] changepoint = new double[(int)abruptiontimes];
		for(int i = 0 ; i < (int)abruptiontimes ; i++){
			changepoint[i] = Math.random();
		}
		Arrays.sort(changepoint);
		
		return variation;
	}

}
