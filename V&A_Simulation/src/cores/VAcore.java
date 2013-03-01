package cores;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fileAccessInterface.FileAccessAgent;

public class VAcore {
	private int count = 0;
	private final int datacount = 34945;
	private FileAccessAgent agent;
	private static Log logger = LogFactory.getLog(VAcore.class);

	public VAcore(String readaddress, String writeaddress) {
		setupFileAccess(readaddress, writeaddress);
	}

	public void setupFileAccess(String readaddress, String writeaddress) {
		agent = new FileAccessAgent(readaddress, writeaddress);
	}

	public void run() {

	}

	
}
