package smartgrid.component;

public class Supplier {

	double averagegeneration;
	double maxstorage;
	double storage;
	double consumption;
	Pattern generationpattern;
	double variation;
	double[][] attribute;
	
	public Supplier(double maxstorage, double storage, double consumption, double averagegeneration, Pattern generationpattern){
		updateConsumption(consumption);
		updateMaxStorage(maxstorage);
		updateStorage(storage);
		updateGenerationPattern(generationpattern);
		updateAverageGeneration(averagegeneration);
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
		return averagegeneration + generationpattern.getValue(variation, attribute, sections, sectionnumber);
	}
	
	public double supplyValue(long sections, long sectionnumber){
		double supply = storage + generation(sections, sectionnumber) - consumption;
		if(supply > maxstorage)
			storage = maxstorage;
		else
			storage = supply;
		return storage;
	}
	
	public void supplytransmission(double transmitpower){
		storage -= transmitpower;
	}
}
