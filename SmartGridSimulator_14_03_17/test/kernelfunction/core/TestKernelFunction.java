package kernelfunction.core;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

import flanagan.analysis.Stat;

import junit.framework.TestCase;

public class TestKernelFunction extends TestCase {
	final short GD = 3;
	double mean = 20;
	double sd = 2;
	
	public void testFunction(){
		Queue<Reading> q = getQueue();
		KernelFunction k = new KernelFunction(q);
		System.out.println("Kernel = " + k.epanechnikovKernel(0));
		System.out.println("Kernel = " + k.epanechnikovKernel(1));
		DecimalFormat df = new DecimalFormat("0.0");
		UnivariateIntegrator integrator = new RombergIntegrator();
		for(double max = 0.0 ; max<=3.0 ; max+=0.1){
			System.out.println("Int[" + df.format(max) + "]: " + integrator.integrate(10000, k, -0.5, max));
		}
		System.out.println("Function = " + k.function(2));
	}
	
	
	private Queue<Reading> getQueue(){
		Queue<Reading> readings = new LinkedList<>();
		for(int i = 0 ; i < 30 ; i++){
			readings.add(new Reading(Stat.normalInverseCDF(mean, sd, Math.random()), GD));
//			constant + 10 * Math.random();
		}
		return readings;
	}
	
	
	private void printQueueD(Queue<Double> q){
		Iterator<Double> it = q.iterator();
		while(it.hasNext()){
			System.out.print(it.next() + " ");
		}
		System.out.println();
	}
	
	private void printQueueR(Queue<Reading> q){
		Iterator<Reading> it = q.iterator();
		while(it.hasNext()){
			System.out.print(it.next().value() + " ");
		}
		System.out.println();
	}
}
