package gad.core;

public class Reading {
	
	private double readingvalue;
	private short condition;
	
	public Reading(double value, short condition){
		this.readingvalue = value;
		this.condition = condition;
	}
	
	public double value(){
		return readingvalue;
	}
	public short isValid(){
		return condition;
	}
	
}
