package experiment.utility;

import mfdr.datastructure.TimeSeries;

public interface FileStructure {

	public void perLineExtraction(TimeSeries ts, String line, double time);
	
}
