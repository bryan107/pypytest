package experiment.utility;

import mfdr.datastructure.TimeSeries;
import mfdr.file.FileAccessAgent;

public interface FileStructure {

	/**
	 * Return a UCRDataDetails Object
	 * When null, clusterNumber() = -1 and timeSeries() = null
	 * @param fagent
	 * @return
	 */
	public UCRDataDetails perTimeSeriesExtraction(FileAccessAgent fagent);
	
}
