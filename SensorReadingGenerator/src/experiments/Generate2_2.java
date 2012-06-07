package experiments;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import Patterns.Pattern;

import dataGenerator.DeployMap;
import dataGenerator.EventSourceManager;
import dataGenerator.SensorManager;

import faultSymptom.DeviationReadingFault;
import faultSymptom.FaultSymptom;
import faultSymptom.NoisyReadingFault;
import faultSymptom.NullFault;
import faultSymptom.StuckOfReadingFault;
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

import junit.framework.TestCase;

public class Generate2_2 extends TestCase {
	FileAccessAgent f = new FileAccessAgent("C:\\TEST\\NULL.txt",
			"C:\\TEST\\NULL.txt");
	private int sourceid = 1;
	int round = Integer.valueOf(PropertyAgent.getInstance().getProperties(
			"Event",
			tagAccumulation("Event" + sourceid,
					tagAccumulation("Pattern", "Sections"))));
	
	private String fileaddress = "EventChange\\";

	
	public void test() {
		double noise = 0.02;

		for(double i = 0.006 ; i <= 0.00601; i+=0.001){
			creatSet(5, noise, i);
			creatSet(7, noise,  i);
			creatSet(10, noise,  i);
			creatSet(15, noise,  i);
			creatSet(20, noise, i);
		}
		

	}

	private void creatSet(int number, double noise, double eventratio) {

		for (int r = 0; r < 30; r++) {
			// -------------------Reset Round-------------------

			DecimalFormat df2 = new DecimalFormat("0.000");
			f.updatewritingpath("C:\\TEST\\"+ fileaddress +"source_1__"
					+ df2.format(eventratio) + "__NUM_" + number + "__" + r + ".txt");
			f.writeLineToFile("Total Round: " + round);
			DeployMap.getInstance().clear();
			EventSourceManager esmanager = new EventSourceManager();
			esmanager.addNewSource(sourceid, 10, 10);
			SensorManager smanager = new SensorManager();
			nodeSetup(smanager, number, noise);
			f.writeLineToFile("Start");
			// -------------------------------------------------

			// -------------Event change Round ---------------
			Queue<int[]> eventchangeround = new LinkedList<int[]>();
			f.writeLineToFile("FaultCondition");

			// -------------------------------------------------

			f.writeLineToFile("Readings");
			for (int section = 0; section <= round; section++) {
				if(Math.random() <= eventratio){
					int[] eventinfo = new int[3];
					eventinfo[0] = section;
					eventinfo[1] = (int)(20*Math.random());
					eventinfo[2] = (int)(20*Math.random());
					esmanager.updateSourceLocation(sourceid, eventinfo[1], eventinfo[2]);
//					if(Math.random()>0.5){
//						esmanager.getEventSource(sourceid).updateAverageValue(10 + (average-10)*Math.random());
//						eventinfo[1] = 0;
//					}
//					else{
//						esmanager.getEventSource(sourceid).updateAttribute1(variance*Math.random());
//						eventinfo[1] = 1;
//					}			
					eventchangeround.add(eventinfo);
				}

				Map<Integer, Double> readingpack = smanager
						.getReadingSet(esmanager.getEventSet(section));
				DecimalFormat df = new DecimalFormat("00.00");
				String outputstring = "";
				Set<Integer> key = readingpack.keySet();
				Iterator<Integer> iterator = key.iterator();
				while (iterator.hasNext()) {
					int nodeid = iterator.next();
					outputstring = outputstring + nodeid + ":"
							+ df.format(readingpack.get(nodeid)) + "\t";
				}
				// f.writeLineToFile("Round" + i);
				f.writeLineToFile(outputstring);
				System.out.println("Finished: "
						+ df.format((double) section * 100 / round) + "%");
			}
			f.writeLineToFile("Event Change round:");
			while(eventchangeround.isEmpty()!=true){
				int[] eventinfo = eventchangeround.poll();
				f.writeLineToFile("Round:" + eventinfo[0]+" x:"+ eventinfo[1] + " y:" + eventinfo[2] );
			}
		}
	}

	private void nodeSetup(SensorManager smanager, int number, double noise) {
		int[][] nodelocation = new int[number][2];
		for (int i = 0; i < number; i++) {
			double xmax = Double.valueOf(PropertyAgent.getInstance()
					.getProperties("Map", "X_Max"));
			double ymax = Double.valueOf(PropertyAgent.getInstance()
					.getProperties("Map", "Y_Max"));
			int x = (int) (xmax * Math.random());
			int y = (int) (ymax * Math.random());
			for (int j = 0; j < i;) {
				if (x == nodelocation[j][0] && y == nodelocation[j][1]) {
					j = 0;
					x = (int) (xmax * Math.random());
					y = (int) (ymax * Math.random());
				}
				j++;
			}
			nodelocation[i][0] = x;
			nodelocation[i][1] = y;
			smanager.addNewSensor(x, y, noise);
		}
		String nodeinfo = "";
		for (int i = 0; i < number; i++) {
			nodeinfo = nodeinfo + "[" + i + "] X:" + nodelocation[i][0] + " Y:"
					+ nodelocation[i][1];
		}
		f.writeLineToFile("Node location:");
		f.writeLineToFile(nodeinfo);
	}

	private String tagAccumulation(String string1, String string2) {
		return (string1 + "." + string2);
	}
}
