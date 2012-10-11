package faultDetection.correlationControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import faultDetection.tools.Calculator;

public final class CorrelationStrengthManager {
	// ------------------Defined Values-----------------------
	//faulty conditions
	private final short FT = 0;

	// -----------------Private Variables---------------------
//	private double TCD;
	private double tolerablenoise;
	private double minimumcorrelationstrength;
	private static Log logger = LogFactory.getLog(CorrelationStrengthManager.class);
	// -------------------Constructor-------------------------
	public CorrelationStrengthManager(double tolerablenoise, double minimumcorrelationstrength){
		updateTolerableNoise(tolerablenoise);
		updateMinimumCorrelationStrength(minimumcorrelationstrength);
//		updateErrorTolerance(errortolerance);
	}
	
	// ------------------Variable Settings--------------------
	
	public void updateTolerableNoise(double tolerablenoise){
		if(tolerablenoise == 1){
			logger.error("CSTolerableNoise cannot be 1. CSTolerableNoise has been temporaryly set to 0.02");
			this.tolerablenoise = 0.02;
		}
		this.tolerablenoise = tolerablenoise;
	}
	public void updateMinimumCorrelationStrength(double minimumcorrelationstrength){
		if(minimumcorrelationstrength == 1){
			logger.error("DFDThreshold cannot be 1. DFDThreshold has been temporaryly set to 0.8");
			this.tolerablenoise = 0.8;
		}
		this.minimumcorrelationstrength = minimumcorrelationstrength;
	}
	
//	public void updateErrorTolerance(double TCD){
//		this.TCD = TCD;
//	}
	
	// ------------------Public Functions---------------------
	
	public double getTolerableNoise(){
		return tolerablenoise;
	}
	
//	public double getErrorTolerance(){
//		return TCD;
//	}

	public Map<Integer, Map<Integer, Double>> getCorrelationStrengthTable(Map<Integer, Map<Integer, Double>> correlationtable, Map<Integer, Map<Integer, Double>> correlationtrendtable){
		Map<Integer, Map<Integer, Double>> correlationstrengthtable = new HashMap<Integer, Map<Integer,Double>>();
		Set<Integer> keyraw = correlationtable.keySet();
		Iterator<Integer> iteratorraw = keyraw.iterator();
		while(iteratorraw.hasNext()){
			int nodeidraw = iteratorraw.next();
			Set<Integer> keycoloum = correlationtable.get(nodeidraw).keySet();
			Iterator<Integer> iteratorcoloum = keycoloum.iterator();
			Map<Integer, Double> tempcontainer = new HashMap<Integer, Double>();
			while(iteratorcoloum.hasNext()){
				int nodeidcoloum = iteratorcoloum.next();
				double correlation = correlationtable.get(nodeidraw).get(nodeidcoloum);
				try {//Check whether the correlationtrend table has the some entry of correlation table
					double correlationtrend = correlationtrendtable.get(nodeidraw).get(nodeidcoloum);
					double correlationstrength = Calculator.getInstance().correlationStrength(correlation, correlationtrend, getTCD());
					tempcontainer.put(nodeidcoloum, correlationstrength);
				} catch (Exception e) {
					tempcontainer.put(nodeidcoloum, 1.0);
					logger.warn("Cannot find the same entry of node[" + nodeidraw + "][" + nodeidcoloum + "] in correlation trend table when calculating correlation strength. " + e);
				}
			}
			correlationstrengthtable.put(nodeidraw, tempcontainer);
		}
		return correlationstrengthtable;
	}

	public Map<Integer, Double> getReadingTrustworthiness(Map<Integer, Map<Integer, Double>> correlationstrengthtable,Map<Integer, Short> readingfaultcondition){
		Map<Integer, Double> readingtrustworthiness = new HashMap<Integer, Double>();
		Set<Integer> keyraw = correlationstrengthtable.keySet();
		Iterator<Integer> iteratorraw = keyraw.iterator();
		while(iteratorraw.hasNext()){
			int nodeidraw = iteratorraw.next();
			Set<Integer> keycoloum = correlationstrengthtable.get(nodeidraw).keySet();
			Iterator<Integer> iteratorcoloum = keycoloum.iterator();
			double tempreadingtrustworthiness = 0;
			int readingcount = 0;
			while(iteratorcoloum.hasNext()){
				int nodeidcoloum = iteratorcoloum.next();
				try {
					if(readingfaultcondition.get(nodeidcoloum) != FT){
						tempreadingtrustworthiness += correlationstrengthtable.get(nodeidraw).get(nodeidcoloum);
						readingcount ++;
					}
				} catch (Exception e) {
					logger.error("The entry of node[" + nodeidraw + "][" + nodeidcoloum + "] does not exits in readingfaultconditiontable. " + e);
				}
				
			}
			readingtrustworthiness.put(nodeidraw, (tempreadingtrustworthiness/readingcount));
		}
		//use correlation strength table & reading fault condition to generate correlation
		return readingtrustworthiness;
	}
	
	public double getTCD(){
		try {
			return (2 * tolerablenoise)/((1 - minimumcorrelationstrength) * (1 - tolerablenoise));
		} catch (Exception e) {
			logger.error("Incorrect FDC Service Setting");
			logger.error("CSTolerableNoise: " + tolerablenoise);
			logger.error("DFDThreshold: " + minimumcorrelationstrength);
			return 0;
		}
		
	}
	
	public double getTCD(double toleralbenoise, double minimumcorrelationstrength){
		updateTolerableNoise(toleralbenoise);
		updateMinimumCorrelationStrength(minimumcorrelationstrength);
		return getTCD();
	}
}
