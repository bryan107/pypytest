import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

import s2h.util.jms.JmsStub;

public class MessageAnalysis {
	public static void main(String[] args) {
		try {
			JmsStub stub = new JmsStub(new ActiveMQConnectionFactory(
					"tcp://192.168.4.100:61616"));

			for (PlatformTopic p : PlatformTopic.values()) {
				System.out.println(p.toString());
				stub.addTopicListener(p.toString(), new Receiver(stub));
			}
//			
//			if(true){
//				stub.close();
//				System.exit(0);
//				
//			}
//
//			stub.send("{\"value\":\"DOOR-LIGHT_ON\"}", "ssh.COMMAND");
//			stub.send("{\"value\":\"DOOR-LIGHT_OFF\"}", "ssh.CONTEXT");
//
//			stub.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
}
