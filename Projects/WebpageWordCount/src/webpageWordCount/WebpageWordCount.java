package webpageWordCount;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.htmlparser.beans.StringBean;

class WebpageWordCount {

	public static void main(String[] args) {

		// Gets text with StringBean (no tags)!!
		StringBean sb = new StringBean();
		sb.setURL("http://shakespeare.mit.edu/macbeth/full.html");
		String content = sb.getStrings();

		// split by all whitespace, take out '-' and downcast capitals, add all words to
		// one list
		List<String> allWords = new ArrayList<>();
		for (String s : content.split("\\s+")) {
			s = s.replaceAll("[^a-zA-z]", "").toLowerCase();
			if (!(s.isEmpty())) {
				allWords.add(s);
			}
		}

		// put all words into map and add counts
		Map<String, Integer> wordMap = new HashMap<>();
		for (String s : allWords) {
			if (!(wordMap.containsKey(s))) {
				wordMap.put(s, 1);
			} else {
				Integer count = wordMap.get(s);
				count++;
				wordMap.replace(s, count);
			}
		}

		// Sort map using stream (Java 8)
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
		wordMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

		System.out.println("Words in Macbeth (by number of appearances)");
		System.out.println("-------------------------------------------");
		for (Entry<String, Integer> e : sortedMap.entrySet()) {
			System.out.println(e.getKey() + ": " + e.getValue());
		}

	}

}
