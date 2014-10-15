
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int num = 16;
		int[] input = new int[num];
		for(int i = 0 ; i < num ; i++){
			input[i] = (int)(Math.random()*100);
//			if(Math.random() > 0.5){
//				input[i] = 1;
//			}
//			else{
//				input[i] = 0;
//			}
		}
		for(int i = 0 ; i < input.length ; i++){
			System.out.print(input[i] + " ");
		}
		System.out.println();
		int[] result = discreteHaarWaveletTransform(input);
		System.out.println("RESULT");
		for(int i = 0 ; i < result.length ; i++){
			System.out.print(result[i] + " ");
		}
			
		
	}
	
	public static int[] discreteHaarWaveletTransform(int[] input) {
		System.out.println("PROCESS");
	    // This function assumes that input.length=2^n, n>1
	    int[] output = new int[input.length];
	 
	    for (int length = input.length >> 1; ; length >>= 1) {
	        // length = input.length / 2^n, WITH n INCREASING to log(input.length) / log(2)
	        for (int i = 0; i < length; ++i) {
	            int sum = (int) ((input[i * 2] + input[i * 2 + 1])/Math.pow(2, 0.5));
	            int difference = (int) ((input[i * 2] - input[i * 2 + 1])/Math.pow(2, 0.5));
	            output[i] = sum;
	            output[length + i] = difference;
	        }
	        if (length == 2) {
	            return output;
	        }
	        
			for(int i = 0 ; i < output.length ; i++){
				System.out.print(output[i] + " ");
			}
			System.out.println();
	        //Swap arrays to do next iteration
	        System.arraycopy(output, 0, input, 0, length << 1);
	    }
	}

}
