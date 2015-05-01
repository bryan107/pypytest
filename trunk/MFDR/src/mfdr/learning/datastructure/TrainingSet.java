package mfdr.learning.datastructure;

public class TrainingSet {

	private final double trenddist, freqdist, origindist;
	
	public TrainingSet(double trenddist, double freqdist, double origindist){
		this.trenddist = trenddist;
		this.freqdist = freqdist;
		this.origindist = origindist;
	}
	
	public double trendDist(){
		return this.trenddist;
	}
	
	public double freqDist(){
		return this.freqdist;
	}
	
	public double originDist(){
		return this.origindist;
	}
	
}
