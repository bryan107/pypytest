package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_6 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Experiment_Faults e = new Experiment_Faults("FaultType\\Stuck\\Median_Estimator\\", "FaultType\\Stuck\\", "Stuck");
		e.runSet(5, 0.1, 0.6);
		e.runSet(7, 0.1, 0.6);
		e.runSet(10,0.1, 0.6);
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(20, 0.007, 0.010, i);
//		}	
	}
	

}
