package application;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.htmlparser.beans.StringBean;

/**
 * Obtains and splits words from webpage into map and sorts map (while inserting
 * to database)
 * 
 * @author Jordan
 *
 */
public class Words {
	private Database d;

	public Words() {
	}

	public Words(Database d) {
		this.d = d;
	}

	/**
	 * Gets text from webpage at specified url with StringBean (no tags!!)
	 * 
	 * @param url URL of webpage to get text from
	 * @return String containing all words from webpage with no HTML tags
	 * @see <a href="http://htmlparser.sourceforge.net/">HTML Parser</a>
	 */
	public String removeHTMLTags(String url) {
		String words = null;

		StringBean sb = new StringBean();
		sb.setURL(url);

		words = sb.getStrings();

		return words;
	}

	/**
	 * Splits String created by removeHTMLTags() by all whitespace, takes out
	 * hypens, and downcasts capitals. Populates a List with those words.
	 * 
	 * @param words String containing all words from webpage with no HTML tags
	 * @return List of all words from webpage
	 */
	public List<String> getAllWords(String words) {
		List<String> allWords = new ArrayList<>();

		for (String s : words.split("\\s+")) {
			s = s.replaceAll("[^a-zA-Z]", "").toLowerCase();
			if (!(s.isEmpty())) {
				allWords.add(s);
			}
		}

		return allWords;
	}

	/**
	 * Puts all words from List into Map along with a count of how many times each
	 * occurs.
	 * 
	 * @param allWords List of all words from webpage
	 * @return Map of word Key and number of occurrences Value
	 */
	public Map<String, Integer> createMap(List<String> allWords) {
		Map<String, Integer> wordMap = new HashMap<>();

		for (String s : allWords) {
			if (!wordMap.containsKey(s)) {
				wordMap.put(s, 1);
			} else {
				Integer count = wordMap.get(s);
				count++;
				wordMap.replace(s, count);
			}
		}

		return wordMap;
	}

	/**
	 * Sorts Map by Value from highest to lowest and inserts into mysql database
	 * 
	 * @param wordMap Map of word Key and number of occurrences Value
	 * @return Map sorted by Value from highest to lowest
	 * @see <a href="https://www.techiedelight.com/sort-map-by-values-java/">Sort
	 *      Map by Values in Java</a>
	 */
	public LinkedHashMap<String, Integer> sortMap(Map<String, Integer> wordMap) throws Exception {
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		wordMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = d.getConnection();
			stmt = conn.createStatement();

			System.out.print("Inserting map... ");
			int i = 1;
			for (Entry<String, Integer> e : sortedMap.entrySet()) {
				String sql = "INSERT INTO word (id, word, count) " + "VALUES(" + i++ + ",\"" + e.getKey() + "\","
						+ e.getValue() + ")";
				stmt.executeUpdate(sql);
			}
			System.out.println("Inserted.");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			stmt.close();
			conn.close();
		}

		return sortedMap;
	}

}
