package experiment;

import experiment_cores.CopyOfExperiment_Events;
import experiment_cores.CopyOfExperiment_Faults;
import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TCD TEST
//		Experiment_TCD et = new Experiment_TCD("TCD\\QE_Estimator\\", "TCD\\");
//		et.runSet(20, 0.005, 0.05, 0.005);
//		et.runSet(20, 0.025, 0.085, 0.01);
		
		// Fault Detection==========================================================================
		CopyOfExperiment_Faults f;
//		f = new CopyOfExperiment_Faults("FaultType\\Exponential_Event\\Deviation\\Median_Estimator\\", "FaultType\\Exponential_Event\\Deviation\\", "Deviation");
//		f.runSet(20, 0.4, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Exponential_Event\\Stuck\\Median_Estimator\\", "FaultType\\Exponential_Event\\Stuck\\", "Stuck");
//		f.runSet(20, 0.4, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Exponential_Event\\Noisy\\Median_Estimator\\", "FaultType\\Exponential_Event\\Noisy\\", "Noisy");
//		f.runSet(20, 0.4, 0.6);
		f = new CopyOfExperiment_Faults("FaultType\\Diffusive_Event\\Deviation\\Median_Estimator\\", "FaultType\\Diffusive_Event\\Deviation\\", "Deviation");
		f.runSet(20, 0.4, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Diffusive_Event\\Stuck\\Median_Estimator\\", "FaultType\\Diffusive_Event\\Stuck\\", "Stuck");
//		f.runSet(20, 0.4, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Diffusive_Event\\Noisy\\Median_Estimator\\", "FaultType\\Diffusive_Eventt\\Noisy\\", "Noisy");
//		f.runSet(20, 0.4, 0.6);
		
		//Event Detection==========================================================================
//		CopyOfExperiment_Events e;
//		e = new CopyOfExperiment_Events("EventChange\\Median_Estimator\\", "EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(20, 0.001, 0.01, i);
//		}	
	}
	

}
