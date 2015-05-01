package mfdr.dimensionality.datastructure;

public class DWTData {
	private double[] hilb;
	
	public DWTData(double[] hilb){
		this.hilb = hilb;
	}
	
	public double[] hilb(){
		return this.hilb;
	}

	public int size(){
		return this.hilb.length;
	}
	
	public double value(int index){
		try {
			return this.hilb[index];
		} catch (Exception e) {
			System.out.println("the index over flow " + e);
			return 0;
		}
		
	}
}
