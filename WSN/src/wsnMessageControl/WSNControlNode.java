package wsnMessageControl;

import java.io.IOException;

import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.UnsupportedCommOperationException;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import s2h.util.jms.JmsStub;
import wsnMessageControl.transform.PropertyAgent;

public class WSNControlNode {
	// ------------------------Global Variables-----------------------------
	private static WSNDriver MSN;
	private static WSNPlatformAdapter adapter;
	// FIXME Taroko: Remove JMS Code After Taroko was finished
	private static JmsStub stub;
	static Log logger = LogFactory.getLog(WSNControlNode.class);

	// FIXME Taroko
	private static TarokoEventManager tarokoEventManager;;
	
	// ---------------------------------------------------------------------

	public WSNControlNode() throws JMSException {
		createJmsStub();// FIXME Taroko: Remove JMS Code After Taroko was
		// finished
		tarokoEventManager = new TarokoEventManager(getStub());
	}

	private static void loadSettings() {
		adapter = new WSNPlatformAdapter(
				PropertyAgent.getInstance().getProperties("config", "MQAddress"));
		
		try {
			MSN = new WSNDriver(PropertyAgent.getInstance().getProperties(
					"config", "UARTPort"));
		} catch (NoSuchPortException e) {
			logger.error("No Such COM Port");
		} catch (PortInUseException e) {
			logger.error("The COM Port has been used");
		} catch (UnsupportedCommOperationException e) {
			logger.error("The COM Port is not Supported");
		}
		logger.info("WSN API Started");
	}

	public static void main(String[] arg) throws IOException, JMSException,
			InterruptedException {
		
		loadSettings();
		// FIXME Taroko
		createJmsStub();
		tarokoEventManager = new TarokoEventManager(getStub());
		while (true) {
		    
			// Dispatch Event from WSN Platform to Message Queue
			Event event = adapter.dispatchEvent(MSN.getData(), "ssh.RAW");
			// Taroko THL Project
			if(event == null){
			    continue;
			}
			if (logger.isInfoEnabled()) {
				logger.info(event);
			}
			// FIXME Taroko NullPointerException
			try
            {
			        tarokoEventManager.dispatchEvent(event);
            }
            catch (Exception e)
            {
                logger.error(e);
            }
			
			
			// Dispatch Command from Message Queue to WSN Platform
//			 MSN.sendData(adapter.dispatchCommand("ssh.COMMAND"));

		}
	}


	// FIXME Taroko: Remove JMS Code After Taroko was finished
	private static void createJmsStub() throws JMSException {
		if (getStub() != null) {
			try {
				getStub().close();
				setStub(null);
			} catch (Exception e) {
			}
		}

		if (getStub() == null) {
			setStub(new JmsStub(new ActiveMQConnectionFactory(
					"tcp://192.168.4.100:61616")));
			// "tcp://127.0.0.1:61616")));
		}
	}

	private static JmsStub getStub() {
		return stub;
	}

	private static void setStub(JmsStub newstub) {
		stub = newstub;
	}
}