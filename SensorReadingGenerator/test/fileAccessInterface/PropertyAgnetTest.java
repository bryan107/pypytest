package fileAccessInterface;

import junit.framework.TestCase;

public class PropertyAgnetTest extends TestCase {

	public void testGetProperties(){
		String reading = PropertyAgent.getInstance().getProperties("Event", "Pattern.Sections");
		System.out.println(reading);
		assertEquals("20", reading);
//		assertEquals("", actual)
	}
}
