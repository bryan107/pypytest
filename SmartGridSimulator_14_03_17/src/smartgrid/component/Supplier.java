package smartgrid.component;

import flanagan.analysis.Stat;

public class Supplier {

	private double averagegeneration;
	private double maxstorage;
	private double storage;
	private double consumption;
	private Pattern generationpattern;
	private double variation;
	private double[][] attribute;
	private double noise;
	private Fault fault;
	
	public Supplier(double maxstorage, double storage, double consumption, double averagegeneration, Pattern generationpattern, double variation, double[][] attribute, double noise, Fault fault){
		updateConsumption(consumption);
		updateMaxStorage(maxstorage);
		updateStorage(storage);
		updateGenerationPattern(generationpattern);
		updateAverageGeneration(averagegeneration);
		updateVariation(variation);
		updateAttribute(attribute);
		updateNoise(noise);
		updateFault(fault);
	}
	
	public void updateFault(Fault fault){
		this.fault = fault;
	}
	
	public void updateNoise(double noise){
		this.noise = noise;
	}
	
	public void updateStorage(double storage){
		this.storage = storage;
	}
	
	public void updateMaxStorage(double maxstorage){
		this.maxstorage = maxstorage;
	}
	
	public void updateConsumption(double consumption){
		this.consumption = consumption;
	}
	
	public void updateGenerationPattern(Pattern generationpattern){
		this.generationpattern = generationpattern;
	}
	
	public void updateAverageGeneration(double averagegeneration){
		this.averagegeneration = averagegeneration;
	}

	public void updateVariation(double variation){
		this.variation = variation;
	}
	
	public void updateAttribute(double[][] attribute){
		this.attribute =attribute;
	}
	
	public double generation(long sections, long sectionnumber){	
		double generatepower = averagegeneration + generationpattern.getValue(variation, attribute, sections, sectionnumber);
		generatepower =  Stat.normalInverseCDF(generatepower, generatepower*noise, Math.random());
//		if(Math.random() > 0.5)
//			generatepower = generatepower * (1 + Math.random() * noise);
//		else
//			generatepower = generatepower * (1 - Math.random() * noise);
		return generatepower;
	}
	
	//TODO Current version do not consider storage
	public double supplyValue(long sections, long sectionnumber){
//		double supply = storage + generation(sections, sectionnumber) - consumption;
//		if(supply > maxstorage)
//			storage = maxstorage;
//		else
//			storage = supply;
//		return storage;
		double value = generation(sections, sectionnumber) - consumption;
		double supply = fault.getValue(value);
		return supply;
	}
	
	public void supplytransmission(double transmitpower){
		storage -= transmitpower;
	}
	
	public boolean isNormal(){
		return fault.isNull();
	}
}
