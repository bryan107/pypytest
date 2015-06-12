package experiment.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.PLA;
import mfdr.file.FileAccessAgent;

public class UCRData implements FileStructure {
	private static Log logger = LogFactory.getLog(UCRData.class);
	
	@Override
	public UCRDataDetails perTimeSeriesExtraction(FileAccessAgent fagent) {
		int clusternumber = -1;
		TimeSeries ts = new TimeSeries();
		String line = fagent.readLineFromFile();
		if(line == null){
			logger.info("Extraction from " + fagent.readingPath() + " is completed");
			return new UCRDataDetails(-1, null);
		}
		int count = -1;
		String[] split = line.split(" ");
		for(int i = 0 ; i < split.length ; i++){
			// Remove unused spaces
			split[i].replaceAll("\\s", "");
			if(!split[i].equals("")){
				count++;
				if(count == 0){
					clusternumber = (int)extractValue(split, i);
				}
				try {
					double test = extractValue(split, i);
					ts.add(new Data(count,test));
				} catch (Exception e) {
					logger.info("split:" + split[i] + e);
				}
			}
		}
		return new UCRDataDetails(clusternumber, ts);
	}

	public double extractValue(String[] split, int i) {
		double test;
		if(split[i].contains("e")){
			String[] split2 = split[i].split("e");
			test = Double.valueOf(split2[0])*Math.pow(10, Double.valueOf(split2[1]));
		} else{
			test = Double.valueOf(split[i]);
		}
		return test;
	}

}
