package experiments;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import dataGenerator.DeployMap;
import dataGenerator.EventSourceManager;
import dataGenerator.SensorManager;
import eventDiffusivePattern.DiffusiveEvent;
import eventDiffusivePattern.EventType;
import eventDiffusivePattern.ExponentialEvent;
import eventDiffusivePattern.LogEvent;
import eventDiffusivePattern.NonDiffusionEvent;

import faultSymptom.DeviationReadingFault;
import faultSymptom.FaultSymptom;
import faultSymptom.NoisyReadingFault;
import faultSymptom.NullFault;
import faultSymptom.StuckOfReadingFault;
import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;

import junit.framework.TestCase;

public class Generate_Fault extends TestCase {
	FileAccessAgent f = new FileAccessAgent("C:\\TEST\\NULL.txt",
			"C:\\TEST\\NULL.txt");
	private int sourceid = 1;
	private EventType eventtype;
	int round = Integer.valueOf(PropertyAgent.getInstance().getProperties(
			"Event",
			tagAccumulation("Event" + sourceid,
					tagAccumulation("Pattern", "Sections"))));

	public void test() {
		//=================NOISE SETTING===================
		double noise = 0.02;						
		//=================================================
		for(double i = 0.1 ; i <= 0.6; i+=0.1){
//			creatSet(5, noise, new NoisyReadingFault(), i);
//			creatSet(7, noise, new NoisyReadingFault(), i);
//			creatSet(10, noise, new NoisyReadingFault(), i);
//			creatSet(15, noise, new NoisyReadingFault(), i);
//			creatSet(20, noise, new NoisyReadingFault(), i);
			creatSet(5, noise, new DeviationReadingFault(), i);
			creatSet(7, noise, new DeviationReadingFault(), i);
			creatSet(10, noise, new DeviationReadingFault(), i);
			creatSet(15, noise, new DeviationReadingFault(), i);
			creatSet(20, noise, new DeviationReadingFault(), i);
//			creatSet(5, noise, new StuckOfReadingFault(), i);
//			creatSet(7, noise, new StuckOfReadingFault(), i);
//			creatSet(10, noise, new StuckOfReadingFault(), i);
//			creatSet(15, noise, new StuckOfReadingFault(), i);
//			creatSet(20, noise, new StuckOfReadingFault(), i);
		}
		

	}

	private void creatSet(int number, double noise, FaultSymptom faultsymptom,
			double faultratio) {
		setupEventType();
		for (int r = 0; r < 30; r++) {
			// -------------------Reset Round-------------------

			DecimalFormat df2 = new DecimalFormat("0.0");
			f.updatewritingpath("C:\\TEST\\FaultType\\" + eventtype.toString() + "\\source_1__" + faultsymptom.getKey() + "__"
					+ df2.format(faultratio) + "__NUM_" + number + "__" + r + ".txt");
			f.writeLineToFile("Total Round: " + round);
			DeployMap.getInstance().clear();
			EventSourceManager esmanager = new EventSourceManager();
			esmanager.addNewSource(sourceid, 10, 10);
			SensorManager smanager = new SensorManager();
			nodeSetup(smanager, number, noise);
			f.writeLineToFile("Start");
			// -------------------------------------------------

			// -------------Fault Node and Round ---------------
			int faultnum = (int) Math.round((number * faultratio));
			int[] faultround = new int[faultnum];
			for (int i = 0; i < faultnum; i++) {
				faultround[i] = (int) Math.ceil((Math.random() * round));
			}
			f.writeLineToFile("FaultCondition");
			String outputstring = "";
			for (int nodeid = 0; nodeid < faultnum; nodeid++) {
				outputstring = outputstring + nodeid + ":" + faultround[nodeid]
						+ "\t";
			}
			f.writeLineToFile(outputstring);
			// -------------------------------------------------

			f.writeLineToFile("Readings");
			for (int section = 0; section <= round; section++) {
				for (int nodeid = 0; nodeid < faultnum; nodeid++) {
					if (section == faultround[nodeid]) {
						smanager.insertFault(nodeid, faultsymptom);
					}
				}
				Map<Integer, Double> readingpack = smanager
						.getReadingSet(esmanager.getEventSet(section));
				DecimalFormat df = new DecimalFormat("00.00");
				outputstring = "";
				Set<Integer> key = readingpack.keySet();
				Iterator<Integer> iterator = key.iterator();
				while (iterator.hasNext()) {
					int nodeid = iterator.next();
					outputstring = outputstring + nodeid + ":"
							+ df.format(readingpack.get(nodeid)) + "\t";
				}
				// f.writeLineToFile("Round" + i);
				f.writeLineToFile(outputstring);

			}
			DecimalFormat df = new DecimalFormat("00.00");
			System.out.println("SYSTEM: " + df.format((double) r * 100 / 30) + "%  processes are finished");
		}
	}

	private void setupEventType() {
		switch(Integer.valueOf(PropertyAgent.getInstance().getProperties("Event", tagAccumulation("Event" + sourceid, "Type")))){
		case 0:
			eventtype = new NonDiffusionEvent();
			break;
		case 1:
			eventtype = new DiffusiveEvent();
			break;
		case 2:
			eventtype = new LogEvent();
			break;
		case 3:
			eventtype = new ExponentialEvent();
		default:
			break;
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
			smanager.addNewSensor(x, y, noise,eventtype);
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
