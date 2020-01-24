import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.htmlparser.beans.StringBean;

// class/sortByValues method found at https://www.techiedelight.com/sort-map-by-values-java/
// changed to work with my specific code
//Custom Comparator to sort the map according to the natural 
//ordering of its values
class CustomComparator<String,Integer extends Comparable> implements Comparator<String>
{
	private Map<String,Integer> map;

	public CustomComparator(Map<String,Integer> map) {
		this.map = new HashMap<>(map);
	}

	@Override
	public int compare(String s1, String s2) {
		return map.get(s2).compareTo(map.get(s1));
	}
}

public class WebpageWordCount {
	
	// Generic function to sort Map by values using TreeMap
		public static  <String, Integer> Map<String, Integer> sortByValues(Map<String, Integer> map)
		{
			Comparator<String> comparator = new CustomComparator(map);

			Map<String,Integer> sortedMap = new TreeMap<>(comparator);
			sortedMap.putAll(map);

			return sortedMap;
		}
	
	/**
	 * -main gets all text from web page, separates into individual words while
	 * clearing out single characters and bringing everything to lower case to compare
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<String> allWords = new ArrayList<>();

		// Gets text with StringBean (no tags)!! 
		StringBean sb = new StringBean();
		sb.setURL("http://shakespeare.mit.edu/macbeth/full.html");
		String content = sb.getStrings();
		
		// split by all whitespace, take out '-' and downcast capitals, add all words to one list
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
			}
			else {
				Integer count = wordMap.get(s);
				count++;
				wordMap.replace(s, count);
			}
		}
		
		// sort by value
		wordMap = sortByValues(wordMap);
		
		System.out.println("Top 20 words in Macbeth (by amount of appearances)");
		System.out.println("--------------------------------------------------");
		
		int j = 0;
		for (String s : wordMap.keySet()) {
				System.out.println(j+1 + ". " + s);
				j++;
				if (j >= 20) {
					break;
				}
		}
	}

}
