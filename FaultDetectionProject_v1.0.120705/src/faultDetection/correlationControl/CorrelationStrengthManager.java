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
	private double errortolerance;
	
	private static Log logger = LogFactory.getLog(CorrelationStrengthManager.class);
	// -------------------Constructor-------------------------
	public CorrelationStrengthManager(double errortolerance){
		updateErrorTolerance(errortolerance);
	}
	
	// ------------------Variable Settings--------------------
	public void updateErrorTolerance(double errortolerance){
		this.errortolerance = errortolerance;
	}
	
	// ------------------Public Functions---------------------
	public double getErrorTolerance(){
		return errortolerance;
	}

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
				try {//Chech whether the correlationtrend table has the some entry of correlation table
					double correlationtrend = correlationtrendtable.get(nodeidraw).get(nodeidcoloum);
					double correlationstrength = Calculator.getInstance().correlationStrength(correlation, correlationtrend, errortolerance);
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
	
}
