package data;

/**
 * The Book interface represents a single book. Each book has an author, title,
 * and description. A book can have 0 or more tags associated with it, each tag
 * is weighted by adding and removing tags.
 *
 * @author Andrew Alm
 */
public interface Book {

	/**
	 * The weight of the given tag is increased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag.
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int tag(String tag) throws IllegalArgumentException;

	/**
	 * The weight of the given tag is decreased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int untag(String tag) throws IllegalArgumentException;

	/**
	 * Gets the weight of a given tag. If the tag has never been used 
	 * with this VirtualBook then the weight will be zero. (Note: A 
	 * return value of zero does not necessicarily indicate that a tag
	 * has never been used with the book.)
	 *
	 * @param tag the name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int weight(String tag) throws IllegalArgumentException;

	/**
	 * Gets the author of the book.
	 *
	 * @return string containing the author's name
	 */
	public String getAuthor();

	/**
	 * Sets the author of the book to the given value.
	 *
	 * @param author string containing the author's name
	 *
	 * @return the previous value of author
	 *
	 * @throws NullPointerExceptoin if the author is null
	 */
	public String setAuthor(String author) throws IllegalArgumentException;

	/**
	 * Gets the title of the book.
	 *
	 * @return string containing the title of the book
	 */
	public String getTitle();

	/**
	 * Sets the title of the book to the given value.
	 *
	 * @param title string containing the title of the book
	 *
	 * @return the previous value of title
	 *
	 * @throws NullPointerExceptoin if the title is null
	 */
	public String setTitle(String title) throws IllegalArgumentException;

}

