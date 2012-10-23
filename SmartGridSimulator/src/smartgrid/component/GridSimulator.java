package smartgrid.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fileAccessInterface.PropertyAgent;

public class GridSimulator {

	//Net Attribute
	int suppliernumber, consumernumber;
	double energy = 0;
	Map<Integer, Supplier> suppliercluster = new HashMap<Integer, Supplier>();
	Map<Integer, Consumer> consumercluster = new HashMap<Integer, Consumer>();
	
	//Supplier Props Attribute
	double maxstorage, storage, consumption, averagegeneration, supplierpatternvariation, suppliernoise;
	String supplierpattern;
	double[][] supplierpatternattribute;
	int supplierfault;
	
	//Consumer Props Attribute
	double averageconsumption, consumerpatternvariation, consumernoise;
	String consumerpattern;
	double[][] consumerpatternattrubute;
	int consumerfault;
	
	//File Access Interface
	PropertyAgent prop = new PropertyAgent("conf");
	
	private static Log logger = LogFactory.getLog(GridSimulator.class);
	
	
	public GridSimulator(){
		
	}
	
	public double run(int totalround){
		
		PropSetting();
		
		//Initial Supplier & Consumer Clusters
		for(int i = 0 ; i < suppliernumber ; i++){
			suppliercluster.put(i, new Supplier(maxstorage, storage , consumption , averagegeneration , getPattern(supplierpattern), supplierpatternvariation, supplierpatternattribute, suppliernoise, getFault(supplierfault)));
		}
		for(int i = 0 ; i < consumernumber ; i++){
			consumercluster.put(i, new Consumer(averageconsumption, getPattern(consumerpattern), consumerpatternvariation, consumerpatternattrubute, consumernoise, getFault(consumerfault)));
		}
		
		//Main Loop
		for(int round = 0 ; round < totalround ; round++){
			
			Iterator<Integer> its = suppliercluster.keySet().iterator();
			while(its.hasNext()){
				int key = its.next();
				//TODO add fault 
				energy += suppliercluster.get(key).supplyValue(totalround, round);
			}
			
			Iterator<Integer> itc = consumercluster.keySet().iterator();
			while(itc.hasNext()){
				int key = itc.next();
				//TODO add fault
				energy -= consumercluster.get(key).getDemand(totalround, round);
			}
		}
		
		
		return energy;
	}
	
	private Pattern getPattern(String patternname){
		if(patternname.equals("SIN"))
			return new PatternSin();
		else if(patternname.equals("STABLE"))
			return new PatternStable();
		else if(patternname.equals("RANDOM"))
			return new PatternRandom();
		else if(patternname.equals("MANUAL"))
			return new PatternManualChange();
		else
			return null;
	}
	
	private Fault getFault(int faulttype){
		switch(faulttype){
			case 0:
				return new FaultNull();
			case 1:
				return new FaultDeviationProportion();
			case 2:
				return new FaultDeviationConstant();
			default:
				logger.error("Undefined Fault Type");
				return null;
		}
	}
	
	private void PropSetting(){
		//Net Setting
		int maxsupnum = Integer.valueOf(prop.getProperties("SET", "MaxSupplierNumber"));
		int minsupnum = Integer.valueOf(prop.getProperties("SET", "MinSupplierNumber"));
		int maxconnum = Integer.valueOf(prop.getProperties("SET", "MaxConsumerNumber"));
		int minconnum = Integer.valueOf(prop.getProperties("SET", "MinConsumerNumber"));
		
		suppliernumber = minsupnum + (int)((maxsupnum - minsupnum) * Math.random());
		consumernumber = minconnum + (int)((maxconnum - minconnum) * Math.random());
		
		//Supplier Props Setting
		maxstorage = Double.valueOf(prop.getProperties("SET", "Supplier.Maxstorage"));
		storage = Double.valueOf(prop.getProperties("SET", "Supplier.Storage"));
		consumption = Double.valueOf(prop.getProperties("SET", "Supplier.Consumption"));
		averagegeneration = Double.valueOf(prop.getProperties("SET", "Supplier.AverageGeneration"));
		supplierpattern = prop.getProperties("SET", "Supplier.Pattern");
		supplierpatternvariation = Double.valueOf(prop.getProperties("SET", "Supplier.Pattern.Variation"));
		supplierpatternattribute = null; //Currently Unused parameter
		suppliernoise = Double.valueOf(prop.getProperties("SET", "Supplier.Noise"));
		supplierfault = Integer.valueOf(prop.getProperties("SET", "Supplier.Fault"));
		
		//Consumer Prop Setting
		averageconsumption = Double.valueOf(prop.getProperties("SEt", "Consumer.AverageConsumption"));
		consumerpattern = prop.getProperties("SET", "Consumer.Pattern");
		consumerpatternvariation = Double.valueOf(prop.getProperties("SET", "Consumer.Pattern.Variation"));
		consumerpatternattrubute = null; //Currently Unused parameter
		consumernoise = Double.valueOf(prop.getProperties("SET", "Consumer.Noise"));
		consumerfault = Integer.valueOf(prop.getProperties("SET", "Consumer.Fault"));
	}
	
}
