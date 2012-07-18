package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;

public class ProcessContainer_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		
		Experiment_Faults e = new Experiment_Faults("FaultType\\Noisy\\Least_Square_Estimator_0.20\\", "FaultType\\Noisy\\", "Noisy");
		e.runSet(5, 0.1, 0.6);
		e.runSet(7, 0.1, 0.6);
		e.runSet(10,0.1, 0.6);
		
//		Experiment_Faults e = new Experiment_Faults("FaultType\\Noisy\\QE_Estimator\\", "FaultType\\Noisy\\", "Noisy");
//		e.runSet(5, 0.1, 0.6);
//		e.runSet(7, 0.1, 0.6);
//		e.runSet(10,0.1, 0.6);
//		
////		e.runSet(5, 0.025, 0.035, 0.01);
////		e.runSet(7, 0.025, 0.035, 0.01);
////		e.runSet(10, 0.025, 0.035, 0.01);
////		
////		e.runSet(5, 0.075, 0.095, 0.02);
////		e.runSet(7, 0.075, 0.095, 0.02);
////		e.runSet(10, 0.075, 0.095, 0.02);
////		
////		e.runSet(5, 0.12, 0.145, 0.03);
////		e.runSet(7, 0.12, 0.145, 0.03);
////		e.runSet(10, 0.12, 0.145, 0.03);
		
		
		//Event Detection
//		Experiment_Events e = new Experiment_Events("EventChange\\QE_Estimator\\", "EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(5, 0.001, 0.01, i);
//		}	
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(5, 0.001, 0.01, i);
//			e.runSet(7, 0.001, 0.01, i);
//			e.runSet(10, 0.001, 0.01, i);
//		}	
	}
	

}
