package dataGenerator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fileAccessInterface.PropertyAgent;

public final class DeployMap {
	private static DeployMap self = new DeployMap();
	private Log logger = LogFactory.getLog(DeployMap.class);
	long xmax, ymax;
	double gridsize;
	Map<Integer, Integer[]> sensorlocation = new HashMap<Integer, Integer[]>();
	Map<Integer, Integer[]> eventsourcelocation = new HashMap<Integer, Integer[]>();
	
	public static DeployMap getInstance(){
		return self;
	}
	
	private DeployMap(){
		reloadMap();
	}
	
	public void clear(){
		sensorlocation.clear();
		eventsourcelocation.clear();
	}
	
	public void reloadMap(){
		xmax = Integer.valueOf(PropertyAgent.getInstance().getProperties("Map", "X_Max"));
		ymax = Integer.valueOf(PropertyAgent.getInstance().getProperties("Map", "Y_Max"));
		gridsize = Double.valueOf(PropertyAgent.getInstance().getProperties("Map", "GridSize"));
		logger.info("Successfully load Map propertoes");
	}
	
	public boolean addSensor(int nodeid, int x, int y){
		if(sensorlocation.containsKey(nodeid)){
			logger.warn("Map already contains the information of node[" + nodeid + "]");
			return false;
		}
		else{
			if(x <= xmax && y <= ymax){
				Integer[] location = {x,y};
				sensorlocation.put(nodeid, location);
				return true;
			}
			logger.warn("node[" + nodeid + "] location out of bound");
			return false;
		}
	}
	
	public boolean addEventSource(int eventsourceid, int x, int y){
		if(eventsourcelocation.containsKey(eventsourceid)){
			logger.warn("Map already contains the information of source[" + eventsourceid + "]");
			return false;
		}
		else{
			if(x <= xmax && y <= ymax){
				Integer[] location = {x,y};
				eventsourcelocation.put(eventsourceid, location);
				return true;
			}
			logger.warn("source[" + eventsourceid + "] location out of bound");
			return false;
		}
	}
	
	public void removeSensor(int nodeid){
		sensorlocation.remove(nodeid);
	}
	
	public void removeEventSource(int sourceid){
		eventsourcelocation.remove(sourceid);
	}
	
	public double getDistance(int nodeid, int sourceid){
		double distance;
		int x = sensorlocation.get(nodeid)[0] - eventsourcelocation.get(sourceid)[0];
		int y = sensorlocation.get(nodeid)[1] - eventsourcelocation.get(sourceid)[1];
		distance = Math.pow(Math.pow(x * gridsize, 2)  + Math.pow(y * gridsize, 2) , 0.5);
		return distance;
	}

}
