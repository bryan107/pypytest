package dataGenerator;


import junit.framework.TestCase;

public class DeployMapTest extends TestCase {

	public void test(){
		DeployMap.getInstance().addSensor(1, 0, 0);
		DeployMap.getInstance().addSensor(2, 0, 4);
		DeployMap.getInstance().addSensor(3, 8, 8);
		DeployMap.getInstance().addSensor(4, 12, 20);
		DeployMap.getInstance().addEventSource(1, 0, 8);
		for(int i = 1 ; i < 5 ; i++){
			System.out.println("Node[" + i + "] : " + DeployMap.getInstance().getDistance(i, 1));
		}
		
		
	}
}
