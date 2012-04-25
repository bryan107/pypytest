package dataGenerator;

import junit.framework.TestCase;

public class SensorTest extends TestCase {

	public void testGetValue(){
//		EventSource e = new EventSource("Event1");
//		
//		double [][] eventpack= new double[1][3];
//		System.out.println("AHA: " + e.value(4678));
//		eventpack[0][0] = e.value(4678);
//		eventpack[0][1] = e.diffusion();
//		eventpack[0][2] = e.constant();
//		for(int i = 0 ; i < eventpack[0].length ; i++){
//			System.out.print("TEST:" + eventpack[0][i]  + "");
//		}
//		System.out.println();
//		Sensor s1 = new Sensor(1, 0.05);
//		System.out.println(s1.getValue(eventpack));
		Long[] location = {(long) 222,(long) 333};
		System.out.println(location[0] + " " + location[1]);
		
	}
}
