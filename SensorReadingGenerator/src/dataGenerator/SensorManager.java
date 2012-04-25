package dataGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SensorManager {
//TODO Finish this Manager to handling Sensors
	Map<Integer, Sensor> sensorpack = new HashMap<Integer, Sensor>();
	int sensoridpool = 0;
	public SensorManager(){
		
	}
	
	public void addNewSensor(double distance, double noise){
		sensorpack.put(sensoridpool, new Sensor(distance, noise));
		sensoridpool++;
	}
	
	public Map<Integer, Double> getReadingSet(double eventvalue, double diffusion, double constant){
		Map<Integer, Double> readingset = new HashMap<Integer, Double>();
		Set<Integer> key = sensorpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int nodeid = iterator.next();
			readingset.put(nodeid, sensorpack.get(nodeid).getValue(eventvalue, diffusion, constant));
		}
		return  readingset;
	}
	
	public Map<Integer, Double> getReadingSet(double[][] eventpack ){
		Map<Integer, Double> readingset = new HashMap<Integer, Double>();
		Set<Integer> key = sensorpack.keySet();
		Iterator<Integer> iterator = key.iterator();
		while(iterator.hasNext()){
			int nodeid = iterator.next();
			readingset.put(nodeid, sensorpack.get(nodeid).getValue(eventvalue, diffusion, constant));
		}
		return  readingset;
	}
	
}
