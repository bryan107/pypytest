package fileAccessInterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileAccessAgent {
	String writingpath;
	String readingpath;
	BufferedReader in;
	private static Log logger = LogFactory.getLog(FileAccessAgent.class);
	
	public FileAccessAgent(String writingpath, String readingpath){
		updatereadingpath(readingpath);
		updatewritingpath(writingpath);
	}
	
	public void updatereadingpath(String readingpath){
		this.readingpath = readingpath;
	}
	
	public void updatewritingpath(String writingpath){
		this.writingpath = writingpath;
	}
	public boolean setFileReader(){
		try {
			in = new BufferedReader(new FileReader(readingpath));
			return true;
		} catch (FileNotFoundException e) {
			logger.error("File " + readingpath + " not found");
			e.printStackTrace();
		}
		return false;
	}
	
	public String readLineFromFile(){
		String str;
		try {
			if((str = in.readLine()) != null){
				return str;
			}
		} catch (FileNotFoundException e) {
			logger.error("File " + readingpath + " not found");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IO failure when read line from " + readingpath);
			e.printStackTrace();
		}
		return null;
	}
	
	public String readLineFromFile(String fileaddress){
		this.readingpath = fileaddress;
		return readLineFromFile();
	}
	
	public boolean closeFileReader(){
		try {
			in.close();
			return true;
		} catch (IOException e) {
			logger.error("IO failure when close file " + readingpath);
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean writeLineToFile(String outputstring) {
		try {
			FileWriter writer = new FileWriter(writingpath, true);
			writer.write(outputstring + "\n");
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean writeLineToFile(String outputstring, String fileaddress){
		this.writingpath = fileaddress;
		return writeLineToFile(outputstring);
	}
}
