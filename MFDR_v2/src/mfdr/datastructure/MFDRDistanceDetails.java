package mfdr.datastructure;

public class MFDRDistanceDetails {

	private final double dist_pla;
	private final double dist_dwt;
	
	public MFDRDistanceDetails(double dist_pla, double dist_dwt){
		this.dist_pla = dist_pla;
		this.dist_dwt = dist_dwt;
	}
	
	public double pla(){
		return this.dist_pla;
	}
	
	public double dwt(){
		return this.dist_dwt;
	}
	
}
