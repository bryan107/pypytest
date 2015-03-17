package mdfr.dimensionality.datastructure;

public class MDFRData {
	private final double time;
	private final PLAData pla;
	private final DWTData dwt;
	
	public MDFRData(double time, PLAData pla, DWTData dwt){
		this.time = time;
		this.pla = pla;
		this.dwt = dwt;
	}
	
	public double time(){
		return this.time;
	}
	
	public PLAData pla(){
		return this.pla;
	}
	
	public DWTData dwt(){
		return this.dwt;
	}
	
}
