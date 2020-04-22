package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Shows results of database query in UI
 * 
 * @author Jordan
 *
 */
public class Results extends Application {

	private Database d;
	private int numToPrint;

	Results(Database d, int numToPrint) {
		this.d = d;
		this.numToPrint = numToPrint;
	}

	/**
	 * Shows results (from database) in UI window
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		String resultsText = "";

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = d.getConnection();
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

}
