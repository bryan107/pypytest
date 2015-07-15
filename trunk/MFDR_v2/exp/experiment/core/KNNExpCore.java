package experiment.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import mfdr.core.MFDRWaveParameterFacade;
import mfdr.core.MFDRParameters;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.datastructure.DFTWaveData;
import mfdr.dimensionality.datastructure.MFDRWaveData;
import mfdr.dimensionality.datastructure.PAAData;
import mfdr.dimensionality.datastructure.PLAData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DFTWave;
import mfdr.dimensionality.reduction.MFDRWave;
import mfdr.dimensionality.reduction.PAA;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.file.FileAccessAgent;
import experiment.utility.DataParser;
import experiment.utility.UCRData;
import experiment.utility.UCRDataDetails;
import flanagan.analysis.Stat;

public class KNNExpCore {
	public void run(String readaddress, String writeaddress,
			String listaddress, int NoC_Start, int NoC_Interval, int NoC_End,
			int K) {
		FileAccessAgent fagent = new FileAccessAgent(writeaddress+"_["+K+"]",
				"C:\\TEST\\MDFR\\Null.txt");
		FileAccessAgent fagent2 = new FileAccessAgent(writeaddress,
				"C:\\TEST\\MDFR\\Null.txt");
		LinkedList<String> filenamelist = getFileNameList(fagent, listaddress);
		MFDRWaveParameterFacade facade = new MFDRWaveParameterFacade(3, 0.5, 6.5);
		double count = 0;
		for (int i = 0; i < filenamelist.size(); i++) {
			for (int NoC = NoC_Start; NoC <= NoC_End; NoC += NoC_Interval) {
				// Train Parameters
				LinkedList<TimeSeries> trainseries = getTimeSeriesListTrain(
						fagent, readaddress, filenamelist.get(i));
				MFDRParameters parameters = facade.learnMFDRParameters(
						trainseries, NoC, false);
				System.out.println("Train MFDR Done");
				MFDRParameters parameters_n = facade.learnMFDRParameters(
						trainseries, NoC, true);
				System.out.println("Train MFDR-N Done");

				// Test
				LinkedList<UCRDataDetails> trainset = getDataDetailsListTrain(
						fagent2, readaddress, filenamelist.get(i));
				System.out.println("Load Train Detail Done");
				LinkedList<UCRDataDetails> testset = getDataDetailsListTest(
						fagent2, readaddress, filenamelist.get(i));
				System.out.println("Load Test Detail Done");
				RepresentationErrorResult r_paa = runPAA(trainset, testset,
						new PAA(NoC), K);
				count++;
				System.out.println("PROGRESS(PAA):"
						+ (count * 100 / 30 / filenamelist.size()) + "%");
				RepresentationErrorResult r_pla = runPLA(trainset, testset,
						new PLA(NoC), K);
				count++;
				System.out.println("PROGRESS(PLA): "
						+ (count * 100 / 30 / filenamelist.size()) + "%");
				RepresentationErrorResult r_dftwave = runDFTWave(trainset,
						testset, new DFTWave(NoC), K);
				count++;
				System.out.println("PROGRESS(DFTWave):"
						+ (count * 100 / 30 / filenamelist.size()) + "%");
				RepresentationErrorResult r_dft = runDFT(trainset, testset,
						new DFT(NoC), K);
				count++;
				System.out.println("PROGRESS(DFT):"
						+ (count * 100 / 30 / filenamelist.size()) + "%");
				RepresentationErrorResult r_mfdr = runMFDR(trainset, testset,
						K, parameters, false);
				count++;
				System.out.println("PROGRESS(MFDR):"
						+ (count * 100 / 30 / filenamelist.size()) + "%");
				RepresentationErrorResult r_mfdr_n = runMFDR(trainset, testset,
						K, parameters_n, true);
				count++;
				System.out.println("PROGRESS(MFDR-N):"
						+ (count * 100 / 30 / filenamelist.size()) + "%");

				// Output
				String outputstring = filenamelist.get(i) + ",[" + NoC + "],";
				outputstring += "PLA,M," + r_pla.mean() + ",V,"
						+ r_pla.variance() + ",T," + r_pla.time() + ",";
				outputstring += "DFTWave,M," + r_dftwave.mean() + ",V,"
						+ r_dftwave.variance() + ",T," + r_dftwave.time() + ",";
				outputstring += "DFT,M," + r_dft.mean() + ",V,"
						+ r_dft.variance() + ",T," + r_dft.time() + ",";
				outputstring += "PAA,M," + r_paa.mean() + ",V,"
						+ r_paa.variance() + ",T," + r_paa.time() + ",";
				outputstring += "MFDR,M," + r_mfdr.mean() + ",V,"
						+ r_mfdr.variance() + ",T," + r_mfdr.time() + ",";
				outputstring += "MFDR-N,M," + r_mfdr_n.mean() + ",V,"
						+ r_mfdr_n.variance() + ",T," + r_mfdr_n.time() + ",";
				fagent.writeLineToFile(outputstring);
				System.out.println("PROGRESS: " + filenamelist.get(i) + "["
						+ NoC + "] Stored...");
			}
		}
	}

	public RepresentationErrorResult runPAA(
			LinkedList<UCRDataDetails> trainset,
			LinkedList<UCRDataDetails> testset, PAA paa, int K) {
		double successcount = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<PAAData>> traindrlist = new LinkedList<LinkedList<PAAData>>();
		LinkedList<LinkedList<PAAData>> testdrlist = new LinkedList<LinkedList<PAAData>>();
		// Accumulate DR data
		for (int i = 0; i < trainset.size(); i++) {
			traindrlist.add(paa.getDR(trainset.get(i).timeSeries()));
		}
		for (int i = 0; i < testset.size(); i++) {
			testdrlist.add(paa.getDR(testset.get(i).timeSeries()));
		}
		System.out.println("PAA DR Done");
		// Distance Calculation
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < testdrlist.size(); i++) {
			HashMap<Double, Integer> candidates = new HashMap<Double, Integer>();
			double[] dists = new double[traindrlist.size()];
			for (int j = 0; j < traindrlist.size(); j++) {
				// Original Distance
				double dist_dr = paa.getDistance(testdrlist.get(i),
						traindrlist.get(j), testset.get(i).timeSeries().size(), d);
				dists[j] = dist_dr;
				candidates.put(dist_dr, trainset.get(j).ClusterNumber());
			}
			successcount = getKNNResult(testset, K, successcount, i,
					candidates, dists);

		}
		long endTime = System.currentTimeMillis();
		System.out.println("PAA KNN Done");
		return new RepresentationErrorResult(successcount / testdrlist.size(),
				0, endTime - startTime);
	}

	public RepresentationErrorResult runDFT(
			LinkedList<UCRDataDetails> trainset,
			LinkedList<UCRDataDetails> testset, DFT dft, int K) {
		double successcount = 0;
		Distance d = new EuclideanDistance();
		LinkedList<DFTData> traindrlist = new LinkedList<DFTData>();
		LinkedList<DFTData> testdrlist = new LinkedList<DFTData>();
		// Accumulate DR data
		for (int i = 0; i < trainset.size(); i++) {
			traindrlist.add(dft.getDR(trainset.get(i).timeSeries()));
		}
		for (int i = 0; i < testset.size(); i++) {
			testdrlist.add(dft.getDR(testset.get(i).timeSeries()));
		}
		System.out.println("DFT DR Done");
		// Distance Calculation
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < testdrlist.size(); i++) {
			HashMap<Double, Integer> candidates = new HashMap<Double, Integer>();
			double[] dists = new double[traindrlist.size()];
			for (int j = 0; j < traindrlist.size(); j++) {
				// Original Distance
				double dist_dr = dft.getDistance(testdrlist.get(i),
						traindrlist.get(j), d, testset.get(i).timeSeries()
								.size());
				dists[j] = dist_dr;
				candidates.put(dist_dr, trainset.get(j).ClusterNumber());
			}
			successcount = getKNNResult(testset, K, successcount, i,
					candidates, dists);

		}
		long endTime = System.currentTimeMillis();
		System.out.println("DFT KNN Done");
		return new RepresentationErrorResult(successcount / testdrlist.size(),
				0, endTime - startTime);
	}

	public RepresentationErrorResult runDFTWave(
			LinkedList<UCRDataDetails> trainset,
			LinkedList<UCRDataDetails> testset, DFTWave dft, int K) {
		double successcount = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<DFTWaveData>> traindrlist = new LinkedList<LinkedList<DFTWaveData>>();
		LinkedList<LinkedList<DFTWaveData>> testdrlist = new LinkedList<LinkedList<DFTWaveData>>();
		// Accumulate DR data
		for (int i = 0; i < trainset.size(); i++) {
			traindrlist.add(dft.getDR(trainset.get(i).timeSeries()));
		}
		for (int i = 0; i < testset.size(); i++) {
			testdrlist.add(dft.getDR(testset.get(i).timeSeries()));
		}
		System.out.println("DFTWave DR Done");
		// Distance Calculation
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < testdrlist.size(); i++) {
			HashMap<Double, Integer> candidates = new HashMap<Double, Integer>();
			double[] dists = new double[traindrlist.size()];
			for (int j = 0; j < traindrlist.size(); j++) {
				// Original Distance
				double dist_dr = dft.getDistance(testdrlist.get(i),
						traindrlist.get(j), d, testset.get(i).timeSeries()
								.size());
				dists[j] = dist_dr;
				candidates.put(dist_dr, trainset.get(j).ClusterNumber());
			}
			successcount = getKNNResult(testset, K, successcount, i,
					candidates, dists);

		}
		long endTime = System.currentTimeMillis();
		System.out.println("DFTWave KNN Done");
		return new RepresentationErrorResult(successcount / testdrlist.size(),
				0, endTime - startTime);
	}

	public RepresentationErrorResult runPLA(
			LinkedList<UCRDataDetails> trainset,
			LinkedList<UCRDataDetails> testset, PLA pla, int K) {
		double successcount = 0;
		Distance d = new EuclideanDistance();
		LinkedList<LinkedList<PLAData>> traindrlist = new LinkedList<LinkedList<PLAData>>();
		LinkedList<LinkedList<PLAData>> testdrlist = new LinkedList<LinkedList<PLAData>>();
		// Accumulate DR data
		for (int i = 0; i < trainset.size(); i++) {
			traindrlist.add(pla.getDR(trainset.get(i).timeSeries()));
		}
		for (int i = 0; i < testset.size(); i++) {
			testdrlist.add(pla.getDR(testset.get(i).timeSeries()));
		}
		System.out.println("PLA DR Done");
		// Distance Calculation
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < testdrlist.size(); i++) {
			HashMap<Double, Integer> candidates = new HashMap<Double, Integer>();
			double[] dists = new double[traindrlist.size()];
			for (int j = 0; j < traindrlist.size(); j++) {
				// Original Distance
				double dist_dr = pla.getDistance(testdrlist.get(i),
						traindrlist.get(j), testset.get(i).timeSeries().size(), d);
				dists[j] = dist_dr;
				candidates.put(dist_dr, trainset.get(j).ClusterNumber());
			}
			successcount = getKNNResult(testset, K, successcount, i,
					candidates, dists);

		}
		long endTime = System.currentTimeMillis();
		System.out.println("PLA KNN Done");
		return new RepresentationErrorResult(successcount / testdrlist.size(),
				0, endTime - startTime);
	}

	public RepresentationErrorResult runMFDR(
			LinkedList<UCRDataDetails> trainset,
			LinkedList<UCRDataDetails> testset, int K, MFDRParameters p,
			boolean use_noise) {
		double successcount = 0;
		MFDRWave mfdr = new MFDRWave(p.trendNoC(), p.seasonalNoC());
		Distance d = new EuclideanDistance();
		LinkedList<MFDRWaveData> traindrlist = new LinkedList<MFDRWaveData>();
		LinkedList<MFDRWaveData> testdrlist = new LinkedList<MFDRWaveData>();
		// Accumulate DR data
		for (int i = 0; i < trainset.size(); i++) {
			if (use_noise) {
				traindrlist.add(mfdr.getDR(trainset.get(i).timeSeries(),
						p.lowestPeriod()));
			} else {
				traindrlist.add(mfdr.getDR(trainset.get(i).timeSeries()));
			}
		}
		for (int i = 0; i < testset.size(); i++) {
			if (use_noise) {
				testdrlist.add(mfdr.getDR(testset.get(i).timeSeries(),
						p.lowestPeriod()));
			} else {
				testdrlist.add(mfdr.getDR(testset.get(i).timeSeries()));
			}
		}
		System.out.println("MFDR DR Done");
		// Distance Calculation
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < testdrlist.size(); i++) {
			HashMap<Double, Integer> candidates = new HashMap<Double, Integer>();
			double[] dists = new double[traindrlist.size()];
			for (int j = 0; j < traindrlist.size(); j++) {
				// Original Distance
				double dist_dr = mfdr.getDistance(testdrlist.get(i),
						traindrlist.get(j), testset.get(i).timeSeries().size(), d);
				dists[j] = dist_dr;
				candidates.put(dist_dr, trainset.get(j).ClusterNumber());
			}
			successcount = getKNNResult(testset, K, successcount, i,
					candidates, dists);

		}
		long endTime = System.currentTimeMillis();
		System.out.println("PLA KNN Done");
		return new RepresentationErrorResult(successcount / testdrlist.size(),
				0, endTime - startTime);
	}

	public double getKNNResult(LinkedList<UCRDataDetails> testset, int K,
			double successcount, int i, HashMap<Double, Integer> candidates,
			double[] dists) {
		// Get K-NN ID
		Arrays.sort(dists);
		HashMap<Integer, Integer> votes = new HashMap<Integer, Integer>();
		for (int k = 0; k < K; k++) {
			if (votes.containsKey(candidates.get(dists[k]))) {
				votes.put(candidates.get(dists[k]),
						votes.get(candidates.get(dists[k])) + 1);
			} else {
				votes.put(candidates.get(dists[k]), 1);
			}
		}

		int candidate = -1;
		int candidate_vote = 0;
		Iterator<Integer> it = votes.keySet().iterator();
		while (it.hasNext()) {
			Integer id = (Integer) it.next();
			if (votes.get(id) > candidate_vote) {
				candidate = id;
			}
		}
		if (candidate == testset.get(i).ClusterNumber()) {
			successcount++;
		}
		return successcount;
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

	public LinkedList<UCRDataDetails> getDataDetailsListTest(
			FileAccessAgent fagent, String address, String filename) {
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<UCRDataDetails> ts = new LinkedList<UCRDataDetails>();
		fagent.updatereadingpath(address + filename + "\\" + filename + "_TEST");
		// Iterate through test data
		while (true) {
			UCRDataDetails temp = parser.getTimeSeriesDetails();
			if (temp.ClusterNumber() == -1)
				break;
			ts.add(temp);
		}
		return ts;
	}

	public LinkedList<UCRDataDetails> getDataDetailsListTrain(
			FileAccessAgent fagent, String address, String filename) {
		DataParser parser = new DataParser(new UCRData(), fagent);
		LinkedList<UCRDataDetails> ts = new LinkedList<UCRDataDetails>();
		fagent.updatereadingpath(address + filename + "\\" + filename
				+ "_TRAIN");
		// Iterate through train data
		while (true) {
			UCRDataDetails temp = parser.getTimeSeriesDetails();
			if (temp.ClusterNumber() == -1)
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
