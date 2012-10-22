package experiment;

import experiment_cores.CopyOfExperiment_Events;
import experiment_cores.CopyOfExperiment_Faults;
import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_6 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TCD TEST
//		Experiment_TCD et = new Experiment_TCD("TCD\\QE_Estimator\\", "TCD\\");
//		et.runSet(10, 0.005, 0.05, 0.005);
//		et.runSet(10, 0.025, 0.085, 0.01);
//		et.runSet(10, 0.075, 0.14, 0.02);
//		et.runSet(10, 0.12, 0.19, 0.03);
		
		// Fault Detection
//		CopyOfExperiment_Faults e = new CopyOfExperiment_Faults("FaultType\\Stuck\\Median_Estimator_Rule\\", "FaultType\\Stuck\\", "Stuck");
//		e.runSet(5, 0.1, 0.6);
//		e.runSet(7, 0.1, 0.6);
//		e.runSet(10,0.1, 0.6);
//		e = new CopyOfExperiment_Faults("FaultType\\Deviation\\Median_Estimator\\", "FaultType\\Deviation\\", "Deviation");
//		e.runSet(5, 0.1, 0.6);
//		e.runSet(7, 0.1, 0.6);
//		e.runSet(10,0.1, 0.6);
//		e = new CopyOfExperiment_Faults("FaultType\\Noisy\\Median_Estimator\\", "FaultType\\Noisy\\", "Noisy");
//		e.runSet(5, 0.1, 0.6);
//		e.runSet(7, 0.1, 0.6);
//		e.runSet(10,0.1, 0.6);
		
		//Event Detection=================================================
		CopyOfExperiment_Events e;
		e = new CopyOfExperiment_Events("EventChange\\Old_Diffusive_Event\\MER\\", "EventChange\\Old_Diffusive_Event\\");
		for(double i = 0.30 ; i < 0.41 ; i += 0.1){
//			e.runSet(15, 0.001, 0.005, i);
			e.runSet(10, 0.001, 0.005, i);
		}	
	}
	

}
