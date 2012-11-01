package smartgrid.resultanalyser;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import faultDetection.correlationControl.ProcessManager;
import faultDetection.correlationControl.ProcessedReadingPack;
import fileAccessInterface.FileAccessAgentT;

public class DataParserWithSTEMP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double attackvalue = 0.15;
		DecimalFormat df1 = new DecimalFormat("0.000");
		DecimalFormat df2 = new DecimalFormat("0.00");
		int demandernumber = 22;
		int suppliernumber = 26;
		
		FileAccessAgentT fagent = new FileAccessAgentT("C:\\TEST\\NULL.txt", "C:\\TEST\\NULL.txt");
		ProcessManager pms = new ProcessManager();
		ProcessManager pmd = new ProcessManager();
		
		
		
		//Demander Analysis
		for(double r = 0 ; r < 0.011 ; r +=0.001){			
			for(int g = 0 ; g < demandernumber ; g++){
				fagent.updatereadingpath("C:\\TEST\\GRID\\DEMANDER_V_" + df2.format(attackvalue) + "\\demander_G_" + g + "_R_" + df1.format(r) + ".txt");
				String line = fagent.readLineFromFile();
				String[] splitedline= line.split("\t");
				Map<Integer, Double> readingpack = new HashMap<Integer, Double>();
				if(splitedline[0].equals("S")){
					for(int key = 0 ; key < 10 ; key++){
						readingpack.put(key, Double.valueOf(splitedline[key + 1]));
					}
					ProcessedReadingPack prpack = pms.markReadings(readingpack);
					for(int key = 0 ; key < 10 ; key++){
						if(prpack.markedReadingPack().get(key).deviceCondition() == 0){
							
						}
					}
					
				}
				else if(splitedline[0].equals("C")){
					
				}
				else{
					
				}
				
				pms.markReadings(readingpack);
			}	
		}

		//Supplier Analysis
		for(double r = 0 ; r < 0.011 ; r +=0.001){
			for(int g = 0 ; g < suppliernumber ; g++){
				fagent.updatereadingpath("C:\\TEST\\GRID\\SUPPLIER_V_" + df2.format(attackvalue) + "\\supplier_G_" + g + "_R_" + df1.format(r) + ".txt");
			}
		}
		
		
	}

}
