package light_control_filtered;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import people_location.HumanBehavior;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class Test {
	
	static JmsStub stub;
	static Log logger = LogFactory.getLog(HumanBehavior.class);
	
	protected static JmsStub getSender() {
		if (stub == null) {
			try {
				logger.info("create stub: " + stub);
				stub = new JmsStub(new ActiveMQConnectionFactory(
						"tcp://192.168.4.100:61616"));
			} catch (JMSException e) {
				logger.error(e.getMessage(), e);
			}
		}

		try {
			stub.send("foo", "NULL");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.info("renew stub");
			stub = null;
			return getSender();
		}
		return stub;
	}
	
	public static void main(String[] args) throws NoSuchPortException,
			PortInUseException, UnsupportedCommOperationException, IOException, JMSException, InterruptedException {
		
		CommPortIdentifier identifier = CommPortIdentifier
				.getPortIdentifier("COM4");

		SerialPort port = (SerialPort) identifier.open("test", 10);
		port.setSerialPortParams(115200, 8, 1, SerialPort.PARITY_NONE);

		InputStream in = port.getInputStream();
		int data = -1;
		StringBuffer sb = new StringBuffer();
		
		int[] lightOnOff = new int[5];
		int[] peopleLocation = new int[6];
		lightControlInitial(lightOnOff, peopleLocation);
		stub.addTopicListener("ssh.CONTEXT", new Receiver(stub,peopleLocation));
		while (true) {
			
			data = in.read();
			if (data == '\n') {
				lightControl(saveData(sb.toString()),lightOnOff,stub, peopleLocation);
				sb.setLength(0);
				continue;
			}
			sb.append((char) data);
		}

	}
	
	private static void lightControlInitial(int[] lightOnOff, int[] peopleLocation)
			throws JMSException, InterruptedException {
		Test.getSender().send(JsonUtils.createBuilder().add("value", "DOOR-LIGHT_OFF").toJson(),"ssh.COMMAND");
		lightOnOff[0] = 0;
		peopleLocation[0] = 0;
		System.out.println("Init: DOOR-LIGHT_OFF");
		Thread.sleep(4000);
		Test.getSender().send(JsonUtils.createBuilder().add("value", "LIVINGROOM-LIGHT_OFF").toJson(), "ssh.COMMAND");
		lightOnOff[1] = 0;
		peopleLocation[1] = 0;
		System.out.println("Init: LIVINGROOM-LIGHT_OFF");
		Thread.sleep(4000);
		Test.getSender().send(JsonUtils.createBuilder().add("value", "KITCHEN-LIGHT_OFF").toJson(), "ssh.COMMAND");
		lightOnOff[2] = 0;
		peopleLocation[2] = 0;
		System.out.println("Init: KITCHEN-LIGHT_OFF");
		Thread.sleep(4000);
		Test.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM-LIGHT_OFF").toJson(), "ssh.COMMAND");
		lightOnOff[3] = 0;
		peopleLocation[3] = 0;
		System.out.println("Init: BEDROOM-LIGHT_OFF");
		Thread.sleep(4000);
		Test.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM-2-LIGHT_OFF").toJson(), "ssh.COMMAND");
		lightOnOff[4] = 0;
		peopleLocation[4] = 0;
		System.out.println("Init: BEDROOM-2-LIGHT_OFF");
		Thread.sleep(4000);
		Test.getSender().send(JsonUtils.createBuilder().add("value", "GARDEN-LIGHT_OFF").toJson(), "ssh.COMMAND");
		System.out.println("Init: GARDEN-LIGHT_OFF");
		Thread.sleep(4000);
		Test.getSender().send(JsonUtils.createBuilder().add("value", "CENTER-LIGHT_OFF").toJson(), "ssh.COMMAND");
		System.out.println("Init: CENTER-LIGHT_OFF");
		Thread.sleep(4000);
	}
	
	//@SuppressWarnings("deprecation")
	private static void lightControl(int[] intData , int[] lightOnOff,JmsStub stub, int[] peopleLocation) throws JMSException, InterruptedException{
		/*int time = new Date().getHours();
		boolean timecondition1 = (time > 6 && time < 9);
		if( !(time > 12) && !timecondition1){
			return ;
		}*/
		switch(intData[0]){
		case 19:
			conditionControl(intData, lightOnOff, stub, peopleLocation, "DOOR-LIGHT_ON", "DOOR-LIGHT_OFF");		
			break;
		case 20:
			conditionControl(intData, lightOnOff, stub, peopleLocation, "LIVINGROOM-LIGHT_ON", "LIVINGROOM-LIGHT_OFF");
			break;
		case 21:
			conditionControl(intData, lightOnOff, stub, peopleLocation, "KITCHEN-LIGHT_ON", "KITCHEN-LIGHT_OFF", "GARDEN-LIGHT_ON", "GARDEN-LIGHT_OFF");
			break;
		case 22:
			conditionControl(intData, lightOnOff, stub, peopleLocation, "BEDROOM-LIGHT_ON", "BEDROOM-LIGHT_OFF");
			break;
		case 23:
			conditionControl(intData, lightOnOff, stub, peopleLocation, "BEDROOM-2-LIGHT_ON", "BEDROOM-2-LIGHT_OFF");
		default:
			break;
		}
	}

	private static void conditionControl(int[] intData, int[] lightOnOff,
			JmsStub stub, int[] peopleLocation, String message1, String message2) throws JMSException, InterruptedException {
		if(peopleLocation[intData[0]-19] == 1 && lightOnOff[intData[0]-19] == 0 && intData[1] < 30000){
			Test.getSender().send(JsonUtils.createBuilder().add("value", message1).toJson(), "ssh.COMMAND");
			lightOnOff[intData[0]-19] = 1;
			System.out.println(message1);	
			Thread.sleep(2000);
		}
		else if(peopleLocation[intData[0]-19] == 0 && lightOnOff[intData[0]-19] == 1){
			sendFiltered(stub,  lightOnOff,  message2, intData);
		}
	}
	
	private static void conditionControl(int[] intData, int[] lightOnOff,
			JmsStub stub, int[] peopleLocation, String message1, String message2, String message3, String message4) throws JMSException, InterruptedException {
		if(peopleLocation[intData[0]-19] == 1 && lightOnOff[intData[0]-19] == 0 && intData[1] < 30000){
			Test.getSender().send(JsonUtils.createBuilder().add("value", message1).toJson(), "ssh.COMMAND");
			lightOnOff[intData[0]-19] = 1;
			System.out.println(message1);	
			Thread.sleep(2000);
			Test.getSender().send(JsonUtils.createBuilder().add("value", message3).toJson(), "ssh.COMMAND");
			lightOnOff[intData[0]-19] = 1;
			System.out.println(message3);	
			Thread.sleep(2000);
		}
		else if(peopleLocation[intData[0]-19] == 0 && lightOnOff[intData[0]-19] == 1){
			sendFiltered(stub,  lightOnOff,  message2, message4,intData);
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void sendFiltered(JmsStub stub, int[] lightOnOff, String message2, int[] intData) throws JMSException, InterruptedException{
		int hour = new Date().getHours();
		if(hour >= 18 && hour <22 )
			if(message2.equalsIgnoreCase("LIVINGROOM-LIGHT_OFF") || 
			   message2.equalsIgnoreCase("KITCHEN-LIGHT_OFF") ||
			   message2.equalsIgnoreCase("GARDEN-LIGHT_OFF")||
			   message2.equalsIgnoreCase("DOOR-LIGHT_OFF")
			)
				return;
		if(hour >= 22 && hour <= 23)
			if(message2.equalsIgnoreCase("BEDROOM-LIGHT_OFF") || 
			   message2.equalsIgnoreCase("BEDROOM-2-LIGHT_OFF")
			)
				return;
	    if(hour >=11 && hour <14)
			if( message2.equalsIgnoreCase("LIVINGROOM-LIGHT_OFF"))
				return;
		Test.getSender().send(JsonUtils.createBuilder().add("value", message2).toJson(), "ssh.COMMAND");
		lightOnOff[intData[0]-19] = 1;
		System.out.println(message2);	
		Thread.sleep(2000);
	}
	
	@SuppressWarnings("deprecation")
	private static void sendFiltered(JmsStub stub, int[] lightOnOff, String message2, String message4 ,int[] intData) throws JMSException, InterruptedException{
		int hour = new Date().getHours();
		if(hour >= 18 && hour <22 )
			if( 
			   message2.equalsIgnoreCase("KITCHEN-LIGHT_OFF") ||
			   message4.equalsIgnoreCase("GARDEN-LIGHT_OFF")
			)
				return;
		Test.getSender().send(JsonUtils.createBuilder().add("value", message2).toJson(), "ssh.COMMAND");
		lightOnOff[intData[0]-19] = 0;
		System.out.println(message2);
		Thread.sleep(2000);
		Test.getSender().send(JsonUtils.createBuilder().add("value", message4).toJson(), "ssh.COMMAND");
		lightOnOff[intData[0]-19] = 0;
		System.out.println(message4);
		Thread.sleep(2000);
	}
	
	private static int[] saveData(String input) {
		String[] rawData = input.split("\\s+");
		int[] intData = new int[2]; 
		if (rawData.length == 5) {
			try {
				String stripLight0x = rawData[rawData.length - 1].replace("0x", "");
				String stripAdd0x = rawData[0].split("=")[1].replace("0x", "");
				intData[1] = Integer.parseInt(stripLight0x, 16);
				intData[0] = Integer.parseInt(stripAdd0x, 10);
				FileWriter writer = new FileWriter("D:\\Message Testing Data\\Light_data.txt", true);
				System.out.println(intData[0] + " " + intData[1]);
				writer.write(new Date().getTime()+ " " + intData[0] + " " + intData[1] + "\n");
				writer.close();
				return intData;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new int[0];
	}
}
