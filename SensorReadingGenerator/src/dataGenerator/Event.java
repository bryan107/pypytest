package dataGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Patterns.AbruptPattern;
import Patterns.Pattern;
import Patterns.RandomPattern;
import Patterns.SinPattern;
import Patterns.StablePattern;

import fileAccessInterface.PropertyAgent;

public class Event {
	private double average;
	private double maxvalue;
	private double minvalue;
	Pattern p;
	private long attribute;
	private long sections;
	private static Log logger = LogFactory.getLog(Event.class);
	public Event(Pattern p){
		setupEvent();
	}
	
	private void setupEvent(){
		updateMaxValue(Double.valueOf(PropertyAgent.getInstance().getProperties("Event", "Maxvalue")));
		updateMinValue(Double.valueOf(PropertyAgent.getInstance().getProperties("Event", "Minvalue")));
		updateAverageValue(Double.valueOf(PropertyAgent.getInstance().getProperties("Event", "average")));
		updatePattern(PropertyAgent.getInstance().getProperties("Event", "Pattern"));
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
		this.attribute = Long.valueOf(PropertyAgent.getInstance().getProperties("Event", "Attribute"));
		this.sections = Long.valueOf(PropertyAgent.getInstance().getProperties("Event", "Sections"));
		return true;
	}
	
	public void updatePattern(Pattern p){
		this.p = p;
	}
	//TODO Implement this function
	public double value(long sectionnumber){
		double value = 0;
		return value;
	}
	
}
