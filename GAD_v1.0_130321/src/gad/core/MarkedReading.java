package gad.core;

import java.text.DecimalFormat;

public class MarkedReading {
	private int nodeid;
	private double reading;
	private short devicecondition;
	private short readingcondition;
	private final short FT = 0;
	private final short GD = 3;
	private final short UN = 4;
	private final String[] faultcondition = { "FT", "LF", "LG", "GD", "UN" };
	
	public MarkedReading(int nodeid, double reading, double trustworthiness, short readingcondition, short devicecondition){
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
	public short deviceCondition(){
		return devicecondition;
	}
	
	public String toFormat(){
		DecimalFormat df = new DecimalFormat("00.00");
		String reading = ("["
				+ nodeid
				+ "] R:"+ df.format(reading())
				+ "% RC:"+ faultcondition[readingContidion()] 
				+ " DC:"+ faultcondition[deviceCondition()]);
		return reading;
	}
	
}
