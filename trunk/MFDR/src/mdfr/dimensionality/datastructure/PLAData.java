package mdfr.dimensionality.datastructure;

public class PLAData {
	// y = a_0 + a_1x
	double time, a0, a1;
	
	public PLAData(double time, double a0, double a1){
		this.time = time;
		this.a0 = a0;
		this.a1 = a1;
	}
	
	public double a0(){
		return this.a0;
	}
	
	public double a1(){
		return this.a1;
	}
	
	public double getValue(double x){
		return a0 + a1*x;
	}
	
}
