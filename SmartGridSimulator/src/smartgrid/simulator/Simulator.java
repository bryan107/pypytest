package smartgrid.simulator;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class Simulator {

	public static void main(String[] args) {
		Log logger = LogFactory.getLog(Simulator.class);
		final int totalround = 365;
		double[] demands = { 396, 2671, 153, 122, 78, 337, 386, 701, 114, 392,
				92, 568, 903, 43, 690, 245, 785, 1483, 664, 241, 622, 446 };
		double[] supplys = { 68, 163, 53, 185, 861, 397, 1015, 646, 89, 147,
				122, 458, 503, 106, 264, 1460, 1582, 101, 1194, 2005, 516, 12,
				52, 129, 316, 115 };
		double[][] detaileddemands = new double[demands.length][2];
		double[][] detailedsupplys = new double[supplys.length][2];
		Map<Integer, GridSimulator> demandermap = new HashMap<Integer, GridSimulator>();
		Map<Integer, GridSimulator> suppliermap = new HashMap<Integer, GridSimulator>();
		double count = 0, totalcount = 520;
		GridSimulator grid;
		DecimalFormat df = new DecimalFormat("0.0");
		
		for (double ratio = 0; ratio < 1.1; ratio += 0.1) {
			for (int i = 0; i < demands.length; i++) {
				detaileddemands[i][0] = 200 + (1800 * Math.random());
				detaileddemands[i][1] = detaileddemands[i][0] + demands[i];
				grid = new GridSimulator("C:\\TEST\\GRID\\DEMANDER\\demander_G_"
						+ i + "_R_"+ df.format(ratio) +".txt", detaileddemands[i][0],
						detaileddemands[i][1]);
				grid.run(totalround);
				System.out.println("[" + i + "] S: " + detaileddemands[i][0]
						+ " D: " + detaileddemands[i][1] + " D-S: "
						+ (detaileddemands[i][1] - detaileddemands[i][0]));
				count++;
				logger.info(count * 100 / totalcount + "% Complete");
			}

			for (int i = 0; i < supplys.length; i++) {
				detailedsupplys[i][1] = 200 + (1800 * Math.random());
				detailedsupplys[i][0] = detailedsupplys[i][1] + supplys[i];
				grid = new GridSimulator("C:\\TEST\\GRID\\SUPPLIER\\supplier_G_"
						+ i + "_R_"+ df.format(ratio) + ".txt", detailedsupplys[i][0],
						detailedsupplys[i][1]);
				grid.run(totalround);
				System.out.println("[" + i + "] S: " + detailedsupplys[i][0]
						+ " D: " + detailedsupplys[i][1] + " S-D: "
						+ (detailedsupplys[i][0] - detailedsupplys[i][1]));
				count++;
				logger.info(df.format(count * 100 / totalcount) + "% Complete");
			}
		}

//		double dtemp = 0, stemp = 0;
//		for (double value : demands) {
//			dtemp += value;
//		}
//		for (double value : supplys) {
//			stemp += value;
//		}
//		System.out.println("D: " + dtemp + " S: " + stemp);
	}

}
