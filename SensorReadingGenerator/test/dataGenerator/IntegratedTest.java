package dataGenerator;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultSymptom.DeviationReadingFault;
import fileAccessInterface.FileAccessAgent;

import junit.framework.TestCase;

public class IntegratedTest extends TestCase {
	FileAccessAgent f = new FileAccessAgent("E:\\test.txt", "E:\\test.txt");
	
	public void test(){
		EventSourceManager esmanager = new EventSourceManager();
		SensorManager smanager = new SensorManager();
		for(int i = 0 ; i < 6 ; i++){
			for(int j = 0 ; j < 6 ; j++){
				smanager.addNewSensor(i*10, j*10, 0.05);
			}
		}
		esmanager.addNewSource(1, 25, 25);
		
		smanager.insertFault(3, new DeviationReadingFault());
		
		for(int i = 0 ; i <= 100 ; i++){
			Map<Integer, Double>readingpack = smanager.getReadingSet(esmanager.getEventSet(i));
			DecimalFormat df = new DecimalFormat("00.00");
			String outputstring = "";
			Set<Integer> key = readingpack.keySet();
			Iterator<Integer> iterator = key.iterator();
			while(iterator.hasNext()){
				int nodeid = iterator.next();
				outputstring = outputstring + "[" + nodeid + "] " + df.format(readingpack.get(nodeid))  + " ";
			}
			f.writeLineToFile("Round" + i);
			f.writeLineToFile(outputstring);
		}
		
	}
}
