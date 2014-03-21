package smartgrid.simulator;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;




import kernelfunction.core.Kernel;
import kernelfunction.core.ProcessedReadingPack;

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
import fileAccessInterface.FileAccessAgentT;
import fileAccessInterface.PropertyAgentT;

public class GridSimulatorWithKernel {

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
	Kernel ks = new Kernel();
	//Consumer Props Attribute
	double averageconsumption, consumerpatternvariation, consumernoise, cfchance;
	String consumerpattern;
	double[][] consumerpatternattrubute;
	int consumerfault;
	Kernel kc = new Kernel();
	
	//File Access Interface
	private FileAccessAgentT fagent;
	private static Log logger = LogFactory.getLog(GridSimulatorWithKernel.class);
	
	//Result Info
	// Attack added
	private String satkround = new String();
	private String catkround = new String();
	// Attack detected
	private String cfdetected = new String();
	private String sfdetected = new String();
	
	public GridSimulatorWithKernel(String writtingaddress, double averagegeneration, double averageconsumption){
		updateWriteFileAddress(writtingaddress);
		updateAverageConsumption(averageconsumption);
		updateAverageGeneration(averagegeneration);
		//Initiate Properties
		PropSetting();
	}
	
	public void updateConsumerFaultChance(double cfchance){
		this.cfchance = cfchance;
	}
	
	public void updateSupplierFaultChance(double sfchance){
		this.sfchance = sfchance;
	}
	
	public void updateAverageGeneration(double averagegeneration){
		this.averagegeneration = averagegeneration;
	}
	
	public void updateAverageConsumption(double averageconsumption){
		this.averageconsumption = averageconsumption;
	}
	
	public void updateWriteFileAddress(String writtingaddress){
		 fagent = new FileAccessAgentT(writtingaddress , "C:\\TEST\\Null.txt");
	}
	
	public double run(int totalround){
		//Initiate Supplier & Consumer Clusters
		initiateSuppliers();
		initiateConsumers();
		//Main Loop
		for(int round = 0 ; round < totalround ; round++){
			String svalues = runSuppliers(totalround, round);	
			String cvalues = runConsumers(totalround, round);
			outPutRoundResults(svalues, cvalues);
		}
		// Performance Calculation and Print 
		double avesenergy = suppliedenergy/(totalround);
		double avecenergy = consumedenergy/(totalround);
		outputResults(avesenergy, avecenergy);

		return avesenergy - avecenergy;
	}

	private void outputResults(double avesenergy, double avecenergy) {
		DecimalFormat df = new DecimalFormat("0.0000");
		fagent.writeLineToFile("Average Energy Supply: " +  df.format(avesenergy));
		fagent.writeLineToFile("Average Energy Consume: " +  df.format(avecenergy));
		fagent.writeLineToFile("SupplierAttackRound:");
		fagent.writeLineToFile(satkround);
		fagent.writeLineToFile("S FDI detected:");
		fagent.writeLineToFile(sfdetected);
		fagent.writeLineToFile("ConsumerAttackRound:");
		fagent.writeLineToFile(catkround);
		fagent.writeLineToFile("C FDI detected:");
		fagent.writeLineToFile(cfdetected);
		fagent.writeLineToFile("Total Average Energy:");
		fagent.writeLineToFile(df.format(avesenergy - avecenergy));
	}

	private void initiateSuppliers() {
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
//		logger.info("Suppliers Initiated");
	}

	private void initiateConsumers() {
		for(int i = 0 ; i < consumernumber ; i++){
			consumercluster.put(i, new Consumer(averageconsumption, 
												getPattern(consumerpattern), 
												consumerpatternvariation, 
												consumerpatternattrubute, 
												consumernoise, 
												new FaultNull()));
		}
//		logger.info("Consumers Initiated");
	}

	private void outPutRoundResults(String svalues, String cvalues) {
		String outputstring = "S\t" + svalues;
		fagent.writeLineToFile(outputstring);
		outputstring = "C\t" + cvalues;
		fagent.writeLineToFile(outputstring);
	}

	// One round consumer
	private String runConsumers(int totalround, int round) {
		String cvalues = "";
		Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
		Iterator<Integer> itc = consumercluster.keySet().iterator();
		while(itc.hasNext()){
			int key = itc.next();
			//-----------Setup Attack Rounds (Probability)-----------
			if(Math.random() < cfchance && consumercluster.get(key).isNormal() && round > 30){
				double impactvalue = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Consumer.Fault.ImpactValue"));
				consumercluster.get(key).updateFault(getFault(consumerfault, impactvalue));
//				logger.info("Round (" + round + "): Consumer Node ["+ key + "] is attacked");
				catkround += " [" + key + "]:" + round;
			}
			//-------------------------------------------------------
			double value = consumercluster.get(key).getDemand(totalround, round);
			readingpack.put(key, value);
		}
		ProcessedReadingPack prpack= kc.markReading(readingpack);
		for(int i = 0 ; i < suppliercluster.size(); i++){
			if (!prpack.markedReadingPack().get(i).deviceCondition()){ //If a "permanent" attack is detected, reset node
				cfdetected += " [" + i + "]:" + round;
				// Reset detected node into normal state.
				// TODO fix the not stop problem
				kc.resetNode(i); 
				consumercluster.get(i).updateFault(new FaultNull());
			}
			DecimalFormat df = new DecimalFormat("0.0000");
			cvalues = cvalues + i + ":" + df.format(prpack.markedReadingPack().get(i).reading()) + "\t"; 
			consumedenergy += prpack.markedReadingPack().get(i).reading();
		}
		return cvalues;
	}

	// One round supplier
	private String runSuppliers(int totalround, int round) {
		String svalues = "";
		Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
		Iterator<Integer> its = suppliercluster.keySet().iterator();
		while(its.hasNext()){
			int key = its.next();
			//-----------Setup Attack Rounds (Probability)-----------
			if(Math.random() < sfchance && suppliercluster.get(key).isNormal()){
				double impactvalue = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Fault.ImpactValue"));
				suppliercluster.get(key).updateFault(getFault(supplierfault, impactvalue));
				satkround += " [" + key + "]:" + round;
			}
			//-------------------------------------------------------
			double value = suppliercluster.get(key).supplyValue(totalround, round);
			readingpack.put(key, value);
		}
		ProcessedReadingPack prpack = ks.markReading(readingpack);
		for(int i = 0 ; i < suppliercluster.size(); i++){
			if (!prpack.markedReadingPack().get(i).deviceCondition()){ //If a "permanent" attack is detected, reset node
				sfdetected += " [" + i + "]:" + round;
				//
				ks.resetNode(i);
				suppliercluster.get(i).updateFault(new FaultNull());
			}
			DecimalFormat df = new DecimalFormat("0.0000");
			svalues = svalues + i + ":" + df.format(prpack.markedReadingPack().get(i).reading()) + "\t"; 
			suppliedenergy += prpack.markedReadingPack().get(i).reading();
		}
		return svalues;
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
		int maxsupnum = Integer.valueOf(PropertyAgentT.getInstance().getProperties("SET", "MaxSupplierNumber"));
		int minsupnum = Integer.valueOf(PropertyAgentT.getInstance().getProperties("SET", "MinSupplierNumber"));
		int maxconnum = Integer.valueOf(PropertyAgentT.getInstance().getProperties("SET", "MaxConsumerNumber"));
		int minconnum = Integer.valueOf(PropertyAgentT.getInstance().getProperties("SET", "MinConsumerNumber"));
		
		suppliernumber = minsupnum + (int)((maxsupnum - minsupnum) * Math.random());
		consumernumber = minconnum + (int)((maxconnum - minconnum) * Math.random());
		
		//Supplier Props Setting
		maxstorage = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Maxstorage"));
		storage = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Storage"));
		consumption = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Consumption"));
//		averagegeneration = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Supplier.AverageGeneration"));
		supplierpattern = PropertyAgentT.getInstance().getProperties("SET", "Supplier.Pattern");
		supplierpatternvariation = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Pattern.Variation"));
		supplierpatternattribute = null; //Currently Unused parameter
		suppliernoise = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Noise"));
		supplierfault = Integer.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Fault"));
		sfchance = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Supplier.Fault.Chance"));
		
		//Consumer Prop Setting
//		averageconsumption = Double.valueOf(PropertyAgent.getInstance().getProperties("SET", "Consumer.AverageConsumption"));
		consumerpattern = PropertyAgentT.getInstance().getProperties("SET", "Consumer.Pattern");
		consumerpatternvariation = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Consumer.Pattern.Variation"));
		consumerpatternattrubute = null; //Currently Unused parameter
		consumernoise = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Consumer.Noise"));
		consumerfault = Integer.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Consumer.Fault"));
		cfchance = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Consumer.Fault.Chance"));
//		logger.info("Properties Initiated");
	}
	
}
