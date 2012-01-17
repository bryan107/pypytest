package wsnMessageControl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.UnsupportedCommOperationException;

import wsnMessageControl.transform.PropertyAgent;

import junit.framework.TestCase;

public class WSNDriverTest extends TestCase {
	public void testSendData() throws NoSuchPortException, PortInUseException,
			UnsupportedCommOperationException {
		byte[] data = { 3, 10, 20, 30, 2, 10, 20, 5, 10, 72, 30, 40, 50};
		final ByteArrayOutputStream finalout = new ByteArrayOutputStream();
		WSNDriver driver = new WSNDriver(null) {

			@Override
			public void sendData(byte[] data) {
				int tag = 0;
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				for (int i = 0; i < data.length; i++) {
					System.out.print(data[i] + " ");
				}
				for (int i = 0; i < data.length;) {
					out.write('H');
					System.out.println("H");
					for (int j = 0; j < 3; j++) {
						int length = data[i];
						out.write(data[i]);
						for (int k = 0; k < length; k++) {
							out.write(data[i + 1 + k]);
							if (data[i + 1 + k] == 'H') {
								out.write('H');
							}
						}
						i = i + 1 + length;
					}
				}
				System.out.println("ss");
				byte[] datapackage = setDataHeader(out.toByteArray(), PropertyAgent
						.getInstance().getProperties("config", "WSNPlatform"));
				while (tag < datapackage.length) {
					finalout.write(datapackage[tag]);
					tag++;
				}
			}
		};
		try {
			driver.sendData(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] finaldata = finalout.toByteArray();
		System.out.println("L = " + finaldata.length);
		for (int i = 0; i < finaldata.length; i++) {
			System.out.print(finaldata[i] + " ");
		}

	}
}
