package wsnMessageControl;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wsnMessageControl.transform.Transformer;

public class Command {
	private static Log logger = LogFactory.getLog(Command.class);
	//
	private String type;
	private String attribute;
	private String value;
	private String location;

	// private String time;

	protected Command(String type, String attribute, String value,
			String location) {
		super();
		this.type = type;
		this.attribute = attribute;
		this.value = value;
		this.location = location;

	}

	public static Command decode(Map<String, String> message) {
		String actuatorType = message.get("type");
		String attribute = message.get("attribute");
		String value = message.get("value");
		String location = message.get("location");

		return new Command(actuatorType, attribute, value, location);
	}

	public byte[] toWSNPlatformFormat() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int[] id = getID();
		int[] attribute = getAttribute();
		int[] value = getValue();
		for (int i = 0; i < id.length; i++) {
			intToByteArrayOutputStream(out, id[i]);
			intToByteArrayOutputStream(out, attribute[0]);
			intToByteArrayOutputStream(out, value[0]);
		}
		return out.toByteArray();
	}

	public int[] getID() {
		return Transformer.getInstance().getActuatorID(type, location);
	}

	public int[] getAttribute() {
		return Transformer.getInstance().getActuatorAttribute(type, attribute);
	}

	public int[] getValue() {
		return Transformer.getInstance().getActuatorValue(type, attribute,
				value);
	}

	private void intToByteArrayOutputStream(ByteArrayOutputStream data, int temp) {
		if (temp < (2 << 7)) {
			data.write(1);
			data.write(temp);
		} else if (temp < (2 << 15)) {
			data.write(2);
			data.write(temp / (2 << 7));
			data.write(temp % (2 << 7));
		} else if (temp < (2 << 23)) {
			data.write(3);
			data.write(temp / (2 << 15));
			data.write(temp / (2 << 7));
			data.write(temp % (2 << 7));
		} else if (temp < (2 << 31)) {
			data.write(4);
			data.write(temp / (2 << 23));
			data.write(temp / (2 << 15));
			data.write(temp / (2 << 7));
			data.write(temp % (2 << 7));
		} else
			logger.error("Message to Data Translation error");
	}

}
