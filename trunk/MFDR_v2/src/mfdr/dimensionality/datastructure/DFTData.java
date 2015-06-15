package mfdr.dimensionality.datastructure;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class DFTData {
	private Map<Integer, Double> hilb = new HashedMap();
	
	public DFTData(){
		
	}
	
	public Map<Integer, Double> getMap(){
		return this.hilb;
	}
}
