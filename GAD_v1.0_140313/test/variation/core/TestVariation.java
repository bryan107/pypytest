package variation.core;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import ewma.core.EWMA;
import ewma.core.MarkedReading;
import ewma.core.ProcessedReadingPack;
import flanagan.analysis.Stat;

import junit.framework.TestCase;

public class TestVariation extends TestCase {

	public void testStandardDeviation() {
		double[] test = { 3, 1, 2, 5, 4 };
		System.out.println(Stat.standardDeviation(test));
	}

	public void testMarkReading() {
		Map<Integer, Double> reading = new HashMap<Integer, Double>();
		EWMA v = new EWMA();
		for (int i = 0; i < 100; i++) {
			// double value = 20 + 10 * Math.sin(i*0.06);
			double value = 20;
			reading.put(1, addNoise(value));
			reading.put(2, addNoise(value));
			if (i > 30 && (i % 7 == 0)) {
				reading.put(1, addError(addError(value)));
				System.out.println("ADD ERROR");
			}
			ProcessedReadingPack prp = v.markReading(reading);
			Map<Integer, MarkedReading> condition = prp.markedReadingPack();
			DecimalFormat df = new DecimalFormat("00.000");
			System.out.println("Round[" + i + "]: (1)R:"
					+ df.format(reading.get(1)) + "  C:" + condition.get(1).readingContidion()
					+ " (2)R:" + df.format(reading.get(2)) + "C:"
					+ condition.get(2).readingContidion());
		}
	}

	private double addNoise(double value) {
		return value * (1 + Math.random() * 0.05);
	}

	private double addError(double value) {
		return value * (1 + Math.random() * 0.5);
	}

}
