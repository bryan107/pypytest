package experiment.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import mfdr.datastructure.TimeSeries;
import mfdr.file.FileAccessAgent;

public class RepresentationErrorAnalysis {

	public void getAverageRunTime(String address, String filename, FileAccessAgent fagent, String filelistaddress){
		LinkedList<String> filenamelist = getFileNameList(fagent, filelistaddress);	
		Map<String, Double> timemap = new HashMap<String, Double>();
		Map<String, Map<String, Result>> map = getResultMap(address, filename);
		for(int i = 0 ; i < filenamelist.size() ; i++){
			String name = filenamelist.get(i);
			double[] timelist = {0,0,0,0,0,0,0,0};
			for(int j = 2 ; j <=10 ; j++){
//				map.get(name+"["+j+"]").;
			}
			
		}
	}
	
	public LinkedList<String> getFileNameList(FileAccessAgent fagent,String filelistaddress){
		LinkedList<String> filenamelist = new LinkedList<String>();
		fagent.updatereadingpath(filelistaddress);
		while(true){
			String filename = fagent.readLineFromFile();
			if(filename == null){
				break;
			}
			filenamelist.add(filename);
		}
		return filenamelist;
	}
	
	public Map<String, Map<String, Result>> getResultMap(String address, String filename) {
		Map<String, Map<String, Result>> results = new HashMap<String, Map<String, Result>>();
		FileAccessAgent fagent = new FileAccessAgent(null, address + filename
				+ "\\" + filename + ".csv");
		while (true) {
			String line = fagent.readLineFromFile();
			if(line == null){
				break;
			}
			String[] linesplit = line.split(",");
			Map<String, Result> temp = new HashMap<String, Result>();
			for (int i = 0; i < 8; i++) {
				temp.put(
						linesplit[i * 7 + 2],
						new Result(linesplit[0], linesplit[i * 7 + 2], Integer
								.valueOf(linesplit[1]), Double
								.valueOf(linesplit[i * 7 + 4]), Double
								.valueOf(linesplit[i * 7 + 6]), Double
								.valueOf(linesplit[i * 7 + 8])));
			}
			results.put(linesplit[0] + linesplit[1], temp);
		}
		return results;
	}
}
