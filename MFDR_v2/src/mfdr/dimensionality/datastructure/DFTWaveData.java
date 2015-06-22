package mfdr.dimensionality.datastructure;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DFTWaveData {

	private final double amplitude, phasedelay, freq;
	private static Log logger = LogFactory.getLog(DFTWaveData.class);
	
	public DFTWaveData(double amplitude, double phasedelay, double freq){
		this.amplitude = amplitude;
		this.phasedelay = phasedelay;
		this.freq = freq;
	}
	
	public double amplitude(){
		return this.amplitude;
	}
	
	public double phaseDelay(){
		return this.phasedelay;
	}
	
	public double freq(){
		return this.freq;
	}
	
	public double getCosAmplitude(){
		return amplitude*Math.cos(phasedelay);
	}
	
	public double getSinAmplitude(){
		return amplitude*Math.sin(phasedelay);
	}
	/**
	 * Only one window use this
	 * @param index
	 * @param length
	 * @return
	 */
	public double getWaveValue(int index, int length){
		if(length < 2*freq){
			logger.info("time length too small for freq" + freq);
			return 0;
		}
		return amplitude*Math.cos(( 2 * Math.PI * freq *index/ length  + phasedelay));
	}
	
	/**
	 * @param x
	 * @param length: size of time series
	 * @param windownum: Window number start from 1
	 * @return
	 */
	public double getWaveValue(double x, int length, int windownum){
		if(length < 2*freq){
			logger.info("time length too small for freq" + freq);
			return 0;
		}
		if(windownum < 1){
			logger.info("Window Number too small: " + freq);
			return 0;
		}
		int window_length = length/windownum;
		return amplitude*Math.cos(( 2 * Math.PI * freq * (window_length*(windownum-1)+x)/ length  + phasedelay));
	}
	
	/**
	 * Get the angle of with a given x and window number
	 * Please be aware that 'x' is not the original index.
	 * It range from 0 to l-1, where l denotes window size.
	 * @param x
	 * @param length
	 * @param windownum
	 * @return current angle.
	 */
	public double getAngle(double x, int length, int windownum){
		return g(length)*x + k(length, windownum);
	}
	
	public double getAngle(int index, int windowsize, int length){
		int x = index % windowsize;
		int windownum = length/windowsize;
		return g(length)*x + k(length, windownum);
	}
	
	public double g(int length){
		return 2*Math.PI*freq/length;
	}
	
	public double k(int length, int windownum){
		return phasedelay + g(length)*length - g(length)*(length/windownum);
	}
	
	
	
}
