package mfdr.dimensionality.reduction;

import java.sql.Time;
import java.util.LinkedList;

import math.jwave.Transform;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.wavelets.haar.Haar1;
import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.DFTData;
import mfdr.dimensionality.datastructure.DWTData;
import mfdr.dimensionality.datastructure.MFDRData;
import mfdr.dimensionality.reduction.DFT;
import mfdr.dimensionality.reduction.DWT;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.dimensionality.reduction.PLA;
import mfdr.distance.Distance;
import mfdr.distance.EuclideanDistance;
import mfdr.utility.File;
import mfdr.utility.Print;
import junit.framework.TestCase;

public class TestALL extends TestCase {
	private final double T1 = 64;
	private final double T2 = 16;
	private final double ANGLE = Math.PI;
	private final int datalength = 1024;
	private final String[] readaddress = { "C:\\TEST\\MFDR\\TEST_GGGG.csv",
			"C:\\TEST\\MFDR\\TEST_GGGG.csv" };
	private final String[] writeaddress = { "C:\\TEST\\MFDR\\TEST_GGGG.csv",
			"C:\\TEST\\MFDR\\TEST_GGGG.csv" };

	public void testALL() {
		PLA pla = new PLA(T2);
		DWT dwt = new DWT(T2);
		DFT dft = new DFT(T2,2);
		MFDR mfdr = new MFDR(T1, T2, ANGLE);
		TimeSeries[] ts = generateTimeSeries();
//		// **** Write *****
//		 for(int i = 0 ; i < ts.length ; i++){
//		 // Generate DR results in full resolution
//
//		 TimeSeries plafull = pla.getFullResolutionDR(ts[i]);
//		 TimeSeries dwtfull = dwt.getFullResolutionDR(ts[i]);
//		 TimeSeries mfdrfull = mfdr.getFullResolutionDR(ts[i]);
//		 // Save Data into File
//		 File.getInstance().saveTimeToFile(ts[i], writeaddress[i]);
//		 File.getInstance().saveLinkedListToFile("TS", ts[i], writeaddress[i]);
//		 File.getInstance().saveLinkedListToFile("PLA", plafull,
//		 writeaddress[i]);
//		 File.getInstance().saveLinkedListToFile("DWT", dwtfull,
//		 writeaddress[i]);
//		 File.getInstance().saveLinkedListToFile("MFDR", mfdrfull,
//		 writeaddress[i]);
//		 }

		// **** Read *****
		// ts = new TimeSeries[2];
		// ts[0] = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[0]);
		// ts[1] = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[1]);
		// TimeSeries pla1 = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[0], "PLA");
		// TimeSeries pla2 = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[1], "PLA");
		// TimeSeries dwt1 = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[0], "DWT");
		// TimeSeries dwt2 = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[1], "DWT");
		// TimeSeries mfdr1 = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[0], "MFDR");
		// TimeSeries mfdr2 = (TimeSeries)
		// File.getInstance().readTimeSeriesFromFile(readaddress[1], "MFDR");
		//
		// // Print TIME Series
		// System.out.println("TS1:");
		// Print.getInstance().printDataLinkedList(ts1, 128);
		// System.out.println("TS2:");
		// Print.getInstance().printDataLinkedList(ts2, 128);
		// System.out.println("PLA1:");
		// Print.getInstance().printDataLinkedList(pla1, 128);
		// System.out.println("PLA2:");
		// Print.getInstance().printDataLinkedList(pla2, 128);
		// System.out.println("DWT1:");
		// Print.getInstance().printDataLinkedList(dwt1, 128);
		// System.out.println("DWT2:");
		// Print.getInstance().printDataLinkedList(dwt2, 128);
		// System.out.println("MFDR1:");
		// Print.getInstance().printDataLinkedList(mfdr1, 128);
		// System.out.println("MFDR2:");
		// Print.getInstance().printDataLinkedList(mfdr2, 128);
		//

		System.out.println("Distance Results:");
		Distance distance = new EuclideanDistance();
		System.out.println("TS: " + distance.calDistance(ts[0], ts[1], ts[0]));
		System.out.println("PLA: " + pla.getDistance(ts[0], ts[1], distance));
		System.out.println("DWT: " + dwt.getDistance(ts[0], ts[1], distance));
		System.out.println("DFT: " + dft.getDistance(ts[0], ts[1], distance));
		System.out.println("MFDR: " + mfdr.getDistance(ts[0], ts[1], distance));
		System.out.println("MFDR_PLA: "	+ mfdr.getDistanceDetails(ts[0], ts[1], distance).pla());
		System.out.println("MFDR_DWT: "	+ mfdr.getDistanceDetails(ts[0], ts[1], distance).dwt());
		System.out.println("MFDR_RAW_PLA: "	+ distance.calDistance(mfdr.getTrend(ts[0]), mfdr.getTrend(ts[1]), ts[0]));
		System.out.println("MFDR_RAW_RES: "	+ distance.calDistance(mfdr.getResidual(ts[0]), mfdr.getResidual(ts[1]), ts[0]));
		System.out.println("MFDR_DWT_FULL_RESO: "
		+ distance.calDistance(mfdr.getFullResolutionDWT(ts[0]), mfdr.getFullResolutionDWT(ts[1]),ts[0]));
		

		// *********** TEST ZONE ***************
//		System.out.println("MFDR_DWT_FULL_MFDR: "
//				+ mfdr.getDistance(mfdr.getFullResolutionDWT(ts[0]), mfdr.getFullResolutionDWT(ts[1]),distance));
//		System.out.println("MFDR_DWT_FULL_DWT: "
//				+ dwt.getDistance(mfdr.getFullResolutionDWT(ts[0]), mfdr.getFullResolutionDWT(ts[1]),distance));
//		//T0
//		File.getInstance().saveTimeToFile(ts[0], writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("TS",ts[0], writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR", mfdr.getFullResolutionDR(ts[0]),
//		writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR_PLA_F", mfdr.getFullResolutionPLA(ts[0]),
//		writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR_DWT_F", mfdr.getFullResolutionDWT(ts[0]),
//		writeaddress[0]);
//		//T1
//		File.getInstance().saveTimeToFile(ts[1], writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("TS",ts[1], writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR", mfdr.getFullResolutionDR(ts[1]),
//		writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR_PLA_F", mfdr.getFullResolutionPLA(ts[1]),
//		writeaddress[0]);
//		File.getInstance().saveLinkedListToFile("MFDR_DWT_F", mfdr.getFullResolutionDWT(ts[1]),
//		writeaddress[0]);
		// *************************************
		//

	}

	private void storeTimeSeries(TimeSeries ts1, TimeSeries ts2) {
		File.getInstance().saveTimeToFile(ts1, writeaddress[0]);
		File.getInstance().saveLinkedListToFile("TS", ts1, writeaddress[0]);
		File.getInstance().saveTimeToFile(ts2, writeaddress[1]);
		File.getInstance().saveLinkedListToFile("TS", ts2, writeaddress[1]);
	}

	private TimeSeries[] generateTimeSeries() {
		TimeSeries ts1 = new TimeSeries();
		generateResidual(ts1, 10, 1, datalength);
		TimeSeries ts2 = new TimeSeries();
		generateResidual(ts2, 5, 1, datalength);

		TimeSeries[] tsarray = { ts1, ts2 };
		return tsarray;
	}

	// TODO FIX DWT DISTANCE ERROR

	private void generateResidual(LinkedList<Data> residual,
			double trendvariation, double noisevariation, long size) {
		for (double i = 0; i < size; i += 1) {
			java.util.Random r = new java.util.Random();
			double noise = r.nextGaussian() * Math.sqrt(noisevariation);
			double trend = trendvariation * Math.pow(i, 0.5);
//			if (i > size / 2) {
//				trend = -trend;
//			}
			// double value = trendvariation*noise;
			double value = trend * Math.sin(i * Math.PI / 12) + trendvariation*21;
			// double value = trend * Math.sin(Math.pow(i, 3))+ noise;
			// double value = 9.5 * Math.sin(i*Math.PI / 3) + 4.5 *
			// Math.cos(i*Math.PI / 6) + noise + trend;
			residual.add(new Data(i, value));
		}
	}
}
