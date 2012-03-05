package caculator;
import flanagan.analysis.Regression;
import flanagan.analysis.Stat;


public class CaculateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double[] x = {20,20,20,20,20};
		double[] y = {10,10,10,10,10};
		
		
		
		Regression reg = new Regression(x, y);
		reg.linearGeneral();
		
		
		double var = Stat.standardDeviation(x);
		System.out.println(var);
		
		

		System.out.println("Stop");
	}

}
