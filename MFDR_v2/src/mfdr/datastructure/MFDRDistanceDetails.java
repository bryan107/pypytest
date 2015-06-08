package mfdr.datastructure;

public class MFDRDistanceDetails {

	private final double dist_trends;
	private final double dist_seasonal;
	private final double dist_noise;
	public MFDRDistanceDetails(double dist_trends, double dist_seasonal, double dist_noise){
		this.dist_trends = dist_trends;
		this.dist_seasonal = dist_seasonal;
		this.dist_noise = dist_noise;
	}
	
	public double trend(){
		return this.dist_trends;
	}
	
	public double seasonal(){
		return this.dist_seasonal;
	}
	
	public double noise(){
		return this.dist_noise;
	}
	
}
