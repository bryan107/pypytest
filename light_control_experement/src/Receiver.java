import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import s2h.util.jms.JmsStub;

public class Receiver implements MessageListener {

	JmsStub stub;
	public Receiver(JmsStub stub) {
		this.stub = stub;
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage t = (TextMessage) message;
			try {
				System.out.println(message.getJMSDestination());
				System.out.println(t.getText());
				System.out.println("TIME:" + new Date().getTime());
				
				BufferedWriter out = new BufferedWriter(new FileWriter("D:\\Message Testing Data\\Full_Message_Log.txt", true));
				out.write(new Date().getTime() + " ");				
				out.write(message.getJMSDestination().toString() + " ");
				out.write(t.getText() + "\n");
				out.close();
				BufferedWriter out2 = new BufferedWriter(new FileWriter("D:\\Message Testing Data\\Simple_Message_Log.txt", true));
				out2.write(new Date().getTime() + " ");				
				out2.write(message.getJMSDestination().toString().substring(8) + "\n");
				out2.close();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}


