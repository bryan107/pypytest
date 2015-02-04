package mdfr.develop.toos;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

public class Print {

	DecimalFormat df = new DecimalFormat("0.000");
	
	private static Print self = new Print(); 
	private Print(){
		
	}
	public static Print getInstance(){
		return self;
	}
	
	public void setupFormat(String st){
		df = new DecimalFormat(st);
	}
	
	public void printLinkedList(LinkedList list){
		Iterator<Double> it = list.iterator();
		while(it.hasNext()){
			System.out.print(" "+ df.format(it.next()));
		}
		System.out.println();
	}
	
}
