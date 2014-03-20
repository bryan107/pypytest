package kernelfunction.core;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import flanagan.analysis.Stat;
import junit.framework.TestCase;

public class TestKernel extends TestCase {
	final short GD = 3;
	double mean = 20;
	double sd = 1;
	double value = 30;
	double deviation = 5;
	int nodenum = 2;

	public void testMarkReading() {
		DecimalFormat df = new DecimalFormat("00.00");
		Kernel k = new Kernel();
		for (int i = 0; i < 60; i++) {
			Map<Integer, Double> reading = getReading(nodenum);
			if(i>30 && (i % 6) == 0)
				reading = addFault(0, reading);
			Map<Integer, MarkedReading> mrpack = k.markReading(reading)
					.markedReadingPack();
			System.out.print("Round [" + i + "]");
			Iterator<Integer> it = mrpack.keySet().iterator();
			while (it.hasNext()) {
				int nodeid = it.next();
				System.out.print("[" + nodeid + "]: R="
						+ df.format(mrpack.get(nodeid).value()) + " C="
						+ mrpack.get(nodeid).readingContidion() + " ");
			}
			System.out.println();
		}

	}

	private Map<Integer, Double> getReading(int number) {
		Map<Integer, Double> reading = new HashMap<Integer, Double>();
		for (int i = 0; i < number; i++) {
			reading.put(i, Stat.normalInverseCDF(mean, sd, Math.random()));
		}
		return reading;
	}

	private Map<Integer, Double> addFault(int nodeid,
			Map<Integer, Double> reading) {
		double freading = reading.get(nodeid) + deviation;
		reading.put(nodeid, freading);
		return reading;
	}
}
