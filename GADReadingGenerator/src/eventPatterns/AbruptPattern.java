package eventPatterns;

public class AbruptPattern implements Pattern {
	@Override
	public double getCorrespondReading(double variation, double abruptionround,
			long sections, long sectionnumber) {
		if (sectionnumber < abruptionround) {
			return 0;
		} else {
			return variation;
		}
	}

}
