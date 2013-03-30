package experiment;

public class ProcessContainer02 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filewritelocation = "C:\\TEST\\GAD\\FaultType\\Results\\Non-Diffusive";
		String filereadlocation = "C:\\TEST\\GAD\\FaultType\\Non-Diffusive";
//		String abnormaltype = "Deviation";
		String abnormaltype = "Deviation";  
		AbnormalDetectionCore core = new AbnormalDetectionCore(	
				filewritelocation, filereadlocation, abnormaltype);
		LOG_AbnormalDetectionCore logcore = new LOG_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
		core.runSet(16, 0.01, 0.05);		
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
		logcore.runSet(16, 0.0001, 0.001);
		
		abnormaltype = "Noisy";
		core = new AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore = new LOG_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
		logcore.runSet(16, 0.0001, 0.001);
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
		core.runSet(16, 0.01, 0.05);

		

		

	}
}
