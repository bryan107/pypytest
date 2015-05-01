package mfdr.dimensionality.datastructure;

import java.util.LinkedList;

public class ReversedMDFRData {
	
	private final LinkedList<PLAData> pla;
	private final DWTData dwt;
	
	public ReversedMDFRData(DWTData dwt, LinkedList<PLAData> pla){
		this.pla = pla;
		this.dwt = dwt;
	}
	
	public LinkedList<PLAData> pla(){
		return this.pla;
	}
	
	public DWTData dwt(){
		return this.dwt;
	}
	
}
