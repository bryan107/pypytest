package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Experiment_Faults e = new Experiment_Faults("FaultType\\Deviation\\Median_Estimator\\", "FaultType\\Deviation\\", "Deviation");
		e.runSet(15, 0.1, 0.6);
		
		e = new Experiment_Faults("FaultType\\Stuck\\Median_Estimator\\", "FaultType\\Stuck\\", "Stuck");
		e.runSet(15, 0.1, 0.6);
		
//		e.runSet(15, 0.025, 0.035, 0.01);
//		
//		e.runSet(15, 0.075, 0.095, 0.02);


		
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(15, 0.001, 0.005, i);
//		}	
	}
	

}
