package smartgrid.simulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import smartgrid.component.Consumer;
import smartgrid.component.Fault;
import smartgrid.component.FaultDeviationConstant;
import smartgrid.component.FaultDeviationProportion;
import smartgrid.component.FaultNull;
import smartgrid.component.Pattern;
import smartgrid.component.PatternManualChange;
import smartgrid.component.PatternRandom;
import smartgrid.component.PatternSin;
import smartgrid.component.PatternStable;
import smartgrid.component.Supplier;

import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

public class GridSimulator {

	//Net Attribute
	int suppliernumber, consumernumber;
	double suppliedenergy = 0;
	double consumedenergy = 0;
	Map<Integer, Supplier> suppliercluster = new HashMap<Integer, Supplier>();
	Map<Integer, Consumer> consumercluster = new HashMap<Integer, Consumer>();
	
	//Supplier Props Attribute
	double maxstorage, storage, consumption, averagegeneration, supplierpatternvariation, suppliernoise, sfchance;
	String supplierpattern;
	double[][] supplierpatternattribute;
	int supplierfault;
	
	//Consumer Props Attribute
	double averageconsumption, consumerpatternvariation, consumernoise, cfchance;
	String consumerpattern;
	double[][] consumerpatternattrubute;
	int consumerfault;
	
	//File Access Interface
	private FileAccessAgent fagent;
	private static Log logger = LogFactory.getLog(GridSimulator.class);
	
	
	public GridSimulator(String writtingaddress){
		updateWriteFileAddress(writtingaddress);
	}
	
	public void updateWriteFileAddress(String writtingaddress){
		 fagent = new FileAccessAgent(writtingaddress , "C:\\TEST\\NULL.txt");
	}
	
	public double run(int totalround){
		
		PropSetting();
		//Initial Supplier & Consumer Clusters
		for(int i = 0 ; i < suppliernumber ; i++){
			suppliercluster.put(i, new Supplier(maxstorage,
												storage,
												consumption,
												averagegeneration,
												getPattern(supplierpattern),
												supplierpatternvariation, 
												supplierpatternattribute, 
												suppliernoise, 
												new FaultNull()));
		}
		for(int i = 0 ; i < consumernumber ; i++){
			consumercluster.put(i, new Consumer(averageconsumption, 
												getPattern(consumerpattern), 
												consumerpatternvariation, 
												consumerpatternattrubute, 
												consumernoise, 
												new FaultNull()));
		}
		//Main Loop
		for(int round = 0 ; round < totalround ; round++){
			//Supplier Rounds
			String svalues = "";
			Iterator<Integer> its = suppliercluster.keySet().iterator();
			while(its.hasNext()){
				int key = its.next();
				if(Math.random() < sfchance && suppliercluster.get(key).isNormal()){
					double impactvalue = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Fault.ImpactValue"));
					suppliercluster.get(key).updateFault(getFault(supplierfault, impactvalue));
					logger.info("Round (" + round + "): Supplier Node ["+ key + "] is attacked");
				}
				double value = suppliercluster.get(key).supplyValue(totalround, round);
				svalues = svalues + key + ":" + value + "\t"; 
				suppliedenergy += value;
			}
			
			//Consumer Rounds
			String cvalues = "";
			Iterator<Integer> itc = consumercluster.keySet().iterator();
			while(itc.hasNext()){
				int key = itc.next();
				if(Math.random() < cfchance && consumercluster.get(key).isNormal()){
					double impactvalue = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Consumer.Fault.ImpactValue"));
					consumercluster.get(key).updateFault(getFault(consumerfault, impactvalue));
					logger.info("Round (" + round + "): Consumer Node ["+ key + "] is attacked");
				}
				double value = consumercluster.get(key).getDemand(totalround, round);
				cvalues = cvalues + key + ":" + value + "\t"; 
				consumedenergy += value;
			}
			String outputstring = "S\t" + svalues;
			fagent.writeLineToFile(outputstring);
			outputstring = "C\t" + cvalues;
			fagent.writeLineToFile(outputstring);
		}
		
		// Performance Calculation and Print 
		double avesenergy = suppliedenergy/(totalround * suppliercluster.size());
		double avecenergy = consumedenergy/(totalround * consumercluster.size());
		System.out.println();
		fagent.writeLineToFile("Average Energy Supply: " +  avesenergy);
		fagent.writeLineToFile("Average Energy Consume: " +  avecenergy);
		fagent.writeLineToFile("Total Average Energy: " +  (avesenergy - avecenergy));
		
		return avesenergy - avecenergy;
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
	
	private Fault getFault(int faulttype, double impactvalue){
		switch(faulttype){
			case 0:
				return new FaultNull();
			case 1:
				return new FaultDeviationProportion(impactvalue);
			case 2:
				return new FaultDeviationConstant(impactvalue);
			default:
				logger.error("Undefined Fault Type");
				return null;
		}
	}
	
	private void PropSetting(){
		//Net Setting
		int maxsupnum = Integer.valueOf(PropertyAgent.getInstance().getProperties("SET", "MaxSupplierNumber"));
		int minsupnum = Integer.valueOf(PropertyAgent.getInstance().getProperties("SET", "MinSupplierNumber"));
		int maxconnum = Integer.valueOf(PropertyAgent.getInstance().getProperties("SET", "MaxConsumerNumber"));
		int minconnum = Integer.valueOf(PropertyAgent.getInstance().getProperties("SET", "MinConsumerNumber"));
		
		suppliernumber = minsupnum + (int)((maxsupnum - minsupnum) * Math.random());
		consumernumber = minconnum + (int)((maxconnum - minconnum) * Math.random());
		
		//Supplier Props Setting
		maxstorage = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Maxstorage"));
		storage = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Storage"));
		consumption = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Consumption"));
		averagegeneration = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.AverageGeneration"));
		supplierpattern = PropertyAgent.getInstance().getProperties("SET", "Supplier.Pattern");
		supplierpatternvariation = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Pattern.Variation"));
		supplierpatternattribute = null; //Currently Unused parameter
		suppliernoise = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Noise"));
		supplierfault = Integer.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Fault"));
		sfchance = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.Fault.Chance"));
		
		//Consumer Prop Setting
		averageconsumption = Double.valueOf(PropertyAgent.getInstance().getProperties("SEt", "Consumer.AverageConsumption"));
		consumerpattern = PropertyAgent.getInstance().getProperties("SET", "Consumer.Pattern");
		consumerpatternvariation = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Consumer.Pattern.Variation"));
		consumerpatternattrubute = null; //Currently Unused parameter
		consumernoise = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Consumer.Noise"));
		consumerfault = Integer.valueOf(PropertyAgent.getInstance().getProperties("SET", "Consumer.Fault"));
		cfchance = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Consumer.Fault.Chance"));
	}
	
}
