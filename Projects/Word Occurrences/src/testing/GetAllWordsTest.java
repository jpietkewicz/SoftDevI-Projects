package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import application.Words;

class GetAllWordsTest {

	@Test
	void test() {

		Words object = new Words();

		List<String> wordsTest = new ArrayList<>();
		wordsTest.add("the");
		wordsTest.add("quick");
		wordsTest.add("brown");
		wordsTest.add("fox");
		wordsTest.add("jumped");
		wordsTest.add("over");
		wordsTest.add("the");
		wordsTest.add("lazy");
		wordsTest.add("dog");

		String words = "THe qUICK brown fo)x jum}ped ov0er t-he l'azy dog";

		List<String> allWords = object.getAllWords(words);

		assertEquals(wordsTest, allWords);

	}

}

