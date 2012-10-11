package faultDetection.tools;

import java.util.LinkedList;
import java.util.ListIterator;

public class PairedReading {
	
	private LinkedList<Double> pairedreadingX = new LinkedList<Double>();
	private LinkedList<Double> pairedreadingY = new LinkedList<Double>();
	private LinkedList<Double> trimmedpairedreadingX = new LinkedList<Double>();
	private LinkedList<Double> trimmedpairedreadingY = new LinkedList<Double>();
	
	
	public PairedReading(int samplesize){
		
	}
	
	public void addPair(double x, double y){
		pairedreadingX.add(x);
		pairedreadingY.add(y);
	}
	
	public boolean removePair(){
		if(pairedreadingX.isEmpty() || pairedreadingY.isEmpty()){
			return true;
		}
		return false;
	}
	
	public double[] getArrayX(){
		double[] x = new double[pairedreadingX.size()]; 
		for(int index = 0 ; index < pairedreadingX.size() ; index++){
			x[index] = pairedreadingX.get(index);
		}
		return x;
	}
	
	public double[] getArrayY(){
		double[] y = new double[pairedreadingY.size()]; 
		for(int index = 0 ; index < pairedreadingY.size() ; index++){
			y[index] = pairedreadingY.get(index);
		}
		return y;
	}
	
	
}
