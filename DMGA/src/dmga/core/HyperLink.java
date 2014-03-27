package dmga.core;

public class HyperLink {
	private int nodeid_i, nodeid_j;
	private double weight;
	
	public HyperLink(int nodeid_i, int nodeid_j, double weight){
		updateLink(nodeid_i, nodeid_j, weight);
	}
	public void updateLink(int nodeid_i, int nodeid_j, double weight){
		this.nodeid_i = nodeid_i;
		this.nodeid_j = nodeid_j;
		this.weight = weight;
	}
	public int[] node(){
		int[] nodepair = {nodeid_i, nodeid_j};
		return nodepair;
	}
	
	public double weight(){
		return weight;
	}
}
