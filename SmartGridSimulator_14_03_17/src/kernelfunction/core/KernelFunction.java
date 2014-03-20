package kernelfunction.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.analysis.UnivariateFunction;

import flanagan.analysis.Stat;

public class KernelFunction implements UnivariateFunction {
	private static Log logger = LogFactory.getLog(KernelFunction.class);
	private Queue<Reading> readings;
	private Queue<Double> unireadings;
	public KernelFunction(Queue<Reading> readings){
		this.readings = readings;
		this.unireadings = unitIntervalMapping();
	}
	@Override
	public double value(double x) {
		double y = 0;
		try {
			y = function(x);
		} catch (Exception e) {
			logger.error("Integration Error" + e);
		}
		return y;
	}
	
	public double function(double x){
		double f = 0;
		// Test k(x) function
//		f = epanechnikovKernel(x);
		
		// Real Integration Function
		Iterator<Double> it = unireadings.iterator();
		while(it.hasNext()){
			double value = it.next();
			f += epanechnikovKernel(x - value);
		}
		f = f/unireadings.size();
		return f;
	}
	
	private Queue<Double> unitIntervalMapping(){
		Queue<Double> unireadings = new LinkedList<Double>();
		double[] temparray = new double[readings.size()];
		//Get min and Max
		int i = 0;
		Iterator<Reading> it = readings.iterator();
		while(it.hasNext()){
			temparray[i] = it.next().value();
			i++;
		}
		Arrays.sort(temparray);
		double maxinterval = temparray[readings.size()-1] - temparray[0];
		// Map into uniinterval
		try {
			it = readings.iterator();
			while(it.hasNext()){
				double value = (it.next().value() - temparray[0])/maxinterval;
				unireadings.add(value);
			}
		} catch (Exception e) {
			logger.error("Interval mapping Error" + e);
			return null;
		}
		return unireadings;
	}
	
	public double epanechnikovKernel(double xp){
		double sd = getStandardDeviation(); // extract standard deviation
		double B = Math.sqrt(5) * sd * Math.pow(unireadings.size(), -0.2);
		if(Math.abs(xp/B) < 1){
			return 0.75*(1/B)*(1-Math.pow((xp/B), 2));
		}
		else{
			return 0;
		}
	}
	
	private double getStandardDeviation() {
		int index = 0;
		double[] values = new double[unireadings.size()];
		Iterator<Double> it2 = unireadings.iterator();
		while(it2.hasNext()){
			values[index] = it2.next();
			index++;
		}
		return Stat.standardDeviation(values);
	}

}
