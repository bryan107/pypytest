package mfdr.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class PropertyAgent {
	private Log logger = LogFactory.getLog(PropertyAgent.class);
	private File currentPath;
	private Properties prop = new Properties();
	
	/*
	 * THIS IS A SINGELTON OBJECT
	 */
	public static PropertyAgent self = new PropertyAgent("conf"); 

	// public PropertyAgent(Object ...args) {
	// for (Object object : args) {
	// System.out.println(object);
	// }
	// }
	//

	/*
	 * Constructor
	 */
	private PropertyAgent(String currentPath) {
		this.currentPath = new File(currentPath);
		this.currentPath.mkdirs();
		logger.info("Success Create Global Object " + this);
		loadProperties();
	}
	
	public static PropertyAgent getInstance(){
		return self;
	}

	private void loadProperties() {
		loadProperties(prop, "MDFR.properties");
	}

	/*
	 * Get property from FILENAME with a given KEY
	 */
	public String getProperties(String filename, String key) {
		if (filename.equals("MDFR") || filename.equals("mdfr"))
			return prop.getProperty(key, null);
		else {
			logger.warn("Property Agent Null File Warning");
			return null;
		}
	}

	/*
	 * Online Property file store function is currently disabled
	 */
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

	/*
	 * Online property store
	 */
	public boolean setProperties(String filename, String key, String value) {
		boolean ret = true;
		if (filename.equals("MDFR") || filename.equals("mdfr")) {
			prop.setProperty(key, value);
		} 
		else {
			logger.warn("Property Agent Null File Warning");
			ret = false;
		}
		return ret;
	}

	/*
	 * Lists the all the properties in the property file.
	 */
	public void listProperties(String filename) {
		if (filename.equals("MDFR") || filename.equals("mdfr"))
			prop.list(System.out);
		else {
			logger.warn("Property Agent Null File Warning");
		}
	}

	/*
	 * Load properties from the given file to prop object
	 */
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
