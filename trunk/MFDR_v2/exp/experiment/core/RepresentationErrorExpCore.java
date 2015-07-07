package experiment.core;

import java.sql.Timestamp;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;
import mfdr.core.MFDRParameterFacade;
import mfdr.core.MFDRParameters;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.DimensionalityReduction;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.dimensionality.reduction.PLA;
import mfdr.utility.DataListOperator;

public class RepresentationErrorExpCore {
	private static Log logger = LogFactory.getLog(RepresentationErrorExpCore.class);
	public RepresentationErrorExpCore(){
	
	}
	
	public RepresentationErrorResult runOptimalSolution(LinkedList<TimeSeries> tsset, DimensionalityReduction dr){
		double[] errors = new double[tsset.size()];
		long startTime = System.currentTimeMillis();
		// Operations
		for(int i = 0 ; i < tsset.size() ; i++){
			TimeSeries reduced = dr.getFullResolutionDR(tsset.get(i));
			TimeSeries error = DataListOperator.getInstance().linkedtListSubtraction(tsset.get(i), reduced);
			errors[i] = error.energyDensity();
		}
		long endTime = System.currentTimeMillis();
		return new RepresentationErrorResult(Stat.mean(errors),Stat.variance(errors), startTime-endTime);
	}
	
	public RepresentationErrorResult runOptimalSolutionMFDR(LinkedList<TimeSeries> tsset, MFDRParameterFacade facade, int NoC, boolean usenoise){
		MFDRWave mfdr = new MFDRWave(1, 1);
		long startTime = System.currentTimeMillis();
		double[] errors = new double[tsset.size()];
		//Operations
		for(int i = 0 ; i < tsset.size() ; i++){
			MFDRParameters p = facade.learnMFDRParameters(tsset.get(i), NoC, usenoise);
			mfdr.updateParameters(p.trendNoC(), p.seasonalNoC());
			TimeSeries reduced = mfdr.getFullResolutionDR(tsset.get(i));
			TimeSeries error = DataListOperator.getInstance().linkedtListSubtraction(tsset.get(i), reduced);
			errors[i] = error.energyDensity();
//			errors[i] = getRepresentationError(reduced, tsset.get(i));
		}
		long endTime = System.currentTimeMillis();
		return new RepresentationErrorResult(Stat.mean(errors),Stat.variance(errors), startTime-endTime);
	}
	
	public double getRepresentationError(TimeSeries a, TimeSeries b){
		if(a.size()!=b.size()){
			logger.info("Input Length is not euqal");
		}
		double value = 0;
		for(int i = 0 ; i < a.size() ; i++){
			value += Math.pow(a.get(i).value()+b.get(i).value(), 2);
		}
		return value/a.size();
	}


}
