package factiva.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import factiva.utility.FileAccessAgent;
import factiva.utility.Print;
import factiva.utility.PropertyAgent;

public class FactivaRTFParser {
	private static FileAccessAgent agent;
	private static String readingpath, writtingpath;
	private static String highlight_start_tag, highlight_end_tag;
	private static String[] tags;
	private static int header;
	private static int x = 0;
	public static void main(String[] args) {
		// Load Properties
		writtingpath = PropertyAgent.getInstance().getProperties("SETTING", "Writting_Path");
		readingpath = PropertyAgent.getInstance().getProperties("SETTING", "Reading_Path");
		String temptags = PropertyAgent.getInstance().getProperties("SETTING", "Tags");
		highlight_start_tag = PropertyAgent.getInstance().getProperties("SETTING", "Highlight_Start_Tag");
		highlight_end_tag = PropertyAgent.getInstance().getProperties("SETTING", "Highlight_End_Tag");
		header = Integer.valueOf(PropertyAgent.getInstance().getProperties("SETTING", "Header"));
		tags = temptags.split(",");
		agent = new FileAccessAgent(writtingpath, readingpath);
		
		// Save Title Line if set.
		if(header == 1){
			String title = "";
			for(int i = 0 ; i < tags.length ; i++){
				title += tags[i] + ",";
			}
			agent.writeLineToFile(title);
		}
		
		// Output Start Message
		Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println("START @ " + sdf.format(cal.getTime()));
		
		// Initiate pool objects
		Map<String, String> map = new HashMap<String, String>();
		Container c = new Container();
		
		// Process Each Line 
		while(true){
			String temp = agent.readLineFromFile();
			// Break if exhaust the file
			if(temp == null){
				break;
			}
			// ************* Debug code ***********
//			if(x%500 ==0){
//				System.out.println("500");
//			}
			// ************************************
			
			// Extract meaningful tags and contents.
			LinkedList<String> keywords = extractKeyWorkds(temp);
			// If contain keywords, remove keyword tags from temp
			if(!keywords.isEmpty()){
				temp = removeKeyWordsTags(temp);
			}
			LinkedList<String> line = extracLine(temp);
			// Iterate through line
			Iterator<String> it = line.iterator();
			while(it.hasNext()){
				String string = it.next();
				// If IS tag
				if(isTag(string)){
					// String is an END Tag
					if(isEndTag(string)){
						// Store previous container
						map.put(c.tag(), c.content());
						// update with current tag
						c.updateTag(getTag(string));
						c.clearContent();
						c.attachContent(it.next());
						// Store AN container
						map.put(c.tag(), c.content());
						// Store map
						storeMap(map);
						c.clear();
						continue;
					}
					// If container has tag
					else if(c.hasTag()){
						// If duplicate to Map
						if(map.containsKey(c.tag())){
							storeMap(map);
						}
						map.put(c.tag(), c.content());
					}
					// Store tag in container
					c.updateTag(getTag(string));
					// As new tag occurred, removed previous err contents.
					c.clearContent();
				}
				// If NOT Tag
				else{
					// Store content to container.
					if(c.hasTag()){
						// If c is a content container
						if(isContentTag(c.tag())){
							Iterator<String> it_keywords = keywords.iterator();
							while (it_keywords.hasNext()) {
								String keyword = (String) it_keywords.next();
								// If string contains any of keywords, attach content otherwise does not attach.
								if(string.contains(keyword)){
									c.attachContent(string);
									break;
								}
							}
						}else{
							c.attachContent(string);
						}
					}
						
				}
			}
//			System.out.print("MAP: ");
//			Print.getInstance().printStringMap(map);
//			System.out.println();
		}
		System.out.println("COMPLETE @" + sdf.format(cal.getTime()));
		System.out.println(x + " entities has been stored in " + writtingpath);
	}

	private static void storeMap(Map<String, String> map){
		String saveline = "" ;
		System.out.print("STORE NEWS: ");
		Print.getInstance().printStringMap(map);
		for(int i = 0 ; i < tags.length ; i++){
			String tostore;
			if(map.get(tags[i]) == null){
				tostore = "";
			}else{
				tostore = map.get(tags[i]);
			}
			saveline += "\"" + symbolCompansate(tostore) + "\"" + ",";
		}
		System.out.println();
		agent.writeLineToFile(saveline);
		map.clear();
		x++;
	}
	
	private static boolean isContentTag(String string){
		for(int i = 0 ; i < tags.length ; i++){
			if(string.contains("LP") || string.contains("TD") && string.length()<4){
				return true;
			}
		}
		return false;
	}
	
	private static String symbolCompansate(String string){
		String newstring = "";
		if(string == null || string.isEmpty() ){
			return string;
		}
		String[] temp = string.split("\"");
		newstring += temp[0]; 
		for(int i = 1 ; i < temp.length ; i++){
			newstring += "\"\"" + temp[i];
		}
		return newstring;
	}
	
	private static boolean isTag(String string){
		for(int i = 0 ; i < tags.length ; i++){
			if(string.contains(tags[i]) && string.length()<5){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isEndTag(String string){
		for(int i = 0 ; i < tags.length ; i++){
			if(string.contains("AN") && string.length()<4){
				return true;
			}
		}
		return false;
	}
	
	private static String getTag(String string){
		for(int i = 0 ; i < tags.length ; i++){
			if(string.contains(tags[i])){
				return tags[i];
			}
		}
		return null;
	}
	
	/*
	 * Extract TAGs and CONTENTs from line x
	 */
	private static LinkedList<String> extracLine(String temp) {
		LinkedList<String> line = new LinkedList<String>();
		
//		System.out.println("LINE[" + x + "]"  + temp);
//		System.out.print("LABELS: ");
		String linetemp = "";
		int i = 0;
		while(i < temp.length()){
			int j;
			if(temp.charAt(i) == '{'){
				// Store
				if(linetemp.length() > 1)
					line.add(linetemp);
				linetemp = "";
				// Loop to find end
				for(j = i + 1 ; j < temp.length() && temp.charAt(j)!= '}' ; j++){
//					System.out.print(""+temp.charAt(j));
				}
				// Output
//				System.out.print("; ");
				i = j+1;
			}else if(temp.charAt(i) == '\\'){
				// Store
				if(linetemp.length() > 1)
					line.add(linetemp);
				linetemp = "";
				// Loop to find end
				for(j = i + 1 ; j < temp.length() && temp.charAt(j)!= '\\' && temp.charAt(j)!=' ' ; j++){
//					System.out.print(""+temp.charAt(j));
				}
				// Output
//				System.out.print("; ");
				i = j;
			}else{
				linetemp += temp.charAt(i);
				i++;
			}
		}
		// Output 
//		System.out.println();
		// If TAG ig content, but labels in this para do not contain highlights
		
		line.add(linetemp);
		// Print Results
//		System.out.print("OUTPUT["+ line.size() + "]: ");
//		Print.getInstance().printStringList(line, 10000);
		
		return line;
	}
	
	private static LinkedList<String> extractKeyWorkds(String temp){
		LinkedList<String> keyworkds = new LinkedList<String>();
		LinkedList<String> rawkeyword = new LinkedList<String>();
		int init = 0;
		int hl_location_s = temp.indexOf(highlight_start_tag);
		int hl_location_e = temp.indexOf(highlight_end_tag);
		// TODO a bug is here 
		// If exists orphan hl_location_e
		if(hl_location_e > 0 && (hl_location_e < hl_location_s || hl_location_s < 0)){
			rawkeyword = extracLine(temp.substring(0,  hl_location_e));
			if(rawkeyword.isEmpty()){
				System.out.println("Error occurs when extract highlights");
			}
		}
		
		// If temp contains highlights
		while(temp.indexOf(highlight_start_tag, init) > 0){
			// Retrive highlight start and end locations
			hl_location_s = temp.indexOf(highlight_start_tag, init);
			hl_location_e = temp.indexOf(highlight_end_tag, hl_location_s + 1);
			// Extract keyword from highlight loaction
			if(temp.indexOf(highlight_end_tag, hl_location_s + 1) > 0){
				// If contains end
				rawkeyword = extracLine(temp.substring(hl_location_s, hl_location_e));
				init=  hl_location_e + 1;
			}else{
				// If contains no end
				rawkeyword = extracLine(temp.substring(hl_location_s, temp.length()));
				if(rawkeyword.isEmpty()){
					System.out.println("Error occurs when extract highlights");
				}
				init=  temp.length() + 1;
			}
			// Accumulate keyword
			String keyword = rawkeyword.peek();
			// Attach keyword to return pool - keywords.
			keyworkds.add(keyword);
		}
		return keyworkds;
	}

	private static String removeKeyWordsTags(String temp){
		// Remove front highlight tags
		String processed = temp.replace(highlight_start_tag, "");
		// Remove rare highlight tags
		processed = processed.replace(highlight_end_tag, "");
		return processed;
	}
}
