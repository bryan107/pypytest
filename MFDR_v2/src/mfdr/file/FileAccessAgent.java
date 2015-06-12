package mfdr.file;

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
	
	/*
	 * Constructor
	 */
	public FileAccessAgent(String writingpath, String readingpath){
		updatereadingpath(readingpath);
		updatewritingpath(writingpath);
	}
	
	/*
	 * Update WRITING and READING path.
	 */
	public boolean updatereadingpath(String readingpath){
		this.readingpath = readingpath;
		return setFileReader();
	}
	
	public String readingPath(){
		return this.readingpath;
	}

	public boolean setFileReader(){
		try {
			in = new BufferedReader(new FileReader(readingpath));
			logger.info("File reading path has been set to: " + readingpath);
			return true;
		} catch (FileNotFoundException e) {
			logger.error("File " + readingpath + " not found");
			e.printStackTrace();
		}
		return false;
	}
	
	public void updatewritingpath(String writingpath){
		this.writingpath = writingpath;
	}
	
	/*
	 * Read line from the set file.	
	 */
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
	
	/*
	 * Change reading file address and read
	 */
	public String readLineFromFile(String fileaddress){
		updatereadingpath(fileaddress);
		return readLineFromFile();
	}
	
	/*
	 * Close file reader must SET before next read.
	 */
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
	
	/*
	 * Write line to the current writing address
	 */
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
	
	/*
	 * Update the current writing path and write a line.
	 */
	public boolean writeLineToFile(String outputstring, String fileaddress){
		this.writingpath = fileaddress;
		return writeLineToFile(outputstring);
	}
}
