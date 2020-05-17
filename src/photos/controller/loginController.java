package photos.controller;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import photos.model.User;

/**
 * This class controls the login functionality
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class loginController {
	
	@FXML
	TextField login;
	@FXML
	Button loginButton;
	
	ArrayList<User> users;
	
	
	/**
	 * This method initializes all needed data for this class
	 * @param users list of users
	 */
	public void initialize(ArrayList<User> users) {
		this.users = users;
		login.setPromptText("Enter Username");

	}
	
	/**
	 * This is where the login is controlled deciding what page to load
	 * @param event
	 * @throws IOException
	 */
	public void login(ActionEvent event) throws IOException {

		String s = login.getText();


		boolean user = false;
		int user_code = 0;

		try {
			FileInputStream fileInputStream = new FileInputStream("data/data.dat");

			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			users = (ArrayList<User>) objectInputStream.readObject();

			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).get_name().equals(s)) {
					user = true;
					user_code = i;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Catch");
		}
		if (s.equals("admin")) {
			((Node) event.getSource()).getScene().getWindow().hide();
			LoadWindow("/photos/view/adminPage.fxml", "adminPage", true, user_code, event);
		} else if (user) {
			((Node) event.getSource()).getScene().getWindow().hide();
			LoadWindow("/photos/view/UserLandingPage.fxml", "userPage", false, user_code, event);
		} else {
			popup("PLEASE ENTER VALID NAME", "ERROR");
		}

	}

	/**
	 * Pops up error message
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
	 * This loads the next window needed to be accessed based on the user input
	 * 
	 * @param location what new fxml file will be opened
	 * @param title title of new fxml window
	 * @throws IOException
	 */
	private void LoadWindow(String location, String title, boolean admin, int user_code, ActionEvent event)
			throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
		Parent parent = (Parent) loader.load();
		if (admin) {
			adminPageController controller = loader.<adminPageController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.initialize(users);
			stage.setScene(scene);
			stage.show();
		} else {
			userLandingPageController controller = loader.<userLandingPageController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.initialize(users, user_code);
			stage.setScene(scene);
			stage.show();
		}

	}
	
	/**
	 * Saves all user data
	 */
	private void saveData() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("data/data.dat");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			objectOutputStream.writeObject(new ArrayList<>(Arrays.asList(users)));
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
