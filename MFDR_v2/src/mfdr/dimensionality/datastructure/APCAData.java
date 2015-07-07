package mfdr.dimensionality.datastructure;

public class APCAData {
	private final double time, average, length;
	
	/**
	 * PAAData contains two parameters
	 * - time: the starting time of a given window.
	 * - average: the average value in that given window.
	 * @param time
	 * @param average
	 */
	public APCAData(double time, double average, int length){
		this.time = time;
		this.average = average;
		this.length = length;
	}
	
	public double time(){
		return this.time;
	}

	public double average(){
		return this.average;
	}
	
	public double length(){
		return this.length;
	}
}
