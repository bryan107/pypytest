package smartgrid.resultanalyser;

import java.text.DecimalFormat;

import fileAccessInterface.FileAccessAgentT;

public class Dataparser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileAccessAgentT fagent = new FileAccessAgentT("C:\\TEST\\NULL.txt", "C:\\TEST\\NULL.txt");
		final double attackvalue = -0.10;
		int demandernumber = 22;
		int suppliernumber = 26;
		double[] demandervalues = new double[demandernumber];
		double[] suppliervalues = new double[suppliernumber];
		DecimalFormat df2 = new DecimalFormat("0.00");
		//Demander Analysis
//		for(double r = 0 ; r < 0.011 ; r += 0.001){
//			DecimalFormat df1 = new DecimalFormat("0.000");
//			fagent.updatewritingpath("C:\\TEST\\GRID\\result_demander_STEMP_V_" + df2.format(attackvalue) + ".txt");
//			for(int g = 0 ; g < demandernumber ; g++){
//				fagent.updatereadingpath("C:\\TEST\\GRID\\DEMANDER_STEMP_V_" + df2.format(attackvalue) + "\\demander_G_" + g + "_R_" + df1.format(r) + ".txt");	
//				while(!fagent.readLineFromFile().equals("Total Average Energy:"));
//				demandervalues[g] = Double.valueOf(fagent.readLineFromFile());
//			}
//			String writeline = "";
//			for(double value : demandervalues){
//				writeline += value + " ";
//			}
//			fagent.writeLineToFile(writeline);
//
//		}
//		
		//Supplier Analysis
		for(double r = 0 ; r < 0.011 ; r += 0.001){
			DecimalFormat df1 = new DecimalFormat("0.000");
			fagent.updatewritingpath("C:\\TEST\\GRID\\result_supplier_V_" + df2.format(attackvalue) + ".txt");
			for(int g = 0 ; g < suppliernumber ; g++){
				fagent.updatereadingpath("C:\\TEST\\GRID\\SUPPLIER_V_" + df2.format(attackvalue) + "\\supplier_G_" + g + "_R_" + df1.format(r) + ".txt");
				while(!fagent.readLineFromFile().equals("Total Average Energy:"));
				suppliervalues[g] = Double.valueOf(fagent.readLineFromFile());
			}
			String writeline = "";
			for(double value : suppliervalues){
				writeline += value + " ";
			}
			fagent.writeLineToFile(writeline);
		}
		System.out.println("Complete");
		
	}
}
