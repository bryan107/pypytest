package fileAccessInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class PropertyAgent {
	private Log logger = LogFactory.getLog(PropertyAgent.class);
	private File currentPath;
	private Properties GADprop = new Properties();
	private Properties EWMAprop = new Properties();

	// public PropertyAgent(Object ...args) {
	// for (Object object : args) {
	// System.out.println(object);
	// }
	// }
	//

	public PropertyAgent(String currentPath) {
		this.currentPath = new File(currentPath);
		this.currentPath.mkdirs();

		logger.info("Success Create Global Object " + this);
		loadProperties();
	}

	private void loadProperties() {
		loadProperties(GADprop, "GAD.properties");
		loadProperties(EWMAprop, "EWMA.properties");
	}

	public String getProperties(String filename, String key) {
		if (filename.equals("GAD"))
			return GADprop.getProperty(key, null);
		else if (filename.equals("EWMA"))
			return EWMAprop.getProperty(key, null);
		else {
			logger.warn("Property Agent Null File Warning");
			return null;
		}
	}
/*
	private boolean doStore(Properties prop, String file) {
		synchronized (prop) {
			boolean ret = false;
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(new File(this.currentPath, file));
				prop.store(out, "store " + file + " at " + new Date());
				ret = true;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (out != null) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
					out = null;
				}
			}
			return ret;
		}
	}
*/
	public boolean setProperties(String filename, String key, String value) {
		boolean ret = true;
		if (filename.equals("GAD")) {
			GADprop.setProperty(key, value);
		} 
		else if (filename.equals("EWMA")){
			EWMAprop.setProperty(key, value);
		}
		else {
			logger.warn("Property Agent Null File Warning");
			ret = false;
		}
		return ret;
	}

	public void listProperties(String filename) {
		if (filename.equals("GAD"))
			GADprop.list(System.out);
		else if (filename.equals("EWMA"))
			EWMAprop.list(System.out);
		else {
			logger.warn("Property Agent Null File Warning");
		}
	}

	private boolean loadProperties(Properties prop, String file) {
		FileInputStream in = null;
		boolean ret = false;
		try {
			in = new FileInputStream(new File(currentPath, file));
			prop.load(in);
			ret = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				in = null;
			}
		}
		return ret;
	}
}
