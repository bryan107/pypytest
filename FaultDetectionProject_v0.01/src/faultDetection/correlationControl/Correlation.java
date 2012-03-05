package faultDetection.correlationControl;

import faultDetection.tools.Caculator;

public class Correlation {
	private double[][] pair;
	private int mappointer;
	private int mapsize;	//Max number of pairs
	private int mapenable;	//Fulfill the requirement 
	
	public Correlation(int mapsize){
		mappointer = 0;
		mapenable = 0;
		this.mapsize = mapsize;
		pair = new double[2][this.mapsize];
	}
	
	public void addPair(double x , double y){
		pair[0][mappointer] = x;
		pair[1][mappointer] = y;
		mappointer++;
		if(mappointer < mapsize){			
			return;
		}
		else{
			if(mapenable == 0){
				mapenable = 1;
			}
			mappointer = 0;
		}
	}
	
	public double getCorrelation(){
		return Caculator.getInstance().getRegressionSlope(pair[0], pair[1]);
	}
	
	public double getCorrelationError(){
		return Caculator.getInstance().getRegressionError(pair[0], pair[1]);
	}
	
}
