package mfdr.dimensionality.datastructure;

public class PAAData {

	private double time, average;
	
	/**
	 * PAAData contains two parameters
	 * - time: the starting time of a given window.
	 * - average: the average value in that given window.
	 * @param time
	 * @param average
	 */
	public PAAData(double time, double average){
		this.time = time;
		this.average = average;
	}
	
	public double time(){
		return this.time;
	}

	public double average(){
		return this.average;
	}
}
