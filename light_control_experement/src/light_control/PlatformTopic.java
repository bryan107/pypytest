package light_control;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.command.ActiveMQTopic;

public enum PlatformTopic {
	RAW_DATA("ssh.RAW_DATA"), HCI_SR("ssh.HCI.SR"), HCI_TTS("ssh.HCI.TTS"), CONTEXT(
			"ssh.CONTEXT"), COMMAND("ssh.COMMAND"), DISPLAY(
			"ssh.COMMAND.DISPLAY"), ADMIN("ssh.ADMIN"), NULL("ssh.NULL");

	private final String topicName;

	private PlatformTopic(String name) {
		topicName = name;
	}

	public String toString() {
		return topicName;
	}

	/**
	 * ��摮葡頧�PlatformTopic���踝�靘�頛詨銝�RAW_DATA"摮葡嚗停�臭誑敺Platform.RAW_DAT
	 * A
	 * 
	 * @param 閬
	 *            ��latformTopic��銝�
	 * @return PlatformTopic
	 */
	public static PlatformTopic fromString(String key) {
		return table.get(key);
	}

	public static ActiveMQTopic toActiveMQTopic(PlatformTopic topic) {
		return new ActiveMQTopic(topic.toString());
	}

	private final static Map<String, PlatformTopic> table = new HashMap<String, PlatformTopic>();

	static {
		table.put("RAW_DATA", PlatformTopic.RAW_DATA);
		table.put("CONTEXT", PlatformTopic.CONTEXT);
		table.put("COMMAND", PlatformTopic.COMMAND);
		table.put("DISPLAY", PlatformTopic.DISPLAY);
		table.put("HCI.SR", PlatformTopic.HCI_SR);
		table.put("HCI.TTS", PlatformTopic.HCI_TTS);
		table.put("ADMIN", PlatformTopic.ADMIN);
	}
}
