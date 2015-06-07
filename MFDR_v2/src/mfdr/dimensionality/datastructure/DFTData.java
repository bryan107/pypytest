package mfdr.dimensionality.datastructure;

import mfdr.dimensionality.reduction.DFT;
import mfdr.utility.ValueComparator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DFTData {
	private static Log logger = LogFactory.getLog(DFTData.class);
	private double[] hilb;
	
	public DFTData(double[] hilb){
		this.hilb = hilb;
	}
	
	public double[] hilb(){
		return this.hilb;
	}
	
	public double[] hilb(long noc){
		double[] sortedhilb = new double[this.hilb.length];
		if(noc > this.hilb.length){
			logger.info("NOC too long");
			return null;
		}
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(bvc);
		LinkedList<Integer> number = new LinkedList<Integer>();
		for(int i = 0 ; i < hilb.length ; i++){
			map.put(i, this.hilb[i]);
		}
		sorted_map.putAll(map);
		// Extract number of coefficient
		Iterator<Integer> it = sorted_map.keySet().iterator();
		for(int j = 0 ; j < noc ; j++){
			number.add(it.next());
		}
		Collections.sort(number);
		for(int i = 0 ; i < hilb.length ; i++){
			if(!number.isEmpty() && i == number.peekFirst()){
				sortedhilb[i] = map.get(number.pop());
			} else{
				sortedhilb[i] = 0;
			}	
		}
		return sortedhilb;
	}
	
	
	
	public double[] hilb(boolean normalize, int signallength){
		double[] normalizedhilb = new double[this.hilb.length]; 
		for(int i = 0 ; i < this.hilb.length ; i++){
			/*
			 *  This normalisation function is especially designed for JWave library
			 *  change to normalizedhilb[i] = hilb[i] / Math.pow(signallength/2, 0.5)
			 *  if use JTransforms library.
			 */
			normalizedhilb[i] = hilb[i] * Math.pow(signallength/2, 0.5);
		}
		return normalizedhilb;
	}

	public int size(){
		return this.hilb.length;
	}
	
	public double value(int index){
		try {
			return this.hilb[index];
		} catch (Exception e) {
			System.out.println("the index over flow " + e);
			return 0;
		}
		
	}
}
