package mfdr.math.emd;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.math.emd.InstantFrequency;
import mfdr.math.emd.InstantFrequencyNonWeighted;
import mfdr.math.emd.InstantFrequencyWeighted;
import mfdr.math.emd.datastructure.LocalExtremas;
import mfdr.math.emd.utility.DataListPropertyExtractor;
import mfdr.utility.Print;
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
		LinkedList<Data> extremas = DataListPropertyExtractor.getInstance().getSortedLocalExtremas(
				data);
		Print.getInstance().printDataLinkedList(extremas, 100);
		System.out.println("Extrema{" + extremas.get(1).time() + ","
				+ extremas.get(2).time() + "}" + "Value{"
				+ extremas.get(1).value() + "," + extremas.get(2).time() + "}");
		double zerocrossing = DataListPropertyExtractor.getInstance().getLocalZeroCrossing(CS,
				extremas.get(1).time(), extremas.get(2).time(), accuracy);
		System.out.println("Zero Crossing[" + timedf.format(zerocrossing)
				+ "]:" + valuedf.format(CS.interpolate(zerocrossing)));
	}
	
	public void testGetZeroCrossings(){
		generateResidual(data, 200);
		// Get points
		LocalExtremas le = DataListPropertyExtractor.getInstance().getLocalExtremas(data);
//		LinkedList<Data> zerocrossings =  Tools.getInstance().getZeroCrossings(data, 0.00001);
		
		// Print
		System.out.print("Upper Extremas: ");
		Print.getInstance().printDataLinkedList(le.localMaxima(), 100);
		System.out.print("Lower Extremas: ");
		Print.getInstance().printDataLinkedList(le.localMinima(), 100);
		System.out.print("Zero Crossings: ");
//		Print.getInstance().printDataLinkedList(zerocrossings);
	}
	
	public void testGetInstantFrequency(){
		double realfre = generateResidual(data, 300);
		InstantFrequency frequency = new InstantFrequencyWeighted(4,2,1);
		InstantFrequency fre = new InstantFrequencyNonWeighted();
		LinkedList<Data> instf = DataListPropertyExtractor.getInstance().getInstantFrequency(data, 0.0001, frequency);
		LinkedList<Data> instf2 = DataListPropertyExtractor.getInstance().getInstantFrequency(data, 0.0001, fre);
		System.out.println();
		double e1 = calcL2Error(instf, realfre);
		System.out.println("Instant Frequency[N]: Error:" + e1 + "%");
		Print.getInstance().printDataLinkedList(instf2, 100);
		double e2 = calcL2Error(instf2, realfre);
		System.out.println("Instant Frequency[W]:" + e2 + "%" );
		Print.getInstance().printDataLinkedList(instf, 100);
	}

	private double generateResidual(LinkedList<Data> residual, long size) {
		for (double i = 0; i < size; i++) {
			double value = 9.5 * Math.sin(i / 3);
			residual.add(new Data(i, value));
		}
		System.out.println("Real Frequency:" + 1/(2*Math.PI*3));
		return 1/(2*Math.PI*3);
	}
	
	private double calcL2Error(LinkedList<Data> data, double average){
		Iterator<Data> it = data.iterator();
		double sum = 0;
		while(it.hasNext()){
			Data fre = it.next();
			if(fre.value() == 0)
				break;
			sum += Math.pow(fre.value() - average, 2);
		}
		return Math.pow(sum, 0.5);
	}
}
