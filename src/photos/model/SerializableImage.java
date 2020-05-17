package photos.model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * This is the Serializable Image object
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class SerializableImage implements Serializable {

	private static final long serialVersionUID = -5064722436023597087L;
	int width;
	int height;
	int[][] pixels;

	/**
	 * Constructor 
	 */
	public SerializableImage() {
	}

	/**
	 * Sets Image
	 * @param image image to be set
	 */
	public void setImage(Image image) {
		width = ((int) image.getWidth());
		height = ((int) image.getHeight());
		pixels = new int[width][height];

		PixelReader r = image.getPixelReader();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				pixels[i][j] = r.getArgb(i, j);
	}

	/**
	 * Returns Image
	 * @return Image
	 */
	public Image getImage() {
		WritableImage image = new WritableImage(width, height);

		PixelWriter w = image.getPixelWriter();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				w.setArgb(i, j, pixels[i][j]);

		return image;
	}

	/**
	 * Width accessor
	 * @return width int
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Height accessor
	 * @return height int
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 2D Pixels array accessor
	 * @return pixels 2D int array
	 */
	public int[][] getPixels() {
		return pixels;
	}

	/**
	 * Check if two images are equal
	 * @param si The serializable image to be checked
	 * @return true if they're equal, else false
	 */
	public boolean equals(SerializableImage si) {
		if (width != si.getWidth())
			return false;
		if (height != si.getHeight())
			return false;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (pixels[i][j] != si.getPixels()[i][j])
					return false;
		return true;
	}

}
