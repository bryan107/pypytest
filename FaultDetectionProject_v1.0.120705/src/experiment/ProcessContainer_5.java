package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TCD TEST
		Experiment_TCD et = new Experiment_TCD("TCD\\QE_Estimator\\", "TCD\\");
//		et.runSet(5, 0.005, 0.05, 0.005);
//		et.runSet(5, 0.025, 0.085, 0.01);
		et.runSet(5, 0.075, 0.14, 0.02);
//		et.runSet(5, 0.12, 0.19, 0.03);
//		et.runSet(7, 0.005, 0.05, 0.005);
//		et.runSet(7, 0.025, 0.085, 0.01);
		et.runSet(7, 0.075, 0.14, 0.02);
//		et.runSet(7, 0.12, 0.19, 0.03);
		
		// Fault Detection
//		Experiment_Faults e = new Experiment_Faults("FaultType\\Deviation\\Median_Estimator\\", "FaultType\\Deviation\\", "Deviation");
//		e.runSet(5, 0.1, 0.6);
//		e.runSet(7, 0.1, 0.6);
//		e.runSet(10,0.1, 0.6);
		
		
//		Experiment_TCD e = new Experiment_TCD("TCD\\");
//		e.runSet(15, 0.145, 0.146, 0.03);
//		e.runSet(20, 0.025, 0.035, 0.01);
		
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(20, 0.004, 0.006, i);
//		}	
	}
	

}
