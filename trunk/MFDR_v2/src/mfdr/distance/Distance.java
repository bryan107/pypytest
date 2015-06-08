package mfdr.distance;

import java.util.Iterator;

import flanagan.interpolation.LinearInterpolation;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;

public abstract class Distance {

	/**
	 * Calculate the distance between two array object Time Series.
	 */
	public abstract double calDistance(double[] xx, double[] yy);

	/**
	 * Calculate the distance between two white noise series with their energy densities.
	 * @param energy density 1 (double)
	 * @param energy density 2 (double)
	 * @param series length (integer)
	 * @return Distance (double)
	 */
	public abstract double calWhiteNoiseDistance(double e1, double e2, int length);
	
	/**
	 * Calculate the distance between two TimeSeries Objects. The third
	 * Parameter provides time references aligning the two input Time Series.
	 * Linear Interpolation is used when the time stamps of two input time
	 * series do not match the reference.
	 */
	public double calDistance(TimeSeries xx, TimeSeries yy, TimeSeries ref) {
		int outputindex = 0;
		double[] xxarray = new double[ref.size()];
		double[] yyarray = new double[ref.size()];
		int xxindex = 0, yyindex = 0;
		Iterator<Data> it = ref.iterator();
		while (it.hasNext()) {
			Data refdata = (Data) it.next();
			
			// Find the data of xx at refdata.time();
			while (xx.get(xxindex).time() < refdata.time()) {
				// If ref time is larger than the last instance of xx
				if (xxindex >= xx.size()) {
					break;
				}
				xxindex++;
			}
			
			// Find the data of yy at refdata.time();
			while (yy.get(yyindex).time() < refdata.time()) {
				// if ref time is larget tan the last instance of yy
				if (yyindex >= yy.size()) {
					break;
				}
				yyindex++;
			}
			
			// Save data to arrays
			saveValueToArray(xx, outputindex, xxarray, xxindex, refdata);
			saveValueToArray(yy, outputindex, yyarray, yyindex, refdata);
			outputindex++;
		}
		return calDistance(xxarray, yyarray);
	}

	private void saveValueToArray(TimeSeries xx, int outputindex,
			double[] xxarray, int xxindex, Data refdata) {
		
		if (xx.get(xxindex).time() == refdata.time()) {
			xxarray[outputindex] = xx.get(xxindex).value();
		} else {
			double[] x = { xx.get(xxindex - 1).time(), xx.get(xxindex).time() };
			double[] y = { xx.get(xxindex - 1).value(), xx.get(xxindex).value() };
			LinearInterpolation interpolation = new LinearInterpolation(x, y);
			xxarray[outputindex] = interpolation.interpolate(refdata.time());
		}
		
	}

}
