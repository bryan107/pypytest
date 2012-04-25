package dataGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fileAccessInterface.PropertyAgent;

import Patterns.AbruptPattern;
import Patterns.Pattern;
import Patterns.RandomPattern;
import Patterns.SinPattern;
import Patterns.StablePattern;


public class EventSource {
	private int sourceid;
	private double average;
	private double maxvalue;
	private double minvalue;
	Pattern p;
	private double attribute1;
	private double attribute2;
	private long sections;
	private String eventfile = "Event";
	private static Log logger = LogFactory.getLog(EventSource.class);
	
	public EventSource(int sourceid){
		logger.info("New Event Created");
		updateEventSourceID(sourceid);
		setupEvent();
	}
	
	private void setupEvent(){
		updateMaxValue(Double.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation("Event" + sourceid, "Maxvalue"))));
		updateMinValue(Double.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation("Event" + sourceid, "Minvalue"))));
		updateAverageValue(Double.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation("Event" + sourceid, "Average"))));
		updatePattern(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation("Event" + sourceid, "Pattern")));
	}
	
	private String tagAccumulation(String string1, String string2){
		return (string1 + "." + string2);
	}
	
	public void updateEventFile(String eventfile){
		this.eventfile = eventfile;
	}
	public void updateEventSourceID(int sourcetid){
		this.sourceid = sourcetid; 
	}
	
	public void updateMaxValue(double maxvalue){
		this.maxvalue = maxvalue;
	}
	
	public void updateMinValue(double minvalue){
		this.minvalue = minvalue;
	}
	
	public void updateAverageValue(double average){
		this.average = average;
	}
	
	public void updateAttribute1(double attribute1){
		this.attribute1 = attribute1;
	}
	
	public void updateAttribute2(double attribute2){
		this.attribute2 = attribute2;
	}
	public void updateSections(long sections){
		this.sections = sections;
	}
	
	public boolean updatePattern(String patternname){
		switch(patternname){
		case "SIN":
			updatePattern(new SinPattern());
			break;
		case "RANDOM":
			updatePattern(new RandomPattern());
			break;
		case "STABLE":
			updatePattern(new StablePattern());
			break;
		case "ABRUPT":
			updatePattern(new AbruptPattern());
			break;	
		default:
			logger.warn("Pattern Name Null Warning in Event.properties");
			return false;
		}
		logger.warn("Setup event pattern = " + patternname);
		this.attribute1 = Double.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation(tagAccumulation("Event" + sourceid, "Pattern"), "Attribute1")));
		this.attribute2 = Double.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation(tagAccumulation("Event" + sourceid, "Pattern"), "Attribute2")));
		this.sections = Long.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation(tagAccumulation("Event" + sourceid, "Pattern"), "Sections")));
		return true;
	}
	
	public void updatePattern(Pattern p){
		this.p = p;
	}
	
	public Event getEvent(long sectionnumber){
		return new Event(sourceid, value(sectionnumber), diffusion(), constant());
	}
	
	public double value(long sectionnumber){
		double value = average + p.getCorrespondReading(attribute1, attribute2, sections, sectionnumber);
		if(value > maxvalue)
			return maxvalue;
		else if(value < minvalue)
			return minvalue;
		else
			return value;
	}
	
	public double diffusion(){
		return Double.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation("Event" + sourceid, "Diffusion")));
	}
	public double constant(){
		return Double.valueOf(PropertyAgent.getInstance().getProperties(eventfile, tagAccumulation("Event" + sourceid, "Constant")));
	}
}
