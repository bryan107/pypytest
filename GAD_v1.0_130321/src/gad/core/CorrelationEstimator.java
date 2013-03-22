package gad.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CorrelationEstimator {
	private static Log logger = LogFactory.getLog(CorrelationEstimator.class);
	private	Map<Integer, Double> reading;
	private	Map<Integer, Map<Integer, EstimatedVariance>> Estimation;
	private	double numberofdeviation;

	public CorrelationEstimator(double numberofdeviation) {
		setDeviation(numberofdeviation);
	}

	public void setDeviation(double numberofdeviation) {
		this.numberofdeviation = numberofdeviation;
	}

	public void setup(Map<Integer, Double> reading,
			Map<Integer, Map<Integer, EstimatedVariance>> Estimation) {
		this.reading = reading;
		this.Estimation = Estimation;
	}

	public Map<Integer, Map<Integer, Boolean>> assessCorrelation(int[] nodeid,
			Map<Integer, Double> reading, Map<Integer, Map<Integer, EstimatedVariance>> Estimation) {
		// Return Correlatoin Map
		Map<Integer, Map<Integer, Boolean>> correlation = new HashMap<Integer, Map<Integer, Boolean>>();
		// Setup Variables
		setup(reading, Estimation);
		// Main function : Assert Correlation
		for(int i = 0 ; i < nodeid.length ; i++){
			Map<Integer, Boolean> temp = new HashMap<Integer, Boolean>();
			for(int j = 0 ; j < nodeid.length ; j++){
				if(nodeid[i] == nodeid[j]){
					continue;
				}
				try {
					temp.put(nodeid[j], assessCorrelation(nodeid[i], nodeid[j]));
				} catch (Exception e) {
					logger.error("No such Estimation Entry" + e);
				}
				
			}
			correlation.put(nodeid[i], temp);
		}
		return correlation;
	}

	private boolean assessCorrelation(int nodei, int nodej) {
		double[] translatedreading = new double[2];
		double[] rotatedreading = new double[2];
		double[][] newcoordinate = Estimation.get(nodei).get(nodej).direction();
		double[] deviation = Estimation.get(nodei).get(nodej).deviation();
		double[] previousreading = Estimation.get(nodei).get(nodej).previousReading();
		// ********************* Translating & rotation ******************** //
		translatedreading[0] = reading.get(nodei) - previousreading[0];
		translatedreading[1] = reading.get(nodej) - previousreading[1];
		for (int i = 0; i < 2; i++) {
			rotatedreading[i] = translatedreading[0] * newcoordinate[i][0]
					+ translatedreading[1] * newcoordinate[i][1];
		}
		// ************************** Asserting *****************************//
		double value = (Math.pow(rotatedreading[0], 2) / Math.pow(numberofdeviation * deviation[0], 2))
				+ (Math.pow(rotatedreading[1], 2) / Math.pow(numberofdeviation	* deviation[1], 2));
		if(value > 1){
			return false;
		}
		else{
			return true;
		}
	}
}
