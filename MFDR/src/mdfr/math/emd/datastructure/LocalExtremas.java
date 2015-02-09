package mdfr.math.emd.datastructure;

import java.util.LinkedList;

public class LocalExtremas {

	private LinkedList<Data> localminima;
	private LinkedList<Data> localmaxima;
	
	public LocalExtremas(){
		localminima = new LinkedList<Data>();
		localmaxima = new LinkedList<Data>();
	}
	
	public LinkedList<Data> localMinima(){
		return localminima;
	}
	public LinkedList<Data> localMaxima(){
		return localmaxima;
	}
	
	public boolean isMonotonic(){
		if ((localminima.size() < 2) || (localmaxima.size() < 2))
			return true;
		return false;
	}
}
