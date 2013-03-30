package gad.calc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flanagan.analysis.Stat;
import flanagan.math.Matrix;

public class PCA {
	private double[][] covariance;
	private Matrix m;
	private static Log logger = LogFactory.getLog(PCA.class);
	private static PCA self = new PCA();
	
	private PCA(){
		
	}
	
	public static PCA getInstance(){
		return self;
	}
	
	public void setReading(double[][] reading){	
		covariance = new double[reading.length][reading.length]; 
		for(int i = 0 ; i < reading.length ; i++){
			for(int j = 0 ; j < reading.length ; j++){
				covariance[i][j] = Stat.covariance(reading[i], reading[j]);
				
			}
		}
		m = new Matrix(covariance);
	}
	
	public double[][] getEigenVector(){
		return m.getEigenVector();
	}
	
	public double[] getEigenValues(){
		return m.getEigenValues();	
	}
	
	public double[] getDeviations(){
		double[] eigenvalues = getEigenValues();
		double[] deviation = new double[eigenvalues.length];
		for(int i = 0 ; i < deviation.length ; i++){
			deviation[i] = Math.sqrt(eigenvalues[i]);
		}
		return deviation;
	}
	
	public double[] getRotatedValue(double[] value){
		double[] rotatedvalue = new double[value.length];
		for(int i = 0 ; i < rotatedvalue.length ; i++){
			rotatedvalue[i] = 0;
		}
		double[][] eigenvector = getEigenVector();
		if(value.length != eigenvector[0].length){
			logger.error("Vector dimention does not match");
			return null;
		}
		else{
			for(int i = 0 ; i < value.length ; i++){
				for(int j = 0 ; j < value.length ; j++){
					rotatedvalue[i] += (value[j] * eigenvector[i][j]);
				} 
			}
		}
		return rotatedvalue;
		
	}

}
