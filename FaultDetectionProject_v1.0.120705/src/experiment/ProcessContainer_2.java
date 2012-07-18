package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TCD TEST
//		Experiment_TCD et = new Experiment_TCD("TCD\\QE_Estimator\\", "TCD\\");
//		et.runSet(15, 0.005, 0.05, 0.005);
//		et.runSet(15, 0.025, 0.085, 0.01);
//		et.runSet(15, 0.075, 0.14, 0.02);
//		et.runSet(15, 0.12, 0.19, 0.03);
		
		// Fault Detection
		Experiment_Faults e = new Experiment_Faults("FaultType\\Deviation\\Least_Square_Estimator_0.20\\", "FaultType\\Deviation\\", "Deviation");
		e.runSet(15, 0.1, 0.6);
		e = new Experiment_Faults("FaultType\\Stuck\\Least_Square_Estimator_0.20\\", "FaultType\\Stuck\\", "Stuck");
		e.runSet(15, 0.1, 0.6);
		e = new Experiment_Faults("FaultType\\Noisy\\Least_Square_Estimator_0.20\\", "FaultType\\Noisy\\", "Noisy");
		e.runSet(15, 0.1, 0.6);
				
		//Event Detection
//		Experiment_Events e = new Experiment_Events("EventChange\\QE_Estimator\\", "EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(15, 0.001, 0.01, i);
//		}	
	}
	

}
