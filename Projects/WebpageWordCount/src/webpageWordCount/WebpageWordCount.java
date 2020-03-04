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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WebpageWordCount extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Button startButton = new Button("Let's do this!");
		startButton.setAlignment(Pos.CENTER);
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				LinkedHashMap<String, Integer> sortedMap = getResults();
				String resultsText = "";

				int i = 0, numToPrint = 20;
				for (Entry<String, Integer> e : sortedMap.entrySet()) {
					if (i++ < numToPrint) {
						resultsText += (i + " - " + e.getKey() + "\n");
					}
				}

				Text results = new Text();
				results.setText(resultsText);

				StackPane root2 = new StackPane();
				root2.getChildren().add(results);
				Scene resultsScene = new Scene(root2, 300, 350);
				primaryStage.setScene(resultsScene);
				primaryStage.show();
			}

		});

		StackPane root = new StackPane();
		root.getChildren().add(startButton);

		Scene scene = new Scene(root, 300, 100);
		primaryStage.setTitle("Word Occurences GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	static LinkedHashMap<String, Integer> getResults() {
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		// Gets text with StringBean (no tags)!!
		StringBean sb = new StringBean();
		sb.setURL("http://shakespeare.mit.edu/macbeth/full.html");
		String content = sb.getStrings();

		// split by all whitespace, take out '-' and downcast capitals, add all words to
		// one list
		List<String> allWords = new ArrayList<>();
		for (String s : content.split("\\s+")) {
			s = s.replaceAll("[^a-zA-Z]", "").toLowerCase();
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
		wordMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

		return sortedMap;
	}

	public static void main(String[] args) {

		launch(args);
	}

}
