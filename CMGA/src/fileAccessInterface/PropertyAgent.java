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
	private Properties MGAprop = new Properties();

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
		loadProperties(MGAprop, "MGA.properties");
	}

	public String getProperties(String filename, String key) {
		if (filename.equals("MGA"))
			return MGAprop.getProperty(key, null);
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
		if (filename.equals("MGA")) {
			MGAprop.setProperty(key, value);
		} else {
			logger.warn("Property Agent Null File Warning");
			ret = false;
		}
		return ret;
	}

	public void listProperties(String filename) {
		if (filename.equals("MGA"))
			MGAprop.list(System.out);
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
