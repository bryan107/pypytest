package light_control;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class Receiver implements MessageListener {

	JmsStub stub;
	int[] peopleLocation;
	public Receiver(JmsStub stub, int[] peopleLocation) {
		this.stub = stub;
		this.peopleLocation = peopleLocation;
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage t = (TextMessage) message;
			try {
				relocate(t.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	protected void relocate(String json) throws JMSException {
		String loc = JsonUtils.get(json, "peoplelocation");
		String[] locs = new String[]{"DOOR", "LIVINGROOM", "KITCHEN", "BEDROOM", "BEDROOM-2","OUT"};
		for (int i = 0; i < locs.length; i++) {
			peopleLocation[i] = locs[i].equals(loc) ? 1 : 0;
		}
	}

}


