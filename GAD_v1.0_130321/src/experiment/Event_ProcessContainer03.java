package experiment;

public class Event_ProcessContainer03 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filewritelocation = "C:\\TEST\\GAD\\Function\\Event\\Results";
		String filereadlocation = "C:\\TEST\\GAD\\Function\\Event\\Diffusive_Event";
//		String abnormaltype = "Deviation";
		String abnormaltype = "Deviation";  
		Event_AbnormalDetectionCore core = new Event_AbnormalDetectionCore(	
				filewritelocation, filereadlocation, abnormaltype);
		Event_AbnormalDetectionCoreLOG logcore = new Event_AbnormalDetectionCoreLOG(filewritelocation, filereadlocation, abnormaltype);
		
//		logcore.runSet(4, 0.0001, 0.001);
//		logcore.runSet(8, 0.0001, 0.001);
//		logcore.runSet(16, 0.0001, 0.001);
//		core.runSet(4, 0.01, 0.05);
//		core.runSet(8, 0.01, 0.05);
//		core.runSet(16, 0.01, 0.05);		

		
		abnormaltype = "Noisy";
		core = new Event_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore = new Event_AbnormalDetectionCoreLOG(filewritelocation, filereadlocation, abnormaltype);
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
		logcore.runSet(16, 0.0001, 0.001);
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
		core.runSet(16, 0.01, 0.05);

		

		

	}

}
