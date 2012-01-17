package wsnMessageControl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class CommandTest extends TestCase {
	public void testtoWSNPlatformFormat(){
		Map<String, String> message = new HashMap<String, String>();
		message.put("type", "Beeper");
		message.put("attribute", "Switch");
		message.put("value", "On");
		message.put("location", "Bedroom");
		
		Command command = Command.decode(message);
		byte[] datapackage = command.toWSNPlatformFormat();
		
		System.out.print("Datapackage = ");
		for(int i = 0 ; i < datapackage.length ; i++){
			System.out.print(datapackage[i] + " ");
		}
		
		
	}
}
