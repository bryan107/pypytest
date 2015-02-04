package mdfr.math.emd;

import java.util.LinkedList;

import flanagan.interpolation.CubicSpline;

public class CalculateEnvelopes {

	private static CalculateEnvelopes self = new CalculateEnvelopes();

	private CalculateEnvelopes() {

	}

	public static CalculateEnvelopes getInstance() {
		return self;
	}

	public Envelopes getEnvelopes(LinkedList<Data> residual, LocalExtremas le) {
		Envelopes envelopes = new Envelopes(new LinkedList<Data>(),
				new LinkedList<Data>());
		int location = 1;
		// TODO Test extraplotation
		// 0. Use symmetric extremes to complete Extrapolation.
		symFrontEnvelope(residual, le, location);
		symRearEnvelope(residual, le, location);
		// TODO the following codes are needed to be updated.

		// 1. Convert Data from linked list to array
		double[] values = LinkedListToArray(residual);
		double[] upperextremas = LinkedListToArray(le.localMaxima());
		double[] lowerextremas = LinkedListToArray(le.localMinima());

		// 2. Prepare value array for interpolation.
		double[] uppervalues = extractValues(values, upperextremas);
		double[] lowervalues = extractValues(values, lowerextremas);

		// 3. Do Cubic Spline Interpolation

		CubicSpline lowerCS = new CubicSpline(lowerextremas, lowervalues);
		// Calculate upperenvelope
		calculateEnvelop(envelopes.upperEnvelope(), values, upperextremas,
				uppervalues);
		// Calculate lowerenvelope
		calculateEnvelop(envelopes.lowerEnvelope(), values, lowerextremas,
				lowervalues);
		return envelopes;
	}

	/*
	 * Symmetric Front Envelopes
	 * */
	
	private void symFrontEnvelope(LinkedList<Data> residual, LocalExtremas le,
			int location) {
		/*
		 * First extrema is a maxima.
		 */
		if (le.localMaxima().peek().time() < le.localMinima().peek().time()) {
			// residual[0] is not a minima.
			if (residual.peek().value() > le.localMinima().peek().value()) {
				// Symmetric section is too small
				if ((2 * le.localMaxima().peek().time())
						- le.localMinima().peek().time() > residual
						.peek().time()) {
					symFrontMaximaAlongOrigin(residual, le, location);
					symFrontMinimaAlongOrigin(residual, le, location);
				}
				// Symmetric section is long enough
				else {
					symFrontMaximaAlongMaxima(residual, le, location);
					symFrontMinimaAlongMaxima(residual, le, location);
				}
			}
			// residual[0] is a minima, sym along residual[0]
			else {
				symFrontMaximaAlongOrigin(residual, le, location);
				symFrontMinimaOriginNewMinima(residual, le, location);
			}
		}
		/*
		 * First extrema is a minima.
		 */
		else {
			// residual[0] is not a maxima.
			if (residual.peek().value() < le.localMaxima().peek().value()) {
				// Symmetric section is too small
				if ((2 * le.localMinima().peek().time())
						- le.localMaxima().peek().time() > residual
						.peek().time()) {
					symFrontMaximaAlongOrigin(residual, le, location);
					symFrontMinimaAlongOrigin(residual, le, location);
				}
				// Symmetric section is long enough
				else {
					symFrontMaximaAlongMinima(residual, le, location);
					symFrontMinimaAlongMinima(residual, le, location);
				}
			}
			// residual[0] is a maxima, sym along residual[0]
			else {
				symFrontMaximaOriginNewMaxima(residual, le, location);
				symFrontMinimaAlongOrigin(residual, le, location);
			}
		}
	}

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
		for (int i = 0; i < location ; i++) {
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
		for (int i = 0; i < location + 1; i++) {
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
	 * */
	
	private void symRearEnvelope(LinkedList<Data> residual, LocalExtremas le,
			int location) {
		/*
		 * Last extrema is a maxima.
		 */
		if (le.localMaxima().peekLast().time() > le.localMinima().peekLast().time()) {
			// residual[n] is not a minima.
			if (residual.peekLast().value() > le.localMinima().peekLast().value()) {
				// Symmetric section is too small
				if ((2 * le.localMaxima().peekLast().time())
						- le.localMinima().peekLast().time() < residual.peekLast().time()) {
					symRearMaximaAlongEnd(residual, le, location);
					symRearMinimaAlongEnd(residual, le, location);
				}
				// Symmetric section is long enough
				else {
					symRearMaximaAlongMaxima(residual, le, location);
					symRearMinimaAlongMaxima(residual, le, location);
				}
			}
			// residual[n] is a minima, sym along residual[n]
			else {
				symRearMaximaAlongEnd(residual, le, location);
				symRearMinimaEndNewMinima(residual, le, location);
			}
		}
		/*
		 * Last extrema is a minima.
		 */
		else {
			// residual[n] is not a maxima.
			if (residual.peekLast().value() < le.localMaxima().peekLast().value()) {
				// Symmetric section is too small
				if ((2 * le.localMinima().peekLast().time())
						- le.localMaxima().peekLast().time() > residual.peek().time()) {
					symRearMaximaAlongEnd(residual, le, location);
					symRearMinimaAlongEnd(residual, le, location);
				}
				// Symmetric section is long enough
				else {
					symRearMaximaAlongMinima(residual, le, location);
					symRearMinimaAlongMinima(residual, le, location);
				}
			}
			// residual[n] is a maxima, sym along residual[n]
			else {
				symRearMaximaEndNewMaxima(residual, le, location);
				symRearMinimaAlongEnd(residual, le, location);
			}
		}
	}

	private void symRearMaximaEndNewMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		double time = residual.peekLast().time();
		double value = residual.peekLast().value();
		le.localMaxima().addLast(new Data(time, value));
		for (int i = 0; i < location; i++) { // Ensure the copy is one less then
												// other situation.
			time = 2 * residual.peekLast().time() - le.localMaxima().get(le.localMaxima().size()-i).time();
			value = le.localMaxima().get(le.localMaxima().size()-i).value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}
	
	private void symRearMinimaEndNewMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		double time = residual.peekLast().time();
		double value = residual.peekLast().value();
		le.localMinima().addLast(new Data(time, value));
		for (int i = 0; i < location - 1; i++) { // Ensure the copy is one less then other situation.
			time = 2 * residual.peekLast().time()
					- le.localMinima().get(le.localMinima().size()-i).time();
			value = le.localMinima().get(le.localMinima().size()-i).value();
			le.localMinima().addFirst(new Data(time, value));
		}
	}
	
	private void symRearMaximaAlongMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			// Here to ensure not to include the first maxima.
			double time = 2 * le.localMaxima().peekLast().time()
					- le.localMaxima().get(le.localMaxima().size()-i-1).time();
			double value = le.localMaxima().get(le.localMaxima().size()-i-1).value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}

	private void symRearMinimaAlongMaxima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location + 1; i++) {
			double time = 2 * le.localMaxima().peekLast().time()
					- le.localMinima().get(le.localMinima().size()-i).time();
			double value = le.localMinima().get(le.localMinima().size()-i).value();
			le.localMinima().addLast(new Data(time, value));
		}
	}
	
	private void symRearMaximaAlongMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location ; i++) {
			double time = 2 * le.localMinima().peekLast().time()
					- le.localMaxima().get(le.localMaxima().size()-i).time();
			double value = le.localMaxima().get(le.localMaxima().size()-i).value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}

	private void symRearMinimaAlongMinima(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location ; i++) {
			// Here to ensure not to include the first minima.
			double time = 2 * le.localMinima().peekLast().time()
					- le.localMinima().get(le.localMinima().size()-i-1).time();
			double value = le.localMinima().get(le.localMinima().size()-i-1).value();
			le.localMinima().addLast(new Data(time, value));
		}
	}
	
	private void symRearMinimaAlongEnd(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double time = 2 * residual.peekLast().time() - le.localMinima().get(le.localMinima().size() -i).time();
			double value = le.localMinima().get(le.localMinima().size() -i).time();
			le.localMinima().addLast(new Data(time, value));
		}
	}
	
	private void symRearMaximaAlongEnd(LinkedList<Data> residual,
			LocalExtremas le, int location) {
		for (int i = 0; i < location; i++) {
			double time = 2 * residual.peekLast().time() - le.localMaxima().get(le.localMaxima().size() -i).time();
			double value = le.localMaxima().get(i).value();
			le.localMaxima().addLast(new Data(time, value));
		}
	}
	
	// TODO complete calculateEnvelop
	private void calculateEnvelop(LinkedList<Data> envelopes, double[] values,
			double[] upperextremas, double[] uppervalues) {
		CubicSpline CS = new CubicSpline(upperextremas, uppervalues);
		for (int i = 0; i < values.length; i++) {
			// envelopes.add(CS.interpolate(i));
		}
	}

	/*
	 * Extract values from values array according to the index array.
	 */
	private double[] extractValues(double[] values, double[] index) {
		double[] extractions = new double[index.length];
		for (int i = 0; i < extractions.length; i++) {
			extractions[i] = values[(int) index[i]];
		}
		return extractions;
	}

	/*
	 * Convert LinkedList to Array
	 */
	private double[] LinkedListToArray(LinkedList linkedlist) { // LinkedList
																// may be
																// different
																// types
		double[] array = new double[linkedlist.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Double.valueOf(linkedlist.get(i).toString());
		}
		return array;
	}

}
