package mdfr.utility;

import java.util.LinkedList;

import flanagan.analysis.Stat;
import mdfr.math.emd.DataListOperator;
import mdfr.math.emd.datastructure.Data;
import junit.framework.TestCase;

public class testStatTool extends TestCase {
	private long datasize = 100;  
	
	public void testAutoCorr(){
		LinkedList<Data> residual = new LinkedList<Data>();
		generateResidual(residual, datasize);
		residual = DataListOperator.getInstance().normalize(residual);
		double[] ac = StatTool.getInstance().autoCorr(residual);
		double acc = StatTool.getInstance().autoCorrCoeff(residual);
		System.out.println("ACC: " + acc);
		System.out.print("AC:");
		Print.getInstance().printArray(ac, 100);
		double[] array = DataListOperator.getInstance().LinkedListToArray(residual, (short) 1);
		double corr = Stat.corrCoeff(array, array);
		System.out.println("Corr: " + corr);
	}
	
	private double generateResidual(LinkedList<Data> residual, long size) {
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
