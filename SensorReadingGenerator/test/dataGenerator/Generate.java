package dataGenerator;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import faultSymptom.DeviationReadingFault;
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

import junit.framework.TestCase;

public class Generate extends TestCase {
	FileAccessAgent f = new FileAccessAgent("C:\\TEST\\NULL.txt", "E:\\test.txt");
	private int sourceid = 1;
	public void test(){
		double noise = 0.01;
		
		creatSet(5, noise);
		creatSet(7, noise);
		creatSet(10, noise);
		creatSet(15, noise);
		creatSet(20, noise);
		
	}

	private void creatSet(int number, double noise) {
		EventSourceManager esmanager = new EventSourceManager();
		SensorManager smanager = new SensorManager();
		DeployMap.getInstance().clear();
		nodeSetup(smanager, number, noise);
		for(int r = 0 ; r < 30 ; r++){
			f.updatewritingpath("C:\\TEST\\source_1__"+ noise +"__NUM_"+ number +"__"+ r +".txt");
//			for(int i = 0 ; i < number ; i++){
//				for(int j = 0 ; j < number ; j++){
//					smanager.addNewSensor(i*20/(number-1), j*20/(number-1), 0.01);
//				}
//			}
			esmanager.addNewSource(sourceid, 10, 10);
			//Insert Fault
//			smanager.insertFault(3, new DeviationReadingFault());
			
			int round = Integer.valueOf(PropertyAgent.getInstance().getProperties("Event", tagAccumulation("Event" + sourceid, tagAccumulation("Pattern", "Sections"))));
			f.writeLineToFile("Total Round: " + round );
			f.writeLineToFile("Start");
			
			for(int i = 0 ; i <= round ; i++){
				Map<Integer, Double>readingpack = smanager.getReadingSet(esmanager.getEventSet(i));
				DecimalFormat df = new DecimalFormat("00.00");
				String outputstring = "";
				Set<Integer> key = readingpack.keySet();
				Iterator<Integer> iterator = key.iterator();
				while(iterator.hasNext()){
					int nodeid = iterator.next();
					outputstring = outputstring + nodeid + ":" + df.format(readingpack.get(nodeid))  + "\t";
				}
//				f.writeLineToFile("Round" + i);
				f.writeLineToFile(outputstring);
				System.out.println("Finished: " + df.format((double)i*100/round) + "%");
			}
		}
	}

	private void nodeSetup(SensorManager smanager, int number, double noise) {
		int[][] nodelocation = new int[number][2];	
		for(int i = 0 ; i < number ; i++){
				double xmax = Double.valueOf(PropertyAgent.getInstance().getProperties("Map", "X_Max"));
				double ymax = Double.valueOf(PropertyAgent.getInstance().getProperties("Map", "Y_Max"));
				int x = (int)(xmax*Math.random());
				int y = (int)(ymax*Math.random());
				for(int j = 0 ; j < i ; ){
					if(x == nodelocation[j][0] && y == nodelocation[j][1]){
						j = 0;
						x = (int)(xmax*Math.random());
						y = (int)(ymax*Math.random());
					}
					j++;
				}
				nodelocation[i][0] = x;
				nodelocation[i][1] = y;
				smanager.addNewSensor(x, y, noise);
			}
			String nodeinfo = "";
			for(int i = 0 ; i < number ; i++){
				nodeinfo = nodeinfo + "[" + i + "] X:" + nodelocation[i][0]+ " Y:" + nodelocation[i][1]; 
			}	
			f.writeLineToFile("Node location:");
			f.writeLineToFile(nodeinfo);
	}
	
	private String tagAccumulation(String string1, String string2){
		return (string1 + "." + string2);
	}
}
