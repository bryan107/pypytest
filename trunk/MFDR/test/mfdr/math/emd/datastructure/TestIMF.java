package mfdr.math.emd.datastructure;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.math.emd.InstantFrequencyWeighted;
import mfdr.math.emd.TestEMD;
import mfdr.math.emd.datastructure.IMF;
import mfdr.utility.Print;
import junit.framework.TestCase;
import junit.framework.TestFailure;

public class TestIMF extends TestCase {
	
	private static Log logger = LogFactory.getLog(TestIMF.class);
	private double[] IFparamaters = {4,2,1}; 
	
	public void testFrequency(){
		TimeSeries residual = new TimeSeries();
		double frequency = generateResidual(residual, 1000);
		Print.getInstance().printDataLinkedList(residual,100);
		logger.info("Residual frequency: " + frequency);
		logger.info("Residual wavelength: " + 1/frequency);
		IMF imf = new IMF(residual, 0.0001, new InstantFrequencyWeighted(IFparamaters[0], IFparamaters[1], IFparamaters[2]));
		logger.info("IMF average frequency: " + imf.averageFrequency());
		logger.info("IMF average wavelength: " + imf.averageWavelength());
		logger.info("Instant Frequency");
		Print.getInstance().printDataLinkedList(imf.instantFrequency(), 100);
		
	}
	
	
	private double generateResidual(LinkedList<Data> residual, long size) {
		for (double i = 0; i < size; i+=0.2) {
			java.util.Random r = new java.util.Random();
			double noise = 0; 
//			noise = r.nextGaussian() * Math.sqrt(100);
//			double value = noise;
			double value = 9.5 * Math.sin(i*Math.PI / 3) + noise;
			residual.add(new Data(i, value));
		}
		return (double)1/6;
//		return 1/(2*Math.PI*3);
	}
	
}
