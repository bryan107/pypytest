package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_6 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Experiment_TCD e = new Experiment_TCD("TCD\\");
//		e.runSet(15, 0.195, 0.20, 0.03);
		e.runSet(20, 0.075, 0.095, 0.02);
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(20, 0.007, 0.010, i);
//		}	
	}
	

}
