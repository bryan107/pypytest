package mfdr.math;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Sum {
	private static Log logger = LogFactory.getLog(Sum.class);
	private static Sum self = new Sum();

	private Sum() {

	}

	public static Sum getInstance() {
		return self;
	}

	/**
	 * Return the summation of \sum_{x=0 or 1}^n xCos(gx+k)
	 * 
	 * @param g
	 *            : scale of x
	 * @param k
	 *            : shift
	 * @param n
	 *            : number of summation
	 * @return
	 */
	public double xCos(double g, double k, int n) {
		double part1 = ((n + 1) * Math.sin(g * (n + 0.5 + k/g)) - Math.sin(g / 2
				+ k))
				/ (2 * Math.sin(g / 2));
		double part2 = (Math.cos(g * (n + 1 + k/g)) - Math.cos(g * (1 + k/g)))
				/ (2 * (1 - Math.cos(g)));
		return part1 + part2;
	}

	/**
	 * Return the summation of \sum_{x=0}^n xCos(gx+k)
	 * 
	 * @param g
	 *            : scale of x
	 * @param k
	 *            : shift
	 * @param n
	 *            : number of summation
	 * @return
	 */
	public double xSin(double g, double k, int n) {
		logger.info("Yet implemented");
		return 0;
	}

	/**
	 * Return the summation of \sum_{x=0}^n Cos(gx+k)
	 * 
	 * @param g
	 *            : scale of x
	 * @param k
	 *            : shift
	 * @param n
	 *            : number of summation
	 * @return
	 */
	public double cos(double g, double k, int n) {
		double nn = n;
		double part1 = Math.sin((nn + 1) * g / 2) / Math.sin(g / 2);
		double part2 = Math.cos((2 * k + nn * g) / 2);
		return part1 * part2;
	}

	/**
	 * Return the summation of \sum_{x=0 or 1}^n Sin(gx+k)
	 * 
	 * @param g
	 *            : scale of x
	 * @param k
	 *            : shift
	 * @param n
	 *            : number of summation
	 * @return
	 */
	public double sin(double g, double k, int n) {
		double part1 = Math.sin((n + 1) * g / 2) / Math.sin(g / 2);
		double part2 = Math.sin((2 * k + n * g) / 2);
		return part1 * part2;
	}

}