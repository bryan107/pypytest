package faultDetection.tools;


import flanagan.analysis.Regression;
import flanagan.analysis.Stat;

public final class Caculator {
	private Regression reg;
//	private static Log logger = LogFactory.getLog(Caculator.class);
	
	private static Caculator self = new Caculator();
	
	public Caculator(){

	}
	
	public static Caculator getInstance(){
		return self;
	}

//	General purpose for the project-------------------------------
	
	public double correlationStrength(double input, double estimatecorrelation, double bondary){
		double upperbondary = estimatecorrelation*bondary;
		double lowerbondary = estimatecorrelation/bondary;
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