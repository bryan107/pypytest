package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Experiment_TCD e = new Experiment_TCD("");
		e.runSet(20, 0.035, 0.050, 0.005);
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(20, 0.001, 0.003, i);
//		}	
	}
	

}
