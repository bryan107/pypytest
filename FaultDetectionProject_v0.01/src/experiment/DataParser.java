package experiment;

import fileAccessInterface.FileAccessAgent;

public class DataParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileAccessAgent agent = new FileAccessAgent(
				"C:\\TEST\\Berkeley\\Parsed_Data_1_2_31_37.txt",
				"C:\\TEST\\Berkeley\\data.txt");
		String line = agent.readLineFromFile();
		System.out.println("Start");
		while (line != null) {
			try {
				String[] dataofline = line.split(" ");
				System.out.println(line);
				int nodeid = Integer.valueOf(dataofline[3]);
				String outputstring = "";
				if ((nodeid > 30 && nodeid < 37) || (nodeid == 1) || (nodeid == 2)) {
					outputstring = dataofline[0] + "\t" + dataofline[1] + "\t"
							+ dataofline[3] + "\t" + dataofline[4];
					agent.writeLineToFile(outputstring);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			line = agent.readLineFromFile();
		}
		System.out.println("Finish");
	}

}
