package mfdr.core;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import junit.framework.TestCase;

public class TestMFDRFacade extends TestCase {
	private double white_noise_level = 5;
	private double white_noise_threshold = 6.2;
	private double min_NSratio = 0.5;
	private double motif_FTratio = 0.5;
	private int motif_k = 3;
	private double motif_threshold = 0.1;

	public void testLearnWindowSize() {
		TimeSeries ts = generateTimeSeries(5000);
		MFDRFacade facade = new MFDRFacade(white_noise_level,
				white_noise_threshold, min_NSratio, motif_FTratio, motif_k, motif_threshold);
		WindowSize ws= facade.learnWindowSizes(ts);
		System.out.println("TREND: " + ws.trend() + " NOISE: " + ws.noise());
	}

	public void testLearnParameters() {

	}

	public void testGetDistance() {

	}
	
	private TimeSeries generateTimeSeries(long size) {
		TimeSeries ts = new TimeSeries();
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = 0; 
			noise = r.nextGaussian() * Math.sqrt(5);
			double trend = 1*Math.pow(i, 0.5);
			if(i%200 == 0){
				trend = trend + r.nextGaussian() * Math.sqrt(50);
			}
//			double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 * Math.cos(i*Math.PI / 6)  + noise;
			double value = 9.5 * Math.sin(i*Math.PI / 64) + trend + noise;
			ts.add(new Data(i, value));
		}
		return ts;
//		return 1/(2*Math.PI*3);
	}
}
