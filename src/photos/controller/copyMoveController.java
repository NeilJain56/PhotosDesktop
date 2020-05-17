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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import photos.model.Album;
import photos.model.Photo;
import photos.model.User;


/**
 * This class controls the copy and move functionality
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class copyMoveController {

	@FXML
	ListView<Photo> ListViewFrom;
	@FXML
	ListView<Album> ListViewTo;

	ArrayList<User> users;
	ObservableList<Photo> photoList;
	ObservableList<Album> albumList;
	int user_code;
	int album_code;
	Photo p;

	/**
	 * This method initializes the copy move controller with any data it will need
	 * @param users list of all users
	 * @param user_code which specific user is being used
	 * @param album_code which specific album in the user is being referenced
	 * @param p which photo has been selected to be moved
	 */
	public void initialize(ArrayList<User> users, int user_code, int album_code, Photo p) {
		this.users = users;
		this.user_code = user_code;
		this.album_code = album_code;

		ArrayList<Photo> temp = new ArrayList<Photo>();
		temp.add(p);
		this.p = p;
		photoList = FXCollections.observableArrayList(temp);
		ListViewFrom.setItems(photoList);

		albumList = FXCollections.observableArrayList(users.get(user_code).getAlbumList());
		ListViewTo.setItems(albumList);
	}

	/**
	 * Copies a photo from one album to another
	 * @param event
	 * @throws IOException
	 */
	public void copy(ActionEvent event) throws IOException {
		Album a = ListViewTo.getSelectionModel().getSelectedItem();

		if (a == null) {
			popup("Please select an Album", "ERROR");
		} else {
			int toCode = 0;
			for (int i = 0; i < users.get(user_code).getAlbumList().size(); i++) {
				if (users.get(user_code).getAlbumList().get(i).equals(a)) {
					toCode = i;
				}
			}
			boolean b = true;
			for (int i = 0; i < a.get_photoList().size(); i++) {
				if (a.get_photoList().get(i).getSerialImage().equals(p.getSerialImage())) {
					b = false;
				}
			}
			if (!b) {
				popup("Photo exists in album being copied to", "ERROR");
			} else {
				users.get(user_code).getAlbumList().get(toCode).add_photo(p);
				saveData();
				LoadWindow("/photos/view/OpenAlbum.fxml", "Open Album", event);

			}

		}
	}

	/**
	 * Moves a photo from one album to another
	 * @param event
	 * @throws IOException
	 */
	public void move(ActionEvent event) throws IOException {
		Album a = ListViewTo.getSelectionModel().getSelectedItem();

		if (a == null) {
			popup("Please select an Album", "ERROR");
		} else {
			int toCode = 0;
			for (int i = 0; i < users.get(user_code).getAlbumList().size(); i++) {
				if (users.get(user_code).getAlbumList().get(i).equals(a)) {
					toCode = i;
				}
			}
			boolean b = true;
			for (int i = 0; i < a.get_photoList().size(); i++) {
				if (a.get_photoList().get(i).getSerialImage().equals(p.getSerialImage())) {
					b = false;
				}
			}
			if (!b) {
				popup("Photo exists in album being moved to", "ERROR");
			} else {
				users.get(user_code).getAlbumList().get(toCode).add_photo(p);
				users.get(user_code).getAlbumList().get(album_code).get_photoList().remove(p);
				saveData();
				LoadWindow("/photos/view/OpenAlbum.fxml", "Open Album", event);

			}

		}

	}

	/**
	 * Displays an error message
	 * @param message What the error is
	 * @param title what type of error
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
	 * Calls the FXML Loader with the correct parameters to load the page the user has prompted to go to
	 * @param event
	 * @throws IOException
	 */
	public void home(ActionEvent event) throws IOException {
		LoadWindow("/photos/view/OpenAlbum.fxml", "Open Album", event);
	}

	/**
	 * Loads the correct fxml based on input
	 * @param location which fxml to open
	 * @param title title of the fxml opening
	 * @param event
	 * @throws IOException
	 */
	private void LoadWindow(String location, String title, ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
		Parent parent = (Parent) loader.load();
		openAlbumController controller = loader.<openAlbumController>getController();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		controller.initialize(users, user_code, users.get(user_code).getAlbumList().get(album_code));
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

			objectOutputStream.writeObject(users);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
