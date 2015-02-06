package mdfr.develop.toos;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import mdfr.math.emd.Data;

public class Print {

	DecimalFormat valuedf = new DecimalFormat("0.000");
	DecimalFormat timedf = new DecimalFormat("0");
	
	private static Print self = new Print(); 
	private Print(){
		
	}
	public static Print getInstance(){
		return self;
	}
	
	public void setupValueFormat(String st){
		this.valuedf = new DecimalFormat(st);
	}
	
	public void setupTimeFormat(String st){
		this.timedf = new DecimalFormat(st);
	}
	
	public void printDataLinkedList(LinkedList<Data> list){
		Iterator<Data> it = list.iterator();
		while(it.hasNext()){
			Data data = it.next();
			System.out.print("["+ timedf.format(data.time()) +"]:"+ valuedf.format(data.value()) + " ");
		}
		System.out.println();
	}
	
}
