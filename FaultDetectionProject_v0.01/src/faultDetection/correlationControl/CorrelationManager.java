package faultDetection.correlationControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class CorrelationManager {
	//----------------------------Variables---------------------------------
	private Map<Integer, Map<Integer, Correlation>> correlationmap = new HashMap<Integer, Map<Integer, Correlation>>(); 
	private List<Integer> nodeindex = new ArrayList<Integer>(); 
	private Map<Integer, Map<Integer, Double>> correlationtable = new HashMap<Integer, Map<Integer,Double>>();
	private int mapsize;
	private ReadingBuffer buffer =new ReadingBuffer();	
	
	private static Log logger = LogFactory.getLog(CorrelationManager.class);
	//----------------------------------------------------------------------
	
	//--------------------------Public Functions----------------------------
	public CorrelationManager(int mapsize){
		this.mapsize = mapsize;
	}

	public void updateMapSize(int mapsize){
		this.mapsize = mapsize;
	}
	public void putReading(int nodeid, double reading){
		for(int i : nodeindex){
			if(i == nodeid){
//				logger.info("GetData=> Node: " + nodeid + " Reading:" + reading);
				buffer.putBufferData(nodeid, reading);
				return;
			}
		}
		logger.info("New Node" + nodeid + " has been added");
		addNewNode(nodeid);
		logger.info("Index Size = " + nodeindex.size());
		buffer.putBufferData(nodeid, reading);
	}
	
	public void removeNode(int nodeid){
		correlationmap.remove(nodeid);
		for(int i = 0 ; i < correlationmap.size() ; i++){
			correlationmap.get(nodeindex.get(i)).remove(nodeid);
		}
	}
	public Map<Integer, Map<Integer, Double>> getCorrelationTable(){
		bufferToCorrelationt();
		updateCorrelationTable();
		return correlationtable;
	}
	public void updateCorrelations(){
		bufferToCorrelationt();
	}

	//--------------------------Private Functions---------------------------
	private void bufferToCorrelationt(){	
		for(int i : nodeindex){
			for(int j : nodeindex){
				if(j != i){
//					logger.info("readfrombuffer: " + buffer.getBufferData(i) + " " + buffer.getBufferData(j));
					correlationmap.get(i).get(j).addPair(buffer.getBufferData(i), buffer.getBufferData(j));
				}
			}
		}
		
//		Map<Integer, Double> bufferdata = buffer.getBufferData();
//		Set<Integer> key = bufferdata.keySet();
//		Iterator<Integer> iterator1 = key.iterator(); 
//		while(iterator1.hasNext()){
//			Iterator<Integer> iterator2 = iterator1; 
//			int i = iterator1.next();
//			logger.info("First node:" + i);
//			iterator2.next();
//			while(iterator2.hasNext()){
//				int j = iterator2.next();
//				logger.info("Second node:" + j);
//				correlationmap.get(i).get(j).addPair(bufferdata.get(i), bufferdata.get(j));
//			}
//		}
	}
	private void addNewNode(int nodeid){
		newCorrelationMapEntry(nodeid);
		newCorrelationTableEntry(nodeid);
		nodeindex.add(nodeid);		//put last to avoid self-reference
	}
	private void newCorrelationMapEntry(int nodeid){
		for(int i = 0 ; i < correlationmap.size() ; i++){
			correlationmap.get(nodeindex.get(i)).put(nodeid, new Correlation(mapsize));
		}
		correlationmap.put(nodeid, newCorrelationMapList());
	}
	
	private void newCorrelationTableEntry(int nodeid){
		for(int i = 0 ; i < correlationtable.size() ; i++){
			correlationtable.get(nodeindex.get(i)).put(nodeid, (double) 0);
		}
		correlationtable.put(nodeid, newCorrelationTableList());
	}
	
	private Map<Integer, Correlation> newCorrelationMapList(){
		Map<Integer, Correlation> newnodecorrelation = new HashMap<Integer, Correlation>();
		for(int i = 0 ; i < nodeindex.size() ; i++){
			newnodecorrelation.put(nodeindex.get(i) ,new Correlation(mapsize));
		}
		return newnodecorrelation;
	}
	
	private Map<Integer, Double> newCorrelationTableList(){
		Map<Integer, Double> newnodecorrelationlist = new HashMap<Integer, Double>();
		for(int i = 0 ; i < nodeindex.size() ; i++){
			newnodecorrelationlist.put(nodeindex.get(i) ,(double) 0);
		}
		return newnodecorrelationlist;
	}
	//Update the newest correlation strengths to correlation table
	private void updateCorrelationTable(){
		for(int i : nodeindex){
			for(int j : nodeindex){
				if(j != i){
					double correlationstrength = correlationmap.get(i).get(j).getCorrelation();
					correlationtable.get(i).put(j, correlationstrength);
					correlationtable.get(j).put(i, correlationstrength);
				}
			}
		}
	}
	
	//-----------------------------------------------------------------------


}
