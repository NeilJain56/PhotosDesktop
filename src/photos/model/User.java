package photos.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the User object
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class User implements Serializable {

	private static final long serialVersionUID = -8004578613527719979L;
	String username;
	ArrayList<Album> album_list = new ArrayList<Album>();
	ArrayList<String> tagList = new ArrayList<String>();

	/**
	 * Initialize user object
	 * @param name name of user
	 */
	public User(String name) {
		this.username = name;
		this.tagList.add("Location");
		this.tagList.add("Person");
	}

	/**
	 * Return name of user
	 * @return String name
	 */
	public String get_name() {
		return this.username;
	}

	/**
	 * Return album list of user
	 * @return Arraylist of Album
	 */
	public ArrayList<Album> getAlbumList() {
		return this.album_list;
	}

	/**
	 * Set album list of user
	 * @param album_list list of albums
	 */
	public void setAlbum_list(ArrayList<Album> album_list) {
		this.album_list = album_list;
	}

	/**
	 * Add album to user album list
	 * @param x album to be added
	 */
	public void add_album(Album x) {
		album_list.add(x);
	}

	/**
	 * Delete album from album list 
	 * @param x album to be deleted
	 */
	public void delete_album(Album x) {
		album_list.remove(x);
	}

	/**
	 * Add tag name to tag list
	 * @param s tag name
	 */
	public void add_tag(String s) {
		this.tagList.add(s);
	}

	/**
	 * Return tag list
	 * @return ArrayList of Strings
	 */
	public ArrayList<String> get_tagList() {
		return this.tagList;
	}

	/**
	 * String of user
	 */
	public String toString() {
		return this.username;
	}

}
