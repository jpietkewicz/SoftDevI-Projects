package application;

import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Login page for inputting MySQL user name and password. Parameters page for
 * inputting web page to pull words from and constrain by number of entries to
 * return.
 * 
 * @author Jordan
 *
 */
public class Login extends Application {
	Database d = new Database();
	Words w = new Words(d);
	int numToPrint;

	/**
	 * Shows login page for database user name and password
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Word Occurences - Login");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);

		Label usernameLabel = new Label("MYSQL User:");
		grid.add(usernameLabel, 0, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label passwordLabel = new Label("Password:");
		grid.add(passwordLabel, 0, 2);

		PasswordField passwordBox = new PasswordField();
		grid.add(passwordBox, 1, 2);

		Button loginButton = new Button("Log in");
		loginButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				d.setDB_USER(userTextField.getText().trim());
				d.setDB_PASS(passwordBox.getText());

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

					@Override
					public void handle(ActionEvent arg0) {
						if (entriesTextField.getText().isEmpty()) {
							numToPrint = 0;
						} else {
							numToPrint = Integer.parseInt(entriesTextField.getText().trim());
						}

						try {
							d.createSchema();
							d.createTable();
						} catch (Exception e) {
							e.printStackTrace();
						}

						String url = urlTextField.getText();

						String words = w.removeHTMLTags(url);

						List<String> allWords = w.getAllWords(words);

						Map<String, Integer> wordMap = w.createMap(allWords);

						try {
							w.sortMap(wordMap);

							Results r = new Results(d, numToPrint);
							r.start(primaryStage);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				});

				Scene inputScene = new Scene(grid, 300, 275);

				primaryStage.setTitle("Word Occurences - Parameters");
				primaryStage.setScene(inputScene);
				primaryStage.show();
			}

		});
		HBox buttonBox = new HBox(10);
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.getChildren().add(loginButton);
		grid.add(buttonBox, 1, 4);

		primaryStage.show();
	}

}
