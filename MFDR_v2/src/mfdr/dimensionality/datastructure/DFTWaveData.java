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
	public double getWaveValue(double x, int length, int j, int windowsize){
		if(length < 2*freq){
			logger.info("time length too small for freq" + freq);
			return 0;
		}
		if(j < 1){
			logger.info("Window Number too small: " + freq);
			return 0;
		}
		
//		double tt =  2 * Math.PI * freq * (windowsize*(j-1)+x)/ length  + phasedelay;
//		double ff = getAngle(x, length, j, windowsize);
		
		return amplitude*Math.cos(getAngle(x, length, j, windowsize));
	}
	
	/**
	 * Get the angle of with a given x and window number
	 * Please be aware that 'x' is not the original index.
	 * It range from 0 to windowsize-1. 
	 * j denotes the number of window. range from 1 to m/windowsize
	 * @param x
	 * @param length
	 * @param j
	 * @param windowsize
	 * @return current angle.
	 */
	public double getAngle(double x, int length, int j, int windowsize){
		return g(length)*x + k(length, j, windowsize);
	}
	
	public double getAngle(int index, int windowsize, int length){
		int x = index % windowsize;
		int j = (index / windowsize) + 1;
		return g(length)*x + k(length, j  ,windowsize);
	}
	
	public double g(int length){
		return 2*Math.PI*freq/length;
	}
	
	/**
	 * 
	 * @param tslength : the length of TS
	 * @param j: the index of window
	 * @param l: window size
	 * @return
	 */
	public double k(int tslength, int j , int l){
		double e1 = g(tslength)*l*j ;
		double e2 = g(tslength)*(l);
		return phasedelay + g(tslength)*l*j - g(tslength)*(l);
	}
	
	
	
}
