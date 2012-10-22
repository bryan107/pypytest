package experiment;

import experiment_cores.CopyOfExperiment_Events;
import experiment_cores.CopyOfExperiment_Faults;
import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TCD TEST====================================================
//		Experiment_TCD et = new Experiment_TCD("TCD\\QE_Estimator\\", "TCD\\");
//		et.runSet(15, 0.005, 0.05, 0.005);
//		et.runSet(15, 0.025, 0.085, 0.01);
//		et.runSet(15, 0.075, 0.14, 0.02);
//		et.runSet(15, 0.12, 0.19, 0.03);
		
		
		// Fault Detection===========================================
		
		CopyOfExperiment_Faults f;
		
//		f = new CopyOfExperiment_Faults("FaultType\\Exponential_Event\\Deviation\\Median_Estimator\\", "FaultType\\Exponential_Event\\Deviation\\", "Deviation");
//		f.runSet(15, 0.1, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Exponential_Event\\Stuck\\Median_Estimator\\", "FaultType\\Exponential_Event\\Stuck\\", "Stuck");
//		f.runSet(15, 0.1, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Exponential_Event\\Noisy\\Median_Estimator\\", "FaultType\\Exponential_Event\\Noisy\\", "Noisy");
//		f.runSet(15, 0.1, 0.6);
				
		f = new CopyOfExperiment_Faults("FaultType\\Diffusive_Event\\Deviation\\Median_Estimator\\", "FaultType\\Diffusive_Event\\Deviation\\", "Deviation");
		f.runSet(15, 0.1, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Diffusive_Event\\Stuck\\Median_Estimator\\", "FaultType\\Diffusive_Event\\Stuck\\", "Stuck");
//		f.runSet(15, 0.1, 0.6);
//		f = new CopyOfExperiment_Faults("FaultType\\Diffusive_Event\\Noisy\\Median_Estimator\\", "FaultType\\Diffusive_Event\\Noisy\\", "Noisy");
//		f.runSet(15, 0.1, 0.6);
		//Event Detection==================================================
//		CopyOfExperiment_Events e = new CopyOfExperiment_Events("EventChange\\Median_Estimator\\", "EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(15, 0.001, 0.01, i);
//		}	
	}
	

}
