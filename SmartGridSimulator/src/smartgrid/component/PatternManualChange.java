package smartgrid.component;

public class PatternManualChange implements Pattern {

	@Override
	public double getValue(double variation, double[][] attribute,
			long sections, long sectionnumber) {
		//attribute[change occurrence section][change value]
		
		int interval;
		for(interval = 0 ; interval < attribute.length ; interval++){
			if(sectionnumber < attribute[interval][0])
				break;
			else
				continue;
		}
		
		if(interval < 1){
			return 0;
		}
		return attribute[interval-1][1];
	}

}
