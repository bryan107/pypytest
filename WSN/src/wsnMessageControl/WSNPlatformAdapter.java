package wsnMessageControl;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import s2h.util.jms.JmsStub;
import s2h.util.jms.annotation.OnTopic;


public class WSNPlatformAdapter {

	private JmsStub stub;
	private String jmsaddress;
	static Log logger = LogFactory.getLog(WSNPlatformAdapter.class);
	Set<String> watchDogLog = new HashSet<String>();

	public WSNPlatformAdapter(String jmsaddress) {
		this.jmsaddress = jmsaddress;
		try {
			createJmsStub();
			
		} catch (JMSException e) {
			logger.info("JMS Initiating Error");
		}
//		FIXME MQ WatchDog Error
//		createWatchDog(); //JMS Connection Watch Dog
	}

	// WSN package array to MOM message array
	public Event dispatchEvent(byte[] data, String topic) {
		Event event = Event.decode(data);
		if (event == null) {
			return null;
		}
		
		try {
			getStub().send(event.toJsonFormat(), topic);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}	
		return event;
	}

	public byte[] dispatchCommand(String topic) { 
		Command command = Command.decode(readJmsMessage(topic));
		return command.toWSNPlatformFormat();
	}
	protected Map<String, String> readJmsMessage(String topic) {
		Map<String, String> message = new HashMap<String, String>();
		getStub().addTopicListener(topic,
				new MessageReceiver(getStub(), message));
		return message;
	}
	// JMS Message
//	protected void sendJmsMessage(String[] message, String topic)
//			throws JMSException, InterruptedException {
//		MessageSender sender = new MessageSender(getStub());
//		sender.send(message, topic);
//	}



	// JMS
	private void createJmsStub() throws JMSException {
		if (getStub() != null) {
			try {
				getStub().close();
				setStub(null);
			} catch (Exception e) {
			}
		}

		if (getStub() == null) {
			setStub(new JmsStub(new ActiveMQConnectionFactory(jmsaddress)));
		}
	}

	private JmsStub getStub() {
		return stub;
	}

	private void setStub(JmsStub newstub) {
		stub = newstub;
	}
	
	@OnTopic("__check__")
	public void checkMessage(TextMessage message) {
		try {
			watchDogLog.add(message.getText());
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void createWatchDog() {
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						String token = "token_" + System.currentTimeMillis();
						try {
							getStub().send(token, "__check__");
						} catch (Exception e) {
						}
						Thread.sleep(3000);

						if (!watchDogLog.contains(token)) {
							logger.info("jms receiver is not alive. try to create new one.");
							createJmsStub();
						} else {
							logger.info("jms receiver is alive.");
						}

						if (watchDogLog.size() > 100) {
							watchDogLog.clear();
						}

					} catch (JMSException e) {
						logger.error(e.getMessage(), e);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		};
		t.start();
	}

}