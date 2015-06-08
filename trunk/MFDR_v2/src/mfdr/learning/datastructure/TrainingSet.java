package mfdr.learning.datastructure;

public class TrainingSet {

	private final double dist_trend, dist_seasonal, dist_noise, dist_origianl;
	
	public TrainingSet(double dist_trend, double dist_seasonal, double dist_noise, double dist_origianl){
		this.dist_trend = dist_trend;
		this.dist_seasonal = dist_seasonal;
		this.dist_noise = dist_noise;
		this.dist_origianl = dist_origianl;
	}
	
	public double trendDist(){
		return this.dist_trend;
	}
	
	public double seasonalDist(){
		return this.dist_seasonal;
	}
	
	public double noiseDist(){
		return this.dist_noise;
	}
	
	public double originalDist(){
		return this.dist_origianl;
	}
	
}
