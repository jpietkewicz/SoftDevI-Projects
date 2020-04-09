package webpageWordCount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/";
	static final String DB_NAME = "WORD_OCCURRENCES";
	
	// MUST ENTER IN CREDENTIALS FOR YOUR COMPUTER
	static final String DB_USER = "root";
	static final String DB_PASS = "";

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

				try {
					sortMap(wordMap);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String resultsText = "";

				int numToPrint;

				if (entriesTextField.getText().isEmpty()) {
					numToPrint = 0;
				} else {
					numToPrint = Integer.parseInt(entriesTextField.getText());
				}

				Connection conn = null;
				Statement stmt = null;

				try {
					conn = getConnection();
					stmt = conn.createStatement();

					String sql = "SELECT * FROM WORD WHERE id <= " + numToPrint;
					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next()) {
						resultsText += rs.getInt(1) + " - " + rs.getString(2) + "\n";
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					try {
						stmt.close();
						conn.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}

				Text results = new Text();
				results.setText(resultsText);

				BorderPane root2 = new BorderPane();
				root2.setCenter(results);

				Scene resultsScene = new Scene(root2, 400, 500);

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
	LinkedHashMap<String, Integer> sortMap(Map<String, Integer> wordMap) throws Exception {
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		wordMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();
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

	/**
	 * Gets connection to schema to execute SQL statements.
	 * 
	 * @return Connection to schema to use for MYSQL Statements
	 * @throws Exception
	 */
	static Connection getConnection() throws Exception {

		try {
			Class.forName(JDBC_DRIVER);

			Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASS);

			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Create schema for word occurrences.
	 * 
	 * @throws Exception
	 */
	static void createSchema() throws Exception {
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);

			System.out.print("Connecting... ");
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			System.out.println("Connected.");

			stmt = conn.createStatement();

			System.out.print("Dropping schema... ");
			String sql = "DROP SCHEMA " + DB_NAME;
			stmt.executeUpdate(sql);
			System.out.println("Dropped.");

			System.out.print("Creating schema... ");
			sql = "CREATE SCHEMA IF NOT EXISTS " + DB_NAME;
			stmt.executeUpdate(sql);
			System.out.println("Schema created.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}

	}

	/**
	 * Create table for storing words and counts
	 * 
	 * @throws Exception
	 */
	static void createTable() throws Exception {
		Connection conn = null;
		Statement stmt = null;

		try {
			System.out.print("Connecting... ");
			conn = getConnection();
			System.out.println("Connected.");

			stmt = conn.createStatement();

			System.out.print("Dropping table... ");
			String sql = "DROP TABLE WORD";
			stmt.executeUpdate(sql);
			System.out.println("Dropped.");

			System.out.print("Creating table... ");
			sql = "CREATE TABLE IF NOT EXISTS WORD " + "(id INTEGER UNSIGNED NOT NULL, " + "word VARCHAR(255), "
					+ "count INTEGER, PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table created.");

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			stmt.close();
			conn.close();
		}
	}

	/**
	 * Creates schema and table for words. Launches application to count word
	 * occurrences.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			createSchema();
			createTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		launch(args);
	}

}
