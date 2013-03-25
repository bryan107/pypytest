package eventPatterns;

public class RandomPattern implements Pattern {

	@Override
	public double getCorrespondReading(double variation, double attribute, long sections,
			long sectionnumber) {
		return (variation * Math.random());
	}

}
