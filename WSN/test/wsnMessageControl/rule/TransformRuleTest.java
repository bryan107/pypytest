package wsnMessageControl.rule;

import wsnMessageControl.transform.PropertyAgent;
import junit.framework.TestCase;

public class TransformRuleTest extends TestCase {

//	private TransformRule getRule(long id) {
//		String key = Transformer.getInstance().getSensorAttribute(id);
//		return Transformer.getInstance().getSensor(key);
//	}
//
//	public void testGetRule() throws Exception {
//		assertEquals(Transformer.NULL_RULE.getClass(), getRule(1000).getClass());
//		assertFalse(Transformer.NULL_RULE.getClass().equals(
//				getRule(1).getClass()));
//		assertEquals(new TemperatureRule().getClass(), getRule(1).getClass());
//	}
//
//	public void testConvertLight() throws Exception {
//		long input = 1234567890L;
//		assertEquals(Long.toString(input), getRule(3).convert(input));
//	}
//
//	public void testConvertHumidity() throws Exception {
//		long input = 1234567890L;
//		assertEquals(Double.toString(input / 100D), getRule(2).convert(input));
//	}
//
//	public void testConvertTemperature() throws Exception {
//		long input = 1234567890L;
//		assertEquals(Double.toString((input / 100D) - 4), getRule(1).convert(
//				input));
//	}

	// public void testGetLocation(){
	// long input = 1L;
	// assertEquals("Bedroom", Transformer.getInstance().getLocation(input));
	// assertFalse("Bedroom".equals(Transformer.getInstance().getLocation(11)));
	// }

//	public void testIOByteStream() {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		int test = 333;
//		out.write(test);
//		byte[] temp = out.toByteArray();
//
//		System.out.println(temp.length + "num" + temp[0]);
//		assertEquals(11, 11);
//	}

//	public void testInfixToProfix() {
//		System.out.println("Postfix:" + InfixToPostfix.convert("10%3"));
//	}
	
//	public void testInfixCalc(){
//		//assertEquals(2,Calculator.getInstance().infixCalc("4-2"));
//		String test = "3*5";
//		System.out.println(19%6);
//		System.out.println("ANS: " + Double.toString(Calculator.getInstance().infixCalc(test)));
//	}
//	
//	public void testGetSensorValue(){
//		assertEquals("26.0", Transformer.getInstance().getSensorValue("Temperature", 3000));
//	}
//	public void testGetValue(){
//		PropertyAgent a = new PropertyAgent("/conf/");
//		
//		System.out.println(a.getProperties("actuator", "Beeper"));
//		System.out.println(Transformer.getInstance().getActuatorID("Beeper","Global"));
//		
//	}
	
	
//	public void testJMS() throws JMSException {
//		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
//		factory.setBrokerURL("tcp://192.168.4.100:61616");
//		JmsStub stub = new JmsStub(factory);
//		stub.addTopicListener("ssh.RAW_DATA", new MessageListener(){
//
//			@Override
//			public void onMessage(Message arg0) {
//				// TODO Auto-generated method stub
//				System.out.println(arg0);
//				
//			}});
//
//	}
	
//	 TODO Cannot Really Save and Read the Properties File Realtime
	public void testPropertyAgentWrite(){
		PropertyAgent.getInstance().listProperties("location");
		System.out.println();
		PropertyAgent.getInstance().setProperties("location", "66", "Living room");
		System.out.println("66:" + PropertyAgent.getInstance().getProperties("location", "66"));
	}
	
//	public void testDriver() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{
//		WSNDriver driver = new WSNDriver("COM6");
//		
//		while(true){
//			System.out.println("TEST");
//			byte[] a = driver.getData();
//			System.out.println("TEST2");
//			for(int i =  0 ; i < a.length ; i++){
//				
//				System.out.print(a[i]);
//			}
//			System.out.println("");
//		}
//		
//		
//	}
}
