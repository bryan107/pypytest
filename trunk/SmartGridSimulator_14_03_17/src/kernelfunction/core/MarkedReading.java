package kernelfunction.core;

import java.text.DecimalFormat;

public class MarkedReading {
	private int nodeid;
	private double reading;
	private boolean devicecondition;
	private short readingcondition;
	private final String[] faultcondition = { "FT", "LF", "LG", "GD", "UN" };
	
	public MarkedReading(int nodeid, double reading, short readingcondition, boolean devicecondition){
		this.nodeid = nodeid;
		this.reading = reading;
		this.devicecondition = devicecondition;
		this.readingcondition = readingcondition;
	}
	
	public int id(){
		return nodeid;
	}
	
	public double value(){
		return reading;
	}
	
	public double reading(){
		return reading;
	}
	
	public short readingContidion(){
		return readingcondition;
	}
	public boolean deviceCondition(){
		return devicecondition;
	}
	
	public String toFormat(){
		DecimalFormat df = new DecimalFormat("00.00");
		String reading = ("["
				+ nodeid
				+ "] R:"+ df.format(reading())
				+ " RC:"+ faultcondition[readingContidion()] 
				+ " DC:"+ deviceCondition());
		return reading;
	}
	
}
