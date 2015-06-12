package experiment.utility;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.file.FileAccessAgent;

public class OneLineOneData implements FileStructure {

	@Override
	public UCRDataDetails perTimeSeriesExtraction(FileAccessAgent fagent) {
		double time = 0; 
		TimeSeries ts = new TimeSeries();
		// store  
		String line = fagent.readLineFromFile();
		while(line != null){
			double value = Double.valueOf(line);
			ts.add(new Data(time, value));
			time++;
			line = fagent.readLineFromFile();
		}
		return new UCRDataDetails(0,ts);
	}

//	@Override
//	public UCRDataDetails perTimeSeriesExtraction(FileAccessAgent fagent) {
//		double time = 0; 
//		TimeSeries ts = new TimeSeries();
//		// store  
//		String line = fagent.readLineFromFile();
//		while(line != null){
//			double value = Double.valueOf(line);
//			ts.add(new Data(time, value));
//			time++;
//			line = fagent.readLineFromFile();
//		}
//		return ts;
//	}

}
