package dataGenerator;

import eventDiffusivePattern.EventType;
import faultSymptom.FaultSymptom;
import faultSymptom.NullFault;
import fileAccessInterface.PropertyAgent;

public class Sensor {
	//TODO change distance to Map for multiple events
	double noise; //reading = value +- value*noise
	int nodeid;
	EventType eventtype;
	FaultSymptom faultsymptom = new NullFault();
	public Sensor(int nodeid, double noise, EventType eventtype){
		updateNodeid(nodeid);
		updateNoise(noise);
		updateEventtype(eventtype);
	}
	
	public void updateEventtype(EventType eventtype){
		this.eventtype = eventtype;
		//logger.info("Event type is set up to" + eventtype.toString);
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
		//TODO Need TEST
		return eventtype.getValue(eventvalue, diffusion, constant, distance);	
	}
}
