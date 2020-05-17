package photos.model;

import java.io.Serializable;

/**
 * This is the Tag object
 * @author nj243
 * @author rp930
 * @version 1.0
 */
public class Tag implements Serializable {

	private static final long serialVersionUID = 6245434283298309880L;
	String name;
	String value;

	/**
	 * Initialize Tag object
	 * @param name name of tag
	 * @param value value of tag
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;

	}

	/**
	 * Returns name of tag
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return value of Tag
	 * @return String value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * String of tag
	 */
	public String toString() {
		return this.name + "= " + this.value;
	}


	/**
	 * checks tag.equals
	 * @param o
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tag tag = (Tag) o;
		return name.toLowerCase().trim().equals(tag.name.trim().toLowerCase()) &&
				value.toLowerCase().trim().equals(tag.value.trim().toLowerCase());
	}
}
