package smartgrid.component;

public class PatternSin implements Pattern {

	@Override
	public double getValue(double variation, double[][] attribute, long sections,
			long sectionnumber) {
		return  variation * Math.sin((double)(2 * Math.PI)*sectionnumber/sections);
	}

}
