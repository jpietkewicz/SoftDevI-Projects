package webpageWordCount;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.htmlparser.beans.StringBean;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * Application that counts word occurences of a particular webpage and orders by
 * number of appearances
 * 
 * @author Jordan Pietkewicz
 *
 */
public class WebpageWordCount extends Application {

	/**
	 * Creates form to input URL and number of words to get, then changes to display
	 * results
	 * 
	 * @param primaryStage Stage to add Scenes to
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label urlLabel = new Label("URL:");
		grid.add(urlLabel, 0, 0);

		TextField urlTextField = new TextField("http://shakespeare.mit.edu/macbeth/full.html");
		grid.add(urlTextField, 1, 0, 2, 1);

		Label entriesLabel = new Label("Entries:");
		grid.add(entriesLabel, 0, 1);

		TextField entriesTextField = new TextField("20");
		grid.add(entriesTextField, 1, 1);

		Button startButton = new Button("Get results!");
		grid.add(startButton, 1, 2);
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * @param event ActionEvent of startButton being pressed
			 */
			@Override
			public void handle(ActionEvent event) {
				String url = urlTextField.getText();

				String words = removeHTMLTags(url);

				List<String> allWords = getAllWords(words);

				Map<String, Integer> wordMap = createMap(allWords);

				LinkedHashMap<String, Integer> sortedMap = sortMap(wordMap);

				String resultsText = "";

				int i = 0, numToPrint;

				if (entriesTextField.getText().isEmpty()) {
					numToPrint = 0;
				} else {
					numToPrint = Integer.parseInt(entriesTextField.getText());
				}

				for (Entry<String, Integer> e : sortedMap.entrySet()) {
					if (i++ < numToPrint) {
						resultsText += (i + " - " + e.getKey() + "\n");
					}
				}

				Text results = new Text();
				results.setText(resultsText);

				BorderPane root2 = new BorderPane();
				root2.setCenter(results);

				Scene resultsScene = new Scene(root2);

				primaryStage.setTitle("Word Occurences - Results");
				primaryStage.setScene(resultsScene);
				primaryStage.show();
			}

		});

		Scene inputScene = new Scene(grid);

		primaryStage.setTitle("Word Occurences - Parameters");
		primaryStage.setScene(inputScene);
		primaryStage.show();
	}

	/**
	 * Gets text from webpage at specified url with StringBean (no tags!!)
	 * 
	 * @param url URL of webpage to get text from
	 * @return String containing all words from webpage with no HTML tags
	 * @see <a href="http://htmlparser.sourceforge.net/">HTML Parser</a>
	 */
	String removeHTMLTags(String url) {
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
	List<String> getAllWords(String words) {
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
	Map<String, Integer> createMap(List<String> allWords) {
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
	 * Sorts Map by Value from highest to lowest
	 * 
	 * @param wordMap Map of word Key and number of occurrences Value
	 * @return Map sorted by Value from highest to lowest
	 * @see <a href="https://www.techiedelight.com/sort-map-by-values-java/">Sort
	 *      Map by Values in Java</a>
	 */
	LinkedHashMap<String, Integer> sortMap(Map<String, Integer> wordMap) {
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		wordMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

		return sortedMap;
	}

	/**
	 * Launches application to count word occurences
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
