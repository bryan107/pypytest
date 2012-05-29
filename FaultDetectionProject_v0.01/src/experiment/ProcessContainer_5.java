package experiment;

import experiment_cores.Experiment_Events;

public class ProcessContainer_5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Experiment_Events e = new Experiment_Events("EventChange\\");
		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
			e.runSet(20, 0.004, 0.006, i);
		}	
	}
	

}
