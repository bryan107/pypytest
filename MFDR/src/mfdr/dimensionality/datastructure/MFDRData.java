package mfdr.dimensionality.datastructure;

public class MFDRData {
	private final double time;
	private final PLAData pla;
	private final DWTData dwt;
	
	public MFDRData(double time, PLAData pla, DWTData dwt){
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
