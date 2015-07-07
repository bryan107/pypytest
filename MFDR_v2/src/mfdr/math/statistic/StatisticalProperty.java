package mfdr.math.statistic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.math.emd.datastructure._BAK_IMF;

public class StatisticalProperty {

	private static Log logger = LogFactory.getLog(_BAK_IMF.class);
	private static StatisticalProperty self = new StatisticalProperty();

	private StatisticalProperty() {
	}

	public static StatisticalProperty getInstance() {
		return self;
	}

	/**
	 * This function exploits energy distribution to evaluate the statistical significance of 
	 * distribution. It considers both upper bounds and lower bounds.
	 * @param sb
	 * @param x
	 * @param y
	 * @param t_threshold
	 * @return
	 */
	public boolean isStatisticalSignificance(StatisticalBounds sb, double x,
			double y, double t_threshold) {
		double x_value = Math.log(x);
		double y_value = Math.log(y);
		
		if(x_value > t_threshold){
			return true;
		}
//		logger.info("X:" + x_value + "  Value:" + y_value + " Upper Bound:"
//				+ sb.upperbound(x_value) + " Lower Bound:"
//				+ sb.lowerbound(x_value));
		if (y_value > sb.upperbound(x_value) ||  y_value < sb.lowerbound(x_value) ){
			return true;
		}
		return false;
	}

	/**
	 * This function exploits energy distribution to evaluate the statistical significance of 
	 * distribution. It considers ONLY upper bounds.
	 * @param sb
	 * @param x
	 * @param y
	 * @param t_threshold
	 * @return
	 */
	public boolean isUpperBoundStatisticalSignificance(StatisticalBounds sb, double x,
			double y, double t_threshold) {
		double x_value = Math.log(x);
		double y_value = Math.log(y);
		
		if(x_value > t_threshold){
			return true;
		}
		logger.info("X:" + x_value + "  Value:" + y_value + " Upper Bound:"
				+ sb.upperbound(x_value));
		if (y_value > sb.upperbound(x_value)){
			return true;
		}
		return false;
	}
}
