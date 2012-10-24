package smartgrid.component;

public class PatternRandom implements Pattern {

	@Override
	public double getValue(double variation, double[][] attribute, long sections,
			long sectionnumber) {
		return (variation * Math.random());
	}

	@Override
	public String name() {
		return "Random";
	}

}
