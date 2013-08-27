package mga.core;

import java.util.LinkedList;

import junit.framework.TestCase;

public class TestMGA extends TestCase {

	public void testLinkedListToDoubleArray(){
		LinkedList<Double> list = new LinkedList<Double>();
		for(int i = 0 ; i < 10 ; i++){
			list.add((double)i);
		}
		
		
		double[] array = new double[list.size()];
		for(int index = 0 ; index < list.size() ; index++){
			array[index] = list.get(index);
		}
		
		System.out.println("Array Content");
		
		for(int i = 1 ; i < array.length ; i++){
			System.out.print(array[i] + " ");
		}
		
		
		
	}
	
}
