package experiment.core;

import java.util.LinkedList;

import experiment.utility.DataParser;
import experiment.utility.UCRData;
import mfdr.core.MFDRParameterFacade;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.reduction.*;
import mfdr.file.FileAccessAgent;
import junit.framework.TestCase;

public class ExperimentContainer2 extends TestCase {

//	private final int NoC = 4;
	private final String readaddress = "C:\\TEST\\MDFR\\Data\\dataset\\";
	private final String writeaddress = "C:\\TEST\\MDFR\\Data\\Experiment\\RepresentationError2.csv";
	private final String listaddress = "C:\\TEST\\MDFR\\Data\\dataset\\dataset_list2.txt";
	
	public void test(){
		
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MDFR\\Null.txt");
		
		LinkedList<String> filenamelist = getFileNameList(fagent,listaddress);
		double count = 0;
		for(int i = 0 ; i < filenamelist.size() ; i++){
			for(int NoC = 2 ; NoC < 9 ; NoC+=2){
			// Get file from list;
			LinkedList<TimeSeries> tsset = getTimeSeriesListALL(fagent,readaddress,filenamelist.get(i));
			RepresentationErrorExpCore core = new RepresentationErrorExpCore();
			RepresentationErrorResult r_pla = core.runOptimalSolution(tsset, new PLA(NoC));
			RepresentationErrorResult r_dft = core.runOptimalSolution(tsset, new DFTWave(NoC));
			RepresentationErrorResult r_paa = core.runOptimalSolution(tsset, new PAA(NoC));
			RepresentationErrorResult r_mfdr = core.runOptimalSolutionMFDR(tsset, new MFDRParameterFacade(3,0.5,6.5), NoC ,false);
			RepresentationErrorResult r_mfdr_n = core.runOptimalSolutionMFDR(tsset, new MFDRParameterFacade(3,0.5,6.5), NoC ,true);
			
			String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
			outputstring += "PLA,M," + r_pla.mean()+",V," + r_pla.variance()+",T," + r_pla.time()+",";
			outputstring += "DFT,M," + r_dft.mean()+",V," + r_dft.variance()+",T," + r_dft.time()+",";
			outputstring += "PAA,M," + r_paa.mean()+",V," + r_paa.variance()+",T," + r_paa.time()+",";
			outputstring += "MFDR,M," + r_mfdr.mean()+",V," + r_mfdr.variance()+",T," + r_mfdr.time()+",";
			outputstring += "MFDR-N,M," + r_mfdr_n.mean()+",V," + r_mfdr_n.variance()+",T," + r_mfdr_n.time()+",";
			fagent.writeLineToFile(outputstring);
			count ++;
			System.out.println("PROGRESS:"+ (count *100 / 4 / filenamelist.size())+"%");
//			System.out.println("PLA- M:" + r_pla.mean()+" V:" + r_pla.variance()+" T:" + r_pla.time());
//			System.out.println("DFT- M:" + r_dft.mean()+" V:" + r_dft.variance()+" T:" + r_dft.time());
//			System.out.println("PAA- M:" + r_paa.mean()+" V:" + r_paa.variance()+" T:" + r_paa.time());
//			System.out.println("MFDR- M:" + r_mfdr.mean()+" V:" + r_mfdr.variance()+" T:" + r_mfdr.time());
//			System.out.println("MFDR-N M:" + r_mfdr_n.mean()+" V:" + r_mfdr_n.variance()+" T:" + r_mfdr_n.time());
			}
		}
	}
	
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
