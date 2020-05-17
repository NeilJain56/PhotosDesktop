package photos.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.image.Image;

/**
 * This is the Photo object
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class Photo implements Serializable {

	private static final long serialVersionUID = -7480026127255450454L;
	SerializableImage photo;
	String caption;
	public Calendar time;
	public Date datetime;
	ArrayList<Tag> tags = new ArrayList<>();
	//Image i;

	/**
	 * Constructor that initializes a photo object
	 */
	public Photo() {
		caption = "";
		time = Calendar.getInstance();
		time.set(Calendar.MILLISECOND, 0);
		photo = new SerializableImage();
	}

	/**
	 * Constructor that initializes a photo object given an image
	 * @param i image 
	 */
	public Photo(Image i) {
		this();
		photo.setImage(i);
	}

	/**
	 * Adds a tag to an image
	 * @param type tag type
	 * @param value tag value
	 */
	public void addTag(String type, String value) {
		tags.add(new Tag(type, value));
	}
	
	/**
	 * Sets time
	 * @param x
	 */
	public void setTime(long x) {
		this.datetime = new Date(x);
		/*String temp = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(x));
		System.out.println(temp);*/
	}

	/**
	 * Remove a tag from the tag list
	 * @param index which tag to delete
	 */
	public void removeTag(int index) {
		tags.remove(index);
	}

	/**
	 * Return a tag given index
	 * @param index which tag
	 * @return Tag
	 */
	public Tag getTag(int index) {
		return tags.get(index);
	}

	/**
	 * Set caption of the image 
	 * @param caption caption string
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Return caption of the image
	 * @return String caption
	 */
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * Get the date of an image
	 * @return String date
	 */
	public String getDate() {
		//String[] str = time.getTime().toString().split("\\s+");
		//return str[0] + " " + str[1] + " " + str[2] + ", " + str[5];
		return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(this.datetime);
	}
	
	/**
	 * Returns date
	 */
	public Date getDateObject() {
		return this.datetime;
	}

	/**
	 * Get Calendar time of image
	 * @return Calendar
	 */
	public Calendar getCalendar() {
		time.setTime(datetime);
		return this.time;
	}
	
	/**
	 * Return image in photo class
	 * @return Image
	 */
	public Image getImage() {
		return photo.getImage();
	}

	/**
	 * Return Serializable Image of photo object
	 * @return SerializableImage
	 */
	public SerializableImage getSerialImage() {
		return photo;
	}

	/**
	 * Return list of tags
	 * @return Arraylist of tags
	 */
	public ArrayList<Tag> getTagList() {
		return tags;
	}

	/**
	 * Return String
	 * @return String
	 */
	public String toString() {
		if (!this.caption.equals("")) {
			return this.caption;
		}
		return this.getDate();
	}

}
