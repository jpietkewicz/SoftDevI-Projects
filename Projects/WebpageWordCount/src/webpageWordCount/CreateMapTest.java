package webpageWordCount;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CreateMapTest {

	@Test
	void test() {

		WebpageWordCount object = new WebpageWordCount();
		
		Map<String, Integer> mapTest = new HashMap<>();
		mapTest.put("the", 3);
		mapTest.put("xyz", 1);
		mapTest.put("abc", 2);
		
		List<String> allWords = new ArrayList<>();
		allWords.add("the");
		allWords.add("the");
		allWords.add("the");
		allWords.add("abc");
		allWords.add("abc");
		allWords.add("xyz");

		Map<String, Integer> wordMap = object.createMap(allWords);
		
		assertEquals(mapTest, wordMap);
	}

}
