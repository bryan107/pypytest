package mfdr.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class MapPool {
	
	public Map<Integer, LinkedList<Integer>> creatIntegerMap(){
		Map<Integer, LinkedList<Integer>> map = new HashMap<Integer, LinkedList<Integer>>();
		return map;
	}
	
	public void removeElement(Map<Integer, LinkedList<Integer>> map, int index){
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
}
