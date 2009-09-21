package data;

/* file: Book.java
 * date: 17-Sep-09
 *
 * hist:
 *  17-Sep-09: file created
 */

/**
 * The Book public interface represents a single book. Each book has an author, title,
 * and description. A book can have 0 or more tags associated with it, each tag
 * is weighted by adding and removing tags.
 *
 * @author Andrew Alm
 */
public interface Book {

	/**
	 * Increases the given Tag's weight.
	 *
	 * @param tag the Tag to recompute the weight of
	 *
	 * @return the newley computed weight
	 *
	 * @throws NullPointerException if tag is null
	 */
	int addTag(Tag tag) throws NullPointerException;

	/**
	 * Decreases the given Tag's weight.
	 *
	 * @param tag the Tag to recompute the weight of
	 *
	 * @return the newely computed weight
	 *
	 * @throws NullPointerException if tag is null
	 */
	int removeTag(Tag tag) throws NullPointerException;

	/**
	 * Gets the weight of the given Tag on the book, a Tag which has not
	 * used will have a weight of zero.
	 *
	 * @param tag the Tag to get the weight of
	 *
	 * @return the weight of the given Tag.
	 *
	 * @throws NullPointerException if tag is null
	 */
	int getWeight(Tag tag) throws NullPointerException;

	/**
	 * Set the author of the book, returning the old value.
	 *
	 * @param author the author's name
	 *
	 * @return the previous value of author
	 *
	 * @throws NullPointerException if author is null
	 */
	String setAuthor(String author) throws NullPointerException;

	/**
	 * Set the title of the Book, returning the old value.
	 *
	 * @param title the book's title
	 *
	 * @return the previous value of title
	 *
	 * @throws NullPointerException if title is null
	 */
	String setTitle(String title) throws NullPointerException;

	/**
	 * Set the description of the Book, returning the old value.
	 *
	 * @param description a short description of the book
	 *
	 * @return the previous value of description
	 *
	 * @throws NullPointerException if description is null
	 */
	String setDescription(String description) throws NullPointerException;

	/**
	 * Get the author of the Book.
	 *
	 * @return a String containing the author of the book.
	 */
	String getAuthor();

	/**
	 * Get the title of the Book.
	 *
	 * @return a String containing the title of the book.
	 */
	String getTitle();

	/**
	 * Get the description of the Book.
	 *
	 * @return a String containing a short desciption of the book
	 */
	String getDescription();

}

