package mfdr.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import junit.framework.TestCase;

public class Test extends TestCase {

	
	public void testg(){
		LinkedList<LinkedList<Integer>> list = creatList();
		Map<Integer, LinkedList<Integer>> map = creatMap();
		removeElement(list, 1);
		removeElement(map, 2);
	}
	
	public Map<Integer, LinkedList<Integer>> creatMap(){
		Map<Integer, LinkedList<Integer>> map = new HashMap<Integer, LinkedList<Integer>>();
		for(int i = 0 ; i < 4 ; i++){
			map.put(i, new LinkedList<Integer>());
			for(int j = 0 ; j < 4 ; j++){
				if( i == j )
					continue;
				map.get(i).add(j);
			}
		}
		return map;
	}
	
	public LinkedList<LinkedList<Integer>> creatList(){
		LinkedList<LinkedList<Integer>> list = new LinkedList<LinkedList<Integer>>();
		for(int i = 0 ; i < 4 ; i++){
			list.add(new LinkedList<Integer>());
			for(int j = 0 ; j < 4 ; j++){
				if( i == j )
					continue;
				list.get(i).add(j);
			}
		}
		return list;
	}
	
	private void removeElement(Map<Integer, LinkedList<Integer>> map, int index){
		// iterator through the index elements.
		Iterator<Integer> it = map.get(index).iterator();
		while (it.hasNext()) {
			// First element
			Integer index1= (Integer) it.next();
			// Remove this element from other lists.
			Iterator<Integer> it2 = map.get(index1).iterator();
			while (it2.hasNext()) {
				Integer index2 = (Integer) it2.next();
				if(index2 == index)
					continue;
				map.get(index2).removeAll(Collections.singleton(index1));
			}
			// Remove the element list from map
			map.remove(index1);
		}
		map.remove(index);
	}
	
	public void removeElement(LinkedList<LinkedList<Integer>> list, int index){
		// iterator through the index elements.
		Iterator<Integer> it = list.get(index).iterator();
		while (it.hasNext()) {
			// First element
			Integer index1= (Integer) it.next();
			// Remove this element from other lists.
			Iterator<Integer> it2 = list.get(index1).iterator();
			while (it2.hasNext()) {
				Integer index2 = (Integer) it2.next();
				if(index2 == index)
					continue;
				list.get(index2).removeAll(Collections.singleton(index1));
			}
			// Remove the element list from map
			list.remove(index1.intValue());
		}
		list.remove(index);
	}
}
