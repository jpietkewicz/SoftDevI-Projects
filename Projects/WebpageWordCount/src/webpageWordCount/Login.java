package webpageWordCount;

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

public class Login extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		WebpageWordCount wwc = new WebpageWordCount();

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
				WebpageWordCount.setDB_USER(userTextField.getText().trim());
				WebpageWordCount.setDB_PASS(passwordBox.getText());

				try {
					wwc.start(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		HBox buttonBox = new HBox(10);
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.getChildren().add(loginButton);
		grid.add(buttonBox, 1, 4);

		primaryStage.show();
	}

}
