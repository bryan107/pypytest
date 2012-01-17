package wsnMessageControl;

import javax.jms.JMSException;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class MessageSender {

	JmsStub stub;

	public MessageSender(JmsStub stub) {
		this.stub = stub;

	}

	public void send(String[] message ,String topic) throws JMSException {
		System.out.println(getStub());
		getStub().send(
				JsonUtils.createBuilder().add("id", message[0]).add(
						"attribute", message[1]).add("time", message[2]).add(
						"location", message[3]).add("value", message[4])
						.toJson(), topic);
	}

	private JmsStub getStub() {
		return stub;
	}

}
