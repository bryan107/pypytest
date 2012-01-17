package wsnMessageControl.archive;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wsnMessageControl.Event;
import wsnMessageControl.transform.Transformer;

public class DataTranslator {

	private static final Log logger = LogFactory.getLog(DataTranslator.class);

	public DataTranslator() {

	}

	public byte[] messageToWsnTranslate(String[] message) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();

		int[] id;
		int[] attribute;
		int[] value;

		// TODO TEST: Actuator Process
		id = Transformer.getInstance().getActuatorID(message[0], message[3]);
		attribute = Transformer.getInstance().getActuatorAttribute(message[0],
				message[1]);
		value = Transformer.getInstance().getActuatorValue(message[0],
				message[1], message[4]);

		for (int i = 0; i < id.length; i++) {
			// Write ID
			int temp = id[i];
			intToByteArrayOutputStream(data, temp);
			// Write Attribute
			intToByteArrayOutputStream(data, attribute[0]);
			// Write Length
			intToByteArrayOutputStream(data, value[0]);
		}

		return data.toByteArray();

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

	public Event wnsToMessageTranslate(final byte[] data) {
		return Event.decode(data);
		
	}
}