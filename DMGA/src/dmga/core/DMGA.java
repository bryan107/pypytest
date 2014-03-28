package dmga.core;

public class DMGA {

	private int hypernodesize;
	private double minlinkweight;
	private Matching m = new Matching();
	public DMGA(int nodesize, double minlinkweight){
		updateNodeSize(nodesize);
		updateMinLinkWeight(minlinkweight);
	}
	
	public void updateNodeSize(int hypernodesize){
		this.hypernodesize = hypernodesize;
	}
	
	public void updateMinLinkWeight(double minlinkweight){
		this.minlinkweight = minlinkweight;
	}
	
	public HyperGraph grouping(HyperGraph g){
		g.updateMinLinkeWeight(minlinkweight);
		int currentsize = 1;
		// Do non-overlap matching
		while(currentsize < hypernodesize){
			m.doMatching(g);
			currentsize*=2;
		}
		// Join residues
		g = m.residueJoin(g);
		return g;
	}
	
}
