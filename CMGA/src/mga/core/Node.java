package mga.core;

import java.util.LinkedList;

public class Node {

	private int id;
	private LinkedList<Double> values = new LinkedList<Double>();
	
	public Node(int id){
		updateID(id);
	}
	
	public void addValue(double value){
		this.values.add(value);
	}
	
	public void addValue(LinkedList<Double> values){
		this.values.addAll(values);
	}
	
	public void updateID(int id){
		this.id = id;
	}
	
	public LinkedList<Double> values(){
		return values;
	}
	
	public int id(){
		return id;
	}

}
