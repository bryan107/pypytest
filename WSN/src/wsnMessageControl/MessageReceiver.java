package wsnMessageControl;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class MessageReceiver implements MessageListener {

	JmsStub stub;
	Map<String, String> message;
	public MessageReceiver(JmsStub stub, Map<String, String> message) {
		this.stub = stub;
		this.message = message;
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage t = (TextMessage) message;
			try {
				setMessage(t.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	protected void setMessage(String json) throws JMSException {
		
		message.put("type", JsonUtils.get(json, "type")) ;
		message.put("attribute", JsonUtils.get(json, "attribute"));
		message.put("time", JsonUtils.get(json, "time"));
		message.put("location", JsonUtils.get(json, "location"));
		message.put("value", JsonUtils.get(json, "value"));
	}

}


