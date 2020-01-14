import java.util.ArrayList;
import java.util.List;
import org.htmlparser.beans.StringBean;

public class WebpageWordCount {
	
	/**
	 * -main gets all text from web page, separates into individual words while
	 * clearing out single characters and bringing everything to lower case to compare
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		
		// Gets text with StringBean (no tags)!! 
		List<String> allWords = new ArrayList<>();

		StringBean sb = new StringBean();
		sb.setURL("http://shakespeare.mit.edu/macbeth/full.html");
		String content = sb.getStrings();
		
		for (String s : content.split("\\s+")) {
			s = s.replaceAll("[^a-zA-z]", "").toLowerCase();
			if (s.isEmpty()) {
			}
			else {
				allWords.add(s);
			}
		}
		
	}

}
