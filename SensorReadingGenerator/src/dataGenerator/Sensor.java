package dataGenerator;

import faultSymptom.FaultSymptom;
import faultSymptom.NullFault;
import fileAccessInterface.PropertyAgent;

public class Sensor {
	//TODO change distance to Map for multiple events
	double noise; //reading = value +- value*noise
	int nodeid;
	FaultSymptom faultsymptom = new NullFault();
	public Sensor(int nodeid, double noise){
		updateNodeid(nodeid);
		updateNoise(noise);
	}
	
	public void updateNoise(double noise){
		this.noise = noise;
	}
	
	public void updateNodeid(int nodeid){
		this.nodeid = nodeid;
	}
	
	
	public void insertFault(FaultSymptom faultsymptom){
		this.faultsymptom = faultsymptom;
	}
	
	public double getValue(Event[] eventpack){
		double value = 0;
		for(int i = 0 ; i < eventpack.length ; i++){
			value += getValue(eventpack[i].value, eventpack[i].diffusion, eventpack[i].constant, DeployMap.getInstance().getDistance(nodeid, eventpack[i].sourceid));
		}
		if(Math.random() > 0.5){
			value = value + value * noise * Math.random();
		}else{
			value = value - value * noise * Math.random();
		}
		return faultsymptom.getValue(value, Double.valueOf(PropertyAgent.getInstance().getProperties("fault", faultsymptom.getKey() + ".Attribute")));
	}
	
	public double getValue(double eventvalue, double diffusion, double constant, double distance){
		return eventvalue/Math.pow((constant*distance + 1), diffusion);
	}
}
