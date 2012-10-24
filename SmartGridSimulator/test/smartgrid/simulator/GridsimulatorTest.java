package smartgrid.simulator;

import junit.framework.TestCase;

public class GridsimulatorTest extends TestCase {

	public void testRun(){
		GridSimulator g = new GridSimulator("C:\\TEST\\simulation.txt", 200 ,30);
		g.run(365);
	}
	
	
}
