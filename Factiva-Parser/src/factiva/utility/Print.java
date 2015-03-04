package factiva.utility;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Print {
	private static Log logger = LogFactory.getLog(Print.class);
	DecimalFormat valuedf = new DecimalFormat("0.0000");
	DecimalFormat timedf = new DecimalFormat("0.00");

	private static Print self = new Print();

	private Print() {

	}

	public static Print getInstance() {
		return self;
	}

	public void printStringList(LinkedList<String> list, long size) {
		try {
			Iterator<String> it = list.iterator();
			int count = 0;
			while (it.hasNext()) {
				if (count > size)
					break;
				count++;
				String data = it.next();
				System.out.print(data + " ");
			}
			System.out.println();
		} catch (Exception e) {
			logger.info("The Listlist is empty, not printable" + e);
		}

	}
	
	
	public void printStringMap(Map<String, String> map){
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = map.get(key);
			System.out.print("[" + key + "]:" + value + " ");
		}
		System.out.println();
	}
}
