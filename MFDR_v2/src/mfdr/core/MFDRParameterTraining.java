//package mfdr.core;
//
//import java.util.LinkedList;
//
//import mfdr.datastructure.TimeSeries;
//import mfdr.math.emd.EMD;
//import mfdr.math.emd.datastructure.IMFS;
//import flanagan.analysis.Stat;
//
//public class MFDRParameterTraining {
//
//	private int NoC;
//	
//	public MFDRParameterTraining(int N){
//		
//	}
//	
//	public void updateNoC(int NoC){
//		this.NoC = NoC;
//	}
//	
//	/*
//	 * PHASE 1: Learn Window Size
//	 */
//
//	/**
//	 * Learn window size with LinkedList input TimeSeries 
//	 * @param ts
//	 * @return WindowSize
//	 */
//	public WindowSize learnWindowSizes(LinkedList<TimeSeries> ts) {
//		double[] windowsize_noises = new double[ts.size()];
//		double[] windowsize_trends = new double[ts.size()];
//		WindowSize ws;
//		// Learn Window sizes of with the training data set
//		for(int i = 0 ; i < ts.size() ; i++){
//			ws = learnWindowSizes(ts.get(i));
//			windowsize_noises[i] = ws.noise();
//			windowsize_trends[i] = ws.trend();
//		}
//		// Take the medians as final results
//		double windowsize_noise = Stat.median(windowsize_noises);
//		double windowsize_trend = Stat.median(windowsize_trends);
//		// Adjust window sizes to fit the sampling rate of input time series. 
//		windowsize_noise = (int) (windowsize_noise / ts.peek().timeInterval()) * ts.peek().timeInterval();
//		windowsize_trend = (int) (Math.log((windowsize_trend / windowsize_noise))/ Math.log(2));
//		windowsize_trend = Math.pow(2, windowsize_trend) *windowsize_noise;
//		return new WindowSize(windowsize_noise,windowsize_trend);
//	}
//	
//	/**
//	 * Learn window size with Array input TimeSeries 
//	 * @param ts
//	 * @return
//	 */
//	public WindowSize learnWindowSizes(TimeSeries[] ts) {
//		double[] windowsize_noises = new double[ts.length];
//		double[] windowsize_trends = new double[ts.length];
//		WindowSize ws;
//		// Learn Window sizes of with the training data set
//		for(int i = 0 ; i < ts.length ; i++){
//			ws = learnWindowSizes(ts[i]);
//			windowsize_noises[i] = ws.noise();
//			windowsize_trends[i] = ws.trend();
//		}
//		// Take the medians as final results
//		double windowsize_noise = Stat.median(windowsize_noises);
//		double windowsize_trend = Stat.median(windowsize_trends);
//		// Adjust window sizes to fit the sampling rate of input time series. 
//		windowsize_noise = (int) (windowsize_noise / ts[0].timeInterval()) * ts[0].timeInterval();
//		windowsize_trend = (int) (Math.log((windowsize_trend / windowsize_noise))/ Math.log(2));
//		windowsize_trend = Math.pow(2, windowsize_trend) *windowsize_noise;
//		return new WindowSize(windowsize_noise,windowsize_trend);
//	}
//	/**
//	 * This function learns the window sizes from a time series
//	 * @param ts
//	 * @return
//	 */
//	public WindowSize learnWindowSizes(TimeSeries ts) {
//		// STEP 1 : EMD
//		// EMD service object
//		EMD emd = new EMD(ts, zerocrossingaccuracy, IFparamaters[0],
//				IFparamaters[1], IFparamaters[2]);
//		// Calculate IMF with EMD
//		IMFS imfs = emd.getIMFs(MAXLEVEL);
//		
//		for(int i = 0 ; i < imfs.size() ; i++){
//			try {
//				System.out.println("IMF[" + i + "]: " + imfs.get(i).averageWavelength());
//			} catch (Exception e) {
//				System.out.println("IMF[" + i + "]: Infinit");
//			}
//		}
//		
//		// STEP 2: AYALYZE IMFs
//		double windowsize_noise = wfilter.getWhiteNoiseWindowSize(imfs, ts);
//		double windowsize_trend = tfilter.getTrendWindowSize(imfs, windowsize_noise);
//		
//		// STEP 4: Set training result;
//		return new WindowSize(windowsize_noise, windowsize_trend);
//	}
//}
