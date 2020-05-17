package photos.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class launches the application
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class Photos extends Application {

	/**
	 * This starts the application
	 * @param stage this is the main stage the application begins on
	 */
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/photos/view/login.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * This calls the start of the program
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
