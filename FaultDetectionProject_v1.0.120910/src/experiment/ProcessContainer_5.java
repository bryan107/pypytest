package experiment;

import experiment_cores.CopyOfExperiment_Events;
import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_Faults;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//EVENT TEST==================================================================
		
		CopyOfExperiment_Events e;
		e = new CopyOfExperiment_Events("EventChange\\Old_Diffusive_Event\\MER\\", "EventChange\\Old_Diffusive_Event\\");
		for(double i = 0.50 ; i < 0.51 ; i += 0.1){
//			e.runSet(20, 0.001, 0.005, i);
			e.runSet(10, 0.001, 0.005, i);
		}	
		
	}
	

}
