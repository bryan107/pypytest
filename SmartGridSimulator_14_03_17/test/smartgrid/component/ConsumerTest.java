package smartgrid.component;

import fileAccessInterface.PropertyAgentT;
import junit.framework.TestCase;

public class ConsumerTest extends TestCase {


	public void testGetDemand(){
		
		Consumer c = new Consumer(100, new PatternStable(), 50, null, 0.1, new FaultNull());
		for(int i = 0 ; i < 200 ; i+=10){
			System.out.println("Round[" + i + "]: " + c.getDemand(365, i));
			
		}
		double impactvalue = Double.valueOf(PropertyAgentT.getInstance().getProperties("SET", "Consumer.Fault.ImpactValue"));
		Fault f = new FaultDeviationProportion(impactvalue);
		c.updateFault(f);
		System.out.println("Fault (" + f.name() + ") has been inserted");
		for(int i = 200 ; i < 365 ; i += 10){
			System.out.println("Round[" + i + "]: " + c.getDemand(365, i));
		}
		
	}
	
}
