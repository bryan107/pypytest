package fileAccess;

import fileAccessInterface.FileAccessAgent;
import junit.framework.TestCase;

public class FileAccessAgentTest extends TestCase {
	String fileaddress = "E:\\test.txt";
	FileAccessAgent agent = new FileAccessAgent(fileaddress, fileaddress);

	public void testFileWriting() {
		for (int i = 0; i < 100; i++) {
			agent.writeLineToFile("[" + i + "] test success");
		}
	}

	public void testFileReading() {
		if ((agent.setFileReader()) == true) {
			for (int i = 0; i < 100; i++) {
				System.out.println(agent.readLineFromFile());
			}
		}
		agent.closeFileReader();

	}
}
