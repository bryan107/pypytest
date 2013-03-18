package test;


import stat.PCA;
import flanagan.analysis.Stat;
import flanagan.math.Matrix;

public class TestPCA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PCA pca;
		double mean = 20;
		double[][] reading = new double[2][30];
		double[][] covariance = new double[2][2];
		generateData(mean, reading);

		pca = new PCA(reading);
		
		double[] point = {22, 19};
		
		double[][] eigenvector = pca.getEigenVector();
		double[] eigenvalue = pca.getEigenValues();
		double[] rotatevalue = pca.getRotatedValue(point);
		double[] deviation = pca.getDeviations();
		System.out.println("Length: " + eigenvector.length);
		System.out.println("LLength: " + eigenvalue.length );
		
		System.out.println("Results:");
		System.out.println("[" +  eigenvector[0][0] + " , " + eigenvector[0][1] + "]");
		System.out.println(eigenvalue[0]);
		System.out.println("[" +  eigenvector[1][0] + " , " + eigenvector[1][1] + "]");
		System.out.println(eigenvalue[1]);
		
		System.out.println("Deviations: [" +  deviation[0] + " , " + deviation[1] + "]");
		
		System.out.println("Rotated:");
		System.out.println("[" +  rotatevalue[0] + " , " + rotatevalue[1] + "]");

	}

	private static void generateData(double mean, double[][] reading) {
		for(int j = 0 ; j < 30 ; j++){	
			reading[0][j] = (double) (mean + 2 * Math.random());
		}
		for(int j = 0 ; j < 30 ; j++){	
			reading[1][j] = (double) (mean + 2 * Math.random());
		}

	}

}
