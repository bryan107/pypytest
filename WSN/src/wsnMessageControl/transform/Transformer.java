package wsnMessageControl.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public final class Transformer {

	private static Log logger = LogFactory.getLog(Transformer.class);
	private IntArrayOutputStream outint = new IntArrayOutputStream();

	private static Transformer self = new Transformer();

	private Transformer() {
		logger.info("Success Create Global Object " + this);
	}

	public static Transformer getInstance() {
		return self;
	}

	public int[] getActuatorID(String type, String location) {
		outint.reset();
		int[] idtemp;
		String ids = PropertyAgent.getInstance().getProperties("actuator", type);
		for (int i = 0; i < ids.length();) {
			for(int j = 1 ;;) {
				if (ids.substring(i + j - 1, i + j).equals(",")) {
					outint.write(Integer.valueOf(ids.substring(i, i+ j -1)));
					i =i + j;
					break;
				}
				else if((i+j) >= ids.length()){
					outint.write(Integer.valueOf(ids.substring(i, i + j)));
					i = i + j;
					break;
				}
				else if (ids.substring(i + j -1, i + j).equals(null)) {
					logger.warn("Actuator.property id null error");
				} else {
					j++;
				}
			}
		}
		if (location.equals(null) || location.equals("Global")  || location.equals("Null") ){
			return outint.toIntArray();
		}	
		else {
			idtemp = outint.toIntArray();
			outint.reset();
			for (int i = 0; i < idtemp.length; i++) {
				if (getLocation(idtemp[i]).equals(location)) {
					outint.write(idtemp[i]);
				}

			}
			return outint.toIntArray();
		}
	}

	public int[] getActuatorAttribute(String type, String attribute) {
		outint.reset();
		String content = PropertyAgent.getInstance()
				.getProperties("actuator", type + "_" + attribute);
		if (content == null) {
			logger.warn("Actuator Type: " + type + "Attribute Null Warning");
			return null;
		}

		else {
			outint.write(Integer.valueOf(content));
			return outint.toIntArray();
		}

	}

	public int[] getActuatorValue(String type, String attribute, String value) {
		outint.reset();
		String content = PropertyAgent.getInstance().getProperties("actuator", type + "_" + attribute
				+ "_" + value);
		if (content == null) {
			logger.warn("Actuator Type: " + type + "Value Null Warning");
			return null;
		} else {
			outint.write(Integer.valueOf(content));
			return outint.toIntArray();
		}

	}

	public String getSensorAttribute(long attributeid) {
		String schema = PropertyAgent.getInstance().getProperties("sensor", "" + attributeid);
		logger.info("Schema = " + schema);
		if ("".equals(schema) || schema == null) {
			logger.warn("schema id[" + attributeid
					+ "] cannot look up the schema key");
			return "Null";
		}
		return schema;
	}

	public String getSensorValue(String attribute, double valuedata) {
		StringBuffer sb = new StringBuffer();
		String expression = PropertyAgent.getInstance().getProperties("sensor", attribute);
		String rawvalue = Long.toString((long) valuedata);
		for (int i = 0; i < expression.length(); i++) {
			if (expression.substring(i, i + 1).equals("x")) {
				sb.append(rawvalue);
			} else {
				sb.append(expression.substring(i, i + 1));
			}
		}
		return Double.toString(Calculator.getInstance()
				.infixCalc(sb.toString()));
	}


	public String getLocation(long id) {
		String location = PropertyAgent.getInstance().getProperties("location","" + id);
		if ("".equals(location) || location == null) {
			logger.warn("Device id[" + id
					+ "] can not loop for the location info");
			return "Global";
		}
		return location;
	}

}
