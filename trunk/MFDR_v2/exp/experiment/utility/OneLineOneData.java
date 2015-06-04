package experiment.utility;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;

public class OneLineOneData implements FileStructure {

	@Override
	public void perLineExtraction(TimeSeries ts, String line, double time) {
		double value = Double.valueOf(line);
		ts.add(new Data(time, value));
	}

}
