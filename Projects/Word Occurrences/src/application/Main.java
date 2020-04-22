package application;
	
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launches application to get webpage word occurrences
 * 
 * @author Jordan
 * @version 2.0
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Login log = new Login();
			log.start(primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
