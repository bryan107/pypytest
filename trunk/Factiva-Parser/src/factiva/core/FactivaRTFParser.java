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
	// Input parameters
	private static String readingpath, writtingpath;
	private static String highlight_start_tag, highlight_end_tag, end_tag;
	private static String[] tags;
	// Here are function switch
	private static int keyword_extraction;
	private static int header;
	//
	private static int x = 0;

	public static void main(String[] args) {
		// Load Properties
		writtingpath = PropertyAgent.getInstance().getProperties("SETTING",
				"Writting_Path");
		readingpath = PropertyAgent.getInstance().getProperties("SETTING",
				"Reading_Path");
		String temptags = PropertyAgent.getInstance().getProperties("SETTING",
				"Tags");
		highlight_start_tag = PropertyAgent.getInstance().getProperties(
				"SETTING", "Highlight_Start_Tag");
		highlight_end_tag = PropertyAgent.getInstance().getProperties(
				"SETTING", "Highlight_End_Tag");
		end_tag = PropertyAgent.getInstance().getProperties("SETTING",
				"End_Tag");
		keyword_extraction = Integer.valueOf(PropertyAgent.getInstance()
				.getProperties("SETTING", "Keywork_Extraction"));
		header = Integer.valueOf(PropertyAgent.getInstance().getProperties(
				"SETTING", "Header"));
		tags = temptags.split(",");
		agent = new FileAccessAgent(writtingpath, readingpath);

		// Save Title Line if set.
		if (header == 1) {
			String title = "";
			for (int i = 0; i < tags.length; i++) {
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
		int round = 0;
		// Process Each Line
		while (true) {
			System.out.println();
			System.out.println("Round: " + round);
			String temp = agent.readLineFromFile();
			// Break if exhaust the file
			if (temp == null) {
				break;
			}
			// ************* Debug code ***********
//			 if(isContentTag(c.tag())){
//				 System.out.println("500");
//			 }
			// ************************************

			// Extract meaningful tags and contents.
			LinkedList<String> keywords = extractKeyWorkds(temp);
			// If contain keywords, remove keyword tags from temp
			if (!keywords.isEmpty()) {
				temp = removeKeyWordsTags(temp);
			}
			// Extract from input line
			LinkedList<String> tag = extracLine(temp).get("Tags");
			LinkedList<String> content = extracLine(temp).get("Contents");

			// Iterate through line
			Iterator<String> it = content.iterator();
			while (it.hasNext()) {
				String string = it.next();
				// If IS tag
				if (isTag(string)) {
					// String is an END Tag
					if (isEndTag(string)) {
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
					else if (c.hasTag()) {
						// If duplicate to Map
						if (map.containsKey(c.tag())) {
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
				else {
					// Store content to container.
					if (c.hasTag()) {
						// If c is a content container
						if (isContentTag(c.tag())) {
							// If only extract keywords !! Here lays bugs that
							// cannot always extract the exact section.
							if (keyword_extraction == 1) {
								Iterator<String> it_keywords = keywords
										.iterator();
								while (it_keywords.hasNext()) {
									String keyword = (String) it_keywords.next();
									// If string contains any of keywords,
									// attach content otherwise does not attach.
									if (string.contains(keyword)) {
										c.attachContent(string);
										break;
									}
								}
							}else {// Extract all paragraphs
								c.attachContent(string);
							}
						}else{
							c.attachContent(string);
						}
					}
				}
			}
			// System.out.print("MAP: ");
			// Print.getInstance().printStringMap(map);
			// System.out.println();
			round++;
		}
		System.out.println("COMPLETE @" + sdf.format(cal.getTime()));
		System.out.println(x + " entities has been stored in " + writtingpath);
	}

	private static void storeMap(Map<String, String> map) {
		String saveline = "";
		System.out.print("STORE NEWS: ");
		Print.getInstance().printStringMap(map);
		for (int i = 0; i < tags.length; i++) {
			String tostore;
			if (map.get(tags[i]) == null) {
				tostore = "";
			} else {
				tostore = map.get(tags[i]);
			}
			saveline += "\"" + symbolCompansate(tostore) + "\"" + ",";
		}
		System.out.println();
		agent.writeLineToFile(saveline);
		map.clear();
		x++;
	}

	private static boolean isContentTag(String string) {
		for (int i = 0; i < tags.length; i++) {
			if (string.contains("LP") || string.contains("TD")
					&& string.length() < 4) {
				return true;
			}
		}
		return false;
	}

	private static String symbolCompansate(String string) {
		String newstring = "";
		if (string == null || string.isEmpty()) {
			return string;
		}
		String[] temp = string.split("\"");
		newstring += temp[0];
		for (int i = 1; i < temp.length; i++) {
			newstring += "\"\"" + temp[i];
		}
		return newstring;
	}

	private static boolean isTag(String string) {
		for (int i = 0; i < tags.length; i++) {
			if (string.contains(tags[i]) && string.length() < 5) {
				return true;
			}
		}
		return false;
	}

	private static boolean isEndTag(String string) {
		for (int i = 0; i < tags.length; i++) {
			if (string.contains("AN") && string.length() < 4) {
				return true;
			}
		}
		return false;
	}

	private static String getTag(String string) {
		for (int i = 0; i < tags.length; i++) {
			if (string.contains(tags[i])) {
				return tags[i];
			}
		}
		return null;
	}

	/*
	 * Extract TAGs and CONTENTs from line x
	 */
	private static Map<String, LinkedList<String>> extracLine(String temp) {
		LinkedList<String> content = new LinkedList<String>();
		LinkedList<String> tag = new LinkedList<String>();

		String contenttemp = "";
		String tagtemp = "";
		int i = 0;
		while (i < temp.length()) {
			int j;
			// PASS Tags start with '{'
			if (temp.charAt(i) == '{') {
				// Store and Refresh Content Buffer
				if (contenttemp.length() > 1) {
					content.add(contenttemp);
					contenttemp = "";
				}
				tagtemp = "{";
				// Loop to find end
				for (j = i + 1; j < temp.length() && temp.charAt(j) != '}'; j++) {
					tagtemp += temp.charAt(j);
				}
				tagtemp += "}";
				// Output
				tag.add(tagtemp);
				i = j + 1;
			}
			// PASS tags start with '\'
			else if (temp.charAt(i) == '\\') {
				// Store and Refresh Content Buffer
				if (contenttemp.length() > 1) {
					content.add(contenttemp);
					contenttemp = "";
				}
				tagtemp = "\\";
				// Loop to find Tag end
				for (j = i + 1; j < temp.length() && temp.charAt(j) != '\\'
						&& temp.charAt(j) != ' '; j++) {
					tagtemp += temp.charAt(j);
				}
				// Output
				tag.add(tagtemp);
				// Exam if there is next paragraph
				if(tag.size() > 1){
					if(tag.get(tag.size()-1).equals("\\par") &&  tag.get(tag.size()-2).equals("\\par")){
						contenttemp += "\n\n";
					}
				}
				i = j;
			}
			// RECORD non-tag chars
			else {
				contenttemp += temp.charAt(i);
				i++;
			}
		}
		// Output
		// System.out.println();
		// If TAG is content, but labels in this para do not contain highlights

		content.add(contenttemp);

		// Print Results
		System.out.print("Content [" + content.size() + "]: ");
		Print.getInstance().printStringList(content, 10000);
		System.out.print("Tag [" + content.size() + "]: ");
		Print.getInstance().printStringList(tag, 10000);
		HashMap<String, LinkedList<String>> extraction = new HashMap<String, LinkedList<String>>();
		extraction.put("Tags", tag);
		extraction.put("Contents", content);
		return extraction;
	}
	
	private static LinkedList<String> extractKeyWorkds(String temp) {
		LinkedList<String> keyworkds = new LinkedList<String>();
		LinkedList<String> rawkeyword = new LinkedList<String>();
		int init = 0;
		int hl_location_s = temp.indexOf(highlight_start_tag);
		int hl_location_e = temp.indexOf(highlight_end_tag);
		// If exists orphan hl_location_e
		if (hl_location_e > 0
				&& (hl_location_e < hl_location_s || hl_location_s < 0)) {
			rawkeyword = extracLine(temp.substring(0, hl_location_e)).get(
					"Contents");
			if (rawkeyword.isEmpty()) {
				System.out.println("Error occurs when extract highlights");
			}
		}

		// If temp contains highlights
		while (temp.indexOf(highlight_start_tag, init) > 0) {
			// Retrive highlight start and end locations
			hl_location_s = temp.indexOf(highlight_start_tag, init);
			hl_location_e = temp.indexOf(highlight_end_tag, hl_location_s + 1);
			// Extract keyword from highlight loaction
			if (temp.indexOf(highlight_end_tag, hl_location_s + 1) > 0) {
				// If contains end
				rawkeyword = extracLine(
						temp.substring(hl_location_s, hl_location_e)).get(
						"Contents");
				init = hl_location_e + 1;
			} else {
				// If contains no end
				rawkeyword = extracLine(
						temp.substring(hl_location_s, temp.length())).get(
						"Contents");
				if (rawkeyword.isEmpty()) {
					System.out.println("Error occurs when extract highlights");
				}
				init = temp.length() + 1;
			}
			// Accumulate keyword
			String keyword = rawkeyword.peek();
			// Attach keyword to return pool - keywords.
			keyworkds.add(keyword);
		}
		return keyworkds;
	}

	private static String removeKeyWordsTags(String temp) {
		// Remove front highlight tags
		String processed = temp.replace(highlight_start_tag, "");
		// Remove rare highlight tags
		processed = processed.replace(highlight_end_tag, "");
		return processed;
	}
}
