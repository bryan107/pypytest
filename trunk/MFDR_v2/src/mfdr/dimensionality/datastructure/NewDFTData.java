package mfdr.dimensionality.datastructure;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class NewDFTData {
	private Map<Integer, Double> hilb = new HashedMap();
	
	public NewDFTData(){
		
	}
	
	public Map<Integer, Double> getMap(){
		return this.hilb;
	}
}
