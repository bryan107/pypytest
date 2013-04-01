package experiment;

public class VAProcessContainer01 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filewritelocation = "C:\\TEST\\GAD\\V&A\\Results\\N_2009";
		String filereadlocation = "C:\\TEST\\GAD\\V&A";
		// String abnormaltype = "Deviation";
		String type = "temp";
		String abnormaltype = "Null";
		VAAbnormalDetectionCore core = new VAAbnormalDetectionCore(
				filewritelocation, filereadlocation, abnormaltype, type);
		VAAbnormalDetectionCoreLOG corelog = new VAAbnormalDetectionCoreLOG(
				filewritelocation, filereadlocation, abnormaltype, type);
//		corelog .runSet(8, 0.0000, 0.002);
		core.runSet(8, 0.0000, 0.00001);
		// abnormaltype = "Proportional_Deviation";
		// core = new VAAbnormalDetectionCore(filewritelocation,
		// filereadlocation, abnormaltype,type);
		// core.runSet(8, 0.01, 0.05);
		// core.runSet(8, 0.0001, 0.001);

//		type = "humi";
//		abnormaltype = "Noisy";
//		core = new VAAbnormalDetectionCore(filewritelocation, filereadlocation,
//				abnormaltype, type);
//		core.runSet(8, 0.01, 0.05);
//		corelog .runSet(8, 0.0001, 0.0001);
		// abnormaltype = "Proportional_Deviation";
		// core = new VAAbnormalDetectionCore(filewritelocation,
		// filereadlocation, abnormaltype,type);
		// core.runSet(8, 0.01, 0.05);
		// core.runSet(8, 0.0001, 0.001);

	}
}
