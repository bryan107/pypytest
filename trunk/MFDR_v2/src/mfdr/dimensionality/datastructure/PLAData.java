package mfdr.dimensionality.datastructure;

public class PLAData {
	// y = a_0 + a_1x
	double time, a0, a1;
	
	/**
	 * PLA Data contains three parameters
	 * - time: the starting time of a given window
	 * - a0: the constant of the linear representation
	 * - a1: the parameter of  x.
	 * @param time
	 * @param a0
	 * @param a1
	 */
	public PLAData(double time, double a0, double a1){
		this.time = time;
		this.a0 = a0;
		this.a1 = a1;
	}
	
	public double time(){
		return this.time;
	}
	
	/**
	 * a0 is constant, which is b
	 * @return
	 */
	public double a0(){
		return this.a0;
	}
	
	/**
	 * a1 is scale, which is a
	 * @return
	 */
	public double a1(){
		return this.a1;
	}
	
	public double getValue(double x){
		return a0 + a1*x;
	}
	
}
