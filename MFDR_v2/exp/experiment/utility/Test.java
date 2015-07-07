package experiment.utility;

import java.util.LinkedList;

import mfdr.datastructure.Data;
import mfdr.datastructure.TimeSeries;
import mfdr.file.FileAccessAgent;
import mfdr.utility.File;
import junit.framework.TestCase;

public class Test extends TestCase {

//	public void testGG(){
//		FileAccessAgent fagent = new FileAccessAgent("C:\\TEST\\MDFR\\Null.txt", "C:\\TEST\\MDFR\\Data\\dataset\\50words\\50words_TEST");
//		int count = 0 ;
//		int linecount = 0;
//		String line = fagent.readLineFromFile();
//		linecount++;
////		if(line == null)
////			break;
//		System.out.println(line);
//		String[] split = line.split(" ");
//		TimeSeries ts = new TimeSeries();
//		for(int i = 0 ; i < split.length ; i++){
//			split[i].replaceAll("\\s", "");
//			if(!split[i].equals("")){
//				count++;
//				String[] split2 = split[i].split("e");
//				double test = Double.valueOf(split2[0])*Math.pow(10, Double.valueOf(split2[1]));
//				ts.add(new Data(count,test));
//			}
//		}
//		System.out.println("Line Count:" + linecount);
//		System.out.println("Count:" + count );
//	}
	
	public void testWholeFile(){
		LinkedList<String> filenamelist = new LinkedList<String>();
		FileAccessAgent fagent = new FileAccessAgent("C:\\TEST\\MDFR\\Null.txt", "C:\\TEST\\MDFR\\Null.txt");
		fagent.updatereadingpath("C:\\TEST\\MDFR\\Data\\dataset2\\dataset2_list.txt");
		while(true){
			String filename = fagent.readLineFromFile();
			if(filename == null){
				break;
			}
			filenamelist.add(filename);
		}
		for(int i = 0 ; i < filenamelist.size() ; i++){
			String filename = filenamelist.get(i);
			fagent.updatereadingpath("C:\\TEST\\MDFR\\Data\\dataset2\\"+ filename +"\\"+ filename +"_TEST");
			DataParser parser = new DataParser(new UCRData(), fagent);
			LinkedList<TimeSeries> ts= new LinkedList<TimeSeries>();
			int count = 0;
			while(true){
				count ++;
				TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
				if(temp == null)
					break;
				ts.add(temp);
				File.getInstance().saveLinkedListToFile("Series " + count, temp, "C:\\TEST\\MDFR\\Data\\DATA5\\"+ filename +".csv");
			}
		}

	
	}
	
	public void testSingle(){
		FileAccessAgent fagent = new FileAccessAgent("C:\\TEST\\MDFR\\Null.txt", "C:\\TEST\\MDFR\\Null.txt");
		fagent.updatereadingpath("C:\\TEST\\MDFR\\Data\\TRY.csv");
		DataParser parser = new DataParser(new OneLineOneData(), fagent);
		TimeSeries temp = parser.getTimeSeriesDetails().timeSeries();
		File.getInstance().saveLinkedListToFile("USD/EUR", temp, "C:\\TEST\\MDFR\\Data\\DATA5\\TRY.csv");
	}
}
