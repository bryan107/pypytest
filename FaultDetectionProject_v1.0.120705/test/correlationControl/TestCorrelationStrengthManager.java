package correlationControl;

import faultDetection.correlationControl.CorrelationStrengthManager;
import junit.framework.TestCase;

public class TestCorrelationStrengthManager extends TestCase {

	public void testgetTCD(){
		CorrelationStrengthManager csmanager = new CorrelationStrengthManager(0.02, 0.8);
		System.out.println(csmanager.getTCD());
	}
}
