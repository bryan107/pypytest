package dataGenerator;

import java.text.DecimalFormat;

import fileAccessInterface.FileAccessAgent;
import fileAccessInterface.PropertyAgent;
import junit.framework.TestCase;

public class EventTest extends TestCase {
	EventSource e = new EventSource(1);
	FileAccessAgent f = new FileAccessAgent("E:\\test.txt", "E:\\test.txt");
	public void testEvent(){
		long sections = Long.valueOf(PropertyAgent.getInstance().getProperties("Event", "Event1.Pattern.Sections"));
		
		for(int sectionnumber = 0 ; sectionnumber < sections + 1; sectionnumber++){
			DecimalFormat d = new DecimalFormat("00.000");
			System.out.println("Complete" + d.format(((double)sectionnumber/sections)*100 )+ "%");
			f.writeLineToFile(d.format(e.value(sectionnumber)) + "");
		}
	}
	
}
