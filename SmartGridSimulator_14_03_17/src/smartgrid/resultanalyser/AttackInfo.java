package smartgrid.resultanalyser;

public class AttackInfo {
	private int nodeid;
	private int round;
	public AttackInfo(int nodeid, int round){
		this.nodeid = nodeid;
		this.round = round;
	}
	
	public int nodeid(){
		return nodeid;
	}
	
	public int round(){
		return round;
	}
}
