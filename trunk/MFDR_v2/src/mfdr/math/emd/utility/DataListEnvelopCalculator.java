package mfdr.math.emd.utility;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.math.emd.datastructure.Envelopes;
import mfdr.math.emd.datastructure.LocalExtremas;
import mfdr.utility.DataListOperator;
import flanagan.interpolation.CubicSpline;

public class DataListEnvelopCalculator {

	private static Log logger = LogFactory
			.getLog(DataListEnvelopCalculator.class);
	private final short TIME = 0;
	private final short VALUE = 1;
	private int location;

	// Default resolution and location. setup before use.
	private static DataListEnvelopCalculator self = new DataListEnvelopCalculator(
			1);

	private DataListEnvelopCalculator(int location) {
		this.location = location;
	}

	public void setupLocation(int location) {
		this.location = location;
	}

	public static DataListEnvelopCalculator getInstance() {
		return self;
	}

	public Envelopes getEnvelopes(LinkedList<Data> residual, LocalExtremas le) {
		Envelopes envelopes = new Envelopes(new TimeSeries(), new TimeSeries());
		// 0. Use symmetric extremes to complete Extrapolation.
		symFrontEnvelope(residual, le, location);
		symRearEnvelope(residual, le, location);

		// 1. Convert Data from linked list to array

		double[] upperextremas = DataListOperator.getInstance()
				.linkedListToArray(le.localMaxima(), TIME);
		double[] lowerextremas = DataListOperator.getInstance()
				.linkedListToArray(le.localMinima(), TIME);

		// 2. Prepare value array for interpolation.
		double[] uppervalues = DataListOperator.getInstance()
				.linkedListToArray(le.localMaxima(), VALUE);
		double[] lowervalues = DataListOperator.getInstance()
				.linkedListToArray(le.localMinima(), VALUE);

		// 3. Do Cubic Spline Interpolation
		CubicSpline upperCS = new CubicSpline(upperextremas, uppervalues);
		CubicSpline lowerCS = new CubicSpline(lowerextremas, lowervalues);
		// Calculate upperenvelope
			calculateEnvelope(envelopes.upperEnvelope(), upperCS, residual);
			// Calculate lowerenvelope
			calculateEnvelope(envelopes.lowerEnvelope(), lowerCS, residual);

		return envelopes;
	}

	/*
	 * Symmetric Front Envelopes
	 */

	private void symFrontEnvelope(LinkedList<Data> residual, LocalExtremas le,
			int location) {
		/*
		 * First extrema is a maxima.
		 */
		if (le.localMaxima().peek().time() < le.localMinima().peek().time()) {
			// Symmetric section is too small
			if (le.localMinima().peek().time() - le.localMaxima().peek().time() < le
					.localMaxima().peek().time()
					- residual.peek().time()) {
				symFrontMaximaAlongOrigin(residual, le, location);
				symFrontMinimaAlongOrigin(residual, le, location);
			}
			// Symmetric section is long enough
			else {
				symFrontMinimaAlongMaxima(residual, le, location);
				symFrontMaximaAlongMaxima(residual, le, location);
			}
		}
		/*
		 * First extrema is a minima.
		 */
		else {
			// Symmetric section is too small
			if ((le.localMaxima().peek().time())
					- le.localMinima().peek().time() < le.localMinima().peek()
					.time()
					- residual.peek().time()) {
				symFrontMaximaAlongOrigin(residual, le, location);
				symFrontMinimaAlongOrigin(residual, le, location);
			}
			// Symmetric section is long enough
			else {
				symFrontMaximaAlongMinima(residual, le, location);
				symFrontMinimaAlongMinima(residual, le, location);
			}
			// residual[0] is a maxima, sym along residual[0]
		}
	}

	// private void symFrontEnvelope(LinkedList<Data> residual, LocalExtremas
	// le,
	// int location) {
	// /*
	// * First extrema is a maxima.
	// */
	// if (le.localMaxima().peek().time() < le.localMinima().peek().time()) {
	// // residual[0] is not a minima.
	// if (residual.peek().value() > le.localMinima().peek().value()) {
	// // Symmetric section is too small
	// if ((2 * le.localMaxima().peek().time())
	// - le.localMinima().peek().time() > residual.peek()
	// .time()) {
	// symFrontMaximaAlongOrigin(residual, le, location);
	// symFrontMinimaAlongOrigin(residual, le, location);
	// }
	// // Symmetric section is long enough
	// else {
	// symFrontMaximaAlongMaxima(residual, le, location);
	// symFrontMinimaAlongMaxima(residual, le, location);
	// }
	// }
	// // residual[0] is a minima, sym along residual[0]
	// else {
	// symFrontMaximaAlongOrigin(residual, le, location);
	// symFrontMinimaOriginNewMinima(residual, le, location);
	// }
	// }
	// /*
	// * First extrema is a minima.
	// */
	// else {
	// // residual[0] is not a maxima.
	// if (residual.peek().value() < le.localMaxima().peek().value()) {
	// // Symmetric section is too small
	// if ((2 * le.localMinima().peek().time())
	// - le.localMaxima().peek().time() > residual.peek()
	// .time()) {
	// symFrontMaximaAlongOrigin(residual, le, location);
	// symFrontMinimaAlongOrigin(residual, le, location);
	// }
	// // Symmetric section is long enough
	// else {
	// symFrontMaximaAlongMinima(residual, le, location);
	// symFrontMinimaAlongMinima(residual, le, location);
	// }
	// }
	// // residual[0] is a maxima, sym along residual[0]
	// else {
	// symFrontMaximaOriginNewMaxima(residual, le, location);
	// symFrontMinimaAlongOrigin(residual, le, location);
	// }
	// }
	// }

	private void symFrontMaximaOriginNewMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		double time = residual.peek().time();
		double value = residual.peek().value();
		le.localMaxima().addFirst(new Data(time, value));
		for (int i = 0; i < location; i++) { // Ensure the copy is one less then
												// other situation.
			time = 2 * residual.peek().time() - le.localMaxima().get(i).time();
			value = le.localMaxima().get(i).value();
			le.localMaxima().addFirst(new Data(time, value));
		}
	}

	private void symFrontMinimaOriginNewMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		double minimatime = residual.peek().time();
		double minimavalue = residual.peek().value();
		le.localMinima().addFirst(new Data(minimatime, minimavalue));
		for (int i = 0; i < location; i++) { // Ensure the copy is one less then
												// other situation.
			minimatime = 2 * residual.peek().time()
					- le.localMinima().get(i).time();
			minimavalue = le.localMinima().get(i).value();
			le.localMinima().addFirst(new Data(minimatime, minimavalue));
		}
	}

	private void symFrontMaximaAlongMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double maximatime = 2 * le.localMinima().peek().time()
					- le.localMaxima().get(i).time();
			double maximavalue = le.localMaxima().get(i).value();
			le.localMaxima().addFirst(new Data(maximatime, maximavalue));
		}
	}

	private void symFrontMinimaAlongMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			// Here to ensure not to include the first minima.
			double minimatime = 2 * le.localMinima().peek().time()
					- le.localMinima().get(i + 1).time();
			double minimavalue = le.localMinima().get(i + 1).value();
			le.localMinima().addFirst(new Data(minimatime, minimavalue));
		}
	}

	private void symFrontMaximaAlongMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			// Here to ensure not to include the first maxima.
			double maximatime = 2 * le.localMaxima().peek().time()
					- le.localMaxima().get(i + 1).time();
			double maximavalue = le.localMaxima().get(i + 1).value();
			le.localMaxima().addFirst(new Data(maximatime, maximavalue));
		}
	}

	private void symFrontMinimaAlongMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double minimatime = 2 * le.localMaxima().peek().time()
					- le.localMinima().get(i).time();
			double minimavalue = le.localMinima().get(i).value();
			le.localMinima().addFirst(new Data(minimatime, minimavalue));
		}
	}

	private void symFrontMaximaAlongOrigin(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double maximatime = 2 * residual.peek().time()
					- le.localMaxima().get(i).time();
			double maximavalue = le.localMaxima().get(i).value();
			le.localMaxima().addFirst(new Data(maximatime, maximavalue));
		}
	}

	private void symFrontMinimaAlongOrigin(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double minimatime = 2 * residual.peek().time()
					- le.localMinima().get(i).time();
			double minimavalue = le.localMinima().get(i).value();
			le.localMinima().addFirst(new Data(minimatime, minimavalue));
		}
	}

	/*
	 * Symmetric Rear Envelopes
	 */

	private void symRearEnvelope(LinkedList<Data> residual, LocalExtremas le,
			int location) {
		/*
		 * Last extrema is a maxima.
		 */
		if (le.localMaxima().peekLast().time() > le.localMinima().peekLast()
				.time()) {
			// Symmetric section is too small
			if (le.localMaxima().peekLast().time()
					- le.localMinima().peekLast().time() < residual.peekLast()
					.time() - le.localMaxima().peekLast().time()) {
				symRearMaximaAlongEnd(residual, le, location);
				symRearMinimaAlongEnd(residual, le, location);
			}
			// Symmetric section is long enough
			else {
				symRearMinimaAlongMaxima(residual, le, location);
				symRearMaximaAlongMaxima(residual, le, location);
			}
		}
		/*
		 * Last extrema is a minima.
		 */
		else {
			// Symmetric section is too small
			if (le.localMinima().peekLast().time()
					- le.localMaxima().peekLast().time() < residual.peekLast()
					.time()-le.localMinima().peekLast().time()) {
				symRearMaximaAlongEnd(residual, le, location);
				symRearMinimaAlongEnd(residual, le, location);
			}
			// Symmetric section is long enough
			else {
				symRearMaximaAlongMinima(residual, le, location);
				symRearMinimaAlongMinima(residual, le, location);
			}
		}
	}

	// private void symRearEnvelope(LinkedList<Data> residual, LocalExtremas le,
	// int location) {
	// /*
	// * Last extrema is a maxima.
	// */
	// if (le.localMaxima().peekLast().time() > le.localMinima().peekLast()
	// .time()) {
	// // residual[n] is not a minima.
	// if (residual.peekLast().value() > le.localMinima().peekLast()
	// .value()) {
	// // Symmetric section is too small
	// if ((2 * le.localMaxima().peekLast().time())
	// - le.localMinima().peekLast().time() < residual
	// .peekLast().time()) {
	// symRearMaximaAlongEnd(residual, le, location);
	// symRearMinimaAlongEnd(residual, le, location);
	// }
	// // Symmetric section is long enough
	// else {
	// symRearMaximaAlongMaxima(residual, le, location);
	// symRearMinimaAlongMaxima(residual, le, location);
	// }
	// }
	// // residual[n] is a minima, sym along residual[n]
	// else {
	// symRearMaximaAlongEnd(residual, le, location);
	// symRearMinimaEndNewMinima(residual, le, location);
	// }
	// }
	// /*
	// * Last extrema is a minima.
	// */
	// else {
	// // residual[n] is not a maxima.
	// if (residual.peekLast().value() < le.localMaxima().peekLast()
	// .value()) {
	// // Symmetric section is too small
	// if ((2 * le.localMinima().peekLast().time())
	// - le.localMaxima().peekLast().time() > residual.peek()
	// .time()) {
	// symRearMaximaAlongEnd(residual, le, location);
	// symRearMinimaAlongEnd(residual, le, location);
	// }
	// // Symmetric section is long enough
	// else {
	// symRearMaximaAlongMinima(residual, le, location);
	// symRearMinimaAlongMinima(residual, le, location);
	// }
	// }
	// // residual[n] is a maxima, sym along residual[n]
	// else {
	// symRearMaximaEndNewMaxima(residual, le, location);
	// symRearMinimaAlongEnd(residual, le, location);
	// }
	// }
	// }

	private void symRearMaximaEndNewMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		double time = residual.peekLast().time();
		double value = residual.peekLast().value();
		le.localMaxima().addLast(new Data(time, value));
		for (int i = 0; i < location; i++) { // Ensure the copy is one less then
												// other situation.
			time = 2
					* residual.peekLast().time()
					- le.localMaxima().get(le.localMaxima().size() - 1 - i)
							.time();
			value = le.localMaxima().get(le.localMaxima().size() - 1 - i)
					.value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}

	private void symRearMinimaEndNewMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		double time = residual.peekLast().time();
		double value = residual.peekLast().value();
		le.localMinima().addLast(new Data(time, value));
		for (int i = 0; i < location - 1; i++) { // Ensure the copy is one less
													// then other situation.
			time = 2
					* residual.peekLast().time()
					- le.localMinima().get(le.localMinima().size() - 1 - i)
							.time();
			value = le.localMinima().get(le.localMinima().size() - 1 - i)
					.value();
			le.localMinima().addFirst(new Data(time, value));
		}
	}

	private void symRearMaximaAlongMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			// Here to ensure not to include the first maxima.
			double time = 2
					* le.localMaxima().peekLast().time()
					- le.localMaxima().get(le.localMaxima().size() - i - 2)
							.time();
			double value = le.localMaxima()
					.get(le.localMaxima().size() - i - 2).value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}

	private void symRearMinimaAlongMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double time = 2
					* le.localMaxima().peekLast().time()
					- le.localMinima().get(le.localMinima().size() - 1 - i)
							.time();
			double value = le.localMinima()
					.get(le.localMinima().size() - 1 - i).value();
			le.localMinima().addLast(new Data(time, value));
		}
	}

	private void symRearMaximaAlongMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double time = 2
					* le.localMinima().peekLast().time()
					- le.localMaxima().get(le.localMaxima().size() - 1 - i)
							.time();
			double value = le.localMaxima()
					.get(le.localMaxima().size() - 1 - i).value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}

	private void symRearMinimaAlongMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			// Here to ensure not to include the first minima.
			double time = 2
					* le.localMinima().peekLast().time()
					- le.localMinima().get(le.localMinima().size() - i - 2)
							.time();
			double value = le.localMinima()
					.get(le.localMinima().size() - i - 2).value();
			le.localMinima().addLast(new Data(time, value));
		}
	}

	private void symRearMinimaAlongEnd(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double time = 2
					* residual.peekLast().time()
					- le.localMinima().get(le.localMinima().size() - 1 - i)
							.time();
			double value = le.localMinima()
					.get(le.localMinima().size() - 1 - i).value();
			le.localMinima().addLast(new Data(time, value));
		}
	}

	private void symRearMaximaAlongEnd(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double time = 2
					* residual.peekLast().time()
					- le.localMaxima().get(le.localMaxima().size() - 1 - i)
							.time();
			double value = le.localMaxima().get(i).value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}

	/*
	 * Use interpolation and extremes to retrieve envelope.
	 */

	private void calculateEnvelope(LinkedList<Data> envelope, CubicSpline CS,
			LinkedList<Data> residual) {
		Iterator<Data> it = residual.iterator();
		while (it.hasNext()) {
			double time = it.next().time();
			double value = CS.interpolate(time);
			envelope.add(new Data(time, value));
		}
	}
}
