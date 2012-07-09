package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Experiment_TCD e = new Experiment_TCD("TCD\\");
		e.runSet(15, 0.145, 0.146, 0.03);
		e.runSet(20, 0.025, 0.035, 0.01);
		
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(20, 0.004, 0.006, i);
//		}	
	}
	

}
