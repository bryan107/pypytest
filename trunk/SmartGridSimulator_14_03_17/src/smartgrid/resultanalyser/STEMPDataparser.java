package smartgrid.resultanalyser;

import java.text.DecimalFormat;

import fileAccessInterface.FileAccessAgentT;

public class STEMPDataparser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileAccessAgentT fagent = new FileAccessAgentT("C:\\TEST\\GRID\\NULL.csv", "C:\\TEST\\GRID\\NULL.csv");
		// User setup values and locations
		final double attackvaluedemand = 0.15;
		final double attackvaluesupply = 0.15;
		final String location = "TRANSMISSION";
		//
		int demandernumber = 22;
		int suppliernumber = 26;
		double[] demandervalues = new double[demandernumber];
		double[] suppliervalues = new double[suppliernumber];
		DecimalFormat df2 = new DecimalFormat("0.00");
		//Demander Analysis
		for(double r = 0 ; r < 0.011 ; r += 0.001){
			DecimalFormat df1 = new DecimalFormat("0.000");
			fagent.updatewritingpath("C:\\TEST\\GRID\\result_demander_GAD_V_" + df2.format(attackvaluedemand) + ".txt");
			for(int g = 0 ; g < demandernumber ; g++){
				fagent.updatereadingpath("C:\\TEST\\GRID\\" + location +"\\DEMANDER_GAD_V_"+ df2.format(attackvaluedemand) +"\\demander_G_" + g + "_R_" + df1.format(r) + ".txt");
//				fagent.updatereadingpath("C:\\TEST\\GRID\\" + location +"\\DEMANDER_EWMA_V_" + df2.format(attackvaluedemand) + "\\demander_G_" + g + "_R_" + df1.format(r) + ".txt");	
				while(!fagent.readLineFromFile().equals("Total Average Energy:"));
				demandervalues[g] = Double.valueOf(fagent.readLineFromFile());
			}
			String writeline = "";
			for(double value : demandervalues){
				writeline += value + " ";
			}
			fagent.writeLineToFile(writeline);

		}
		
		//Supplier Analysis
		for(double r = 0 ; r < 0.011 ; r += 0.001){
			DecimalFormat df1 = new DecimalFormat("0.000");
			fagent.updatewritingpath("C:\\TEST\\GRID\\result_supplier_GAD_V_" + df2.format(attackvaluesupply) + ".txt");
			for(int g = 0 ; g < suppliernumber ; g++){
				fagent.updatereadingpath("C:\\TEST\\GRID\\"+ location +"\\SUPPLIER_GAD_V_"+ df2.format(attackvaluedemand) +"\\supplier_G_" + g + "_R_" + df1.format(r) + ".txt");
//				fagent.updatereadingpath("C:\\TEST\\GRID\\"+ location +"\\SUPPLIER_EWMA_V_" + df2.format(attackvaluesupply) + "\\supplier_G_" + g + "_R_" + df1.format(r) + ".txt");
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
