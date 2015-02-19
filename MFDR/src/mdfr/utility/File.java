package mdfr.utility;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import mdfr.math.emd.datastructure.Data;
import mdr.file.FileAccessAgent;

public class File {
	
	private static File self = new File();
	DecimalFormat valuedf = new DecimalFormat("0.0000");
	DecimalFormat timedf = new DecimalFormat("0.00");
	FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\MDFR\\", "C:\\TEST\\MDFR\\NULL.txt");
	
	private File(){
		
	}
	
	public static File getInstance(){
		return self;
	}
	
	public void saveLinkedListToFile(LinkedList<Data> data, String fileaddress){
		String value = new String();
		Iterator<Data> it = data.iterator();
		while (it.hasNext()) {
			Data data2 = (Data) it.next();
			value = value + "," + valuedf.format(data2.value());
		}
		agent.writeLineToFile(value, fileaddress);
	}
	
	public void saveTimeToFile(LinkedList<Data> data, String fileaddress){
		String time = new String();
		Iterator<Data> it = data.iterator();
		while (it.hasNext()) {
			Data data2 = (Data) it.next();
			time = time + "," + timedf.format(data2.time());
		}
		agent.writeLineToFile(time, fileaddress);
	}
	
	public void saveArrayToFile(double[] xx, String fileaddress){
		String value = new String();
		for (int i = 1 ; i < xx.length ; i++) {
			value = value + "," + valuedf.format(xx[i]);
		}
		agent.writeLineToFile(value, fileaddress);
	}
	
}
