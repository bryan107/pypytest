package mfdr.dimensionality.reduction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.TimeSeries;
import mfdr.utility.DataListOperator;

public class NoCWindowSizeConverter {

	private static Log logger = LogFactory.getLog(NoCWindowSizeConverter.class);
	private static NoCWindowSizeConverter self = new NoCWindowSizeConverter();

	private NoCWindowSizeConverter() {

	}

	public static NoCWindowSizeConverter getInstance() {
		return self;
	}

	public boolean NoCToWindowSizeIsValid(int NoC, int datasize) {
		if ((datasize % NoC) == 0) {
			return true;
		} else {
			logger.info("NoC does not match data size");
			return false;
		}
	}

	public double NoCToWindowSize(int NoC, int datasize, double timeinterval) {
		if(NoCToWindowSizeIsValid(NoC, datasize)){
			return timeinterval*datasize/NoC;
		}
		return 0;
	}
	
	public boolean WindowSizeToNoCIsValid(int windowsize, int datasize) {
		if ((datasize % windowsize) == 0) {
			return true;
		} else {
			logger.info("NoC does not match data size");
			return false;
		}
	}
	
//	public double WindowSizeToNoC(int windowsize, int datasize, double timeinterval){
//		if(WindowSizeToNoCIsValid(windowsize, datasize)){
//			return (double) datainterval;
//		}
//		return 0;
//	}
//	

}
