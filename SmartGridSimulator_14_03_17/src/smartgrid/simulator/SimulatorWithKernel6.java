package smartgrid.simulator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class SimulatorWithKernel6 {

	public static void main(String[] args) {
		Log logger = LogFactory.getLog(SimulatorWithKernel6.class);
		final int totalround = 365;
		double[] demands = { 396, 2671, 153, 122, 78, 337, 386, 701, 114, 392,
				92, 568, 903, 43, 690, 245, 785, 1483, 664, 241, 622, 446 };
		double[] supplys = { 68, 163, 53, 185, 861, 397, 1015, 646, 89, 147,
				122, 458, 503, 106, 264, 1460, 1582, 101, 1194, 2005, 516, 12,
				52, 129, 316, 115 };
		double[][] detaileddemands = new double[demands.length][2];
		double[][] detailedsupplys = new double[supplys.length][2];
		Map<Integer, GridSimulatorWithGAD> demandermap = new HashMap<Integer, GridSimulatorWithGAD>();
		Map<Integer, GridSimulatorWithGAD> suppliermap = new HashMap<Integer, GridSimulatorWithGAD>();
		double count = 0;
		GridSimulatorWithKernel grid;
		DecimalFormat df = new DecimalFormat("0.000");
		
		for (double ratio = 0.010; ratio < 0.011; ratio += 0.001) {
//			DEMANDS
			for (int i = 0; i < demands.length; i++) {
				detaileddemands[i][0] = 20 + (180 * Math.random());
				detaileddemands[i][1] = detaileddemands[i][0] + demands[i];
				grid = new GridSimulatorWithKernel("C:\\TEST\\GRID\\OUTAGE\\DEMANDER_Kernel_V_2.00\\demander_G_"
						+ i + "_R_"+ df.format(ratio) +".txt", (detaileddemands[i][0])/10,
						(detaileddemands[i][1])/10);
				// CONSUMER FAULT CHANCE
				grid.updateConsumerFaultChance(ratio);
				grid.run(totalround);
				System.out.println("[" + i + "] S: " + detaileddemands[i][0]
						+ " D: " + detaileddemands[i][1] + " D-S: "
						+ (detaileddemands[i][1] - detaileddemands[i][0]));
				count++;
				logger.info(df.format(count * 100 / ((demands.length + supplys.length) *11)) + "% Complete");
			}

			//SUPPLIERS
			for (int i = 0; i < supplys.length; i++) {
				detailedsupplys[i][1] = 20 + (180 * Math.random());
				detailedsupplys[i][0] = detailedsupplys[i][1] + supplys[i];
				grid = new GridSimulatorWithKernel("C:\\TEST\\GRID\\OUTAGE\\SUPPLIER_Kernel_V_2.00\\supplier_G_"
						+ i + "_R_"+ df.format(ratio) + ".txt", (detailedsupplys[i][0])/10,
						(detailedsupplys[i][1])/10);
				// SUPPLIER FAULT CHANCE
				grid.updateSupplierFaultChance(ratio);
				grid.run(totalround);
				System.out.println("[" + i + "] S: " + detailedsupplys[i][0]
						+ " D: " + detailedsupplys[i][1] + " S-D: "
						+ (detailedsupplys[i][0] - detailedsupplys[i][1]));
				count++;
				logger.info(df.format(count * 100 / ((demands.length + supplys.length) *11)) + "% Complete");
			}
		}
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
	}

}
