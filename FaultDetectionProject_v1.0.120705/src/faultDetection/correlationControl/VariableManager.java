package faultDetection.correlationControl;

public class VariableManager {

	private static VariableManager self = new VariableManager();
	private int samplesize;
	private double DFDThreshold;
	private double CSErrorTolerance;
	
	
	private VariableManager(){
		
	}
	public static VariableManager getInstance(){
		return self;
	}
}
