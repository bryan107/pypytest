package mfdr.datastructure;

import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import junit.framework.TestCase;

public class TestTimeSeries extends TestCase {

	public void testEnergy(){
		TimeSeries ts= new TimeSeries();
		generateResidual(ts, 100);
		System.out.println("Energy:" + ts.energy());
		System.out.println("Energy Density" + ts.energyDensity());
//		System.out.println("Normalised Energy Density" + ts.normalizedEnergyDensity());
	}
	
	
	private double generateResidual(TimeSeries residual, long size) {
		for (double i = 0; i < size; i+=0.2) {
			java.util.Random r = new java.util.Random();
			double noise = 0; 
			noise = r.nextGaussian() * Math.sqrt(5);
//			double value = noise;
			double value = 9.5 * Math.sin(i*Math.PI / 3);
			
			residual.add(new Data(i, value));
		}
		return (double)1/6;
//		return 1/(2*Math.PI*3);
	}
}
