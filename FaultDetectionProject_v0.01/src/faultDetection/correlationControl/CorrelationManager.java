package faultDetection.correlationControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CorrelationManager {
	//------------------------Private Variables------------------------------
	private Map<Integer, Map<Integer, Correlation>> correlationmap = new HashMap<Integer, Map<Integer, Correlation>>(); 
	private List<Integer> nodeindex = new ArrayList<Integer>(); 
	private Map<Integer, Map<Integer, Double>> correlationtrendtable = new HashMap<Integer, Map<Integer,Double>>();
	//Correlation Trend Table is the estimate correlation calculated with regression
	private Map<Integer, Map<Integer, Double>> correlationtable = new HashMap<Integer, Map<Integer,Double>>();
	//Correlation Table is the correlation between readings in the reading buffer
	private int samplesize;
	private int correlationpower;
	private ReadingBuffer buffer =new ReadingBuffer();	
	
	private static Log logger = LogFactory.getLog(CorrelationManager.class);
	//----------------------------------------------------------------------
	
	//----------------------------Constructor-------------------------------
	public CorrelationManager(int samplesize, int correlationpower){
		updateSampleSize(samplesize);
		updateCorrelationPower(correlationpower);
	}
	//----------------------------------------------------------------------
	//--------------------------Public Functions----------------------------

	public void updateSampleSize(int samplesize){
		this.samplesize = samplesize;
	}
	
	public void updateCorrelationPower(int correlationpower){
		this.correlationpower = correlationpower;
	}
	
	public void putReading(int nodeid, double reading){
		for(int i : nodeindex){
			if(i == nodeid){
//				logger.info("GetData=> Node: " + nodeid + " Reading:" + reading);
				buffer.putBufferData(nodeid, Math.pow(reading, (1.0/correlationpower)));
				logger.info("Node[" + nodeid + "] has been update");
				return;
			}
		}
		logger.info("New Node[" + nodeid + "] has been added");
		addNewNode(nodeid);
		logger.info("Index Size = " + nodeindex.size());
//		logger.info("Reading = " + reading + " Correlation = " + Math.pow(reading, (1.0/correlationpower)) + "Power" + correlationpower);
		buffer.putBufferData(nodeid, Math.pow(reading, (1.0/correlationpower)));
	}
	public void putReading(Map<Integer, Double> reading){
		Set<Integer> key = reading.keySet();
		Iterator<Integer> iterator = key.iterator();
		for(double i : reading.values()){
			buffer.putBufferData(iterator.next(), Math.pow(i, (1.0/correlationpower)));
		}
	}
	public Map<Integer, Map<Integer, Double>> getCorrelationTable(){//get correlation from reading buffer	
		Map<Integer, Double> reading = buffer.getBufferData();
		Set<Integer> key = reading.keySet();
		Iterator<Integer> iterator= key.iterator();
		for(double i : reading.values()){
			int nodei = iterator.next();
			Set<Integer> key2 = reading.keySet();
			Iterator<Integer> iterator2= key2.iterator();
			Map<Integer, Double> temp = new HashMap<Integer, Double>();
			for(double j : reading.values()){
				int nodej = iterator2.next();
				
				if(nodei != nodej){
					temp.put(nodej, (j/i));
				}
			}
			correlationtable.put(nodei, temp);
		}		
		if(correlationtable.size() == 0){
			logger.warn("Warn: Null correlation table");
			return null;
		}
		return correlationtable;
	}
	public void removeNode(int nodeid){
		correlationmap.remove(nodeid);
		for(int i = 0 ; i < correlationmap.size() ; i++){
			correlationmap.get(nodeindex.get(i)).remove(nodeid);
		}
	}
	public Map<Integer, Map<Integer, Double>> getCorrelationTrendTable(){//get correlation from regression correlation
//		bufferToCorrelationt();
		updateCorrelationTable();
		if(correlationtrendtable == null){
			logger.warn("Warn: Null correlation trend table");
			return null;
		}
		return correlationtrendtable;
	}
	//TODO only update the readings qualified by DFDEngine
	public void updateCorrelations(){//From buffer to correlation Map
		bufferToCorrelationt();
	}
	//----------------------------------------------------------------------
	//--------------------------Private Functions---------------------------
	private void bufferToCorrelationt(){	
		for(int i : nodeindex){
			for(int j : nodeindex){
				if(j != i){
					logger.info("readfrombuffer: " + buffer.getBufferData(i) + " " + buffer.getBufferData(j));
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
			correlationmap.get(nodeindex.get(i)).put(nodeid, new Correlation(samplesize));
		}
		correlationmap.put(nodeid, newCorrelationMapList());
	}
	
	private void newCorrelationTableEntry(int nodeid){
		for(int i = 0 ; i < correlationtrendtable.size() ; i++){
			correlationtrendtable.get(nodeindex.get(i)).put(nodeid, (double) 0);
		}
		correlationtrendtable.put(nodeid, newCorrelationTableList());
	}
	
	private Map<Integer, Correlation> newCorrelationMapList(){
		Map<Integer, Correlation> newnodecorrelation = new HashMap<Integer, Correlation>();
		for(int i = 0 ; i < nodeindex.size() ; i++){
			newnodecorrelation.put(nodeindex.get(i) ,new Correlation(samplesize));
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
					double correlation = correlationmap.get(i).get(j).getCorrelation();
					correlationtrendtable.get(i).put(j, correlation);
//					correlationtable.get(j).put(i, correlation);
				}
			}
		}
	}
	
	//-----------------------------------------------------------------------


}
