package data_reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

public class datareader{
	private static long[] count1 = new long[100];
	private static long[] count2 = new long[100];
	private static long[] count3 = new long[100];
	public static void main(String[] args){
        BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("D:\\Message Testing Data\\Test1\\Simple_Message_Log.txt"));
	        String str;
	        long now_number = 0;
	        long number;
	        int i = -1;
	        for(int j = 0 ; j < 25 ; j++){
	        	count1[j] = 0;
	        	count2[j] = 0;
	        	count3[j] = 0;
	        }
	        long time_1 = new Date().getTime();
	        while ((str = in.readLine()) != null) {
	           String subs1 = str.substring(0, 13);
	           String subs2 = str.substring(14);
//	           System.out.println(subs1);
//	           System.out.println(subs2);
	           number = Long.parseLong(subs1);
	           if((number/3600000) > now_number){
	        	   now_number = (number/3600000);   
	        	   i++;
	           }
	        	   
	           if(subs2.equalsIgnoreCase("ssh.RAW_DATA"))
	        	   count1[i] ++;
	           else if(subs2.equalsIgnoreCase("ssh.CONTEXT"))
	        	   count2[i] ++;
	           else if(subs2.equalsIgnoreCase("ssh.COMMAND"))
	        	   count3[i] ++;    	        	   
	        }
	        long time_2 = new Date().getTime();
	        long time_3 = time_2 - time_1;
	        in.close();		
	        
	        for(i = 0 ; i < 100 ; i++){
	        	System.out.println("The " + (i+1) + "th hour:  " + "Raw Data=" + count1[i] + " Context=" + count2[i] + " Command=" + count3[i]);
	        	FileWriter writer = new FileWriter(
						"D:\\Message Testing Data\\Test1\\Messagr Number.txt", true);
				writer.write(i + "\t" + count1[i] +  "\t" + count2[i] +"\t" + count3[i] + "\n");
				writer.close();
	        }
	        	
	        System.out.println("Process Time: " + time_3);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}