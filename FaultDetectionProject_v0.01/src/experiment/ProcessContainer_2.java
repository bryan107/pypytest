package experiment;

import experiment_cores.Experiment_Events;
import experiment_cores.Experiment_TCD;

public class ProcessContainer_2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Experiment_TCD e = new Experiment_TCD("TCD\\");
		e.runSet(15, 0.015, 0.05, 0.005);
		
		e.runSet(15, 0.04, 0.10, 0.01);
		
		e.runSet(15, 0.10, 0.15, 0.02);


		
		
//		Experiment_Events e = new Experiment_Events("EventChange\\");
//		for(double i = 0.03 ; i < 0.1 ; i += 0.03){
//			e.runSet(15, 0.001, 0.005, i);
//		}	
	}
	

}
