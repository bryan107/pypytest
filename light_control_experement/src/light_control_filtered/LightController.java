package light_control_filtered;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import s2h.util.jms.JmsStub;
import s2h.util.jms.annotation.OnTopic;
import s2h.util.json.JsonUtils;

public class LightController {

	static Log logger = LogFactory.getLog(LightController.class);
	Set<String> watchDogLog = new HashSet<String>();

	private JmsStub stub;
	int[] lightOnOff = new int[5];
	int[] peopleLocation = new int[6];

	public LightController() throws JMSException, InterruptedException {
		createJmsStub();
		lightControlInitial();
		createWatchDog();
	}

	void createJmsStub() throws JMSException {
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
			getStub().addTopicListener("ssh.CONTEXT",
					new Receiver(getStub(), peopleLocation));
			getStub().addTopicListener(this);
		}
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
							logger
									.info("jms receiver is not alive. try to create new one.");
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

	void mainLoop() throws NoSuchPortException, PortInUseException,
			UnsupportedCommOperationException, IOException, JMSException,
			InterruptedException {

		CommPortIdentifier identifier = CommPortIdentifier
				.getPortIdentifier("COM4");

		SerialPort port = (SerialPort) identifier.open("test", 10);
		port.setSerialPortParams(115200, 8, 1, SerialPort.PARITY_NONE);

		InputStream in = port.getInputStream();
		int data = -1;
		StringBuffer sb = new StringBuffer();

		while (true) {
			data = in.read();
			if (data == '\n') {
				lightControl(saveData(sb.toString()));
				sb.setLength(0);
				continue;
			}
			sb.append((char) data);
		}
	}

	public static void main(String[] args) throws NoSuchPortException,
			PortInUseException, UnsupportedCommOperationException, IOException,
			JMSException, InterruptedException {

		new LightController().mainLoop();
	}

	public void sendCOMMANDMessage(String cmd) throws JMSException,
			InterruptedException {
		getStub().send(JsonUtils.createBuilder().add("value", cmd).toJson(),
				"ssh.COMMAND");
		Thread.sleep(3000);
	}

	private void lightControlInitial() throws JMSException,
			InterruptedException {
		sendCOMMANDMessage("DOOR-LIGHT_OFF");
		lightOnOff[0] = 0;
		peopleLocation[0] = 0;
		System.out.println("Init: DOOR-LIGHT_OFF");
		
		sendCOMMANDMessage("LIVINGROOM-LIGHT_OFF");
		lightOnOff[1] = 0;
		peopleLocation[1] = 0;
		System.out.println("Init: LIVINGROOM-LIGHT_OFF");

		sendCOMMANDMessage("KITCHEN-LIGHT_OFF");
		sendCOMMANDMessage("GARDEN-LIGHT_OFF");
		lightOnOff[2] = 0;
		peopleLocation[2] = 0;
		System.out.println("Init: KITCHEN-LIGHT_OFF");
		
		sendCOMMANDMessage("BEDROOM-LIGHT_OFF");
		lightOnOff[3] = 0;
		peopleLocation[3] = 0;
		System.out.println("Init: BEDROOM-LIGHT_OFF");
		
		sendCOMMANDMessage("BEDROOM-2-LIGHT_OFF");
		lightOnOff[4] = 0;
		peopleLocation[4] = 0;
		System.out.println("Init: BEDROOM-2-LIGHT_OFF");
	}

	// @SuppressWarnings("deprecation")
	private void lightControl(int[] intData) throws JMSException,
			InterruptedException, IOException {

		switch (intData[0]) {
		case 19:
			conditionControl(intData, "DOOR-LIGHT_ON", "DOOR-LIGHT_OFF");
			break;
		case 20:
			conditionControl(intData, "LIVINGROOM-LIGHT_ON","LIVINGROOM-LIGHT_OFF");
			break;
		case 21:
			conditionControl(intData, "KITCHEN-LIGHT_ON", "KITCHEN-LIGHT_OFF" ,"GARDEN-LIGHT_ON", "GARDEN-LIGHT_OFF");
			break;
		case 22:
			conditionControl(intData, "BEDROOM-LIGHT_ON", "BEDROOM-LIGHT_OFF");
			break;
		case 23:
			conditionControl(intData, "BEDROOM-2-LIGHT_ON",
					"BEDROOM-2-LIGHT_OFF");
		default:
			break;
		}
	}

	private void conditionControl(int[] intData, String onCommand,
			String offCommand) throws JMSException, InterruptedException, IOException {
		if (peopleLocation[intData[0] - 19] == 1
				&& lightOnOff[intData[0] - 19] == 0 && intData[1] < 30000) {
			sendCOMMANDMessage(onCommand);
			lightOnOff[intData[0] - 19] = 1;
			System.out.println(onCommand);
			Thread.sleep(2000);
		}
		if (peopleLocation[intData[0] - 19] == 0
				&& lightOnOff[intData[0] - 19] == 1) {
			sendFiltered(offCommand, intData);
		}
	}
	private void conditionControl(int[] intData, String onCommand1,
			String offCommand1, String onCommand2,
			String offCommand2) throws JMSException, InterruptedException {
		if (peopleLocation[intData[0] - 19] == 1
				&& lightOnOff[intData[0] - 19] == 0 && intData[1] < 30000) {
			sendCOMMANDMessage(onCommand1);
			lightOnOff[intData[0] - 19] = 1;
			System.out.println(onCommand1);
			Thread.sleep(2000);
			sendCOMMANDMessage(onCommand2);
			lightOnOff[intData[0] - 19] = 1;
			System.out.println(onCommand2);
			Thread.sleep(2000);
		}
		if (peopleLocation[intData[0] - 19] == 0
				&& lightOnOff[intData[0] - 19] == 1) {
			sendFiltered(offCommand1, offCommand2, intData);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void sendFiltered(String offCommand, int[] intData) throws JMSException, InterruptedException, IOException{
		int hour = new Date().getHours();
		if(hour >= 18 && hour <22 )
			if(offCommand.equalsIgnoreCase("LIVINGROOM-LIGHT_OFF") || 
					offCommand.equalsIgnoreCase("KITCHEN-LIGHT_OFF") ||
					offCommand.equalsIgnoreCase("GARDEN-LIGHT_OFF")||
					offCommand.equalsIgnoreCase("DOOR-LIGHT_OFF")
			){
				FileWriter writer = new FileWriter(
						"D:\\Message Testing Data\\CommandFiltered.txt", true);
				System.out.println("Command " + offCommand + " filtered");
				writer.write(new Date().getTime() + offCommand + "\n");
				writer.close();
				return;
			}
				
		if(hour >= 22 && hour <= 23)
			if(offCommand.equalsIgnoreCase("BEDROOM-LIGHT_OFF") || 
					offCommand.equalsIgnoreCase("BEDROOM-2-LIGHT_OFF")
			){
				FileWriter writer = new FileWriter(
						"D:\\Message Testing Data\\CommandFiltered.txt", true);
				System.out.println("Command " + offCommand + " filtered");
				writer.write(new Date().getTime() + offCommand + "\n");
				writer.close();
				return;
			}
				
	    if(hour >=11 && hour <14)
			if( offCommand.equalsIgnoreCase("LIVINGROOM-LIGHT_OFF")){
				FileWriter writer = new FileWriter(
						"D:\\Message Testing Data\\CommandFiltered.txt", true);
				System.out.println("Command " + offCommand + " filtered");
				writer.write(new Date().getTime() + " " + offCommand + "\n");
				writer.close();
				return;
			}
				
		sendCOMMANDMessage(offCommand);
		lightOnOff[intData[0] - 19] = 0;
		System.out.println(offCommand);
		Thread.sleep(2000);
	}
	
	@SuppressWarnings("deprecation")
	private void sendFiltered(String offCommand1, String offCommand2 ,int[] intData) throws JMSException, InterruptedException{
		int hour = new Date().getHours();
		if(hour >= 18 && hour <22 )
			if( offCommand1.equalsIgnoreCase("KITCHEN-LIGHT_OFF") ||
					offCommand2.equalsIgnoreCase("GARDEN-LIGHT_OFF")
			)
				return;
		sendCOMMANDMessage(offCommand1);
		lightOnOff[intData[0] - 19] = 0;
		System.out.println(offCommand1);
		Thread.sleep(2000);
		sendCOMMANDMessage(offCommand2);
		lightOnOff[intData[0] - 19] = 0;
		System.out.println(offCommand2);
		Thread.sleep(2000);
	}
	
	
	
	private static int[] saveData(String input) {
		String[] rawData = input.split("\\s+");
		int[] intData = new int[2];
		if (rawData.length == 5) {
			try {
				String stripLight0x = rawData[rawData.length - 1].replace("0x",
						"");
				String stripAdd0x = rawData[0].split("=")[1].replace("0x", "");
				intData[1] = Integer.parseInt(stripLight0x, 16);
				intData[0] = Integer.parseInt(stripAdd0x, 10);
				FileWriter writer = new FileWriter(
						"D:\\Message Testing Data\\Light_data[" + intData[0] + "].txt", true);
				System.out.println(intData[0] + " " + intData[1]);
				writer.write(new Date().getTime() + " " + intData[0] + " "
						+ intData[1] + "\n");
				writer.close();
				return intData;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new int[0];
	}

	void setStub(JmsStub stub) {
		this.stub = stub;
	}

	JmsStub getStub() {
		return stub;
	}
}
