package experiment;

public class DFDProcessContainer02 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filewritelocation = "C:\\TEST\\GAD\\FaultType\\Results\\Log_Event";
		String filereadlocation = "C:\\TEST\\GAD\\FaultType\\Log_Event";
//		String abnormaltype = "Deviation";
		String abnormaltype = "Deviation";  
		DFDAbnormalDetectionCore core = new DFDAbnormalDetectionCore(	
				filewritelocation, filereadlocation, abnormaltype);
		DFDLOG_AbnormalDetectionCore logcore = new DFDLOG_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
		logcore.runSet(16, 0.0001, 0.001);
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
		core.runSet(16, 0.01, 0.05);		

		
		abnormaltype = "Noisy";
		core = new DFDAbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore = new DFDLOG_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
		logcore.runSet(16, 0.0001, 0.001);
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
		core.runSet(16, 0.01, 0.05);

		

		

	}
}
