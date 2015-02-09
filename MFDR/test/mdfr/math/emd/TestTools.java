package mdfr.math.emd;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import mdfr.develop.toos.Print;
import mdfr.math.emd.datastructure.Data;
import mdfr.math.emd.datastructure.LocalExtremas;
import flanagan.interpolation.CubicSpline;
import junit.framework.TestCase;

public class TestTools extends TestCase {

	DecimalFormat valuedf = new DecimalFormat("0.00000000");
	DecimalFormat timedf = new DecimalFormat("0.000000");
	private final double  accuracy = 0.0001;
	LinkedList<Data> data = new LinkedList<Data>();
	
	public void testGetLocalZeroCrossing() {

		generateResidual(data, 200);
		double[] datapoints = new double[data.size()];
		double[] datavalues = new double[data.size()];

		// Prepare data structures for CubicSpline
		Iterator<Data> it = data.iterator();
		try {
			for (int i = 0; it.hasNext(); i++) {
				Data d = it.next();
				datapoints[i] = d.time();
				datavalues[i] = d.value();
			}
		} catch (Exception e) {
		}

		// Calculate zero crossings
		CubicSpline CS = new CubicSpline(datapoints, datavalues);
		LinkedList<Data> extremas = Tools.getInstance().getSortedLocalExtremas(
				data);
		Print.getInstance().printDataLinkedList(extremas);
		System.out.println("Extrema{" + extremas.get(1).time() + ","
				+ extremas.get(2).time() + "}" + "Value{"
				+ extremas.get(1).value() + "," + extremas.get(2).time() + "}");
		double zerocrossing = Tools.getInstance().getLocalZeroCrossing(CS,
				extremas.get(1).time(), extremas.get(2).time(), accuracy);
		System.out.println("Zero Crossing[" + timedf.format(zerocrossing)
				+ "]:" + valuedf.format(CS.interpolate(zerocrossing)));
	}
	
	
	public void testGetZeroCrossings(){
		generateResidual(data, 200);
		// Get points
		LocalExtremas le = Tools.getInstance().getLocalExtremas(data);
		LinkedList<Data> zerocrossings =  Tools.getInstance().getZeroCrossings(data, 0.00001);
		
		// Print
		System.out.print("Upper Extremas: ");
		Print.getInstance().printDataLinkedList(le.localMaxima());
		System.out.print("Lower Extremas: ");
		Print.getInstance().printDataLinkedList(le.localMinima());
		System.out.print("Zero Crossings: ");
		Print.getInstance().printDataLinkedList(zerocrossings);
	}
	
	public void testGetInstantFrequency(){
		generateResidual(data, 200);
		InstantFrequency frequency = new NonWeightedInstantFrequency();
		LinkedList<Data> instf = Tools.getInstance().getInstantFrequency(data, 0.0001, frequency);
		System.out.println("Instant Frequency:");
		Print.getInstance().printDataLinkedList(instf);
	}

	private void generateResidual(LinkedList<Data> residual, long size) {
		for (double i = 0; i < size; i++) {
			double value = 9.5 * Math.sin(i / 3);
			residual.add(new Data(i, value));
		}
		System.out.println("Real Frequency:" + 1/(2*Math.PI*3));
	}
}
