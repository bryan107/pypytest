package mfdr.math.trigonometric;

import mfdr.dimensionality.reduction.DFT;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Wave {

	private final double energy, phasedelay, freq;
	private static Log logger = LogFactory.getLog(Wave.class);
	
	public Wave(double energy, double phasedelay, double freq){
		this.energy = energy;
		this.phasedelay = phasedelay;
		this.freq = freq;
	}
	
	public double energy(){
		return this.energy;
	}
	
	public double phaseDelay(){
		return this.phasedelay;
	}
	
	public double freq(){
		return this.freq;
	}
	
	public double getValue(int index, int length){
		if(length < 2*freq){
			logger.info("time length too small for freq" + freq);
			return 0;
		}
		return energy*Math.cos((freq * 2 * (index + phasedelay) * Math.PI / length));
	}
}
