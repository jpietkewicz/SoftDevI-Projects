import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;

public class WebpageWordCount {
	
	/**
	 * -main gets all text from webpage (including HTML tags)
	 * 1. research ways to get without tags
	 * 2. add try/catch
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
//		// get text with BufferedReader (still shows tags)
//		List<String> wordsWithHTML = new ArrayList<>();
//		URL macbeth = new URL("http://shakespeare.mit.edu/macbeth/full.html");
//        BufferedReader in = new BufferedReader(new InputStreamReader(macbeth.openStream()));
//        String inputLine;
//        while ((inputLine = in.readLine()) != null) {
//            wordsWithHTML.add(inputLine);
//        }
//        in.close();
		
		// Gets text with StringBean (no tags)!! 
		StringBean sb = new StringBean();
		sb.setURL("http://shakespeare.mit.edu/macbeth/full.html");
		String content = sb.getStrings();
		List<String> allWords = new ArrayList<>();
		for (String s : content.split(" ")) {
			allWords.add(s);
		}
		
		System.out.println(allWords);
		//System.out.println(content);
	}

}
