package dataGenerator;

import java.util.HashMap;
import java.util.Map;

import fileAccessInterface.FileAccessAgent;

public class SensorDataGenerator {
	// Setup reading package (Auto data generating)
	// Packet variables
	private int readingpacksize;
	private int nodenumber;
	private double readingbaseline;
	private double[] baselinecurve;
	
	//Insert Errors
	private double noise = 0.05;
	private int[] errornodeid;
	Map<Integer, Integer> errorround;
	private double error = 1.0;
	private int newnoderound = 60;
	//Error types
	
	FileAccessAgent agent;
	
	public SensorDataGenerator(int readingpacksize, int nodenumber, double readingbaseline, double[] baselinecurve){
		updateReadingBaseline(readingbaseline);
		updateReadingPacksize(readingpacksize);
		updateNodeNumber(nodenumber);
		updateBaselineCurve(baselinecurve);
	}
	
	public void updateReadingPacksize(int readingpacksize){
		this.readingpacksize = readingpacksize;
	}
	public void updateNodeNumber(int nodenumber){
		this.nodenumber = nodenumber;
	}
	public void updateReadingBaseline(double readingbaseline){
		this.readingbaseline = readingbaseline;
	}
	public void updateBaselineCurve(double[] baselinecurve){
		this.baselinecurve = baselinecurve;
	}
	
	
	
	public void insertFaults(int[] errornodeid, Map<Integer, Integer> errorround, String errortype){
		
	}
	
	
	
	public Map<Integer, Map<Integer, Double>> setupReadingPack(double noise,
			int errornodeid, double error, double errorround,
			long newnoderound, double readingbaseline, long round,
			int nodenumber) {
		Map<Integer, Map<Integer, Double>> readingpack = new HashMap<Integer, Map<Integer, Double>>();

		for (int i = 0; i < round; i++) {
			Map<Integer, Double> readingpackentry = new HashMap<Integer, Double>();
			for (int j = 0; j < nodenumber; j++) {
				if (Math.random() < 0.5) {
					readingpackentry.put(j, (readingbaseline + (readingbaseline
							* noise * Math.random())));
				} else {
					readingpackentry.put(j, (readingbaseline - (readingbaseline
							* noise * Math.random())));
				}

			}
			if (i > errorround) {
				if (Math.random() < 0.5) {
					readingpackentry.put(errornodeid, readingbaseline
							+ (readingbaseline * error * Math.random()));
				} else {
					readingpackentry.put(errornodeid, readingbaseline
							- (readingbaseline * error * Math.random()));
				}

			}
			if (i > newnoderound) {
				readingpackentry.put(nodenumber,
						(readingbaseline + (readingbaseline * noise * Math
								.random())));
			}
			readingpack.put(i, readingpackentry);

		}
		return readingpack;
	}
}
