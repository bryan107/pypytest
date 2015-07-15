package experiment.core;

import java.util.LinkedList;

import mfdr.core.MFDRParameterFacade;
import mfdr.core.MFDRWaveParameterFacade;
import mfdr.core.MFDRParameters;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.datastructure.DFTDataOld;
import mfdr.dimensionality.datastructure.DFTWaveData;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.datastructure.PAAData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DFTWave;
import mfdr.dimensionality.reduction.DimensionalityReduction;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.dimensionality.reduction.MFDRLCM;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.dimensionality.reduction.PAA;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.file.FileAccessAgent;
import mfdr.utility.DataListOperator;
import experiment.utility.DataParser;
import experiment.utility.UCRData;
import flanagan.analysis.Stat;

public class ClosenessOfDistanceCoreWithNewMFDR {

	
	public void runRandom1000(String readaddress,String writeaddress ,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MFDR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent,listaddress);
		MFDRParameterFacade facade = new MFDRParameterFacade(3,0.5,6.5);
		double count = 0;
		for(int i = 0 ; i < filenamelist.size() ; i++){
			for(int NoC = NoC_Start ; NoC <= NoC_End ; NoC+=NoC_Interval){
			// Train Parameters
			
			
			LinkedList<TimeSeries> tsset = getTimeSeriesListTest(fagent,readaddress,filenamelist.get(i));
			int[][] pairlist = new int[1000][2]; 
            for(int j = 0 ; j < 1000 ; j++){
    			int pair_1 = (int) (tsset.size()*Math.random());
    			int pair_2 = (int) (tsset.size()*Math.random());
    			while(pair_1==pair_2){
    				pair_2 = (int) (tsset.size()*Math.random());
    			}
    			pairlist[j][0]=pair_1;
    			pairlist[j][1]=pair_2;
            }
			
			// Test 
			tsset = getTimeSeriesListTest(fagent,readaddress,filenamelist.get(i));
			RepresentationErrorResult r_paa = runPAA1000(tsset, pairlist , new PAA(NoC));
			count ++;
			System.out.println("PROGRESS(PAA):"+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_pla = runPLA1000(tsset, pairlist ,new PLA(NoC));
			count ++;
			System.out.println("PROGRESS(PLA): "+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_dftwave = runDFTWave1000(tsset, pairlist ,new DFTWave(NoC));
			count ++;
			System.out.println("PROGRESS(DFTWave):"+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_dft = runDFT1000(tsset, pairlist ,new DFT(NoC));
			count ++;
			System.out.println("PROGRESS(DFT):"+ (count*100 / 25 / filenamelist.size()) + "%");
			RepresentationErrorResult r_mfdr_t = runMFDR1000(tsset,pairlist ,facade, NoC, false);
			count ++;
			System.out.println("PROGRESS(MFDR-T):"+ (count*100 / 25/ filenamelist.size()) + "%");
			
			// Output
			String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
			//  PLA
			outputstring += "M," + r_pla.mean()+",V," + r_pla.variance()+",T," + r_pla.time()+",,";
		    //  DFTWave
			outputstring += "M," + r_dftwave.mean()+",V," + r_dftwave.variance()+",T," + r_dftwave.time()+",";
		//  DFT
			outputstring += "M," + r_dft.mean()+",V," + r_dft.variance()+",T," + r_dft.time()+",,";
		//  PAA
			outputstring += "M," + r_paa.mean()+",V," + r_paa.variance()+",T," + r_paa.time()+",,";
		//  MFDR-wave
			outputstring += "M," + 0+",V," + 0+",T," + 0+",";
		//  MFDR-wave-N
			outputstring += "M," + 0+",V," + 0+",T," + 0+",";
		//  MFDR
			outputstring += "M," + r_mfdr_t.mean()+",V," + r_mfdr_t.variance()+",T," + r_mfdr_t.time()+",,";
			fagent.writeLineToFile(outputstring);
			System.out.println("PROGRESS: " + filenamelist.get(i) + "[" + NoC + "] Stored...");
			}
		}
	}
	
	public void runMFDR(String readaddress,String writeaddress ,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MFDR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent,listaddress);
		MFDRParameterFacade facade = new MFDRParameterFacade(3,0.5,6.5);
		double count = 0;
		for(int i = 0 ; i < filenamelist.size() ; i++){
			for(int NoC = NoC_Start ; NoC <= NoC_End ; NoC+=NoC_Interval){
			// Train Parameters
			LinkedList<TimeSeries> tsset = getTimeSeriesListTrain(fagent,readaddress,filenamelist.get(i));
			MFDRParameters parameters = facade.learnMFDRParameters(tsset, NoC, false);
			
			// Test 
			RepresentationErrorResult r_mfdr = runMFDR(tsset, parameters, false);
			count ++;
			System.out.println("PROGRESS(MFDR):"+ (count*100 / 5 / filenamelist.size()) + "%");
			
			// Output
			String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
			outputstring += "MFDR,M," + r_mfdr.mean()+",V," + r_mfdr.variance()+",T," + r_mfdr.time()+",";
			fagent.writeLineToFile(outputstring);
			System.out.println("PROGRESS: " + filenamelist.get(i) + "[" + NoC + "] Stored...");
			}
		}
	}
	
	public void run(String readaddress,String writeaddress ,String listaddress, int NoC_Start, int NoC_Interval, int NoC_End) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress, "C:\\TEST\\MFDR\\Null.txt");
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
			RepresentationErrorResult r_paa = runPAA(tsset, new PAA(NoC));
			count ++;
			System.out.println("PROGRESS(PAA):"+ (count*100 / 30 / filenamelist.size()) + "%");
			RepresentationErrorResult r_pla = runPLA(tsset, new PLA(NoC));
			count ++;
			System.out.println("PROGRESS(PLA): "+ (count*100 / 30 / filenamelist.size()) + "%");
//			RepresentationErrorResult r_dftwave = runDFTWave(tsset, new DFTWave(NoC));
//			count ++;
//			System.out.println("PROGRESS(DFTWave):"+ (count*100 / 30 / filenamelist.size()) + "%");
			RepresentationErrorResult r_dft = runDFT(tsset, new DFT(NoC));
			count ++;
			System.out.println("PROGRESS(DFT):"+ (count*100 / 30 / filenamelist.size()) + "%");
//			RepresentationErrorResult r_mfdr = runMFDR(tsset, parameters, false);
//			count ++;
//			System.out.println("PROGRESS(MFDR):"+ (count*100 / 30 / filenamelist.size()) + "%");
//			RepresentationErrorResult r_mfdr_n = runMFDR(tsset, parameters_n, true);
//			count ++;
//			System.out.println("PROGRESS(MFDR-N):"+ (count*100 / 30 / filenamelist.size()) + "%");
			
			
			// Output
			String outputstring = filenamelist.get(i) + ",[" + NoC+"],";
			outputstring += "PLA,M," + r_pla.mean()+",V," + r_pla.variance()+",T," + r_pla.time()+",";
//			outputstring += "DFTWave,M," + r_dftwave.mean()+",V," + r_dftwave.variance()+",T," + r_dftwave.time()+",";
			outputstring += "DFT,M," + r_dft.mean()+",V," + r_dft.variance()+",T," + r_dft.time()+",";
			outputstring += "PAA,M," + r_paa.mean()+",V," + r_paa.variance()+",T," + r_paa.time()+",";
//			outputstring += "MFDR,M," + r_mfdr.mean()+",V," + r_mfdr.variance()+",T," + r_mfdr.time()+",";
//			outputstring += "MFDR-N,M," + r_mfdr_n.mean()+",V," + r_mfdr_n.variance()+",T," + r_mfdr_n.time()+",";
			fagent.writeLineToFile(outputstring);
			System.out.println("PROGRESS: " + filenamelist.get(i) + "[" + NoC + "] Stored...");
			}
		}
	}
	
	public RepresentationErrorResult runPLA1000(LinkedList<TimeSeries> tsset, int[][] pairlist, PLA pla){
		double[] result = new double[1000];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<PLAData>> drlist = new LinkedList<LinkedList<PLAData>>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(pla.getDR(tsset.get(i))) ;
		}
		System.out.println("PLA DR Done");
		// Distance Calculation
		long time = 0;
		long startTime=0,endTime=0;
		for(int round = 0 ; round < 1000 ; round++){
			// Original Distance
			double dist_ori = d.calDistance(tsset.get(pairlist[round][0]), tsset.get(pairlist[round][1]), tsset.get(pairlist[round][0]));
			int size = tsset.get(pairlist[round][0]).size();
			startTime = System.nanoTime();
			double dist_dr = pla.getDistance(drlist.get(pairlist[round][0]), drlist.get(pairlist[round][1]), size , d);
			endTime = System.nanoTime();
			time += endTime - startTime;
			if(dist_ori!=0){
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("PLA Distance Done");
		// Result Analysis
		return new RepresentationErrorResult(sum/count,Stat.variance(result), time);
	}
	
	public RepresentationErrorResult runPAA1000(LinkedList<TimeSeries> tsset, int[][] pairlist, PAA paa){
		double[] result = new double[1000];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<PAAData>> drlist = new LinkedList<LinkedList<PAAData>>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(paa.getDR(tsset.get(i))) ;
		}
		System.out.println("PAA DR Done");
		// Distance Calculation
		long time = 0;
		long startTime=0,endTime=0;
		for(int round = 0 ; round < 1000 ; round++){
			// Original Distance
			double dist_ori = d.calDistance(tsset.get(pairlist[round][0]), tsset.get(pairlist[round][1]), tsset.get(pairlist[round][0]));
			int size = tsset.get(pairlist[round][0]).size();
			startTime = System.nanoTime();
			double dist_dr = paa.getDistance(drlist.get(pairlist[round][0]), drlist.get(pairlist[round][1]), size, d);
			endTime = System.nanoTime();
			time += endTime - startTime;
			if(dist_ori!=0){
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("PAA Distance Done");
		// Result Analysis
		
		return new RepresentationErrorResult(sum/count,Stat.variance(result), time);
	}
	
	public RepresentationErrorResult runDFT1000(LinkedList<TimeSeries> tsset, int[][] pairlist, DFT dft){
		double[] result = new double[1000];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<DFTData> drlist = new LinkedList<DFTData>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(dft.getDR(tsset.get(i))) ;
		}
		System.out.println("DFT DR Done");
		// Distance Calculation
		long time = 0;
		long startTime=0,endTime=0;
		for(int round = 0 ; round < 1000 ; round++){
			// Original Distance
			double dist_ori = d.calDistance(tsset.get(pairlist[round][0]), tsset.get(pairlist[round][1]), tsset.get(pairlist[round][0]));
			startTime = System.nanoTime();
			double dist_dr = dft.getDistance(drlist.get(pairlist[round][0]), drlist.get(pairlist[round][1]), d, tsset.get(pairlist[round][0]).size());
			endTime = System.nanoTime();
			time += endTime - startTime;
			if(dist_ori!=0){
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("DFT Distance Done");
		// Result Analysis
		
		return new RepresentationErrorResult(sum/count,Stat.variance(result), time);
	}
	
	public RepresentationErrorResult runDFTWave1000(LinkedList<TimeSeries> tsset,int[][] pairlist, DFTWave dftwave){
		double[] result = new double[1000];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<DFTWaveData>> drlist = new LinkedList<LinkedList<DFTWaveData>>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(dftwave.getDR(tsset.get(i))) ;
		}
		System.out.println("DFTWave DR Done");
		// Distance Calculation
		long time = 0;
		long startTime=0,endTime=0;
		for(int round = 0 ; round < 1000 ; round++){
			// Original Distance
			double dist_ori = d.calDistance(tsset.get(pairlist[round][0]), tsset.get(pairlist[round][1]), tsset.get(pairlist[round][0]));
			int size = tsset.get(pairlist[round][0]).size();
			startTime = System.nanoTime();
			double dist_dr = dftwave.getDistance(drlist.get(pairlist[round][0]), drlist.get(pairlist[round][1]), d, size);
			endTime = System.nanoTime();
			time += endTime-startTime;
			if(dist_ori!=0){
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("DFTWave Distance Done");
		// Result Analysis
		
		return new RepresentationErrorResult(sum/count,Stat.variance(result), time);
	}
	
	public RepresentationErrorResult runMFDRWave1000(LinkedList<TimeSeries> tsset, MFDRParameters p, boolean use_noise){
		MFDRWave mfdr = new MFDRWave(p.trendNoC(), p.seasonalNoC());
		double[] result = new double[1000];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<MFDRWaveData> drlist = new LinkedList<MFDRWaveData>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			if(use_noise){
				drlist.add(mfdr.getDR(tsset.get(i),p.lowestPeriod()));
			} else{
				drlist.add(mfdr.getDR(tsset.get(i)));
			}
		}
		System.out.println("MFDR DR Done");
		// Distance Calculation
		long time = 0;
		long startTime=0,endTime=0;
		for(int round = 0 ; round < 1000 ; round++){
			int i = (int) (drlist.size()*Math.random());
			int j = (int) (drlist.size()*Math.random());
			while(i==j){
				j = (int) (drlist.size()*Math.random());
			}
			// Original Distance
			// Original Distance
			double dist_ori = d.calDistance(tsset.get(i), tsset.get(j), tsset.get(i));
			int size = tsset.get(i).size();
			startTime = System.nanoTime();
			double dist_dr = mfdr.getDistance(drlist.get(i), drlist.get(j), size , d);
			endTime = System.nanoTime();
			time += endTime-startTime;
			if(dist_ori!=0){
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("MFDR Distance Done");
		// Result Analysis
		return new RepresentationErrorResult(sum/count,Stat.variance(result), time);
	}
	
	public RepresentationErrorResult runMFDR1000(LinkedList<TimeSeries> tsset, int[][] pairlist, MFDRParameterFacade facade, int NoC, boolean use_noise){
		MFDRLCM mfdr = new MFDRLCM(0, 0);
		double[] result = new double[1000];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<MFDRWaveData> drlist = new LinkedList<MFDRWaveData>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			MFDRParameters p = facade.learnMFDRParameters(tsset.get(i), NoC, use_noise);
			mfdr.updateParameters(p.trendNoC(), p.seasonalNoC());
			if(use_noise){
				drlist.add(mfdr.getDR(tsset.get(i),p.lowestPeriod()));
			} else{
				drlist.add(mfdr.getDR(tsset.get(i)));
			}
		}
		System.out.println("MFDR DR Done");
		// Distance Calculation
		long time = 0;
		long startTime=0,endTime=0;
		for(int round = 0 ; round < 1000 ; round++){
			// Original Distance
			double dist_ori = d.calDistance(tsset.get(pairlist[round][0]), tsset.get(pairlist[round][1]), tsset.get(pairlist[round][0]));
			int size =  tsset.get(pairlist[round][0]).size();
			startTime = System.nanoTime();
			double dist_dr = mfdr.getDistance(drlist.get(pairlist[round][0]), drlist.get(pairlist[round][1]),size, d);
			endTime = System.nanoTime();
			time += endTime - startTime;
			if(dist_ori!=0){
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("MFDR Distance Done");
		// Result Analysis
		return new RepresentationErrorResult(sum/count,Stat.variance(result), time);
	}
	
	public RepresentationErrorResult runPAA(LinkedList<TimeSeries> tsset, PAA paa){
		double[] result = new double[tsset.size()*(tsset.size()-1)/2];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<PAAData>> drlist = new LinkedList<LinkedList<PAAData>>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(paa.getDR(tsset.get(i))) ;
		}
		System.out.println("PAA DR Done");
		// Distance Calculation
		long startTime=0,endTime=0;
		for(int i = 0 ; i < drlist.size() ; i++){
			for(int j = i+1 ; j<drlist.size();j++){
				// Original Distance
				double dist_ori = d.calDistance(tsset.get(i), tsset.get(j), tsset.get(i));
				int size =  tsset.get(i).size();
				startTime = System.nanoTime();
				double dist_dr = paa.getDistance(drlist.get(i), drlist.get(j),size , d);
				endTime = System.nanoTime();
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("PAA Distance Done");
		// Result Analysis
		
		return new RepresentationErrorResult(sum/count,Stat.variance(result), endTime-startTime);
	}
	
	public RepresentationErrorResult runDFT(LinkedList<TimeSeries> tsset, DFT dft){
		double[] result = new double[tsset.size()*(tsset.size()-1)/2];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<DFTData> drlist = new LinkedList<DFTData>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(dft.getDR(tsset.get(i))) ;
		}
		System.out.println("DFT DR Done");
		// Distance Calculation
		// Distance Calculation
		long startTime=0,endTime=0;
		for(int i = 0 ; i < drlist.size() ; i++){
			for(int j = i+1 ; j<drlist.size();j++){
				// Original Distance
				double dist_ori = d.calDistance(tsset.get(i), tsset.get(j), tsset.get(i));
				startTime = System.nanoTime();
				double dist_dr = dft.getDistance(drlist.get(i), drlist.get(j), d, tsset.get(i).size());
				endTime = System.nanoTime();
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("DFT Distance Done");
		// Result Analysis
		
		return new RepresentationErrorResult(sum/count,Stat.variance(result), endTime-startTime);
	}
	
	public RepresentationErrorResult runDFTWave(LinkedList<TimeSeries> tsset, DFTWave dftwave){
		double[] result = new double[tsset.size()*(tsset.size()-1)/2];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<DFTWaveData>> drlist = new LinkedList<LinkedList<DFTWaveData>>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(dftwave.getDR(tsset.get(i))) ;
		}
		System.out.println("DFTWave DR Done");
		// Distance Calculation
		long startTime=0,endTime=0;
		for(int i = 0 ; i < drlist.size() ; i++){
			for(int j = i+1 ; j<drlist.size();j++){
				// Original Distance
				double dist_ori = d.calDistance(tsset.get(i), tsset.get(j), tsset.get(i));
				startTime = System.nanoTime();
				double dist_dr = dftwave.getDistance(drlist.get(i), drlist.get(j), d, tsset.get(i).size());
				endTime = System.nanoTime();
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("DFTWave Distance Done");
		// Result Analysis
		
		return new RepresentationErrorResult(sum/count,Stat.variance(result), endTime-startTime);
	}
	
	public RepresentationErrorResult runPLA(LinkedList<TimeSeries> tsset, PLA pla){
		double[] result = new double[tsset.size()*(tsset.size()-1)/2];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<PLAData>> drlist = new LinkedList<LinkedList<PLAData>>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			drlist.add(pla.getDR(tsset.get(i))) ;
		}
		System.out.println("PLA DR Done");
		// Distance Calculation
		long startTime=0,endTime=0;
		for(int i = 0 ; i < drlist.size() ; i++){
			for(int j = i+1 ; j<drlist.size();j++){
				// Original Distance
				double dist_ori = d.calDistance(tsset.get(i), tsset.get(j), tsset.get(i));
				startTime = System.nanoTime();
				double dist_dr = pla.getDistance(drlist.get(i), drlist.get(j), tsset.get(i).size(), d);
				endTime = System.nanoTime();
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("PLA Distance Done");
		// Result Analysis
		return new RepresentationErrorResult(sum/count,Stat.variance(result), endTime-startTime);
	}
	
	public RepresentationErrorResult runMFDRWave(LinkedList<TimeSeries> tsset, MFDRParameters p, boolean use_noise){
		MFDRWave mfdr = new MFDRWave(p.trendNoC(), p.seasonalNoC());
		double[] result = new double[tsset.size()*(tsset.size()-1)/2];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<MFDRWaveData> drlist = new LinkedList<MFDRWaveData>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			TimeSeries reduced ;
			if(use_noise){
				drlist.add(mfdr.getDR(tsset.get(i),p.lowestPeriod()));
			} else{
				drlist.add(mfdr.getDR(tsset.get(i)));
			}
		}
		System.out.println("MFDR DR Done");
		// Distance Calculation
		long startTime=0,endTime=0;
		for(int i = 0 ; i < drlist.size() ; i++){
			for(int j = i+1 ; j<drlist.size();j++){
				// Original Distance
				double dist_ori = d.calDistance(tsset.get(i), tsset.get(j), tsset.get(i));
				startTime = System.nanoTime();
				double dist_dr = mfdr.getDistance(drlist.get(i), drlist.get(j), tsset.get(i).size(), d);
				endTime = System.nanoTime();
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("MFDR Distance Done");
		// Result Analysis
		return new RepresentationErrorResult(sum/count,Stat.variance(result), endTime-startTime);
	}
	
	public RepresentationErrorResult runMFDR(LinkedList<TimeSeries> tsset, MFDRParameters p, boolean use_noise){
		MFDR mfdr = new MFDR(p.trendNoC(), p.seasonalNoC());
		double[] result = new double[tsset.size()*(tsset.size()-1)/2];
		int count = 0;
		double sum = 0;
		Distance d = new EuclideanDistance();
		LinkedList<MFDRWaveData> drlist = new LinkedList<MFDRWaveData>();
		// Accumulate DR data
		for(int i = 0 ; i < tsset.size() ; i++){
			TimeSeries reduced ;
			if(use_noise){
				drlist.add(mfdr.getDR(tsset.get(i),p.lowestPeriod()));
			} else{
				drlist.add(mfdr.getDR(tsset.get(i)));
			}
		}
		System.out.println("MFDR DR Done");
		// Distance Calculation
		long startTime=0,endTime=0;
		for(int i = 0 ; i < drlist.size() ; i++){
			for(int j = i+1 ; j<drlist.size();j++){
				// Original Distance
				double dist_ori = d.calDistance(tsset.get(i), tsset.get(j), tsset.get(i));
				startTime = System.nanoTime();
				double dist_dr = mfdr.getDistance(drlist.get(i), drlist.get(j), tsset.get(i), d);
				endTime = System.nanoTime();
				result[count]=Math.abs(dist_ori-dist_dr)/dist_ori;
				count++;
				sum += Math.abs(dist_ori-dist_dr)/dist_ori;
			}
		}
		System.out.println("MFDR Distance Done");
		// Result Analysis
		return new RepresentationErrorResult(sum/count,Stat.variance(result), endTime-startTime);
	}
	
	
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







