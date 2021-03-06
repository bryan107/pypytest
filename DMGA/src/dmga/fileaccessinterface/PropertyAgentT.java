package dmga.fileaccessinterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class PropertyAgentT {
	private Log logger = LogFactory.getLog(PropertyAgentT.class);
	private File currentPath;
	// private Properties sensorprop = new Properties();
	// private Properties locationprop = new Properties();
	// private Properties actuatorprop = new Properties();
	private Properties SETprop = new Properties();
	private Properties GADprop = new Properties();
	private Properties FDCprop = new Properties();
	public static PropertyAgentT self = new PropertyAgentT("conf");


	private PropertyAgentT(String currentPath) {
		this.currentPath = new File(currentPath);
		this.currentPath.mkdirs();

		logger.info("Success Create Global Object " + this);
		loadProperties();
	}
	
	public static PropertyAgentT getInstance(){
		return self;
	}
	

	private void loadProperties() {
		// loadProperties(sensorprop, "sensor.properties");
		// loadProperties(locationprop, "location.properties");
		// loadProperties(actuatorprop, "actuator.properties");
		loadProperties(SETprop, "SET.properties");
		loadProperties(GADprop, "GAD.properties");
		loadProperties(FDCprop, "FDCservice.properties");
	}

	public String getProperties(String filename, String key) {
		if (filename.equals("SET"))
			return SETprop.getProperty(key, null);
		else if (filename.equals("GAD") || filename.equals("gad"))
			return GADprop.getProperty(key, null);
		else if (filename.equals("FDC") || filename.equals("gad"))
			return FDCprop.getProperty(key, null);
		// else if (filename.equals("location") || filename.equals("Location"))
		// return locationprop.getProperty(key, null);
		// else if (filename.equals("config") || filename.equals("Config"))
		// return confprop.getProperty(key, null);
		else {
			logger.warn("Property Agent Null File Warning");
			return null;
		}
	}

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

	public boolean setProperties(String filename, String key, String value) {
		boolean ret = true;
		if (filename.equals("SET") || filename.equals("SET")) {
			SETprop.setProperty(key, value);
			// do save
			// } else if (filename.equals("actuator")) {
			// actuatorprop.setProperty(key, value);
			// // do save
			// } else if (filename.equals("location")) {
			// locationprop.setProperty(key, value);
			// ret = doStore(locationprop, "location.properties");
			// // do save
		} else if(filename.equals("GAD") || filename.equals("gad")){
			GADprop.setProperty(key, value);
		} else if(filename.equals("FDC") || filename.equals("fdc")){
			FDCprop.setProperty(key, value);
		}else {
			logger.warn("Property Agent Null File Warning");
			ret = false;
		}
		return ret;
	}

	public void listProperties(String filename) {
		if (filename.equals("SET") || filename.equals("SEt"))
			SETprop.list(System.out);
		else if (filename.equals("GAD") || filename.equals("gad"))
			GADprop.list(System.out);
		// else if (filename.equals("location") || filename.equals("Location"))
		// locationprop.list(System.out);
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
