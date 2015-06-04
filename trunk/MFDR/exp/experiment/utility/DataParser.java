package experiment.utility;

import mfdr.datastructure.TimeSeries;
import mfdr.file.FileAccessAgent;

public class DataParser {
	
	private FileStructure fs;
	private FileAccessAgent fagent;
	
	public DataParser(FileStructure fs, FileAccessAgent fagent){
		updateFileStructure(fs);
		updateFileAccessAgent(fagent);
	}
	
	public void updateFileStructure(FileStructure fs){
		this.fs = fs;
	}
	
	public void updateFileAccessAgent(FileAccessAgent fagent){
		this.fagent = fagent;
	}
	
	/**
	 * Store time series data from file to a TimeSeries object	
	 * @return TimeSeries
	 */
	public TimeSeries getTimeSeries(){
		double time = 0;
		TimeSeries ts = new TimeSeries();
		// store  
		String line = fagent.readLineFromFile();
		while(line != null){
			fs.perLineExtraction(ts, line, time);
			time++;
			line = fagent.readLineFromFile();
		}
		return ts;
	}
	
	/**
	 * Store time series data from between specific segment	
	 * @return TimeSeries
	 */
	public TimeSeries getTimeSeries(double start, double end){
		double time = 0;
		TimeSeries ts = new TimeSeries();
		// store  
		String line = fagent.readLineFromFile();
		while(line != null){
			if(time < start){
				time++;
				continue;
			} else if(time > end){
				break;
			}
			fs.perLineExtraction(ts, line, time);
			time++;
			line = fagent.readLineFromFile();
		}
		return ts;
	}
	
	/**
	 * Store time series data from between specific segment with updated reading path
	 * @param readingpath
	 * @return TimeSeries
	 */
	public TimeSeries getTimeSeries(double start, double end, String readingpath){
		TimeSeries ts = new TimeSeries();
		fagent.updatereadingpath(readingpath);
		getTimeSeries(start, end);
		return ts;
	}
	
	/**
	 * Store time series data from file to a TimeSeries object with updated reading path
	 * @param readingpath
	 * @return TimeSeries
	 */
	public TimeSeries getTimeSeries(String readingpath){
		TimeSeries ts = new TimeSeries();
		fagent.updatereadingpath(readingpath);
		getTimeSeries();
		return ts;
	}
	
}
