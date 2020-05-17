package photos.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the album object
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class Album implements Serializable {
	String album_name;
	ArrayList<Photo> photo_list = new ArrayList<Photo>();
	private static final long serialVersionUID = 2031517614397169889L;

	/**
	 * Constructor that initializes the album object
	 * @param album_name name of the album
	 */
	public Album(String album_name) {
		this.album_name = album_name;
	}
	
	/**
	 * Sets the name of the album
	 * @param s name
	 */
	public void setName(String s) {
		this.album_name = s;
	}

	/**
	 * Returns name of the album
	 * @return name of the album
	 */
	public String get_albumName() {
		return this.album_name;
	}

	/**
	 * Adds photo to album
	 * @param photo photo to be added
	 */
	public void add_photo(Photo photo) {
		photo_list.add(photo);
	}

	/**
	 * Removes photo from album
	 * @param photo photo to be removed
	 */
	public void delete_photo(Photo photo) {
		photo_list.remove(photo);
	}

	/**
	 * Returns the list of all photos
	 * @return Arraylist of photos
	 */
	public ArrayList<Photo> get_photoList() {
		return this.photo_list;
	}

	/**
	 * String version of album
	 */
	public String toString() {
		if (photo_list.size() == 0) {
			return "ALBUM: " + this.album_name + ", # OF PHOTOS: " + photo_list.size();
		}
		if (photo_list.size() == 1) {
			return "ALBUM: " + this.album_name + ", # OF PHOTOS: " + photo_list.size() + ", "
					+ photo_list.get(0).getDate();
		} else {
			Photo oldest = photo_list.get(0);
			Photo newest = photo_list.get(0);
			String first = photo_list.get(0).getDate();
			String second = photo_list.get(0).getDate();
			for (int i = 0; i < photo_list.size(); i++) {
				//oldest check
				if (photo_list.get(i).getDateObject().before(oldest.getDateObject())) {
					oldest = photo_list.get(i);
					first = photo_list.get(i).getDate();
				}
				//newest check
				if (photo_list.get(i).getDateObject().after(newest.getDateObject())) {
					newest = photo_list.get(i);
					second = photo_list.get(i).getDate();
				}
			}
			return "ALBUM: " + this.album_name + ", # OF PHOTOS: " + photo_list.size() + ", " + first + " | " + second;
		}
	}
}
