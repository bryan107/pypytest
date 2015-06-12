package mfdr.learning;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Regression;
import mfdr.datastructure.MFDRDistanceDetails;
import mfdr.datastructure.TimeSeries;
import mfdr.dimensionality.datastructure.MFDRData;
import mfdr.dimensionality.reduction.MFDR;
import mfdr.distance.Distance;
import mfdr.learning.datastructure.TrainingSet;

public class LR4DLearning extends LinearLearning {
	private static Log logger = LogFactory.getLog(LR4DLearning.class);

	
	public LR4DLearning() {
	}
	
//	public LR3DAngleLearning(LinkedList<TrainingSet> ts, Distance d) {
//		trainingParameters(ts);
//	}
//	
//	public LR3DAngleLearning(TimeSeries[] ts, MFDR mfdr, Distance d) {
//		trainingParameters(ts, mfdr);
//	}

	@Override
	public String getType() {
		logger.info("Using 4 dimentional LR Learning");
		return "LR4D";
	}

	/**
	 * This is the main function for training
	 */
	@Override
	public LinearLearningResults trainingParameters(TimeSeries[] ts, MFDR mfdr, Distance d) {
		LinkedList<TrainingSet> trainingset = getTrainingSet(ts, mfdr, d);
		return trainingParameters(trainingset);
	}

	@Override
	public LinearLearningResults trainingParameters(LinkedList<TimeSeries> ts, MFDR mfdr, Distance d) {
		LinkedList<TrainingSet> trainingset = getTrainingSet(ts, mfdr, d);
		return trainingParameters(trainingset);
	}

	
	@Override
	public LinearLearningResults trainingParameters(LinkedList<TrainingSet> ts) {
		double[][] traininginput = new double[3][ts.size()];
		double[] trainingoutput = new double[ts.size()];
		// Prepare training data structure
		for (int i = 0; i < ts.size(); i++) {
			traininginput[0][i] = ts.get(i).trendDist();
			traininginput[1][i] = ts.get(i).seasonalDist();
			if(ts.get(i).noiseDist() == 0){
				traininginput[2][i] = 0.0001;
			}else{
				traininginput[2][i] = ts.get(i).noiseDist();
			}
			trainingoutput[i] = ts.get(i).originalDist();
		}
		// 4D input Linear regression
		Regression reg = new Regression(traininginput, trainingoutput);
		reg.linear();
		double[] coeff = reg.getBestEstimates();
		return new LinearLearningResults(coeff[1], coeff[2], coeff[3], coeff[0]);
	}
	
	@Override
	public LinkedList<TrainingSet> getTrainingSet(LinkedList<TimeSeries> ts, MFDR mfdr, Distance d) {
		LinkedList<TrainingSet> trainingset = new LinkedList<TrainingSet>();
		MFDRData[] mfdrdata= new MFDRData[ts.size()];
		for(int i = 0 ; i < ts.size() ; i++){
			mfdrdata[i] = mfdr.getDR(ts.get(i));
		}
		for (int i = 0; i < ts.size() - 1; i++) {
			for(int j = i+1 ; j < ts.size() ; j++){
				MFDRDistanceDetails details = mfdr.getDistanceDetails(mfdrdata[i], mfdrdata[j], ts.get(i), d);
				double oridist = d.calDistance(ts.get(i), ts.get(j), ts.get(i));
				trainingset.add(new TrainingSet(details.trend(), details.seasonal(), details.noise(), oridist));
			}
		}
		return trainingset;
	}
	
	@Override
	public LinkedList<TrainingSet> getTrainingSet(TimeSeries[] ts, MFDR mfdr, Distance d) {
		LinkedList<TrainingSet> trainingset = new LinkedList<TrainingSet>();
		MFDRData[] mfdrdata= new MFDRData[ts.length];
		for(int i = 0 ; i < ts.length ; i++){
			mfdrdata[i] = mfdr.getDR(ts[i]);
		}
		for (int i = 0; i < ts.length - 1; i++) {
			for(int j = i+1 ; j < ts.length ; j++){
				MFDRDistanceDetails details = mfdr.getDistanceDetails(mfdrdata[i], mfdrdata[j], ts[i], d);
				double oridist = d.calDistance(ts[i], ts[j], ts[i]);
				trainingset.add(new TrainingSet(details.trend(), details.seasonal(), details.noise(), oridist));
			}
		}
		return trainingset;
	}
}
