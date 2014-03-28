package cores;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import faultSymptom.FaultSymptom;
import faultSymptom.NoisyReadingFault;
import faultSymptom.NullFault;
import faultSymptom.ProportionalDeviationFault;

public class ProcessContainer2 {

	private static Log logger = LogFactory.getLog(ProcessContainer2.class);
	
	public static void main(String[] args) {
		Map<String, Integer> labels = new HashMap<String, Integer>();
		labels.put("G47D", 106);
		labels.put("G47E", 107);
		labels.put("G47F", 108);
		labels.put("G47G", 109);
		labels.put("G47F Csilver goblet", 11);
		labels.put("G47F Ccabinet", 12);
		labels.put("G47G C2", 19);
		labels.put("G47G C12", 20);
		String writingpath = "C:\\TEST\\GAD\\V&A\\";
		String readingpath = "C:\\My Imperial (IRIS)\\SkyDrive\\Paper Writing\\SenSys_2013\\V&A Trace\\vam_trace_original\\vam trace\\2009OceanData.csv";
		int rounds = 34944;
		
//		for(double faultratio = 0.01 ; faultratio <= 0.051 ; faultratio+= 0.01){
//			FaultSymptom fs = new ProportionalDeviationFault(0.2);
//			DataParser dp = new DataParser(writingpath, readingpath, labels, fs, faultratio);
//			dp.run();
//		}
		for(double faultratio = 0.0001 ; faultratio < 0.01 ; faultratio *= 10){
			FaultSymptom fs = new ProportionalDeviationFault(0.2);
			DataParser dp = new DataParser(writingpath, readingpath, labels, fs, faultratio, rounds);
			dp.run();
		}
//		for(double faultratio = 0.01 ; faultratio <= 0.051 ; faultratio+= 0.01){
//			FaultSymptom fs = new NoisyReadingFault(2);
//			DataParser dp = new DataParser(writingpath, readingpath, labels, fs, faultratio);
//			dp.run();
//		}
//		for(double faultratio = 0.0001 ; faultratio < 0.01 ; faultratio *= 10){
//			FaultSymptom fs = new NoisyReadingFault(2);
//			DataParser dp = new DataParser(writingpath, readingpath, labels, fs, faultratio);
//			dp.run();
//		}
		

	}

}
