package dataGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import faultSymptom.FaultSymptom;

public class SensorManager {
	private static Log logger = LogFactory.getLog(SensorManager.class);
	Map<Integer, Sensor> sensorpack = new HashMap<Integer, Sensor>();
	int sensoridpool = 0;
	public SensorManager(){
		
	}
	
	public void addNewSensor(int xlocation, int ylocation, double noise){
		sensorpack.put(sensoridpool, new Sensor(sensoridpool, noise));
		DeployMap.getInstance().addSensor(sensoridpool, xlocation, ylocation);
		sensoridpool++;
	}
	
	public void removeSensor(int nodeid){
		sensorpack.remove(nodeid);
		DeployMap.getInstance().removeSensor(nodeid);
	}
	
	public void clear(){
		sensorpack.clear();
		DeployMap.getInstance().removeSensor();
	}
	
	public void insertFault(int nodeid, FaultSymptom faultsymptom){
		logger.info(faultsymptom.getKey() + " has been inserted into node[" + nodeid + "]");
		sensorpack.get(nodeid).insertFault(faultsymptom);
	}
	
//	public Map<Integer, Double> getReadingSet(Event event, double diffusion, double constant){
//		Map<Integer, Double> readingset = new HashMap<Integer, Double>();
//		Set<Integer> key = sensorpack.keySet();
//		Iterator<Integer> iterator = key.iterator();
//		while(iterator.hasNext()){
//			int nodeid = iterator.next();
//			readingset.put(nodeid, sensorpack.get(nodeid).getValue(eventvalue, diffusion, constant));
//		}
//		return  readingset;
//	}
	
	public Map<Integer, Double> getReadingSet(Event[] eventpack){
		Map<Integer, Double> readingset = new HashMap<Integer, Double>();
		Set<Integer> key = sensorpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int nodeid = iterator.next();
			readingset.put(nodeid, sensorpack.get(nodeid).getValue(eventpack));
		}
		return  readingset;
	}
	
}
