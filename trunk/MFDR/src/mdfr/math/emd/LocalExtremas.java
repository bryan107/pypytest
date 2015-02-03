package mdfr.math.emd;

import java.util.LinkedList;

public class LocalExtremas {

	private LinkedList<Integer> localminima;
	private LinkedList<Integer> localmaxima;
	
	public LocalExtremas(){
		localminima = new LinkedList<Integer>();
		localmaxima = new LinkedList<Integer>();
	}
	
	public LinkedList<Integer> localMinima(){
		return localminima;
	}
	public LinkedList<Integer> localMaxima(){
		return localmaxima;
	}
}
