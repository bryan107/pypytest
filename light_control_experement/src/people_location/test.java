package people_location;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class test {

	/**
	 * @param args
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws JMSException {
		JmsStub stub = new JmsStub(new ActiveMQConnectionFactory(
		"tcp://192.168.4.100:61616"));
		stub.send(JsonUtils.createBuilder().add("peoplelocation", "OUT").toJson(), "ssh.CONTEXT");
		System.out.println("SUCCESS");
		
	}

}
