package experiment.core;

import java.sql.Timestamp;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import experiment.utility.DataParser;
import experiment.utility.UCRData;
import flanagan.analysis.Stat;
import mfdr.core.MFDRParameterFacade;
import mfdr.core.MFDRParameters;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DFTWave;
import mfdr.dimensionality.reduction.DimensionalityReduction;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.dimensionality.reduction.PAA;
import mfdr.dimensionality.reduction.PLA;
import mfdr.file.FileAccessAgent;
import mfdr.utility.DataListOperator;

public class RepresentationErrorExpCore {
	private static Log logger = LogFactory.getLog(RepresentationErrorExpCore.class);
	public RepresentationErrorExpCore(){
	
	}
	
	public void runReal(String readaddress,String writeaddress ,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MDFR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent,listaddress);
		MFDRParameterFacade facade = new MFDRParameterFacade(3,0.5,6.5);
		double count = 0;
		for(int i = 0 ; i < filenamelist.size() ; i++){
			for(int NoC = NoC_Start ; NoC <= NoC_End ; NoC+=NoC_Interval){
			// Train Parameters
			LinkedList<TimeSeries> tsset = getTimeSeriesListTrain(fagent,readaddress,filenamelist.get(i));
			MFDRParameters parameters = facade.learnMFDRParameters(tsset, NoC, false);
			MFDRParameters parameters_n = facade.learnMFDRParameters(tsset, NoC, true);
			// Test 
			tsset = getTimeSeriesListTest(fagent,readaddress,filenamelist.get(i));
			RepresentationErrorResult r_mfdr = runRealSolutionMFDR(tsset, parameters, false);
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 10 / filenamelist.size()) + "%");
			RepresentationErrorResult r_mfdr_n = runRealSolutionMFDR(tsset, parameters_n, true);
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 10 / filenamelist.size()) + "%");
			// Output
			String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
			outputstring += "MFDR,M," + r_mfdr.mean()+",V," + r_mfdr.variance()+",T," + r_mfdr.time()+",";
			outputstring += "MFDR-N,M," + r_mfdr_n.mean()+",V," + r_mfdr_n.variance()+",T," + r_mfdr_n.time()+",";
			fagent.writeLineToFile(outputstring);
			System.out.println("PROGRESS: " + filenamelist.get(i) + "[" + NoC + "] Stored...");
//			System.out.println("PLA- M:" + r_pla.mean()+" V:" + r_pla.variance()+" T:" + r_pla.time());
//			System.out.println("DFT- M:" + r_dft.mean()+" V:" + r_dft.variance()+" T:" + r_dft.time());
//			System.out.println("PAA- M:" + r_paa.mean()+" V:" + r_paa.variance()+" T:" + r_paa.time());
//			System.out.println("MFDR- M:" + r_mfdr.mean()+" V:" + r_mfdr.variance()+" T:" + r_mfdr.time());
//			System.out.println("MFDR-N M:" + r_mfdr_n.mean()+" V:" + r_mfdr_n.variance()+" T:" + r_mfdr_n.time());
			}
		}
	}
	
	public void runDFT(String readaddress,String writeaddress ,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MDFR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent,listaddress);
		double count = 0;
		for(int i = 0 ; i < filenamelist.size() ; i++){
			for(int NoC = NoC_Start ; NoC <= NoC_End ; NoC+=NoC_Interval){
			// Get file from list;
			LinkedList<TimeSeries> tsset = getTimeSeriesListALL(fagent,readaddress,filenamelist.get(i));
			RepresentationErrorResult r_dft = runOptimalSolution(tsset, new DFT(NoC));
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 5 / filenamelist.size()) + "%");
			String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
			outputstring += "DFT,M," + r_dft.mean()+",V," + r_dft.variance()+",T," + r_dft.time()+",";
			fagent.writeLineToFile(outputstring);
			System.out.println("PROGRESS: " + filenamelist.get(i) + "[" + NoC + "] Stored...");
//			System.out.println("PLA- M:" + r_pla.mean()+" V:" + r_pla.variance()+" T:" + r_pla.time());
//			System.out.println("DFT- M:" + r_dft.mean()+" V:" + r_dft.variance()+" T:" + r_dft.time());
//			System.out.println("PAA- M:" + r_paa.mean()+" V:" + r_paa.variance()+" T:" + r_paa.time());
//			System.out.println("MFDR- M:" + r_mfdr.mean()+" V:" + r_mfdr.variance()+" T:" + r_mfdr.time());
//			System.out.println("MFDR-N M:" + r_mfdr_n.mean()+" V:" + r_mfdr_n.variance()+" T:" + r_mfdr_n.time());
			}
		}
	}
	
	public void runOptimal(String readaddress,String writeaddress ,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MDFR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent,listaddress);
		double count = 0;
		for(int i = 0 ; i < filenamelist.size() ; i++){
			for(int NoC = NoC_Start ; NoC <= NoC_End ; NoC+=NoC_Interval){
			// Get file from list;
			LinkedList<TimeSeries> tsset = getTimeSeriesListALL(fagent,readaddress,filenamelist.get(i));
			RepresentationErrorResult r_pla = runOptimalSolution(tsset, new PLA(NoC));
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_dft = runOptimalSolution(tsset, new DFTWave(NoC));
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_paa = runOptimalSolution(tsset, new PAA(NoC));
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_mfdr = runOptimalSolutionMFDR(tsset, new MFDRParameterFacade(3,0.5,6.5), NoC ,false);
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_mfdr_n = runOptimalSolutionMFDR(tsset, new MFDRParameterFacade(3,0.5,6.5), NoC ,true);
			count ++;
			System.out.println("PROGRESS:"+ (count*100 / 25 / filenamelist.size()) + "%");
			String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
			outputstring += "PLA,M," + r_pla.mean()+",V," + r_pla.variance()+",T," + r_pla.time()+",";
			outputstring += "DFT,M," + r_dft.mean()+",V," + r_dft.variance()+",T," + r_dft.time()+",";
			outputstring += "PAA,M," + r_paa.mean()+",V," + r_paa.variance()+",T," + r_paa.time()+",";
			outputstring += "MFDR,M," + r_mfdr.mean()+",V," + r_mfdr.variance()+",T," + r_mfdr.time()+",";
			outputstring += "MFDR-N,M," + r_mfdr_n.mean()+",V," + r_mfdr_n.variance()+",T," + r_mfdr_n.time()+",";
			fagent.writeLineToFile(outputstring);
			System.out.println("PROGRESS: " + filenamelist.get(i) + "[" + NoC + "] Stored...");
//			System.out.println("PLA- M:" + r_pla.mean()+" V:" + r_pla.variance()+" T:" + r_pla.time());
//			System.out.println("DFT- M:" + r_dft.mean()+" V:" + r_dft.variance()+" T:" + r_dft.time());
//			System.out.println("PAA- M:" + r_paa.mean()+" V:" + r_paa.variance()+" T:" + r_paa.time());
//			System.out.println("MFDR- M:" + r_mfdr.mean()+" V:" + r_mfdr.variance()+" T:" + r_mfdr.time());
//			System.out.println("MFDR-N M:" + r_mfdr_n.mean()+" V:" + r_mfdr_n.variance()+" T:" + r_mfdr_n.time());
			}
		}
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
		return new RepresentationErrorResult(Stat.mean(errors),Stat.variance(errors), endTime-startTime);
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
		}
		long endTime = System.currentTimeMillis();
		return new RepresentationErrorResult(Stat.mean(errors),Stat.variance(errors), endTime-startTime);
	}
	
	public RepresentationErrorResult runRealSolutionMFDR(LinkedList<TimeSeries> tsset, MFDRParameters p, boolean use_noise){
		MFDRWave mfdr = new MFDRWave(1, 1);
		long startTime = System.currentTimeMillis();
		double[] errors = new double[tsset.size()];
		//Operations
		for(int i = 0 ; i < tsset.size() ; i++){
			mfdr.updateParameters(p.trendNoC(), p.seasonalNoC());
			TimeSeries reduced ;
			if(use_noise){
				reduced = mfdr.getFullResolutionDR(tsset.get(i),p.lowestPeriod());
			} else{
				reduced = mfdr.getFullResolutionDR(tsset.get(i));
			}
			TimeSeries error = DataListOperator.getInstance().linkedtListSubtraction(tsset.get(i), reduced);
			errors[i] = error.energyDensity();
		}
		long endTime = System.currentTimeMillis();
		return new RepresentationErrorResult(Stat.mean(errors),Stat.variance(errors), endTime-startTime);
	}
	
//	public double getRepresentationError(TimeSeries a, TimeSeries b){
//		if(a.size()!=b.size()){
//			logger.info("Input Length is not euqal");
//		}
//		double value = 0;
//		for(int i = 0 ; i < a.size() ; i++){
//			value += Math.pow(a.get(i).value()+b.get(i).value(), 2);
//		}
//		return value/a.size();
//	}
	
	
	
	// Get list
	public LinkedList<String> getFileNameList(FileAccessAgent fagent,String filelistaddress){
		LinkedList<String> filenamelist = new LinkedList<String>();
		fagent.updatereadingpath(filelistaddress);
		while(true){
			String filename = fagent.readLineFromFile();
			if(filename == null){
				break;
			}
			filenamelist.add(filename);
		}
		return filenamelist;
	}
	
	public LinkedList<TimeSeries> getTimeSeriesListTest(FileAccessAgent fagent ,String address, String filename){
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<TimeSeries> ts= new LinkedList<TimeSeries>();
		fagent.updatereadingpath(address + filename +"\\"+ filename +"_TEST");
		// Iterate through test data
		while(true){
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if(temp == null)
				break;
			ts.add(temp);
		}
		return ts;
	}
	
	public LinkedList<TimeSeries> getTimeSeriesListTrain(FileAccessAgent fagent ,String address,String filename){
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<TimeSeries> ts= new LinkedList<TimeSeries>();
		fagent.updatereadingpath(address+ filename +"\\"+ filename +"_TRAIN");
		// Iterate through train data
		while(true){
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if(temp == null)
				break;
			ts.add(temp);
		}
		return ts;
	}
	
	public LinkedList<TimeSeries> getTimeSeriesListALL(FileAccessAgent fagent ,String address,String filename){
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<TimeSeries> ts= new LinkedList<TimeSeries>();
		// Iterate through train data
		fagent.updatereadingpath(address+ filename +"\\"+ filename +"_TRAIN");
		while(true){
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if(temp == null)
				break;
			ts.add(temp);
		}
		// Iterate through test data
		fagent.updatereadingpath(address+ filename +"\\"+ filename +"_TEST");
		while(true){
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if(temp == null)
				break;
			ts.add(temp);
		}
		return ts;
	}
	



}
