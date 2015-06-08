package mfdr.dimensionality.datastructure;

import java.util.LinkedList;

public class MFDRData {
	private final LinkedList<PLAData> trends;
	private final NewDFTData seasonal;
	private final double noise_energy_density;
	
	public MFDRData(LinkedList<PLAData> trends, NewDFTData seasonal, double noise_energy_density){
		this.trends = trends;
		this.seasonal = seasonal;
		this.noise_energy_density = noise_energy_density;
	}
	
	public LinkedList<PLAData> trends(){
		return this.trends;
	}
	
	public NewDFTData seasonal(){
		return this.seasonal;
	}
	
	public double noiseEnergyDensity(){
		return this.noise_energy_density;
	}
	
}
