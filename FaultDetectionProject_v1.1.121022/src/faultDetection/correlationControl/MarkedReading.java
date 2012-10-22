package faultDetection.correlationControl;

import java.text.DecimalFormat;

public class MarkedReading {
	private int nodeid;
	private double reading;
	private double trustworthiness;
	private short devicecondition;
	private short readingcondition;
//	private final short FT = 0;
//	private final short LF = 1;
//	private final short LG = 2;
//	private final short GD = 3;
//	private final short UNKNOWN = 4;
	private final String[] faultcondition = { "FT", "LF", "LG", "GD", "UNKNOWN" };
	
	public MarkedReading(int nodeid, double reading, double trustworthiness, short readingcondition, short devicecondition){
		this.nodeid = nodeid;
		this.reading = reading;
		this.trustworthiness = trustworthiness;
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
	
	public double trustworthiness(){
		return trustworthiness;
	}
	public short readingContidion(){
		return readingcondition;
	}
	public short deviceCondition(){
		return devicecondition;
//		switch(devicecondition){
//		case FT:
//			return false;
//		case GD:
//		case UNKNOWN:
//		default:
//			return true;
//		}
	}
	
	public String toFormat(){
		DecimalFormat df = new DecimalFormat("00.00");
		String reading = ("["
				+ nodeid
				+ "] R:"+ df.format(reading())
				+ " T:"+ df.format(trustworthiness()*100)
				+ "% RC:"+ faultcondition[readingContidion()] 
				+ " DC:"+ faultcondition[deviceCondition()]);
		return reading;
	}
	
}
