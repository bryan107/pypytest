package mfdr.math.motif;

import java.util.Iterator;
import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.math.motif.Motif;
import mfdr.utility.File;
import mfdr.utility.Print;
import mfdr.utility.StatTool;
import junit.framework.TestCase;

public class TestMotif extends TestCase {

	public void testGetKMotif(){
		TimeSeries ts = new TimeSeries();
		generateResidual(ts, 120);
		System.out.println("Time Series");
		Print.getInstance().printDataLinkedList(ts, 120);
		
		
		Motif motif = new Motif(ts, 12);
		LinkedList<LinkedList<Integer>> kmotif = motif.getKMotifs(2, 0.2);
		
		//Print results
		int i = 0;
		Iterator<LinkedList<Integer>> it = kmotif.iterator();
		while(it.hasNext()){
			LinkedList<Integer> k = it.next();
			System.out.println("[" + i + "]: ");
			Iterator<Integer> it2 = k.iterator();
			while (it2.hasNext()) {
				Integer index = (Integer) it2.next();
				Print.getInstance().printDataLinkedList(motif.getSubSignal(index), 30);
			}
		}
		
//		File.getInstance().saveTimeToFile(residual, "C:\\TEST\\MDFR\\IMFTest_Norm_AutoCorr.csv");
//		try {
//			for(int i = 0 ; i < imfs.size() ; i++){
//				System.out.print("IF[" + StatTool.getInstance().autoCorrCoeff(imfs.getIMF(i).instFreqFullResol(residual)) + "]: ");
//				File.getInstance().saveArrayToFile(StatTool.getInstance().autoCorr(imfs.getIMF(i).instFreqFullResol(residual)), "C:\\TEST\\MDFR\\IMFTest_Norm_AutoCorr.csv");
//			}
//		} catch (Exception e) {
//		}

	}
	
	
	private double generateResidual(LinkedList<Data> residual, long size) {
		for (double i = 0; i < size; i+=1) {
			java.util.Random r = new java.util.Random();
			double noise = 0; 
			noise = r.nextGaussian() * Math.sqrt(5);
//			double value = noise;
			double value = 1 * Math.sin(i*Math.PI / 6) + 0.5 * Math.cos(i*Math.PI / 12);
			
			residual.add(new Data(i, value));
		}
		return (double)1/6;
//		return 1/(2*Math.PI*3);
	}
}
