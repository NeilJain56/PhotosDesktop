package photos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photos.model.Album;
import photos.model.Photo;
import photos.model.Tag;
import photos.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class controls the search functionality
 * @author nj243
 * @author rp930
 * @version 1.0
 */

public class searchController {
	
    @FXML TextField start;
    @FXML TextField end;
    @FXML Button dateSearch;
    @FXML TextField tag1;
    @FXML TextField tag2;
    @FXML ListView ListView;
    @FXML TextField addAlbum;
    int user_code;
    ArrayList<User> users;

    Tag one;
    Tag two;


    /**
     * Initializes all data needed to run this page
     * @param user list of users
     * @param user_code which specific user has been chosen
     */
    public void initialize(ArrayList<User> user, int user_code){
        this.user_code = user_code;
        this.users = user;
        start.setPromptText("yyyy/MM/dd");
        end.setPromptText("yyyy/MM/dd");
    }

    /**
     * Searches by date
     * @throws ParseException
     */
    public void dateSearch() throws ParseException {
        String st = start.getText().trim();
        String en = end.getText().trim();
        if (st.length() != 10 || en.length()!=10){
            popup("Please enter date in format \"yyyy/MM/dd\"", "ERROR");
            return;
        }
        if (!(checkDates(st) && checkDates(en))) {
            return;
        }
        st += " 00:00:00";
        en += " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(st);
        long smillis = date.getTime();
        date = sdf.parse(en);
        long emillis = date.getTime();
        if (smillis > emillis){
            popup("Please make sure start dates come before end dates.", "ERROR");
            return;
        }

        ArrayList<Photo> photos = new ArrayList<>();
        for (Album a: users.get(user_code).getAlbumList()){
            for (Photo p: a.get_photoList()){
                if (p.getCalendar().getTimeInMillis() >= smillis && p.getCalendar().getTimeInMillis() <= emillis){
                    //System.out.println(p.getCalendar().getTimeInMillis());
                    photos.add(p);
                }
            }
        }

        setImageView(photos);
    }


    /**
     * check if dates in right format
     * @param str
     * @return boolean
     */
    public boolean checkDates(String str){
        for (int i = 0; i < str.length(); i++){
            if (i==4 || i==7){
                if (str.charAt(i) != '/') {
                    popup("Please enter date in format \"yyyy/MM/dd\"", "ERROR");
                    return false;
                }
            } else {
                if (!(Character.getNumericValue(str.charAt(i)) >= 0 && Character.getNumericValue(str.charAt(i)) <= 9)){
                    popup("Please enter date in format \"yyyy/MM/dd\"", "ERROR");
                    return false;
                }
            }
        }

        if (!(Integer.parseInt(str.substring(5,7)) > 0 && Integer.parseInt(str.substring(5,7)) <= 12)){
            popup("Please enter VALID dates in format \"yyyy/MM/dd\"", "ERROR");
            return false;
        }

        if (Integer.parseInt(str.substring(5,7)) == 2){
            if (Integer.parseInt(str.substring(8)) > 29){
                popup("Please enter VALID dates in format \"yyyy/MM/dd\"", "ERROR");
                return false;
            }
            if (Integer.parseInt(str.substring(8)) == 29){
                if (Integer.parseInt(str.substring(0,4))%4!=0){
                    popup("Please enter VALID dates in format \"yyyy/MM/dd\"", "ERROR");
                    return false;
                }
            }
        }

        if (!(Integer.parseInt(str.substring(8)) > 0 && Integer.parseInt(str.substring(8)) <= 31)){
            popup("Please enter VALID dates in format \"yyyy/MM/dd\"", "ERROR");
            return false;
        }

        if (Integer.parseInt(str.substring(5,7)) % 2 == 0 && Integer.parseInt(str.substring(5,7)) <= 6){
            if (Integer.parseInt(str.substring(8)) == 31){
                popup("Please enter VALID dates in format \"yyyy/MM/dd\"", "ERROR");
                return false;
            }
        }

        if (Integer.parseInt(str.substring(5,7)) % 2 == 1 && Integer.parseInt(str.substring(5,7)) <= 11 && Integer.parseInt(str.substring(5,7)) >= 8){
            if (Integer.parseInt(str.substring(8)) == 31){
                popup("Please enter VALID dates in format \"yyyy/MM/dd\"", "ERROR");
                return false;
            }
        }


        return true;
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void logout(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        loadWindow("/photos/view/userLandingPage.fxml", "userPage", 0, event);
    }

    /**
     *
     * @param message
     * @param title
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
     *
     * @param location
     * @param title
     * @param i
     * @param event
     * @throws IOException
     */
    private void loadWindow(String location, String title, int i, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        Parent parent = (Parent) loader.load();
        Scene scene = new Scene(parent);
        Stage stage;
        userLandingPageController controller = loader.<userLandingPageController>getController();
        controller.initialize(users, user_code);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /**
     * sets imageview
     * @param p
     */
    public void setImageView(ArrayList<Photo> p) {
        ArrayList<String> string = new ArrayList<>();
        for (Photo i : p)
            string.add(i.time.toString());


        ObservableList<String> items = FXCollections.observableArrayList(string);
        ListView.setItems(items);

        ListView.setCellFactory(param -> new ListCell<String>() {
            private javafx.scene.image.ImageView ImageView = new ImageView();

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
     * searches by one tag
     */
    public void tagSearch(){
        if (tag1.getText().trim().equals("")){
            popup("Please enter a tag above.","ERROR");
            return;
        }

        if (!tag2.getText().trim().equals("")){
            popup("Please enter only one tag.","ERROR");
            return;
        }

        String fullTag = tag1.getText().toLowerCase().trim();
        String[] arr = fullTag.split("=");
        if (arr.length != 2){
            popup("Please enter a tag as newValue=newValue", "ERROR");
            return;
        }
        Tag tag = new Tag(arr[0], arr[1]);
        ArrayList<Photo> photos = new ArrayList<>();
        for (Album a: users.get(user_code).getAlbumList()){
            for (Photo p: a.get_photoList()){
                for (Tag t:p.getTagList()){
                    if (t.equals(tag)){
                        photos.add(p);
                    }
                }
            }
        }

        setImageView(photos);
    }

    /**
     * Searches disjunctive search
     */
    public void disSearch(){
        if (tag1.getText().trim().equals("")){
            popup("Please enter two tags above.","ERROR");
            return;
        }
        if (tag2.getText().trim().equals("")){
            popup("Please enter two tags above.","ERROR");
            return;
        }
        String fullTag1 = tag1.getText().toLowerCase().trim();
        String[] arr1 = fullTag1.split("=");
        String fullTag2 = tag2.getText().toLowerCase().trim();
        String[] arr2 = fullTag2.split("=");
        if (arr1.length != 2 || arr2.length != 2){
            popup("Please enter a tag as newValue=newValue", "ERROR");
            return;
        }
        Tag tag1 = new Tag(arr1[0], arr1[1]);
        Tag tag2 = new Tag(arr2[0], arr2[1]);
        ArrayList<Photo> photos = new ArrayList<>();
        for (Album a: users.get(user_code).getAlbumList()){
            for (Photo p: a.get_photoList()){
                boolean works = false;
                for (Tag t:p.getTagList()){
                    if (t.equals(tag1) || t.equals(tag2)){
                        one = tag1;
                        two = tag2;
                        works = true;
                    }
                }
                if (works){
                    photos.add(p);
                }
            }
        }

        setImageView(photos);
    }

    /**
     * searches conjunctive search
     */
    public void conSearch(){
        if (tag1.getText().trim().equals("")){
            popup("Please enter two tags above.","ERROR");
            return;
        }
        if (tag2.getText().trim().equals("")){
            popup("Please enter two tags above.","ERROR");
            return;
        }
        String fullTag1 = tag1.getText().toLowerCase().trim();
        String[] arr1 = fullTag1.split("=");
        String fullTag2 = tag2.getText().toLowerCase().trim();
        String[] arr2 = fullTag2.split("=");
        if (arr1.length != 2 || arr2.length != 2){
            popup("Please enter a tag as newValue=newValue", "ERROR");
            return;
        }
        Tag tag1 = new Tag(arr1[0], arr1[1]);
        Tag tag2 = new Tag(arr2[0], arr2[1]);
        ArrayList<Photo> photos = new ArrayList<>();
        for (Album a: users.get(user_code).getAlbumList()){
            for (Photo p: a.get_photoList()){
                int x = 0;
                for (Tag t:p.getTagList()){
                    one = tag1;
                    two = tag2;
                    if (t.equals(tag1)){
                        x++;
                    }
                    if (t.equals(tag2)){
                        x++;
                    }
                }
                if (x==2){
                    photos.add(p);
                }
            }
        }

        setImageView(photos);
    }


    /**
     * saves search as an album
     */
    public void saveAsAlbum(){
        ObservableList<String> alb = ListView.getItems();
        boolean b = false;
        for(int i = 0; i < users.get(user_code).getAlbumList().size(); i++) {
        	if(users.get(user_code).getAlbumList().get(i).get_albumName().equals(addAlbum.getText())) {
        		b = true;
        	}
        }
        if (alb.size() == 0){
            popup("Must have at least 1 picture to save in album","ERROR");
            return;
        }
        
        else if(addAlbum.getText().equals("")) {
        	popup("Please enter valid album name", "ERROR");
        }
        else if(b) {
        	popup("Album name already exists", "ERROR");
        }
        else {
        	
        	
        	Album temp = new Album(addAlbum.getText());
        	
        	for(String s: alb) {
        		for(Album a: users.get(user_code).getAlbumList()) {
        			for(Photo p: a.get_photoList()) {
        				if(p.time.toString().equals(s)) {
        					System.out.println(a);
        					temp.add_photo(p);
        				}
        			}
        		}
        	}
        	users.get(user_code).add_album(temp);
        	addAlbum.clear();
        	saveData();
        	
    }
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
