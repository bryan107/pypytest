package mdfr.core.emd;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import mdfr.develop.toos.Print;
import mdfr.math.emd.EMD;
import mdfr.math.emd.datastructure.Data;
import mdfr.math.emd.datastructure.IMFs;
import junit.framework.TestCase;

public class TestEMD extends TestCase {
	private double zerocrossingaccuracy = 0.0001;
	private long datasize = 2000;
	private double[] IFparamaters = {4,2,1}; 
	DecimalFormat df = new DecimalFormat("0.0");
	
	public void testGetIMFs(){
		LinkedList<Data> residual = new LinkedList<Data>();
		double realfre = generateResidual(residual, datasize);
		EMD emd = new EMD(residual, zerocrossingaccuracy, IFparamaters[0], IFparamaters[1], IFparamaters[2]);
		IMFs imfs = emd.getIMFs(10);
		LinkedList<LinkedList<Data>> IFs = emd.getInstantFrequency();
		for(int i = 0 ; i < imfs.size() ; i++){
			try {
				double average = average(imfs.getIMF(i));
				System.out.println();
				System.out.println("IMF[" + i + "]:" + average);
				Print.getInstance().printDataLinkedList(imfs.getIMF(i));
			} catch (Exception e) {
				System.out.println(e);
			}
			try {
				System.out.println("Current Frequency:" + realfre/(i+1));
				System.out.println("Instant Frequency[" + i + "]" + "Error:" + calcL2Error(IFs.get(i), realfre/(i+1)));
				Print.getInstance().printDataLinkedList(IFs.get(i));
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}
	
	public void testGetInstantFrequency(){
		
	}
	
	private double generateResidual(LinkedList<Data> residual, long size) {
		for (double i = 0; i < size; i+=0.6) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(0.1);
			double value = 9.5 * Math.sin(i / 3) + noise;
			residual.add(new Data(i, value));
		}
		System.out.println("Real Frequency:" + 1/(2*Math.PI*3));
		return 1/(2*Math.PI*3);
	}
	
	private double calcL2Error(LinkedList<Data> data, double average){
		Iterator<Data> it = data.iterator();
		double sum = 0;
		while(it.hasNext()){
			sum += Math.pow(it.next().value() - average, 2);
		}
		return Math.pow(sum, 0.5);
	}
	
	private double average(LinkedList<Data> data){
		Iterator<Data> it = data.iterator();
		double sum = 0;
		while(it.hasNext()){
			sum += it.next().value();
		}
		return sum/data.size();
	}
}
