package smartgrid.simulator;

import junit.framework.TestCase;

public class GridsimulatorTest extends TestCase {

	public void testRun(){
		GridSimulator g = new GridSimulator("D:\\TEST\\.simulation.txt");
		g.run(365);
	}
	
}
