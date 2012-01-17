package wsnMessageControl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wsnMessageControl.transform.PropertyAgent;

//TODO NEW: Check UART Communication
public class WSNDriver {
	private SerialPort port;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	static Log logger = LogFactory.getLog(WSNDriver.class);

	public WSNDriver(String comport) throws NoSuchPortException,
			PortInUseException, UnsupportedCommOperationException {

		 logger.info("set com port: " + comport);
		 CommPortIdentifier identifier = CommPortIdentifier
		 .getPortIdentifier(comport);
		 port = (SerialPort) identifier.open("test", 10);
		 port.setSerialPortParams(115200, 8, 1, SerialPort.PARITY_NONE);
		 logger.info("Comport Connecting Success");
	}

	// TODO Adjust Start Byte Function using reseted algorithm (Reset when got
	// odd numbers of H)
	public byte[] getData() throws IOException {

		int firstlength = 0;
		out.reset();
		InputStream in = port.getInputStream();
		firstlength = readHeadLine(in);

		try {
			readAColumn(firstlength, in, out);
			readAColumn(in.read(), in, out);
			readAColumn(in.read(), in, out);
		} catch (Exception e) {
			return new byte[0];
		}
		logger.info("Get Data Success!");
		return out.toByteArray();
	}

	private int readHeadLine(InputStream in) {
		int firstlength = 0;
		int startbytecheck = 0;
		int bytedata = 0;

		while (true) {
			try {
				bytedata = in.read();
			} catch (IOException e) {
				logger.error("WSN Data Stream Read Error");
			}

			if (bytedata == 'H') {
				if (startbytecheck == 0) {// First H
					startbytecheck++;
					continue;
				}
			} else {
				if (startbytecheck == 1) {
					firstlength = bytedata;
					startbytecheck = 0;
					return firstlength;
				} else {
					logger.warn("Start Byte Exception: Data = " + bytedata);
				}
			}
		}
	}

	private void readAColumn(int len, InputStream in, ByteArrayOutputStream out)
			throws IOException {
		out.write(len);
		int data;
		int functionalbyte = 0;
		for (int i = 0; i < len; i++) {
			data = in.read();
			System.out.print(data);
			if (data == -1) {
				throw new IllegalStateException("early eof.");
			}
			if (data == 'H') {
				if (functionalbyte == 0) {
					functionalbyte++;
					// If Double does not that account in data length than i--
					// i--;
					continue;
				} else if (functionalbyte == 1) {
					functionalbyte = 0;
				} else {
					logger.warn("Double H Error, Error Count = "
							+ functionalbyte);
					functionalbyte = 0;
				}
			}
			out.write(data);
		}
	}
	public void sendData(byte[] data) throws IOException {
		int tag = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream wsnout = port.getOutputStream();

		for (int i = 0; i < data.length;) {
			out.write('H');
			for (int j = 0; j < 3; j++) {
				int length = data[i];
				out.write(length);
				for (int k = 0; k < length; k++) {
					out.write(data[i + 1 + k]);
					if (data[i + 1 + k] == 'H') {
						out.write('H');
					}
				}
				i = i + 1 + length;
			}
		}
		byte[] datapackage = setDataHeader(out.toByteArray(), PropertyAgent
				.getInstance().getProperties("config", "WSNPlatform"));
		while (tag < datapackage.length) {
			wsnout.write(datapackage[tag]);
			tag++;
		}
		wsnout.close();
	}

	protected byte[] setDataHeader(byte[] data, String wsnplatform) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if ("UNET".equals(wsnplatform)) {
			out.write(0xCC);
			out.write(data.length);
		} else if (wsnplatform == null) {
			logger.error("WSN Platform Null Selection");
			return null;
		}
		try {
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}