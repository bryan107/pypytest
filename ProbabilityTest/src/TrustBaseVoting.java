
public class TrustBaseVoting {

	/**
	 * @param args
	 */
	static int samplesize = 10;
	static double p = 0.3; 
	static int networksize = 11;
	static double pff = 1, pgf = 1, pfg = 1, pgg = 1;
	static double PFF = 1, PGF = 1, PFG = 1, PGG = 1;
	static int k = networksize - 1;
	static int m;
	public static void main(String[] args) {

		checkM();	
		calcpff();
		calcpgf();
		calcpfg();
		calcpgg();
		printp();
		
		PFG = p * calcDifferLeftBody() + pfg * calcRightBody();
		PGF = (1-p) * calcDifferLeftBody() + pgf * calcRightBody();
		PGG = (1-p) * calcSameLeftBody() + pgg * calcRightBody();
		PFF = p * calcSameLeftBody() + pff * calcRightBody();
		System.out.println();
		System.out.println("======TOTAL=====");
		printP();
	
		
		double CPFF = PFF/(PFF+PFG);
		double CPFG = PFG/(PFF+PFG);
		double CPGG = PGG/(PGG+PGF);
		double CPGF = PGF/(PGG+PGF);
//		double CGGG = CPFF + CPGG;
		
		System.out.println("EVENT");
		double FCDF = 0;
		for(int i = m ; i <=k ; i++){
			FCDF += Combin(k,i) * Math.pow(CPFG, i) * Math.pow(CPFF, k - i);
			System.out.println("PF" + i + ": " + FCDF * 100);
		}
		
		double falsenegetiveevent = 0;
		for(int i = 0 ; i <= samplesize ; i++){
			falsenegetiveevent += Combin(samplesize, i)*Math.pow(FCDF, i)*Math.pow(1-FCDF, samplesize -i);
			System.out.println("False Positive [" + i + "] " + (1-falsenegetiveevent) * 100);
		}
		
		FCDF = 0;
		for(int i = m ; i <=k ; i++){
			FCDF += Combin(k,i) * Math.pow(CPGF, i) * Math.pow(CPGG, k - i);
			System.out.println("PF" + i + ": " + FCDF * 100);
		}
		
		double falsepositiveevent = 0;
		for(int i = 0 ; i <= samplesize ; i++){
			falsepositiveevent += Combin(samplesize, i)*Math.pow(FCDF, i)*Math.pow(1-FCDF, samplesize -i);
			System.out.println("False Positive [" + i + "] " + (1- falsepositiveevent) * 100);
		}
		
		

		
//		System.out.println("PF");
//		double CDF = 0;
//		for(int i = 0 ; i <= samplesize ; i++){
//			CDF += Combin(samplesize,i)*Math.pow(CPFG, i)*Math.pow(CPFF, samplesize-i);
//			System.out.println("PF" + i + ": " + CDF*100);
//		}
//		System.out.println("PG");
//		CDF = 0;
//		for(int i = 0 ; i <= samplesize ; i++){
//			CDF += Combin(samplesize,i)*Math.pow(CPGF, i)*Math.pow(CPGG, samplesize-i);
//			System.out.println("PG" + i + ": " + CDF*100);
//		}
		

	}

	private static void printP() {
		System.out.println("PFF: " + PFF);
		System.out.println("PGG: " + PGG);
		System.out.println("PFG: " + PFG);
		System.out.println("PGF: " + PGF);
		System.out.println("Totoal: " + (PFF + PFG + PGF + PGG));
		System.out.println("GD:" + (PFF + PGG));
	}

	private static void printp() {
		System.out.println("Pff: " + pff);
		System.out.println("Pgf: " + pgf);
		System.out.println("Pfg: " + pfg);
		System.out.println("Pgg: " + pgg);
		System.out.println("Totoal: " + (pff + pfg + pgf + pgg));
	}

	private static double calcSameLeftBody() {
		double temp = 0;
		for(int x = 1 ; x <= k ; x++){
			int n = checkN(x);
			double temp2 = 0, temp3 = 0;
			for(int z = 0 ; z < n ; z++){
				temp2 += Combin(x, z) * Math.pow(pgg, (x-z)) * Math.pow(pfg, z);
			}
			for(int a = 0 ; a <= k-x ; a++){
				temp3 += Combin(k-x, a) * Math.pow(pgf, a) * Math.pow(pff, (k-x-a));
			}
			temp += Combin(k,x) * temp2 * temp3;
		}
		return temp;
	}
	
	private static double calcRightBody() {
		double temp4 = 0;
		for(int a = 0; a <=k ; a++){
			temp4 += Combin(k, a) * Math.pow(pgf, a) * Math.pow(pff, k-a);
		}
		return temp4;
	}

	private static double calcDifferLeftBody() {
		double temp = 0;
		for(int x = 1 ; x <= k ; x++){
			int n = checkN(x);
			double temp2 = 0, temp3 = 0;
			if(x % 2 ==0){
				for(int y = 0 ; y < n - 1 ; y++){
					temp2 += Combin(x, y) * Math.pow(pgg, y) * Math.pow(pfg, (x-y));
				}
			}
			else{
				for(int y = 0 ; y < n ; y++){
					temp2 += Combin(x, y) * Math.pow(pgg, y) * Math.pow(pfg, (x-y));
				}
			}

			for(int a = 0 ; a <= k-x ; a++){
				temp3 += Combin(k-x, a) * Math.pow(pgf, a) * Math.pow(pff, (k-x-a));
			}
			
			temp += Combin(k,x) * temp2 * temp3;
		}
		return temp;
	}

	private static int checkN(int x){
		if(x%2 == 0)
			return (x/2)+1;
		else
			return (x+1)/2;
	}
	private static void calcpgg() {
		double temp = 0;
		for(int i = 0 ; i < m ; i++){
			temp += Combin(k, i) * Math.pow((1-p), k-i) * Math.pow(p, i);
		}
		pgg = (1-p) * temp;
	}

	private static void calcpfg() {
		double temp = 0;
		if((k % 2) == 0){
			for(int i = 0 ; i < m - 1 ; i++){
				temp += Combin(k, i) * Math.pow((1-p), i) * Math.pow(p, (k-i));
			}
		}
		else{
			for(int i = 0 ; i < m ; i++){
				temp += Combin(k, i) * Math.pow((1-p), i) * Math.pow(p, (k-i));
			}
		}

		pfg = p * temp;
	}

	private static void calcpgf() {
		double temp = 0;
		if ((k % 2) == 0){
			for(int i = 0 ; i < m -1 ; i++){
				temp += Combin(k, i) * Math.pow((1-p), i) * Math.pow(p, (k-i));
			}
		}
		else{
			for(int i = 0 ; i < m ; i++){
				temp += Combin(k, i) * Math.pow((1-p), i) * Math.pow(p, (k-i));
			}
		}

		pgf = (1-p) * temp;
	}

	private static void calcpff() {
		double temp = 0;
		for(int i = 0 ; i < m ; i++){
			temp += Combin(k, i) * Math.pow((1-p), k-i) * Math.pow(p, i);
		}
		pff = p * temp;
	}



	private static void checkM() {
		if ((k % 2) == 0)
			m = (k/2) + 1;
		else
			m = (k+1)/2;
	}

	
	
	static public double Combin(int n, int k){
		double numerator = 1;
		double denominator = 1;
		if(k > n){
			System.out.println("Illigel attribute in combination !");
			return 0;
		}
		for(int i = n ; i > k ; i--){
			numerator *= i;
		}
		for(int i = n-k ; i > 0 ; i--){
			denominator *= i;
		}
		return numerator/denominator;
	}
}
