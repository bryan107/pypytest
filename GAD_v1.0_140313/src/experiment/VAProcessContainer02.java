package experiment;

public class VAProcessContainer02 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filewritelocation = "C:\\TEST\\GAD\\V&A\\Results\\8_2009";
		String filereadlocation = "C:\\TEST\\GAD\\V&A\\2009";
//		String abnormaltype = "Deviation";
		String type = "temp";
		String abnormaltype = "Proportional_Deviation";
		VAAbnormalDetectionCore core = new VAAbnormalDetectionCore(
				filewritelocation, filereadlocation, abnormaltype, type);
		VAAbnormalDetectionCoreLOG corelog = new VAAbnormalDetectionCoreLOG(
				filewritelocation, filereadlocation, abnormaltype, type);
		corelog.runSet(8, 0.0001, 0.002);
		core.runSet(8, 0.01, 0.05);		
//		abnormaltype = "Proportional_Deviation";
//		core = new VAAbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype,type);
//		core.runSet(8, 0.01, 0.05);		
//		core.runSet(8, 0.0001, 0.001);

		
//		type = "humi";
//		abnormaltype = "Proportional_Deviation";
//		core = new VAAbnormalDetectionCore(
//				filewritelocation, filereadlocation, abnormaltype, type);
//		core.runSet(8, 0.01, 0.05);		
//		corelog.runSet(8, 0.0001, 0.001);
//		abnormaltype = "Proportional_Deviation";
//		core = new VAAbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype,type);
//		core.runSet(8, 0.01, 0.05);		
//		core.runSet(8, 0.0001, 0.001);

	}
}
