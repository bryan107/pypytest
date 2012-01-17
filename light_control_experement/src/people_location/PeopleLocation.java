package people_location;

import java.util.Date;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class PeopleLocation {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
		JmsStub stub;
		try {
			stub = new JmsStub(new ActiveMQConnectionFactory(
			"tcp://192.168.4.100:61616"));


			while((new Date().getHours() != 19) || (new Date().getMinutes() != 13));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

//			while((new Date().getHours() != 19) || (new Date().getMinutes() != 13));
//			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
//			Thread.sleep(3000);

			while((new Date().getHours() != 19) || (new Date().getMinutes() != 14));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 19) || (new Date().getMinutes() != 17));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 19) || (new Date().getMinutes() != 25));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "CURTAIN_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 19) || (new Date().getMinutes() != 27));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 20) || (new Date().getMinutes() != 0));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 20) || (new Date().getMinutes() != 46));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 20) || (new Date().getMinutes() != 49));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 20) || (new Date().getMinutes() != 49));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 21) || (new Date().getMinutes() != 30));
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 22) || (new Date().getMinutes() != 0));
			stub.send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 22) || (new Date().getMinutes() != 1));
			stub.send(JsonUtils.createBuilder().add("value", "BEDROOM").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 22) || (new Date().getMinutes() != 32));
			stub.send(JsonUtils.createBuilder().add("value", "BEDROOM-2").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 23) || (new Date().getMinutes() != 49));
			stub.send(JsonUtils.createBuilder().add("value", "LIVINGROOM").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 0) || (new Date().getMinutes() != 30));
			stub.send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "BEDROOM").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 0) || (new Date().getMinutes() != 40));
			stub.send(JsonUtils.createBuilder().add("value", "OUT").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 7));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 7) || (new Date().getMinutes() != 20));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "CURTAIN_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "FAN-N_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "FAN-S_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 7) || (new Date().getMinutes() != 30));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 7) || (new Date().getMinutes() != 45));
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "FAN-N_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "FAN-S_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 8) || (new Date().getMinutes() != 15));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 8) || (new Date().getMinutes() != 27));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 8) || (new Date().getMinutes() != 30));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 8) || (new Date().getMinutes() != 33));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "OUT").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 12) || (new Date().getMinutes() != 10));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 12) || (new Date().getMinutes() != 11));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 12) || (new Date().getMinutes() != 21));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 12) || (new Date().getMinutes() != 22));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 12) || (new Date().getMinutes() != 45));
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 13) || (new Date().getMinutes() != 15));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);

			while((new Date().getHours() != 13) || (new Date().getMinutes() != 45));
			stub.send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
			Thread.sleep(3000);
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDGROOM").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 14) || (new Date().getMinutes() != 30));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 14) || (new Date().getMinutes() != 45));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

			while((new Date().getHours() != 14) || (new Date().getMinutes() != 47));
			stub.send(JsonUtils.createBuilder().add("peoplelocation", "OUT").toJson(), "ssh.CONTEXT");
			Thread.sleep(3000);

		} catch (JMSException e) {
			e.printStackTrace();
		}


	}

}
