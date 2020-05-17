package photos.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photos.model.Album;
import photos.model.Photo;

import photos.model.Tag;
import photos.model.User;

/**
 * This class controls the open album functionality
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class openAlbumController {

	@FXML
	ImageView imageView;
	@FXML
	TextField textfield;
	@FXML
	ListView<String> ListView;
	@FXML
	ListView<Tag> TagListView;
	@FXML
	TextField tagAdd;
	@FXML
	Label dateLabel;
	@FXML
	ComboBox<String> comboBox;

	ObservableList<Tag> tagobservableList;
	ObservableList<String> tagValueList;
	ArrayList<User> users;
	ArrayList<Photo> p;
	Album a;
	int user_code;
	int album_code;
	int photo_code;
	

	/**
	 * Initializes all data needed to run this page
	 * @param user list of users
	 * @param user_code which specific user has been chosen
	 * @param a which album has been opened
	 */
	public void initialize(ArrayList<User> user, int user_code, Album a) {
		this.user_code = user_code;
		this.users = user;
		this.a = a;
		int x = 0;
		for (int i = 0; i < users.get(user_code).getAlbumList().size(); i++) {
			if (users.get(user_code).getAlbumList().get(i).equals(a)) {
				x = i;
				break;
			}
		}
		this.album_code = x;
		// The bottom part creates a listener for whenever a selection is changed
		ListView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			ArrayList<Photo> p = users.get(user_code).getAlbumList().get(album_code).get_photoList();
			for (int j = 0; j < p.size(); j++) {
				if (p.get(j).time.toString().equals(newValue)) {
					Image r = p.get(j).getImage();
					imageView.setImage(r);
					tagList(p.get(j));
					textfield.setText(p.get(j).getCaption());
					dateLabel.setText("Date/Time: " + p.get(j).getDate());
					break;
				}
			}
		});
		if (a.get_photoList().size() > 0) {
			Image testImage;
			testImage = a.get_photoList().get(0).getImage();

			imageView.setImage(testImage);
			p = users.get(user_code).getAlbumList().get(this.album_code).get_photoList();

			Photo ph = a.get_photoList().get(0);
			tagList(ph);
			setImageView();
			ListView.getSelectionModel().select(0);
		}


		for (int i = 0; i < users.get(user_code).get_tagList().size(); i++) {
			//comboBox.autosize();
			comboBox.getItems().add(users.get(user_code).get_tagList().get(i));
		}

	}

	/**
	 * Displays the list of tags for a photo
	 * @param p photo selected
	 */
	public void tagList(Photo p) {
		for (int i = 0; i < this.a.get_photoList().size(); i++) {
			if (this.a.get_photoList().get(i).equals(p))
				this.photo_code = i;
		}

		if (p.getTagList() != null) {

			tagobservableList = FXCollections.observableArrayList(p.getTagList());
			TagListView.setItems(tagobservableList);
		}
	}

	/**
	 * Deletes a selected tag from the list
	 */
	public void tagListdelete() {
		Tag t = TagListView.getSelectionModel().getSelectedItem();
		if (t == null) {
			popup("Enter Pick a Valid Tag Value", "ERROR");
		} else {
			users.get(user_code).getAlbumList().get(album_code).get_photoList().get(this.photo_code).getTagList()
					.remove(t);
			saveData();
			tagList(users.get(user_code).getAlbumList().get(album_code).get_photoList().get(this.photo_code));
		}
	}
	
	

	/**
	 * Adds a tag to the list
	 */
	public void tagListAdd() {

		if (tagAdd.getText().equals("")) {
			popup("Enter Tag Value", "ERROR");
		} else if (tagAdd.getText().split("=").length == 2
				&& users.get(user_code).getAlbumList().get(album_code).get_photoList().size() >= 1) {
			String s = tagAdd.getText();
			String[] stemp = s.split("=");
			Tag temp = new Tag(stemp[0], stemp[1]);
			
			if(users.get(user_code).getAlbumList().get(album_code).get_photoList().get(photo_code).getTagList().contains(temp)) {
				popup("Tag already exists", "ERROR");
			}
			else {
			users.get(user_code).getAlbumList().get(album_code).get_photoList().get(this.photo_code).getTagList()
					.add(temp);
			saveData();
			tagAdd.clear();
			tagList(users.get(user_code).getAlbumList().get(album_code).get_photoList().get(this.photo_code));

			for (int i = 0; i < users.get(user_code).get_tagList().size(); i++) {
				if (users.get(user_code).get_tagList().contains(stemp[0])) {

				} else {
					users.get(user_code).get_tagList().add(stemp[0]);
					setTagList(users.get(user_code).get_tagList());
					saveData();

				}
			}
			
			}
		} else if (comboBox.getValue() != null && tagAdd.getText().split("=").length == 1
				&& users.get(user_code).getAlbumList().get(album_code).get_photoList().size() >= 1) {
			Tag temp = new Tag(comboBox.getValue(), tagAdd.getText());
			
			if(users.get(user_code).getAlbumList().get(album_code).get_photoList().get(photo_code).getTagList().contains(temp)) {
				popup("Tag already exists", "ERROR");
			}else {
			users.get(user_code).getAlbumList().get(album_code).get_photoList().get(this.photo_code).getTagList()
					.add(temp);
			saveData();
			tagAdd.clear();
			tagList(users.get(user_code).getAlbumList().get(album_code).get_photoList().get(this.photo_code));
			}
		} else {
			popup("1) Make sure there is an image where the tag is attempting to be added \n 2) If you with to add a new tag enter in the box newValue=newValue \n 3) If you wish to use the premade tag Value, select the correct tag and give its value in the box",
					"ERROR");
		}

	}

	/**
	 * Adds new tag type to preset tags 
	 * @param s arraylist of all tags
	 */
	public void setTagList(ArrayList<String> s) {
		comboBox.getItems().add(s.get(s.size() - 1));
	}

	/**
	 * Sets the list of photos in the album view
	 */
	public void setImageView() {

		ArrayList<Photo> p = users.get(user_code).getAlbumList().get(this.album_code).get_photoList();
		ArrayList<String> string = new ArrayList<>();
		for (Photo i : p)
			string.add(i.time.toString());

		ObservableList<String> items = FXCollections.observableArrayList(string);
		ListView.setItems(items);

		ListView.setCellFactory(param -> new ListCell<String>() {
			private ImageView ImageView = new ImageView();

			@Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					for (int i = 0; i < p.size(); i++) {

						if (p.get(i).time.toString().equals(name)) {

							Image x = p.get(i).getImage();
							ImageView.setFitHeight(100);
							ImageView.setFitWidth(100);
							ImageView.setImage(x);
							if (p.get(i).getCaption().equals("")) {
								setText("");
							} else {
								setText(p.get(i).getCaption());
							}
							setGraphic(ImageView);
							break;
						}
					}
				}
			}
		});
	}

	/**
	 * Shows next image in manual slideshow
	 */
	public void next() {
		if (ListView.getSelectionModel().getSelectedItems().size() == 0) {
			ListView.getSelectionModel().select(0);
		} else {
			int index = ListView.getSelectionModel().getSelectedIndex();
			ListView.getSelectionModel().select(index + 1);
		}
	}

	/**
	 * Shows previous image in manual slideshow
	 */
	public void prev() {
		if (ListView.getSelectionModel().getSelectedItems().size() == 0) {
			ListView.getSelectionModel().select(0);
		} else {
			int index = ListView.getSelectionModel().getSelectedIndex();
			ListView.getSelectionModel().select(index - 1);
		}
	}

	/**
	 * Uploads an image from the user system
	 * @param event
	 * @throws NullPointerException
	 */
	public void upload(ActionEvent event) throws NullPointerException {
		FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(imageFilter);
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(stage);
		if(file != null) {
		Image image = new Image(file.toURI().toString());
		Photo temp = new Photo(image);

		boolean b = false;
		for (int i = 0; i < users.get(user_code).getAlbumList().get(album_code).get_photoList().size(); i++) {

			if (users.get(user_code).getAlbumList().get(album_code).get_photoList().get(i).getSerialImage()
					.equals(temp.getSerialImage())) {

				b = true;
			}
		}
		if (b) {
			popup("Image exists", "ERROR");
		} else {
			//imageView.setImage(image);
			Photo tempPhoto = new Photo(image);
			tempPhoto.setTime(file.lastModified());
			
			users.get(user_code).getAlbumList().get(album_code).add_photo(tempPhoto);
			saveData();
			TagListView.setItems(null);
			setImageView();
		}
		}

	}

	/**
	 * Deletes selected photo from album
	 */
	public void delete() {

		String s = ListView.getSelectionModel().getSelectedItem();

		ArrayList<Photo> p = users.get(user_code).getAlbumList().get(album_code).get_photoList();
		for (int i = 0; i < p.size(); i++) {

			if (p.get(i).time.toString().equals(s)) {
				users.get(user_code).getAlbumList().get(album_code).get_photoList().remove(i);
			}
		}
		saveData();
		setImageView();
		if (ListView.getItems().size() > 0) {
			ListView.getSelectionModel().select(0);

		} else {
			textfield.setText("");
			dateLabel.setText("Date/Time");
			imageView.setImage(null);
			TagListView.setItems(null);
		}

	}

	/**
	 * Renames or adds caption to selected photo
	 */
	public void addRenameCaption() {
		if (textfield.getText().trim().equals("")) {
			popup("No Caption Given", "ERROR");
		} else {
			String s = ListView.getSelectionModel().getSelectedItem();
			ArrayList<Photo> p = users.get(user_code).getAlbumList().get(album_code).get_photoList();
			for (int i = 0; i < p.size(); i++) {
				if (p.get(i).time.toString().equals(s)) {
					users.get(user_code).getAlbumList().get(album_code).get_photoList().get(i)
							.setCaption(textfield.getText());
				}
			}
			setImageView();
			saveData();
			//textfield.clear();
		}

	}

	/**
	 * Displays possible errors
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
	 * Calls copy move page to be displayed
	 * @param event
	 * @throws IOException
	 */
	public void copyMove(ActionEvent event) throws IOException {

		if (ListView.getSelectionModel().getSelectedItem() == null) {
			popup("Select Image to Copy or Move", "ERROR");

		} else {
			((Node) event.getSource()).getScene().getWindow().hide();
			loadWindow("/photos/view/CopyMovePage.fxml", "Copy Move", 1, event);
		}

	}

	/**
	 * Calls user landing page to be displayed
	 * @param event
	 * @throws IOException
	 */
	public void logout(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		loadWindow("/photos/view/userLandingPage.fxml", "userPage", 0, event);
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
		Scene scene = new Scene(parent);
		Stage stage;
		if (i == 0) {
			userLandingPageController controller = loader.<userLandingPageController>getController();
			controller.initialize(users, user_code);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		} else {

			copyMoveController controller = loader.<copyMoveController>getController();
			Photo temp = null;
			for (int j = 0; j < users.get(user_code).getAlbumList().get(album_code).get_photoList().size(); j++) {
				if (users.get(user_code).getAlbumList().get(album_code).get_photoList().get(j).time.toString()
						.equals(ListView.getSelectionModel().getSelectedItem())) {
					temp = users.get(user_code).getAlbumList().get(album_code).get_photoList().get(j);
				}
			}

			controller.initialize(users, user_code, album_code, temp);
			stage = new Stage();
		}
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * Save all user data
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
