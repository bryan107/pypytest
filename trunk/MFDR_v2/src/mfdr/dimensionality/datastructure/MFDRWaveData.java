package mfdr.dimensionality.datastructure;

import java.util.LinkedList;

import mfdr.dimensionality.reduction.DFTWave;

public class MFDRWaveData {
	private final LinkedList<PLAData> trends;
	private final LinkedList<DFTWaveData> seasonal;
	private final double noise_energy_density;
	
	public MFDRWaveData(LinkedList<PLAData> trends, LinkedList<DFTWaveData> seasonal, double noise_energy_density){
		this.trends = trends;
		this.seasonal = seasonal;
		this.noise_energy_density = noise_energy_density;
	}
	
	public LinkedList<PLAData> trends(){
		return this.trends;
	}
	
	public LinkedList<DFTWaveData> seasonal(){
		return this.seasonal;
	}
	
	public double noiseEnergyDensity(){
		return this.noise_energy_density;
	}
	
}
