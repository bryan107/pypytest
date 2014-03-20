package experiment;

public class ProcessContainer02 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filewritelocation = "C:\\TEST\\GAD\\FaultType\\Results\\_Variation\\Exp_Event";
		String filereadlocation = "C:\\TEST\\GAD\\FaultType\\Exp_Event";

		// Type 1
		String abnormaltype = "Deviation";
		
		VariationAbnormalDetectionCore core = new VariationAbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		VariationLOG_AbnormalDetectionCore logcore = new VariationLOG_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);		
		
//		VariationAbnormalDetectionCore core = new VariationAbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
//		VariationLOG_AbnormalDetectionCore logcore = new VariationLOG_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
//		logcore.runSet(16, 0.0001, 0.001);
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
//		core.runSet(16, 0.01, 0.05);		

		// Type 2
		abnormaltype = "Noisy";
		core = new VariationAbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore = new VariationLOG_AbnormalDetectionCore(filewritelocation, filereadlocation, abnormaltype);
		logcore.runSet(4, 0.0001, 0.001);
		logcore.runSet(8, 0.0001, 0.001);
//		logcore.runSet(16, 0.0001, 0.001);
		core.runSet(4, 0.01, 0.05);
		core.runSet(8, 0.01, 0.05);
//		core.runSet(16, 0.01, 0.05);
	}
}
