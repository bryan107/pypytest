package mfdr.dimensionality.datastructure;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Wave {

	private final double amplitude, phasedelay, freq;
	private static Log logger = LogFactory.getLog(Wave.class);
	
	public Wave(double amplitude, double phasedelay, double freq){
		this.amplitude = amplitude;
		this.phasedelay = phasedelay;
		this.freq = freq;
	}
	
	public double energy(){
		return this.amplitude;
	}
	
	public double phaseDelay(){
		return this.phasedelay;
	}
	
	public double freq(){
		return this.freq;
	}
	
	public double getCosValue(int index, int length){
		if(length < 2*freq){
			logger.info("time length too small for freq" + freq);
			return 0;
		}
		return amplitude*Math.cos((freq * 2 * (index + phasedelay) * Math.PI / length));
	}
	
	
}
