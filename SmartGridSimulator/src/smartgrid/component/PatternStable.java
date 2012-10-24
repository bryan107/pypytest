package smartgrid.component;

public class PatternStable implements Pattern {

	@Override
	public double getValue(double variation, double[][] attribute, long sections,
			long sectionnumber) {
		return 0;
	}

	@Override
	public String name() {
		return "Stable";
	}

}
