package faultDetection.tools;


//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Regression;
import flanagan.analysis.Stat;

public final class Calculator {
	private Regression reg;
//	private static Log logger = LogFactory.getLog(Calculator.class);
	
	private static Calculator self = new Calculator();
	
	private Calculator(){

	}
	
	public static Calculator getInstance(){
		return self;
	}

//	General purpose for the project-------------------------------
	public double correlationStrengthOriginal(double input, double estimatecorrelation, double boundary){
		double upperbondary = estimatecorrelation*(1.0 + boundary);
		double lowerbondary = estimatecorrelation/(1.0 + boundary);
		double result = 1;
		if(input < estimatecorrelation){
			result = (input - lowerbondary) / (estimatecorrelation - lowerbondary);
			if(result < 0)
				return 0;
		}
		else if(input > estimatecorrelation){
			result = (upperbondary - input) / (upperbondary - estimatecorrelation);
			if(result < 0)
				return 0;
		}
		return result;
	}
	
	public double correlationStrength(double input, double estimatecorrelation, double boundary){
		double upperbondary = estimatecorrelation + (estimatecorrelation*boundary);
		double lowerbondary = estimatecorrelation - (estimatecorrelation*boundary);
		double result = 1;
		if(input < estimatecorrelation){
			result = (input - lowerbondary) / (estimatecorrelation - lowerbondary);
			if(result < 0)
				return 0;
		}
		else if(input > estimatecorrelation){
			result = (upperbondary - input) / (upperbondary - estimatecorrelation);
			if(result < 0)
				return 0;
		}
		return result;
	}
	
	public double generateCorrelation(double x , double y){
		return (y/x);
	}
	
//	Regression----------------------------------------------------
	
	private void regressionSetup(double[] x, double[] y){
		reg = new Regression(x, y);
		reg.linearGeneral();
	}
	
	public double getRegressionSlope(double[] x, double[] y){
		regressionSetup(x, y);
		double[] read = reg.getBestEstimates();
		if (read.length == 1){
//			logger.info("Regression read success");
			return read[0];
		}
//		logger.error("Regression read out of boundary");
		return 0;
	}
	
	public double getRegressionError(double[] x, double[] y){
		regressionSetup(x, y);
		double[] read = reg.getBestEstimatesErrors();
		if (read.length == 1){
//			logger.info("Regression error read success");
			return read[0];
		}
//		logger.error("Regression error read out of boundary");
		return 0;
	}
	

	public double getTheilSenRegressionSlope(double[] x, double[] y){
		double[] slopes = new double[(x.length*(x.length-1)/2)]; 
		int cnt = 0;
		double xref, yref;
		for(int i = 0 ; i < (x.length -1) ; i++){
			xref = x[i];
			yref = y[i];
//			logger.info("Xref: " + xref + " Yref: " + yref);
			for(int j = i + 1 ; j < x.length ; j++){
				double value = (x[j]-xref)/(y[j]-yref);
				if(value > 0){								//Make sure no minus slope
					slopes[cnt] = (x[j]-xref)/(y[j]-yref);
//				logger.info("S: " + slopes[cnt]);
				cnt++;
				}
			}
		}
		return Stat.median(slopes);
	}
	
	
//	Statistics------------------------------------------------
	
	public double getaverage(double[] inputarray){
		double accumulate = 0;
		for(int i = 0 ; i < inputarray.length ; i++){
			accumulate+= inputarray[i];
		}
		return (accumulate/inputarray.length);
	}
	
	public double getStandardDeviation(double[] input){
		return Stat.standardDeviation(input);
	}
	
	public double getStandardDeviationError(double[] input){
		return Stat.standardError(input);
	}
	
	public double getMedian(double[] input){
		return Stat.median(input);
	}
	
	
	
}
