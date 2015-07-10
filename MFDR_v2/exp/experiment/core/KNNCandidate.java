package experiment.core;

public class KNNCandidate {

	final private int cluster_id;
	final private double dist;
	
	public KNNCandidate(int cluster_id , double dist){
		this.cluster_id = cluster_id;
		this.dist = dist;
	}
	
	public int ID(){
		return this.cluster_id;
	}
	
	public double dist(){
		return this.dist;
	}
	
	
}
