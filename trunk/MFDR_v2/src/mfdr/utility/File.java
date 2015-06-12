package mfdr.utility;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.file.FileAccessAgent;

public class File {
	private static Log logger = LogFactory.getLog(File.class);
	private static File self = new File();
	DecimalFormat valuedf = new DecimalFormat("0.0000");
	DecimalFormat timedf = new DecimalFormat("0.00");
	FileAccessAgent agent = new FileAccessAgent("C:\\TEST\\MDFR\\", "C:\\TEST\\MDFR\\NULL.txt");
	
	private File(){
		
	}
	
	public static File getInstance(){
		return self;
	}
	
	public LinkedList<TimeSeries> readreadTimeSeriesFromFile(String fileaddress, int num){
		LinkedList<TimeSeries> tslist = new LinkedList<TimeSeries>();
		this.agent.updatereadingpath(fileaddress);
		for(int i = 0 ; i < num ; i++){
			TimeSeries ts = new TimeSeries();
			String values = this.agent.readLineFromFile();
			if(values == null)
				break;
			String[] valuearray = values.split(",");
			// Read start from 1 as the first column is reserved for tags. 
			for(int j = 1 ; j < valuearray.length ; j++){
				ts.add(new Data(Double.valueOf(j), Double.valueOf(valuearray[j])));
			}
			tslist.add(ts);
		}
		return tslist;
	}
	
	public LinkedList<Data> readTimeSeriesFromFile(String fileaddress){
		TimeSeries ts = new TimeSeries();
		String values = this.agent.readLineFromFile(fileaddress);
		String[] valuearray = values.split(",");
		// Read start from 1 as the first column is reserved for tags. 
		for(int i = 1 ; i < valuearray.length ; i++){
			ts.add(new Data(Double.valueOf(i), Double.valueOf(valuearray[i])));
		}
		return ts;
	}
	
	public LinkedList<Data> readTimedTimeSeriesFromFile(String fileaddress){
		TimeSeries ts = new TimeSeries();
		String times = this.agent.readLineFromFile(fileaddress);
		String values = this.agent.readLineFromFile();
		String[] timearray = times.split(",");
		String[] valuearray = values.split(",");
		if(timearray.length != valuearray.length){
			logger.info("Input data length does not match");
			return null;
		}
		// Read start from 1 as the first column is reserved for tags. 
		for(int i = 1 ; i < timearray.length ; i++){
			ts.add(new Data(Double.valueOf(timearray[i]), Double.valueOf(valuearray[i])));
		}
		return ts;
	}
	
	public LinkedList<Data> readTimeSeriesFromFile(String fileaddress, String tag){
		TimeSeries ts = new TimeSeries();
		String times = this.agent.readLineFromFile(fileaddress);
		String values = this.agent.readLineFromFile();
		while(!values.split(",")[0].equals(tag)){
			values = this.agent.readLineFromFile();
		}
		String[] timearray = times.split(",");
		String[] valuearray = values.split(",");
		if(timearray.length != valuearray.length){
			logger.info("Input data length does not match");
			return null;
		}
		// Read start from 1 as the first column is reserved for tags. 
		for(int i = 1 ; i < timearray.length ; i++){
			ts.add(new Data(Double.valueOf(timearray[i]), Double.valueOf(valuearray[i])));
		}
		return ts;
	}
	
	public void saveLinkedListToFile(String outputstring, LinkedList<Data> data, String fileaddress){
		Iterator<Data> it = data.iterator();
		while (it.hasNext()) {
			Data data2 = (Data) it.next();
			outputstring = outputstring + "," + valuedf.format(data2.value());
		}
		agent.writeLineToFile(outputstring, fileaddress);
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
	
	public void saveArrayToFile(String outputstring, double[] xx, String fileaddress){
		for (int i = 1 ; i < xx.length ; i++) {
			outputstring = outputstring + "," + valuedf.format(xx[i]);
		}
		agent.writeLineToFile(outputstring, fileaddress);
	}
	
}
