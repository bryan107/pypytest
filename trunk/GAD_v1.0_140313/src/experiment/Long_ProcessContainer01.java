package experiment;

public class Long_ProcessContainer01 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filewritelocation = "C:\\TEST\\GAD\\Function\\Long-term\\Results";
		String filereadlocation = "C:\\TEST\\GAD\\Function\\Long-term\\Diffusive_Event";
//		String abnormaltype = "Deviation";
		String abnormaltype = "Deviation";  
		Long_AbnormalDetectionCore core = new Long_AbnormalDetectionCore(	
				filewritelocation, filereadlocation, abnormaltype);
		Long_AbnormalDetectionCoreLOG logcore = new Long_AbnormalDetectionCoreLOG(filewritelocation, filereadlocation, abnormaltype);
		
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
		logcore.runSet(16, 0.0001, 0.001);
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
		core.runSet(16, 0.01, 0.05);		

//		
//		abnormaltype = "Noisy";
//		core = new Long_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
//		logcore = new Long_AbnormalDetectionCoreLOG(filewritelocation, filereadlocation, abnormaltype);
//		logcore.runSet(4, 0.0001, 0.001);
//		logcore.runSet(8, 0.0001, 0.001);
//		logcore.runSet(16, 0.0001, 0.001);
//		core.runSet(4, 0.01, 0.05);
//		core.runSet(8, 0.01, 0.05);
//		core.runSet(16, 0.01, 0.05);

		

		

	}
}
