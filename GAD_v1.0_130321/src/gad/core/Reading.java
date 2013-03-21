package gad.core;

public class Reading {
	
	private double readingvalue;
	private boolean condition;
	
	public Reading(double value, boolean condition){
		this.readingvalue = value;
		this.condition = condition;
	}
	
	public double value(){
		return readingvalue;
	}
	public boolean isValid(){
		return condition;
	}
	
}
