package experiment.core;

import java.util.LinkedList;

import experiment.utility.DataParser;
import experiment.utility.UCRData;
import flanagan.analysis.Stat;
import mfdr.core.MFDRParameterFacade;
import mfdr.core.MFDRParameters;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.dimensionality.reduction.MFDRLCM;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.file.FileAccessAgent;
import mfdr.utility.DataListOperator;

public class NoiseMFDRCore {

	public NoiseMFDRCore() {

	}
	/**
	 * DO NOT USE THIS FUNCTION!!! IT DOES NOT WORK 
	 * @param readaddress
	 * @param writeaddress
	 * @param listaddress
	 * @param NoC_Start
	 * @param NoC_Interval
	 * @param NoC_End
	 * @param zstart
	 * @param zend
	 */
	public void runError(String readaddress,String writeaddress ,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End, double zstart, double zend) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MFDR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent,listaddress);
		double count = 0;
		for(int i = 0 ; i < filenamelist.size() ; i++){
			for(int NoC = NoC_Start ; NoC <= NoC_End ; NoC+=NoC_Interval){
				LinkedList<TimeSeries> tsset = getTimeSeriesListALL(fagent,readaddress,filenamelist.get(i));
				// Test Distances 1000 
				tsset = getTimeSeriesListALL(fagent,readaddress,filenamelist.get(i));
			//  MFDR
				RepresentationErrorResult r_mfdr = runOptimalSolutionMFDR(tsset, new MFDRParameterFacade(3,0.5,6.5), NoC ,false);
				count ++;
				System.out.println("PROGRESS:"+ (count*100 / 32 / filenamelist.size()) + "%");	
				String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
				outputstring += "M," + r_mfdr.mean()+",V," + r_mfdr.variance()+",T," + r_mfdr.time()+",,";
				// MFDR-N
				for(double z = zstart ; z <= zend; z++){
					RepresentationErrorResult r_mfdr_n = runOptimalSolutionMFDR(tsset, new MFDRParameterFacade(z,6.5,0.5), NoC ,true);
					count ++;
					System.out.println("PROGRESS:"+ (count*100 / 32 / filenamelist.size()) + "%");	
					outputstring += "M," + r_mfdr_n.mean()+",V," + r_mfdr_n.variance()+",T," + r_mfdr_n.time()+",,";
				}
			
				//------------------------- Output -------------------------
				fagent.writeLineToFile(outputstring);
				System.out.println("PROGRESS: " + filenamelist.get(i) + "[" + NoC + "] Stored...");
			}
		}
		
	}


	public void runDistance(String readaddress, String writeaddress,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End,
			double zstart, double zend) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress,
				"C:\\TEST\\MFDR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent, listaddress);
		double count = 0;
		for (int i = 0; i < filenamelist.size(); i++) {
			for (int NoC = NoC_Start; NoC <= NoC_End; NoC += NoC_Interval) {
				LinkedList<TimeSeries> tsset = getTimeSeriesListTrain(fagent,
						readaddress, filenamelist.get(i));
				// Test Distances 1000
				tsset = getTimeSeriesListALL(fagent, readaddress,
						filenamelist.get(i));
				int[][] pairlist = new int[1000][2];
				for (int j = 0; j < 1000; j++) {
					int pair_1 = (int) (tsset.size() * Math.random());
					int pair_2 = (int) (tsset.size() * Math.random());
					while (pair_1 == pair_2) {
						pair_2 = (int) (tsset.size() * Math.random());
					}
					pairlist[j][0] = pair_1;
					pairlist[j][1] = pair_2;
				}
				//  MFDR
				RepresentationErrorResult r_mfdr = runMFDR1000(tsset, pairlist,	new MFDRParameterFacade(3, 0.66, 6.5), NoC, false);
				count++;
				System.out.println("PROGRESS(MFDR):"+ (count * 100 / 25 / filenamelist.size()) + "%");
				String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
				outputstring += "M," + r_mfdr.mean()+",V," + r_mfdr.variance()+",T," + r_mfdr.time()+",,";
				//  MFDR-N
				for(double z = zstart ; z <= zend; z++){
					RepresentationErrorResult r_mfdr_n = runMFDR1000(tsset,	pairlist, new MFDRParameterFacade(z,6.5,0.65), NoC, true);
					count++;
					System.out.println("PROGRESS(MFDR-N):" + (count * 100 / 25 / filenamelist.size()) + "%");
					outputstring += "M," + r_mfdr_n.mean()+",V," + r_mfdr_n.variance()+",T," + r_mfdr_n.time()+",,";
				}
				// ------------------------- Output -------------------------
				fagent.writeLineToFile(outputstring);
				System.out.println("PROGRESS: " + filenamelist.get(i) + "["	+ NoC + "] Stored...");
			}
		}

	}
	
	public RepresentationErrorResult runOptimalSolutionMFDR(
			LinkedList<TimeSeries> tsset, MFDRParameterFacade facade, int NoC,
			boolean usenoise) {
		MFDR mfdr = new MFDR(1, 1);
		long startTime = System.currentTimeMillis();
		double[] errors = new double[tsset.size()];
		// Operations
		for (int i = 0; i < tsset.size(); i++) {
			MFDRParameters p = facade.learnMFDRParameters(tsset.get(i), NoC,usenoise);
			mfdr.updateParameters(p.trendNoC(), p.seasonalNoC());
			TimeSeries reduced = mfdr.getFullResolutionDR(tsset.get(i));
			TimeSeries error = DataListOperator.getInstance()
					.linkedtListSubtraction(tsset.get(i), reduced);
			errors[i] = error.energyDensity();
		}
		long endTime = System.currentTimeMillis();
		return new RepresentationErrorResult(Stat.mean(errors),
				Stat.variance(errors), endTime - startTime);
	}

	public RepresentationErrorResult runMFDR1000(LinkedList<TimeSeries> tsset,
			int[][] pairlist, MFDRParameterFacade facade, int NoC,
			boolean use_noise) {
		MFDRLCM mfdr = new MFDRLCM(0, 0);
		double[] result = new double[1000];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<MFDRWaveData> drlist = new LinkedList<MFDRWaveData>();
		long time = 0;
		long startTime = 0, endTime = 0;
		// Accumulate DR data
		for (int i = 0; i < tsset.size(); i++) {
			startTime = System.nanoTime();
			MFDRParameters p = facade.learnMFDRParameters(tsset.get(i), NoC,
					use_noise);
			endTime = System.nanoTime();
			mfdr.updateParameters(p.trendNoC(), p.seasonalNoC());
			if (use_noise) {
				drlist.add(mfdr.getDR(tsset.get(i), p.lowestPeriod()));
			} else {
				drlist.add(mfdr.getDR(tsset.get(i)));
			}
			time += (endTime - startTime)/tsset.size();
		}
		System.out.println("MFDR DR Done");
		// Distance Calculation
		
		
		for (int round = 0; round < 1000; round++) {
			// Original Distance
			double dist_ori = d.calDistance(tsset.get(pairlist[round][0]),	tsset.get(pairlist[round][1]),tsset.get(pairlist[round][0]));
			int size = tsset.get(pairlist[round][0]).size();
//			startTime = System.nanoTime();
			double dist_dr = mfdr.getDistance(drlist.get(pairlist[round][0]),drlist.get(pairlist[round][1]), size, d);
//			endTime = System.nanoTime();
//			time += endTime - startTime;
			if (dist_ori != 0) {
				result[count] = Math.abs(dist_ori - dist_dr) / dist_ori;
				count++;
				sum += Math.abs(dist_ori - dist_dr) / dist_ori;
			}
		}
		System.out.println("MFDR Distance Done");
		// Result Analysis
		return new RepresentationErrorResult(sum / count,
				Stat.variance(result), time);
	}

	// Get list
	public LinkedList<String> getFileNameList(FileAccessAgent fagent,
			String filelistaddress) {
		LinkedList<String> filenamelist = new LinkedList<String>();
		fagent.updatereadingpath(filelistaddress);
		while (true) {
			String filename = fagent.readLineFromFile();
			if (filename == null) {
				break;
			}
			filenamelist.add(filename);
		}
		return filenamelist;
	}

	public LinkedList<TimeSeries> getTimeSeriesListTest(FileAccessAgent fagent,
			String address, String filename) {
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<TimeSeries> ts = new LinkedList<TimeSeries>();
		fagent.updatereadingpath(address + filename + "\\" + filename + "_TEST");
		// Iterate through test data
		while (true) {
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if (temp == null)
				break;
			ts.add(temp);
		}
		return ts;
	}

	public LinkedList<TimeSeries> getTimeSeriesListTrain(
			FileAccessAgent fagent, String address, String filename) {
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<TimeSeries> ts = new LinkedList<TimeSeries>();
		fagent.updatereadingpath(address + filename + "\\" + filename
				+ "_TRAIN");
		// Iterate through train data
		while (true) {
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if (temp == null)
				break;
			ts.add(temp);
		}
		return ts;
	}

	public LinkedList<TimeSeries> getTimeSeriesListALL(FileAccessAgent fagent,
			String address, String filename) {
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<TimeSeries> ts = new LinkedList<TimeSeries>();
		// Iterate through train data
		fagent.updatereadingpath(address + filename + "\\" + filename
				+ "_TRAIN");
		while (true) {
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if (temp == null)
				break;
			ts.add(temp);
		}
		// Iterate through test data
		fagent.updatereadingpath(address + filename + "\\" + filename + "_TEST");
		while (true) {
			TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
			if (temp == null)
				break;
			ts.add(temp);
		}
		return ts;
	}

}
