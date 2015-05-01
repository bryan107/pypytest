package mdfr.learning.datastructure;

public class TrainingSet {

	private final double trendlength, freqlength, originlength;
	
	public TrainingSet(double trendlength, double freqlength, double originlength){
		this.trendlength = trendlength;
		this.freqlength = freqlength;
		this.originlength = originlength;
	}
	
	public double trendLength(){
		return this.trendlength;
	}
	
	public double freqLength(){
		return this.freqlength;
	}
	
	public double originLength(){
		return this.originlength;
	}
	
}
