package photos.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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
import javafx.stage.Stage;
import photos.model.Album;

import photos.model.User;

/**
 * This class controls the user landing page functionality
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class userLandingPageController {

	@FXML
	TextField create;
	@FXML
	ListView<Album> ListView;
	@FXML
	TextField rename;

	ArrayList<User> users;
	ArrayList<Album> albums = new ArrayList<>();
	ObservableList<Album> observableList;
	int user_code;

	/**
	 * Initializes all data needed in this class
	 * @param user list of users
	 * @param user_code specific user which has been selected
	 */
	public void initialize(ArrayList<User> user, int user_code) {
		this.user_code = user_code;
		this.users = user;
		if (user.get(user_code).getAlbumList().size() != 0) {
			observableList = FXCollections.observableArrayList(user.get(user_code).getAlbumList());
			ListView.setItems(observableList);
		}

	}

	/**
	 * Adds album to user
	 * @throws IOException
	 */
	public void add() throws IOException {

		boolean b = false;
		for (int i = 0; i < users.get(user_code).getAlbumList().size(); i++) {
			if (users.get(user_code).getAlbumList().get(i).get_albumName().equals(create.getText())) {
				b = true;
			}
		}

		if (create.getText().equals("")) {
			popup("PLEASE ENTER ALBUM NAME", "ERROR");
		} else if (b) {
			popup("Album already exists", "ERROR");
		} else {

			Album temp = new Album(create.getText());
			ListView.getItems().add(temp);
			users.get(user_code).add_album(temp);
			ListView.refresh();
			saveData();
		}
		create.clear();
	}

	/**
	 * Deletes selected album
	 */
	public void delete() {
		Album a = ListView.getSelectionModel().getSelectedItem();
		if(a == null) {
			popup("Select a valid image to delete", "ERROR");
		}
		else if (a.get_albumName().equals("Stock")) {
			popup("Can not delete Stock Album", "ERROR");
		} else {
			ListView.getItems().remove(a);

			users.get(user_code).delete_album(a);
			ListView.refresh();
			saveData();
		}

	}

	/**
	 * Renames selected album
	 */
	public void rename() {
		Album a = ListView.getSelectionModel().getSelectedItem();
		if(a == null) {
			popup("Please choose a valid album", "ERROR");
		}
		else if (rename.getText().equals("")) {
			popup("PLEASE ENTER ALBUM NAME", "ERROR");
		} else if (a.get_albumName().equals("Stock")) {
			popup("Can not rename Stock", "ERROR");
		} else {
			String s = rename.getText();
			a.setName(s);
			ListView.refresh();
			saveData();
		}
		rename.clear();
	}

	/**
	 * Displays possible errors
	 * @param message error message
	 * @param title type of error
	 */
	public void popup(String message, String title) {
		if (title.equals("ERROR")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(message);
			alert.setTitle(title);

			alert.showAndWait();

		}
	}

	/**
	 * Loads open album based on selected album
	 * @param event
	 * @throws IOException
	 */
	public void open(ActionEvent event) throws IOException {
		if (ListView.getSelectionModel().getSelectedItem() == null) {
			popup("Please select album", "ERROR");
		} else {
			((Node) event.getSource()).getScene().getWindow().hide();
			loadWindow("/photos/view/OpenAlbum.fxml", "Login Page", 2, event);
		}
	}

	/**
	 * Calls login page to be displayed
	 * @param event
	 * @throws IOException
	 */
	public void logout(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		loadWindow("/photos/view/login.fxml", "Login Page", 0, event);
	}

	/**
	 * Calls the search page to be displayed
	 * @param event
	 * @throws IOException
	 */
	public void search(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		loadWindow("/photos/view/Search.fxml", "Search Page", 1, event);
	}

	/**
	 * Loads fxml based on input parameters
	 * @param location which fxml should be loaded
	 * @param title Title of the new page
	 * @param i which method called this method, used for which controllers initialize to call
	 * @param event
	 * @throws IOException
	 */
	private void loadWindow(String location, String title, int i, ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
		Parent parent = (Parent) loader.load();

		if (i == 0) {
			loginController controller = loader.<loginController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.initialize(users);
			stage.setScene(scene);
			stage.show();
		}
		if (i == 1) {
			searchController controller = loader.<searchController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.initialize(users, user_code);
			stage.setScene(scene);
			stage.show();
		}
		if (i == 2) {
			openAlbumController controller = loader.<openAlbumController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Album a = ListView.getSelectionModel().getSelectedItem();
			controller.initialize(users, user_code, a);
			stage.setScene(scene);
			stage.show();
		}
	}

	/**
	 * Saves user data
	 */
	private void saveData() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("data/data.dat");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			objectOutputStream.writeObject(users);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
