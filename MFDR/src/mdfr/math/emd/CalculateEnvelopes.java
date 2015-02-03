package mdfr.math.emd;

import java.util.LinkedList;

import flanagan.interpolation.CubicSpline;

public class CalculateEnvelopes {

	private CubicSpline upperCS, lowerCS;
	private LocalExtremas le;
	private LinkedList<Double> residual;
	private static CalculateEnvelopes self = new CalculateEnvelopes();
	
	private CalculateEnvelopes(){
		
	}
	
	public static CalculateEnvelopes getInstance(){
		return self;
	}
	
	public Envelopes getEnvelopes(LinkedList<Double> residual, LocalExtremas le){
		this.residual = residual;
		this.le = le;
		Envelopes envelopes = new Envelopes(new LinkedList<Double>(),  new LinkedList<Double>());
		// 1. Convert Datatype from Linkedlist to array
		double[] values = LinkedListToArray(residual);
		double[] upperextremas = LinkedListToArray(le.localMaxima());
		double[] lowerextremas = LinkedListToArray(le.localMinima());
		
		// 2. Prepare value array for interpolation.
		double[] uppervalues = extractValues(values, upperextremas);
		double[] lowervalues = extractValues(values, lowerextremas);
		
		// 3. Do Cubic Spline Interpolation
		upperCS = new CubicSpline(upperextremas, uppervalues);
		lowerCS = new CubicSpline(lowerextremas, lowervalues);
		// Calculate upperenvelope
		for(int i = 0 ; i < values.length ; i++){
			envelopes.upperEnvelope().add(upperCS.interpolate(i));
		}
		// Calculate lowerenvelope
		for(int i = 0 ; i < values.length ; i++){
			envelopes.lowerEnvelope().add(lowerCS.interpolate(i));
		}
		
		return envelopes;
	}
	
	
	/*
	 * Extract values from values array according to the index array.
	 * */
	private double[] extractValues(double[] values, double[] index){
		double[] extractions = new double[index.length];
		for(int i = 0 ; i < extractions.length ; i++){
			extractions[i] = values[(int) index[i]];
		}
		return extractions;
	}
	
	/*
	 * Convert LinkedList to Array
	 * */
	
	private double[] LinkedListToArray(LinkedList linkedlist){ // LinkedList may be different types
		double[] array = new double[linkedlist.size()];
		for(int i = 0 ; i < array.length ; i++){
			array[i] = (double)linkedlist.get(i);
		}
		return array;
	}

	
	
	
}
