package smartgrid.resultanalyser;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import fileAccessInterface.FileAccessAgentT;

public class KernelDataparser {

	// ** This Parser is the up-to-date version
	/**
	 * @param args
	 */
	// Global variable
	static double atkdetect = 0;
	static double atknumber = 0;
	static double falsedetect = 0;

	public static void main(String[] args) {
		FileAccessAgentT fagent = new FileAccessAgentT(
				"C:\\TEST\\GRID\\NULL.csv", "C:\\TEST\\GRID\\NULL.csv");
		// User setup values and locations
		final double attackvaluedemand = 2.00;
		final double attackvaluesupply = 2.00;
		final String location = "OUTAGE";
		//
		int demandernumber = 22;
		int suppliernumber = 26;
		double[] demandervalues = new double[demandernumber];
		double[] suppliervalues = new double[suppliernumber];
		DecimalFormat df2 = new DecimalFormat("0.00");
		System.out.println("Start");
		// Demander Analysis
		performAnalysis(fagent, attackvaluedemand, location, demandernumber,
				demandervalues, df2, "Kernel", "DEMANDER", "demander" , "DE");
		System.out.println("50%......");
		// Supplier Analysis
		performAnalysis(fagent, attackvaluesupply, location, suppliernumber,
				suppliervalues, df2, "Kernel", "SUPPLIER", "supplier" , "SU");
		System.out.println("Complete");
		// for(double r = 0 ; r < 0.011 ; r += 0.001){
		// DecimalFormat df1 = new DecimalFormat("0.000");
		// fagent.updatewritingpath("C:\\TEST\\GRID\\result_supplier_GAD_V_" +
		// df2.format(attackvaluesupply) + ".txt");
		// for(int g = 0 ; g < suppliernumber ; g++){
		// fagent.updatereadingpath("C:\\TEST\\GRID\\"+ location
		// +"\\SUPPLIER_GAD_V_"+ df2.format(attackvaluedemand) +"\\supplier_G_"
		// + g + "_R_" + df1.format(r) + ".txt");
		//
		// // Parse Total Energy
		// while(!fagent.readLineFromFile().equals("Total Average Energy:"));
		// suppliervalues[g] = Double.valueOf(fagent.readLineFromFile());
		// }
		// String writeline = "";
		// for(double value : suppliervalues){
		// writeline += value + " ";
		// }
		// fagent.writeLineToFile(writeline);
		// }
		// System.out.println("Complete");
	}

	private static void performAnalysis(FileAccessAgentT fagent,
			final double attackvaluedemand, final String location,
			int demandernumber, double[] demandervalues, DecimalFormat df2,
			String algorithm, String nodetype, String nodetype2 , String nodetypebri) {
		for (double r = 0; r < 0.011; r += 0.001) {
			DecimalFormat df1 = new DecimalFormat("0.000");
			// Initiate attack count
			atkdetect = 0;
			atknumber = 0;
			falsedetect = 0;
			// each number
			for (int g = 0; g < demandernumber; g++) {
				// Initiate attack map
				LinkedList<AttackInfo> strmap = new LinkedList<AttackInfo>();
				LinkedList<AttackInfo> sdtmap = new LinkedList<AttackInfo>();
				LinkedList<AttackInfo> ctrmap = new LinkedList<AttackInfo>();
				LinkedList<AttackInfo> cdtmap = new LinkedList<AttackInfo>();
				fagent.updatereadingpath("C:\\TEST\\GRID\\" + location + "\\"
						+ nodetype + "_" + algorithm + "_V_"
						+ df2.format(attackvaluedemand) + "\\"+ nodetype2 +"_G_" + g
						+ "_R_" + df1.format(r) + ".txt");
				// Parse Supplier Attacks
				while (!fagent.readLineFromFile()
						.equals("SupplierAttackRound:"));
				String str = fagent.readLineFromFile();
				extractAtkInfo(str, strmap);
				while (!fagent.readLineFromFile().equals("S FDI detected:"))
					;
				String sdt = fagent.readLineFromFile();
				extractAtkInfo(sdt, sdtmap);

				// Parse Consumer Attacks
				while (!fagent.readLineFromFile()
						.equals("ConsumerAttackRound:"))
					;
				String ctr = fagent.readLineFromFile();
				extractAtkInfo(ctr, ctrmap);
				while (!fagent.readLineFromFile().equals("C FDI detected:"))
					;
				String cdt = fagent.readLineFromFile();
				extractAtkInfo(cdt, cdtmap);
				// Parse Total Energy
				while (!fagent.readLineFromFile().equals(
						"Total Average Energy:"))
					;
				demandervalues[g] = Double.valueOf(fagent.readLineFromFile());
				// Calculate sub-suppliers
				updateSupplierResult(strmap, sdtmap, sdt);
				// Calculate sub-consumers
				updateConsummerResult(sdtmap, ctrmap, cdtmap, sdt);
			}
			String writeline = "";
			for (double value : demandervalues) {
				writeline += value + " ";
			}
			// Write false detection to file
			// set write file path
			fagent.updatewritingpath("C:\\TEST\\GRID\\F_" + algorithm
					+ nodetypebri + (int) (attackvaluedemand * 100) + ".txt");
			fagent.writeLineToFile("FDI Ratio," + df1.format(r)
					+ ",Detection Rate," + df1.format(atkdetect / atknumber)
					+ ",False Detection,"
					+ df1.format(falsedetect / (3650 * demandernumber)));
			// Write energy result to file
			fagent.updatewritingpath("C:\\TEST\\GRID\\" + algorithm
					+ nodetypebri + (int) (attackvaluedemand * 100) + ".txt");
			fagent.writeLineToFile(writeline);
		}
	}

	private static void updateConsummerResult(LinkedList<AttackInfo> sdtmap,
			LinkedList<AttackInfo> ctrmap, LinkedList<AttackInfo> cdtmap,
			String sdt) {
		for (AttackInfo ctrr : ctrmap) {
			atknumber++; // count real attack number
			for (AttackInfo cdtt : cdtmap) {
				// if detect matches
				if (ctrr.nodeid() == cdtt.nodeid()
						&& (cdtt.round() - ctrr.round()) < 15
						&& (cdtt.round() - ctrr.round()) > 0) {
					atkdetect++; // Count successful detection number
					sdtmap.remove(sdt); // Remove detected matched object from
										// list.
					break;
				}
			}
			falsedetect += sdtmap.size();
			// Count Remaining false-positive detections
		}
	}

	private static void updateSupplierResult(LinkedList<AttackInfo> strmap,
			LinkedList<AttackInfo> sdtmap, String sdt) {
		for (AttackInfo strr : strmap) {
			atknumber++; // count real attack number
			for (AttackInfo sdtt : sdtmap) {
				// if detect matches
				if (strr.nodeid() == sdtt.nodeid()
						&& (sdtt.round() - strr.round()) < 15
						&& (sdtt.round() - strr.round()) > 0) {
					atkdetect++; // Count successful detection number
					sdtmap.remove(sdt); // Remove detected matched object from
										// list.
					break;
				}
			}
			falsedetect += sdtmap.size();
			// Count Remaining false-positive detections
		}
	}

	private static void extractAtkInfo(String str, LinkedList<AttackInfo> strmap) {
		if (str.length() == 0) {
			return;
		}
		String[] strsplit = str.split(" ");
		for (int i = 1; i < strsplit.length; i++) {
			String[] temp = strsplit[i].split(":");
			int nodeid = Integer.valueOf(temp[0].substring(1,
					temp[0].length() - 1));
			int round = Integer.valueOf(temp[1]);
			strmap.add(new AttackInfo(nodeid, round));
		}
	}
}
