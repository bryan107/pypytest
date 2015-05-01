package mdfr.dimensionality.datastructure;

public class DFTData {
	
	private double[] hilb;
	
	public DFTData(double[] hilb){
		this.hilb = hilb;
	}
	
	public double[] hilb(){
		return this.hilb;
	}
	
	public double[] hilb(boolean normalize, int signallength){
		double[] normalizedhilb = new double[this.hilb.length]; 
		for(int i = 0 ; i < this.hilb.length ; i++){
			/*
			 *  This normalisation function is especially designed for JWave library
			 *  change to normalizedhilb[i] = hilb[i] / Math.pow(signallength/2, 0.5)
			 *  if use JTransforms library.
			 */
			normalizedhilb[i] = hilb[i] * Math.pow(signallength/2, 0.5);
		}
		return normalizedhilb;
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
