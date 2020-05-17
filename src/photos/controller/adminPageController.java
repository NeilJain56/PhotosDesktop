package photos.controller;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import photos.model.User;


/**
 * This class controls the admin functionality
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class adminPageController {

	@FXML
	ListView<User> ListView;
	@FXML
	TextField name;

	ObservableList<User> observableList;
	ArrayList<User> users;

	/**
	 * This method initializes all needed data for this class to work
	 * @param users list of all users
	 */
	public void initialize(ArrayList<User> users) {
		if (users != null) {
			this.users = users;
		} else {
			this.users = new ArrayList<User>();
		}
		if (users != null) {
			observableList = FXCollections.observableArrayList(users);
			ListView.setItems(observableList);
		}
	}

	/**
	 * This method adds a new user
	 * @throws FileNotFoundException
	 */
	public void add() throws FileNotFoundException {
		String s = name.getText();
		boolean b = false;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).get_name().equals(s)) {
				b = true;
			}
		}
		if (s.equals("")) {
			popup("PLEASE ENTER NAME", "ERROR");
		} else if (b) {
			popup("This user exists", "ERROR");
		} else {
			User temp = new User(s);
			users.add(temp);
			ListView.getItems().add(temp);
			ListView.refresh();
			saveData();
		}
		name.clear();
	}

	/**
	 * This method deletes the selected user
	 */
	public void delete() {

		User s = ListView.getSelectionModel().getSelectedItem();

		if (s.get_name().equals("Stock")) {
			popup("Can not delete Stock", "ERROR");
		} else {
			users.remove(s);
			ListView.getItems().remove(s);
			ListView.refresh();
			saveData();
		}

	}

	/**
	 * This method displays an error 
	 * @param message error message
	 * @param title type of error
	 */
	public void popup(String message, String title) {
		if (title.equals("ERROR")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(message);
			alert.setTitle(title);

			alert.showAndWait();

		}
	}

	/**
	 * This method calls the method that returns the user to the home page
	 * @param event
	 * @throws IOException
	 */
	public void home(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		loadWindow("/photos/view/login.fxml", "Login Page", event);
	}

	/**
	 * This method loads the page the user has prompted to go to and displays it
	 * @param location which page to go to
	 * @param title what is the title of the page
	 * @param event
	 * @throws IOException
	 */
	private void loadWindow(String location, String title, ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
		Parent parent = (Parent) loader.load();
		loginController controller = loader.<loginController>getController();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		controller.initialize(users);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * This method saves all user data
	 */
	private void saveData() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("data/data.dat");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			objectOutputStream.writeObject(new ArrayList<>(Arrays.asList(ListView.getItems().toArray())));
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
