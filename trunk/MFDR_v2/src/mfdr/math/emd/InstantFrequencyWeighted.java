package mfdr.math.emd;

public class InstantFrequencyWeighted implements InstantFrequency {
	
	double W4, W2, W1;
	
	public InstantFrequencyWeighted(double W4, double W2, double W1) {
		updateWeights(W4, W2, W1);
	}
	
	public void updateWeights(double W4, double W2, double W1){
		this.W4 = W4;
		this.W2 = W2;
		this.W1 = W1;
	}
	
	@Override
	public double calFrequency(double T4, double T2_1, double T2_2,
			double T1_1, double T1_2, double T1_3, double T1_4) {
		return (W4/(4*(T4)) + (W2/(2*T2_1)) + (W2/(2*T2_2)) + (W1/(T1_1)) + (W1/(T1_2)) + (W1/(T1_3)) + (W1/(T1_4))) / (W4 + (2*W2) + (4*W1));
	}

}
