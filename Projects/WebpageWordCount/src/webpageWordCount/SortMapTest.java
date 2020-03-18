package webpageWordCount;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SortMapTest {

	@Test
	void test() {

		WebpageWordCount object = new WebpageWordCount();
		
		LinkedHashMap<String, Integer> sortedMapTest = new LinkedHashMap<>();
		sortedMapTest.put("abc", 3);
		sortedMapTest.put("123", 2);
		sortedMapTest.put("u&me", 1);

		Map<String, Integer> wordMap = new HashMap<>();
		wordMap.put("abc", 3);
		wordMap.put("u&me", 1);
		wordMap.put("123", 2);
		
		LinkedHashMap<String, Integer> sortedMap = object.sortMap(wordMap);
	
		assertEquals(sortedMapTest, sortedMap);
	}

}
