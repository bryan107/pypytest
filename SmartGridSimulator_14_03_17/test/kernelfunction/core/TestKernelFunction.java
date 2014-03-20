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
	double sd = 1;
	double value = 30;

	public void testFunction() {
		DecimalFormat df = new DecimalFormat("0.0");
		DecimalFormat df2 = new DecimalFormat("0.000");
		Queue<Reading> q = getQueue();
		UnivariateIntegrator integrator = new RombergIntegrator();
		for (double value = 10; value <= 30; value += 5) {
			System.out.println("V: " + value);
			Queue<Double> rdq = getRelativeDistanceQueue(value, q);
//			System.out.print("RDQ: ");
//			printQueueD(rdq);
			KernelFunction k = new KernelFunction(rdq);
			for (double max = 0.1; max <= 1.3; max += 0.1) {
				System.out.print("Int[" + df.format(max) + "]: " + df2.format(integrator.integrate(100000, k, 0, max)) +" ");
			}
			System.out.println();
		}

	}

	private Queue<Reading> getQueue() {
		Queue<Reading> readings = new LinkedList<>();
		for (int i = 0; i < 30; i++) {
			readings.add(new Reading(Stat.normalInverseCDF(mean, sd,
					Math.random()), GD));
			// constant + 10 * Math.random();
		}
		return readings;
	}

	private Queue<Double> getRelativeDistanceQueue(double value,
			Queue<Reading> readings) {
		Queue<Double> rdq = new LinkedList<Double>();
		Iterator<Reading> it = readings.iterator();
		while (it.hasNext()) {
			Reading r = it.next();
			if (r.isValid() == GD) {
				rdq.add(Math.abs(value - r.value()));
			}
		}
		return rdq;
	}

	private void printQueueD(Queue<Double> q) {
		DecimalFormat df2 = new DecimalFormat("0.000");
		Iterator<Double> it = q.iterator();
		while (it.hasNext()) {
			System.out.print(df2.format(it.next()) + " ");
		}
		System.out.println();
	}

	private void printQueueR(Queue<Reading> q) {
		Iterator<Reading> it = q.iterator();
		while (it.hasNext()) {
			System.out.print(it.next().value() + " ");
		}
		System.out.println();
	}
}
