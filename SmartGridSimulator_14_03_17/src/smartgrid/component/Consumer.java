package smartgrid.component;

import flanagan.analysis.Stat;

public class Consumer {

	private double averageconsumption;
	private Pattern consumepattern;
	private double variation;
	private double[][] attribute;
	private double noise;
	private Fault fault;
	
	public Consumer(double averageconsumption, Pattern consumepPattern, double variation, double[][] attribute, double noise, Fault fault){
		updateAverageConsumption(averageconsumption);
		updateConsumePattern(consumepPattern);
		updateVariation(variation);
		updateAttribute(attribute);
		updateFault(fault);
		updateNoise(noise);
	}
	
	public void updateFault(Fault fault){
		this.fault = fault;
	}
	
	public void updateNoise(double noise){
		this.noise = noise;
	}
	
	public void updateAverageConsumption(double averageconsumption){
		this.averageconsumption = averageconsumption;
	}
	
	public void updateConsumePattern(Pattern consumepattern){
		this.consumepattern = consumepattern;
	}
	
	public void updateVariation(double variation){
		this.variation = variation;
	}
	
	public void updateAttribute(double[][] attribute){
		this.attribute = attribute;
	}
	
	public double getDemand(long sections, long sectionnumber){
		double value = averageconsumption + consumepattern.getValue(variation, attribute, sections, sectionnumber);
		value =  Stat.normalInverseCDF(value, value*noise, Math.random());
//		if(Math.random() > 0.5)
//			value *=  (1 + Math.random() * noise);
//		else
//			value *=  (1 - Math.random() * noise);
		
		return fault.getValue(value);
	}
	
	
	public boolean isNormal(){
		return fault.isNull();
	}
}
