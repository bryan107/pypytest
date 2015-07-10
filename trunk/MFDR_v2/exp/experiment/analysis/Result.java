package experiment.analysis;

public class Result {
	final private String dataset,DRtech;
	final private int NoC;
	final private double mean, variance, time;
	
	public Result(String dataset, String DRtech, int NoC, double mean, double variance, double time){
		this.dataset = dataset;
		this.DRtech = DRtech;
		this.NoC = NoC;
		this.mean = mean;
		this.variance = variance;
		this.time = time;
	}
	
	public String dataSet(){
		return this.dataset;
	}
	public String dRTech(){
		return this.DRtech;
	}
	public int NoC(){
		return this.NoC;
	}
	public double mean(){
		return this.mean;
	}
	public double variance(){
		return this.variance;
	}
	public double time(){
		return this.time;
	}
}
