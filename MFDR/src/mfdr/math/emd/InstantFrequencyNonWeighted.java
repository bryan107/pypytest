package mfdr.math.emd;

public class InstantFrequencyNonWeighted implements InstantFrequency {
	
	public InstantFrequencyNonWeighted() {
	}
	
	@Override
	public double calFrequency(double T4, double T2_1, double T2_2,
			double T1_1, double T1_2, double T1_3, double T1_4) {
		return (1/(4*T4) + 1/(2*T2_1) + 1/(2*T2_2) + 1/(T1_1) + 1/(T1_2) + 1/(T1_3) + 1/(T1_4)) / 7;
	}
	
	

}
